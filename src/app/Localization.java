package app;

import java.io.InputStream;
import java.util.*;

import org.json.JSONObject;

public class Localization extends ReadJson{
	JSONObject dic;

	static Dictionary<String, String> headers = new Hashtable<String, String>();
	static Dictionary<String, String> prefixes = new Hashtable<String, String>();
	static Dictionary<String, String> suffixes = new Hashtable<String, String>();
	static Dictionary<String, String> messages = new Hashtable<String, String>();
	static Dictionary<String, String> Localizing = new Hashtable<String, String>();
	
	protected void switchTo(String listname){
		dic = obj.getJSONObject(listname);
	}
	
	private String lookUpField(String key) {
    	if(dic.has(key))
    		return dic.getString(key);
    	System.out.println("Key "+key+" was not found.");
    	return "Empty key";
    }
	
	public Localization(){
		super("/json/localized.json");
		switchTo("EN");
		
		prefixes.put("ISBN", "ISBN candidate");
		prefixes.put("URL", "URL");
		prefixes.put("EMail", "E-Mail address found");
		prefixes.put("Datetime", "Date or time pattern");
		prefixes.put("Found", "Found");
		prefixes.put("Phrasefound", "Standard typo");
		prefixes.put("also", "also found");
		prefixes.put("more", "more frequent");
		prefixes.put("times", "times");
		prefixes.put("in", "in");
		
		suffixes.put("wordscounted", " words counted.");
		
		messages.put("FileNotRead", "File could not be read");
		messages.put("FilePrepFailed", "Could not prepare output file.");
		messages.put("GitHubNotOpening", "GitHub could not be opened in your browser.");
		messages.put("OutputFileNotOpening", "Output file could not be opened.");
		messages.put("Internal error", "Some internal error");
		messages.put("jsonfailed", "Word list is not available.");
		messages.put("PossibleFileWriteError", "Error: File may have not been written.");
		messages.put("suggestFileType", "Please choose a text file, of types:");
		messages.put("BadSymbolInFilename", "Symbols {0} are not allowed in filenames");
		messages.put("PleaseSelectFile", "Please select a file before clicking Start.");
		
		Localizing.put("description", "analyze text files and find words");
		Localizing.put("Search terms:", "Search terms:");
		Localizing.put("Edit search terms:", "Edit search terms:");
		Localizing.put("Type topic:", "Type topic:");
		Localizing.put("Result word list", "Result word list");
		Localizing.put("Target file:", "Target file:");
		Localizing.put("Word_occurrences.html", "Word_occurrences.html");
		Localizing.put("Selected folder:", "Selected folder:");
		Localizing.put("Selected file:", "Selected file:");
		Localizing.put("Select file", "Select file");
		Localizing.put("Open file", "Open file");
		Localizing.put("Start", "Start");
		Localizing.put("Show Results", "Show Results");
		Localizing.put("Close window", "Close window");
		Localizing.put("Clear search", "Clear search");
        
		Localizing.put("Please select a file", "Please select a file");
		Localizing.put("Info:","Info:");
		Localizing.put("Numbers", "Numbers");
		Localizing.put("Symbols", "Symbols");
		Localizing.put("Words", "Words");
		Localizing.put("Search terms", "Search terms");
		Localizing.put("ChooseList", "choose a list");
	}
	
	public String Header(String key) {
		return lookUpField(key);
	}
	
	public String Prefix(String key) {
		return prefixes.get(key);
	}
	
	public String wrapPrefix(String key) {
		return "- " + prefixes.get(key) + ": ";
	}
	
	public String Suffix(String key) {
		return suffixes.get(key);
	}
	
	public String Messages(String key) {
		return messages.get(key);
	}
	
	public String Element(String key, Dictionary<String, String> dic) {
		if(((Hashtable<String, String>) dic).containsKey(key))
			return dic.get(key);
		System.out.println("Key "+key+" was not found.");
		return "Empty key";
	}
	
	public String Element(String key) {
		if(((Hashtable<String, String>) Localizing).containsKey(key))
			return Localizing.get(key);
		System.out.println("Key "+key+" was not found.");
		return "Empty key";
	}
}
