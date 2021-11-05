package app;

import java.awt.Color;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.*;
import java.awt.Checkbox;

/** inner class for labelled fields */
class LabelledField extends TextField {
	/**
	 * 
	 */
	private static final long serialVersionUID = 424211L;
	protected static int FIELD_WIDTH = 40;
	String thepreset = "";
	Label thelabel;
	TextField thetextfield;
	   
	public LabelledField(String title, String preset) {
		thelabel = new Label(title);
		thepreset = preset;
		thetextfield = new TextField(FIELD_WIDTH);
		thetextfield.setText(preset);
	}
	public LabelledField(String title, String preset, Color bgcolor) {
		thelabel = new Label(title);
		thepreset = preset;
		thetextfield = new TextField(FIELD_WIDTH);
		thetextfield.setText(preset);
		thetextfield.setBackground(bgcolor);
	}
	public LabelledField(String title, String preset, Color bgcolor, boolean editable) {
		thelabel = new Label(title);
		thepreset = preset;
		thetextfield = new TextField(FIELD_WIDTH);
		thetextfield.setText(preset);
		thetextfield.setBackground(bgcolor);
		thetextfield.setEditable(editable);
	}
	
	public String getText() {
		return thetextfield.getText();
	}
	public void setText(String displayedtext) {
		thetextfield.setText(displayedtext);
	}
}

class ToggleFunction extends Checkbox {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8097995714747461357L;
	
	String name;
	
	public void changeState() {
		if(this.getState()) {
			this.setState(false);
		}
		else {
			this.setState(true);
		}
	}
	
	public ToggleFunction(String name, UserDialog udi, int level) {
		super(name, true);
		this.name = name;
		this.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
                udi.switchMode((e.getStateChange()==1?1:(-1))*level);
            }
        });
		this.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {      
            	if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            		changeState();
            		udi.switchMode((getState()?1:(-1))*level);
            	}
            }
        });
	}
}