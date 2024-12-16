import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;

class Graph {
   // Maps a vertex to an ArrayList of all edges that start from that vertex
   private HashMap<Vertex, ArrayList<Edge>> fromEdges;
   
   // Maps a vertex to an ArrayList of all edges that go to that vertex
   private HashMap<Vertex, ArrayList<Edge>> toEdges;
   
   // global variable to count the number of nodes visited
   public int visitCounter = 0;
   
   public Graph() {
      fromEdges = new HashMap<Vertex, ArrayList<Edge>>();
      toEdges = new HashMap<Vertex, ArrayList<Edge>>();
   }
   
   public Vertex addVertex(String newVertexLabel) {
      // Create the new Vertex object
      Vertex newVertex = new Vertex(newVertexLabel);
       
      // Every vertex must exist as a key in both maps
      fromEdges.put(newVertex, new ArrayList<Edge>());
      toEdges.put(newVertex, new ArrayList<Edge>());
      
      return newVertex;
   }
   
   public Edge addDirectedEdge(Vertex fromVertex, Vertex toVertex) {
      // Use 1.0 as the default edge weight
      return addDirectedEdge(fromVertex, toVertex, 1.0);
   }
   
   public Edge addDirectedEdge(Vertex fromVertex, Vertex toVertex, double weight) {
      // Don't add the same edge twice
      if (hasEdge(fromVertex, toVertex)) {
         return null;
      }
      
      // Create the Edge object
      Edge newEdge = new Edge(fromVertex, toVertex, weight);
      
      // Add the edge to the appropriate list in both maps
      fromEdges.get(fromVertex).add(newEdge);
      toEdges.get(toVertex).add(newEdge);
      
      return newEdge;
   }
   
   public Edge[] addUndirectedEdge(Vertex vertexA, Vertex vertexB) {
      // Use 1.0 as the default edge weight
	  //System.out.println(vertexA.contents + " <-> " + vertexB.contents);
      return addUndirectedEdge(vertexA, vertexB, 1.0);
   }
   
   public Edge[] addUndirectedEdge(Vertex vertexA, Vertex vertexB, double weight) {
      Edge edge1 = addDirectedEdge(vertexA, vertexB, weight);
      Edge edge2 = addDirectedEdge(vertexB, vertexA, weight);
      Edge[] result = { edge1, edge2 };
      
      return result;
   }

   // Returns a collection of all edges in the graph
   public Collection<Edge> getEdges() {
      HashSet<Edge> edges = new HashSet<Edge>();
      for (ArrayList<Edge> edgeList : fromEdges.values()) {
         edges.addAll(edgeList);
      }
      return edges;
   }
   
   // Returns the collection of edges with the specified fromVertex
   public Collection<Edge> getEdgesFrom(Vertex fromVertex) {
      return fromEdges.get(fromVertex);
   }
   
   // Returns the collection of edges with the specified toVertex
   public Collection<Edge> getEdgesTo(Vertex toVertex) {
      return toEdges.get(toVertex);
   }

   // Returns the collection of vertices that are adjacent
   public List<Vertex> getAdjacent(Vertex fromVertex){
	   List<Vertex> adjacent = new ArrayList<Vertex>();
	   
	   for(Edge e : getEdgesFrom(fromVertex)) {
		   adjacent.add(e.toVertex);
	   }
	   return adjacent;
   }
   
   // Returns a vertex with a matching label, or null if no such vertex exists
   public Vertex getVertex(int vertexLabel) {
      // Search the collection of vertices for a vertex with a matching label
      return getVertices()[vertexLabel];
   }
   
   public Vertex getVertex(String vertexContents) {
      // Search the collection of vertices for a vertex with a matching label
      for (Vertex vertex : getVertices()) {
         if (vertex.contents.equals(vertexContents)) {
            return vertex;
         }
      }
      return null;
   }
   
   // Returns the collection of all of this graph's vertices
   public Vertex[] getVertices() {
      return fromEdges.keySet().toArray(new Vertex[0]);
   }

   // Returns true if this graph has an edge from fromVertex to toVertex
   public boolean hasEdge(Vertex fromVertex, Vertex toVertex) {
      if (!fromEdges.containsKey(fromVertex)) {
         // fromVertex is not in this graph
         return false;
      }
      
      // Search the list of edges for an edge that goes to toVertex
      ArrayList<Edge> edges = fromEdges.get(fromVertex);
      for (Edge edge : edges) {
         if (edge.toVertex == toVertex) {
            return true;
         }
      }
      
      return false;
   }
   
   public boolean hasPathDFS(String source, String destination) { // helper function
	   visitCounter = 0;
	   boolean result = false;
	   Vertex s = getVertex(source);
	   Vertex d = getVertex(destination);
	   HashSet<Integer> visited = new HashSet<Integer>();
	   result = hasPathDFS(s, d, visited);
	   System.out.println("Reached destination in " + String.valueOf(visitCounter) + " visits");
	   return result;
   }
   
   public boolean hasPathDFS(Vertex source, Vertex destination, HashSet<Integer> visited) { // main function
	   //do not revisit the same node
	   if(visited.contains(source.label)) {
		   return false;
	   }
	   //record the visit
	   visited.add(source.label);
	   System.out.println(source.contents);
	   visitCounter++;
	   //break recursion when found
	   if(source == destination) {
		   return true;
	   }
	   
	   for(Vertex adj : getAdjacent(source)) {
		   if(hasPathDFS(adj, destination, visited)) {
			   return true;
		   }
	   }
	   return false;
   }
   
   public boolean hasPathBFS(String source, String destination) { // helper function
	   visitCounter = 0;
	   source = source.toLowerCase();
	   destination = destination.toLowerCase();
	   Vertex src = getVertex(source);
	   Vertex dest = getVertex(destination);
	   boolean result = false;
	   // error handling
	   if(src == null) {
		   System.out.println("Invalid word provided: '" + source + "'");
		   return false;
	   }
	   if(dest == null) {
		   System.out.println("Invalid word provided: '" + destination + "'");
		   return false;
	   }
	   result = hasPathBFS(src, dest);
	   System.out.println("Reached destination in " + String.valueOf(visitCounter) + " visits");
	   return result;
   }
   
   public boolean hasPathBFS(Vertex source, Vertex destination) { // main function
	   Queue<Vertex> nextToVisit = new LinkedList<Vertex>();
	   //HashSet<Integer> visited = new HashSet<Integer>();
	   // Establish an array to fetch the distance from the source node
	   int[] distances = new int[getVertices().length];
	   Arrays.parallelSetAll(distances, i -> -1);
	   distances[source.label] = 0;
	   
	   nextToVisit.add(source);
	   while(!nextToVisit.isEmpty()) {
		   Vertex v = nextToVisit.poll();
		   //do not revisit same node
		   //if(visited.contains(v.label)) {
//		   if(distances[v.label] > 0) {
//			   continue;
//		   }
		   
		   // record the visit 
		   System.out.println(v.contents);
		   visitCounter++;
		   
		   // end when found
		   if(v == destination) {
			   System.out.println("Distance of " + String.valueOf(distances[v.label]));
			   return true;
		   }
		   
		   // extend the loop when not found
		   for(Vertex adj : getAdjacent(v)) {
			   if(distances[adj.label] == -1) {
				   distances[adj.label] = distances[v.label] + 1;
				   nextToVisit.add(adj);
			   }
		   }
	   }
	   System.out.println("Path does not exist.");
	   return false;
   }
}

