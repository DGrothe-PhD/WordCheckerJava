package app;

/** diff code taken from 
 * Java Blog Buch By Stefan Kiesel Published May 27, 2009, edited May 6, 2010 at 20:47 
 * https://www.java-blog-buch.de/c-levenshtein-distanz/ 
 * downloaded on March 16, 2021 at 14:52 GMT+1
 * */
import java.util.*;

public class StringCompare {
	
	private String subseq ="";
	private int levenshtein_distance;
	private static int NUMW = 5;
	private static double ALLOVAR = 0.5;
	
	private LinkedList<String> words;
	
	public StringCompare() {
		subseq = "";
		levenshtein_distance = 0;
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
		
		levenshtein_distance = 0;
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
		
		levenshtein_distance = matrix[word_a.length()][word_b.length()];
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
			//
			if(subseq.length() > 0) {
				if(ShortWords.isStopWordDE(worda) && ShortWords.isStopWordDE(wordb)) {
					return "";
				}
				if(ShortWords.isStopWordEN(worda) && ShortWords.isStopWordEN(wordb)) {
					return "";
				}
				
				if(levenshtein_distance < 3) {
					return "; "+ ((occa>=occb && occb>0)?lang.Header("also")+" (A)":lang.Header("more")+" (*)")
							+ ": <em><b><font color=red>" + b + "</font></b></em>";
				}
				
				//Anagram checker
				char[] cha = worda.replace("&emsp;", "").toCharArray();
				Arrays.sort(cha);
				String ta = new String(cha);
				ta = ta.strip();
				char[] chb = wordb.replace("&emsp;", "").toCharArray();
				Arrays.sort(chb);
				String tb = new String(chb);
				tb = tb.strip();
				
				if(ta.equals(tb)) {
					return "; "+ ((occa>=occb && occb>0)?lang.Header("also")+" (A)":lang.Header("more")+" (A)")
							+ ": <em><font color=red>" + b + "</font></em>";
				}
				
				//Word starting with same letter sequence
				if(worda.substring(0,7).equals(wordb.substring(0,7))) {
					return "; "+ ((occa>=occb && occb>0)?lang.Header("also")+" (S)":lang.Header("more")+" (S)")
							+ ": <em><b><font color=blue>" + b + "</font></b></em>";
				}
				if(worda.substring(0,4).equals(wordb.substring(0,4))) {
					return "; "+ ((occa>=occb && occb>0)?lang.Header("also")+" (S)":lang.Header("more")+" (S)")
							+ ": <em><font color=navy>" + b + "</font></em>";
				}
				
				//when words are similar
				return "; <em>"+ ((occa>=occb && occb>0)?lang.Header("also"):lang.Header("more"))
					+ ":</em> " + b;
			}
			return "";
		}
		catch(Exception sdEx) {
			//entry frequency parsing failed
			diff(worda, wordb);
			return "; "+ b;
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
