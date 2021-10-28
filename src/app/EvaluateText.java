package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
 
public class EvaluateText
{
	CountWords cwo;
	String content = "";

	public EvaluateText(String input, int mode) {
		cwo = new CountWords(input, mode);
	}
    public void eTextToolBox(String filename) {
        String filePath = filename;
 
        System.out.println( readAllBytesJava7( filePath ) );
        cwo.cwoToolBox(content);
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