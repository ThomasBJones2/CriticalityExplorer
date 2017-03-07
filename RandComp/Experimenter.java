package RandComp;

import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.*;
import java.io.*;
import java.lang.annotation.Annotation;
import org.reflections.Reflections;
import org.reflections.scanners.*;
public class Experimenter implements Runnable{

	public static final int ERROR_POINTS = 1000;

	static final int MAX_RUN_TIME = 10000;
	
	
	String inputClassName, experimentClassName,fallibleMethodName;
	int errorPoint, runName, experimentSize;	

	boolean experimentRunning, sdcError = true;

	Exception nonSDCError;

	static ArrayList<Location> finalLocationsWithScores = new ArrayList<>();
	static ArrayList<Location> finalNonSDCLocations = new ArrayList<>();
		
	Location locLocation;

	static final int numThreads = 16;

	static String imageRootDirectory = "./output_images/";
	static String rawRootDirectory = "./raw_output/";
	static String processedRootDirectory = "./output/";

	static List<String> FallibleMethods = new ArrayList<String>();


	public static void main(String args[]){
		if(args[0].equals("h") || args[0].equals("H")){
			System.out.println("Welcome to the RandomComputation Service");
			System.out.println("There are two ways to use this service: \n" +
					"(1) java.jar RandJava.jar Input_Object Main_Object  OR \n"+
				  "(2) java.jar RandJava.jar Input_Object Main_Object Images_Directory " + 
					"Raw_Data_Directory Processed_Data_Directory");
		} else {

			String inputClassName = args[0]; 
			String experimentClassName = args[1];	

			if(args.length == 5){
				imageRootDirectory = args[2];
				rawRootDirectory = args[3];
				processedRootDirectory = args[4];
			}

			Experimenter.runIds = new ArrayList<>();		

			try{
				testInputObjects(inputClassName, experimentClassName);
				initialize(inputClassName);
				initialize(experimentClassName);

				//FallibleMethods = getMethodsAnnotatedWith(Randomize.class);

				RunTimeTriple<Long>[][] rTime = getRunTimes(inputClassName, experimentClassName);
				printRunTimes(rTime);
				changeToExperimentTime(rTime);
				printRunTimes(rTime);
				printFallibleMethods();	
				//runExperiments(inputClassName, experimentClassName, rTime);

				ArrayList<EpsilonProbability> probabilityShapes = getProbabilityShapes();

				for(EpsilonProbability probabilityShape : probabilityShapes){
					getAverageError(probabilityShape, inputClassName, experimentClassName);
				}

			} catch (IllegalArgumentException E){
				System.out.println("illegal argument exception at top level of experimenter program");
				System.out.println(E);
			} catch (InterruptedException E){
				System.out.println(E);
			}
		}

	}

	static ArrayList<EpsilonProbability> getProbabilityShapes(){
		ArrayList<EpsilonProbability> out = new ArrayList<EpsilonProbability>();
		out.add(new NullEpsilon());
		return out;
	}

	public static void getAverageError(EpsilonProbability probabilityShape,
			String inputClassName,
			String experimentClassName)
			throws InterruptedException{
		
		RandomMethod.eProbability = probabilityShape;
		RandomMethod.epsilonTest = true;

		System.out.print("Getting Average Error on Epsilon Probability Distribution");
		probabilityShape.printName();	

		finalLocationsWithScores = new ArrayList<>();
		RandomMethod.clearAspect();
		for(int q = 10, c = 0; q <= 1000; q *= 10, c ++){
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

			for(double probability = 0; probability <= 0.1; probability += 0.01){
				probabilityShape.setProbability(probability);


				for(int j = 0; j < 1000; j ++){ 
					
					long runName = j + (long)(probability*10000.0*1000.0);
					if(runName % 100 == 0){
						System.out.println("Now on runtime: " + j);
					}
					while(threadQueue.size() >= 8){}
					while(checkThreadQueue.size() >= 8){}
					Experimenter exp = new Experimenter(inputClassName,
						experimentClassName, 
						(int) runName, j, q, true, "All");
					Future theFuture = thePool.submit(exp);
					CheckFuture cf = new CheckFuture(theFuture);
					checkThread.submit(cf);
					//Thread thread = new Thread(exp);
					//threads.add(thread);
					//thread.start();
				}
			}
		
			thePool.shutdown();
			while (!thePool.awaitTermination(60, TimeUnit.SECONDS)) {
				  System.out.println("Awaiting completion of threads.");
			}
		
			System.out.println("Printing Average Error Data data for size: " + q);	
		
			printAverageErrorData(q);

			finalLocationsWithScores = new ArrayList<>();
			RandomMethod.clearAspect();
		}
	}

