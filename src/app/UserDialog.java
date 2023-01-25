package app;

import java.awt.*;
import java.awt.event.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

import java.text.MessageFormat;
import java.awt.Color;

import javax.swing.JLabel;
import javax.swing.JFrame;
import javax.swing.JButton;
import javax.swing.border.Border;
import javax.swing.border.SoftBevelBorder;

import javax.imageio.ImageIO;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JComboBox;
import javax.swing.JComponent;
//import javax.swing.JFileChooser;

/** User dialog widget */
public class UserDialog {

	private Localization lang;
	protected JFrame mainFrame;

	private SearchWords jsonSearchWords;

	private LabelledField field_topic, field_targetFile, field_status;
	private LabelledField field_fileToAnalyze;
	private LabelledArea field_supplinfo;
	private JLabel textareaLabel, searchTermBoxLabel, extractLengthLabel;
	private JLabel headline;
	private JLabel copyright = new JLabel("© 2021 Daniela Grothe");
	private JButton github = new JButton("GitHub");

	private WButton fileDialogButton, startButton, openButton, closeButton, clearButton;

	private JScrollPane listScroller;
	private JTextArea userTermsTextArea;

	private JCBox searchTermBox, localizationBox;
	private ToggleFunction chkNumbers, chkSymbols, chkWords, chkUserTerms;
	private JComboBox<Integer> extractLength;
	private Integer[] extractLengthValues = new Integer[] { 10, 20, 50, 75, 100, 120, 150 };
	private JPanel controlPanel, statusPanel, top;
	private ImagePanel background;
	private Image image;

	private GridBagConstraints fieldGridConfig, pgc;
	private GridBagLayout panelgrid, grid3;

	private String workingFolder;
	private FileDialog fileDialog;
	// private JFileChooser fileDialog;
	private int mode;

	public void switchMode(int mode) {
		this.mode += mode;
	}

	private String selFile, selTargetFile, fileType = "";
	private static final String[] ALLOWED_INPUT_FILES = { ".txt", ".md", ".tex", ".py", ".rb", ".yml", "html", "eml",
			".java", ".cpp", ".hpp", ".csv", ".cs" };
	private static final char[] ILLEGAL_CHARACTERS = { '/', '\n', '\r', '\t', '\0', '\f', '`', '?', '*', '\\', '<', '>',
			'|', '\"', ':' };
	private TimeCalc tc;

	// Colors
	private Color warnFG = new Color(255, 0, 0);
	private Color normalFG = new Color(0, 0, 0);
	private Color openFileBG = new Color(255, 177, 91);
	private Color startButtonBG = new Color(232, 111, 55);
	private Color openHTMLBG = new Color(92, 177, 92);
	private Color moss = new Color(170, 200, 170);
	private Color clearBG = moss;
	private Color light = new Color(190, 220, 190);
	private Color darker = new Color(130, 170, 130);
	private Color hint = new Color(130, 170, 130);
	private Color green = new Color(160, 180, 170);

