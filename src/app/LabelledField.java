package app;

import java.awt.Color;
import java.awt.Label;
import java.awt.TextField;

/** inner class for labelled fields */
class LabelledField extends TextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 424211L;
	int LABEL_WIDTH = 40;
	String thepreset = "";
	Label thelabel;
	TextField thetextfield;
	   
	public LabelledField(String title, String preset) {
		thelabel = new Label(title);
		thepreset = preset;
		thetextfield = new TextField(LABEL_WIDTH);
		thetextfield.setText(preset);
	}
	public LabelledField(String title, String preset, Color bgcolor) {
		thelabel = new Label(title);
		thepreset = preset;
		thetextfield = new TextField(LABEL_WIDTH);
		thetextfield.setText(preset);
		thetextfield.setBackground(bgcolor);
	}
	
	/** returns the field content*/
	public String getText() {
		return thetextfield.getText();
	}
	public void setText(String displayedtext) {
		thetextfield.setText(displayedtext);
	}
}