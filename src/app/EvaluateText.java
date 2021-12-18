package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
 
public class EvaluateText
{
	CountWords cwo;
	String content = "";

	public EvaluateText(String input, int mode, Localization lang) {
		cwo = new CountWords(input, mode, lang);
	}
    public void eTextToolBox(String filename, Localization lang) throws Exception {
        String filePath = filename;
 
        System.out.println( readAllBytesJava7( filePath, lang ) );
        cwo.cwoToolBox(content);
    }
    
    public ArrayList<String> GetWordsList() {
    	return cwo.GetWordsList();
    }
    public int GetNumberOfWords() {
    	return cwo.GetNOW();
    }
 
    private int readAllBytesJava7(String filePath, Localization lang) 
    		throws IOException, WriteException {
        try {
        	content = new String ( Files.readAllBytes( Paths.get(filePath)) );
        } 
        catch (IOException e) {
            throw e;
        }
        catch (Exception e) {
        	throw new WriteException(lang.Messages("Internal error"));
        }
        return 0;
    }
}