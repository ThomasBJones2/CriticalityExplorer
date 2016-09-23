import java.util.*;
import java.lang.reflect.*;

public class Experimenter {

	public static void main(String[] args){
		String experimentClassName = args[0]; 
		try{
			Experiment eClass = getExperimentObject(experimentClassName);

			long avgRunTime = System.nanoTime();
			for(int i = 0; i < 10; i ++){
				eClass.experiment();
			}
			avgRunTime = (System.nanoTime() - avgRunTime)/10; 
		} catch (IllegalArgumentException E){
			System.out.println("The object " + experimentClassName + " is not experimentable, please extend the experiment class.");
		}

	}

	static Experiment getExperimentObject(String experimentName) throws IllegalArgumentException{
		
		try {
			Class cls = Class.forName(experimentName);
			Object clsInstance = (Object) cls.newInstance();		
			if(!(clsInstance instanceof Experiment)){
				throw new IllegalArgumentException();
			}
			return (Experiment)clsInstance;
		} catch (ClassNotFoundException E) {
			System.out.println(experimentName + " is not a class, please add to the classpath: " + E);
		} catch (InstantiationException E) {
			System.out.println("Trouble instantiating " + experimentName + ": " + E);
		} catch (IllegalAccessException E) {
			System.out.println("Trouble accessing " + experimentName + ": " + E);
		}
		return null;
	}

}
