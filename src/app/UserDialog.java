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
import javax.swing.JComboBox;

/** User dialog widget */
public class UserDialog {

    private JFrame mainFrame;
    private String workingFolder;
    
    private ReadJson jsonSearchWords;
	   
    private LabelledField field_topic, field_targetFile, field_status, field_fileToAnalyze, field_supplinfo;
    private Label textareaLabel, searchTermBoxLabel;
    private Label headline = new Label("analyze text files and find words");
    private Label copyright = new Label("© 2021 Daniela Grothe");
    private Button github = new Button("GitHub");
    private FileDialog fileDialog;
    private WButton fileDialogButton, startButton, openButton, closeButton, clearButton;
    
    private TextArea userTermsTextArea;
    
    public JCBox searchTermBox;
    private ToggleFunction chkNumbers, chkSymbols, chkWords, chkUserTerms;
    private Panel controlPanel, statusPanel, top;
    private ImagePanel background;
    private Image image;
    
    private GridBagConstraints fieldGridConfig, pgc;
    private GridBagLayout panelgrid, grid3;
    
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
    private Color light = new Color(190, 220, 190);
    private Color darker = new Color(130,170,130);
    private Color hint = new Color(130,170,130);
    private Color green = new Color(160,180,170);
 
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
            
        	mainFrame = new JFrame("Java Wordchecker App");
        	mainFrame.setSize(440,460);
        	mainFrame.setLayout(new GridLayout());        	
        	
        	makeFields();
        	makeButtons();

            prepareGUI();
            searchTermBox = new JCBox();
            
            addButtons();
            addCheckboxGroup();
            
            positionFields();
    	    
            addListenersToUI();
            
            addListenersToButtons();
            
            showFileDialog();
            searchTermBox.connect(userTermsTextArea);
            
