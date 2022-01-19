package app;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import java.awt.Checkbox;
import java.awt.Font;

/** inner class for labelled fields */
class LabelledField /*extends TextField*/ {
	
	protected static int FIELD_WIDTH = 30;
	String thepreset = "";
	JLabel thelabel;
	JTextField thetextfield;
	   
	public LabelledField(String title, String preset) {
		thelabel = new JLabel(title);
		thelabel.setFont(WFont.labelfont);
		thepreset = preset;
		thetextfield = new JTextField(FIELD_WIDTH);
		thetextfield.setFont(WFont.descriptionFont);
		thetextfield.setText(preset);
	}
	
	public LabelledField(String title, String preset, Color bgcolor) {
		this(title, preset);
		thetextfield.setBackground(bgcolor);
	}
	public LabelledField(String title, String preset, Color bgcolor, boolean editable) {
		this(title, preset);
		if(!editable) thelabel.setFont(WFont.descriptionFont);
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
		this.setFont(WFont.labelfont);
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

class WButton extends JButton {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5037983284871342515L;
	private Border raised = new SoftBevelBorder(SoftBevelBorder.RAISED);
	// github.setBorder(raised);
	
	public WButton(String title, Color color) {
		super(title);
		this.setBorder(raised);
		this.setBackground(color);
		this.setFont(WFont.labelfont);
	}
}

class WFont {
	public static Font descriptionFont = new Font("Helvetica", Font.PLAIN, 12);
	public static Font labelfont = new Font("Helvetica", Font.BOLD, 12);
	
}