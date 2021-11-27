package app;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import org.json.*;
//the jar file for org.json.* is found in https://mvnrepository.com/artifact/org.json/json

public class Readinfile {
    public Readinfile(String filename) {

        File file = new File(filename);

        try (FileInputStream fis = new FileInputStream(file)) {
            System.out.println("Total file size to read (in bytes) : "+ fis.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class ReadJson {
	String searchwords = "";
	String[] keysArray;
	String jsonString;
	JSONObject obj;
	
    public String get(String listname) {    	
        try {
        	searchwords ="";
        	JSONArray  arr = obj.getJSONArray(listname);
        	
        	for (int i=0;i<arr.length(); i++) {
        		searchwords += arr.get(i)+ System.lineSeparator();
        	}
        	return searchwords;
        } catch (Exception e) {
        	return searchwords;
        }
    }
    
    public ReadJson() {

    	jsonString = "";
    	
        try {
        	jsonString = new String(Files.readAllBytes( Paths.get("src/json/searchwords.json")));
        	obj = new JSONObject(jsonString);
        	Iterator<String> keysIt = obj.keys();
        	List<String> keysList = new ArrayList<String>();
        	while(keysIt.hasNext()) {
        	    String key = (String) keysIt.next();
        	    keysList.add(key);
        	}
        	keysArray = keysList.toArray(new String[keysList.size()]);
        	
        } catch (Exception e) {
        	searchwords="Word list is not available.";
        }
    }
    public String get() {
    	return searchwords;
    }
    public String[] getAll() {
    	return keysArray;
    }
}