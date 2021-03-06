package course02._13._01_topological_sort.practice;

import java.util.List;

public interface GraphPractice {

	enum GraphPracticeType {
		DIRECTED, UNDIRECTED
	}

	GraphPracticeType TypeofGraph();

	void addEdge(int v1, int v2);

	void addEdge(int v1, int v2, int weight);

	int getWeightedEdge(int v1, int v2);

	List<Integer> getAdjacentVertices(int v);

	int getNumVertices();

	int getIndegree(int v);
}
