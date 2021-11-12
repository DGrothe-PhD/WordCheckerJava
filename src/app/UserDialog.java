package app;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.ImageIcon;

/** User dialog widget */
public class UserDialog {

    private Frame mainFrame;
    //private Label headerLabel;
    private String workingFolder;
	   
    LabelledField field_topic, field_targetFile, field_status, field_fileToAnalyze, field_supplinfo;
    Label textareaLabel;
    private TextArea userTermsTextArea;

    private ToggleFunction chkNumbers, chkSymbols, chkWords, chkUserTerms;
    private Panel controlPanel, statusPanel;
    
    private int mode;
    public void switchMode(int mode) {
    	this.mode += mode;
    }
    
    private Font labelfont = new Font("Helvetica", Font.BOLD, 12);
    private Color warnFG = new Color(255, 0,0);
    private Color normalFG = new Color(0,0,0);
    private Color openFileBG = new Color(255, 177, 91);
    private Color startButtonBG = new Color(232, 111, 55);
    private Color openHTMLBG = new Color(92,177,92);
    private Color moss = new Color(170,200,170);
    private Color clearBG = moss;
 
    public String selFile, selTargetFile, selTopicString = "", fileType = "";
    public /*static*/ String[] userSearchTerms;
    
    private static final String[] ALLOWED_INPUT_FILES = {
			   ".txt", ".md", ".tex", ".py", ".rb", ".yml", "html",
			   ".java", ".cpp", ".hpp", ".csv", ".cs"};
    private static final char[] ILLEGAL_CHARACTERS = {
			   '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*',
			   '\\', '<', '>', '|', '\"', ':' };
    private TimeCalc tc;
	
    public UserDialog(){
        try {
        	Toolkit tk = Toolkit.getDefaultToolkit();
            Dimension d = tk.getScreenSize();
            System.out.println("Screen width = " + d.width);
            System.out.println("Screen height = " + d.height);

            prepareGUI();
            showFileDialog();
        }
        catch(AWTError awe) {
            System.out.println("Application window could not be opened.");
        }
    }
	   
    public String getSelectedFile() {
        return selFile;
    }
	   
    /** Open the results HTML file */
    public void htmlOpen(){	
        Desktop desktop = Desktop.getDesktop();

        try {
            URI uri = new File(selTargetFile).toURI();
				desktop.browse(uri);
		} catch (Exception oError){
	        setMessage("Output file could not be opened.", 1);
	    }
	}
	   
    /** Lets user select which tokens to collect */
    private void makeCheckboxGroup() {
        mode = 15;
		   
        chkNumbers = new ToggleFunction("Numbers", this, CountWords.switchMode.c_Numbers.getMode());
        chkSymbols = new ToggleFunction("Symbols", this, CountWords.switchMode.c_Symbols.getMode());
        chkWords = new ToggleFunction("Words", this, CountWords.switchMode.c_Words.getMode());
        chkUserTerms = new ToggleFunction("Search terms", this, CountWords.switchMode.c_UserTerms.getMode());
        
        controlPanel.add(chkNumbers);
        controlPanel.add(chkSymbols);
    	controlPanel.add(chkWords);
    	controlPanel.add(chkUserTerms);
    }
	   
