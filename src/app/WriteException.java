package app;

/** Writing exceptions are handled separately */
public class WriteException extends Exception {
	private static final long serialVersionUID = -6474905865752155884L;
	public WriteException(){
		super();
	}
	public WriteException(String s) {
		super(s);
	}
}
