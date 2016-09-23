import java.util.*;
import java.lang.reflect.*;

public class Experimenter {

	public static void main(String[] args){
		String experimentClassName = args[0];
		String inputClassName = args[1]; 
		try{
			Object inputClass = getNewObject(inputClassName);
			Object experimentClass = getNewObject(experimentClassName);
			if(!(experimentClass instanceof Experiment)) {
				throw new IllegalArgumentException();
			}
			if(!(inputClass instanceof Input)){
				throw new IllegalArgumentException();
			}

			/*long avgRunTime = System.nanoTime();
			for(int i = 0; i < 10; i ++){
				eClass.experiment();
			}
			avgRunTime = (System.nanoTime() - avgRunTime)/10; 
			*/



		} catch (IllegalArgumentException E){
			System.out.println("The object " + experimentClassName + " is not experimentable, please extend the experiment class.");
		}

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
