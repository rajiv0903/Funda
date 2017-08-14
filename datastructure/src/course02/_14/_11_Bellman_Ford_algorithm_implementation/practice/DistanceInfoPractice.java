package course02._14._11_Bellman_Ford_algorithm_implementation.practice;

public class DistanceInfoPractice {

	private int distance;
	private int lastVertex;

	public DistanceInfoPractice() {
		 // The initial distance to all nodes is assumed infinite. Set it to
        // a very large value rather than the maximum integer value. Bellman Ford
        // supports negative weights and adding anything to this distance can
        // make it a negative value which interferes with the algorithm.
        distance = 100000;
        lastVertex = -1 ;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public int getLastVertex() {
		return lastVertex;
	}

	public void setLastVertex(int lastVertex) {
		this.lastVertex = lastVertex;
	}
	
	

}