	   /** Widget settings */
    private void prepareGUI(){
        mainFrame = new Frame("Java Wordchecker App");
        mainFrame.setSize(440,360);
        //Color moss = new Color(170,200,170);
        Color light = new Color(190, 220, 190);
        Color darker = new Color(130,170,130);
        Color hint = new Color(130,170,130);
        Color green = new Color(160,180,170);
        
        mainFrame.setBackground(moss);

        mainFrame.setLayout(new FlowLayout());
        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }        
        });    
	      
        //labels and textfields
        field_topic = new LabelledField("Type topic:", "Result word list");
        field_targetFile = new LabelledField("Target file:", "Word_occurrences.html");
        field_status = new LabelledField("Selected folder:", "", hint, false);
        field_fileToAnalyze = new LabelledField("Selected file:", "- Please select a file -", light, false);
        field_supplinfo = new LabelledField("Info:", "", green, false);
	    
        textareaLabel = new Label("Search terms:");
        textareaLabel.setFont(labelfont);
        userTermsTextArea = new TextArea();
        userTermsTextArea.setColumns(LabelledField.FIELD_WIDTH);
        userTermsTextArea.setRows(5);
        userTermsTextArea.setEditable(true);
        
        userTermsTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            		mainFrame.requestFocus();
            	}
            }
        });
        
        controlPanel = new Panel();
        controlPanel.setLayout(new GridLayout(3,2));
        controlPanel.setBackground(darker);
        controlPanel.setSize(424,280);
	      
        statusPanel = new Panel();
        statusPanel.setBackground(moss);
        statusPanel.setSize(424,280);
	      
        GridBagLayout grid3 = new GridBagLayout();
        GridBagConstraints gbc = new GridBagConstraints();
        statusPanel.setLayout(grid3);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        // left column
        gbc.gridx = 0;
        gbc.gridy = 0;
        statusPanel.add(field_topic.thelabel, gbc);
        //gbc.gridx = 0;
        gbc.gridy = 1;
        statusPanel.add(field_targetFile.thelabel, gbc);
        gbc.gridy = 3;
        statusPanel.add(field_status.thelabel, gbc);
        gbc.gridy = 4;
        statusPanel.add(field_fileToAnalyze.thelabel, gbc);
        gbc.gridy = 5;
        statusPanel.add(field_supplinfo.thelabel, gbc);
        gbc.gridy = 6;
        statusPanel.add(textareaLabel, gbc);
	      
        // right column is broader
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;
        statusPanel.add(field_topic.thetextfield, gbc);
        gbc.gridy = 1;
        statusPanel.add(field_targetFile.thetextfield, gbc);
        gbc.gridy = 3;
        statusPanel.add(field_status.thetextfield, gbc);
        gbc.gridy = 4;
        statusPanel.add(field_fileToAnalyze.thetextfield, gbc);
        gbc.gridy = 5;
        statusPanel.add(field_supplinfo.thetextfield, gbc);
        gbc.gridy = 6;
        statusPanel.add(userTermsTextArea, gbc);
	      
        //mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusPanel);
        mainFrame.setVisible(true);  
    }
	   
    /** show status messages */
    public void setMessage(String settext, int warning) {
        field_status.setText(settext);
        if(warning == 1) {
            field_status.thetextfield.setForeground(warnFG);
        }
        else {
			   field_status.thetextfield.setForeground(normalFG);
		}
    }
	   
	   /** show extra message @param up to 3 strings */
    public void setSupplMessage(String s, String... t) {
        String t1 = t.length > 0 ? t[0] : "";
        String t2 = t.length > 1 ? t[1] : "";
	    field_supplinfo.setText(s + " " + t1 + " "+ t2);
        field_supplinfo.thetextfield.setForeground(normalFG);
    }
    public void setSupplWarning(String s, String... t) {
        String t1 = t.length > 0 ? t[0] : "";
        String t2 = t.length > 1 ? t[1] : "";
        field_supplinfo.setText(s + " " + t1 + " "+ t2);
        field_supplinfo.thetextfield.setForeground(warnFG);
    }
	   
    private String setWritingTarget() {
        // Validate results filename from input text field
        String s = field_targetFile.getText();
        String illchar = "";
        for(char c:ILLEGAL_CHARACTERS) {
            if(s.contains(""+c)) {illchar += (""+c);}
        } 
        if(illchar.length()>0) {
            setMessage("Symbols "+illchar+" are not allowed in filenames", 1);
            return null;
        }
        if(s.length() > 5 && s.endsWith(".html")) return "Results_" + s; 
        return workingFolder + "Results_" + s + ".html";
    }
	
    public String[] getSearchTerms() {
    	return userSearchTerms;
    }
	   
	   /** File dialog and button actions */
    public void showFileDialog(){
        final FileDialog fileDialog = new FileDialog(mainFrame,"Select file");
        WButton fileDialogButton = new WButton("Open File", openFileBG);

        WButton startButton = new WButton("Start", startButtonBG);
        startButton.setPreferredSize(new Dimension(116, 30));
        
        /*try{
        	JButton startButton = new JButton("Start");
            startButton.setPreferredSize(new Dimension(116, 30));
            startButton.setBackground(startButtonBG);
        	File imgFile=new File("start.png");
        	Image imgio = ImageIO.read(imgFile);
        	ImageIcon icon = new ImageIcon(imgio);
            startButton.setIcon(icon);
        }
        catch(Exception exc) {
        	System.out.println("Klappt nicht");
        }*/
        
        WButton openButton = new WButton("Show Results", openHTMLBG);
        WButton closeButton = new WButton("Close window", moss);
        WButton clearButton = new WButton("Clear search", clearBG);
	    
        /** The Open File or Browse... button to select input text file.*/
        fileDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMessage("", 0);
                fileDialog.setVisible(true);
                workingFolder = ""+fileDialog.getDirectory();
	            
                selFile = workingFolder + fileDialog.getFile();
                field_status.setText("..." + workingFolder);
                field_fileToAnalyze.setText(fileDialog.getFile());
                field_supplinfo.setText("");
	            
                fileType="";
                for(String str:ALLOWED_INPUT_FILES) {
                    if(selFile.endsWith(str)) {
                        fileType=str;
                        break;
                    }
                }
                selTopicString = field_topic.getText();
                String str = field_fileToAnalyze.getText();
                if(str.lastIndexOf('.')>0) str = str.substring(0,str.lastIndexOf('.'));
                field_targetFile.setText(str);
                field_topic.setText(str);
                }
        });
	      
        fileDialogButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            if (event.getKeyChar() == KeyEvent.VK_TAB) {
            	fileDialogButton.requestFocus();
            }
            else if (event.getKeyChar() == KeyEvent.VK_ENTER) {
            	//double code portion. perhaps to be revised.
            	setMessage("", 0);
                fileDialog.setVisible(true);
                workingFolder = ""+fileDialog.getDirectory();
	            
                selFile = workingFolder + fileDialog.getFile();
                field_status.setText("..." + workingFolder);
                field_fileToAnalyze.setText(fileDialog.getFile());
                field_supplinfo.setText("");
	            
                fileType="";
                for(String str:ALLOWED_INPUT_FILES) {
                    if(selFile.endsWith(str)) {
                        fileType=str;
                        break;
                    }
                }
                selTopicString = field_topic.getText();
                String str = field_fileToAnalyze.getText();
                if(str.lastIndexOf('.')>0) str = str.substring(0,str.lastIndexOf('.'));
                field_targetFile.setText(str);
                field_topic.setText(str);
            }
           }
        });
        
        /** The start button. When pressed, chosen file is processed, 
        * and results are written into the target file. */
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                tc = new TimeCalc();
                try{
                    if (selFile == null || selFile.length()<2) {
                        setMessage("Please select a file before clicking Start.", 1);
                    }
                    
                    if(fileType.length()<2) {
                        setMessage(
                            "Please choose a text file, of types:"+
                                String.join(", ", ALLOWED_INPUT_FILES), 1);
                        throw new IllegalArgumentException("File type not allowed");
                    }
                    selTargetFile = setWritingTarget();
                    
                    String str = userTermsTextArea.getText();
                    
                    EvaluateText etx = new EvaluateText(str, mode);
                    
                    Writeinfile WordPlace = new Writeinfile(selTargetFile, field_topic.getText(), mode);
                    
                    try{etx.eTextToolBox(selFile);}
                    catch(Exception exc) {System.out.println("EtextToolbox went wrong.");}
                    
                    try{WordPlace.storeAllItems(etx.GetWordsList());}
                    catch(Exception exc) {System.out.println("WordPlace Storage went wrong.");}
                    
                    WordPlace.finishWriting();

                    fileType = "";
                    setSupplMessage(Integer.toString(etx.GetNumberOfWords())+" words counted.",
                        tc.getDuration());
                }
                catch(WriteFileException wfe) {
                    setSupplWarning(wfe.getMessage());
                    System.out.println(wfe.getCause());
                }
                catch(Exception exc) {System.out.println("Some UI or other exception."+exc.getCause());}
                }
        });
	      
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                }
            });
	    
        closeButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            	if (event.getKeyChar() == KeyEvent.VK_ENTER) {
            		mainFrame.dispose();
            }
           }
        });
        
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
            	userTermsTextArea.setText("");
            }
        });
        
        clearButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyChar() == KeyEvent.VK_ENTER) {
            		userTermsTextArea.setText("");
            	}
            }
        });
        
        /** Opens the result HTML file (in the standard browser).*/
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                htmlOpen();
            }
        });
        
        openButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            	if (event.getKeyChar() == KeyEvent.VK_ENTER) {
            		htmlOpen();
            	}
            }
        });
	      
        Component[] abc = {fileDialogButton, startButton, openButton, closeButton, clearButton};
        for (Component s:abc) controlPanel.add(s);
	      
        makeCheckboxGroup();
        
        mainFrame.setFocusable(true);
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            	if (event.getKeyChar() == KeyEvent.VK_ESCAPE) {
            		mainFrame.dispose();
            	}
            }
        });

        mainFrame.setVisible(true);
        mainFrame.requestFocus();
    }
}
