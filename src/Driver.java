import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.util.stream.*;

public class Driver{
	public static HashMap<String, ArrayList<Vertex>> Buckets = new HashMap<>();
	public static engDictionary wordSet;
	public static Graph wordGraph = new Graph();
	public static ArrayList<String> existing = new ArrayList<>();
	
	public static void buildLadder(String w) {
		//List<HashMap> Buckets = new ArrayList<HashMap>();
		// create wildcard buckets for each letter of the word
		// (commented) list is pre-trimmed of duplicates, no need to check
//		if(existing.contains(w)) {
//			return;
//		}
//		existing.add(w);
		Vertex vw = wordGraph.addVertex(w);
		
		for(int i = 0; i < w.length(); i++) {
			String bucketLabel;
			boolean isLast = (i == w.length()-1);
			
			if(isLast == true) {
				bucketLabel = w.substring(0, i)+"_";
			}
			else if(i == 0) {
				bucketLabel = "_" + w.substring(1, w.length());
			}
			else {
				bucketLabel = w.substring(0, i) + "_" + w.substring(i+1, w.length());
			}

			if(Buckets.get(bucketLabel) == null) {
				Buckets.put(bucketLabel, new ArrayList<>() {{add(vw);}});
			}
			else {
				Buckets.get(bucketLabel).add(vw);
			}
		}
	}
	
//	public static void addWordVertex(Graph wordGraph, String w) {
//		wordGraph.addVertex(w);
//	}
	
	public static void main(String[] args){
		String src = "";
		String dest = "";
		int wordCap = -1;
		int charLength = 0;
		// auto fill while testing
		if(args.length > 0 && args[0].equals("dev")) {
				src = "fool";
				dest = "sage";
				wordCap = 50;
		}
		else {
			Scanner in = new Scanner(System.in);
			while(src.length() == 0 || src.length() != dest.length()) {
				System.out.println("Choose two words of equal length");
				src = in.nextLine();
				dest = in.nextLine();
			}
			while(wordCap < 2) {
				System.out.println("Type a number above 2 if you want a cap on the ladder, or press enter to load the full dictionary");
				String response = in.nextLine();
				if(response.length() == 0) {
					wordCap = -1;
					break;
				}
				else {
					wordCap = Integer.parseInt(response);
				}
			}
			in.close();
		}
		charLength = src.length();
		
		wordSet = new engDictionary(charLength, wordCap);
		
		
		// create the word ladder
		for(String w : wordSet.words) {
			buildLadder(w.toLowerCase());
		}
		
		for(String b : Buckets.keySet()) {
			for(Vertex w : Buckets.get(b)) {
				for(Vertex w2 : Buckets.get(b)) {
					if(w != w2) {
						wordGraph.addDirectedEdge(w, w2);
					}
				}
			}
		}
		System.out.println("There are " + String.valueOf(wordSet.words.size()) + " words in the ladder.");
		System.out.println("The ladder has generated " + String.valueOf(Buckets.size()) + " buckets.");
		
		//demo is only designed for fool & sage to guarantee a match when capping words
		
		long startTime;
		long endTime;
		long BFSelapsedTime;
		long DFSelapsedTime;

		startTime = System.currentTimeMillis();
		wordGraph.hasPathBFS(src, dest);
		endTime = System.currentTimeMillis();
		BFSelapsedTime = endTime - startTime;
		System.out.println("BFS took " + String.valueOf(BFSelapsedTime) + " milliseconds.");
		
		startTime = System.currentTimeMillis();
		wordGraph.hasPathDFS(src, dest);
		endTime = System.currentTimeMillis();
		DFSelapsedTime = endTime - startTime;
		
		System.out.println("BFS took " + String.valueOf(BFSelapsedTime) + " milliseconds.");
		System.out.println("DFS took " + String.valueOf(DFSelapsedTime) + " milliseconds.");

		
	}
}
