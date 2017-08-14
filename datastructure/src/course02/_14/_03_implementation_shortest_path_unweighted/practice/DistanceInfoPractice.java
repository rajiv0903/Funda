package course02._14._03_implementation_shortest_path_unweighted.practice;

public class DistanceInfoPractice {
	
	private int distance;
    private int lastVertex;
	
    public DistanceInfoPractice(){
    	this.distance = -1;
    	this.lastVertex = -1;
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


	@Override
	public String toString() {
		return "DistanceInfoPractice [distance=" + distance + ", lastVertex="
				+ lastVertex + "]";
	}
    
	
	
    

}
