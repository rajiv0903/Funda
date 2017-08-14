package course02._12._08_depth_and_breadth_first.practice;

import java.util.List;

public interface GraphPractice {
	
	enum GraphPracticeType{
		DIRECTED,
		UNDIRECTED
	}
	
	void addEdge(int v1, int v2);
	
	List<Integer> getAdjacentVertices (int v);

}
