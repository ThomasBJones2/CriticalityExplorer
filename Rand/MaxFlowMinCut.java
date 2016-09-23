import java.util.*;

public class MaxFlowMinCut implements Experiment{

	static double maxFlowFF(Graph inGraph){
		double auxFlow = 1;
		double out = 0;		
		while(auxFlow > 0) {
			auxFlow = inGraph.findPathValueAndDecrement();		
			out += auxFlow;
		}
		return out;
	}

	static void printAspect(){}

	public void experiment(){
		Graph theGraph = new Graph(10);
		Random rand = new Random();
		theGraph.randomize(rand);
		theGraph.print();
		double maxFlow = maxFlowFF(theGraph);
		System.out.println("The MaxFlow is: " + maxFlow);
		printAspect();
	}

}
