class PrintVisitor implements VertexVisitor {
   public void visit(Vertex vertex) {
      System.out.print(vertex.label + " ");
   }
}