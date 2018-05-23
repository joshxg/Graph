import java.util.ArrayList;
import java.util.List;

public class Graph{

	private ArrayList<Node> nodes;
	private ArrayList<Node> whiteNodes;
	private ArrayList<Node> greyNodes;
	private ArrayList<Node> blackNodes;
	private Node current;
	private int vertices;
	
public Graph(List<String> adjListContent){
		this.blackNodes = new ArrayList<Node>();
		this.greyNodes = new ArrayList<Node>();
		this.whiteNodes = new ArrayList<Node>();
		this.nodes = new ArrayList<Node>();
		populateGraph(adjListContent.size());
		configureEdges(adjListContent);
		vertices = this.nodes.size();
		current = this.nodes.get(0);
	}
	
	//create and add the Edge objects to each node in the graph.
	public void configureEdges(List<String> adjList)
	{
		for(int i = 0; i < adjList.size(); i++)
		{
			String[] temp = adjList.get(i).split(",");
			for(int j = 0; j < temp.length; j++)
			{
				int tempInt = Integer.parseInt(temp[j]);
				Node startNode = nodes.get(i);
				Node endNode = nodes.get(tempInt);
				Edge tempEdge = new Edge(startNode, endNode);
				startNode.addEdge(tempEdge);
			}
		}
	}
	//Create the Node objects before we add edges. Labeled 0-n where n is the number of elements in the AdjList
	public void populateGraph(int numberOfNodes){
		
		for(int i = 0; i < numberOfNodes; i++){
			Node nodeToAdd = new Node(i, this);
				nodes.add(nodeToAdd);
				whiteNodes.add(nodeToAdd);
		}
	}

	//UTILS
	public void setCurrent(Node current){
		this.current = current;
	}
	
	public int getBlackNodeOrder(){
		return this.blackNodes.size();
	}
	
	public int getWhiteNodeOrder(){
		return this.whiteNodes.size();
	}
	
	public int getGreyNodeOrder(){
		return this.greyNodes.size();
	}
	
	public int getOrder(){
		return this.vertices;
	}
	
	public void pushGrey(Node node){
		this.whiteNodes.remove(node);
		this.greyNodes.add(node);
	}
	
	public Node popGrey(){
		Node node = this.greyNodes.remove(greyNodes.size()-1);
		return node;
	}
	
	public Node getNode(int nodeName){
		return this.nodes.get(nodeName);
	}
	
	public Node getWhiteNode(int nodeName){
		return this.whiteNodes.get(nodeName);
	}
	
	public boolean containsWhiteNode(Node node){
		return this.whiteNodes.contains(node);
	}
	
	public void removeWhiteNode(Node node){
		this.whiteNodes.remove(node);
	}
	
	public void addBlackNode(Node node){
		this.blackNodes.add(node);
	}
	
	public ArrayList<Node> getPriorityList(){
		return this.blackNodes;
	}
	
	public Node getCurrent(){
		return this.current;
	}
	
	public void resetTree(){
		this.blackNodes = new ArrayList<Node>();
		this.whiteNodes = new ArrayList<Node>();
		this.greyNodes = new ArrayList<Node>();
		
		for (int i = 0; i < nodes.size(); i++){
			this.whiteNodes.add(nodes.get(i));
			nodes.get(i).colourWhite();
		}
	}
}

class Node
{
	private static int WHITE = 0;	
	private static int GREY = 1;
	private static int BLACK = 2;
	private int colour;
	private int name;
	private Graph graph;
	private ArrayList<Edge> edges;
	private ArrayList<Edge> tempEdges;
	
	public Node(int name, Graph graph)
	{
		this.graph = graph;
		this.name = name;
		this.colour = WHITE;
		this.edges = new ArrayList<Edge>();
		this.tempEdges = new ArrayList<Edge>();
	}
	
	//Util's
	public int getName(){
		return this.name;
	}
	
	public ArrayList<Edge> getEdges(){
		return this.edges;
	}
	
	public boolean isWhite(){
		return this.colour == WHITE;
	}

	public boolean isGrey(){
		return this.colour == GREY;
	}

	public boolean isBlack(){
		return this.colour == BLACK;
	}
	
	public void colourWhite(){
		this.colour = WHITE;
	}
	
	//colour this node grey, push onto the greyNodes "stack"
	public void colourGrey(){
		this.graph.removeWhiteNode(this);
		this.colour = GREY;
		this.graph.pushGrey(this);
	}

	//colour this node black, add to graphs blackNodes ArrayList.
	public void colourBlack(){

		this.colour = BLACK;
		this.graph.addBlackNode(graph.popGrey());
	}
	
	//compares this Node to another using the int name, -1,1,0 for <,>,= respectively
	public int compareTo(Node node){
		if(this.name > node.getName()){
			return 1;
		}
		else if(this.name < node.getName()){
			return -1;
		}
		return 0;
	}
	
	//True if this node has an edge connecting to a white Node
	public boolean hasWhiteEdge(){
		for (int i = 0; i < this.edges.size(); i++){
			if (edges.get(i).getEnd().isWhite()){
				return true;
			}
		}
		return false;
	}
	//returns this Nodes lowest named white node
	public Node getNextWhite(){
		Node temp = edges.get(0).getEnd();
		for (int i = edges.size()-1; i > -1; i--){
			if (edges.get(i).getEnd().isWhite()){
				temp = edges.get(i).getEnd();
			}
		}
		return temp;
	}
	
	//Add's an edge to this Node. Keeps them in order of lowest name to highest for 
	//simplicity of other methods/traversing
	public void addEdge(Edge edge){
		if (edges.isEmpty()){
			this.edges.add(edge);
			return;
		}
		if  (edge.getEndName() < edges.get(0).getEndName()){
			edges.add(0, edge);
		}	
		else if(edge.getEndName() > edges.get(0).getEndName()){
			for (int i = 0; i < edges.size(); i++){
				if (edge.getEndName() < edges.get(i).getEndName()){
					edges.add(i, edge);
					return;
				}
			}
		}
		edges.add(edge);
	}
	
	//Used for reversing a full graph
	public void addTempEdge(Edge edge){
		this.tempEdges.add(edge);
	}
	
	//After reversing all the edges in a graph, set all of the new edges to actual edges
	public void tempEdgeToReal(){
		this.edges = new ArrayList<Edge>();
		for (int i = 0; i < tempEdges.size(); i++){
			this.addEdge(tempEdges.get(i));
		}
		tempEdges = new ArrayList<Edge>();
			
	}
	
	//reverses all of the edges of a Node, by removing it from the current start, and
	//adding it to the new start Nodes tempEdge array for use on reversing an entire graph
	public void reverseEdges(){
		for(int i = edges.size()-1; i > -1; i--){
			edges.remove(i).reverse();
		}
	}
}

class Edge
{
	Node start;
	Node end;
	int startName;
	int endName;
	
	public Edge(Node start, Node end){
		this.start = start;
		this.end = end;
		this.setNames();
	}
	
	//Util's
	private void setNames(){
		this.startName = start.getName();
		this.endName = end.getName();
	}
	
	public int getEndName(){
		return this.endName;
	}
	
	public int getStartName(){
		return this.startName;
	}
	
	public Node getEnd(){
		return this.end;
	}
	
	public Node getStart(){
		return this.start;
	}
	
	//reverse the edges, add to the tempEdges array of the new starting Node.
	public void reverse(){
		Node temp = this.start;
		this.start = this.end;
		this.end = temp;
		this.setNames();
		this.start.addTempEdge(this);
	}
	
}
