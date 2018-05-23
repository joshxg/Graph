import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class reverse2list{

	Graph graph;
	
	public reverse2list(Graph graph){
		this.graph = graph;
	}
	
	public void reverseGraph(){
		for (int i = 0; i < graph.getOrder(); i++){
			graph.getNode(i).reverseEdges();
		}
		
		for (int i = 0; i < this.graph.getOrder(); i++){
			this.graph.getNode(i).tempEdgeToReal();
		}
	}
	
	public String adjacencyList(){
		StringBuilder string = new StringBuilder();
		for (int i = 0; i < graph.getOrder(); i++){
			Node tempNode = graph.getNode(i);
			ArrayList<Edge> tempEdges = tempNode.getEdges();
			
			for (int j = 0; j < tempEdges.size(); j++){
				string.append(tempEdges.get(j).getEndName() + ",");
			}
			string.setCharAt(string.length()-1,'\n');
		}
		string.deleteCharAt(string.length()-1);
		return string.toString();
	}
	
	public static void main(String[] args){
		
		List<String> adjListContent = null;
		
		//Create a List such that each nodes edges are one element
		try{
			adjListContent = Files.readAllLines(Paths.get("list.txt"));
		}
		catch (IOException e){e.printStackTrace();}
		
		Graph graph = new Graph(adjListContent);
		reverse2list testing = new reverse2list(graph);
		testing.reverseGraph();
		try{
			BufferedWriter writer = new BufferedWriter(new FileWriter("reverselist.txt"));
			writer.write(testing.adjacencyList());
			writer.close();
		}
		catch (IOException e){e.printStackTrace();}


	}

}