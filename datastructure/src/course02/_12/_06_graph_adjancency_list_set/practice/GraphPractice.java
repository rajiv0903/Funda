package course02._12._06_graph_adjancency_list_set.practice;

import java.util.List;

public interface GraphPractice {
	
	enum GraphPracticeType{
		DIRECTED,
		UNDIRECTED
	}
	
	void addEdge(int v1, int v2);

	List<Integer> getAdjacentVertices(int v);
}
