package app;

import java.awt.Color;
import java.awt.Label;
import java.awt.TextField;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
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
	
	/** returns the field content*/
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
	
	Checkbox thecheckbox;
	/*public ToggleFunction(String name, int mode, int level) {
		final int curval = mode;
		thecheckbox = new Checkbox(name, true);
		thecheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
                curval += (e.getStateChange()==1?1:(-1))*level;
                }
        });
		
	}*/
	
	public ToggleFunction(String name, UserDialog udi, int level) {
		thecheckbox = new Checkbox(name, true);
		thecheckbox.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
                udi.switchMode((e.getStateChange()==1?1:(-1))*level);
            }
        });
		// so now add an enter-key listener doing same.
	}
}