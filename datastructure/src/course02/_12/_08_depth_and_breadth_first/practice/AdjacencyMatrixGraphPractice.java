package course02._12._08_depth_and_breadth_first.practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AdjacencyMatrixGraphPractice implements GraphPractice{
	
	
	private int numVertices= 0;
	private int[][] adjacencyMatrix;
    private GraphPracticeType graphPracticeType = GraphPracticeType.DIRECTED;
	
    
    public AdjacencyMatrixGraphPractice(int numVertices, GraphPracticeType graphPracticeType)
    {
    	this.graphPracticeType = graphPracticeType;
    	this.numVertices = numVertices;
    	
    	adjacencyMatrix = new int[numVertices][numVertices];
    	
    	for (int i = 0; i < this.numVertices; i++)
    	{
    		for (int j = 0 ; j < this.numVertices; j++)
    		{
    			adjacencyMatrix[i][j] = 0;
    		}
    	}
    }
	
	public void addEdge(int v1, int v2)
	{
		if(v1 <0 || v1 >= this.numVertices || v2 <0 || v2 >= numVertices)
		{
			throw new IllegalArgumentException("Vertex number is not valid");
		}
		
		this.adjacencyMatrix[v1][v2]=1;
		if(this.graphPracticeType == GraphPracticeType.UNDIRECTED)
		{
			this.adjacencyMatrix[v2][v1]=1;
		}
	}
	
	public List<Integer> getAdjacentVertices (int v)
	{
		if(v <0 || v >= this.numVertices )
		{
			throw new IllegalArgumentException("Vertex number is not valid");
		}
		List<Integer> adjacentVerticesList = new ArrayList<>();
		for (int i =0 ; i < this.numVertices; i ++)
		{
			if (this.adjacencyMatrix[v][i]== 1)
			{
				adjacentVerticesList.add(i);
			}
		}
		
		Collections.sort(adjacentVerticesList);
		
		return adjacentVerticesList;
	}

}
