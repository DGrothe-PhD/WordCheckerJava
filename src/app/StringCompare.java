package app;

/** diff code taken from 
 * Java Blog Buch By Stefan Kiesel Published May 27, 2009, edited May 6, 2010 at 20:47 
 * https://www.java-blog-buch.de/c-levenshtein-distanz/ 
 * downloaded on March 16, 2021 at 14:52 GMT+1
 * */
import java.util.*;

public class StringCompare extends Localization {
	
	static Vector<Integer> mins = new Vector<Integer>();
	static String subseq ="", dfs = "", theword = "";
	private static int NUMW = 5;
	private static double ALLOVAR = 0.5;
	
	static LinkedList<String> words = new LinkedList<String>();
	
	public static int diff(String firstword, String secondword) {
		String firs, seco;
		//first word will be the longer word
		if(firstword.length()>=secondword.length()) {
			firs = firstword;
			seco = secondword;
		}
		else {
			seco = firstword;
			firs = secondword;
		}
		mins.clear();
		subseq = "";
		int matrix[][] = new int[firs.length() + 1][seco.length() + 1];
		int i;
		for (i = 0; i < firs.length() + 1; i++) {
			matrix[i][0] = i;
		}
		for (i = 0; i < seco.length() + 1; i++) {
			matrix[0][i] = i;
		}
		for (int a = 1; a < firs.length() + 1; a++) {
			for (int b = 1; b < seco.length() + 1; b++) {
				int right = 0;
				if (firs.charAt(a - 1) != seco.charAt(b - 1)) {
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
		if (matrix[firs.length()][seco.length()] < ALLOVAR *firs.length()) {
			subseq="###";
		}
		return matrix[firs.length()][seco.length()];
	}
	
	public static int getMinValue(int[] array) {
	    int minValue = array[0];
	    for (int i = 1; i < array.length; i++) {
	        if (array[i] < minValue) minValue = array[i];
	    }
	    return minValue;
	}
	
	public static String showDifference(String a, String b, Localization lang) {
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
	
	public static String compareWords(String a, Localization lang) {
		/** word is compared to the preceding {NUMW} words */
		dfs = a;
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
