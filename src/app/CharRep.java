package app;
import java.util.*;

public class CharRep {
	protected String thebom = "" +((char)0xef) + ((char)0xbb) + ((char)0xbf);
	protected String apost = "" + ((char)0xe2) + ((char)0x20ac) + ((char)0x2122);
	
	public class zeichen {
		public String ansi;
		public String utf;
		public zeichen(String a, String u) {
			ansi=a; utf=u;
		}
	}
	protected ArrayList<zeichen> umlaut = new ArrayList<zeichen>();

	public CharRep() {
		umlaut.clear();
		umlaut.add(new zeichen(""+(char)0xdc, ""+(char)0xc3+ (char)0x153));//UE
		umlaut.add(new zeichen(""+(char)0xc4, ""+(char)0xc3+ (char)0x201e));//AE
		umlaut.add(new zeichen(""+(char)0xd6, ""+(char)0xc3+ (char)0x2013));//OE
		umlaut.add(new zeichen(""+(char)0xdf, ""+(char)0xc3+ (char)0x178));//�
		umlaut.add(new zeichen(""+(char)0xe4, ""+(char)0xc3+ (char)0xa4));//�
		umlaut.add(new zeichen(""+(char)0xe5, ""+(char)0xc3 + (char)0xa5));//�
		umlaut.add(new zeichen(""+(char)0xe6, ""+(char)0xc3 + (char)0xa6));//�
		umlaut.add(new zeichen(""+(char)0xe7, ""+(char)0xc3 + (char)0xa7));//�
		umlaut.add(new zeichen(""+(char)0xe8, ""+(char)0xc3 + (char)0xa8));//�
		umlaut.add(new zeichen(""+(char)0xe9, ""+(char)0xc3 + (char)0xa9));//�
		umlaut.add(new zeichen(""+(char)0xea, ""+(char)0xc3 + (char)0xaa));//�
		umlaut.add(new zeichen(""+(char)0xeb, ""+(char)0xc3 + (char)0xab));//�
		umlaut.add(new zeichen(""+(char)0xef, ""+(char)0xc3 + (char)0xaf));//�
		umlaut.add(new zeichen(""+(char)0xf3, ""+(char)0xc3 + (char)0xb3));//�
		umlaut.add(new zeichen(""+(char)0xf4, ""+(char)0xc3 + (char)0xb4));//�
		umlaut.add(new zeichen(""+(char)0xf6, ""+(char)0xc3+(char)0xb6));//�
		umlaut.add(new zeichen(""+(char)0xfc, ""+(char)0xc3+(char)0xbc));//�
		//
		umlaut.add(new zeichen(""+(char)0xe2, ""+(char)0xc3 + (char)0xa2));//�
		umlaut.add(new zeichen(""+(char)0xe1, ""+(char)0xc3 + (char)0xa1));//�
		umlaut.add(new zeichen(""+(char)0xe0, ""+(char)0xc3 + (char)0xa0));//�
		umlaut.add(new zeichen(""+(char)0xfb, ""+(char)0xc3 + (char)0xbb));//�
		umlaut.add(new zeichen(""+(char)0xfa, ""+(char)0xc3 + (char)0xba));//�
		umlaut.add(new zeichen(""+(char)0xf9, ""+(char)0xc3 + (char)0xb9));//�
	}
	
	public String unapostrophe(String str) {
		String loc=str;
		if(loc.startsWith(apost)) loc= loc.substring(3);
		if(loc.endsWith(apost)) loc=loc.replaceAll(apost, "");
		return loc;
	}
	
	public String clarify(String str) {
		try{String w = unapostrophe(str);
		for(zeichen z : umlaut) {
			if(w.contains(z.ansi)) System.out.println("Fund: "+z.ansi);
			w = w.replace(z.ansi, z.utf);
		}
		return w;
		}
		catch(Exception e) {System.out.println(e); return str;}
	}
}
