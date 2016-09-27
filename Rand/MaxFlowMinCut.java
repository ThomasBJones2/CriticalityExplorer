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

	public void experiment(Graph input){
		//input.print();
		flow = maxFlowFF((Graph) input);
	}

	public Score[] scores(MaxFlowMinCut correctObject){
		//if(correctObject instanceof MaxFlowMinCut
		Score[] out = new Score [2];
		out[0] = new Score(Math.abs(this.flow - correctObject.flow), "Absolute Value"); 
		out[1] = new Score(correctObject.flow > 0?
			Math.abs(this.flow - correctObject.flow)/correctObject.flow:0, 
			"Absolute Percent Value"); 
		return out; 
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
