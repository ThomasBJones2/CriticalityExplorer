package com.criticalityworkbench.randcomphandler;

import au.com.bytecode.opencsv.CSVReader;
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

	public static final int ERROR_POINTS = 4000;
	public static final int NUM_RUNS = 10; //1000;

	public static String criticalityExperimentName = "RandComp.CriticalityExperimenter";

	static String inputClassName, experimentClassName, experimentTypeName;

	static int[] inputSizes = {4,8,16}; //{10, 50, 100}; //{10, 100, 250, 500};	
	
	String fallibleMethodName, scoreName;
	int errorPoint, runName, experimentSize;	

	boolean experimentRunning, sdcError = true;

	Exception nonSDCError;

	static int nonSDCCount = 0;
		
	Location locLocation;

  static long MAX_RUN_TIME = 2*60*1000; //This is the number of milliseconds one run might take

  static RunTimeTriple<Long> singleRunTime; 

	static final int numThreads = 16;

	static String rawDataOutputDirectory = "./output/";

	static List<String> fallibleMethods = new ArrayList<String>();

	static CSVWriter outputWriter;

	static CSVReader inputReader;

	static String outputFile;

  static Writer writerForOutput = null;

	static Reader readerForInput = null;

  static Collection<Future<?>> theFutures = new ArrayList<Future<?>>(); 

	Experimenter(){}

	Experimenter(
		int runName, 
		int errorPoint,
		int experimentSize,
		boolean experimentRunning,
		String fallibleMethodName,
		String scoreName){

		this.errorPoint = errorPoint;
		this.runName = runName;
		this.experimentSize = experimentSize;
		this.experimentRunning = experimentRunning;
		this.fallibleMethodName = fallibleMethodName;
		this.scoreName = scoreName;
	}

	public abstract void runMain() throws InterruptedException, IOException;

	public static Experimenter emptyObject(){return null;}

  public static String getResetString(String errorPoint, String fallMethName){
    return("# " + errorPoint + "\n# " + fallMethName);
	}

  public static String handler(String argument) throws IOException{
			String[] new_args = argument.split(" ");
			if(new_args[0].equals("h")){ 
				System.out.println("");
				return(null);
			} else {	

				inputClassName = new_args[0]; 
				experimentClassName = new_args[1];	
				experimentTypeName = new_args[4];
        int experimentSize = Integer.parseInt(new_args[5]);
				inputSizes = new int[] {experimentSize};


        writerForOutput = new StringWriter();

        readerForInput = new StringReader(getResetString(new_args[2], new_args[3]));
	

				Experimenter.runIds = new ArrayList<>();		

				try{
					testInputObjects();
					initialize(inputClassName);
					initialize(experimentClassName);

					//fallibleMethods = getMethodsAnnotatedWith(Randomize.class);
					((Experimenter)getNewObject(experimentTypeName)).runMain();

				} catch (IllegalArgumentException E){
					System.out.println("illegal argument exception at top level of experimenter program");
					System.out.println(E);
				} catch (InterruptedException E){
					System.out.println(E);
				}
			
			}
			return writerForOutput.toString();
	}

	public static void main(String args[]) throws IOException{
		if(args[0].equals("h") || 
				args[0].equals("H") ||
				args[0].equals("help") ||
				args[0].equals("Help") ||
				args[0].equals("--help") ||
				args[0].equals("--Help")){
			System.out.println("Welcome to the RandomComputation Service");
			System.out.println("There are two ways to use this service: \n" +
					"(1) java.jar RandJava.jar Input_Object Main_Object Experiment_Type(i.e.\n" +
					"CriticalityExperimenter) OR \n"+
				  "(2) java.jar RandJava.jar Input_Object Main_Object Experiment_Type \n" +
 					"Data_output_directory \n"+
					"Alternatively you can call the handler by setting the 5th input to \n" +
					"org.criticalityworkbench.randcomphandler.CriticalityExperimenter \n" + 
					"in the handler... Here the first and second \n" + 
					"items in the arguments should be the same as before while the third argument \n" +
					"is the errorpoint under examination and and the fourth argument is the \n" +
				  "fallible method index, The sixth argument is the scale \n" + 
					"of the computation \n");
		} else if(args[4].
				equals("com.criticalityworkbench.randcomphandler.CriticalityExperimenter")){
				System.out.println(handler(args[0] + 
							" " + args[1] + 
							" " + args[2] + 
							" " + args[3] + 
							" " + args[4] + 
							" " + args[5] ));
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

				//fallibleMethods = getMethodsAnnotatedWith(Randomize.class);
				((Experimenter)getNewObject(experimentTypeName)).runMain();

			} catch (IllegalArgumentException E){
				System.out.println("illegal argument exception at top level of experimenter program");
				System.out.println(E);
			} catch (InterruptedException E){
				System.out.println(E);
			}
		}

	}

	public static ReentrantReadWriteLock IdLock = new ReentrantReadWriteLock(true); 
	public static ReentrantReadWriteLock insdcLock = new ReentrantReadWriteLock(true); 
  public static ReentrantReadWriteLock outputStringLock = new ReentrantReadWriteLock(true);


	public static ArrayList<RunId> runIds;

	public static RunId getId(RunId inId){
		IdLock.readLock().lock();
		RunId theId = runIds.get(runIds.indexOf(inId));
		IdLock.readLock().unlock();
		return theId;
	}

	public static void incrementNonSDCCount(){
		insdcLock.writeLock().lock();
		nonSDCCount ++;
		insdcLock.writeLock().unlock();
	}

	public static void addId(RunId inId){
		IdLock.writeLock().lock();
		runIds.add(inId);
		IdLock.writeLock().unlock();
	}

	public static void removeId(RunId inId){
		IdLock.writeLock().lock();
		runIds.remove(runIds.indexOf(inId));
		IdLock.writeLock().unlock();
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
			fallibleMethodName,
			scoreName);

		if(curId.methodName == null){
			System.out.println("found null methodName in runObject");
			try{
				Thread.sleep(MAX_RUN_TIME/1000);
			} catch (Exception E){
				System.out.println(E);
			}
		}
		addId(curId);
		RandomMethod.createLocation(curId);
		//if (experimentRunning == false)
			//System.out.println("The error point is: " + errorPoint + " " + errorful);

		try{
			experiment.experiment(input);
		} catch (Exception e){
			Location theLocation = RandomMethod.getLocation(curId);
			
			System.out.println("printing the location!" + theLocation);
			theLocation.burnIn();
			incrementNonSDCCount();
			System.out.println("Non SDC error: " + errorful + 
					" non SDC Count " + nonSDCCount +  " " + e);
			nonSDCError = e;
			sdcError = false;
		}
	

		RandomMethod.registerTimeCount();
		if(errorful){
			locLocation = RandomMethod.getLocation(curId).getBurnIn();
			if(locLocation == null){
          Location l = RandomMethod.getLocation(curId);
					l.burnIn();
					locLocation = l.getBurnIn();
			}
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

	public abstract void dropZeros();

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
			saveFinalResults(correctObject, errorObject);
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
				theFuture.get(MAX_RUN_TIME, TimeUnit.MILLISECONDS);
			} catch (Exception e){
				theFuture.cancel(true);
			}
		}
	}

	static ArrayBlockingQueue<Runnable> threadQueue = null;
	static ThreadPoolExecutor thePool = null;

	static void resetThreading (){

			threadQueue = 
				new ArrayBlockingQueue<Runnable>(NUM_RUNS);
			thePool = 
				new ThreadPoolExecutor(numThreads,
					numThreads,
					0, 
					TimeUnit.SECONDS,
					threadQueue);
	}



	public static void runExperiments(ExperimentFunction EF)
			throws InterruptedException, IOException{
		
		RandomMethod.clearAspect();

		int loopCount = 0;
		for(int inputSize : inputSizes) {
		//for(int inputSize = 10; inputSize <= 1000; inputSize *= 10){

			resetThreading();

      outputFile = rawDataOutputDirectory + 
				inputClassName + 
				experimentClassName + 
				experimentTypeName + 
				inputSize + ".csv";

			if(writerForOutput == null)
        writerForOutput = new FileWriter(new File(outputFile), true);

			if(readerForInput == null)
        readerForInput = new FileReader(new File(outputFile));
	

			outputWriter = new CSVWriter(writerForOutput);
			inputReader = new CSVReader(readerForInput);

			EF.readResultsAndResetExperiment(inputReader);

			EF.runExperiment(inputSize, loopCount);
		
			Thread.sleep(MAX_RUN_TIME);

			for (Future<?> future : theFutures){
				try{
           System.out.println(future.get());
				} catch (Exception E){
           System.out.println("There was a future exception " + E);
				}

			}
			thePool.shutdownNow(); //to kill things immediately
			RandomMethod.in_debug_termination = true;
      //thePool.shutdown();
			while (!thePool.awaitTermination(60, TimeUnit.SECONDS)) {
				  System.out.println("Awaiting completion of threads." +
							thePool.isTerminating());
			}
		
			System.out.println("Done Printing Error Data for size: " + inputSize);	
		
			outputWriter.close();
			RandomMethod.clearAspect();
			loopCount ++;
		}
	}

	
	String[] concat(String[] first, String[] second) {
    List<String> both = new ArrayList<String>(first.length + second.length);
    Collections.addAll(both, first);
    Collections.addAll(both, second);
    return both.toArray(new String[both.size()]);
	}

	public void saveState(String[] state){
		outputStringLock.writeLock().lock();
		outputWriter.writeNext(state);
		try{
			outputWriter.flush();
		} catch (IOException e) {
			System.out.println("There was a problem flushing the result printer to csv " + e);
		}
		outputStringLock.writeLock().unlock();
	}

	public void saveFinalResults(
		Experiment correctObject, 
		Experiment errorObject){


		outputStringLock.writeLock().lock();
		Score[] scores = errorObject.scores(correctObject);				
		Score errorScore = null;		
		if(!sdcError) {
			errorScore = ScorePool.nullScore(nonSDCError);
		}

		String[] extras = new String[2];		
		extras[0] = "timeCount: " + locLocation.timeCount;

		//this must be a score because economy epsilon, etc. use failcount score
		//to prove that they have the same or greater number of runs
		extras[1] = "score: failCount " + locLocation.getFailCount();	

		String[] locationStrings = getLocationStrings(locLocation);
		String[] scoreStrings = getScoreStrings(scores, errorScore);
		

		outputWriter.writeNext(concat(extras, concat(locationStrings, scoreStrings)));

		try{
			outputWriter.flush();
		} catch (IOException e) {
			System.out.println("There was a problem flushing the result printer to csv " + e);
		}
		outputStringLock.writeLock().unlock();
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
		String[] locationOutput = 
			locationOutputList.toArray(new String[locationOutputList.size()]);
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
		} catch (IllegalAccessException E) {
			System.out.println("Trouble accessing " + inName + ": " + E);
		} catch (NoSuchMethodException E) {
			System.out.println("Trouble initializing class" + inName + ". Is it possible that you don't have an initialization method for that class?" + E);
		} catch (InvocationTargetException E) {
			System.out.println("Trouble getting random inputs:" + E);
		}
	}

	static Object getNewInputObject(String inName, int size){
		try {
			Class cls = Class.forName(inName);
			Method clsMethod = cls.getMethod("newInputOfSize", int.class);

			Constructor ctor = cls.getConstructor(int.class);
			Object clsInstance = clsMethod.invoke(null, (Object) size);
			return clsInstance;
		} catch (ClassNotFoundException E) {
			System.out.println(inName + " is not a class, please add to the classpath: " + E);
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
		if(!fallibleMethods.contains(methodName) 
				&& methodName != null && 
				!methodName.equals("null")){
			
			fallibleMethods.add(new String(methodName));
		}
	}

	public static void printFallibleMethods(){
		System.out.println("Printing Fallible Methods");	
		
		for(String f : fallibleMethods){
			System.out.println(f);
		}
		try{
			Thread.sleep(1000);
		} catch (Exception E){
			System.out.println(E);
		}
	}

	static RunTimeTriple<Long>[][] getRunTimes() throws InterruptedException{
		RunTimeTriple<Long>[] nearOut = new RunTimeTriple[inputSizes.length];

		
		int loopCount = 0;
		for(int inputSize : inputSizes){	
			long avgRunTime = System.currentTimeMillis();
			ArrayList<Thread> threads = new ArrayList<>();
			
				for(int i = 0; i < ERROR_POINTS; i ++){
					Experimenter exp = new CriticalityExperimenter(i, i, inputSize, false, "All", "All");
					Thread thread = new Thread(exp);
					threads.add(thread);
					thread.start();
				}
				for(Thread t : threads){
					t.join(MAX_RUN_TIME);
					if(t.isAlive()) System.out.println("interupting " + t.toString()); t.interrupt();
				}
				avgRunTime = (System.currentTimeMillis() - avgRunTime)/ERROR_POINTS;
				nearOut[loopCount] = new RunTimeTriple<Long>(avgRunTime,
						(long)RandomMethod.getMaxTimeCount());
				System.out.println("The average run time is: " + avgRunTime);
				System.out.println("The max error point is: " + RandomMethod.getMaxTimeCount());
				RandomMethod.clearAspect();
			loopCount ++;		
		}
		RunTimeTriple<Long>[][] out = runTimeBoost(nearOut);

		return out;
	}

	static RunTimeTriple<Long>[][] runTimeBoost(RunTimeTriple<Long>[] rtt){
		RunTimeTriple<Long>[][] out = new RunTimeTriple[fallibleMethods.size()][rtt.length];
		for(int j = 0; j < fallibleMethods.size(); j ++){
			for(int i = 0; i < rtt.length; i ++){
				out[j][i] = new RunTimeTriple(rtt[i]);
				out[j][i].name = fallibleMethods.get(j);
			}
		}
		return out;
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
