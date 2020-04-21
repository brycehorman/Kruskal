import java.util.*; 
import java.lang.*; 
import java.io.*; 

public class KruskalMST{
  
    class Graph{ 

        // Class to represent an edge in graph
        class Edge implements Comparable<Edge>{ 
            int from, to, weight; 
    
            // Comparator for sorting edges by weight 
            public int compareTo(Edge compareEdge) 
            { 
                return this.weight - compareEdge.weight; 
            } 
        }; 
    
        // Class to represent a subset for union-find 
        class Subset{ 
            int parent, rank; 
        }; 
    
        int V, E;    
        Edge edges[]; 
    
        // Creates a graph with V vertices and E edges 
        Graph(int v, int e){ 
            V = v; 
            E = e; 
            edges = new Edge[E]; 
            for (int i = 0; i < e; ++i){
                edges[i] = new Edge(); 
            }
        } 
    
        // Finds set of an element 
        int find(Subset subsets[], int i) 
        { 
            if (subsets[i].parent != i){
                subsets[i].parent = find(subsets, subsets[i].parent); 
            }
    
            return subsets[i].parent; 
        } 
    
        // Finds union of two sets 
        void Union(Subset subsets[], int x, int y){ 
            int xroot = find(subsets, x); 
            int yroot = find(subsets, y); 
    
            // Put smaller rank tree under root of high rank tree 
            if (subsets[xroot].rank < subsets[yroot].rank) 
                subsets[xroot].parent = yroot; 
            else if (subsets[xroot].rank > subsets[yroot].rank) 
                subsets[yroot].parent = xroot; 
    
            // If same rank, then make one as root and increment by one
            else
            { 
                subsets[yroot].parent = xroot; 
                subsets[xroot].rank++; 
            } 
        } 
    
        // Function to find MST using Kruskal's algorithm 
        Edge[] KruskalMST() {

            Edge result[] = new Edge[V];  
            int j = 0;  
            int i = 0; 

            for (i = 0; i < V; ++i){
                result[i] = new Edge();
            } 
    
            // Sort all edges in non-decreasing order by weight
            Arrays.sort(edges); 
    
            Subset subsets[] = new Subset[V]; 
            for(i = 0; i < V; ++i){
                subsets[i] = new Subset(); 
            }
    
            // Create V subsets with single elements 
            for (int v = 0; v < V; ++v) 
            { 
                subsets[v].parent = v; 
                subsets[v].rank = 0; 
            } 
    
            i = 0;  
    
            while (j < V - 1){ 
                Edge next = new Edge(); 
                next = edges[i++]; 
    
                int x = find(subsets, next.from); 
                int y = find(subsets, next.to); 
    
                // If edge does't cause cycle, include in result 
                if (x != y){ 
                    result[j++] = next; 
                    Union(subsets, x, y); 
                } 
            } 

            return result;
        }

    } 
}