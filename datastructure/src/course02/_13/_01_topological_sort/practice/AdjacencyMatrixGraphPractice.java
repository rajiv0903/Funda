package course02._13._01_topological_sort.practice;

import java.util.ArrayList;
import java.util.List;

public class AdjacencyMatrixGraphPractice implements GraphPractice{
	
	private int[][] adjacencyMatrix;
    private  GraphPracticeType graphType = GraphPracticeType.DIRECTED;
    private int numVertices = 0;
    
    public AdjacencyMatrixGraphPractice (int numVertices, GraphPracticeType graphType)
    {
    	this.numVertices = numVertices;
    	this.graphType = graphType;
    	adjacencyMatrix = new int [numVertices][numVertices];
    	for (int i =0 ; i < numVertices; i++)
    	{
    		for (int j=0; j < numVertices; j++)
    		{
    			adjacencyMatrix[i][j]=0;
    		}
    	}
    }
    
    @Override
    public GraphPracticeType TypeofGraph()
    {
    	return this.graphType;
    }
    @Override
    public void addEdge(int v1, int v2)
    {
    	if(v1 <0 || v1 >= getNumVertices() || v2 <0 || v2 >= getNumVertices())
    	{
    		throw new  IllegalArgumentException("Vertex number is not valid");
    	}
    	
    	adjacencyMatrix[v1][v2] = 1;
    	if(TypeofGraph() == GraphPracticeType.UNDIRECTED)
    	{
    		adjacencyMatrix[v2][v1] = 1;
    	}
    }
    
    @Override
    public void addEdge(int v1, int v2, int weight) {
    	
    	if(v1 <0 || v1 >= getNumVertices() || v2 <0 || v2 >= getNumVertices())
    	{
    		throw new  IllegalArgumentException("Vertex number is not valid");
    	}
    	
    	adjacencyMatrix[v1][v2] = weight;
    	if(TypeofGraph() == GraphPracticeType.UNDIRECTED)
    	{
    		adjacencyMatrix[v2][v1] = weight;
    	}
    }
    
    @Override
    public int getWeightedEdge(int v1, int v2) 
    {
    	if(v1 <0 || v1 >= getNumVertices() || v2 <0 || v2 >= getNumVertices())
    	{
    		throw new  IllegalArgumentException("Vertex number is not valid");
    	}
    	return adjacencyMatrix[v1][v2];
    }
    
    @Override
    public List<Integer> getAdjacentVertices(int v) 
    {
    	List<Integer> list = new ArrayList<Integer>();
    	for (int i = 0 ; i <getNumVertices(); i ++)
    	{
    		if(adjacencyMatrix[v][i] !=0)
    		{
    			list.add(i);
    		}
    	}
    	return list;
    }
    
    @Override
    public int getIndegree(int v) 
    {
    	if (v < 0 ||  v >= getNumVertices()) 
    	{
             throw new  IllegalArgumentException("Vertex number is not valid");
        }
    	int count = 0;
    	for (int i = 0 ; i <getNumVertices(); i ++)
    	{
    		if(adjacencyMatrix[i][v] !=0)
    		{
    			count++;
    		}
    	}
    	return count;
    }
    
    @Override
    public int getNumVertices() 
    {
    	return numVertices;
    }

}
