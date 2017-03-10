package RandComp;

import au.com.bytecode.opencsv.CSVWriter;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.*;
import java.io.*;
import java.lang.annotation.Annotation;
import org.reflections.Reflections;
import org.reflections.scanners.*;
public abstract class Experimenter implements Runnable{

	public static final int ERROR_POINTS = 1000;

	static final int MAX_RUN_TIME = 10000;

	static String inputClassName, experimentClassName, experimentTypeName;
	
	
	String fallibleMethodName;
	int errorPoint, runName, experimentSize;	

	boolean experimentRunning, sdcError = true;

	Exception nonSDCError;
		
	Location locLocation;

	static final int numThreads = 16;

	static String rawDataOutputDirectory = "./output/";

	static List<String> FallibleMethods = new ArrayList<String>();

	static CSVWriter outputWriter;

	static String outputFile;

	Experimenter(){}

	Experimenter(
		int runName, 
		int errorPoint,
		int experimentSize,
		boolean experimentRunning,
		String fallibleMethodName){

		this.errorPoint = errorPoint;
		this.runName = runName;
		this.experimentSize = experimentSize;
		this.experimentRunning = experimentRunning;
		this.fallibleMethodName = fallibleMethodName;
	}

	public abstract void runMain() throws InterruptedException, IOException;

	public static Experimenter emptyObject(){return null;}

	public static void main(String args[]) throws IOException{
		if(args[0].equals("h") || args[0].equals("H")){
			System.out.println("Welcome to the RandomComputation Service");
			System.out.println("There are two ways to use this service: \n" +
					"(1) java.jar RandJava.jar Input_Object Main_Object Experiment_Type  OR \n"+
				  "(2) java.jar RandJava.jar Input_Object Main_Object Experiment_Type "
 					+ "Data_output_directory");
		} else {

			inputClassName = args[0]; 
			experimentClassName = args[1];	
			experimentTypeName = args[2];

			if(args.length == 4){
				rawDataOutputDirectory = args[3];
			}

			Experimenter.runIds = new ArrayList<>();		

			try{
				testInputObjects();
				initialize(inputClassName);
				initialize(experimentClassName);

				//FallibleMethods = getMethodsAnnotatedWith(Randomize.class);
				((Experimenter)getNewObject(experimentTypeName)).runMain();

			} catch (IllegalArgumentException E){
				System.out.println("illegal argument exception at top level of experimenter program");
				System.out.println(E);
			} catch (InterruptedException E){
				System.out.println(E);
			}
		}

	}

	public static ReentrantReadWriteLock rwLock = new ReentrantReadWriteLock(); 

	public static ArrayList<RunId> runIds;

	public static RunId getId(RunId inId){
		rwLock.readLock().lock();
		RunId theId = runIds.get(runIds.indexOf(inId));
		rwLock.readLock().unlock();
		return theId;
	}

	public static void addId(RunId inId){
		rwLock.writeLock().lock();
		runIds.add(inId);
		rwLock.writeLock().unlock();
	}

	public static void removeId(RunId inId){
		rwLock.writeLock().lock();
		runIds.remove(runIds.indexOf(inId));
		rwLock.writeLock().unlock();
	}

	private static void printAspect(){}

	private Experiment runObject(Input input, 
			Experiment experiment, 
			Random rand, 
			boolean errorful){

		RunId curId = new RunId(2*runName + (errorful?1:0), 
			true,
			errorful, 
			errorPoint, 
			Thread.currentThread().getId(), 
			rand,
			fallibleMethodName);

		if(curId.methodName == null){
			System.out.println("found null methodName in runObject");
			try{
				Thread.sleep(MAX_RUN_TIME);
			} catch (Exception E){
				System.out.println(E);
			}
		}
		addId(curId);
		RandomMethod.createLocation(curId);
		if (experimentRunning == false)
			System.out.println("The error point is: " + errorPoint + " " + errorful);

		try{
			experiment.experiment(input);
		} catch (Exception e){
			//System.out.println("Non SDC error: " + errorful + " " + e);
			nonSDCError = e;
			sdcError = false;
		}
	

		RandomMethod.registerTimeCount();
		if(errorful){
			locLocation = RandomMethod.getLocation(curId).getBurnIn();
		}
		RandomMethod.clearLocation(curId);
		removeId(curId);
		return experiment;
	}

	void printLocationsAndScores(Experiment correctObject, Experiment errorObject){
		locLocation.print();		
		Score[] scores = errorObject.scores(correctObject);
		for(Score s : scores){
			s.print();
		}
	}

	Experiment errorObject, correctObject;

