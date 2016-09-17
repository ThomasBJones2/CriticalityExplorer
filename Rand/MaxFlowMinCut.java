import java.util.*;

public class MaxFlowMinCut{
	private class GraphNode{
		ArrayList<GraphLink> links;
	}
	
	private class GraphLink{
		private double weight;
		GraphNode target;
	
		//@RandMethod(getRandomWeight())
		double getWeight() {
			return weight;
		}
	
	}

	double maxFlow(){
		return 0.0;
	}

}
