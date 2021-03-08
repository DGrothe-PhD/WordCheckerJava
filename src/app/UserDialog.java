package app;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.net.URI;

/** User dialog widget */
public class UserDialog {
	//
	   private Frame mainFrame;
	   private Label headerLabel, statusLabelHeader;
	   private TextField statusField, supplField;
	   private Panel controlPanel;
	   public String selFile, selTargetFile, selTopicString, fileType;
	   private static final String[] ALLOWED_INPUT_FILES = {
			   ".txt", ".md", ".tex", ".py", ".rb", ".yml", "html",
			   ".java", ".cpp", ".hpp", ".csv", ".cs"};
	   private static final char[] ILLEGAL_CHARACTERS = {
			   '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*',
			   '\\', '<', '>', '|', '\"', ':' };
	   private TimeCalc tc;
	   
	   public UserDialog(){
	      prepareGUI();
	      showFileDialog();
	   }
	   
	   public String getSelectedFile() {
		   return selFile;
	   }
	   
	   /** Open the results HTML file */
	   public void htmlOpen()
		{	
			Desktop desktop = Desktop.getDesktop();

			try
			{
				URI uri = new File(selTargetFile).toURI();
				desktop.browse(uri);
			} catch (Exception oError)
			{
				System.out.println("Output file could not be opened.");
			}
		}
	   
	   /** Widget settings */
	   private void prepareGUI(){
	      mainFrame = new Frame("Java Wordchecker App");
	      mainFrame.setSize(425,307);
	      Color moss = new Color(170,200,170);
	      Color darker = new Color(130,170,130);
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
	      
	      statusLabelHeader = new Label(); 
	      statusLabelHeader.setBackground(green);
	      statusLabelHeader.setText("Output file...");
	      statusLabelHeader.setAlignment(Label.LEFT);
	      statusField = new TextField(40);
	      statusField.setText("");
	      supplField = new TextField(40);
	      supplField.setBackground(green);
	      supplField.setText("");
	      
	      controlPanel = new Panel();
	      controlPanel.setLayout(new GridLayout(3,2));
	      controlPanel.setBackground(darker);
	      controlPanel.setSize(424,280);
	      
	      Component[] abc = {headerLabel, controlPanel};
	      for (Component c:abc) {
	    	  mainFrame.add(c);
	      }

	      mainFrame.setVisible(true);  
	   }
	   
	   /** show status messages */
	   public void setMessage(String s) {
		   statusField.setText(s);
	   }
	   /** show extra message @param up to 3 strings */
	   public void setSupplMessage(String s, String... t) {
		   String t1 = t.length > 0 ? t[0] : "";
		   String t2 = t.length > 1 ? t[1] : "";
		   supplField.setText(s + " " + t1 + " "+ t2);
	   }
	   /** File dialog and button actions */
	   public void showFileDialog(){
	      final FileDialog fileDialog = new FileDialog(mainFrame,"Select file");
	      Button showFileDialogButton = new Button("Open File");
	      Button startButton = new Button("Start");
	      Button openButton = new Button("Show Results");
	      Button closeButton = new Button("Close window");
	      
	      TextField targetFile = new TextField(40);
	      targetFile.setText("Results.html");
	      Label targetFileLabel = new Label("Target file:");
	      
	      TextField topicString = new TextField(40);
	      topicString.setText("Result word list");
	      Label topicStringLabel = new Label("Type topic:");
	      
	      /** The Open File or Browse... button to select input text file.*/
	      showFileDialogButton.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            fileDialog.setVisible(true);
	            String loc = ""+fileDialog.getDirectory();
	            int q = loc.length()>40?loc.length()-40:0;
	            loc = loc.substring(q);
	            statusLabelHeader.setText("File Selected :");
	            selFile = fileDialog.getDirectory() + fileDialog.getFile();
	            statusField.setText("..." + selFile);
	            supplField.setText("");
	            
	            fileType="";
	            for(String str:ALLOWED_INPUT_FILES) {
	            	if(selFile.endsWith(str)) {
	            		fileType=str;
	            		break;
	            	}
	            }
	            selTopicString = topicString.getText();
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
		    	// Validate source text file selection: must be text file
		        if(fileType.length()<2) {
		        	setMessage(
		        			"Please choose a text file, of types:"+
		        			String.join(", ", ALLOWED_INPUT_FILES)
		        			);
		        	throw new ArithmeticException("File type not allowed");//placeholder
		        }
		        // handling unset results file at start
		        if (selFile == null || selFile.length()<2) {
		        	setMessage("Please select a file before clicking Start.");
		        	throw new ArithmeticException("No file selected");//placeholder
		        }
		        
		        // Validate results filename from input text field
		        String s = targetFile.getText();
		        String illchar = "";
		        for(char c:ILLEGAL_CHARACTERS) {
		        	if(s.contains(""+c)) {
		        		//s = s.replace(c, '0');
		        		illchar += (""+c);
		        	}
		        }
		        
		        if(illchar.length()>0) {
		        	setMessage("Symbols "+illchar+" are not allowed in filenames");
		        	throw new ArithmeticException("File type not allowed");//placeholder
		        }
		        if(s.length() > 5 ) {
		        	if(s.endsWith(".html")) {
		        		selTargetFile = s;
		        	}
		        	else {
		        		selTargetFile = s + ".html";
		        	}
		        }
		        else {
		        	selTargetFile = "Results_" + s + ".html";
		        }
		        
		        Writeinfile WordPlace = new Writeinfile(selTargetFile, topicString.getText());
		        EvaluateText etx = new EvaluateText(selFile);
		        WordPlace.storeAllItems(etx.GetWordsList());
		        WordPlace.finishWriting();
		        setSupplMessage(Integer.toString(etx.GetNumberOfWords())+" words counted.",
		        		tc.getDuration());
		        tc.printDuration();
		     }// end try
		     catch(ArithmeticException bannedfiletype) {;}
		     catch(Exception exc) {System.out.println("Some error");}
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
	      for (Component s:abc) {
	    	  controlPanel.add(s);
	      }
	      //labels
	      mainFrame.add(topicStringLabel);
	      mainFrame.add(topicString);
	      mainFrame.add(targetFileLabel);
	      mainFrame.add(targetFile);
	      mainFrame.add(statusLabelHeader);
	      mainFrame.add(statusField);
	      mainFrame.add(supplField);
	      mainFrame.setVisible(true);  
	   }
}
