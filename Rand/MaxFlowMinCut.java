import java.util.*;

public class MaxFlowMinCut{
	private class GraphNode{
		ArrayList<GraphLink> links;

		GraphNode(ArrayList<GraphLink> links){
			this.links = new ArrayList<>();
			for(GraphLink in : links){
				this.links.add(new GraphLink(in));
			}
		}
		
		void generate(int n){

		}
	}
	
	private class GraphLink{
		private double weight;
		GraphNode target;
	
		//@RandMethod(getRandomWeight())
		double getWeight() {
			return weight;
		}

		GraphLink(GraphLink in){
			this.target = in.target;
			this.weight = in.weight;
		}
	}

	double maxFlow(){
		return 0.0;
	}

}
