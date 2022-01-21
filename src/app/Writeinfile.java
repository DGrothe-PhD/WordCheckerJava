package app;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.io.*;

public class Writeinfile {
	
	private int mode;
	private Localization lang;
	StringCompare comp;
	PrintWriter pInit = null;
	PrintWriter pWriter = null;
	int type_of_word = 0;
	boolean detailsOpen = false;
	
	ArrayList<String> specialTokens;
	ArrayList<String> refSignTokens;
	ArrayList<String> userSearchTokens;
	
	private void init() {
		specialTokens = new ArrayList<String>();
		refSignTokens = new ArrayList<String>();
		userSearchTokens = new ArrayList<String>();
		pWriter = null;
		pInit = null;
	}
	
	public Writeinfile(String filename, String topic, int mode, Localization lang)
			throws WriteException {
		init();
		this.mode = mode;
		this.lang = lang;
		try {
			pInit = new PrintWriter(new BufferedWriter(new FileWriter(filename)));
			pInit.println("<html><head>");
			pInit.println("<title>"+lang.Header("Results")
				+"</title><meta charset=\"UTF-8\"></meta>");
			pInit.println("<style>"
				+"details > summary {font-weight:bold;color:navy;} h3 {color:maroon;}"
				+"</style>");
			pInit.println("</head><body><font face=\"Helvetica\">");
			pInit.println("<h1>"+lang.Header("Results")+" - "+topic+":</h1>");
			pInit.flush();
		}
		catch (FileNotFoundException fne) {
			String fne_string = fne.toString();
			if(fne_string.contains(lang.Header("Access"))) {
				throw new WriteException(lang.Header("FilePrepFailed")
						+System.lineSeparator()+lang.Header("Access denied reason")
						+System.lineSeparator()+fne_string
				);
			}
			throw new WriteException(lang.Header("FilePrepFailed")
					+System.lineSeparator()+fne_string
			);
		}
		catch (Exception wfe) {
			throw new WriteException(lang.Header("FilePrepFailed")
					+System.lineSeparator()+wfe.toString()
			);
		}
		finally{
			if(pInit!=null) {
				pInit.flush();
				pInit.close();
			}
		}
		
		try {
			//the boolean true inside FileWriter says to append the content
			pWriter = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
			// file overwritten, newline at line ends is automatically added
		} 
		catch (Exception wfe) {
			throw new WriteException(lang.Header("PossibleFileWriteError"));
		}
	}
	
	/** complete the html section */
	private void insertParEnd() {
		if(detailsOpen) {
			pWriter.println("</details><br>");
			detailsOpen = false;
		}
	}
	private void insertNewSection(String str) {
		insertParEnd();
		pWriter.println("<details><summary>"+str+"</summary>");
		detailsOpen = true;
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

	public void storeAllItems(ArrayList<String> targs) throws WriteException {
		// stores all items, already sorted in alphabetical order, 
		//in sub-lists according to entry type
		comp = new StringCompare();
		char cap = '0';
		//cap will be set to first char of first word in alphabetical order
		//regardless of which
		try {
			for( String entry : targs ){
				if(entry.startsWith("- "+lang.Header("Found"))) {
					userSearchTokens.add(entry);
					continue;
				}
				
				if (Character.isDigit(entry.charAt(0))) {
					if(RegexList.hasISBN(entry)) {
						specialTokens.add(lang.wrapPrefix("ISBN")+entry);
						continue;
					}
					else if(RegexList.hasDateTime(entry)) {
						specialTokens.add(lang.wrapPrefix("Datetime")+ entry);
						continue;
					}
					// Plain numbers can be excluded from the printed result.
					else if(!CountWords.switchMode.c_Numbers.isMode(mode)) {
						continue;
					}
					else if(type_of_word != 1) {
						insertNewSection(lang.Header("Digits"));
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
						specialTokens.add(lang.wrapPrefix("EMail") +entry);
						continue;
					}
					else if(RegexList.hasURL(entry)){
						specialTokens.add(lang.wrapPrefix("URL") + entry);
						continue;
					}
					// Plain words can be excluded from the printed result.
					else if(!CountWords.switchMode.c_Words.isMode(mode)) {
						type_of_word = 2;
						continue;
					}
					else {
						//words
						if(type_of_word != 2) {
							insertNewSection(lang.Header("Words"));
						}
						type_of_word = 2;
						char curr_char = entry.charAt(0);
						
						String u = startsWithUmlaut(entry);
						
						if(Character.toLowerCase(curr_char)!= Character.toLowerCase(cap)) {
							//insert an alph. section line if the word starts with 
							//different character
							//insert full first symbol if umlaut
							cap = curr_char;
							pWriter.println("");
							pWriter.println("<h3>__"+(u==""?cap:"Umlaut")+"____</h3>");
						}
						
						pWriter.println( comp.compareWords(entry, lang)+"<br>");
						continue;
					}
				}
				else if (!Character.isLetterOrDigit(entry.charAt(0))){
					entry = entry.trim();
					if(RegexList.hasRefSign(entry)) {
						refSignTokens.add(entry);
						continue;
					}
					else if(!CountWords.switchMode.c_Symbols.isMode(mode)) {
						continue;
					}
					else if (entry.length()>0) {
						specialTokens.add(entry);
						continue;
					}	
				}
				
				pWriter.println( entry+"<br>");
			}
			
			insertNewSection(lang.Header("specialTokens"));
			Collections.sort(specialTokens);
			for (String o:specialTokens) {
				pWriter.println(o+"<br>");
			}
			if(refSignTokens.size()>0) {
				insertNewSection(lang.Header("refSigns"));
				for (String o:refSignTokens) {
					pWriter.println(o+"<br>");
				}
			}
			if(userSearchTokens.size()>0) {
				insertNewSection(lang.Header("userST"));
				for (String o:userSearchTokens) {
					pWriter.println(o+"<br>");
				}
			}
		}
		catch(Exception wfe){
			close();
			throw new WriteException(lang.Header("PossibleFileWriteError"));
		}
	}
	
	/** finish the html file */
	public void finishWriting() {
		insertParEnd();
		pWriter.println("</font></body></html>");
		pWriter.flush();
		pWriter.close();
	}
	
	protected void close() {
		if(pWriter!=null) pWriter.close();
	}
}
