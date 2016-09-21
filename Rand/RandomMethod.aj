import java.util.*;
import java.lang.reflect.*;

public aspect RandomMethod{
	Random rand = new Random();

	pointcut Randomize(): call(@Randomize * *(..));

	Object around() : Randomize() {
		Object targetObject = thisJoinPoint.getTarget();
		System.out.println(thisJoinPoint.getTarget().toString());
    	final Object[] args = thisJoinPoint.getArgs();
		for(Object arg : args){
			System.out.print("the arguments: " + arg);
		}
		System.out.println();
		System.out.println(thisJoinPointStaticPart.getSignature().getName());
		
		try {
			Method m = targetObject.getClass().getMethod(thisJoinPointStaticPart.getSignature().getName()+"Rand");

			m.invoke(rand, targetObject);
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
