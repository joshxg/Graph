import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class dfs
	{

		Graph graph;
		StringBuilder string;
		private int count;
		
		public dfs(Graph graph){
			this.graph = graph;
		}
		
		public String DFS(){
			count = 0;
			string = new StringBuilder();
			Node current = graph.getCurrent();
			DFSHelper(current);
			return string.toString();
		}
		
		public void DFSHelper(Node current){
			
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
		}
		
		public Node getNextTree(){
			Node tempNode = graph.getWhiteNode(graph.getWhiteNodeOrder()-1);
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
	dfs testing;

	//Create a List such that each nodes edges are one element
	try
	{
		adjListContent = Files.readAllLines(Paths.get("list.txt"));
	}
	catch (IOException e){e.printStackTrace();}
	graph = new Graph(adjListContent);
	testing = new dfs(graph);
	
	try{
	BufferedWriter writer = new BufferedWriter(new FileWriter("dfs.txt"));
	writer.write(testing.DFS());
	writer.close();
	}
	catch (IOException e){e.printStackTrace();}
	}

}