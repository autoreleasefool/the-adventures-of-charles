package charles.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.game.OptionManager;

public class QuestMenu extends GameMenu implements ListSelectionListener, FocusListener {

    private JTabbedPane tabbedPane;
    private JPanel[] questPanel = new JPanel[3];
    private JList[] questList = new JList[3];
    private JScrollPane[] questScroll = new JScrollPane[3];
    private JSplitPane[] questSplit = new JSplitPane[3];
    private JLabel[][] lblQuestInfo = new JLabel[3][4];
    
    private JTextField txtSearch;;
    private JButton btnSearch;;
    
    private int lastTab;
    private boolean searching;
    private String searchTerm = "";

    public QuestMenu(JDesktopPane desktop, JInternalFrame internal) {
	//Creates the frame
	super(desktop, internal, "Quests", 2);
	Dimension size = new Dimension(200, 450);
	
	//Creates panels and a list to display current quests
	//Creates another panel to show specifics about each quest in labels
	JPanel[] questInfo = new JPanel[3];
	for (int i = 0; i<3; i++) {
	    questPanel[i] = new JPanel();
	    questPanel[i].setPreferredSize(size);
	    questList[i] = new JList();
	    questList[i].setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	    questList[i].addListSelectionListener(this);
	    questScroll[i] = new JScrollPane(questList[i]);
	    questScroll[i].setMinimumSize(new Dimension(200,150));
	    
	    questInfo[i] = new JPanel();
	    questInfo[i].setLayout(new BoxLayout(questInfo[i],BoxLayout.Y_AXIS));
	    lblQuestInfo[i][0] = new JLabel("Name: N/A");
	    lblQuestInfo[i][1] = new JLabel("NPC: N/A");
	    lblQuestInfo[i][2] = new JLabel("Description: N/A");
	    lblQuestInfo[i][3] = new JLabel("Required: N/A");
	    questInfo[i].add(lblQuestInfo[i][0]);
	    questInfo[i].add(lblQuestInfo[i][1]);
	    questInfo[i].add(lblQuestInfo[i][2]);
	    questInfo[i].add(lblQuestInfo[i][3]);
	}
	
	//Creates a split pane to show both sections at once
	questSplit[0] = new JSplitPane(JSplitPane.VERTICAL_SPLIT, questScroll[0], questInfo[0]);
	questSplit[1] = new JSplitPane(JSplitPane.VERTICAL_SPLIT, questScroll[1], questInfo[1]);
	questSplit[2] = new JSplitPane(JSplitPane.VERTICAL_SPLIT, questScroll[2], questInfo[2]);
	
	//Adds split panes to the panels
	questPanel[0].add(questSplit[0]);
	questPanel[1].add(questSplit[1]);
	questPanel[2].add(questSplit[2]);
	
	//Adds the panels to the JTabbedpane
	tabbedPane = new JTabbedPane();
	tabbedPane.add("Avail", questPanel[0]);
	tabbedPane.add("In Prog", questPanel[1]);
	tabbedPane.add("Complete", questPanel[2]);
	
	//Creates a textfield to search and adds a focuslistener
	txtSearch = new JTextField(14);
	txtSearch.addFocusListener(this);
	
	//Creates a button to search and adds actionlistener
	btnSearch = new JButton("Search");
	btnSearch.addActionListener(this);
	
	//Creates panel to hold the search elements
	JPanel searchPanel = new JPanel();
	searchPanel.setPreferredSize(new Dimension(200,50));
	searchPanel.add(txtSearch);
	searchPanel.add(btnSearch);
	
	//Adds both to a contentpanel
	JPanel contentPanel = new JPanel(new BorderLayout());
	contentPanel.add(tabbedPane, BorderLayout.CENTER);
	contentPanel.add(searchPanel, BorderLayout.NORTH);
	
	//Sets the content panel on the center of the contentpane
	getContentPane().add(contentPanel,BorderLayout.CENTER);
	pack(); //Fits the frame to the contents
	
	addToDesktop(); //Adds the menu to the desktop
    }
    
    public void setTab(int index) {
	tabbedPane.setSelectedIndex(index); //Sets the current tab of the tabbed pane
    }
    
    public int getTab() {
	return tabbedPane.getSelectedIndex();   //Gets the current tab of the tabbed pane
    }
    
    public void updateComponents(Engine engine) {
	int index = tabbedPane.getSelectedIndex();  //Gets the current tab of the tabbed pane

	//Checks if the quests have been change and updates the JLists if so        
	if (engine.getPlayer().shouldUpdateQuests() || lastTab != index) {
	    txtSearch.setText("");
	    questList[index] = new JList(engine.getPlayer().getQuestNames(index));
	    engine.getPlayer().questsUpdated();
	    questScroll[index].removeAll();
	    questScroll[index].add(questList[index]);
	}
	
	//If the user is attempting to search, compiles an array of all the current quests and searches for the keyword which the user provides
	//in the name and the required items for the quest. If there is a match, it is added to a result arraylist and then put into
	//a JList and displayed
	if (searching) {
	    String[] names = engine.getPlayer().getQuestNames(index);
	    String[][] items = engine.getPlayer().getQuestItems(index);
	    boolean[] added = new boolean[names.length];
	    ArrayList<String> results = new ArrayList<String>();
	    for (int i = 0; i<names.length; i++) {
		if (names[i].indexOf(searchTerm) != -1) {
		    results.add(names[i]);
		    added[i] = true;
		}
	    }
	    for (int i = 0;i<items.length; i++) {
		for (int j = 0; j<items[i].length; j++) {
		    if (items[i][j].indexOf(searchTerm) != -1 && !added[i]) {
			results.add(names[i]);
			added[i] = true;
		    }
		}
	    }
	    
	    questList[index] = new JList(results.toArray());
	    questScroll[index].removeAll();
	    questScroll[index].add(questList[index]);
	    searching = false;
	}
	
	//Stores the last tab the user was on, to update quests if they change
	lastTab = index;
    }
    
    public void resetLocation() {
	//Sets the default hidden location of the menu
	setLocation(internal.getX() + internal.getWidth() - getWidth(), internal.getY());
    }
    
    public void actionPerformed(ActionEvent ae) {
	super.actionPerformed(ae);
	
	//If the search button is pressed and the search is not empty, the quests are searched
	if (ae.getSource() == btnSearch) {
	    if (!txtSearch.getText().equals("")) {
		searching = true;
		searchTerm = txtSearch.getText();
	    }
	}
    }
    
    public byte[] getConflictingMenus() {
	//Closes these menus when this menu opens
	return new byte[]{GameConstants.MENU_MINIMAP_ID, GameConstants.MENU_INVENTORY_ID};
    }
    
    public void valueChanged(ListSelectionEvent e) {
	//Intended to show the specifics about a quest when it is clicked
    }
    
    
    //Allows the search bar to take the focus from the main GameCanvas and returns it
    public void focusLost(FocusEvent fe) {
	OptionManager.canCanvasRequestFocus(true);
    }
    
    public void focusGained(FocusEvent fe) {
	OptionManager.canCanvasRequestFocus(false);
    }
}
