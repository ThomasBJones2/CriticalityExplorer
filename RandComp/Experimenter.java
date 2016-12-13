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
public class Experimenter implements Runnable {

	public static final int ERROR_POINTS = 1000;

	String inputClassName, experimentClassName,fallibleMethodName;
	int errorPoint, runName, experimentSize;	

	boolean experimentRunning;

	static ArrayList<Distance> finalDistancesWithScores = new ArrayList<>();

	Distance locDistance;

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
			
				//FallibleMethods = getMethodsAnnotatedWith(Randomize.class);

				RunTimeTriple<Long>[][] rTime = getRunTimes(inputClassName, experimentClassName);
				printRunTimes(rTime);
				changeToExperimentTime(rTime);
				printRunTimes(rTime);
				runExperiments(inputClassName, experimentClassName, rTime);

			} catch (IllegalArgumentException E){
				System.out.println(E);
			} catch (InterruptedException E){
				System.out.println(E);
			}
		}

	}

	static void printRunTimes(RunTimeTriple<Long>[][] rTime){
				for(int i = 0; i < rTime.length; i++){
					for(int j = 0; j < rTime[i].length; j ++){
						rTime[i][j].print();
					}
				}
	}

	private static synchronized void addDistanceWithScores(Distance in){
		finalDistancesWithScores.add(in);
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

	private Experiment runObject(Input input, Experiment experiment, Random rand, boolean errorful, Experimenter exper){
		RunId curId = new RunId(2*runName + (errorful?1:0), 
			true,
			errorful, 
			errorPoint, 
			Thread.currentThread().getId(), 
			rand,
			exper.fallibleMethodName);
		addId(curId);
		RandomMethod.createDistance(curId);
		if (exper.experimentRunning == false)
			System.out.println("The error point is: " + errorPoint + " " + errorful);

		experiment.experiment(input);
		
		RandomMethod.registerTimeCount();
		if(errorful){
			exper.locDistance = RandomMethod.getDistance(curId).getBurnIn();
		}
		RandomMethod.clearDistance(curId);
		removeId(curId);
		return experiment;
	}

	void printDistancesAndScores(Experiment correctObject, Experiment errorObject){
		locDistance.print();		
		Score[] scores = errorObject.scores(correctObject);
		for(Score s : scores){
			s.print();
		}
	}

	@Override
	public void run() {
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
		Experiment errorObject = (Experiment)getNewObject(experimentClassName);
		Experiment correctObject = (Experiment)getNewObject(experimentClassName);
		Input iObject1 = (Input)getNewInputObject(inputClassName, experimentSize);
		Input iObject2 = (Input)getNewObject(inputClassName);
		iObject1.randomize(rand);
		iObject2.copy(iObject1);
		removeId(curId);

		correctObject = runObject(iObject1, correctObject, rand1, false, this);
		errorObject = runObject(iObject2, errorObject, rand2, true, this);
		
		if(this.locDistance != null) {
			this.locDistance.addScores(errorObject.scores(correctObject));
			addDistanceWithScores(locDistance);
		}

		/* This is how you print scores out here...
		 *
		System.out.println("The errors were: \n");
		for(int q = 0; q < errorObject.scores(correctObject).length; q ++)
			System.out.println(	
				errorObject.scores(correctObject)[q].getName() + " " + 
				errorObject.scores(correctObject)[q].getScore()
				);
		*/
		//System.out.println("Done " + Thread.currentThread().getId());
	}

	
	static void changeToExperimentTime(RunTimeTriple<Long>[][] in){
		for(int i = 0; i < in.length; i ++){
			for(int j = 0; j < in[i].length; j ++){
				System.out.println((in[i][j].runTime*in[i][j].errorPoints));
				if (in[i][j].runTime > 0 && in[i][j].errorPoints>0) {
					in[i][j].runTime = 
						Math.min(60*60*1000/(in[i][j].runTime*ERROR_POINTS), (long)1000);
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
					t.join(10000);
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

	static void runExperiments(String inputClassName, 
			String experimentClassName, 
			RunTimeTriple<Long>[][] rTimes) throws InterruptedException{
		RunTimeTriple<Long>[][] out = new RunTimeTriple[FallibleMethods.size()][3];

		for(int q = 10, c = 0; q <= 1000; q *= 10, c ++){	
			System.out.println("Experimenting on size: " + q);	

			ArrayBlockingQueue<Runnable> threadQueue = 
				new ArrayBlockingQueue<Runnable>(100);
			ThreadPoolExecutor thePool = 
				new ThreadPoolExecutor(numThreads,
					numThreads,
					0, 
					TimeUnit.SECONDS,
					threadQueue);
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
								((rTimes[fallmeth][c].runTime*rTimes[fallmeth][c].errorPoints)/10) == 0){
							System.out.println("Now on fallible method: " + 
									FallibleMethods.get(fallmeth) + 
									"runtime: " + i + " and errorPoint " + j);
						}
						while(threadQueue.size() >= 100){					
						}
						Experimenter exp = new Experimenter(inputClassName,
							experimentClassName, 
							(int) runName, j, q, true, FallibleMethods.get(fallmeth));
						thePool.execute(exp);

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
			printAllRawData(finalDistancesWithScores, q);
			printAllProcessedData(new DataEnsemble(finalDistancesWithScores), q);	
			
			finalDistancesWithScores = new ArrayList<>();
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
			String distanceName,
			int inputSize){
			return directory + 
						scoreName + "_on_" +
						distanceName + "_" +
						inputSize;
	}

	private static void printOutput(String scoreName, 
			double score, 
			Double stdErr,
			String distanceName, 
			double distance, 
			String rootDirectoryName,
			int inputSize) {
		try(FileWriter fw = 
					new FileWriter(createFile(rootDirectoryName, 
						scoreName,
						distanceName,
						inputSize) + ".csv", true);

				BufferedWriter bw = 
					new BufferedWriter(fw);
				PrintWriter out = 
					new PrintWriter(bw))
		{
			if(stdErr == null)
				out.println(distance + ", " + score);	
			else
				out.println(distance + ", " + score + ", " + stdErr);
		} catch (IOException E){
			System.out.print(E);
		}

	}

	private static void printAllRawData (ArrayList<Distance> outputDistances, int inputSize){
		clearOutputOnInputSize(rawRootDirectory, inputSize, ".csv");
	
		for(int i = 0; i < outputDistances.size(); i ++){
			Score[] scores = outputDistances.get(i).get_scores();
			ArrayList<DefinedLocation> distances = outputDistances.get(i).dDistances;
			for(int j = 0; j < scores.length; j++) {
				for(int k = 0; k < distances.size(); k ++){
					if(distances.get(k).pertinent)
						if(distances.get(k).distance > 1000)
							outputDistances.get(i).print();
						printOutput(scores[j].name,
								scores[j].score,
								null,
								distances.get(k).name,
							 	distances.get(k).distance,
								rawRootDirectory,
								inputSize);
				}
			}
		}
	}

	private static String cleanString(String in){
		return  in.replaceAll("\\$","\\\\\\$").replaceAll("\\ ", "\\\\ ");
	}

	private static void printGraph(String scoreName, String distanceName,
																	String inputDirectory, 
																	String outputDirectory,
																	int inputSize,
																	double[][] plottable){

		String outputName = createFile(outputDirectory,
																		scoreName,
																		distanceName,
																		inputSize) + ".png";


		String fileName = createFile(inputDirectory,
																		scoreName,
																		distanceName,
																		inputSize) + ".csv";
		
		System.out.println("creating pdf for " + fileName);
		Plotter plotter = new Plotter(fileName, 
										outputName, 
										scoreName,
										distanceName,
										plottable);
		plotter.plot();		
		
		/*try{
			
		

			String execStrings[] = {
				"gnuplot",
				"-e",
				"\"outname=\'" + cleanString(outputName) + "\';title=\'" 
					+ scoreName,
				"vs",
				cleanString(distanceName) + 
					"\';distance=\'" + cleanString(distanceName) 
					+ "\';error=\'" + scoreName + 
					"\';filename=\'" + cleanString(fileName) + "\'\"",
				"Graph.plt"
			};
			
			String execString = "gnuplot -e \"outname=\'" + cleanString(outputName) + "\';title=\'" 
					+ scoreName + cleanString("vs") + cleanString(distanceName) + 
					"\';distance=\'" + cleanString(distanceName) 
					+ "\';error=\'" + scoreName + 
					"\';filename=\'" + cleanString(fileName) + "\'\" Graph.plt"; 
			System.out.println(execString);	
			Runtime rt = Runtime.getRuntime();
			Process pr = rt.exec(execString);
			InputStream stderr = pr.getErrorStream();
			InputStreamReader isr = new InputStreamReader(stderr);
			BufferedReader br = new BufferedReader(isr);
			String line = null;
			System.out.println("<ERROR>");
			while((line = br.readLine()) != null)
				System.out.println(line);
			System.out.println("</ERROR>");

			System.out.println("Gnuplot exited on: " + pr.waitFor());
		} catch (Exception E){
			System.out.println(E);
		}*/
	}

	private static void printAllProcessedData(DataEnsemble dataEnsemble, int inputSize){
		clearOutputOnInputSize(processedRootDirectory, inputSize, ".csv");
		for(int i = 0; i < dataEnsemble.scores.size(); i ++) {
			DataEnsemble.EnsScore score = dataEnsemble.scores.get(i);
			for(int j = 0; j < score.distances.size(); j ++){
				DataEnsemble.EnsDistance distance = score.distances.get(j);
				double[][] theData = new double[distance.triples.size()][3];
				for(int q = 0; q < distance.triples.size(); q ++) {
					DataEnsemble.EnsTriple triple = distance.triples.get(q);
					printOutput(score.name,
						triple.avg,
						triple.stdErr,
						distance.name,
						triple.distance,
						processedRootDirectory,
						inputSize);
					theData[q][0] = triple.distance;
					theData[q][1] = triple.avg;
					theData[q][2] = triple.stdErr;
				}
								
				clearOutputOnInputSize(imageRootDirectory, inputSize, ".pdf");
				printGraph(score.name, 
						distance.name, 
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
			throw new IllegalArgumentException();
		}
		if(!(inputClass instanceof Input)){
			throw new IllegalArgumentException();
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

	public static void addToFallibleMethods(String methodName){
		if(!FallibleMethods.contains(methodName)){
			FallibleMethods.add(methodName);
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
