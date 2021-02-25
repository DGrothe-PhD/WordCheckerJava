package app;
import java.util.*; 

public class Word implements Comparator<Word>, Comparable<Word>{
	private int count = 1;
	private String name = "";
	
	public Word(String the_name) {
		name = the_name;
		count = 1;
	}
	public void Increment() {
		count+=1;
	}
	public int getCount() {
		return count;
	}
	public String getWord() {
		return name;
	}
	public int compareTo(Word w) {
	      return (this.name).compareTo(w.name);
	}
	public int compare(Word w, Word w1) {
		return w.count - w1.count;
	}
    
	@Override
	public boolean equals(Object o){
        if(o instanceof Word){
             Word p = (Word) o;
             return this.name.equals(p.getWord());
        } else
             return false;
    }
}