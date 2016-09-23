import java.util.*;
import java.lang.reflect.*;

public class Experimenter {

	public static void main(String[] args){
		String inputClassName = args[0]; 
		String experimentClassName = args[1];
		Random rand = new Random();
		try{
			Object inputClass = getNewObject(inputClassName);
			Object experimentClass = getNewObject(experimentClassName);
			if(!(experimentClass instanceof Experiment)) {
				throw new IllegalArgumentException();
			}
			if(!(inputClass instanceof Input)){
				throw new IllegalArgumentException();
			}

			long avgRunTime = System.nanoTime();
			for(int i = 0; i < 10; i ++){
				Experiment errorObject = (Experiment)getNewObject(experimentClassName);
				Experiment correctObject = (Experiment)getNewObject(experimentClassName);
				Input iObject1 = (Input)getNewInputObject(inputClassName,100);
				Input iObject2 = (Input)getNewObject(inputClassName);
				iObject1.randomize(rand);
				iObject2.copy(iObject1);
				correctObject.experiment(iObject1);
				errorObject.experiment(iObject2);
			}
			avgRunTime = (System.nanoTime() - avgRunTime)/10; 
			System.out.println("The average run time is: " + avgRunTime);


		} catch (IllegalArgumentException E){
			System.out.println("The object " + experimentClassName + " is not experimentable, please extend the experiment class.");
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
