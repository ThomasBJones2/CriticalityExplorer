import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class Experimenter implements Runnable {

	String inputClassName, experimentClassName;
	int errorPoint, runName;

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

	Experimenter(String inputClassName, 
		String experimentClassName,
		int runName, 
		int errorPoint){
		this.inputClassName = inputClassName;
		this.experimentClassName = experimentClassName;
		this.errorPoint = errorPoint;
		this.runName = runName;
	}

	private void printAspect(){}

	private Experiment runObject(Input input, Experiment experiment, Random rand, boolean errorful){
		RunId curId = new RunId(2*runName + (errorful?1:0), 
			true,
			errorful, 
			errorPoint, 
			Thread.currentThread().getId(), 
			rand);
		addId(curId);
		experiment.experiment(input);
		printAspect();
		removeId(curId);
		return experiment;
	}

	@Override
	public void run() {
		long seed = new Random().nextLong();
		long inputSeed = new Random().nextLong();
		System.out.println("Starting " + 
			Thread.currentThread().getId() + 
			" on seed: " + seed + " and input seed " + inputSeed);
		Random rand = new Random(inputSeed);
		Random rand1 = new Random(seed);
		Random rand2 = new Random(seed);

		RunId curId = new RunId(Thread.currentThread().getId());
		curId.setExperiment(false);
		addId(curId);
		Experiment errorObject = (Experiment)getNewObject(experimentClassName);
		Experiment correctObject = (Experiment)getNewObject(experimentClassName);
		Input iObject1 = (Input)getNewInputObject(inputClassName,10);
		Input iObject2 = (Input)getNewObject(inputClassName);
		iObject1.randomize(rand);
		iObject2.copy(iObject1);
		removeId(curId);

		correctObject = runObject(iObject1, correctObject, rand1, false);
		errorObject = runObject(iObject2, errorObject, rand2, true);

		System.out.println("The error was: " + errorObject.scores(correctObject)[0].getScore());
		
		System.out.println("Done " + Thread.currentThread().getId());
	}

	public static void main(String args[]){
		String inputClassName = args[0]; 
		String experimentClassName = args[1];	

		Experimenter.runIds = new ArrayList<>();		

		try{
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

			long avgRunTime = System.nanoTime();
			ArrayList<Thread> threads = new ArrayList<>();
			for(int i = 0; i < 10; i ++){
				Experimenter exp = new Experimenter(inputClassName,
					experimentClassName, i, i);
				Thread thread = new Thread(exp);
				threads.add(thread);
				thread.start();
			}
			for(Thread t : threads){
				t.join();
			}
			avgRunTime = (System.nanoTime() - avgRunTime)/10; 
			System.out.println("The average run time is: " + avgRunTime);


		} catch (IllegalArgumentException E){
			System.out.println(E);
		} catch (InterruptedException E){
			System.out.println(E);
		}

	}

	static Object getNewInputObject(String inName, int size){
		try {
			Class cls = Class.forName(inName);
			Constructor ctor = cls.getConstructor(int.class);
			Object clsInstance = ctor.newInstance(size);		
			return clsInstance;
		} catch (ClassNotFoundException E) {
			System.out.println(inName + " is not a class, please add to the classpath: " + E);
		} catch (InstantiationException E) {
			System.out.println("Trouble instantiating " + inName + ": " + E);
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
			Object clsInstance = (Object) cls.newInstance();		
			return clsInstance;
		} catch (ClassNotFoundException E) {
			System.out.println(inName + " is not a class, please add to the classpath: " + E);
		} catch (InstantiationException E) {
			System.out.println("Trouble instantiating " + inName + ": " + E);
		} catch (IllegalAccessException E) {
			System.out.println("Trouble accessing " + inName + ": " + E);
		}
		return null;
	}
}
