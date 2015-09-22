package charles.game;

import java.awt.Dimension;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JOptionPane;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JLabel;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JTextField;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.FileInputStream;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import charles.entity.Player;
import charles.map.Map;

public class SaveGame extends JInternalFrame implements ActionListener, FocusListener{

    Engine engine;  
    JDesktopPane desktop;
    JButton[] btnNames;  
    JButton btnSearch;
    JPanel panel;
    JTextField txtSearch;
    JButton btnClose;

    public SaveGame(JDesktopPane desktop, Engine engine) {
	//Creates the frame and puts it on the desktop
	setTitle("Save/Load Game");
	this.desktop = desktop;
	this.engine = engine;
	desktop.add(this);
	setVisible(false);
    }
    
    public void openMenu() {
	//Either opens save game or load depending on user response
	int response = JOptionPane.showOptionDialog(null, "Would you like to save or load a game?", "Saving and Loading", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, new String[]{"Save", "Load"}, null);
	switch (response) {
	    case 0: saveGame(); break;
	    case 1: loadGame(); break;
	    default: return;
	}
    }
    
    private void loadGame() {
	
	ArrayList<String> savedGames = new ArrayList<String>(); //Creates an arraylist to load the files to
	
	//Opens this folder and adds the name for any files in this folder
	File folder = new File("C:\\temp\\TAOC");
	File[] files = folder.listFiles();
	for (int i = 0; i < files.length; i++) {
	    if (files[i].isFile())
		savedGames.add(files[i].getName());
	}
	
	//Converts the arraylist to an array
	String[] games = new String[savedGames.size()];
	savedGames.toArray(games);
	savedGames = null;
	
	
	//Creates a JPanel and adds all of the components to it, for searching and selecting
	panel = new JPanel();
	txtSearch = new JTextField(14);
	txtSearch.addFocusListener(this);
	btnSearch  = new JButton("Search");
	btnSearch.addActionListener(this);
	btnClose = new JButton("Cancel");
	btnClose.addActionListener(this);
	
	panel.setPreferredSize(new Dimension(200, 600));
	panel.add(btnClose);
	panel.add(txtSearch);
	panel.add(btnSearch);
	panel.add(new JLabel("SAVED GAMES BELOW"));
	
	//Loads each file onto a JButton
	btnNames = new JButton[games.length];
	for (int i= 0; i<games.length; i++) {
	    btnNames[i] = new JButton(games[i]);
	    btnNames[i].addActionListener(this);
	    panel.add(btnNames[i]);
	}

	add(panel);
	pack();             //Fits frame to the contents
	setVisible(true);   //Sets frame visible
    }
    
    private void saveGame() {
	//Prompts user for a name for their save
	String name = JOptionPane.showInputDialog(null, "Please enter a name for your save file:", "Name Your Save", JOptionPane.QUESTION_MESSAGE);
	if (name == null) {
	    JOptionPane.showMessageDialog(null, "That is an invalid name! Try again.", "Oops!", JOptionPane.WARNING_MESSAGE);
	    return;
	}
	
	//Checks if the temp folder exists and if it doesn't it creates one
	File dir = new File("C:\\temp\\TAOC");
	if (!dir.isDirectory())
	    dir.mkdir();
	
	//Writes all of the necessary values to a dat file (text file) in the temp folder on C Drive
	PrintWriter outputStream = null;
	try {
	    Player p = engine.getPlayer();
	    outputStream = new PrintWriter(new FileWriter("C:\\temp\\TAOC\\" + name + ".dat"));
	    String contents = p.getSaveInfo();
	    contents += engine.getMap().getSaveInfo();
	    outputStream.println(contents);
	} catch (Exception e) {
	    //Informs user if there is an error
	    JOptionPane.showMessageDialog(null, "That is an invalid name! Try again.", "Oops!", JOptionPane.WARNING_MESSAGE);
	    return;
	} finally {
	    //Closes the output stream and finalizes the file
	    outputStream.close();
	}
    }
    
    private void load(String name) {
	//Used to load strings
	StringBuffer loader = new StringBuffer();
	
	try {
	    //Loads the data from the selected file
	    InputStream in = new FileInputStream(new File("C:\\temp\\TAOC\\"+name));
	    BufferedReader input = new BufferedReader(new InputStreamReader(in));
	    try {
		String line = null;
		while ((line = input.readLine()) != null) {
		    loader.append(line + ":");
		}
	    } finally {
		input.close();//Closes the input stream
	    }
	} catch (IOException io) {
	    io.printStackTrace();
	}
	
	//Breaks the file up into an array
	String[] loadedText = loader.toString().split(":");
	engine.loadGame(loadedText);    //Sends the values to the engine to be loaded
	setVisible(false);
    }
    
    public void actionPerformed(ActionEvent ae) {
	for (int i = 0; i<btnNames.length; i++) {
	    if (ae.getSource() == btnNames[i]) {
		load(btnNames[i].getText());
		return;
		//Loads the selected button
	    }
	}
	
	//Hides buttons that do not contain the search time - does not work properly with desktoppane?
	if (ae.getSource() == btnSearch) {
	    if (!txtSearch.getText().equals("")) {
		for (int i = 0; i<btnNames.length; i++) {
		    panel.remove(btnNames[i]);
		    if (btnNames[i].getText().indexOf(txtSearch.getText()) != -1)
			panel.add(btnNames[i]);
		}
		desktop.validate();
		validate();
	    }
	}
	
	//Closes the frame
	if (ae.getSource() == btnClose) {
	    setVisible(false);
	    OptionManager.canCanvasRequestFocus(true);
	}
	
	
    }
    
    //Allows the textfield to take the focus from the canvas
    public void focusLost(FocusEvent fe) {
	OptionManager.canCanvasRequestFocus(true);
    }
    
    public void focusGained(FocusEvent fe) {
	OptionManager.canCanvasRequestFocus(false);
	JTextField f = (JTextField) fe.getSource();
	f.requestFocus();
    }
}
