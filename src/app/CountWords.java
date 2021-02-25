package app;

import java.util.ArrayList;
//Für Anzahl Vorkommen gebraucht:
import java.util.*; 

public class CountWords {
	public int num_of_lines;
	public int num_of_words;
	/*private String[][] brackets = {{"(",")"}, {"[","]"}, {"{","}"},
			{"\"","\""}, {"„","“"}, {"«","»"}, {"»","«"}};*/

	public ArrayList<String> CountWordReportLines = new ArrayList<String>();
	
	public int GetNOL() {
		return num_of_lines;
	}
	public int GetNOW() {
		return num_of_words;
	}
	
	//private ArrayList<String> result = new ArrayList<String>();
	//private ArrayList<Integer> numbers = new ArrayList<Integer>();
	//create a lookup table
	private HashMap<String, Integer> WordsList = new HashMap<String, Integer>();
	
	//this is the outer interface that outputs the data.
	public ArrayList<String> GetWordsList() {
		return CountWordReportLines;
	}
	
	public void AddWord(String str) {
		String str2 = str.trim();
		//@NOTE replaceAll uses regex, whereas replace simply does not!
		//so replace would remove points in words like in URLs
		str2= str2.replaceAll("\\.$","").replaceAll(",$","");
		str2= str2.replaceAll("[\";„“»«]","");
		// Finding words and incrementing occurrence counts
		int q /*, iof*/;
		
		if (WordsList.containsKey(str2)) {
			q=WordsList.get(str2);
			q+=1;
			WordsList.replace(str2, q);
		}
		else {
			WordsList.put(str2, 1);//Add word to word list
		}
	}
	public CountWords() {
		;
	}
	public CountWords(String str){
		String[] lines = str.split("\n");
		num_of_lines = lines.length;
		int j, k;
		ArrayList<String> buf = new ArrayList<String>();
		for (j=0; j<num_of_lines; j++) {
			for (String bar: lines[j].split(" ")) {
				buf.add(bar);
			}
		}
		num_of_words = buf.size();
		for (k=0;k<num_of_words; k++) {
			AddWord(buf.get(k));
		}
		
		ArrayList<String> ResultWordList = new ArrayList<String>();
		
		for( Map.Entry<String, Integer> entry : WordsList.entrySet() ){
		    ResultWordList.add(entry.getKey());
		}
		Collections.sort(ResultWordList);
		
		//So let's print out the WordsList entries in alphabetical order.
		for( String entry : ResultWordList ){
			String worep = entry + "\t" + WordsList.get(entry);
			CountWordReportLines.add(worep);
		}

		System.out.println(num_of_words+" words counted!");
	}
}
