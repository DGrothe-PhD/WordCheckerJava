package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
 
public class EvaluateText
{
	CountWords cwo;
	String content = "";
	//TODO maybe use enum.
	public EvaluateText(String input) {
		cwo = new CountWords(input);
		//TODO local regression test :)
	}
    public void eTextToolBox(String filename, boolean collectWords, boolean collectNumbers, boolean collectSymbols, boolean collectUserTerms) {
        String filePath = filename;
 
        System.out.println( readAllBytesJava7( filePath ) );
        cwo.cwoToolBox(content, collectWords, collectNumbers, collectSymbols, collectUserTerms);
    }
    
    public ArrayList<String> GetWordsList() {
    	return cwo.GetWordsList();
    }
    public int GetNumberOfWords() {
    	return cwo.GetNOW();
    }
    //Read file content into string with - Files.readAllBytes(Path path)
 
    private int readAllBytesJava7(String filePath) {
        try {
        	content = new String ( Files.readAllBytes( Paths.get(filePath)) );
        } 
        catch (IOException e) {
            e.printStackTrace();
        }
        return 0;
    }
}