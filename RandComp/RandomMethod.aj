package RandComp;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;

public aspect RandomMethod{

	static ArrayList<Distance> distances = new ArrayList<>();

	static ArrayList<Integer> timeCounts = new ArrayList<>();

	
	public static void clearAspect(){
		distances = new ArrayList<>();
		timeCounts = new ArrayList<>();
	}

	public static synchronized void registerTimeCount(){
		RunId curId = new RunId(Thread.currentThread().getId());
		curId = Experimenter.getId(curId);
		Distance theDistance = getDistance(curId);
		timeCounts.add(theDistance.timeCount);
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
		Distance theDistance = getDistance(curId);

		System.out.println("On thread: " + curId.getThreadId() + ": ");
		System.out.println("The time count is: " + theDistance.timeCount);
		for(DefinedDistance d : theDistance.getDefinedDistances()){
			System.out.println("The time count on method " + d.name + 
				" is " + d.distance);
		}
	}

	public void printAllDistances(){
		for(Distance distance : distances){
			distance.print();
		}

		for(Integer integer: timeCounts){
			System.out.println(integer);
		}
	}


	pointcut Randomize(): call(@Randomize * *(..));

	pointcut PrintAspect() : call(void *.printAspect());

	after() : PrintAspect() {
		printAllDistances();
		//printThisAspect();
	}

	public static synchronized void clearDistance(RunId curId){
		Distance checkDistance = new Distance(curId.getRunName(), curId.getThreadId());
    while(distances.indexOf(checkDistance) != -1){
			distances.remove(distances.indexOf(checkDistance));
		}
	}

	public static synchronized void createDistance(RunId curId){
		Distance checkDistance = new Distance(curId.getRunName(), curId.getThreadId());
		distances.add(checkDistance);
	}

	public static synchronized Distance getDistance(RunId curId){
		Distance checkDistance = new Distance(curId.getRunName(), curId.getThreadId());
		if(distances.indexOf(checkDistance) >= 0)
			return distances.get(distances.indexOf(checkDistance));
		else
			return checkDistance;
	}

	boolean forcedError(int timeCount, RunId curId){
		return timeCount == curId.errorPoint && curId.errorful;
	}

	boolean unForcedError(int timeCount, RunId curId){
		return timeCount != curId.errorPoint;
	}

	void updateSingleDistance(Distance theDistance, DefinedDistance handle){
		if(theDistance.dDistances.contains(handle)){
			int index = theDistance.dDistances.indexOf(handle);
			theDistance.dDistances.get(index).setDistance(handle.getDistance());
			theDistance.dDistances.get(index).pertinent = true;
		} else {
			DefinedDistance dist = new DefinedDistance(handle);
			dist.pertinent = true;
			theDistance.dDistances.add(dist);
		}
	}

	void incrementSingleDistance(Distance theDistance, DefinedDistance handle){
		DefinedDistance dDistance = null;
		if(theDistance.dDistances.contains(handle)){
			dDistance = 
				theDistance.dDistances.get(theDistance.dDistances.indexOf(handle));
		} else {
			dDistance = new DefinedDistance(handle.getName(), 0);
			theDistance.dDistances.add(dDistance);
		}
		dDistance.increment();
		dDistance.pertinent = true;
	}

	void updateDistances(Distance theDistance, 
			RunId curId, 
			String methodName, 
			AbstractDistance targetObject){

			theDistance.clearPertinence();

			DefinedDistance handle = new DefinedDistance(methodName);		

			incrementSingleDistance(theDistance, handle);
			ArrayList<DefinedDistance> absDistances = targetObject.getCurrentDistances();
			for(DefinedDistance d : absDistances){
				updateSingleDistance(theDistance, d);
			}

			if(forcedError(theDistance.timeCount, curId)){
				theDistance.burnIn();
			}
	}

	Object around() : Randomize() {
		Object targetObject = thisJoinPoint.getTarget();    	
		final Object[] args = thisJoinPoint.getArgs();

		RunId curId = new RunId(Thread.currentThread().getId());
		curId = Experimenter.getId(curId);
		if(curId.getExperiment() == true){
			Random rand = curId.getRand();
			Distance theDistance = getDistance(curId);
			
			String methodName = thisJoinPointStaticPart.
				getSignature().
				getDeclaringTypeName()
				+ "." + thisJoinPointStaticPart.
				getSignature().
				getName();
		
			updateDistances(theDistance, curId, methodName, (AbstractDistance) targetObject);
			//increment time count seperately to account for 0 indexing
			theDistance.timeCount ++;
			
			String shortMethodName = thisJoinPointStaticPart.
				getSignature().
				getName();

			String randMethodName = shortMethodName + "Rand";

			//must account for early increment due to return...
			if((rand.nextDouble() < 0.0 && 
					unForcedError(theDistance.timeCount - 1, curId)) || 
					forcedError(theDistance.timeCount - 1, curId)) {
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
