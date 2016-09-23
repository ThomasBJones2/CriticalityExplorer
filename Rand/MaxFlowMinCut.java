import java.util.*;

public class MaxFlowMinCut implements Experiment<Graph, MaxFlowMinCut>{
	double flow;

	public double maxFlowFF(Graph inGraph){
		double auxFlow = 1;
		double out = 0;		
		while(auxFlow > 0) {
			auxFlow = inGraph.findPathValueAndDecrement();		
			out += auxFlow;
		}
		return out;
	}

	static void printAspect(){}

	public MaxFlowMinCut experiment(Graph input){
		if(input instanceof Graph) {
			flow = maxFlowFF((Graph) input);
			return this;
		}
		else 
			throw new IllegalArgumentException();
	}

	public double score(MaxFlowMinCut correctObject, MaxFlowMinCut errorObject){
		return Math.abs(errorObject.flow - correctObject.flow)/correctObject.flow; 
	}


	/*{
		Graph firstGraph = new Graph(10);
		Random rand = new Random();		
		theGraph.randomize(rand);
		Graph secondGraph = new Graph(firstGraph);
		firstGraph.print();
		double maxFlow = maxFlowFF(firstGraph);
		System.out.println("The MaxFlow is: " + maxFlow);
		printAspect();
		return maxFlow;
	}*/
}
