package app;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import javax.swing.JFrame;

import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.JPanel;

/** User dialog widget */
public class UserDialog {

    private JFrame mainFrame;
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
    
    private Color warnFG = new Color(255, 0,0);
    private Color normalFG = new Color(0,0,0);
    private Color openFileBG = new Color(255, 177, 91);
    private Color startButtonBG = new Color(232, 111, 55);
    private Color openHTMLBG = new Color(92,177,92);
    private Color moss = new Color(170,200,170);
    private Color clearBG = moss;
 
    public String selFile, selTargetFile, selTopicString = "", fileType = "";
    
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
	
    public void githubOpen(){	
        Desktop desktop = Desktop.getDesktop();

        try {
        	URL url = new URL("https://github.com/DGrothe-PhD/WordCheckerJava/");
			desktop.browse(url.toURI());
		}
        catch (URISyntaxException e) {
	    	setMessage("GitHub could not be opened in your browser.", 1);
		}
        catch (Exception oError){
	        setMessage("GitHub opening button raised an exception.", 1);
	    }
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
    	statusPanel = new Panel();
    	controlPanel = new Panel();
    	ImagePanel background;
    	Label headline = new Label("analyze text files and find words");
    	Label copyright = new Label("© 2021 Daniela Grothe");
    	Button github = new Button("GitHub");
    	
    	github.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                githubOpen();
            }
        });
    	
    	mainFrame = new JFrame("Java Wordchecker App");
    	mainFrame.setSize(440,460);
    	mainFrame.setLayout(new GridLayout());
    	
    	Panel top = new Panel();
    	GridBagLayout panelgrid = new GridBagLayout();
    	top.setLayout(panelgrid);
    	GridBagConstraints pgc = new GridBagConstraints();
    	pgc.fill = GridBagConstraints.HORIZONTAL;
        pgc.anchor = GridBagConstraints.NORTH;
        pgc.gridheight = 1;
    	pgc.gridx=0;
    	pgc.gridy=0;
    	top.add(controlPanel, pgc);
    	pgc.gridheight = 2;
    	pgc.gridx=0;
    	pgc.gridy=2;
    	top.add(statusPanel, pgc);
    	
    	Image image;
    	try {
    		image = ImageIO.read(getClass().getResource("./background.JPG"));
        	background = new ImagePanel(image);
        	background.setLayout(new FlowLayout());
        	background.add(headline);
        	background.add(top, "Center");
        	background.add(copyright);
        	background.add(github);
        	mainFrame.add(background);
    	}
    	catch(Exception e) {
    		mainFrame.setBackground(moss);
    		mainFrame.add(headline);
    		mainFrame.add(top, "Center");
    		mainFrame.add(copyright);
    		mainFrame.add(github);
    	}
        Color light = new Color(190, 220, 190);
        Color darker = new Color(130,170,130);
        Color hint = new Color(130,170,130);
        Color green = new Color(160,180,170);

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
        textareaLabel.setFont(WFont.labelfont);
        userTermsTextArea = new TextArea();
        userTermsTextArea.setFont(WFont.descriptionFont);
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
        
        controlPanel.setLayout(new GridLayout(3,2));
        controlPanel.setBackground(darker);
        controlPanel.setSize(424,280);
	    
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
	   
	   /** File dialog and button actions */
    public void showFileDialog(){
        final FileDialog fileDialog = new FileDialog(mainFrame,"Select file");
        WButton fileDialogButton = new WButton("Open File", openFileBG);

        WButton startButton = new WButton("Start", startButtonBG);
        startButton.setPreferredSize(new Dimension(116, 30));
        
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


@SuppressWarnings("serial")
class ImagePanel extends JPanel {
    private Image image;
    private boolean tile;

    ImagePanel(Image image) {
        this.image = image;
        this.tile = false;
        //final JLabel foo = new JLabel("");
        this.setSize(440,360);
        //final JCheckBox checkBox = new JCheckBox();
        /*checkBox.setAction(new AbstractAction("Tile") {
            public void actionPerformed(ActionEvent e) {
                tile = checkBox.isSelected();
                repaint();
            }
        });*/
        //add(foo);
        //add(checkBox);
        //add(bu4);//, BorderLayout.SOUTH);
    };

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (tile) {
            int iw = image.getWidth(this);
            int ih = image.getHeight(this);
            if (iw > 0 && ih > 0) {
                for (int x = 0; x < getWidth(); x += iw) {
                    for (int y = 0; y < getHeight(); y += ih) {
                        g.drawImage(image, x, y, iw, ih, this);
                    }
                }
            }
        } else {
        	g.drawImage(image, 0, 0, this);
            //g.drawImage(image, 0, 0, getWidth(), getHeight(), this);
        }
    }
}