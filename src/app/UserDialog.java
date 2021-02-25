package app;

import java.awt.*;
import java.awt.event.*;

public class UserDialog {
	//
	   private Frame mainFrame;
	   private Label headerLabel;
	   private Label statusLabel;
	   private Panel controlPanel;
	   public String selFile, selTargetFile, selTopicString;
	   private static final char[] ILLEGAL_CHARACTERS = {
			   '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*',
			   '\\', '<', '>', '|', '\"', ':' };

	   public UserDialog(){
	      prepareGUI();
	      showFileDialogDemo();
	   }
	   
	   public String getSelectedFile() {
		   return selFile;
	   }

	   private void prepareGUI(){
	      mainFrame = new Frame("Java Wordchecker App");
	      mainFrame.setSize(400,400);
	      mainFrame.setLayout(new GridLayout(3, 1));
	      mainFrame.addWindowListener(new WindowAdapter() {
	         public void windowClosing(WindowEvent windowEvent){
	            System.exit(0);
	         }        
	      });    
	      headerLabel = new Label();
	      headerLabel.setAlignment(Label.CENTER);
	      statusLabel = new Label();        
	      statusLabel.setAlignment(Label.CENTER);
	      statusLabel.setSize(350,100);
	      
	      controlPanel = new Panel();
	      controlPanel.setLayout(new FlowLayout());

	      mainFrame.add(headerLabel);
	      mainFrame.add(controlPanel);
	      mainFrame.add(statusLabel);
	      mainFrame.setVisible(true);  
	   }

	   public void showFileDialogDemo(){
	      headerLabel.setText("Control in action: FileDialog"); 
	      final FileDialog fileDialog = new FileDialog(mainFrame,"Select file");
	      Button showFileDialogButton = new Button("Open File");
	      Button startButton = new Button("Start");
	      
	      //Shorter maybe
	      TextField targetFile = new TextField("Results.html");
	      Label targetFileLabel = new Label("Target file:");
	      
	      TextField topicString = new TextField("Result word list");
	      Label topicStringLabel = new Label("Type topic:");
	      
	      showFileDialogButton.addActionListener(new ActionListener() {
	         @Override
	         public void actionPerformed(ActionEvent e) {
	            fileDialog.setVisible(true);
	            statusLabel.setText("File Selected :" 
	            + fileDialog.getDirectory() + fileDialog.getFile());
	            selFile = fileDialog.getDirectory() + fileDialog.getFile();
	            
	            selTopicString = topicString.getText();
	         }
	      });
	      
	      startButton.addActionListener(new ActionListener() {
		         @Override
		         public void actionPerformed(ActionEvent e){
		        	//User selected target filename for results.
		        	//FIXME currently writing rights of current directory are not checked for
		        	String s = targetFile.getText();
		        	for(char c:ILLEGAL_CHARACTERS) {
		        		if(s.contains(""+c)) {
		        			s = s.replace(c, '0');
		        			//TODO set some warning window
		        		}
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
		        	selTargetFile = targetFile.getText();
		        	Writeinfile WordPlace = new Writeinfile(selTargetFile, selTopicString);
		        	EvaluateText foobar = new EvaluateText(selFile);
		        	WordPlace.storeAllItems(foobar.GetWordsList());
		        	WordPlace.finishWriting();
		        	mainFrame.dispose();        	
		         }
		  });

	      Component[] abc = {showFileDialogButton, startButton, targetFileLabel, 
	    		  targetFile, topicStringLabel, topicString};
	      for (Component s:abc) {
	    	   controlPanel.add(s);
	      }
	      mainFrame.setVisible(true);  
	   }
}
