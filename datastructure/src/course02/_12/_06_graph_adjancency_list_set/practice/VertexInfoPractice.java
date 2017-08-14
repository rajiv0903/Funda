package course02._12._06_graph_adjancency_list_set.practice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VertexInfoPractice {

	private int vertexId;
	private Set<Integer> adjacencySet = new HashSet<>();
	
	public VertexInfoPractice(int vertexId) {
		this.vertexId = vertexId;
	}

	public int getVertexId() {
		return vertexId;
	}
	public void addEdge(int vertexNumber) {
		adjacencySet.add(vertexNumber);
	}
	
	public List<Integer> getAdjacentVertices() {
		List<Integer> sortedList = new ArrayList<Integer>(adjacencySet);
		Collections.sort(sortedList);
		return sortedList;
	}

}
