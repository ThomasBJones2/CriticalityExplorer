import java.util.*;

public class MaxFlowMinCut implements Experiment<Graph, MaxFlowMinCut>{
	double flow;

	Graph theGraph;

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
		theGraph = input;
		flow = maxFlowFF((Graph) input);
	}

	public ArrayList<DefinedDistance> getCurrentDistances(){
		return theGraph.getCurrentDistances();
	}

	public Score[] scores(MaxFlowMinCut correctObject){
		//if(correctObject instanceof MaxFlowMinCut
		Score[] out = new Score [2];
		out[0] = new Score(Math.abs(this.flow - correctObject.flow), "Absolute_Value"); 
		out[1] = new Score(correctObject.flow > 0?
			Math.abs(this.flow - correctObject.flow)/correctObject.flow:0, 
			"Absolute_Percent_Value"); 
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
