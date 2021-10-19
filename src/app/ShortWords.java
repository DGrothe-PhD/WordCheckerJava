package app;
import java.util.*;

public class ShortWords {
	// caps : case-sensitive words
	static String[] capsDE = {
		"AG", "mbB", "Bälde", "Fall", "Frau", "GmbH", "Inc", "Info", "Ltd", "Mann", "Paar", 
		"Seite", "Tages", "Teile", "Titel", "Herr"
	};
	static String[] capsEN = {
			"Mr", "Mrs", "Miss", "GmbH", "Inc", "Corp", "Ltd"
	};
	
	static String[] shwEN = {
		"and", "been", "by", "his", "her", "is", "it", "my", "mine", "of", "off", "on", "or", "take", "that", "the", "they", "them", 
		"then", "this", "these", "when"
	};
	
	static String[] shwDE = {
		"ab", "am", "an", "ca", "da", "du", "er", "es", "im", "in", "ja", 
		"je", "ob", "so", "um", "wo", "zu", "aber", "acht", "alle", "allem", 
		"allen", "aller", "alles", "allg", "allzu", "als", "also", 
		"ander", "ans", "auch", "auf", "aus", "autor", "außen", "bald", 
		"bei", "beide", "beim", "bevor", "bin", "bis", "bist", 
		"bitte", "blieb", "bloß", "bzw", "circa", "dabei", "dafür", 
		"daher", "dahin", "damit", "dank", "danke", "dann", "daran", "darf", 
		"darin", "darum", "das", "dass", "davon", "davor", "dazu", "dein", 
		"deine", "dem", "den", "denen", "denn", "der", "derem", "deren", 
		"derer", "des", "desto", "dich", "die", "dies", "diese", "diesen", "diesem", 
		"dieser", "dieses", "dinge", "dir", "doch", "dort", "dran", "drauf", 
		"drei", "drin", "drum", "durch", "eben", "ehe", "eher", "eigen", "ein", 
		"eine", "einem", "einen", "einer", "eines", "einig", "eins", "einst", 
		"ergo", "erst", "erste", "etc", "etwa", "etwas", "euch", "euer", "eure", 
		"eurem", "euren", "eurer", "eures", "falls", "fand", "fast", "finde", "for", 
		"fort", "frei", "freie", "fünf", "für", "fürs", "gab", 
		"ganz", "ganze", "gar", "geb", "geben", "gegen", "gehen", "geht", 
		"genau", "genug", "gern", "getan", "ggf", "gib", "gibt", "gilt", "gut", 
		"gute", "guten", "habe", "haben", "habt", "halb", "hallo", "hast", "hat", 
		"hatte", "hatten", "her", "herum", "heute", "hier", "hin", "hoch", "http", "hätte", 
		"hätte", "ich", "ihm", "ihn", "ihnen", "ihr", "ihre", "ihrem", "ihren", 
		"ihrer", "ihres", "immer", "indem", "innen", "ins", "ist", "jede", "jedem", 
		"jeden", "jeder", "jedes", "jene", "jenem", "jenen", "jener", "jenes", 
		"jetzt", "jung", "junge", "kam", "kann", "kaum", "kein", "keine", "klar", 
		"klare", "klein", "komme", "kommt", "könnt", "lag", "lagen", "laut", "leer", 
		"legen", "legte", "lesen", "liegt", "liest", "links", "mache", "macht", 
		"mag", "magst", "mal", "man", "manch", "mehr", "mein", "meine", "meiner",
		"meinem", "meinen", "meins", "meist", "meta", "mich", "mir", "mit", 
		"muss", "musst", "mögen", "nach", 
		"nacht", "nahm", "neben", "nein", "neu", "neue", "neuem", "neuen", "neuer", 
		"neues", "neun", "nicht", "nie", "nimm", "nimmt", "noch", "nun", "nur", "nutzt", 
		"nützt", "oben", "ober", "oder", "oft", "ohne", "paar", "per", "pro", 
		"quasi", "recht", "rief", "rund", "sage", "sagen", "sagt", "sagte", "samt", 
		"sang", "schon", "sechs", "sehe", "sehen", "sehr", "seht", "sei", "seid", 
		"seien", "seiet", "sein", "seine", "seiner", "seit", "selbe", "senke", "senkt", 
		"setzt", "sich", "sie", "siehe", "sieht", "sind", "singt", "sog", "sogar", 
		"solch", "soll", "sollt", "sollte", "somit", "sonst", "sooft", "sowie", "statt", 
		"steht", "stets", "stieg", "such", "tat", 
		"tief", "toll", "total", "trage", "trug", "trägt", "tun", "tust", 
		"tut", "täte", "ueber", "ums", "umso", "und", "uns", "unser",
		"unsere", "unten", "unter", "usw", "viel", 
		"viele", "vielen", "vier", "vom", "von", "vor", "voran", "vorne", "waere", "wann", 
		"war", "waren", "warst", "wart", "warum", "was", "weder", "weg", "wegen", 
		"weil", "weit", "weiß", "wem", "wen", "wenig", "wenn", "wer", "werde", 
		"wie", "wieso", "will", "wir", "wird", "wirst", "wobei", "wofür", 
		"woher", "wohin", "wohl", "wolle", "wollt", "womit", "woran", "worin", 
		"wurde", "www", "wäre", "wäre", "wären", "würde", "zehn", "zieht", 
		"zirka", "zog", "zogen", "zudem", "zum", "zumal", "zur", "zwar", 
		"zwei", "zwölf", "öfter", "übel", "über", "übrig"
	};
	
	public static List<String> shortlistDE = Arrays.asList(shwDE);
	public static List<String> shortlistEN = Arrays.asList(shwEN);
	public static List<String> uppercaselistDE = Arrays.asList(capsDE);
	public static List<String> uppercaselistEN = Arrays.asList(capsEN);
	
	public static boolean isStopWordDE(String sz) {
		if(shortlistDE.contains(sz.toLowerCase())) {return true;}
		else if(uppercaselistDE.contains(sz)) {return true;}
		else return false;
	}
	
	public static boolean isStopWordEN(String sz) {
		if(shortlistEN.contains(sz.toLowerCase())) {return true;}
		else if(uppercaselistEN.contains(sz)) {return true;}
		else return false;
	}
}
