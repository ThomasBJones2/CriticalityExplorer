import java.util.*;
import java.lang.reflect.*;

public aspect RandomMethod{
	Random rand = new Random();

	pointcut Randomize(): call(@Randomize * *(..));

	Object around() : Randomize() {
		Object targetObject = thisJoinPoint.getTarget();    	
		final Object[] args = thisJoinPoint.getArgs();
/*		for(Object arg : args){
			System.out.println(arg.getClass());
		}
		System.out.println(thisJoinPoint.getTarget().toString());

		for(Object arg : args){
			System.out.print("the arguments: " + arg);
		}
		System.out.println();
		System.out.println(thisJoinPointStaticPart.getSignature().getName());
		
		System.out.println(targetObject.getClass().toString());
		for(Method m : targetObject.getClass().getDeclaredMethods()){
			System.out.println(m.toString());
		}
		System.out.println();
		for(Method m : Graph.class.getMethods()){
			System.out.println(m.toString());
		}*/

		if(rand.nextDouble() < 0.75)
		try {
			Class[] types = new Class[args.length + 1];
			types[0] = Random.class;
			for(int i = 1; i < args.length + 1; i ++){
				types[i] = args[i - 1].getClass();
			}

			Object[] arguments = new Object[args.length + 1];
			arguments[0] = rand;
			for(int i = 1; i < args.length + 1; i ++){
				arguments[i] = args[i - 1];
			}

			Method m = targetObject.getClass().getDeclaredMethod(thisJoinPointStaticPart.getSignature().getName() + "Rand", types);

			return m.invoke(targetObject, arguments);
		} catch (IllegalAccessException e) {
			System.out.println("IllegalAccessException " + e);
		} catch (InvocationTargetException e) {
			System.out.println("InvocationTargetException " + e);
		} catch (NoSuchMethodException e) {
			System.out.println("NoSuchMethodException " + e);
		}



		return proceed();
	}

/*	after() returning (double cow): callMaxFlowFF() {
		System.out.println("doubleBarf " + cow);
	}

	pointcut callMaxFlowFF2(): call(!@Randomize double *()) ;

	after() returning (double cow): callMaxFlowFF2() {
		System.out.println("superbarf " + cow);
	}*/
}
