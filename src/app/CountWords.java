package app;

import java.util.ArrayList;
import java.util.*; 

/** Words counting class */
public class CountWords extends CharRep {
	public int num_of_lines;
	public int num_of_words;
	private ArrayList<String> buf;
	
	private int mode;
	private ArrayList<String> userSearchTerms;
	
	protected static enum switchMode {
		c_Numbers (0x1),
		c_Symbols (0x2),
		c_Words (0x4),
		c_UserTerms (0x8);
		
		private int mode;
		switchMode(int mode){
			this.mode = mode;
		}
		
		public int getMode() {
			return mode;
		}
		
		public boolean isMode(int m) {
			return (m & this.mode) == this.mode;
		}
	}
	
	public CountWords(String input, int mode) {
		userSearchTerms = new ArrayList<String>();
		if(input != "") {
			String[] buf = input.split(System.lineSeparator());
			for (String x : buf) {
				String y = clarify(x);
				userSearchTerms.add(x);
				if(x != y) userSearchTerms.add(y);
			}
		}
		
		//else {userSearchTerms = null;}
		this.mode = mode;
	}
		
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
		if(str2.startsWith("- Found")) {
			str = clarify(str);
			return str;
		}
		//replaceAll uses regex, whereas replace simply does not!
		//remove points
		str2 = str2.replaceAll("[\";„“»«]","");
		str2 = str2.replaceAll("[,\u2019\\.\\:;',\\!\\?]*$","");
		str2 = str2.replaceAll("^[\\u8303\\u8217\\’\\']", "").replaceAll("[\\’\\']$", "");
		//remove unicode quotation marks; test for Â
		str2 = clarify(str2);
		//HTML protection
		str2 = str2.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
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
	public void cwoToolBox(String str){
		
		if(switchMode.c_UserTerms.isMode(mode)) {
			str = str.replace("\r\n", "\n");
			String[] lines = str.split("\n");
			
			num_of_lines = lines.length;

			buf = new ArrayList<String>();
			for (int j=0; j<num_of_lines; j++) {
				lines[j] = lines[j].replace("\t", " ");
				lines[j] = lines[j].replace(thebom, "");
				if(lines[j].length()==0) continue;
				phraseFinder(lines[j]);
				userPhraseFinder(lines[j]);
			
				for (String bar: lines[j].split(" ")) {
					buf.add(bar);
				}
			}
		}
		else {
			String[] lines = str.split("\n");
			num_of_lines = lines.length;
			buf = new ArrayList<String>();
			for (int j=0; j<num_of_lines; j++) {
				lines[j] = lines[j].replace("\t", " ");
				lines[j] = lines[j].replace(thebom, "");
				if(lines[j].length()==0) continue;
				phraseFinder(lines[j]);
			
				for (String bar: lines[j].split(" ")) {
					buf.add(bar);
				}
			}
		}
		
		num_of_words = buf.size();
		
		if(!switchMode.c_Symbols.isMode(mode)) {
			for (int k=0;k<num_of_words; k++) {
				AddNumbersAndWords(buf.get(k));
			}
		}
		else {
			for (int k=0;k<num_of_words; k++) {
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
			String worep = entry + "&emsp;\t" + WordsList.get(entry);
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
	
	void userPhraseFinder(String line) {
		if(userSearchTerms.size() == 1 && userSearchTerms.get(0).length()==0){
			return;
		}
		for(String w: userSearchTerms) {
			if(line.length()>0 && line.contains(w)) {
				int fixpoint = line.indexOf(w);
				int leftspace = 0;
				int b = fixpoint+w.length()+20;
				int rightbound = line.length();
				if(b < line.length()-1) {
					rightbound = Math.max(line.indexOf(" ",b), b);
				}
				else {
					leftspace = b - line.length()+1;
				}
				int leftbound = 0;
				if(fixpoint - 20 - leftspace > 0) {
					leftbound = Math.max(0, line.lastIndexOf(" ", fixpoint - 20 - leftspace));
				}
				String a = line.substring(leftbound,  rightbound);
				buf.add("- Found: \""+w+"\" in: "+ a);
			}
		}
	}
	
}
