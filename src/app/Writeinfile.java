package app;

import java.io.BufferedWriter;
import java.util.ArrayList;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Writeinfile {
	PrintWriter pInit = null;
	PrintWriter pWriter = null;
	int type_of_word = 0;
	ArrayList<String> specialTokens = new ArrayList<String>();
	ArrayList<String> refSignTokens = new ArrayList<String>();
	
    public Writeinfile(String filename, String topic) {
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
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {;}
    }
    
    /** complete the html section */
    public void insertParEnd() {
    	pWriter.println(type_of_word==0?"":"</details><br>");
    }
    
    private String[] uml = {"Ã", "Ä", "Ü", "Ö", "ä", "ö", "ü", "Ø", "ø", "Å", "å", "Æ", "æ",
    		"ç", "Ç", "Ñ", "ñ", "á", "à", "â", "é", "è", "ê", "ë", "í", "ï", "ì", "î", "ó", "ò", "ô", "ú", "ù", "û",
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

    public void storeAllItems(ArrayList<String> targs) {
    	char cap = '0';
    	//cap will be set to first char of first word in alphabetical order
    	//regardless of which
    	for( String entry : targs ){
    		if (Character.isDigit(entry.charAt(0))) {
    			if(RegexList.hasISBN(entry)) {
   					specialTokens.add("ISBN candidate: "+entry);
   					continue;
    			}
    			else if(RegexList.hasDateTime(entry)) {
    				specialTokens.add("Date or time pattern_ "+ entry);
    				continue;
    			}
    			else if(type_of_word != 1) {
    				insertParEnd();
    				pWriter.println("<details><summary>Tokens beginning with a digit:</summary>");
    				type_of_word = 1;
    			}
			}
			else if (Character.isLetter(entry.charAt(0))){
				if (RegexList.hasPatentNumber(entry)){
					specialTokens.add(entry);
					continue;
				}
				else {
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
				}
			}
			else if (!Character.isLetterOrDigit(entry.charAt(0))){
				if(RegexList.hasRefSign(entry)) {
					refSignTokens.add(entry);
					continue;
				}
				else if(type_of_word != 3) {
					insertParEnd();
					pWriter.println("<details><summary>Special tokens found:</summary>");
					type_of_word = 3;
				}
			}
		    pWriter.println( entry+"<br>");
		}
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
    
    /** finish the html file */
    public void finishWriting() {
    	insertParEnd();
    	pWriter.println("</font></body></html>");
    	pWriter.flush();
        pWriter.close();
    }
    
}
