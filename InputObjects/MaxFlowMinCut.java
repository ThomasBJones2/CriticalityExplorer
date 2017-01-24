package InputObjects;

import RandComp.*;
import java.util.*;

public class MaxFlowMinCut implements Experiment<Graph, MaxFlowMinCut>{
	double flow;

	Graph theGraph;


	public static MaxFlowMinCut emptyObject(){return new MaxFlowMinCut();}

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

	public ArrayList<DefinedLocation> getCurrentLocations(){
		return theGraph.getCurrentLocations();
	}

	public Score[] scores(MaxFlowMinCut correctObject){
		//if(correctObject instanceof MaxFlowMinCut
		Score[] out = new Score [2];
		out[0] = ScorePool.absolutePercentValue(this.flow, correctObject.flow);
		out[1] = ScorePool.absoluteValue(this.flow, correctObject.flow);
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
