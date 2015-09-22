package charles.menu;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JCheckBox;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.game.OptionManager;
import charles.resources.Art;

public class CustomizeCharacterMenu extends GameMenu implements ChangeListener, ItemListener {

    Dimension size = new Dimension(160, 480);
    BufferedImage player = new BufferedImage(150, 300, BufferedImage.TYPE_INT_RGB);
    
    JSlider[] colorSlider = new JSlider[3];
    JMenuItem[] itemPiece = new JMenuItem[10];
    JMenuItem[] itemStyle = new JMenuItem[3];
    JMenu menuCustomize;
    JCheckBox chkLinkShadows;
    JCheckBox chkBowtie;
    JLabel lblPlayerPic;
    
    int selected = 0;
    boolean changeActive = true;
    boolean linkShadows = true;
    
    String[] itemText = {"Shirt", "Shirt shadow", "Undershirt", "Pants", "Buttons", "Hair", "Eyes", "Skin", "Skin shadow", "Shoes"};

    public CustomizeCharacterMenu(JDesktopPane desktop, JInternalFrame internal) {
	//Creates the frame
	super(desktop, internal, "Customize", 0);
	
	//Creates labels to display color names
	JLabel[] colorLabel = new JLabel[3];
	
	for (int i = 0; i<3; i++) {
	    colorLabel[i] = new JLabel();
	    
	    //Creates JSliders to control RGB amounts
	    colorSlider[i] = new JSlider(JSlider.HORIZONTAL, 0, 100, 100);
	    colorSlider[i].setPreferredSize(new Dimension(120, 27));
	    colorSlider[i].addChangeListener(this);
	}
	
	//Sets text of labels
	colorLabel[0].setText("R: ");
	colorLabel[1].setText("G: ");
	colorLabel[2].setText("B: ");
	
	//Creates a check box for autoshading option
	chkLinkShadows = new JCheckBox("Auto Shade");
	chkLinkShadows.setSelected(true);
	chkLinkShadows.addItemListener(this);
	
	//Creates checkbox for bowtie option
	chkBowtie = new JCheckBox("Bowtie");
	chkBowtie.setSelected(false);
	chkBowtie.addItemListener(this);
	
	//Gets picture of the player
	lblPlayerPic = new JLabel();
	lblPlayerPic.setIcon(loadPlayerPicture());
	
	//Creates a JMenuBar listing the things the player can customize
	menuCustomize = new JMenu("Change Shirt");
	for (int i = 0; i<itemPiece.length; i++) {
	    itemPiece[i] = new JMenuItem("Change " + itemText[i]);
	    itemPiece[i].addActionListener(this);
	    menuCustomize.add(itemPiece[i]);
	}
	//Disables these buttons for autoshading
	itemPiece[GameConstants.PLAYER_COLOR_SHIRT_SHADOW].setEnabled(false);
	itemPiece[GameConstants.PLAYER_COLOR_SKIN_SHADOW].setEnabled(false);
	
	//Creates styles which the player can set as JMenuItems
	String[] styleText = {GameConstants.PLAYER_NAME_FIRST, "Leprechaun", "Psy"};
	JMenu menuStyle = new JMenu("Style");
	for (int i = 0; i<itemStyle.length; i++) {
	    itemStyle[i] = new JMenuItem(styleText[i]);
	    itemStyle[i].addActionListener(this);
	    menuStyle.add(itemStyle[i]);
	}
	
	//Creates the JMenuBar and adds the JMenuItems
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(menuCustomize);
	menuBar.add(menuStyle);
	
	//Creates a panel for all of the color sliders and labels
	JPanel colorPanel = new JPanel();
	colorPanel.setPreferredSize(new Dimension(160, 300));
	colorPanel.add(colorLabel[0]);
	colorPanel.add(colorSlider[0]);
	colorPanel.add(colorLabel[1]);
	colorPanel.add(colorSlider[1]);
	colorPanel.add(colorLabel[2]);
	colorPanel.add(colorSlider[2]);
	
	//Creates a panel for the picture and checkboxes
	JPanel customPanel = new JPanel();
	customPanel.setPreferredSize(size);
	customPanel.add(lblPlayerPic);
	customPanel.add(chkLinkShadows);
	customPanel.add(chkBowtie);
	customPanel.add(colorPanel);
	
	//Resets the JSliders, adds the menu bar and panels and fits the frame to its contents
	resetSliders();
	setJMenuBar(menuBar);
	getContentPane().add(customPanel, BorderLayout.CENTER);
	pack();
	
	addToDesktop(); //Adds this menu to the desktop
    }
    
    private ImageIcon loadPlayerPicture() {
	//Gets graphics from the player image
	Graphics g = (Graphics) player.getGraphics();

	//Fills the background with a dark green
	g.setColor(new Color(77,194,76));
	g.fillRect(0, 0, player.getWidth(), player.getHeight());
	
	//Draws the image of the player to the image
	g.drawImage(Art.charTiled.getSubimage(0, 0, 16, 32), 0, 0, player.getWidth(), player.getHeight(), null);
	
	//Returns the BufferedImage cast to an ImageIcon
	return new ImageIcon((Image)player);
    }
    
