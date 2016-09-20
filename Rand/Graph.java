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
	
		double findPathValueAndDecrement(){
			clearLookBack();			
			nodes.get(0).lookBack.setWeight(Double.MAX_VALUE);
			ArrayList<GraphNode> queue = new ArrayList<>();
			queue.add(nodes.get(0));
			while(queue.size() > 0){
				GraphNode nextNode = queue.get(0);
				queue.remove(0);
				for(GraphLink link : nextNode.links){
					GraphNode targetNode = link.getTarget();
					if(targetNode.lookBack == null && !queue.contains(targetNode)){
						queue.add(targetNode);
						targetNode.lookBack = new GraphLink(nextNode, Math.max(0, 
							Math.min(link.getWeight(), 
								nextNode.lookBack.getWeight())));
					}
				}
			}
			double out = nodes.get(nodes.size() - 1).lookBack.getWeight();
			decrementAlongPath(out);
			return out;	
		}

		void decrementAlongPath(double out){
			GraphNode curNode = nodes.get(nodes.size() - 1);
			if(out <= 0){
				return;
			} else {
				while(curNode.name != 0){
					GraphNode prevNode = curNode;
					curNode = curNode.lookBack.getTarget();
					GraphLink link = curNode.getLinkFromNode(prevNode);
					link.setWeight(link.getWeight() - out);
				}
			}
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
				for(GraphLink link : links){
					if(link.getTarget().name == in.name)
						return link;
				}
				return null;
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
