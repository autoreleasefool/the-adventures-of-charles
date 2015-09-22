package charles.menu;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import charles.game.GameConstants;
import charles.game.Engine;
import charles.entity.Player;

public class TraitsMenu extends GameMenu {

    private JLabel[] lblTraits = new JLabel[6];
    
    private int totalPoints;
    private int usedPoints;
    private int[] statPoints = new int[6];
    private int[] curStats = new int[6];
    
    String[] strTraits = {"Strength", "Intel", "Dexterity", "Luck", "HP", "Mana"};

    public TraitsMenu(JDesktopPane desktop, JInternalFrame internal) {
	super(desktop, internal, "Traits", 0);  //Sends these values to super class
	
	JPanel traitsPanel = new JPanel();      //Creates a new JPanel with Left FlowLayout
	traitsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));
	traitsPanel.setPreferredSize(new Dimension(160, 500));  //Sets size of JPanel
	
	JPanel[] statPanel = new JPanel[6]; //Creates array of jpanels

	for (int i = 0; i<statPanel.length; i++) {
	    statPanel[i] = new JPanel();    //Initialises JPanel
	    statPanel[i].setLayout(new FlowLayout(FlowLayout.RIGHT));   //Sets layout to a right flowlayout
	    statPanel[i].setPreferredSize(new Dimension(160, 30));  //Sets size of panel;

	    lblTraits[i] = new JLabel(strTraits[i] + " (0 + 0)");   //Creates a new label
	    
	    //Adds label to panel, adds panel to main panel
	    statPanel[i].add(lblTraits[i]);
	    traitsPanel.add(statPanel[i]);
	}
	
	//Adds panel to contentpane
	getContentPane().add(traitsPanel, BorderLayout.CENTER);
	pack(); //Fits frame to the panel
	
	addToDesktop(); //Adds to the desktoppane
    }
    
    public void updateComponents(Engine engine) {
	
	//Loops through stats and updates any that have changed
	for (int i = 0; i<curStats.length; i++) {
	    int statCheck = getStatValue(engine.getPlayer(), i);
	    if (curStats[i] != statCheck) {
		curStats[i] = statCheck;
		lblTraits[i].setText(strTraits[i] + " (" + curStats[i] + " + " + statPoints[i] + ")");
	    }
	}
    }
    
    private int getStatValue(Player player, int stat) {
	//Returns the corresponding stat value
	switch(stat) {
	    case 0: return player.getStrength();
	    case 1: return player.getIntelligence();
	    case 2: return player.getDexterity();
	    case 3: return player.getLuck();
	    case 4: return player.getFullHealth();
	    case 5: return player.getFullMana();
	    default: return 0;
	}
    }
    
    public void resetLocation() {
	//Resets frame to its default position
	setLocation(internal.getX(), internal.getY());
    }
    
    public void actionPerformed(ActionEvent ae) {
	super.actionPerformed(ae);
    }
    
    public byte[] getConflictingMenus() {
	//Closes these menu when this one is opened
	return new byte[]{GameConstants.MENU_INFO_ID, GameConstants.MENU_CUSTOMIZE_ID, GameConstants.MENU_ABILITIES_ID};
    }
}