    private void resetSliders() {
	changeActive = false;   //Stops sliders from changing values in the option manager
	
	//Sets the color sliders to the value of the new colors
	colorSlider[0].setValue((int)(OptionManager.playerColors[selected].getRed() / 255D * 100));
	colorSlider[1].setValue((int)(OptionManager.playerColors[selected].getGreen() / 255D * 100));
	colorSlider[2].setValue((int)(OptionManager.playerColors[selected].getBlue() / 255D * 100));
	
	changeActive = true;    //Re-enables sliders
    }
    
    public void resetLocation() {
	//Sets default hidden location of the menu
	setLocation((int)(internal.getX()), (int)(internal.getY()));
    }
    
    public byte[] getConflictingMenus() {
	//Closes these menus when this one opens
	return new byte[]{GameConstants.MENU_TRAITS_ID, GameConstants.MENU_ABILITIES_ID};
    }
    
    public void stateChanged(ChangeEvent ce) {
	if (!changeActive)  //Exits method if the sliders are not active
	    return;
	    
	//Updates the color of the selected options
	OptionManager.setPlayerColor(selected, colorSlider[0].getValue() / 100F, colorSlider[1].getValue() / 100F, colorSlider[2].getValue() / 100F);
	if (linkShadows) {  //If the shadows should be updated as well
	    int shade[];
	    switch (selected) {
		case GameConstants.PLAYER_COLOR_SHIRT:  //If the user changed the shirt color
		
		    //Updates the shirt shadow color with a slightly darker than currently used color
		    shade = new int[]{10,9,7};
		    for (int i = 0; i<3; i++) {
			while (colorSlider[i].getValue() - shade[i] < 0)
			    shade[i]--;
		    }
		    OptionManager.setPlayerColor(selected + 1, (colorSlider[0].getValue() - shade[0]) / 100F, (colorSlider[1].getValue() - shade[1]) / 100F, (colorSlider[2].getValue() - shade[2]) / 100F);
		    break;
		case GameConstants.PLAYER_COLOR_SKIN:   //If the user changed the skin color
		
		    //Updates the skin shadow color with a slightly darker than currently used color
		    shade = new int[]{1,10,16};
		    for (int i = 0; i<3; i++) {
			while (colorSlider[i].getValue() - shade[i] < 0)
			    shade[i]--;
		    }
		    OptionManager.setPlayerColor(selected + 1, (colorSlider[0].getValue() - shade[0]) / 100F, (colorSlider[1].getValue() - shade[1]) / 100F, (colorSlider[2].getValue() - shade[2]) / 100F);
		    break;
	    }
	}
	//Reloads the image of the player to the JLabel
	lblPlayerPic.setIcon(loadPlayerPicture());
    }
    
    public void itemStateChanged(ItemEvent ie) {
	if (ie.getSource() == chkLinkShadows) { //If the link shadows button was pressed
	    if (ie.getStateChange() == ItemEvent.SELECTED) {    //If it is now active
		//Disables options to change shirt colors
		itemPiece[GameConstants.PLAYER_COLOR_SHIRT_SHADOW].setEnabled(false);
		itemPiece[GameConstants.PLAYER_COLOR_SKIN_SHADOW].setEnabled(false);
		linkShadows = true;
		int saveSelected = selected;
		
		//Calls events to make the shadows relink to their counterparts
		selected = 0;
		stateChanged(new ChangeEvent(colorSlider[0]));
		selected = 7;
		colorSlider[0].setValue((int)(OptionManager.playerColors[7].getRed() / 255F));
		stateChanged(new ChangeEvent(colorSlider[0]));
		selected = saveSelected;
		lblPlayerPic.setIcon(loadPlayerPicture());  //Reloads the user pictures
	    } else {
		//Enables options to change shadow colors
		itemPiece[GameConstants.PLAYER_COLOR_SHIRT_SHADOW].setEnabled(true);
		itemPiece[GameConstants.PLAYER_COLOR_SKIN_SHADOW].setEnabled(true);
		linkShadows = false;
	    }
	} else if (ie.getSource() == chkBowtie) {   //If the bowtie checkbox was pressed
	    //Sets the bowtie active or inactive and reloads the picture
	    OptionManager.setBowtieActive(ie.getStateChange() == ItemEvent.SELECTED);
	    lblPlayerPic.setIcon(loadPlayerPicture());
	}
    }
    
    public void actionPerformed(ActionEvent ae) {
	super.actionPerformed(ae);

	//Loops through JMenuItems and if any wer pressed, it becomes the active changing option
	for (int i = 0; i<itemPiece.length; i++) {
	    if (ae.getSource() == itemPiece[i]) {
		selected = i;
		menuCustomize.setText("Change " + itemText[i]);
		resetSliders();
		return;
	    }
	}
	
	//Loops through style items
	for (int i = 0; i<itemStyle.length; i++) {
	    if (ae.getSource() == itemStyle[i]) {
		//If one one pressed, the style is loaded and the image relaoded
		OptionManager.setPlayerStyle(i);
		resetSliders();
		lblPlayerPic.setIcon(loadPlayerPicture());
		return;
	    }
	}
    }
    
    public void updateComponents(Engine engine) {}
}
