package app;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
 
public class EvaluateText// extends CountWords
{
	CountWords cwo;
	String content = "";
    public EvaluateText(String filename) 
    {
        String filePath = filename;
 
        System.out.println( readAllBytesJava7( filePath ) );
        cwo = new CountWords(content);
    }
    
    public ArrayList<String> GetWordsList() {
    	return cwo.GetWordsList();
    }
 
    //Read file content into string with - Files.readAllBytes(Path path)
 
    private int readAllBytesJava7(String filePath) 
    {
        //CountWords cwo;
        try
        {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        } 
        catch (IOException e) 
        {
            e.printStackTrace();
        }
        return 0;
    }
}