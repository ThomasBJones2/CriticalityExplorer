import java.util.*;

public class Graph{
		ArrayList<GraphNode> nodes;	

		Graph(int n){
			nodes = new ArrayList<>();
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
				nodes.get(i).links = copyLinksInGraph(in.nodes.get(i).links);
			}
		}

		ArrayList<GraphLink> copyLinksInGraph(ArrayList<GraphLink> in){
			ArrayList<GraphLink> out = new ArrayList<>();
			for(int i = 0; i < in.size(); i ++){
				out.add(new GraphLink(nodes.get(in.get(i).getTarget().getName()), in.get(i).getWeight()));
			}
			return out;
		}

		void clearLookBack(){
			for(GraphNode node : nodes){
				node.lookBack = null;
			}
		}

		void print() {
			System.out.println("This is the Graph");
			System.out.println("The number of nodes is " + nodes.size());
			System.out.println("the nodes are: ");
			for (GraphNode node : nodes){
				System.out.print(node.name + ": ");
				for(GraphLink link : node.links){
					System.out.print(link.getTarget().name + "," + link.getWeight() + " ");
				} 
				System.out.println();
				System.out.println();
			}
		}
	
		double findPathValueAndDecrement(){
			clearLookBack();			
			nodes.get(0).lookBack = new GraphLink(nodes.get(0), Double.MAX_VALUE);
			ArrayList<GraphNode> queue = new ArrayList<>();
			queue.add(nodes.get(0));
			while(queue.size() > 0){
				GraphNode nextNode = queue.get(0);
				queue.remove(0);
				for(GraphLink link : nextNode.links){
						if(link.getWeight() > 0) {
						GraphNode targetNode = link.getTarget();
						if(targetNode.lookBack == null && !queue.contains(targetNode)){
							queue.add(targetNode);
							targetNode.lookBack = new GraphLink(nextNode, Math.max(0, 
								Math.min(link.getWeight(), 
									nextNode.lookBack.getWeight())));
						}
					}
				}
			}
			double out = 0;
			if(nodes.get(nodes.size() - 1).lookBack != null) {
				out = nodes.get(nodes.size() - 1).lookBack.getWeight();
			}	
			decrementAlongPath(out);
			return out;	
		}

		void decrementAlongPath(double out){
			GraphNode curNode = nodes.get(nodes.size() - 1);
			if(out <= 0){
				return;
			} else {
				while(curNode.name != nodes.get(0).name){
					GraphNode prevNode = curNode;
					curNode = curNode.lookBack.getTarget();
					//System.out.println("looping infinitely." + prevNode.name + " " + curNode.name + " " + out);
					GraphLink link = curNode.getLinkFromNode(prevNode);
					link.setWeight(link.getWeight() - out);
				}
			}
			//this.print();
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

			GraphLink lookBack;

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

			GraphLink getLinkFromNode(GraphNode in){
				GraphLink out = null;
				for(GraphLink link : links){
					if(link.getTarget().name == in.name && (out == null || link.getWeight() >= out.getWeight()))
						out = link;
				}
				return out;
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

			int getName(){
				return this.name;
			}
		}
	
		private class GraphLink{
			private double weight;
			GraphNode target;
	


			GraphLink(GraphLink in){
				this.target = in.target;
				this.weight = in.weight;
			}

			GraphLink(GraphNode target, double weight){
				this.target = target;
				this.weight = weight;
			}
			
			GraphNode getTarget(){
				return this.target;
			}

			void setTarget(GraphNode in){
				this.target = in;
			}

			//@RandMethod(getRandomWeight())
			double getWeight(){
				return this.weight;
			}

			void setWeight(double weight){
				this.weight = weight;
			}
			
			double rand_getWeight(Random rand){
				return rand.nextDouble();
			}

			void deduct(double n){
				weight -= n;				
			}
		}
	}
