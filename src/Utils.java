import java.util.LinkedList;
import java.util.Scanner;
import java.util.Vector;

public class Utils {
    int start;
    int end;
    Vector<Edge> path = new Vector<>();


//    void InputEdges(Graph graph){
//
//        Scanner scanner = new Scanner(System.in);
//        System.out.println("Enter the starting node");
//        start = scanner.nextInt();
//        System.out.println("Enter the ending node");
//        end = scanner.nextInt();
//        while (true) {
//            System.out.println("Enter the source, destination and the capacity of the edge");
//            System.out.println("and enter any negative number if you are finished");
//            Edge Tempedge= new Edge();
//            Tempedge.source = scanner.nextInt();
//            if (Tempedge.source < 0 || Tempedge.source >= graph.Vertex)break;
//            Tempedge.dest = scanner.nextInt();
//            if (Tempedge.dest < 0 || Tempedge.dest >= graph.Vertex)break;
//            Tempedge.capacity = scanner.nextInt();
//            if (Tempedge.capacity < 0)break;
//            graph.edges.add(Tempedge);
//        }
//    }


    boolean bfs(Graph g, int s, int t, int[] parent)
    {
        // Create a visited array and mark all vertices as not
        // visited
        boolean[] visited = new boolean[g.Vertex];
        for(int i=0; i<g.Vertex; ++i)
            visited[i]=false;

        // Create a queue, enqueue source vertex and mark
        // source vertex as visited
        LinkedList<Integer> queue = new LinkedList<Integer>();
        queue.add(s);
        visited[s] = true;
        parent[s]=-1;

        // Standard BFS Loop
        while (queue.size()!=0)
        {
            int u = queue.poll();

            for (int v=0; v<g.Vertex; v++)
            {
                if (visited[v]==false && getCapacity(u,v,g) > 0)
                {
                    queue.add(v);
                    parent[v] = u;
                    visited[v] = true;
                }
            }
        }

        // If we reached sink in BFS starting from source, then
        // return true, else false
        return (visited[t] == true);
    }

    int fordFulkerson(Graph g, int s, int t)
    {
        int u, v;

        // Create a residual graph and fill the residual graph
        // with given capacities in the original graph as
        // residual capacities in residual graph

        // Residual graph where rGraph[i][j] indicates
        // residual capacity of edge from i to j (if there
        // is an edge. If rGraph[i][j] is 0, then there is
        // not)
        Graph rgraph = g;



        // This array is filled by BFS and to store path
        int[] parent = new int[g.Vertex];

        int max_flow = 0;  // There is no flow initially

        // Augment the flow while tere is path from source
        // to sink

        while (bfs(rgraph, s, t, parent))
        {
            // Find minimum residual capacity of the edhes
            // along the path filled by BFS. Or we can say
            // find the maximum flow through the path found.
            int path_flow = Integer.MAX_VALUE;
            for (v=t; v!=s; v=parent[v])
            {
                u = parent[v];
                path_flow = Math.min(path_flow, getCapacity(u,v,rgraph));
            }

            // update residual capacities of the edges and
            // reverse edges along the path
            Vector<Edge> temp = new Vector<>();
            for (v=t; v != s; v=parent[v])
            {
                u = parent[v];
                changeCapacityNegative(u,v,rgraph,path_flow);
                changeCapacitypositive(v,u,rgraph,path_flow);
                Edge ToBeAdded = new Edge(path_flow, u, v);
                temp.add(ToBeAdded);
            }
            for (int i = temp.size() - 1; i >= 0; i--) {
                path.add(temp.elementAt(i));
            }
            // Add path flow to overall flow
            max_flow += path_flow;
        }

        // Return the overall flow
        return max_flow;
    }
    int getCapacity(int first,int second,Graph g){
        for (int i = 0; i <g.edges.size() ; i++) {
            if ((g.edges.elementAt(i).source==first && g.edges.elementAt(i).dest==second))
                return g.edges.elementAt(i).capacity;
        }
        return 0;
    }

    void changeCapacityNegative(int first, int second , Graph g ,int value){
        for (int i = 0; i <g.edges.size() ; i++) {
            if ((g.edges.elementAt(i).source==first && g.edges.elementAt(i).dest==second)){
            g.edges.elementAt(i).capacity-=value;
            }
        }
    }
    void changeCapacitypositive(int first, int second , Graph g ,int value){
        for (int i = 0; i <g.edges.size() ; i++) {
            if ((g.edges.elementAt(i).source==first && g.edges.elementAt(i).dest==second)){
                g.edges.elementAt(i).capacity+=value;
            }
        }
    }
}
