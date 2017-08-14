package course02._14._11_Bellman_Ford_algorithm_implementation.practice;

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
