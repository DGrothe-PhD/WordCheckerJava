package app;

import org.json.JSONObject;

public class Localization extends ReadJson{
	JSONObject dic;
	CharRep charRep;
	
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
		charRep = new CharRep();
		switchTo("EN");		
	}
	
	public String Header(String key) {
		return lookUpField(key);
	}
	
	public String getLocalizedText(String key) {
		return charRep.toANSI(lookUpField(key));
	}
	
	public String wrapPrefix(String key) {
		return "- " + lookUpField(key) + ": ";
	}	
}
