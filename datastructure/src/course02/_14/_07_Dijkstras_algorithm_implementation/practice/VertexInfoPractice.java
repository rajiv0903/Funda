package course02._14._07_Dijkstras_algorithm_implementation.practice;

public class VertexInfoPractice {

	private int vertexId;
	private int distance;

	public VertexInfoPractice(int vertexId, int distance){
		this.vertexId = vertexId;
		this.distance = distance;
	}
	
	public int getVertexId() {
		return vertexId;
	}

	public int getDistance() {
		return distance;
	}

	@Override
	public String toString() {
		return "VertexInfoPractice [vertexId=" + vertexId + ", distance="
				+ distance + "]";
	}
	
	

}
