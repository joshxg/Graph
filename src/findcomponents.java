import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class findcomponents
{
	Graph graph;
	StringBuilder string;
	ArrayList<Node> priority;
	ArrayList<Integer> component;
	private int count;
	
	public findcomponents(Graph graph){
		this.graph = graph;
	}
	
	public static ArrayList<String> matrixToList(String fileName){
		List<String> matrixContent = null;
		ArrayList<String> adjListContent = new ArrayList<String>();
		StringBuilder string = null;
		String[] line = null;
		
		try
		{
			matrixContent = Files.readAllLines(Paths.get(fileName));
			
		}
		catch (IOException e){e.printStackTrace();}
		
		for (int i = 0; i < matrixContent.size(); i++){
			string = new StringBuilder();
			line = matrixContent.get(i).split(",");
			for (int j = 0; j < line.length; j++){
				
				if (line[j].compareTo("1") == 0){
					string.append(j + ",");
				}
			}
			string.deleteCharAt(string.length()-1);
			
			adjListContent.add(string.toString());
			}
		return adjListContent;
	}
	
	//runs DFS on a graph, resets afterward
	public String DFS(){
		count = 0;
		string = new StringBuilder();
		DFSHelper(graph.getCurrent());
		graph.resetTree();
		return string.toString();
	}
	
	//sorts and builds a string of the components in the graph
	public String componentsString(){
		string = new StringBuilder();
		ArrayList<Integer> sorted = new ArrayList<Integer>();
		
		for (int i = 0; i < component.size(); i++){
			if (component.get(i).compareTo(-1) != 0){
				sorted.add(component.get(i));
				
			}
			else{
				Collections.sort(sorted);
				for (int j = 0; j < sorted.size(); j++){
					string.append(sorted.get(j) + ",");
				}
				string.setCharAt(string.length()-1, '\n');
				sorted = new ArrayList<Integer>();
			}
		}
		return string.toString();
	}
	
	//Used for finding components of a graph, and returns a string representing each component, sorted by name of the nodes in the graph.
	public String getComponents(){
		component = new ArrayList<Integer>();
		priority = DFSHelper(graph.getCurrent());
		string = new StringBuilder();
		this.reverseGraph();
		graph.resetTree();
		PFSHelper(priority.get(priority.size()-1));
		graph.resetTree();
		return componentsString();	
	}
	
	//just reverses all edges in the graph
	public void reverseGraph(){
		for (int i = 0; i < graph.getOrder(); i++){
			graph.getNode(i).reverseEdges();
		}
		
		for (int i = 0; i < this.graph.getOrder(); i++){
			this.graph.getNode(i).tempEdgeToReal();
		}
	}
	
	//Used for a DFS traversal with priority. For displaying components after the traversal, uses a list where each component is delimited by a -1.
	//Need to call components string on this list afterwards for a string to display.
	public void PFSHelper(Node current){
		
		current.colourGrey();
		
		if (current.hasWhiteEdge()){
			PFSHelper(current.getNextWhite());
		}
		else if(graph.getGreyNodeOrder() > 1){
			current.colourBlack();
			priority.remove(current);
			component.add(current.getName());
			PFSHelper(graph.popGrey());
		}
		else if (graph.getBlackNodeOrder()+1 < this.graph.getOrder()){
			current.colourBlack();
			priority.remove(current);
			component.add(current.getName());
			PFSHelper(this.getNextTreePriority());
		}
		else{
			current.colourBlack();
			priority.remove(current);
			component.add(current.getName());
			component.add(-1);
		}
	}
	
	//Used for finding the next tree in the forest with a priority queue
	public Node getNextTreePriority(){
		component.add(-1);
		return priority.get(priority.size()-1);
	}
	
	//DFSHelper for traversal, recursively finds new white nodes, and does all colouring
	public ArrayList<Node> DFSHelper(Node current){
		
		current.colourGrey();
		if (current.hasWhiteEdge()){
			DFSHelper(current.getNextWhite());
		}
		else if(graph.getGreyNodeOrder() > 1){
			current.colourBlack();
			string.append(count + "," + current.getName() + "\n");
			count++;
			DFSHelper(graph.popGrey());
		}
		else if (graph.getBlackNodeOrder()+1 < this.graph.getOrder()){
			current.colourBlack();
			string.append(count + "," + current.getName() + "\n");
			count++;
			DFSHelper(this.getNextTree());
		}
		else{
			current.colourBlack();
			string.append(count + "," + current.getName() + "\n");
			count++;
		}
		return graph.getPriorityList();
	}
	
	//Used for DFS traversal, finds the next tree in the forest
	public Node getNextTree(){
		Node tempNode = graph.getWhiteNode(0);
		for(int i = 0; i < graph.getWhiteNodeOrder(); i++){
			if(tempNode.compareTo(graph.getWhiteNode(i)) == 1 ){
				tempNode = graph.getWhiteNode(i);
			}
		}
		string.append("\n");
		return tempNode;
	}
	
	
	public static void main(String[] args){
		
		List<String> adjListContent = null;
		Graph graph;
		findcomponents testing;
		
		//Turn the matrix into an adjacency list
		adjListContent = findcomponents.matrixToList("matrix.txt");
	
		graph = new Graph(adjListContent);
		testing = new findcomponents(graph);
		testing.reverseGraph();
		
		//write a reversed DFS traversal
		try{
		BufferedWriter writer = new BufferedWriter(new FileWriter("dfs.txt"));
		writer.write(testing.DFS());
		writer.close();
		}
		catch (IOException e){e.printStackTrace();}
		
		testing.reverseGraph();
		
		//write the components of the graph
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter("components.txt"));
			writer.write(testing.getComponents());
			writer.close();
			}
			catch (IOException e){e.printStackTrace();}
		}
	}
	