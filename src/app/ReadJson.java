package app;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
//the jar file for org.json.* is found in https://mvnrepository.com/artifact/org.json/json
//source code at https://github.com/stleary/JSON-java

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
        	InputStream inputAsStream = this.getClass().getResourceAsStream("/json/searchwords.json");
        	String jsonString = new String(inputAsStream.readAllBytes());
        	
        	obj = new JSONObject(jsonString);
        	Iterator<String> keysIt = obj.keys();
        	List<String> keysList = new ArrayList<String>();
        	while(keysIt.hasNext()) {
        	    String key = (String) keysIt.next();
        	    keysList.add(key);
        	}
        	
        	keysList.sort(null);
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
