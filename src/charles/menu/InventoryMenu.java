/*

I originally intended to use swing components to create this menu, but as I would need to update it 30 times per second,
removing, redeclaring and readding the JButtons each time would be highly ineffecient, so I worked with images and a MouseListener
instead, which I can update quickly

*/

package charles.menu;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;

import charles.entity.Item;
import charles.game.GameConstants;
import charles.game.Engine;
import charles.resources.Art;
import charles.resources.FontRenderer;

public class InventoryMenu extends GameMenu implements MouseListener, MouseMotionListener {

    JTabbedPane tabbedPane;
    JPanel[] invenPanel = new JPanel[3];
    BufferedImage inventory = new BufferedImage(197, 491, BufferedImage.TYPE_INT_RGB);
    int[] playerInventory;
    
    JLabel lblCoins;
    JButton btnDropCoins;
    
    boolean dragging;
    boolean dropItem;
    boolean dropAll;
    int dropIndex;
    int dragIndex;
    int mouseX, mouseY;
    int nameDisplay;
    
    boolean dropCoin;
    int coinAmount;
    
    boolean useItem;
    int useIndex;
    
    boolean doubleClicked;
    boolean doubleClickUsed;
    int doubleTimer;

    public InventoryMenu(JDesktopPane desktop, JInternalFrame internal) {
	super(desktop, internal, "Inventory", 2);   //Sends these values to the super class
	Dimension size = new Dimension(197, 491);   //Sets size of the panel
	
	//Initialises panels, sets sizes and adds mouselisteners
	invenPanel[0] = new JPanel();
	invenPanel[1] = new JPanel();
	invenPanel[2] = new JPanel();
	invenPanel[0].setPreferredSize(size);
	invenPanel[1].setPreferredSize(size);
	invenPanel[2].setPreferredSize(size);
	invenPanel[0].addMouseListener(this);
	invenPanel[1].addMouseListener(this);
	invenPanel[2].addMouseListener(this);
	invenPanel[0].addMouseMotionListener(this);
	invenPanel[1].addMouseMotionListener(this);
	invenPanel[2].addMouseMotionListener(this);
	
	//Creates a tabbed pane and adds the panels to them
	tabbedPane = new JTabbedPane();
	tabbedPane.add("Misc", invenPanel[0]);
	tabbedPane.add("Useable", invenPanel[1]);
	tabbedPane.add("Equip", invenPanel[2]);
	
	//Creates button to drop coins
	btnDropCoins = new JButton("Drop Coins");
	btnDropCoins.addActionListener(this);
	
	//Creates label for coins
	lblCoins = new JLabel("Coins: 0");
	
	//Adds coin elements to coin panel
	JPanel coinPanel = new JPanel();
	coinPanel.setPreferredSize(new Dimension(197, 49));
	coinPanel.add(lblCoins);
	coinPanel.add(btnDropCoins);
	
	//Adds tabbedPane and coinPanel to the contentPanel
	JPanel contentPanel = new JPanel(new BorderLayout());
	contentPanel.add(tabbedPane, BorderLayout.CENTER);
	contentPanel.add(coinPanel, BorderLayout.SOUTH);
	
	//Adds contentPanel to the frame
	getContentPane().add(contentPanel, BorderLayout.CENTER);
	pack(); //Fits frame to contents
	
	addToDesktop(); //Adds frame to desktoppane
    }
    
    public void setTab(int index) {
	tabbedPane.setSelectedIndex(index); //Sets the current showing tab
    }
    
    public int getTab() {
	return tabbedPane.getSelectedIndex();   //Returns the current showing tab
    }
    
