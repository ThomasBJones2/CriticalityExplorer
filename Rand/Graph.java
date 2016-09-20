import java.util.*;

public class Graph{
		ArrayList<GraphNode> nodes;	

		Graph(int n){
			for(int i = 0; i < n; i ++){
				nodes.add(new GraphNode(i, this));
			}

		}
		
		void randomize(Random rand){
			for(int i = 0; i < nodes.size(); i ++){
				nodes.get(i).randomize(rand);
			}
		}

		Graph(Graph in){
			this(in.nodes.size());
			for(int i = 0; i < nodes.size(); i ++){

			}
		}
	
		int findPath(GraphNode in, GraphNode out){
			return 0;
		}

		void deduct(double n){
			for(int i = 0; i < nodes.size(); i ++){
				for(int j = 0; j < nodes.get(i).links.size(); j ++){
					nodes.get(i).links.get(j).deduct(n);
				}
			}
		}

		private class GraphNode{
			int name;
			ArrayList<GraphLink> links;
			Graph theGraph;

			GraphNode(int name, ArrayList<GraphLink> inLinks, Graph inGraph){
				this.links = new ArrayList<>();
				for(GraphLink in : inLinks){
					this.links.add(new GraphLink(in));
				}
				this.name = name;
				this.theGraph = inGraph;
			}			

			GraphNode(GraphNode in){
				this.name = in.name;
				for(int i = 0; i < in.links.size(); i ++){
					this.links.add(new GraphLink(in.links.get(i)));
				}
				this.theGraph = in.theGraph;
			}

			GraphNode(int in, Graph graph){
				this.name = in;
				this.theGraph = graph;
			}
		
			void randomize(Random rand){
				this.name = rand.nextInt();
				int newLinkCount = rand.nextInt(theGraph.nodes.size());
				this.links = new ArrayList<>();				
				for(int i = 0; i < newLinkCount; i ++){
					GraphLink nextLink = new GraphLink(
						theGraph.nodes.get(rand.nextInt(theGraph.nodes.size())),
						rand.nextDouble());
					links.add(nextLink);
				}
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

			GraphLink(GraphNode target, double weight){
				this.target = target;
				this.weight = weight;
			}

			void deduct(double n){
				weight -= n;				
			}
		}
	}
