package app;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
 
public class EvaluateText// extends CountWords
{
	CountWords cwo;
	String content = "";
    public EvaluateText(String filename, boolean collectWords, boolean collectNumbers, boolean collectSymbols) {
        String filePath = filename;
 
        System.out.println( readAllBytesJava7( filePath ) );
        cwo = new CountWords(content, collectWords, collectNumbers, collectSymbols);
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