    public void updateComponents(Engine engine) {
	int index = tabbedPane.getSelectedIndex();  //Gets the currently showing tab
	
	if (doubleTimer > 0)    //If timer is greater than 0
	    doubleTimer--;      //Decreases it (used to check for double clicking)

	//Checks if an item is going to be dropped and drops the specified amount
	if (dropItem) {
	    if (dropAll) {
		while (engine.getPlayer().getItemCount(dropIndex) > 0)
		    engine.getPlayer().dropItem(dropIndex);
	    } else
		engine.getPlayer().dropItem(dropIndex);
	    dropItem = false;
	}
	
	//Drops amount of coins specified
	if (dropCoin) {
	    engine.getPlayer().dropMoney(coinAmount);
	    dropCoin = false;
	}
	
	//If the number of coins the player has has changed, the label is updated
	if (Integer.parseInt(lblCoins.getText().substring(7)) != engine.getPlayer().getWallet())
	    lblCoins.setText("Coins: " + engine.getPlayer().getWallet());
	    
	//Uses an item if the player double clicks it
	if (useItem) {
	    engine.getPlayer().useItem(useIndex);
	    useItem = false;
	}
	
	//Gets graphics from the inventory image
	Graphics g = (Graphics) inventory.getGraphics();
	g.setColor(new Color(230, 230, 230));   //Fills with gray rectangle
	g.fillRect(0, 0, inventory.getWidth(), inventory.getHeight());
	
	//Adds black lines to section off areas
	g.setColor(Color.black);
	for (int x = 0; x<40; x++) {
	    int xx = x % 4, yy = x / 4;
	    g.drawRect(xx * 49, yy * 49, 49, 49);
	}

	nameDisplay = -1;
	
	//Loops through items in the player's inventory and checks if it is being hovered over.
	//If it is, then the name of the item is drawn to the screen
	int itemCount = 0;
	playerInventory = engine.getPlayer().getInventory();
	for (int i = index * 80; i<(index + 1) * 80 && i<playerInventory.length; i++) {
	    if (playerInventory[i] > 0) {
		if (engine.getPlayer().isItemEquipped(i))
		    g.setColor(new Color(201,255,178));
		else
		    g.setColor(Color.white);
		    
		g.fillRect(itemCount % 4 * 49 + 1, itemCount / 4 * 49 + 1, 48, 48);
		int xx = i % 16, yy = i / 16;
		g.drawImage(Art.itemSheet.getSubimage(xx * 16, yy * 16, 16, 16), itemCount % 4 * 49 + 1, itemCount / 4 * 49 + 1, 48, 48, null);
		switch (i) {
		    case 6: //Black images get gray font
		    case 12:
		    case 16:
		    case 21:
		    case 22:
		    case 35:
		    case 42:
			FontRenderer.drawString(playerInventory[i]+"", itemCount % 4 * 49 + 49 - Integer.toString(playerInventory[i]).length() * 12, itemCount / 4 * 49 + 49 - 20, 0xff606060, 2, g);
			break;
		    default: //Not black images
			FontRenderer.drawString(playerInventory[i]+"", itemCount % 4 * 49 + 49 - Integer.toString(playerInventory[i]).length() * 12, itemCount / 4 * 49 + 49 - 20, 0xff000000, 2, g);
			break;
		}
		
		if (mouseX > itemCount % 4 * 49 + 1 && mouseX < itemCount % 4 * 49 + 49 && mouseY > itemCount / 4 * 49 + 1 && mouseY < itemCount / 4 * 49 + 49)
		    nameDisplay = i;
		
		itemCount++;
	    }
	}
	
	//If there are no items in the inventory, no name is shown
	if (itemCount == 0)
	    nameDisplay = -1;
	
	if (dragging) {
	    //Draws a draggedimage of the selected item where the mouse is
	    g.setColor(new Color(0, 0, 0, 0.75f));
	    g.fillRect(mouseX - 16, mouseY - 16, 32, 32);
	    int xx = dragIndex % 16, yy = dragIndex / 16;
	    g.drawImage(Art.itemSheet.getSubimage(xx * 16, yy * 16, 16, 16), mouseX - 16, mouseY - 16, 32, 32, null);
	    nameDisplay = -1;
	} else {
	    //Draws the name of the item being hovered over
	    if (nameDisplay >= 0) {
		int xOffset = mouseX + 13 + Art.itemNames[nameDisplay].getWidth() - inventory.getWidth();
		int yOffset = mouseY + 18 + Art.itemNames[nameDisplay].getHeight() - inventory.getHeight();
		if (xOffset < 0)
		    xOffset = 0;
		if (yOffset < 0)
		    yOffset = 0;
		    
		g.drawImage(Art.itemNames[nameDisplay], mouseX + 13 - xOffset, mouseY + 18 - yOffset, null);
	    }
	}
	
	//Disposes the graphics object
	g.dispose();
	
	//Draws the BufferedImage to the current panel
	g = (Graphics) invenPanel[index].getGraphics();
	g.drawImage(inventory, 0, 0, null);
	g.dispose();

	//Cancels any double clicks
	doubleClicked = false;
    }
    
