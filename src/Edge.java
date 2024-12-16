class Edge {
   public Vertex fromVertex;
   public Vertex toVertex;
   public double weight;
    
   public Edge(Vertex from, Vertex to, double weight) {
      fromVertex = from;
      toVertex = to;
      this.weight = weight;
   }
}
