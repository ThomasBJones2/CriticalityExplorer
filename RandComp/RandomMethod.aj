package RandComp;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;

public aspect RandomMethod{

	static ArrayList<Location> locations = new ArrayList<>();

	static ArrayList<Integer> timeCounts = new ArrayList<>();

	double NEAR_DIST = 0.0000000001;
	
	public static void clearAspect(){
		locations = new ArrayList<>();
		timeCounts = new ArrayList<>();
	}

	public static synchronized void registerTimeCount(){
		RunId curId = new RunId(Thread.currentThread().getId());
		curId = Experimenter.getId(curId);
		Location theLocation = getLocation(curId);
		timeCounts.add(theLocation.timeCount);
	}

	public static int getAverageTimeCount(){
		double out = 0;
		for(int i = 0; i < timeCounts.size(); i ++){
			out += ((double) timeCounts.get(i) / (double) timeCounts.size());
		}
		return (int)out;
	}

	public void printThisAspect(){
		RunId curId = new RunId(Thread.currentThread().getId());
		curId = Experimenter.getId(curId);
		Location theLocation = getLocation(curId);

		System.out.println("On thread: " + curId.getThreadId() + ": ");
		System.out.println("The time count is: " + theLocation.timeCount);
		for(DefinedLocation d : theLocation.getDefinedLocations()){
			System.out.println("The time count on method " + d.name + 
				" is " + d.location);
		}
	}

	public void printAllLocations(){
		for(Location location : locations){
			location.print();
		}

		for(Integer integer: timeCounts){
			System.out.println(integer);
		}
	}


	pointcut Randomize(): call(@Randomize * *(..));

	pointcut PrintAspect() : call(void *.printAspect());

	after() : PrintAspect() {
		printAllLocations();
		//printThisAspect();
	}

	public static synchronized void clearLocation(RunId curId){
		Location checkLocation = new Location(curId.getRunName(), curId.getThreadId());
    while(locations.indexOf(checkLocation) != -1){
			locations.remove(locations.indexOf(checkLocation));
		}
	}

	public static synchronized void createLocation(RunId curId){
		Location checkLocation = new Location(curId.getRunName(), curId.getThreadId());
		locations.add(checkLocation);
	}

	public static synchronized Location getLocation(RunId curId){
		Location checkLocation = new Location(curId.getRunName(), curId.getThreadId());
		if(locations.indexOf(checkLocation) >= 0)
			return locations.get(locations.indexOf(checkLocation));
		else
			return checkLocation;
	}

	boolean near(double a, int b){
		return (a + NEAR_DIST >= (double) b) && ((a - NEAR_DIST) <= (double) b);

	}

	boolean forcedError(double methodCount, String methodName, RunId curId){
		return near(methodCount, curId.errorPoint) && 
			curId.errorful && 
			(methodName.equals(curId.methodName) || curId.methodName.equals("All"));
	}

	boolean unForcedError(double methodCount, String methodName, RunId curId){
		return !near(methodCount,curId.errorPoint)
			&& (methodName.equals(curId.methodName) || curId.methodName.equals("All"));
	}

	void updateSingleLocation(Location theLocation, DefinedLocation handle){
		if(theLocation.dLocations.contains(handle)){
			int index = theLocation.dLocations.indexOf(handle);
			theLocation.dLocations.get(index).setLocation(handle.getLocation());
			theLocation.dLocations.get(index).pertinent = true;
		} else {
			DefinedLocation dist = new DefinedLocation(handle);
			dist.pertinent = true;
			theLocation.dLocations.add(dist);
		}
	}

	void incrementSingleLocation(Location theLocation, DefinedLocation handle){
		DefinedLocation dLocation = null;
		if(theLocation.dLocations.contains(handle)){
			dLocation = 
				theLocation.dLocations.get(theLocation.dLocations.indexOf(handle));
		} else {
			dLocation = new DefinedLocation(handle.getName(), 0);
			theLocation.dLocations.add(dLocation);
		}
		dLocation.increment();
		dLocation.pertinent = true;
	}

	void updateLocations(Location theLocation, 
			RunId curId, 
			String methodName, 
			AbstractLocation targetObject){

			theLocation.clearPertinence();

			DefinedLocation handle = new DefinedLocation(methodName);		

			incrementSingleLocation(theLocation, handle);
			ArrayList<DefinedLocation> absLocations = targetObject.getCurrentLocations();
			for(DefinedLocation d : absLocations){
				updateSingleLocation(theLocation, d);
			}

			if(forcedError(theLocation.getDefinedLocationFromName(methodName).location, 
						methodName,
						curId)){

				theLocation.burnIn();
			}
	}

	Object around() : Randomize() {
		Object targetObject = thisJoinPoint.getTarget();    	
		final Object[] args = thisJoinPoint.getArgs();

		RunId curId = new RunId(Thread.currentThread().getId());
		curId = Experimenter.getId(curId);
		if(curId.getExperiment() == true){
			Random rand = curId.getRand();
			Location theLocation = getLocation(curId);
			
			String methodName = thisJoinPointStaticPart.
				getSignature().
				getDeclaringTypeName()
				+ "." + thisJoinPointStaticPart.
				getSignature().
				getName();

			Experimenter.addToFallibleMethods(methodName);

			updateLocations(theLocation, curId, methodName, (AbstractLocation) targetObject);
			//increment time count seperately to account for 0 indexing
			theLocation.timeCount ++;
			
			String shortMethodName = thisJoinPointStaticPart.
				getSignature().
				getName();

			String randMethodName = shortMethodName + "Rand";

			//must account for early increment due to return...
			if((rand.nextDouble() < 0.0 && 
					unForcedError(theLocation.getDefinedLocationFromName(methodName).getLocation() - 1, 
						methodName,
						curId)) || 
					forcedError(theLocation.getDefinedLocationFromName(methodName).getLocation() - 1, 
						methodName, 
						curId)) {
//					unForcedError(theLocation.timeCount - 1, curId)) || 
//					forcedError(theLocation.timeCount - 1, methodName, curId)) {
				return randomizedCall(targetObject, args, randMethodName, rand);
			}
		}


		return proceed();
	}

	Object randomizedCall(Object targetObject, 
		final Object[] args,
		String randMethodName,
		Random rand){
		try {
			Class[] types = new Class[args.length + 1];
			types[0] = Random.class;
			for(int i = 1; i < args.length + 1; i ++){
				types[i] = args[i - 1].getClass();
			}

			Object[] arguments = new Object[args.length + 1];
			arguments[0] = rand;
			for(int i = 1; i < args.length + 1; i ++){
				arguments[i] = args[i - 1];
			}

			Method m = targetObject.getClass().getDeclaredMethod(randMethodName, types);

			return m.invoke(targetObject, arguments);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException " + e);
		} catch (InvocationTargetException e) {
			System.out.println("InvocationTargetException " + e);
		} catch (NoSuchMethodException e) {
			System.out.println("NoSuchMethodException " + e);
		}
		return null;
	}
}