            mainFrame.setVisible(true);
            mainFrame.requestFocus();
            
            }
        catch(AWTError awe) {
        	//intended as fallback if UI cannot be shown on system
            System.out.println("Application window could not be opened.");
        }
    }
	   
    public String getSelectedFile() {
        return selFile;
    }
	
    public void getSearchWordsFromJson(String key) {
    	String rjs = jsonSearchWords.get(key);
        userTermsTextArea.setText(rjs);
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
        finally{
        	mainFrame.revalidate();
        }
	}
	   
    /** Lets user select which tokens to collect */
    private void addCheckboxGroup() {
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
    
	
    private void prepareGUI(){
    	grid3 = new GridBagLayout();
    	statusPanel = new Panel();
    	statusPanel.setBackground(moss);
        statusPanel.setSize(424,280); 
        statusPanel.setLayout(grid3);
    	
    	controlPanel = new Panel();
    	controlPanel.setLayout(new GridLayout(3,2));
        controlPanel.setBackground(darker);
        controlPanel.setSize(424,280);
        
        top = new Panel();
        panelgrid = new GridBagLayout();
    	top.setLayout(panelgrid);
        fieldGridConfig = new GridBagConstraints();
    	
    	pgc = new GridBagConstraints();
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
    }

    
    public void makeFields() {
    	searchTermBoxLabel = new Label("Search terms:");
    	searchTermBoxLabel.setFont(WFont.labelfont);
    	
    	textareaLabel = new Label("Search terms:");
        textareaLabel.setFont(WFont.labelfont);
        
        field_topic = new LabelledField("Type topic:", "Result word list");
        field_targetFile = new LabelledField("Target file:", "Word_occurrences.html");
        field_status = new LabelledField("Selected folder:", "", hint, false);
        field_fileToAnalyze = new LabelledField("Selected file:", "- Please select a file -", light, false);
        field_supplinfo = new LabelledField("Info:", "", green, false);
        
        userTermsTextArea = new TextArea();
        userTermsTextArea.setFont(WFont.descriptionFont);
        userTermsTextArea.setColumns(LabelledField.FIELD_WIDTH);
        userTermsTextArea.setRows(5);
        userTermsTextArea.setEditable(true);
    }
    
    public void makeButtons() {
    	fileDialog = new FileDialog(mainFrame,"Select file");
        fileDialogButton = new WButton("Open File", openFileBG);

        startButton = new WButton("Start", startButtonBG);
        startButton.setPreferredSize(new Dimension(116, 30));
        
        openButton = new WButton("Show Results", openHTMLBG);
        closeButton = new WButton("Close window", moss);
        clearButton = new WButton("Clear search", clearBG);
    }
    
    public void addButtons() {
    	Component[] abc = {fileDialogButton, startButton, openButton, closeButton, clearButton};
        for (Component s:abc) controlPanel.add(s);
    }
    
    //placement of UI elements
    public void positionFields() {  	
    	/// ~~~ gbc region ~~~
        fieldGridConfig.fill = GridBagConstraints.HORIZONTAL;
        fieldGridConfig.anchor = GridBagConstraints.NORTH;
        // left column
        fieldGridConfig.gridx = 0;
        fieldGridConfig.gridy = 0;
        statusPanel.add(field_topic.thelabel, fieldGridConfig);
        //gbc.gridx = 0;
        fieldGridConfig.gridy = 1;
        statusPanel.add(field_targetFile.thelabel, fieldGridConfig);
        fieldGridConfig.gridy = 3;
        statusPanel.add(field_status.thelabel, fieldGridConfig);
        fieldGridConfig.gridy = 4;
        statusPanel.add(field_fileToAnalyze.thelabel, fieldGridConfig);
        fieldGridConfig.gridy = 5;
        statusPanel.add(field_supplinfo.thelabel, fieldGridConfig);
        fieldGridConfig.gridy = 32;
        statusPanel.add(textareaLabel, fieldGridConfig);
	      
        // right column is broader
        fieldGridConfig.gridwidth = 2;
        fieldGridConfig.gridx = 1;
        fieldGridConfig.gridy = 0;
        statusPanel.add(field_topic.thetextfield, fieldGridConfig);
        fieldGridConfig.gridy = 1;
        statusPanel.add(field_targetFile.thetextfield, fieldGridConfig);
        fieldGridConfig.gridy = 3;
        statusPanel.add(field_status.thetextfield, fieldGridConfig);
        fieldGridConfig.gridy = 4;
        statusPanel.add(field_fileToAnalyze.thetextfield, fieldGridConfig);
        fieldGridConfig.gridy = 5;
        statusPanel.add(field_supplinfo.thetextfield, fieldGridConfig);
        
        /// snippet searchtermbox start
        fieldGridConfig.gridx = 0;
    	fieldGridConfig.gridy = 6;
    	statusPanel.add(searchTermBoxLabel, fieldGridConfig);
    	fieldGridConfig.gridx = 1;
    	fieldGridConfig.gridy = 6;
    	statusPanel.add(searchTermBox, fieldGridConfig);
    	///snippet end
    	
        fieldGridConfig.gridy = 32;
        statusPanel.add(userTermsTextArea, fieldGridConfig);
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
	
    
    public void addListenersToUI() {
    	github.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                githubOpen();
            }
        });
    	
    	userTermsTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	if (e.getKeyChar() == KeyEvent.VK_ESCAPE) {
            		mainFrame.requestFocus();
            	}
            }
        });
    	
    	mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent){
                System.exit(0);
            }        
        });   
    	mainFrame.setFocusable(true);
        mainFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            	if (event.getKeyChar() == KeyEvent.VK_ESCAPE) {
            		mainFrame.dispose();
            	}
            }
        });
    }
    
    public void addListenersToButtons() {
    	/** Opens the result HTML file (in the standard browser).*/
        openButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                htmlOpen();
                mainFrame.revalidate();
            }
        });
        
        openButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            	if (event.getKeyChar() == KeyEvent.VK_ENTER) {
            		htmlOpen();
            		mainFrame.revalidate();
            	}
            }
        });
    }
    
	   /** File dialog and button actions */
    public void showFileDialog(){
    	
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
                mainFrame.revalidate();
            }
        });
	      
        fileDialogButton.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent event) {
            if (event.getKeyChar() == KeyEvent.VK_TAB) {
            	fileDialogButton.requestFocus();
            }
            else if (event.getKeyChar() == KeyEvent.VK_ENTER) {
            	//TODO double code portion. perhaps to be revised.
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
            //mainFrame.revalidate();
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
                finally {mainFrame.revalidate();}
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
        
    }
}


@SuppressWarnings("serial")
class ImagePanel extends JPanel {
    private Image image;
    private boolean tile;

    ImagePanel(Image image) {
        this.image = image;
        this.tile = false;
        this.setSize(440,360);
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
        }
    }
}

class JCBox extends JComboBox<String> {
	public ReadJson jsonSearchWords;
	String rjs="";

	public String[] choices;
	
	public String getSelectedList() {
		return rjs;
	}
	
	public JCBox() {
		this.setFont(WFont.labelfont);
		jsonSearchWords = new ReadJson();
		choices = jsonSearchWords.getAll();
		this.addItem("<choose a list>");
		this.setSelectedItem("<choose a list>");
		for(String s : choices) {
			this.addItem(s);
		}
	}
	
	public void connect(TextArea textarea) {
		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
			    if(e.getStateChange() == ItemEvent.SELECTED) {
			        System.out.println("Hello!"+choices[0]+" "+choices[1]+" "+choices[2]);
			        rjs = jsonSearchWords.get(getSelectedItem().toString());
			        textarea.setText(rjs);
			    }
			}
		});
	}
	
}