	public UserDialog() {
		try {
			lang = new Localization();
			
			jsonSearchWords = new SearchWords();
			headline = new JLabel(lang.getLocalizedText("description"));

			Toolkit tk = Toolkit.getDefaultToolkit();
			Dimension d = tk.getScreenSize();
			System.out.println("Screen width = " + d.width);
			System.out.println("Screen height = " + d.height);

			mainFrame = new JFrame("Java Wordchecker");
			mainFrame.setSize(600, 460);
			mainFrame.setLayout(new GridLayout());

			makeFields();
			makeButtons();

			prepareGUI();

			searchTermBox = new JCBox("...", jsonSearchWords);

			listScroller = new JScrollPane(userTermsTextArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
					JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
			int height_of_scrollbar = listScroller.getVerticalScrollBar().getHeight();

			listScroller.getVerticalScrollBar().setPreferredSize(new Dimension(19, height_of_scrollbar));

			localizationBox = new JCBox("Language settings", lang);

			addButtons();
			addCheckboxGroup();

			positionFields();

			addListenersToUI();

			addListenersToButtons();

			showFileDialog();

			statusPanel.setVisible(true);
			searchTermBox.connect(this, userTermsTextArea, jsonSearchWords);
			localizationBox.connect(this, lang);

			mainFrame.setVisible(true);
			mainFrame.requestFocus();

		}
		catch (Exception awe) {
			// intended as fallback if UI cannot be shown on system
			System.out.println("Application window could not be opened. Reason:");
			System.out.println(awe.toString());
		}
	}

	public String getSelectedFile() {
		return selFile;
	}

	public void getSearchWordsFromJson(String key) {
		String rjs = jsonSearchWords.getText(key);
		userTermsTextArea.setText(rjs);
	}

	private void githubOpen() {
		Desktop desktop = Desktop.getDesktop();

		try {
			URL url = new URL("https://github.com/DGrothe-PhD/WordCheckerJava/");
			desktop.browse(url.toURI());
		} catch (Exception oError) {
			setMessage(lang.getLocalizedText("GitHubNotOpening"), 1);
		}
	}

	/** Open the results HTML file */
	private void htmlOpen() {
		Desktop desktop = Desktop.getDesktop();

		try {
			URI uri = new File(selTargetFile).toURI();
			desktop.browse(uri);
		} catch (Exception oError) {
			setMessage(lang.getLocalizedText("OutputFileNotOpening"), 1);
		} finally {
			mainFrame.revalidate();
		}
	}

	/** Lets user select which tokens to collect */
	private void addCheckboxGroup() {
		mode = 15;

		chkNumbers = new ToggleFunction(lang.getLocalizedText("Numbers"), this,
				CountWords.switchMode.c_Numbers.getMode());
		chkSymbols = new ToggleFunction(lang.getLocalizedText("Symbols"), this,
				CountWords.switchMode.c_Symbols.getMode());
		chkWords = new ToggleFunction(lang.getLocalizedText("Words"), this, CountWords.switchMode.c_Words.getMode());
		chkUserTerms = new ToggleFunction(lang.getLocalizedText("Search terms"), this,
				CountWords.switchMode.c_UserTerms.getMode());

		extractLengthLabel = new JLabel(lang.getLocalizedText("Extract length:"));
		extractLength = new JComboBox<Integer>(extractLengthValues);

		controlPanel.add(chkNumbers);
		controlPanel.add(chkSymbols);
		controlPanel.add(chkWords);
		controlPanel.add(chkUserTerms);
		controlPanel.add(extractLengthLabel);
		controlPanel.add(extractLength);
	}

	private void prepareGUI() {
		grid3 = new GridBagLayout();
		statusPanel = new JPanel();
		statusPanel.setBackground(moss);
		statusPanel.setSize(424, 280);
		statusPanel.setLayout(grid3);

		controlPanel = new JPanel();
		controlPanel.setLayout(new GridLayout(3, 2));
		controlPanel.setBackground(darker);
		controlPanel.setSize(424, 280);

		top = new JPanel();
		panelgrid = new GridBagLayout();
		top.setLayout(panelgrid);
		fieldGridConfig = new GridBagConstraints();

		pgc = new GridBagConstraints();
		pgc.fill = GridBagConstraints.HORIZONTAL;
		pgc.anchor = GridBagConstraints.NORTH;
		pgc.gridheight = 1;
		pgc.gridx = 0;
		pgc.gridy = 0;
		top.add(controlPanel, pgc);
		pgc.gridheight = 2;
		pgc.gridx = 0;
		pgc.gridy = 2;
		top.add(statusPanel, pgc);

		try {
			image = ImageIO.read(getClass().getResource("/app/background.JPG"));
			background = new ImagePanel(image);
			background.setLayout(new FlowLayout());
			background.add(headline);
			background.add(top, "Center");
			background.add(copyright);
			Border raised = new SoftBevelBorder(SoftBevelBorder.RAISED);
			github.setBorder(raised);
			github.setSize(20, 50);
			github.setBackground(moss);
			background.add(github);
			mainFrame.add(background);
		} catch (Exception e) {
			mainFrame.setBackground(moss);
			mainFrame.add(headline);
			mainFrame.add(top, "Center");
			mainFrame.add(copyright);
			mainFrame.add(github);
		}
	}

	private void makeFields() {
		searchTermBoxLabel = new JLabel(lang.getLocalizedText("Search terms:"));
		searchTermBoxLabel.setFont(WFont.labelfont);

		textareaLabel = new JLabel(lang.getLocalizedText("Edit search terms:"));
		textareaLabel.setFont(WFont.labelfont);

		field_topic = new LabelledField(lang.getLocalizedText("Type topic:"),
				lang.getLocalizedText("Result word list"));
		field_targetFile = new LabelledField(lang.getLocalizedText("Target file:"), "Word_occurrences.html");
		field_status = new LabelledField(lang.getLocalizedText("Selected folder:"), "", hint, false);
		field_fileToAnalyze = new LabelledField(lang.getLocalizedText("Selected file:"),
				"- " + lang.getLocalizedText("Please select a file") + " -", light, false);
		field_supplinfo = new LabelledArea(lang.getLocalizedText("Info:"), "", green, false);

		userTermsTextArea = new JTextArea();
		userTermsTextArea.setFont(WFont.descriptionFont);
		userTermsTextArea.setColumns(LabelledField.FIELD_WIDTH);
		userTermsTextArea.setRows(5);
		userTermsTextArea.setToolTipText(lang.getLocalizedText("Edit tooltip"));
		userTermsTextArea.setEditable(true);
	}

	private void makeButtons() {
		fileDialog = new FileDialog(mainFrame, lang.getLocalizedText("Select file"));
		// fileDialog = new JFileChooser(mainFrame);
		// fileDialog.setDialogTitle(lang.getANSI("Select file"));
		fileDialogButton = new WButton(lang.getLocalizedText("Open file"), openFileBG);

		startButton = new WButton(lang.getLocalizedText("Start"), startButtonBG);
		startButton.setPreferredSize(new Dimension(116, 30));

		openButton = new WButton(lang.getLocalizedText("Show Results"), openHTMLBG);
		closeButton = new WButton(lang.getLocalizedText("Close window"), moss);
		clearButton = new WButton(lang.getLocalizedText("Clear search"), clearBG);
	}

	protected void addTextToButtons() {
		fileDialog.setTitle(lang.getLocalizedText("Select file"));
		fileDialogButton.setText(lang.getLocalizedText("Open file"));
		startButton.setText(lang.getLocalizedText("Start"));
		openButton.setText(lang.getLocalizedText("Show Results"));
		closeButton.setText(lang.getLocalizedText("Close window"));
		clearButton.setText(lang.getLocalizedText("Clear search"));

		headline.setText(lang.getLocalizedText("description"));
		chkNumbers.setText(lang.getLocalizedText("Numbers"));
		chkSymbols.setText(lang.getLocalizedText("Symbols"));
		chkWords.setText(lang.getLocalizedText("Words"));
		chkUserTerms.setText(lang.getLocalizedText("Search terms"));

		searchTermBoxLabel.setText(lang.getLocalizedText("Search terms:"));
		extractLengthLabel.setText(lang.getLocalizedText("Extract length:"));
		textareaLabel.setText(lang.getLocalizedText("Edit search terms:"));
		userTermsTextArea.setToolTipText(lang.getLocalizedText("Edit tooltip"));

		field_topic.setText(lang.getLocalizedText("Result word list"));
		field_topic.thelabel.setText(lang.getLocalizedText("Type topic:"));
		field_targetFile.thelabel.setText(lang.getLocalizedText("Target file:"));
		field_status.thelabel.setText(lang.getLocalizedText("Selected folder:"));
		field_fileToAnalyze.thelabel.setText(lang.getLocalizedText("Selected file:"));
		if (field_fileToAnalyze.getText().startsWith("-")) {
			field_fileToAnalyze.setText(lang.getLocalizedText("Please select a file"));
		}
		field_supplinfo.thelabel.setText(lang.getLocalizedText("Info:"));
	}

	private void addButtons() {
		JComponent[] abc = { fileDialogButton, startButton, openButton, closeButton, clearButton };
		for (JComponent s : abc)
			controlPanel.add(s);
	}

	// placement of UI elements
	private void positionFields() {
		/// ~~~ gbc region ~~~
		fieldGridConfig.fill = GridBagConstraints.HORIZONTAL;
		fieldGridConfig.anchor = GridBagConstraints.NORTH;
		// left column
		fieldGridConfig.gridx = 0;
		fieldGridConfig.gridy = 0;
		statusPanel.add(field_topic.thelabel, fieldGridConfig);
		// gbc.gridx = 0;
		fieldGridConfig.gridy = 1;
		statusPanel.add(field_targetFile.thelabel, fieldGridConfig);
		fieldGridConfig.gridy = 3;
		statusPanel.add(field_status.thelabel, fieldGridConfig);
		fieldGridConfig.gridy = 4;
		statusPanel.add(field_fileToAnalyze.thelabel, fieldGridConfig);
		fieldGridConfig.weightx = 1.0;
		fieldGridConfig.gridy = 5;
		statusPanel.add(field_supplinfo.thelabel, fieldGridConfig);
		fieldGridConfig.gridy = 32;
		statusPanel.add(textareaLabel, fieldGridConfig);

		// right column is broader
		fieldGridConfig.gridwidth = 2;
		fieldGridConfig.gridx = 1;
		fieldGridConfig.gridy = 0;
		statusPanel.add(field_topic.jcomp, fieldGridConfig);
		fieldGridConfig.gridy = 1;
		statusPanel.add(field_targetFile.jcomp, fieldGridConfig);
		fieldGridConfig.gridy = 3;
		statusPanel.add(field_status.jcomp, fieldGridConfig);
		fieldGridConfig.gridy = 4;
		statusPanel.add(field_fileToAnalyze.jcomp, fieldGridConfig);
		fieldGridConfig.gridy = 5;
		statusPanel.add(field_supplinfo.jcomp, fieldGridConfig);

		/// snippet searchtermbox start
		fieldGridConfig.gridx = 0;
		fieldGridConfig.gridy = 6;
		fieldGridConfig.gridwidth = 2;
		statusPanel.add(searchTermBoxLabel, fieldGridConfig);
		fieldGridConfig.gridx = 1;
		fieldGridConfig.gridy = 6;

		statusPanel.add(listScroller);
		statusPanel.add(searchTermBox, fieldGridConfig);
		fieldGridConfig.gridy = 8;
		statusPanel.add(localizationBox, fieldGridConfig);
		/// snippet end

		fieldGridConfig.gridy = 32;
		statusPanel.add(listScroller, fieldGridConfig);
	}

	/** show status messages */
	private void setMessage(String settext, int warning) {
		field_status.setText(settext);
		field_status.jcomp.setForeground(warning == 1 ? warnFG : normalFG);
	}

	/** show extra message @param up to 3 strings */
	private void setSupplMessage(String s, String... t) {
		String t1 = t.length > 0 ? t[0] : "";
		String t2 = t.length > 1 ? t[1] : "";
		field_supplinfo.setText(s + " " + t1 + " " + t2);
		field_supplinfo.setForeground(normalFG);
	}

	private void setSupplWarning(String s, String... t) {
		String t1 = t.length > 0 ? t[0] : "";
		String t2 = t.length > 1 ? t[1] : "";
		field_supplinfo.setText(s + " " + t1 + " " + t2);
		field_supplinfo.setForeground(warnFG);
	}

	private String setWritingTarget() {
		// Validate results filename from input text field
		String s = field_targetFile.getText();
		String illchar = "";

		for (char c : ILLEGAL_CHARACTERS) {
			if (s.contains("" + c)) {
				illchar += ("" + c);
			}
		}
		if (illchar.length() > 0) {
			setMessage(MessageFormat.format(lang.getLocalizedText("BadSymbolInFilename"), illchar), 1);
			return null;
		}
		String fileprefix = lang.getLocalizedText("Results") + "_";
		if (s.length() > 5 && s.endsWith(".html"))
			return fileprefix + s;
		return workingFolder + fileprefix + s + ".html";
	}

	private void addListenersToUI() {
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

		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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

	private void addListenersToButtons() {
		/** Opens the result HTML file (in the standard browser). */
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
	private void showFileDialog() {

		/** The Open File or Browse... button to select input text file. */
		fileDialogButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setMessage("", 0);
				fileDialog.setVisible(true);
				processFile();
				mainFrame.revalidate();
			}
		});

		fileDialogButton.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent event) {
				if (event.getKeyChar() == KeyEvent.VK_TAB) {
					fileDialogButton.requestFocus();
				} else if (event.getKeyChar() == KeyEvent.VK_ENTER) {
					setMessage("", 0);
					fileDialog.setVisible(true);
					processFile();
				}
				mainFrame.revalidate();
			}
		});
		// internal file reading error
		/**
		 * The start button. When pressed, chosen file is processed, and results are
		 * written into the target file.
		 */
		startButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				tc = new TimeCalc();
				try {
					Settings.setExtractSize((int) extractLength.getSelectedItem());
					if (selFile == null || selFile.length() < 2) {
						setMessage(lang.getLocalizedText("PleaseSelectFile"), 1);
						throw new WriteException("");
					} else if (fileType.length() < 2) {
						setMessage(lang.getLocalizedText("suggestFileType") + " .txt, .html, .tex, .csv, ...", 1);
						throw new WriteException("");
					}
					selTargetFile = setWritingTarget();

					String str = userTermsTextArea.getText();

					EvaluateText etx = new EvaluateText(str, mode, lang);

					try {
						etx.eTextToolBox(selFile, lang);
						Writeinfile WordPlace = new Writeinfile(selTargetFile, field_topic.getText(), mode, lang);
						WordPlace.storeAllItems(etx.GetWordsList());
						WordPlace.finishWriting();
						fileType = "";
						setSupplMessage(
								Integer.toString(etx.GetNumberOfWords()) + lang.getLocalizedText("wordscounted"),
								tc.getDuration());
					} catch (IOException exc) {
						setSupplWarning(lang.getLocalizedText("FileNotRead"));
					} catch (WriteException wfe) {
						setSupplWarning(wfe.getMessage());
						System.out.println(wfe.getCause());
					} catch (Exception exc) {
						setSupplWarning(lang.getLocalizedText("Internal error"));
					}
				}

				catch (WriteException wfe) {
					if (wfe.getMessage() != "") {
						setSupplWarning(wfe.getMessage());
						System.out.println(wfe.getCause());
					}
				} catch (Exception exc) {
					System.out.println("Some UI or other exception." + exc.getCause() + exc.toString());
				} finally {
					mainFrame.revalidate();
				}
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

	private void processFile() {
		workingFolder = "" + fileDialog.getDirectory();
		selFile = workingFolder + fileDialog.getFile();
		field_status.setText("..." + workingFolder);
		field_fileToAnalyze.setText(fileDialog.getFile());
		field_supplinfo.setText("");

		fileType = "";
		for (String str : ALLOWED_INPUT_FILES) {
			if (selFile.endsWith(str)) {
				fileType = str;
				break;
			}
		}

		String str = field_fileToAnalyze.getText();
		if (str.lastIndexOf('.') > 0)
			str = str.substring(0, str.lastIndexOf('.'));
		field_targetFile.setText(str);
		field_topic.setText(str);
	}
}