    public void mouseClicked(MouseEvent me) {
	if (me.getButton() != MouseEvent.BUTTON1)
	    return; //If any other mouse buttons are clicked, the method exits
	    
	if (doubleTimer == 0) { //Starts a timer for the player to double click within
	    doubleTimer = 10;
	    return;
	}
	//Gets here if the user has double clicked fast enough
	
	//Checks which item the user double clicked and saves it
	doubleClicked = true;
	int index = tabbedPane.getSelectedIndex();
	int itemCount = 0;
	for (int i = index * 80; i<(index + 1) * 80 && i < playerInventory.length; i++) {
	    if (playerInventory[i] > 0) {
		if (me.getX() >= (itemCount % 4) * 49 + 1 && me.getX() <= (itemCount % 4) * 49 + 49 && me.getY() >= itemCount / 4 * 49 + 1 && me.getY() <= itemCount / 4 * 49 + 49) {
		    useIndex = i;
		    useItem = true;
		}
		itemCount++;
	    }
	}
    }

    public void mousePressed(MouseEvent me) {
	//Pauses the method for 100 milliseconds to allow time for double clicking
	try {
	    Thread.sleep(100);
	} catch (InterruptedException ie) {
	    ie.printStackTrace();
	}
    
	//If the user is dragging or double clicking, the method exits
	if (dragging || doubleClicked)
	    return;
    
	//Gets the item which the user has selected and sets whether it will drop one item or all items if dragged off the screen
	//Left mouse button is one item, right mouse button is all items
	int index = tabbedPane.getSelectedIndex();
	int itemCount = 0;
	for (int i = index * 80; i<(index + 1) * 80 && i < playerInventory.length; i++) {
	    if (playerInventory[i] > 0) {
		if (me.getX() >= (itemCount % 4) * 49 + 1 && me.getX() <= (itemCount % 4) * 49 + 49 && me.getY() >= itemCount / 4 * 49 + 1 && me.getY() <= itemCount / 4 * 49 + 49) {
		    dragging = true;
		    dragIndex = i;
		    
		    if (me.getButton() == MouseEvent.BUTTON1)
			dropAll = false;
		    else if (me.getButton() == MouseEvent.BUTTON3)
			dropAll = true;
		}
		itemCount++;
	    }
	}
    }
    
    public void mouseReleased(MouseEvent me) {
	if (dragging) {
	    //Stops th item from being dragged and drops it if it is not within boundaries of the menu
	    if (me.getX() < 0 || me.getX() > 197 || me.getY() < 0 || me.getY() > 540) {
		dropItem = true;
		dropIndex = dragIndex;
	    }
	    dragging = false;
	}
    }
    
    
    //Stores x and y of the mouse
    public void mouseMoved(MouseEvent me) {
	mouseX = me.getX();
	mouseY = me.getY();
    }
    
    //Stores x and y of the mouse
    public void mouseDragged(MouseEvent me) {
	mouseX = me.getX();
	mouseY = me.getY();
    }
    
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}
    
    public void resetLocation() {
	//Sets the menu to the default hidden location
	setLocation(internal.getX() + internal.getWidth() - getWidth(), internal.getY());
    }
    
    public void actionPerformed(ActionEvent ae) {
	super.actionPerformed(ae);
	
	//If the user selects to drop coins
	if (ae.getSource() == btnDropCoins) {
	    int playerCoins = Integer.parseInt(lblCoins.getText().substring(7));    //Gets the amount of coins the player has
	    if (playerCoins > 0) {  //If they have more than 0 coins
		do {
		    //Prompts user to drop coins
		    String s = JOptionPane.showInputDialog(null, "How many coins would you like to drop? (Max: " + playerCoins +")", "Drop Coins", JOptionPane.QUESTION_MESSAGE);
		    if (s == null) return;  //If they cancel, nothing is down
		    try {
			//Checks to ensure a number was input and they player can drop that many coins
			coinAmount = Integer.parseInt(s);
			if (coinAmount < 0 || coinAmount > playerCoins)
			    throw new NumberFormatException();
			    
			dropCoin = true;    //Sets coins to be dropped at next update
			return;
		    } catch (NumberFormatException nfe) {
			//Prints warning message for player that they can't drop the amount they specified
			JOptionPane.showMessageDialog(null, "You cannot drop that amount!", "Oops!", JOptionPane.WARNING_MESSAGE);
		    }
		} while(true);
	    }
	}
    }

    public byte[] getConflictingMenus() {
	//Closes these menus when this menu opens
	return new byte[]{GameConstants.MENU_MINIMAP_ID, GameConstants.MENU_QUESTS_ID};
    }
}
