package app;

/** diff code taken from 
 * Java Blog Buch By Stefan Kiesel Published May 27, 2009, edited May 6, 2010 at 20:47 
 * https://www.java-blog-buch.de/c-levenshtein-distanz/ 
 * downloaded on March 16, 2021 at 14:52 GMT+1
 * */
import java.util.*;

public class StringCompare {
	
	private String subseq ="";
	private static int NUMW = 5;
	private static double ALLOVAR = 0.5;
	
	private LinkedList<String> words;
	
	public StringCompare() {
		subseq = "";
		words = new LinkedList<String>();
	}
	
	private int diff(String firstword, String secondword) {
		String word_a, word_b;
		//first word will be the longer word
		if(firstword.length()>=secondword.length()) {
			word_a = firstword;
			word_b = secondword;
		}
		else {
			word_b = firstword;
			word_a = secondword;
		}
		
		subseq = "";
		int matrix[][] = new int[word_a.length() + 1][word_b.length() + 1];
		int i;
		for (i = 0; i < word_a.length() + 1; i++) {
			matrix[i][0] = i;
		}
		for (i = 0; i < word_b.length() + 1; i++) {
			matrix[0][i] = i;
		}
		for (int a = 1; a < word_a.length() + 1; a++) {
			for (int b = 1; b < word_b.length() + 1; b++) {
				int right = 0;
				if (word_a.charAt(a - 1) != word_b.charAt(b - 1)) {
					right = 1;
				}
				int mini = matrix[a - 1][b] + 1;
				if (matrix[a][b - 1] + 1 < mini) {
					mini = matrix[a][b - 1] + 1;
				}
				if (matrix[a - 1][b - 1] + right < mini) {
					mini = matrix[a - 1][b - 1] + right;
				}
				matrix[a][b] = mini;
			}
		}
		//blanks for differences in similar words
		if (matrix[word_a.length()][word_b.length()] < ALLOVAR *word_a.length()) {
			subseq="###";
		}
		return matrix[word_a.length()][word_b.length()];
	}
	
	private String showDifference(String a, String b, Localization lang) {
		/** compare two words, return message **/
		String worda = a, wordb = b;
		int occa=0, occb=0;
		
		try {
			if(a.contains("\t")) {
				worda = a.substring(0,a.indexOf("\t"));
				occa = Integer.parseInt(a.substring(a.indexOf("\t")+1));
			}
			if(b.contains("\t")) {
				wordb = b.substring(0,b.indexOf("\t"));
				occb = Integer.parseInt(b.substring(b.indexOf("\t")+1));
			}
			
			diff(worda, wordb);
			//TODO remove subseq handling
			if(subseq.length() > 0) {
				if(ShortWords.isStopWordDE(worda) && ShortWords.isStopWordDE(wordb)) {
					return "";
				}
				if(ShortWords.isStopWordEN(worda) && ShortWords.isStopWordEN(wordb)) {
					return "";
				}
				//when words are similar
				return "; <em>"+ ((occa>=occb && occb>0)?lang.Prefix("also"):lang.Prefix("more"))
					+ ":</em> " + b + " (" + Integer.toString(occb)+ "x) ";
			}
			return "";
		}
		catch(Exception sdEx) {
			//entry frequency parsing failed
			diff(worda, wordb);
			return "; "+ b + " (nn "+lang.Prefix("times")+")";
		}
	}
	
	public String compareWords(String a, Localization lang) {
		/** word is compared to the preceding {NUMW} words */
		String dfs = a;
		for(String s: words) {
			dfs += showDifference(a,s,lang);
		}
		//shift by one
		words.addLast(a);
		if(words.size() > NUMW) {
			words.removeFirst();
		}
		return dfs;
	}
	
}
