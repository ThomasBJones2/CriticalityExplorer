package com.criticalityworkbench.inputobjects;

import com.criticalityworkbench.randcomphandler.*;
import java.util.*;
 
public class Graph implements Input<Graph>{


		ArrayList<GraphNode> nodes;	
	
		double newLinkMultiplier = 0.6;

		public Graph(){}

		public static Graph emptyObject(){return new Graph();}


		public ArrayList<DefinedLocation> getCurrentLocations(){
			return new ArrayList<DefinedLocation>();
		}

		public Graph(int n){
			nodes = new ArrayList<>();
			for(int i = 0; i < n; i ++){
				nodes.add(new GraphNode(i, this));
			}
		}

		public static Graph newInputOfSize(int n){
			return new Graph(n);
		}

		static public void initialize(){}

		public void randomize(Random rand){
			for(int i = 0; i < nodes.size(); i ++){
				nodes.get(i).randomize(rand);
			}
		}

		Graph(Graph in){
			this.copy(in);
		}

		public void copy(Graph in) {
			nodes = new ArrayList<>();
			for(int i = 0; i < in.nodes.size(); i ++){
				nodes.add(new GraphNode(in.nodes.get(i).getName(), this));
			}
			for(int i = 0; i < nodes.size(); i ++){
				nodes.get(i).setLinks(copyLinksInGraph(in.nodes.get(i).getLinks()));
			}
		}

		ArrayList<GraphLink> copyLinksInGraph(ArrayList<GraphLink> in){
			ArrayList<GraphLink> out = new ArrayList<>();
			for(int i = 0; i < in.size(); i ++){
				GraphNode nextNodeProxy = new GraphNode(in.get(i).getTarget().getName());
				int nextNodeIndex = nodes.indexOf(nextNodeProxy);
				GraphNode nextNode = nodes.get(nextNodeIndex);
				out.add(new GraphLink(nextNode, in.get(i).getWeight(), this));
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
				for(GraphLink link : node.getLinks()){
					System.out.print(link.getTarget().name + "," + link.getWeight() + " ");
				} 
				System.out.println();
				System.out.println();
			}
		}
	
