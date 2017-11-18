package com.criticalityworkbench.randcomphandler;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;
import org.aspectj.lang.JoinPoint;

public aspect RandomMethod{

	static ArrayList<Location> locations = new ArrayList<>();

	static ArrayList<Integer> timeCounts = new ArrayList<>();

	double NEAR_DIST = 0.0000000001;

	public static boolean randomize = true;

	public static EpsilonProbability eProbability = new NullEpsilon();
	public static boolean epsilonTest = false;

  public static boolean use_decompose = false;

  public static boolean useProxyMethod = false;
	public static String proxyMethodName = "";

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

	public static int getMaxTimeCount(){
		double out = 0;
		for(int i = 0; i < timeCounts.size(); i ++){
			if(out < (double) timeCounts.get(i))
				out = (double) timeCounts.get(i);
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

	pointcut anyInputObjectCall(): call(* com.criticalityworkbench.inputobjects..* (..));
	
	pointcut Randomize(): call(@Randomize * *(..));
	
	pointcut Randomize_Decompose(): call(@Randomize_Decompose * *(..));

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
		
		if(curId == null || methodName == null || curId.methodName == null){
			System.out.println("in forced error");
			System.out.println("mathod name: " + methodName + " ");
			System.out.println("curIDMethodName: " + curId.methodName);
		}
		return near(methodCount, curId.errorPoint) && 
			curId.errorful && 
			(methodName.equals(curId.methodName) || curId.methodName.equals("All"));
	}

	boolean unForcedError(double methodCount, String methodName, RunId curId){
		return !near(methodCount,curId.errorPoint)
			&& (methodName.equals(curId.methodName) || curId.methodName.equals("All"));
	}


	void updateLocations(Location theLocation, 
			RunId curId, 
			String methodName, 
			AbstractLocation targetObject){

			//only keep track of locations when *not* running the epsilon test
			theLocation.clearPertinence();

			theLocation.incrementSingleLocation(new DefinedLocation(methodName));
			
			ArrayList<DefinedLocation> absLocations = targetObject.getCurrentLocations();
			for(DefinedLocation d : absLocations){
				theLocation.updateSingleLocation(d);
			}

			if(forcedError(theLocation.getDefinedLocationFromName(methodName).location, 
						methodName,
						curId)){
				theLocation.burnIn();
			}
			if(epsilonTest){
				
				theLocation.clearPertinence();
				DefinedLocation epsilonHandle	= new DefinedLocation(eProbability.getName(), eProbability.getLocation());
				epsilonHandle.pertinent = true;
				theLocation.updateSingleLocation(epsilonHandle);
				theLocation.burnIn();
			}
	
	}

  public static boolean in_debug_termination = false;

  before() : anyInputObjectCall(){
			if(Thread.currentThread().isInterrupted()){
				throw new RuntimeException();
			}
	}


	Object around() : Randomize(){
		if(randomize && !use_decompose){
			Object targetObject = thisJoinPoint.getTarget();    	
			final Object[] args = thisJoinPoint.getArgs();

			RunId curId = new RunId(Thread.currentThread().getId());
			curId = Experimenter.getId(curId);

			if(curId.getExperiment() == true){
				Random rand = curId.getRand();
				Location theLocation = getLocation(curId);

				//This forces a non-sdc error which allows us to clean up execution
				//through 'non-sdc' error methods...
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
				if(
						curId.errorful &&
						(
						 (rand.nextDouble() < eProbability.getProbability(curId.scoreName, 
																																methodName, 
																																theLocation) && 
							(
								(epsilonTest) ||
								unForcedError(
									theLocation.getDefinedLocationFromName(methodName).getLocation() - 1, 
									methodName,
									curId)
							)
						) || (
							!epsilonTest &&	
							forcedError(theLocation.getDefinedLocationFromName(methodName).getLocation() - 1, 
								methodName, 
								curId)
							)
							) 
						){
	//					unForcedError(theLocation.timeCount - 1, curId)) || 
	//					forcedError(theLocation.timeCount - 1, methodName, curId)) {
					theLocation.incrementFailCount();
					return randomizedCall(targetObject, args, randMethodName, rand);
				}
			}
		}

		return proceed();
	}

	Object around() : Randomize_Decompose(){
    if(randomize && useProxyMethod){
			Object targetObject = thisJoinPoint.getTarget();    	
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
				updateLocations(theLocation, curId, methodName, (AbstractLocation) targetObject);
			}
		}
		if(randomize && use_decompose ){
			Object targetObject = thisJoinPoint.getTarget();    	
			final Object[] args = thisJoinPoint.getArgs();

			RunId curId = new RunId(Thread.currentThread().getId());
			curId = Experimenter.getId(curId);

			if(curId.getExperiment() == true){
				Random rand = curId.getRand();
				Location theLocation = getLocation(curId);

				//This forces a non-sdc error which allows us to clean up execution
				//through 'non-sdc' error methods...
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
				if(
						curId.errorful &&
						(
						 (rand.nextDouble() < eProbability.getProbability(curId.scoreName, 
																																methodName, 
																																theLocation) && 
							(
								(epsilonTest) ||
								unForcedError(
									theLocation.getDefinedLocationFromName(methodName).getLocation() - 1, 
									methodName,
									curId)
							)
						) || (
							!epsilonTest &&	
							forcedError(theLocation.getDefinedLocationFromName(methodName).getLocation() - 1, 
								methodName, 
								curId)
							)
							) 
						){
	//					unForcedError(theLocation.timeCount - 1, curId)) || 
	//					forcedError(theLocation.timeCount - 1, methodName, curId)) {
					theLocation.incrementFailCount();
					return randomizedCall(targetObject, args, randMethodName, rand);
				}
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
			System.out.println("RandomizedCall (RandomMethod.aj) use_decompose : " + 
					use_decompose + " IllegalAccessException " + e);
		} catch (InvocationTargetException e) {

			System.out.println("random methodName: " + randMethodName);
			for(Object arg : args){
				System.out.println(arg);
			}
			System.out.println("Done with args");
			System.out.println("targetObject " + targetObject);

			e.printStackTrace();
			System.out.println("now also getting internal exception");
			e.getTargetException().printStackTrace();
			System.out.println("RandomizedCall (RandomMethod.aj) use_decompose: " +
					use_decompose + " InvocationTargetException " + e);
		} catch (NoSuchMethodException e) {
			System.out.println("RandomizedCall (RandomMethod.aj) use_decompose: " + 
					use_decompose + " NoSuchMethodException " + e);
		}
		return null;
	}
}
