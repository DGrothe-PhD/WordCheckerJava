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
	String resource;
	String[] keysArray;
	String jsonString;
	JSONObject obj;
	List<String> keysList;
    
    public ReadJson(String resource) {

    	jsonString = "";
    	
        try {
        	this.resource = resource;
        	InputStream inputAsStream = this.getClass().getResourceAsStream(resource);
        	jsonString = new String(inputAsStream.readAllBytes());
        	
        	obj = new JSONObject(jsonString);
        	
        	// standard
        	Iterator<String> keysIt = obj.keys();
        	keysList = new ArrayList<String>();
        	while(keysIt.hasNext()) {
        	    String key = (String) keysIt.next();
        	    keysList.add(key);
        	}
        	
        	keysList.sort(null);
        	keysArray = keysList.toArray(new String[keysList.size()]);
        } 
        catch (org.json.JSONException js) {
        	System.out.println("json error");
        }
        catch (Exception e) {
        	System.out.println("JSON loader failed for "+resource);
        }
    }

    public String[] getAll() {
    	return keysArray;
    }
}

class SearchWords extends ReadJson {
	String content;
	public SearchWords() {
		super("/json/searchwords.json");
		content="";
	}
	
	public String getText(String listname) {
        try {
        	content ="";
        	JSONArray  arr = obj.getJSONArray(listname);
        	
        	for (int i=0;i<arr.length(); i++) {
        		content += arr.get(i)+ System.lineSeparator();
        	}
        	return content;
        } catch (Exception e) {
        	return content;
        }
    }
	
    public String get() {
    	return content;
    }
}