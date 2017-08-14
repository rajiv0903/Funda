package course02._12._08_depth_and_breadth_first.practice;

import java.util.List;

import course02._12._08_depth_and_breadth_first.practice.GraphPractice.GraphPracticeType;

public class GraphBreadthFirstTraversalPractice{
	
	private static int N = 8;

	public static void main(String[] args) throws QueuePractice.QueueOverflowException, QueuePractice.QueueUnderflowException
	{
		GraphPractice graph = new AdjacencyMatrixGraphPractice(N, GraphPracticeType.DIRECTED);

		graph.addEdge(1, 0);
		graph.addEdge(1, 2);
		graph.addEdge(2, 7);
		graph.addEdge(2, 4);
		graph.addEdge(2, 3);
		graph.addEdge(1, 5);
		graph.addEdge(5, 6);
		graph.addEdge(6, 3);
		graph.addEdge(3, 4);
		
		//Initialize visited vertex 
		int[] visited = new int[N];
		// This for-loop ensures that all nodes are covered even for an unconnected
        // graph.
		for (int i=0; i < N; i++){
			breadthFirstTraversal(graph, visited, i);
		}
	}
	
	public static void breadthFirstTraversal(GraphPractice graph, int[] visited, int currentVertex)
	        throws QueuePractice.QueueOverflowException, QueuePractice.QueueUnderflowException 
	{		
		/*QueuePractice<Integer> queue = new QueuePractice<Integer>(Integer.class);
		queue.enqueue(currentVertex);
		
		while (! queue.isEmpty())
		{
			int vertex = queue.dequeue();
			
			if(visited[vertex] ==1){
				continue;
			}
			
			System.out.print(vertex + "->");
            visited[vertex] = 1;
            
            List<Integer> list = graph.getAdjacentVertices(vertex);
            for (int v : list) 
            {
                if (visited[v] != 1) {
                    queue.enqueue(v);
                }
            }
            
		}*/
		
		QueuePractice<Integer> queue = new QueuePractice<Integer>(Integer.class);
		queue.enqueue(currentVertex);
		
		while (! queue.isEmpty())
		{
			int vertex = queue.dequeue();
			
			if(visited[vertex]==1)
				continue;
			
			System.out.print(vertex+"->");
			
			visited[vertex] =1;
			
			List<Integer> list = graph.getAdjacentVertices(vertex);
			
			for (int v: list)
			{
				if(visited[v] != 1)
				{
					queue.enqueue(v);
				}
			}
		}
	}

}
