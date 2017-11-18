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

	public static final int RUNTIME_ERROR_POINTS = 10;
	public static final int NUM_RUNS = 1000;

  public static boolean in_proxy_experiment = false;

	public static String criticalityExperimentName = 
		"com.criticalityworkbench.randcomphandler.CriticalityExperimenter";

	static String inputClassName, experimentClassName, experimentTypeName;

	static int[] inputSizes = {4,8,16}; //{10, 50, 100}; //{10, 100, 250, 500};	
	
	String fallibleMethodName, scoreName;
	int errorPoint, runName, experimentSize;	

	boolean experimentRunning, sdcError = true;

	Exception nonSDCError;

	static int nonSDCCount = 0;
		
	Location locLocation;

  static long MAX_RUN_TIME = 2*60*1000; //This is the number of milliseconds one run might take

  //static RunTimeTriple<Long> singleRunTime; 

	static final int numThreads = 100;

	static String rawDataOutputDirectory = "./output/";

  static String processedDataInputDirectory = "";
	static String imageRootDirectory = "";

	static List<String> fallibleMethods = new ArrayList<String>();

	static CSVWriter outputWriter;

	static CSVReader inputReader;

	static String outputFile;
	
	static String proxyMethodName = "";

  static Writer writerForOutput = null;

	static Reader readerForInput = null;

  static Collection<Future<?>> theFutures = new ArrayList<Future<?>>(); 

  static RunTimeTriple<Long>[][] rTimeTriple;

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
    return("#,errorPoint: " + errorPoint + ",fallmeth: " + fallMethName);
	}

  public static String runTimeHandler(String argument) throws IOException{
			String[] new_args = argument.split(" ");
			
			String out = "";
			if(new_args[0].equals("h")){ 
				System.out.println("");
				return(null);
			} else {	

	      fallibleMethods = new ArrayList<String>();
        proxyMethodName = "";
				writerForOutput = null;
        readerForInput = null;



				inputClassName = new_args[1]; 
				experimentClassName = new_args[2];	
				experimentTypeName = new_args[3];
        int experimentSize = Integer.parseInt(new_args[4]);
				inputSizes = new int[] {experimentSize};

				Experimenter.runIds = new ArrayList<>();		

        if(new_args[5].equals("Decompose")){
            RandomMethod.use_decompose = true;
				} else {
					  System.out.println("In runTimeHandler set use_decompose to False");
					  RandomMethod.use_decompose = false;
				}

				try{
					testInputObjects();
					initialize(inputClassName);
					initialize(experimentClassName);

					out = ((CriticalityExperimenter)getNewObject(experimentTypeName)).getRunTimeString();

				} catch (IllegalArgumentException E){
					System.out.println("illegal argument exception at top level of experimenter program");
					System.out.println(E);
				}
			
			}
			return out;
	}

  public static String handler(String argument) throws IOException{
			String[] new_args = argument.split(" ");
			if(new_args[0].equals("h")){ 
				System.out.println("");
				return(null);
			} else {	

	      fallibleMethods = new ArrayList<String>();
        proxyMethodName = "";
				writerForOutput = null;
        readerForInput = null;




				inputClassName = new_args[0]; 
				experimentClassName = new_args[1];	
				experimentTypeName = new_args[4];
        int experimentSize = Integer.parseInt(new_args[5]);
        int runTime = Integer.parseInt(new_args[6]);
				int errorPoints = Integer.parseInt(new_args[7]);
        

				inputSizes = new int[] {experimentSize};
        fallibleMethods = Arrays.asList(new_args[8].split("/"));   
			
        if(new_args[9].equals("Decompose")){
            RandomMethod.use_decompose = true;
						System.out.println("RandomMethod use Decompose is set to True");
				} else {
            RandomMethod.use_decompose = false;
				}

				RunTimeTriple<Long>[] rtt = new RunTimeTriple[1];
				rtt[0] = new RunTimeTriple<Long>((long)Math.max(1, runTime),
					(long)errorPoints);
				rTimeTriple = runTimeBoost(rtt); 



        writerForOutput = new StringWriter();

        readerForInput = new StringReader(
						    getResetString(new_args[2], 
							  fallibleMethods.get(Integer.parseInt(new_args[3]))
							)
						);
	

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
			System.out.println(writerForOutput.toString());
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
		} else if (args[0].equals("runTime")){ 
					System.out.println(runTimeHandler(args[0] + 
							" " + args[1] + 
							" " + args[2] + 
							" " + args[3] + 
							" " + args[4] + 
							" " + args[5]));
		}
		else if(args[4].
				equals("com.criticalityworkbench.randcomphandler.CriticalityExperimenter")){
				System.out.println(handler(args[0] + 
							" " + args[1] + 
							" " + args[2] + 
							" " + args[3] + 
							" " + args[4] + 
							" " + args[5] + 
							" " + args[6] + 
							" " + args[7] +
							" " + args[8] + 
							" " + args[9]));
		} else {
			inputClassName = args[0]; 
			experimentClassName = args[1];	
			experimentTypeName = args[2];
			rawDataOutputDirectory = args[3];
      
			int experimentSize = Integer.parseInt(args[4]);
			inputSizes = new int[] {experimentSize};


      fallibleMethods = Arrays.asList(args[5].split("/"));   


			if(args[6].equals("Decompose")){
					RandomMethod.use_decompose = true;
			} else {
					RandomMethod.use_decompose = false;
			}

      if(args.length > 7)
			  processedDataInputDirectory = args[7];
			if(args.length > 8)
				imageRootDirectory = args[8];
			if(args.length > 9)
				proxyMethodName = args[9];
			
			System.out.println("List of input arguments: ");
			int v = 0;
			for(String arg : args) {
				System.out.println("argument " + v + ": " +arg);
				v ++;
			}
			System.out.println();
      


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
    System.out.println("Now at the last line of Main in experimenter!");
		System.exit(0);
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
			
			System.out.println("printing the location! " + theLocation);
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
		if(!sdcError){
        System.out.println("no sdc error and made it here!");
		}
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

	static void resetThreading () throws InterruptedException{
			threadQueue = 
				new ArrayBlockingQueue<Runnable>(numThreads*3);
			thePool = 
				new ThreadPoolExecutor(numThreads,
					numThreads,
					0, 
					TimeUnit.SECONDS,
					threadQueue);
      
			long start_time = System.currentTimeMillis();
		  long wait_time = start_time + MAX_RUN_TIME - System.currentTimeMillis();
			for(long i = 0; i < wait_time; i += 1000){
        boolean all_done = true;
				for(Future<?> f : theFutures){
          all_done &= f.isDone();
				}
				if(all_done)
					i = wait_time;
				System.out.println("reseting threads... " + i + " of " + wait_time + " " + all_done);
			  Thread.sleep(1000);
			}
			thePool.shutdownNow(); 
			RandomMethod.in_debug_termination = true;
			for(Future<?> f : theFutures){
		      f.cancel(true);
			}
			while (!thePool.awaitTermination(60, TimeUnit.SECONDS)) {
				  System.out.println("Awaiting completion of threads in reset threading..." +
							thePool.isTerminating());
			}

			threadQueue = 
				new ArrayBlockingQueue<Runnable>(numThreads*3);
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

      String loc_proxy_name = "";
			if(in_proxy_experiment){
        	loc_proxy_name = proxyMethodName.split("[.]")[proxyMethodName.split("[.]").length - 2]
					  + "." + proxyMethodName.split("[.]")[proxyMethodName.split("[.]").length - 1] + "_";
			}

      outputFile = rawDataOutputDirectory + 
				inputClassName.split("[.]")[inputClassName.split("[.]").length - 1] + "_" +
				experimentClassName.split("[.]")[experimentClassName.split("[.]").length - 1] + "_" +
				loc_proxy_name +
				experimentTypeName.split("[.]")[experimentClassName.split("[.]").length - 1] + "_" +
				inputSize + ".csv";

			if(writerForOutput == null)
        writerForOutput = new FileWriter(new File(outputFile), true);

			if(readerForInput == null)
        readerForInput = new FileReader(new File(outputFile));
	

			outputWriter = new CSVWriter(writerForOutput);
			inputReader = new CSVReader(readerForInput);

			EF.readResultsAndResetExperiment(inputReader);
      long start_time = System.currentTimeMillis();
			EF.setOutputWriter(outputWriter);
			EF.runExperiment(inputSize, loopCount);
		  long wait_time = start_time + MAX_RUN_TIME - System.currentTimeMillis();
			for(long i = 0; i < wait_time; i += 1000){
        boolean all_done = true;
				for(Future<?> f : theFutures){
          all_done &= f.isDone();
				}
				if(all_done)
					i = wait_time;
				System.out.println("main thread waiting... " + i + " of " + wait_time + " " + all_done);
			  Thread.sleep(1000);
			}
			thePool.shutdownNow(); 
			RandomMethod.in_debug_termination = true;
			for(Future<?> f : theFutures){
		      f.cancel(true);
			}


			//to kill things immediately
      //thePool.shutdown();
			while (!thePool.awaitTermination(60, TimeUnit.SECONDS)) {
				  System.out.println("Awaiting completion of threads." +
							thePool.isTerminating());
			}
		
			System.out.println("Done Printing Error Data for size: " + inputSize);	
		
			outputWriter.close();
			inputReader.close();
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

    Score[] scores;
    if(sdcError){
		    scores = errorObject.scores(correctObject);			
		} else {
        scores = new Score[0];
		}	
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
			System.out.println("Fallible method added! " + methodName + " use_decompose " +
				 RandomMethod.use_decompose);	
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
			
			ArrayBlockingQueue<Runnable> locThreadQueue = null;
			ThreadPoolExecutor locPool = null;
			Collection<Future<?>> locFutures = new ArrayList<Future<?>>(); 
			locThreadQueue = 
				new ArrayBlockingQueue<Runnable>(numThreads*3);
			locPool = 
				new ThreadPoolExecutor(numThreads,
					numThreads,
					0, 
					TimeUnit.SECONDS,
					locThreadQueue);


			for(int i = 0; i < RUNTIME_ERROR_POINTS; i ++){
				while(locThreadQueue.size() >= numThreads*3){}
				Experimenter exp = new CriticalityExperimenter(i, i, inputSize, false, "All", "All");
				locFutures.add(locPool.submit(exp));
			}

			locPool.shutdown(); 
			//RandomMethod.in_debug_termination = true;
			//for(Future<?> f : locFutures){
		  //    f.cancel(true);
			//}

			while (!locPool.awaitTermination(60, TimeUnit.SECONDS)) {
				  System.out.println("Awaiting completion of threads in get runTimes" +
							locPool.isTerminating());
			}
			//RandomMethod.in_debug_termination = false;
			
			avgRunTime = (System.currentTimeMillis() - avgRunTime)/RUNTIME_ERROR_POINTS;
			nearOut[loopCount] = new RunTimeTriple<Long>(Math.max(1, avgRunTime),
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
		System.out.println("fallibleMethods.size is: " + fallibleMethods.size());
		for(int j = 0; j < fallibleMethods.size(); j ++){
			for(int i = 0; i < rtt.length; i ++){
				out[j][i] = new RunTimeTriple(rtt[i]);
				out[j][i].name = fallibleMethods.get(j);
				System.out.println(out[j][i].name);
			}
		}
		return out;
	}



}