class ImagePanel extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -617389724139894161L;
	private Image image;
	private boolean tile;

	ImagePanel(Image image) {
		this.image = image;
		this.tile = false;
		this.setSize(getWidth(), getHeight());
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
			Image scaled = image.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH);
			g.drawImage(scaled, 0, 0, this);
		}
	}
}

class JCBox extends JComboBox<String> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 6742906931820220645L;
	String rjs = "";
	String defaultDescription = "";

	public String[] choices;

	public String getSelectedList() {
		return rjs;
	}

	public JCBox(String defaultDescription, ReadJson jsonData) {
		try {
			this.defaultDescription = defaultDescription;
			this.setFont(WFont.labelfont);
			this.setLightWeightPopupEnabled(false);
			choices = jsonData.getAll();
			this.addItem(defaultDescription);
			this.setSelectedItem(defaultDescription);
			for (String s : choices) {
				this.addItem(s);
			}
		} catch (Exception exc) {
			this.setSelectedItem(exc.toString());
		}
	}

	public void connect(UserDialog ud, JTextArea textarea, SearchWords jsonData) {
		/// adding functionality

		/// add listener
		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					rjs = jsonData.getText(getSelectedItem().toString());
					textarea.setText(rjs);
					ud.mainFrame.revalidate();
				}
			}
		});
	}

	public void connect(UserDialog ud, Localization lang) {
		this.addItemListener(new ItemListener() {
			@Override
			public void itemStateChanged(ItemEvent e) {
				if (e.getStateChange() == ItemEvent.SELECTED) {
					String foo = getSelectedItem().toString();
					lang.switchTo(foo);
					ud.addTextToButtons();
					ud.mainFrame.revalidate();
				}
			}
		});
	}

}