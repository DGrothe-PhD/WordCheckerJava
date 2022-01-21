package app;

import java.awt.Color;
import javax.swing.JLabel;
import javax.swing.text.JTextComponent;
import javax.swing.JTextField;
import javax.swing.JTextArea;
import java.awt.event.*;

import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import java.awt.Checkbox;
import java.awt.Font;

/** inner class for labelled fields */
class LabelledField extends LabelledComponent {
	JTextField jcomp;
	 
	public LabelledField(String title, String preset) {
		super(title, preset);
		setup();
	}
	
	public LabelledField(String title, String preset, Color bgcolor) {
		super(title, preset, bgcolor);
		setup();
	}
	public LabelledField(String title, String preset, Color bgcolor, boolean editable) {
		super(title, preset, bgcolor, editable);
		setup();
	}
	
	private void setup() {
		jcomp = new JTextField();
		format(jcomp);
	}
	
	public void setText(String displayedtext) {
		jcomp.setText(displayedtext);
	}
	public String getText() {
		return jcomp.getText();
	}
	
	protected void setForeground(Color color) {
		jcomp.setForeground(color);
	}
}

class LabelledArea extends LabelledComponent {
	JTextArea jcomp;
	   
	/*public LabelledArea(String title, String preset) {
		super(title, preset);
		setup();
	}
	public LabelledArea(String title, String preset, Color bgcolor) {
		super(title, preset, bgcolor);
		setup();
	}*/
	
	public LabelledArea(String title, String preset, Color bgcolor, boolean editable) {
		super(title, preset, bgcolor, editable);
		setup();
	}
	
	private void setup() {
		jcomp = new JTextArea();
		format(jcomp);
		jcomp.setRows(3);
		jcomp.setLineWrap(true);
	}
	
	public void setText(String displayedtext) {
		jcomp.setText(displayedtext);
	}
	public String getText() {
		return jcomp.getText();
	}
	protected void setForeground(Color color) {
		jcomp.setForeground(color);
	}
}

abstract class LabelledComponent {
	
	protected static int FIELD_WIDTH = 30;
	String thepreset = "", thetitle="";
	JLabel thelabel;
	Color bgcolor;
	Boolean editable = true;
	   
	public LabelledComponent(String title, String preset) {
		thetitle = title;
		thelabel = new JLabel(title);
		thelabel.setFont(WFont.labelfont);
		thepreset = preset;
	}
	
	public LabelledComponent(String title, String preset, Color bgcolor) {
		this(title, preset);
		this.bgcolor=bgcolor;
	}
	public LabelledComponent(String title, String preset, Color bgcolor, boolean editable) {
		this(title, preset, bgcolor);
		this.editable = editable;
	}

	protected void format(JTextComponent comp) {
		comp.setFont(WFont.descriptionFont);
		comp.setText(this.thepreset);
		if(bgcolor!=null) comp.setBackground(bgcolor);
		if(!editable) thelabel.setFont(WFont.descriptionFont);
		comp.setEditable(editable);
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