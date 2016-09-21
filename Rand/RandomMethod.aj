public aspect RandomMethod{
	pointcut callMaxFlowFF(): call(* MaxFlowMinCut.maxFlowFF(Graph));

	before() : callMaxFlowFF() {
		System.out.println("barf");
	}

	after() : callMaxFlowFF() {
		System.out.println("doubleBarf");
	}
}
