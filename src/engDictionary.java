import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.temporal.ValueRange;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

import org.w3c.dom.ranges.Range;
//Dictionary sourced from: https://www.kaggle.com/datasets/dfydata/the-online-plain-text-english-dictionary-opted?select=OPTED-Dictionary.csv

public class engDictionary {
	
	int charLimit;
	int wordCap;
	List<String> words = new ArrayList<>();
	
	public engDictionary(int charLimit) {
		this.charLimit = charLimit;
		importDict(charLimit, -1);
	}
	
	public engDictionary(int charLimit, int wordCap) {
		this.charLimit = charLimit;
		importDict(charLimit, wordCap);
	}
	
	public void importPremade(String fileName) {
		String line;
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			while ((line = br.readLine()) != null) 
					words.add(line.trim());
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}
	
    public void importDict(int charLimit, int wordCap) {
    	String fileName = "character" + String.valueOf(charLimit) + "_word" + String.valueOf(wordCap) + ".txt";
		String delimiter = ",";
		String line; // hold each line read
		
		File f = new File(fileName);
		if(f.exists() && !f.isDirectory()) { 
		    importPremade(fileName);
		    return;
		}
		
		// Try-with-resources: Automatically close the BufferedReader after use
		try (BufferedReader br = new BufferedReader(new FileReader("OPTED-Dictionary.csv"))) {
			List<String> header = List.of(br.readLine().split(delimiter));
			int wordCol = header.indexOf("Word");
			
			
			while ((line = br.readLine()) != null) {
				String[] values = line.split(delimiter);
				String word = values[wordCol];
				
				if(word.matches("\\w+-?\\w") && word.length() == charLimit) {
					// words must not contain special characters
					// words must have the specified character length
					words.add(word.toLowerCase());
				}
			}
		}		
		catch(Exception e) {
			e.printStackTrace();
		}
		
		// remove duplicates via sort
		Collections.sort(words);
		Collections.reverse(words);
		String lastWord = "";
		// since removing from arraylist shifts the index, only increment if lastWord is different
		for(int i = 0; i < words.size();) {
			String word = words.get(i);
			if(lastWord.equals(word)) {
				words.remove(i);
				//System.out.println("removed " + word);
			}
			else {
				i++;
			}
			lastWord = word;
		}
		
		// trim the list to the word cap
		if(wordCap > -1) {
			// list of words that must be kept to keep a valid demo
			ArrayList<String> whiteList =  new ArrayList<>(){{
				add("fool");
				add("pool");
				add("poll");
				add("pole");
				add("pale");
				add("sale");
				add("sage");
			}};
			wordCap = words.size() - wordCap;
			ArrayList<Integer> keepIdx = new ArrayList<>();
			Random rand = new Random();
			
			for(int i = 0; i < wordCap; i++) {
				int idx = rand.nextInt(0, words.size());
				// do not remove whitelisted words
				if(whiteList.contains(words.get(idx))) {
					// reset counter & pick new random number
					i--;
				}
				else {
					words.remove(idx);
				}
			}
		}
		System.out.println(words);
		
		// create text file for re-use
	    try {
			FileWriter myWriter = new FileWriter(fileName);
			for(String word : words) {
				myWriter.write(word + "\r\n");
			}
			myWriter.close();
			System.out.println("Created file for reuse: " + fileName);
		}
	    catch (IOException e) {
			System.out.println("An error occurred.");
			e.printStackTrace();
	    }
    }
}
