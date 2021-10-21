package app;

import java.util.ArrayList;
//Für Anzahl Vorkommen gebraucht:
import java.util.*; 

/** Words counting class */
public class CountWords extends CharRep {
	public int num_of_lines;
	public int num_of_words;
	private ArrayList<String> buf;
		
	public boolean collectNumbers = true, collectSymbols = true, collectWords = true;
	/*private String[][] brackets = {{"(",")"}, {"[","]"}, {"{","}"},
			{"\"","\""}, {"„","“"}, {"«","»"}, {"»","«"}};*/
	// punctuation ",;\"\\/()[]{}!?"
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
	
	
	/** removing unbalanced brackets, end punctuation*/
	private String TrimWord(String str) {
		String str2 = str.trim();
		//replaceAll uses regex, whereas replace simply does not!
		//remove points
		str2= str2.replaceAll("[\";„“»«]","");
		str2= str2.replaceAll("[,\u2019\\.\\:;',\\!\\?]*$","");
		str2=str2.replaceAll("^[\\u8303\\u8217\\’\\']", "").replaceAll("[\\’\\']$", "");
		//remove unicode quotation marks; test for Â
		str2=clarify(str2);
		//
		if(str2.startsWith("(") && str2.endsWith(")")) {
			if(Character.isAlphabetic(str2.charAt(1)) 
					&& Character.isLetterOrDigit(str2.charAt(str2.length()-2))) {
				// remove brackets around words
				str2=str2.replaceAll("^\\(", "").replaceAll("\\)$", "");
			}
		}
		else if(str2.contains("(") ^ str2.contains(")")) {
			//remove only unbalanced outer brackets
			str2=str2.replaceAll("^\\(", "").replaceAll("\\)$", "");
		}
		return str2;
	}
	
	/** Add a word to the WordsList. If new, add, if already in list, increment counter */
	public void AddWord(String str) {
		try {
			String str2 = TrimWord(str);
			// Finding words and incrementing occurrence counts
			int q;
			
			if (WordsList.containsKey(str2)) {
				q=WordsList.get(str2);
				q+=1;
				WordsList.replace(str2, q);
			}
			else {
				WordsList.put(str2, 1);//Add word to word list
			}
		}
		catch(Exception e) {
			System.out.println("CountWords' internal error");
		}
	}
	
	/** this method shall gather alphanumeric sequences with low amount of hyphens etc. */
	public void AddNumbersAndWords(String str) {
		String str2 = TrimWord(str);
		
		try {
		// Finding words and incrementing occurrence counts
			int q;
			if (RegexList.hasSymbols(str2) == false) {
				if (WordsList.containsKey(str2)) {
					q=WordsList.get(str2);
					q+=1;
					WordsList.replace(str2, q);
				}
				else {
					WordsList.put(str2, 1);//Add word to word list
				}
			}
		}
		catch(Exception e) {
			System.out.println("CountWords' internal error");
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
		buf = new ArrayList<String>();
		for (j=0; j<num_of_lines; j++) {
			lines[j] = lines[j].replace("\t", " ");
			lines[j] = lines[j].replace(thebom, "");
			
			phraseFinder(lines[j]);
			
			for (String bar: lines[j].split(" ")) {
				buf.add(bar);
			}
		}
		num_of_words = buf.size();
		
		if(!collectSymbols) {
			for (k=0;k<num_of_words; k++) {
				//TODO should e.g. not collect numbers if user unchecks that box
				//"boolean collectWords, boolean collectNumbers" should have effect
				AddNumbersAndWords(buf.get(k));
			}
		}
		else {
			for (k=0;k<num_of_words; k++) {
			//TODO should e.g. not collect numbers if user unchecks that box
			//"boolean collectWords, boolean collectNumbers" should have effect
			AddWord(buf.get(k));
			}
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
	
	void phraseFinder(String line) {
		for(String w: ShortWords.ListOfPhrases) {
			if(line.contains(w)) {
				buf.add(" - Phrase found: "+ w);
			}
		}
	}
	
}
