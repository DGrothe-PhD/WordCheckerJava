package app;

/** Writing errors are handled separately */
public class WriteFileException extends Exception {
	private static final long serialVersionUID = -6474905865752155884L;
	public WriteFileException(){
		super();
	}
	public WriteFileException(String s) {
		super(s);
	}
}