	@Override
	public void run(){
		long seed = new Random().nextLong();
		long inputSeed = new Random().nextLong();
		/*System.out.println("Starting " + 
			Thread.currentThread().getId() + 
			" on seed: " + seed + " and input seed " + inputSeed);*/
		Random rand = new Random(inputSeed);
		Random rand1 = new Random(seed);
		Random rand2 = new Random(seed);


		//this is hear to program the aspect to not run the experiment
		RunId curId = new RunId(Thread.currentThread().getId());
		curId.setExperiment(false);
		addId(curId); 
		errorObject = (Experiment)getNewObject(experimentClassName);
		correctObject = (Experiment)getNewObject(experimentClassName);
		Input iObject1 = (Input)getNewInputObject(inputClassName, experimentSize);
		Input iObject2 = (Input)getNewObject(inputClassName);
		iObject1.randomize(rand);
		iObject2.copy(iObject1);
		removeId(curId);

		correctObject = runObject(iObject1, correctObject, rand1, false);
		errorObject = runObject(iObject2, errorObject, rand2, true);

		if(experimentRunning) {
			printFinalResults(correctObject, errorObject);
		}
	}

	static class CheckFuture implements Runnable{
		Future theFuture;

		CheckFuture(Future theFuture){
			this.theFuture = theFuture;
		}

		@Override
		public void run(){
			try{
				theFuture.get(MAX_RUN_TIME*2, TimeUnit.MILLISECONDS);
			} catch (Exception e){
				theFuture.cancel(true);
			}
		}
	}

	public static void runExperiments(ExperimentFunction EF)
			throws InterruptedException, IOException{
		
		RandomMethod.clearAspect();
		for(int inputSize = 10, loopCount = 0; inputSize <= 1000; inputSize *= 10, loopCount ++){
			ArrayBlockingQueue<Runnable> threadQueue = 
				new ArrayBlockingQueue<Runnable>(8);
			ThreadPoolExecutor thePool = 
				new ThreadPoolExecutor(numThreads,
					numThreads,
					0, 
					TimeUnit.SECONDS,
					threadQueue);

			ArrayBlockingQueue<Runnable> checkThreadQueue = 
				new ArrayBlockingQueue<Runnable>(8);
			ThreadPoolExecutor checkThread = 
				new ThreadPoolExecutor(numThreads,
						numThreads,
						0,
						TimeUnit.SECONDS,
						checkThreadQueue);

			outputFile = rawDataOutputDirectory + 
				inputClassName + 
				experimentClassName + 
				experimentTypeName + 
				inputSize + ".csv";

			outputWriter = new CSVWriter(new FileWriter(new File(outputFile)));

			EF.runExperiment(threadQueue, thePool, checkThreadQueue, checkThread, inputSize, loopCount);
		
			thePool.shutdown();
			while (!thePool.awaitTermination(60, TimeUnit.SECONDS)) {
				  System.out.println("Awaiting completion of threads.");
			}
		
			System.out.println("Printing Average Error Data data for size: " + inputSize);	
		
			outputWriter.close();
			RandomMethod.clearAspect();
		}
	}

	public static void readResultsAndResetExperiment(){

	}
	
	String[] concat(String[] first, String[] second) {
    List<String> both = new ArrayList<String>(first.length + second.length);
    Collections.addAll(both, first);
    Collections.addAll(both, second);
    return both.toArray(new String[both.size()]);
	}

	public synchronized void printFinalResults(
		Experiment correctObject, 
		Experiment errorObject){

		Score[] scores = errorObject.scores(correctObject);				
		Score errorScore = null;		
		if(!sdcError) {
			errorScore = ScorePool.nullScore(nonSDCError);
		}			

		String[] timeCount = new String[1];
		timeCount[0] = "timeCount: " + locLocation.timeCount;
	
		String[] locationStrings = getLocationStrings(locLocation);
		String[] scoreStrings = getScoreStrings(scores, errorScore);
			
		outputWriter.writeNext(concat(timeCount, concat(locationStrings, scoreStrings)));

		try{
			outputWriter.flush();
		} catch (IOException e) {
			System.out.println("There was a problem flushing the result printer to csv " + e);
		}
	}


	public void iteratorPrint(String[] strings){
		for(String string : strings){
			System.out.println(string);
		}

	}

	public String[] getScoreStrings(Score[] scores, Score errorScore){
		String[] theScores = new String[scores.length + (errorScore==null?0:1)];
		for(int i = 0; i < scores.length; i ++){
			theScores[i] = "score: " + scores[i].toString();
		}
		if(errorScore != null)
			theScores[theScores.length - 1] = "score: " + errorScore.toString();
		
		return theScores;
	}

