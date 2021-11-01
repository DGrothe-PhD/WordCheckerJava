package app;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.io.FileWriter;
import java.io.PrintWriter;

public class Writeinfile {
	PrintWriter pInit = null;
	PrintWriter pWriter = null;
	int type_of_word = 0;
	ArrayList<String> specialTokens = new ArrayList<String>();
	ArrayList<String> refSignTokens = new ArrayList<String>();
	ArrayList<String> userSearchTokens = new ArrayList<String>();
	
	public Writeinfile(String filename, String topic) throws WriteFileException {
		pWriter = null;
		try {
			pInit = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			pInit.println("<html><head>");
			pInit.println("<title>Result</title><meta charset=\"UTF-8\"></meta>");
			pInit.println("<style>details > summary {font-weight:bold;color:navy;} h3 {color:maroon;}</style>");
			pInit.println("</head><body><font face=\"Helvetica\">");
			pInit.println("<h1>Results of "+topic+":</h1>");
			pInit.flush();
			pInit.close();
			//the boolean true inside FileWriter says to append the content
			pWriter = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			// file overwritten, newline at line ends is automatically added
		} 
		catch (Exception wfe) {
			throw new WriteFileException("Could not initialize file writing.");
		}
	}
	
	/** complete the html section */
	public void insertParEnd() {
		pWriter.println(type_of_word==0?"":"</details><br>");
	}
	
	private String[] uml = {"Ã", "Ä", "Ü", "Ö", "ä", "ö", "ü", "Ø", "ø", "Å",
			"å", "Æ", "æ", "ç", "Ç", "Ñ", "ñ", "á", "à", "â", "é", "è", "ê",
			"ë", "í", "ï", "ì", "î", "ó", "ò", "ô", "ú", "ù", "û",
			};
	
	public String startsWithUmlaut(String str) {
		String u = "";
		if(str.charAt(0) == (char)0xc3) {
			u+= (""+ str.charAt(0)+str.charAt(1));
		}
		else {
			for (String s: uml) {
				if(str.startsWith(s)) {
					u= s;
					break;
				}
			}
		}
		return u;
	}

	public void storeUserTerms() {
		if(!userSearchTokens.isEmpty()) {
			insertParEnd();
			pWriter.println("<details><summary>Search terms found:</summary>");
			for(String o : userSearchTokens) {
				pWriter.println(o+"<br>");
			}
		}
	}
	
	public void storeAllItems(ArrayList<String> targs) throws WriteFileException {
		
		storeUserTerms();
		char cap = '0';
		//cap will be set to first char of first word in alphabetical order
		//regardless of which
		try {
			for( String entry : targs ){
				if (Character.isDigit(entry.charAt(0))) {
					if(RegexList.hasISBN(entry)) {
						specialTokens.add("ISBN candidate: "+entry);
						continue;
					}
					else if(RegexList.hasDateTime(entry)) {
						specialTokens.add("Date or time pattern: "+ entry);
						continue;
					}
					else if(type_of_word != 1) {
						insertParEnd();
						pWriter.println("<details><summary>Tokens beginning with a digit:</summary>");
						type_of_word = 1;
					}
				}
				else if (Character.isAlphabetic(entry.charAt(0))){
					//including words, URLs, etc.
					if (RegexList.hasPatentNumber(entry)){
						specialTokens.add(entry);
						continue;
					}
					else if(RegexList.hasEMail(entry)){
						specialTokens.add("E-Mail address found: " +entry);
						continue;
					}
					else if(RegexList.hasURL(entry)){
						specialTokens.add("URL: " + entry);
						continue;
					}
					else {
						//words
						if(type_of_word != 2) {
							insertParEnd();
							pWriter.println("<details><summary>Words:</summary>");
						}
						type_of_word = 2;
						char curr_char = entry.charAt(0);
						
						String u = startsWithUmlaut(entry);
						
						if(curr_char!= cap) {
							//insert an alph. section line if the word starts with different character
							//insert full first symbol if umlaut
							cap = curr_char;
							pWriter.println("");
							pWriter.println("<h3>__"+(u==""?cap:"Umlaut")+"____</h3>");
						}
						pWriter.println( StringCompare.compareWords(entry)+"<br>");
						continue;
					}
				}
				else if (!Character.isLetterOrDigit(entry.charAt(0))){
					entry = entry.trim();
					if(RegexList.hasRefSign(entry)) {
						refSignTokens.add(entry);
						continue;
					}
					else if (entry.length()>0) {
						specialTokens.add(entry);
						continue;
					}
				}
				pWriter.println( entry+"<br>");
			}
			insertParEnd();
			pWriter.println("<details><summary>Special tokens found:</summary>");
			Collections.sort(specialTokens);
			for (String o:specialTokens) {
					pWriter.println(o+"<br>");
			}
			if(refSignTokens.size()>0) {
				insertParEnd();
				pWriter.println("<details><summary>Reference signs found:</summary>");
				for (String o:refSignTokens) {
					pWriter.println(o+"<br>");
				}
			}
		}
		catch(Exception wfe){
			throw new WriteFileException("Error: File may have not been written.");
		}
	}
	
	/** finish the html file */
	public void finishWriting() {
		insertParEnd();
		pWriter.println("</font></body></html>");
		pWriter.flush();
		pWriter.close();
	}
}
