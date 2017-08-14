package course02._12._05_graph_adjancency_matrix.practice;

import java.util.List;

public interface GraphPractice {
		
	enum GraphPracticeType{
		DIRECTED, UNDIRECTED
	}
	
	void addEdge(int v1, int v2);
	
	List<Integer> getAdjacentVertices(int v);
}