	public static void printAverageErrorData(int inputSize){
			printAllProcessedData(
					new DataEnsemble<EnsTriple>(finalLocationsWithScores,EnsTriple::new ), inputSize);	
			
	}

	private static void printFallibleMethods(){
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

	static void printRunTimes(RunTimeTriple<Long>[][] rTime){
				for(int i = 0; i < rTime.length; i++){
					for(int j = 0; j < rTime[i].length; j ++){
						rTime[i][j].print();
					}
				}
	}

	private static synchronized void addLocationWithScores(Location in){
		finalLocationsWithScores.add(in);
	}

	private static synchronized void addNonSDCLocation(Location in){
		finalNonSDCLocations.add(in);
	}

	Experimenter(String inputClassName, 
		String experimentClassName,
		int runName, 
		int errorPoint,
		int experimentSize,
		boolean experimentRunning,
		String fallibleMethodName){

		this.inputClassName = inputClassName;
		this.experimentClassName = experimentClassName;
		this.errorPoint = errorPoint;
		this.runName = runName;
		this.experimentSize = experimentSize;
		this.experimentRunning = experimentRunning;
		this.fallibleMethodName = fallibleMethodName;
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

	void gleanExperimenterLocation(){
		if(locLocation != null 
				&& errorObject != null
				&& correctObject != null) {
				
				locLocation.addScores(errorObject.scores(correctObject));
				addLocationWithScores(locLocation);
			if(!sdcError) {
				Score[] scores = new Score[1];
				scores[0] = ScorePool.nullScore(nonSDCError);
				locLocation.addScores(scores);
				addNonSDCLocation(locLocation);
			}
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

		gleanExperimenterLocation();
	}

	
	static void changeToExperimentTime(RunTimeTriple<Long>[][] in){
		for(int i = 0; i < in.length; i ++){
			for(int j = 0; j < in[i].length; j ++){
				System.out.println((in[i][j].runTime*in[i][j].errorPoints));
				if (in[i][j].runTime > 0 && in[i][j].errorPoints>0) {
					in[i][j].runTime = 
						Math.min(24*60*60*ERROR_POINTS/(in[i][j].runTime*ERROR_POINTS), (long)ERROR_POINTS);
						
				} else {
					in[i][j].runTime = 0L;
				}
			}
		}
	}

	static RunTimeTriple<Long>[][] getRunTimes(String inputClassName, String experimentClassName) throws InterruptedException{
		RunTimeTriple<Long>[] nearOut = new RunTimeTriple[3];

		for(int q = 10, c = 0; q <= 1000; q *= 10, c ++){		
			long avgRunTime = System.currentTimeMillis();
			ArrayList<Thread> threads = new ArrayList<>();
			
				for(int i = 0; i < 10; i ++){
					Experimenter exp = new Experimenter(inputClassName,
						experimentClassName, i, i, q, false, "All");
					Thread thread = new Thread(exp);
					threads.add(thread);
					thread.start();
				}
				for(Thread t : threads){
					t.join(MAX_RUN_TIME);
					if(t.isAlive()) System.out.println("interupting " + t.toString()); t.interrupt();
				}
				avgRunTime = (System.currentTimeMillis() - avgRunTime)/10;
				nearOut[c] = new RunTimeTriple<Long>(avgRunTime,
						(long)RandomMethod.getAverageTimeCount());
				System.out.println("The average run time is: " + avgRunTime);
				System.out.println("The average error point is: " + RandomMethod.getAverageTimeCount());
				RandomMethod.clearAspect();
			//Thread.sleep(10000);
		}
		RunTimeTriple<Long>[][] out = runTimeBoost(nearOut);

		return out;
	}

	static RunTimeTriple<Long>[][] runTimeBoost(RunTimeTriple<Long>[] rtt){
		RunTimeTriple<Long>[][] out = new RunTimeTriple[FallibleMethods.size()][rtt.length];
		for(int j = 0; j < FallibleMethods.size(); j ++){
			for(int i = 0; i < rtt.length; i ++){
				out[j][i] = new RunTimeTriple(rtt[i]);
				out[j][i].name = FallibleMethods.get(j);
			}
		}
		return out;
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

	static void runExperiments(String inputClassName, 
			String experimentClassName, 
			RunTimeTriple<Long>[][] rTimes) throws InterruptedException{


		finalLocationsWithScores = new ArrayList<>();
		RandomMethod.clearAspect();

		RunTimeTriple<Long>[][] out = new RunTimeTriple[FallibleMethods.size()][3];

		for(int q = 10, c = 0; q <= 1000; q *= 10, c ++){	
			System.out.println("Experimenting on size: " + q);	

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

			//System.out.println("there are " + rTimes[c].errorPoints + " errorpoints");
			for(int fallmeth = 0; fallmeth < FallibleMethods.size(); fallmeth ++){
				for(int i = 0; i < rTimes[fallmeth][c].runTime; i ++){
					for(int j = 0; j < Math.min(ERROR_POINTS, rTimes[fallmeth][c].errorPoints); j ++){ 
						//rTimes[c].errorPoints; j ++){
						long runName = 
							fallmeth*rTimes[fallmeth][c].runTime
							*Math.min(ERROR_POINTS, rTimes[fallmeth][c].errorPoints)
							+ i*Math.min(ERROR_POINTS, rTimes[fallmeth][c].errorPoints) 
							+ j;
						if(runName % 
								((rTimes[fallmeth][c].runTime*
									Math.min(ERROR_POINTS, rTimes[fallmeth][c].errorPoints))/100) == 0){
							System.out.println("Now on fallible method: " + 
									FallibleMethods.get(fallmeth) + 
									"runtime: " + i + " and errorPoint " + j);
						}
						while(threadQueue.size() >= 8){}
						while(checkThreadQueue.size() >= 8){}
						if(FallibleMethods.get(fallmeth) == null){
							System.out.println("well the fallmeth: " + fallmeth + "gives us a null method");

						}
						Experimenter exp = new Experimenter(inputClassName,
							experimentClassName, 
							(int) runName, j, q, true, FallibleMethods.get(fallmeth));
						Future theFuture = thePool.submit(exp);
						CheckFuture cf = new CheckFuture(theFuture);
						checkThread.submit(cf);
						//Thread thread = new Thread(exp);
						//threads.add(thread);
						//thread.start();
					}
				}
			}

			

			thePool.shutdown();
			while (!thePool.awaitTermination(60, TimeUnit.SECONDS)) {
				  System.out.println("Awaiting completion of threads.");
			}
		
			System.out.println("Printing data for size: " + q);	
			printAllRawData(finalLocationsWithScores, q);
			printAllProcessedData(
					new DataEnsemble<EnsTriple>(finalLocationsWithScores,EnsTriple::new ), q);	
			printAllNoSDCData(
					new DataEnsemble<NoSDCEnsTriple>(finalNonSDCLocations, NoSDCEnsTriple::new), q);
			

			finalLocationsWithScores = new ArrayList<>();
			RandomMethod.clearAspect();
			
			//printAspect();
		}
	}



	private static void clearOutputOnInputSize (String directoryName, 
			int input_size, 
			String fileTerminal){
		File directory = new File(directoryName);
		for(File f: directory.listFiles())
	    if(f.getName().endsWith(input_size + fileTerminal))
				f.delete();
	}


	private static String createFile(String directory,
			String scoreName,
			String locationName,
			int inputSize){
			return directory + 
						scoreName + "_on_" +
						locationName + "_" +
						inputSize;
	}

	private static void printOutput(String scoreName, 
			double score, 
			Double stdErr,
			Integer count,
			String locationName, 
			double location, 
			String rootDirectoryName,
			int inputSize) {
		try(FileWriter fw = 
					new FileWriter(createFile(rootDirectoryName, 
						scoreName,
						locationName,
						inputSize) + ".csv", true);

				BufferedWriter bw = 
					new BufferedWriter(fw);
				PrintWriter out = 
					new PrintWriter(bw))
		{
			if(stdErr == null)
				out.println(location + ", " + score);	
			else
				out.println(location + ", " + score + ", " + stdErr + ", " + count);
		} catch (IOException E){
			System.out.print(E);
		}

	}

	private static void printAllRawData (ArrayList<Location> outputLocations, int inputSize){
		clearOutputOnInputSize(rawRootDirectory, inputSize, ".csv");
	
		for(int i = 0; i < outputLocations.size(); i ++){
			Score[] scores = outputLocations.get(i).get_scores();
			ArrayList<DefinedLocation> locations = outputLocations.get(i).dLocations;
			for(int j = 0; j < scores.length; j++) {
				for(int k = 0; k < locations.size(); k ++){
					if(locations.get(k).pertinent)
						if(locations.get(k).location > ERROR_POINTS)
							outputLocations.get(i).print();
						//System.out.println("bob is here");
						//scores[j].print();
						//System.out.println("name " + scores[j].name);
						//System.out.println("location name " + locations.get(k).name);
						//System.out.println("location value " + locations.get(k).location);
						//System.out.println("rawRootDirectory " + rawRootDirectory);
						//System.out.println("input size "  + inputSize);

						printOutput(scores[j].name,
								scores[j].score,
								null,
								null,
								locations.get(k).name,
							 	locations.get(k).location,
								rawRootDirectory,
								inputSize);
				}
			}
		}
	}

	private static String cleanString(String in){
		return  in.replaceAll("\\$","\\\\\\$").replaceAll("\\ ", "\\\\ ");
	}
	
	private static void printNoSDCGraph(String scoreName, String locationName,
																	String outputDirectory,
																	int inputSize,
																	double[][] plottable){

		String outputName = createFile(outputDirectory,
																		scoreName,
																		locationName,
																		inputSize) +  ".png";

		System.out.println("creating no SDC Count pdfs for " + outputName);
		NoSDCPlotter plotter = new NoSDCPlotter(outputName, 
										scoreName,
										locationName,
										plottable);
		plotter.plot();		
	}

	private static void printGraph(String scoreName, String locationName,
																	String inputDirectory, 
																	String outputDirectory,
																	int inputSize,
																	double[][] plottable){

		String outputName = createFile(outputDirectory,
																		scoreName,
																		locationName,
																		inputSize) +  ".png";

		String numRunsOutputName = createFile(outputDirectory,
																		scoreName,
																		locationName,
																		inputSize) + "_Num_Runs" + ".png";


		System.out.println("creating pdfs for " + outputName);
		Plotter plotter = new Plotter(outputName, 
										scoreName,
										locationName,
										plottable);
		plotter.plot();		
		
		RunsPlotter plotter2 = new RunsPlotter(numRunsOutputName, 
										scoreName,
										locationName,
										plottable);
		plotter2.plot();		
		
	}
	private static void printAllNoSDCData(DataEnsemble<NoSDCEnsTriple> dataEnsemble, 
			int inputSize
			){

		for(int i = 0; i < dataEnsemble.scores.size(); i ++) {
			DataEnsemble<NoSDCEnsTriple>.EnsScore score = dataEnsemble.scores.get(i);
			for(int j = 0; j < score.locations.size(); j ++){
				DataEnsemble<NoSDCEnsTriple>.EnsLocation location = score.locations.get(j);
				double[][] theData = new double[location.triples.size()][2];
				for(int q = 0; q < location.triples.size(); q ++) {
					NoSDCEnsTriple triple = location.triples.get(q);
					theData[q][0] = triple.location;
					theData[q][1] = triple.count;
				}
		
				//clearOutputOnInputSize(imageRootDirectory, inputSize, ".png");
				printNoSDCGraph(score.name, 
						location.name,  
						imageRootDirectory,
						inputSize,
						theData);
			}
		}

	}

	private static void printAllProcessedData(DataEnsemble<EnsTriple> dataEnsemble, 
			int inputSize
			){

		clearOutputOnInputSize(processedRootDirectory, inputSize, ".csv");
		for(int i = 0; i < dataEnsemble.scores.size(); i ++) {
			DataEnsemble<EnsTriple>.EnsScore score = dataEnsemble.scores.get(i);
			for(int j = 0; j < score.locations.size(); j ++){
				DataEnsemble<EnsTriple>.EnsLocation location = score.locations.get(j);
				double[][] theData = new double[location.triples.size()][4];
				for(int q = 0; q < location.triples.size(); q ++) {
					EnsTriple triple = location.triples.get(q);
					printOutput(score.name,
						triple.avg,
						triple.stdErr,
						triple.count,
						location.name,
						triple.location,
						processedRootDirectory,
						inputSize);
					theData[q][0] = triple.location;
					theData[q][1] = triple.avg;
					theData[q][2] = triple.stdErr;
					theData[q][3] = triple.count;
				}
		
				//clearOutputOnInputSize(imageRootDirectory, inputSize, ".png");
				printGraph(score.name, 
						location.name, 
						processedRootDirectory, 
						imageRootDirectory,
						inputSize,
						theData);
			}
		}

	}

	private static void testInputObjects(String inputClassName, String experimentClassName) throws IllegalArgumentException {
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
