package charles.menu;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import charles.game.GameCanvas;
import charles.game.GameConstants;

public class CharlesMenuBar extends JMenuBar implements ActionListener {

    GameCanvas gc;

    public JMenu file, character, inventory, quests, map;
    public JMenuItem itemCredits, itemQuit;
    public JMenuItem itemTraits, itemAbilities, itemCustom;
    public JMenuItem itemEquip, itemUse, itemMisc;
    public JMenuItem itemAvail, itemProgress, itemComplete;
    public JMenuItem itemMini;
    
    public CharlesMenuBar(GameCanvas gc) {
	this.gc = gc;    

	
	//Create a JMenuBar, initialising the JMenuItems and their action listeners
	itemCredits = new JMenuItem("Credits");
	itemQuit = new JMenuItem("Exit");
	itemCredits.addActionListener(this);
	itemQuit.addActionListener(this);

	itemTraits = new JMenuItem("Traits");
	itemAbilities = new JMenuItem("Abilities");
	itemCustom = new JMenuItem("Customize Character");
	itemTraits.addActionListener(this);
	//itemAbilities.addActionListener(this);
	itemCustom.addActionListener(this);
	
	itemEquip = new JMenuItem("Equippable Items");
	itemUse = new JMenuItem("Useable Items");
	itemMisc = new JMenuItem("Miscellaneous Items");
	itemEquip.addActionListener(this);
	itemUse.addActionListener(this);
	itemMisc.addActionListener(this);
	
	itemAvail = new JMenuItem("Available");
	itemProgress = new JMenuItem("In Progress");
	itemComplete = new JMenuItem("Completed");
	itemAvail.addActionListener(this);
	itemProgress.addActionListener(this);
	itemComplete.addActionListener(this);

	itemMini = new JMenuItem("Mini Map");
	itemMini.addActionListener(this);
    
	//Adds these JMenuItems to JMenus
	file = new JMenu("File");
	file.add(itemCredits);
	file.addSeparator();
	file.add(itemQuit);
	
	character = new JMenu("Character");
	character.add(itemTraits);
	character.add(itemAbilities);
	character.addSeparator();
	character.add(itemCustom);
	
	inventory = new JMenu("Inventory");
	inventory.add(itemUse);
	inventory.add(itemEquip);
	inventory.add(itemMisc);
	
	quests = new JMenu("Quests");
	quests.add(itemAvail);
	quests.add(itemProgress);
	quests.add(itemComplete);
	
	map = new JMenu("Map");
	map.add(itemMini);
	
	//Adds these JMenus to this JMenuBar
	add(file);
	add(character);
	add(inventory);
	add(quests);
	add(map);
    }
    
    public void actionPerformed(ActionEvent ae) {
	if (ae.getSource() == itemCredits) {
	} else if (ae.getSource() == itemQuit) {
	    showQuitPrompt();   //Shows quit game prompt
	} else if (ae.getSource() == itemTraits) {
	    openOrCloseGameMenu(GameConstants.MENU_TRAITS_ID);  //Opens traits menu
	} else if (ae.getSource() == itemAbilities) {
	    openOrCloseGameMenu(GameConstants.MENU_ABILITIES_ID);   //Opens abilities menu
	} else if (ae.getSource() == itemCustom) {
	    openOrCloseGameMenu(GameConstants.MENU_CUSTOMIZE_ID);   //Opens customize menu
	} else if (ae.getSource() == itemEquip) {
	    openOrCloseInventoryMenu(2);    //Opens equip menu
	} else if (ae.getSource() == itemUse) {
	    openOrCloseInventoryMenu(1);    //Opens useable items menu
	} else if (ae.getSource() == itemMisc) {
	    openOrCloseInventoryMenu(0);    //Opens miscellaneous items menu
	} else if (ae.getSource() == itemAvail) {
	    openOrCloseQuestMenu(0);        //Opens availble quests menu
	} else if (ae.getSource() == itemProgress) {
	    openOrCloseQuestMenu(1);        //Opens quests in progress menu
	} else if (ae.getSource() == itemComplete) {
	    openOrCloseQuestMenu(2);        //Opens completed quests menu
	} else if (ae.getSource() == itemMini) {
	    openOrCloseGameMenu(GameConstants.MENU_MINIMAP_ID); //Opens minimap
	}
    }
    
    public void openOrCloseInventoryMenu(int index) {
	//Gets the Inventory Menu from the engine
	InventoryMenu im = (InventoryMenu) gc.getEngine().getMenu(GameConstants.MENU_INVENTORY_ID);
	if (im.getTab() == index)   //If the tab open is the selected tab
	    openOrCloseGameMenu(GameConstants.MENU_INVENTORY_ID);   //Opens or closes the menu
	else {                      //If the tab open is not the selected tab
	    im.setTab(index);       //Shows the selected tab
	    if (!im.isVisible())    //If the menu is not visible, opens it
		openOrCloseGameMenu(GameConstants.MENU_INVENTORY_ID);
	}
    }
    
    public void openOrCloseQuestMenu(int index) {
	//Gets Quest Menu from the engine
	QuestMenu qm = (QuestMenu) gc.getEngine().getMenu(GameConstants.MENU_QUESTS_ID);
	if (qm.getTab() == index)   //If the tab open is the selected tab
	    openOrCloseGameMenu(GameConstants.MENU_QUESTS_ID);  //Opens or closes the menu
	else {                      //If the tab open is not the selected tab
	    qm.setTab(index);       //Shows the selected tab
	    if (!qm.isVisible())    //If the menu is not visible, opens it
		openOrCloseGameMenu(GameConstants.MENU_QUESTS_ID);
	}
    }
    
    public void openOrCloseGameMenu(int index) {
	//Closes any menus that conflict with this meny opening
	if (!gc.getEngine().getMenu(index).isVisible()) {
	    byte[] conflictArray = gc.getEngine().getMenu(index).getConflictingMenus();
	    for (int i = 0; i<conflictArray.length; i++) {
		if (conflictArray[i]<5){
		if (gc.getEngine().getMenu(conflictArray[i]).isVisible())
		    gc.getEngine().closeMenu(conflictArray[i]);}
	    }
	    gc.getEngine().openMenu(index);
	} else {
	    gc.getEngine().closeMenu(index);
	}
    }
    
    public void disableGameOptions() {
	//Disables these buttons for when a game is not in play
	character.setEnabled(false);
	inventory.setEnabled(false);
	quests.setEnabled(false);
	map.setEnabled(false);
    }
    
    public void enableGameOptions() {
	//Enables these buttons for when a game is in 
	character.setEnabled(true);
	inventory.setEnabled(true);
	quests.setEnabled(true);
	map.setEnabled(true);
    }
    
    public void showQuitPrompt() {
	//Shows a confirm dialog prompting user whether they want to quit or not
	int response = JOptionPane.showConfirmDialog(gc.getGame().getFrame(), "Would you like to exit the game? Any progress since your last save will be lost.", "Exit Game", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	
	//If the user selects yes, the game exits
	if (response == JOptionPane.OK_OPTION)
	    System.exit(0);
    }
}
