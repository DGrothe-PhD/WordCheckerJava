package app;

import java.util.ArrayList;
//Für Anzahl Vorkommen gebraucht:
import java.util.*; 

/** Words counting class */
public class CountWords {
	public int num_of_lines;
	public int num_of_words;
	
	public boolean collectNumbers = true, collectSymbols = true, collectWords = true;
	/*private String[][] brackets = {{"(",")"}, {"[","]"}, {"{","}"},
			{"\"","\""}, {"„","“"}, {"«","»"}, {"»","«"}};*/

	public ArrayList<String> CountWordReportLines = new ArrayList<String>();
	
	public int GetNOL() {
		return num_of_lines;
	}
	public int GetNOW() {
		return num_of_words;
	}
	
	/** WordsList is an aggregation of entry pairs of:
	 *  &lt; string: the word, int: counts&gt; */
	private HashMap<String, Integer> WordsList = new HashMap<String, Integer>();
	
	//this outputs the data.
	public ArrayList<String> GetWordsList() {
		return CountWordReportLines;
	}
	
	/** Add a word to the WordsList. If new, add, if already in list, increment counter */
	public void AddWord(String str) {
		String str2 = str.trim();
		//replaceAll uses regex, whereas replace simply does not!
		//remove points
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
	/** empty constructor */
	public CountWords() {
		;
	}
	/** CountWords @param string: the text, that is split into lines and tokens (words). */
	public CountWords(String str, boolean collectWords, boolean collectNumbers, boolean collectSymbols){
		String[] lines = str.split("\n");
		num_of_lines = lines.length;
		int j, k;
		ArrayList<String> buf = new ArrayList<String>();
		for (j=0; j<num_of_lines; j++) {
			lines[j] = lines[j].replace("\t", " ");
			for (String bar: lines[j].split(" ")) {
				buf.add(bar);
			}
		}
		num_of_words = buf.size();
		for (k=0;k<num_of_words; k++) {
			//TODO should e.g. not collect numbers if user unchecks that box
			//"boolean collectWords, boolean collectNumbers, boolean collectSymbols" should have effect
			AddWord(buf.get(k));
		}
		
		//need to have an ArrayList as Collections.sort(HashMap) is not implemented.
		ArrayList<String> ResultWordList = new ArrayList<String>();
		
		for( Map.Entry<String, Integer> entry : WordsList.entrySet() ){
		    ResultWordList.add(entry.getKey());
		}
		Collections.sort(ResultWordList);
		
		//So let's print out WordsList entries in alph. order.
		for( String entry : ResultWordList ){
			String worep = entry + "\t" + WordsList.get(entry);
			CountWordReportLines.add(worep);
		}

		System.out.println(num_of_words+" words counted!");
	}
}
