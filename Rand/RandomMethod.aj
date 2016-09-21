import java.util.*;

public aspect RandomMethod{
	Random rand;

	pointcut callMaxFlowFF(Graph bob): call(* *.maxFlowFF(Graph)) && args(bob);

	before(Graph bob) : callMaxFlowFF(bob) {
		System.out.println("barf");
	}

	after(Graph bob) returning (double cow): callMaxFlowFF(bob) {
		System.out.println("doubleBarf " + cow);
		bob.print();
	}
}
