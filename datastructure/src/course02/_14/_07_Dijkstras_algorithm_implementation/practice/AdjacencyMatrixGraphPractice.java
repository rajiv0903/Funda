package course02._14._07_Dijkstras_algorithm_implementation.practice;

import java.util.ArrayList;
import java.util.List;


public class AdjacencyMatrixGraphPractice implements GraphPractice {

	private int[][] adjacencyMatrix;
    private  GraphPracticeType graphType = GraphPracticeType.DIRECTED;
    private int numVertices = 0;
    
    public AdjacencyMatrixGraphPractice(int numVertices, GraphPracticeType graphType)
    {
    	this.numVertices = numVertices;
    	this.graphType = graphType;
    	
    	adjacencyMatrix = new int[numVertices][numVertices];
    	
    	for (int i = 0 ; i < numVertices; i ++)
    		for (int j =0 ; j < numVertices; j++)
    			adjacencyMatrix[i][j] = 0;
    }
    
    @Override
    public void addEdge(int v1, int v2) {
    	 if (v1 >= numVertices || v2 >= numVertices || v1 < 0 || v2 < 0) {
             throw new  IllegalArgumentException("Vertex number is not valid");
         }
    	 adjacencyMatrix[v1][v2] = 1;
    	 if(graphType == GraphPracticeType.UNDIRECTED)
    		 adjacencyMatrix[v2][v1]= 1;
    }
    
    @Override
    public void addEdge(int v1, int v2, int weight) {
    	if (v1 >= numVertices || v2 >= numVertices || v1 < 0 || v2 < 0) {
            throw new  IllegalArgumentException("Vertex number is not valid");
        }
    	 adjacencyMatrix[v1][v2] = weight;
    	 if(graphType == GraphPracticeType.UNDIRECTED)
    		 adjacencyMatrix[v2][v1]= weight;
    }
    
    @Override
    public List<Integer> getAdjacentVertices(int v) {
    	if (v >= numVertices || v >= numVertices ) {
            throw new  IllegalArgumentException("Vertex number is not valid");
        }
    	
    	List<Integer> adjacents = new ArrayList<Integer>();
    	for (int i = 0; i < numVertices; i++)
    	{
    		if(adjacencyMatrix[v][i] != 0)
    			adjacents.add(i);
    	}
    	return adjacents;
    }
    
    @Override
    public int getIndegree(int v) {
    	if (v >= numVertices || v >= numVertices ) {
            throw new  IllegalArgumentException("Vertex number is not valid");
        }
    	int indegree = 0;
    	for (int i = 0 ; i < numVertices; i++){
    		if(adjacencyMatrix[i][v]!= 0){
    			indegree++;
    		}
    	}
    	return indegree; 
    }
    
    @Override
    public int getNumVertices() {
    	return this.numVertices;
    }
    
    @Override
    public int getWeightedEdge(int v1, int v2) {
    	return adjacencyMatrix[v1][v2];
    }
    
    @Override
    public GraphPracticeType TypeofGraph() {
    	return this.graphType;
    }
    
    
}
