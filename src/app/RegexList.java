package app;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexList {
	/*public static String regexISBN = "^(?:ISBN(?:-1[03])?:? )?(?=[0-9X]{10}$|(?=(?:[0-9]+["
			+ "- ]){3})[- 0-9X]{13}$|97[89][0-9]{10}$|(?=(?:[0-9]+[- ]){4})"
			+ "[- 0-9]{17}$)(?:97[89][- ]?)?[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]$";
	public static Pattern isbnpattern = Pattern.compile(regexISBN);*/
	
	public static String simpleisbn = "[0-9]*-[0-9]*-[0-9]*-[0-9]*";
	public static Pattern simplePatternISBN = Pattern.compile(simpleisbn);
	
	/** Finds a number code similar to an ISBN in a @param string, 
	 * @return boolean true if such an ISBN-like number was found.*/
	public static boolean hasISBN(String s) {
		Matcher mat = simplePatternISBN.matcher(s);
		return mat.find();
	}
	
	public static String datetime = "[0-9][0-9].[0-9][0-9].[0-9][0-9]*";
	public static Pattern simplePatternDateTime = Pattern.compile(datetime);
	
	/** Finds a number similar to a date or time in a @param string, 
	 * @return boolean true if such a number was found.*/
	public static boolean hasDateTime(String s) {
		Matcher mat = simplePatternDateTime.matcher(s);
		return mat.find();
	}
	
	public static String patnum = "[A-Z][A-Z][0-9][0-9]*[A-Z][0-9]?";
	public static Pattern patnumpattern = Pattern.compile(patnum);
	
	/** Finds a patent number in a @param string, 
	 * @return boolean true if patent number was found.*/
	public static boolean hasPatentNumber(String s) {
		Matcher mat = patnumpattern.matcher(s);
		return mat.find();
	}
	
	//TODO verfeinere den Inhalt der Klammern
	/** Finds a round-bracketed substring in a @param string, 
	 * @return boolean true if substring was found.*/
	public static boolean hasRefSign(String s) {
		boolean b = false;
		if(s.contains("(") && s.contains(")")) {
			b = true;
			/*if(Character.isDigit(s.charAt(1))) {
				b = true;
			}*/
		}
		return b;
	}
}
