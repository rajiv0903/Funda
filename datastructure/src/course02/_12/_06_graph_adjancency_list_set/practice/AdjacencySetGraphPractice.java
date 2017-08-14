package course02._12._06_graph_adjancency_list_set.practice;

import java.util.ArrayList;
import java.util.List;

public class AdjacencySetGraphPractice implements GraphPractice 
{

	private List<VertexInfoPractice> vertexList = new ArrayList<VertexInfoPractice>();
	private GraphPracticeType graphType = GraphPracticeType.DIRECTED;
	private int numVertices = 0;

	public AdjacencySetGraphPractice(int numVertices,GraphPracticeType graphType) 
	{
		
		this.numVertices = numVertices;
		this.graphType = graphType;
		for (int i = 0 ; i < numVertices; i++)
		{
			vertexList.add(new VertexInfoPractice(i));
		}
	}

	@Override
	public void addEdge(int v1, int v2) 
	{		
		if(v1 <0 || v1>= numVertices || v2<0 || v2 >=numVertices)
		{
			 throw new IllegalArgumentException("Vertex number is not valid: " + v1 + ", " + v2);
		}
		vertexList.get(v1).addEdge(v2);
		if(this.graphType == GraphPracticeType.UNDIRECTED){
			vertexList.get(v2).addEdge(v1);
		}
	}

	@Override
	public List<Integer> getAdjacentVertices(int v) 
	{
		if(v <0 || v>=numVertices)
		{
			 throw new IllegalArgumentException("Vertex number is not valid: " + v);
		}
		
		return vertexList.get(v).getAdjacentVertices();
	}

}
