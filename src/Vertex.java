import java.util.ArrayList;
import java.util.Comparator;

class Vertex implements Comparator<Vertex>{
   public static int lastLabel = -1;
   public static int maxLabel = -1;
   public int label;
   public String contents;
   
   public Vertex(String c) {
	   contents = c;
	   label = ++maxLabel;
	   lastLabel = label;
   }

	@Override
	public int compare(Vertex o1, Vertex o2) {
		if(o1.label == o2.label) {
			return 1;
		}
		return 0;
	}
}
