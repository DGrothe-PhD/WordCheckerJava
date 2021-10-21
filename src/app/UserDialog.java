package app;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;

/** User dialog widget */
public class UserDialog {
	//fields
    private Frame mainFrame;
    private Label headerLabel;
    private String workingFolder;
	   
    LabelledField topic, targetFile, status, fileToAnalyze, supplinfo;
    Checkbox chkNumbers, chkSymbols, chkWords;
	   
    public boolean collectNumbers = true, collectSymbols = true, collectWords = true;
    private Color warnFG = new Color(255, 0,0);
    private Color normalFG = new Color(0,0,0);
	   
    private Panel controlPanel, statusPanel;
    public String selFile, selTargetFile, selTopicString = "", fileType = "";
    private static final String[] ALLOWED_INPUT_FILES = {
			   ".txt", ".md", ".tex", ".py", ".rb", ".yml", "html",
			   ".java", ".cpp", ".hpp", ".csv", ".cs"};
    private static final char[] ILLEGAL_CHARACTERS = {
			   '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*',
			   '\\', '<', '>', '|', '\"', ':' };
    private TimeCalc tc;
	
	//constructor
    public UserDialog(){
        try {
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
	        System.out.println("Output file could not be opened.");
	    }
	}
	   
    /** Lets user select which tokens to collect */
    private void makeCheckboxGroup() {
        chkNumbers = new Checkbox("Numbers", true);
        chkSymbols = new Checkbox("Symbols", true);
        chkWords = new Checkbox("Words", true);
		   
        chkNumbers.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
                collectNumbers = e.getStateChange()==1?true:false;
                }
            });
        chkSymbols.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
                collectSymbols = e.getStateChange()==1?true:false;
                }
            });
        chkWords.addItemListener(new ItemListener() {
            public void itemStateChanged(ItemEvent e) {             
                collectWords = e.getStateChange()==1?true:false;
                }
            });
		   
        controlPanel.add(chkNumbers);
        controlPanel.add(chkSymbols);
    	controlPanel.add(chkWords);
    }
	   
	   /** Widget settings */
    private void prepareGUI(){
        mainFrame = new Frame("Java Wordchecker App");
        mainFrame.setSize(425,307);
        Color moss = new Color(170,200,170);
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
	      
        headerLabel = new Label();
        headerLabel.setAlignment(Label.CENTER);
	      
        //labels and textfields
        topic = new LabelledField("Type topic:", "Result word list");
        targetFile = new LabelledField("Target file:", "Word_occurrences.html");
        status = new LabelledField("Selected folder:", "", hint);
        fileToAnalyze = new LabelledField("Selected file:", "", light);
        supplinfo = new LabelledField("Info:", "", green);
	      
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
        // left column
        gbc.gridx = 0;
        gbc.gridy = 0;
        statusPanel.add(topic.thelabel, gbc);
        //gbc.gridx = 0;
        gbc.gridy = 1;
        statusPanel.add(targetFile.thelabel, gbc);
        gbc.gridy = 3;
        statusPanel.add(status.thelabel, gbc);
        gbc.gridy = 4;
        statusPanel.add(fileToAnalyze.thelabel, gbc);
        gbc.gridy = 5;
        statusPanel.add(supplinfo.thelabel, gbc);
	      
        // right column is broader
        gbc.gridwidth = 2;
        gbc.gridx = 1;
        gbc.gridy = 0;
        statusPanel.add(topic.thetextfield, gbc);
        gbc.gridy = 1;
        statusPanel.add(targetFile.thetextfield, gbc);
        gbc.gridy = 3;
        statusPanel.add(status.thetextfield, gbc);
        gbc.gridy = 4;
        statusPanel.add(fileToAnalyze.thetextfield, gbc);
        gbc.gridy = 5;
        statusPanel.add(supplinfo.thetextfield, gbc);
	      
        mainFrame.add(headerLabel);
        mainFrame.add(controlPanel);
        mainFrame.add(statusPanel);
        mainFrame.setVisible(true);  
    }
	   
    /** show status messages */
    public void setMessage(String settext, int warning) {
        status.setText(settext);
        if(warning == 1) {
            status.thetextfield.setForeground(warnFG);
        }
        else {
			   status.thetextfield.setForeground(normalFG);
		}
    }
	   
	   /** show extra message @param up to 3 strings */
    public void setSupplMessage(String s, String... t) {
        String t1 = t.length > 0 ? t[0] : "";
        String t2 = t.length > 1 ? t[1] : "";
	    supplinfo.setText(s + " " + t1 + " "+ t2);
        supplinfo.thetextfield.setForeground(normalFG);
    }
    public void setSupplWarning(String s, String... t) {
        String t1 = t.length > 0 ? t[0] : "";
        String t2 = t.length > 1 ? t[1] : "";
        supplinfo.setText(s + " " + t1 + " "+ t2);
        supplinfo.thetextfield.setForeground(warnFG);
    }
	   
    private String setWritingTarget() {
        // Validate results filename from input text field
        String s = targetFile.getText();
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
	   
	   
	   /** File dialog and button actions */
    public void showFileDialog(){
        final FileDialog fileDialog = new FileDialog(mainFrame,"Select file");
        Button showFileDialogButton = new Button("Open File");
        Button startButton = new Button("Start");
        Button openButton = new Button("Show Results");
        Button closeButton = new Button("Close window");
	      	      
        /** The Open File or Browse... button to select input text file.*/
        showFileDialogButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                setMessage("", 0);
                fileDialog.setVisible(true);
                workingFolder = ""+fileDialog.getDirectory();
	            
                selFile = workingFolder + fileDialog.getFile();
                status.setText("..." + workingFolder);
                fileToAnalyze.setText(fileDialog.getFile());
                supplinfo.setText("");
	            
                fileType="";
                for(String str:ALLOWED_INPUT_FILES) {
                    if(selFile.endsWith(str)) {
                        fileType=str;
                        break;
                        }
                    }
                selTopicString = topic.getText();
                String str = fileToAnalyze.getText();
                if(str.lastIndexOf('.')>0) str = str.substring(0,str.lastIndexOf('.'));
                targetFile.setText(str);
                topic.setText(str);
                }
        });
	      
        /** The start button. When pressed, chosen file is processed, 
        * and results are written into the target file. */
        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e){
                //User selected target filename for results.
                tc = new TimeCalc();
                try{
                    // handling unset results file at start
                    if (selFile == null || selFile.length()<2) {
                        setMessage("Please select a file before clicking Start.", 1);
                        throw new IllegalArgumentException("No file selected");
                        }
                    
                    // Validate source text file selection: must be text file
                    if(fileType.length()<2) {
                        setMessage(
                                "Please choose a text file, of types:"+
                                        String.join(", ", ALLOWED_INPUT_FILES), 1);
                        throw new IllegalArgumentException("File type not allowed");
                        }
                    selTargetFile = setWritingTarget();
		        
                    Writeinfile WordPlace = new Writeinfile(selTargetFile, topic.getText());
		        
                    /** Check boxes status */
                    EvaluateText etx = new EvaluateText(selFile, collectWords, collectNumbers, collectSymbols);
                    WordPlace.storeAllItems(etx.GetWordsList());
                    WordPlace.finishWriting();
                    //forget input file
                    fileType = "";
                    setSupplMessage(Integer.toString(etx.GetNumberOfWords())+" words counted.",
                            tc.getDuration());
                    tc.printDuration();
                    }// end try
                catch(IllegalArgumentException bannedfiletype) {System.out.println(bannedfiletype);}
                catch(WriteFileException wfe) {
                    setSupplWarning(wfe.getMessage());
                    System.out.println(wfe.getCause());
                    }
                catch(Exception exc) {System.out.println("Some UI or other exception.");}
                }
        });
	      
	      /** When close button is pressed, the window is closed.*/
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.dispose();
                }
            });
	      
        /** Opens the result HTML file (in the standard browser).*/
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                htmlOpen();
                }
            });
        //buttons
	      
        Component[] abc = {showFileDialogButton, startButton, openButton, closeButton};
        for (Component s:abc) controlPanel.add(s);
	      
        makeCheckboxGroup();

        mainFrame.setVisible(true);  
    }
}