		double findPathValueAndDecrement(){
			clearLookBack();			
			nodes.get(0).lookBack = new GraphLink(nodes.get(0), Double.MAX_VALUE, this);
			ArrayList<GraphNode> queue = new ArrayList<>();
			queue.add(nodes.get(0));
			while(queue.size() > 0){
				GraphNode nextNode = queue.get(0);
				queue.remove(0);
				for(GraphLink link : nextNode.getLinks()){
						if(link.getWeight() > 0) {
						GraphNode targetNode = link.getTarget();
						if(targetNode.lookBack == null && !queue.contains(targetNode)){
							queue.add(targetNode);
							targetNode.lookBack = new GraphLink(nextNode, Math.max(0, 
								Math.min(link.getWeight(), 
									nextNode.lookBack.getWeight())), this);
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

		void printNumLinksAndNodes(){
			System.out.println("nodes: " + nodes.size());
			int locCount = 0;
			for(int i = 0; i < nodes.size(); i ++){
				locCount += nodes.get(i).links.size();
			}
			System.out.println("links: " + locCount);
		}

		void decrementAlongPath(double out){
			GraphNode curNode = nodes.get(nodes.size() - 1);
			if(out <= 0){
				return;
			} else {
				while(curNode.name != nodes.get(0).name){
					GraphNode prevNode = curNode;
					curNode = curNode.lookBack.getTarget();
					GraphLink link = curNode.getLinkFromNode(prevNode);
					link.setWeight(link.getWeight() - out);
				}
			}
		}

		void deduct(double n){
			for(int i = 0; i < nodes.size(); i ++){
				for(int j = 0; j < nodes.get(i).getLinks().size(); j ++){
					nodes.get(i).getLinks().get(j).deduct(n);
				}
			}
		}

		GraphNode randomNode(Random rand){
			return nodes.get(rand.nextInt() % nodes.size());
		}

		public class GraphNode implements Input{
			int name;

			ArrayList<GraphLink> links;
			Graph theGraph;			

			GraphLink lookBack;
		
			GraphNode(){}

			GraphNode(int in){
				this.name = in;
			}
			
			GraphNode(int name, ArrayList<GraphLink> inLinks, Graph inGraph){
				this.links = new ArrayList<>();
				for(GraphLink in : inLinks){
					this.links.add(new GraphLink(in));
				}
				this.name = name;
				this.theGraph = inGraph;
			}			

			GraphNode(GraphNode in){
				this();
				this.copy(in);
			}

			GraphNode(int in, Graph graph){
				this.name = in;
				this.theGraph = graph;
			}

			public void copy(Input in){
				if((in instanceof GraphNode)){
					copy((GraphNode) in);
				}
			}
		
			public void copy(GraphNode in){
				this.name = in.name;
				for(int i = 0; i < in.links.size(); i ++){
					this.links.add(new GraphLink(in.links.get(i)));
				}
				this.theGraph = in.theGraph;
			}

			public ArrayList<DefinedLocation> getCurrentLocations(){
				return new ArrayList<DefinedLocation>();
			}
			
			@Override
			public boolean equals(Object in){				
				if(in == null)
					return false;
				if(!(in instanceof GraphNode))
					return false;
				return name == ((GraphNode) in).name;
			}

			GraphLink getLinkFromNode(GraphNode in){
				GraphLink out = null;
				for(GraphLink link : links){
					if(link.getTarget().name == in.name && (out == null || link.getWeight() >= out.getWeight()))
						out = link;
				}
				return out;
			}
		
			public void randomize(Random rand){
				this.name = rand.nextInt();
				int newLinkCount = rand.nextInt(
						(int) ((double)theGraph.nodes.size()*theGraph.newLinkMultiplier));
				this.links = new ArrayList<>();				
				for(int i = 0; i < newLinkCount; i ++){
					GraphLink nextLink = new GraphLink(
						theGraph.nodes.get(rand.nextInt(theGraph.nodes.size())),
						rand.nextDouble(),
						theGraph);
					links.add(nextLink);
				}
			}

			int getName(){
				return this.name;
			}

			ArrayList<GraphLink> getLinks(){
				return this.links;
			}

			void setLinks(ArrayList<GraphLink> inList){
				this.links = inList;
			}
		}
	
		public class GraphLink implements Input{
			private double weight;
			GraphNode target;
			Graph theGraph;

			GraphLink(){}
			
			GraphLink(GraphLink in){
				this();
				this.copy(in);
			}

			GraphLink(GraphNode target, double weight, Graph inGraph){
				this.target = target;
				this.weight = weight;
				this.theGraph = inGraph;
			}
			
			public ArrayList<DefinedLocation> getCurrentLocations(){
				return new ArrayList<DefinedLocation>();
			}			

			public void copy(Input in){
				if(in instanceof GraphLink){
					copy((GraphLink) in);
				}
			}

			public void copy(GraphLink in){
				this.target = in.target;
				this.weight = in.weight;
				this.theGraph = in.theGraph;
			}

			GraphNode getTarget(){
				return this.target;
			}

			public void randomize(Random rand){
				this.weight = rand.nextDouble();
				this.target = theGraph.randomNode(rand);
			}



			void setTarget(GraphNode in){
				this.target = in;
			}

			@Randomize
			public double getWeight(){
				return this.weight;
			}

			@Randomize
			public void setWeight(double weight){
				this.weight = weight;
			}

			public void setWeightRand(Random rand, Double weight){
				this.weight = rand.nextDouble();
			}
			
			public double getWeightRand(Random rand){
				return rand.nextDouble();
			}

			void deduct(double n){
				weight -= n;				
			}
		}
	}
