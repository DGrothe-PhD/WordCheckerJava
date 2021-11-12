package app;

import java.awt.Color;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.*;
import java.awt.Checkbox;
import java.awt.Font;
import java.awt.Button;

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
	protected static Font descriptionFont = new Font("Helvetica", Font.PLAIN, 12);
	protected static Font labelfont = new Font("Helvetica", Font.BOLD, 12);
	   
	public LabelledField(String title, String preset) {
		thelabel = new Label(title);
		thelabel.setFont(labelfont);
		thepreset = preset;
		thetextfield = new TextField(FIELD_WIDTH);
		thetextfield.setFont(descriptionFont);
		thetextfield.setText(preset);
	}
	
	public LabelledField(String title, String preset, Color bgcolor) {
		this(title, preset);
		thetextfield.setBackground(bgcolor);
	}
	public LabelledField(String title, String preset, Color bgcolor, boolean editable) {
		this(title, preset);
		if(!editable) thelabel.setFont(descriptionFont);
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

class WButton extends Button {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5037983284871342515L;

	public WButton(String title, Color color) {
		super(title);
		this.setBackground(color);
		this.setFont(LabelledField.labelfont);
	}
}