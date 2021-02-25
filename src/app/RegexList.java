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
	
	public static boolean hasISBN(String s) {
		Matcher mat = simplePatternISBN.matcher(s);
		return mat.find();
	}
	
	public static String patnum = "[A-Z][A-Z][0-9][0-9]*[A-Z][0-9]?";
	public static Pattern patnumpattern = Pattern.compile(patnum);
	
	public static boolean hasPatentNumber(String s) {
		Matcher mat = patnumpattern.matcher(s);
		return mat.find();
	}
	
	/*public static String refsign = "[(][0-9][0-9]*[)]";
	public static Pattern refpattern = Pattern.compile(refsign);*/
	
	//TODO verfeinere den Inhalt der Klammern
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
