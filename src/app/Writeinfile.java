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
            pInit.println("<title>Result</title>");
            pInit.println("<style>details > summary {font-weight:bold;color:navy;}</style>");
            pInit.println("</head><body><font face=\"Helvetica\">");
            pInit.println("<h1>Results of "+topic+":</h1>");
            pInit.flush();
            pInit.close();
        	//the boolean true inside FileWriter says to append the content
            pWriter = new PrintWriter(new BufferedWriter(new FileWriter(filename, true)));
            // file is silently overwritten, and newline at each line is automatically added
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } finally {;}
    }
    
    public void insertParEnd() {
    	pWriter.println(type_of_word==0?"":"</details><br>");
    }

    public void storeAllItems(ArrayList<String> targs) {
    	char cap = '0';
    	//cap automatically set to first char of first word in alphabetical order
    	//regardless of which
    	for( String entry : targs ){
    		if (Character.isDigit(entry.charAt(0))) {
    			if(RegexList.hasISBN(entry)) {
   					specialTokens.add("ISBN candidate: "+entry);
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
					char curr_char = Character.toUpperCase(entry.charAt(0));
					if(curr_char!= cap) {
						//insert an alph. section line if the word starts with different character
						cap = curr_char;
						pWriter.println("");
						pWriter.println("<h3>__"+cap+"____</h3>");
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
    
    public void finishWriting() {
    	insertParEnd();
    	pWriter.println("</font></body></html>");
    	pWriter.flush();
        pWriter.close();
    }
    
}
