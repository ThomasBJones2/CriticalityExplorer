import java.util.*;
import java.lang.reflect.*;
import java.lang.Thread;

public class Experimenter implements Runnable {

	String inputClassName, experimentClassName;

	Experimenter(String inputClassName, String experimentClassName){
		this.inputClassName = inputClassName;
		this.experimentClassName = experimentClassName;
	}

	@Override
	public void run() {
		System.out.println("Starting " + Thread.currentThread().getId());
		Random rand = new Random();
		Experiment errorObject = (Experiment)getNewObject(experimentClassName);
		Experiment correctObject = (Experiment)getNewObject(experimentClassName);
		Input iObject1 = (Input)getNewInputObject(inputClassName,10);
		Input iObject2 = (Input)getNewObject(inputClassName);
		iObject1.randomize(rand);
		iObject2.copy(iObject1);
		correctObject.experiment(iObject1);
		errorObject.experiment(iObject2);
		System.out.println("Done " + Thread.currentThread().getId());
	}

	public static void main(String[] args){
		String inputClassName = args[0]; 
		String experimentClassName = args[1];			

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
					experimentClassName);
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