	public String[] getLocationStrings(Location location){
		ArrayList<String> locationOutputList = location.outputIterator();		
		String[] locationOutput = locationOutputList.toArray(new String[locationOutputList.size()]);

		return locationOutput;
	}
	

	private static void testInputObjects() throws IllegalArgumentException {
		Object inputClass = getNewObject(inputClassName);
		Object experimentClass = getNewObject(experimentClassName);
		System.out.println(inputClass.getClass());
		System.out.println(experimentClass.getClass());
		if(!(experimentClass instanceof Experiment)) {
			System.out.println("The experiment class is not correct!");
			throw new IllegalArgumentException();
		}
		if(!(inputClass instanceof Input)){
			System.out.println("The input class is not correct!");
			throw new IllegalArgumentException();
		}
	}


	static void initialize(String inName){
		try {
			Class cls = Class.forName(inName);
			Method clsMethod = cls.getMethod("initialize");
			clsMethod.invoke(null);
	
		} catch (ClassNotFoundException E) {
			System.out.println(inName + " is not a class, please add to the classpath: " + E);
//		} catch (InstantiationException E) {
//			System.out.println("Trouble instantiating " + inName + ": " + E);
		} catch (IllegalAccessException E) {
			System.out.println("Trouble accessing " + inName + ": " + E);
		} catch (NoSuchMethodException E) {
			System.out.println("Trouble building random input using an input size on class" + inName + ". Is it possible that you don't have a constructor for input size or that the constructor is private?" + E);
		} catch (InvocationTargetException E) {
			System.out.println("Trouble getting random inputs:" + E);
		}
	}

	static Object getNewInputObject(String inName, int size){
		try {
			Class cls = Class.forName(inName);
			Method clsMethod = cls.getMethod("newInputOfSize", int.class);

			Constructor ctor = cls.getConstructor(int.class);
			//Object clsInstance = ctor.newInstance(size);		
			Object clsInstance = clsMethod.invoke(null, (Object) size);
			return clsInstance;
		} catch (ClassNotFoundException E) {
			System.out.println(inName + " is not a class, please add to the classpath: " + E);
//		} catch (InstantiationException E) {
//			System.out.println("Trouble instantiating " + inName + ": " + E);
		} catch (IllegalAccessException E) {
			System.out.println("Trouble accessing " + inName + ": " + E);
		} catch (NoSuchMethodException E) {
			System.out.println("Trouble building random input using an input size on class" + inName + ". Is it possible that you don't have a constructor for input size or that the constructor is private?" + E);
		} catch (InvocationTargetException E) {
			System.out.println("Trouble getting random inputs:" + E);
		}
		return null;
	}

	static Object getNewObject(String inName) {
		try {
			Class cls = Class.forName(inName);
			Method clsMethod = cls.getMethod("emptyObject");
			Object clsInstance = clsMethod.invoke(null);
			//Object clsInstance = (Object) cls.newInstance();		
			return clsInstance;
		} catch (ClassNotFoundException E) {
			System.out.println(inName + " is not a class, please add to the classpath: " + E);
//		} catch (InstantiationException E) {
//			System.out.println("Trouble instantiating " + inName + ": " + E);
		} catch (IllegalAccessException E) {
			System.out.println("Trouble accessing " + inName + ": " + E);
		} catch (NoSuchMethodException E) {
			System.out.println("Trouble building random input using an input size on class" + inName + ". Is it possible that you don't have a constructor for input size or that the constructor is private?" + E);
		} catch (InvocationTargetException E) {
			System.out.println("Trouble getting random inputs:" + E);
		}
	
		return null;
	}

	public static synchronized void addToFallibleMethods(String methodName){
		if(!FallibleMethods.contains(methodName) 
				&& methodName != null && 
				!methodName.equals("null")){
			
			FallibleMethods.add(new String(methodName));
		}
	}

	public static void printFallibleMethods(){
		System.out.println("Printing Fallible Methods");	
		
		for(String f : FallibleMethods){
			System.out.println(f);
		}
		try{
			Thread.sleep(1000);
		} catch (Exception E){
			System.out.println(E);
		}
	}


	public static List<String> getMethodsAnnotatedWith(
			final Class<? extends Annotation> annotation) {

		Reflections reflections = new Reflections("InputObjects", 
				new MethodAnnotationsScanner());
		Set<Method> methods = reflections.getMethodsAnnotatedWith(annotation);
		List<String> out = new ArrayList<>();
		for(Method method : methods){
			out.add(method.getDeclaringClass() + "." + method.getName());
		}
		return out;
	}


}
