package charles.game;

import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Color;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JInternalFrame;
import javax.swing.JDesktopPane;
import javax.swing.JPanel;

import charles.resources.Art;

public class KeyConfiguration extends JInternalFrame implements MouseListener, ActionListener, Runnable {
    
    JDesktopPane desktop;
    String options[] = {"Call of the Wind", "Double Swipe", "Healer's Touch", "Magic Armor", "MP Guard", "Energy Bolt", "Shockwave", "Teleport", "Wind Blast", "Earthquake", "Blaze", "Flamethrower", "Revitalize", "Meditate", "Freeze", "Inventory Menu", "Customization Menu", "Abilities Menu", "Traits Menu", "Minimap", "Pick-up Item", "Go Gangnam"};
    JMenuItem[] itemEvents;
    JPanel keyPanel;
    Thread updateThread;
    boolean updating = false;
    
    byte curEvent;
    
    int key[][] = new int[4][10];

    public KeyConfiguration(JDesktopPane desktop) {
	//Creates the JInternal Frame with a menu bar
	
	this.desktop = desktop;
	setTitle("Key Configuration");
	setFocusable(false);
	
	JMenu menuEvents = new JMenu("Select Event");
	
	itemEvents = new JMenuItem[options.length];
	for (int i = 0; i<itemEvents.length; i++) {
	    itemEvents[i] = new JMenuItem("Place " + options[i]);
	    itemEvents[i].addActionListener(this);
	    menuEvents.add(itemEvents[i]);
	}
	
	JMenuBar menuBar = new JMenuBar();
	menuBar.add(menuEvents);
	setJMenuBar(menuBar);
	
	keyPanel = new JPanel();
	keyPanel.setPreferredSize(new Dimension(600,240));
	keyPanel.addMouseListener(this);
	add(keyPanel);

	//Adds the JInternal frame to the desktoppane
	setLocation(0,0);
	pack();
	desktop.add(this);
	setVisible(true);
	
	//Sets current event to be used
	curEvent = 13;
	
	startUpdateThread();    //Starts update thread
    }
    
    private void startUpdateThread() {
	//Creates the update thread
	updating = true;
	updateThread = new Thread(this);
	updateThread.start();
    }
    
    public void run() {
	while(updating) {   //While it should update
	    updateMenu();   //Updates the menu
	    try {
		//Pauses for 100 milliseconds
		Thread.sleep(100);
	    } catch (InterruptedException ie) {
		ie.printStackTrace();
	    }
	}
    }
    
    private void updateMenu() {
	//Creates new BufferedImage
	BufferedImage bi = new BufferedImage(600, 240, BufferedImage.TYPE_INT_RGB);
	Graphics g = (Graphics)bi.getGraphics();    //Gets graphics from bufferedimage
	
	//Fills it with a white rectangle
	g.setColor(Color.white);
	g.fillRect(0, 0, bi.getWidth(), bi.getHeight());
	
	//Loops through position for each key on the keyboard
	for (int x = 0; x< 10; x++) {
	    for (int y = 0; y<4; y++) {
		//Draws black lines around the area
		g.setColor(Color.black);
		g.drawLine((x + 1) * 60, y * 60, (x+1)*60, (y+1)*60);
		g.drawLine(x*60, (y+1)*60,(x+1)*60,(y+1)*60);
		
		//If the key has the same event as the current event
		//it is filled with green. If it has another event, it is filled with red
		//If neither, it is left white
		if (OptionManager.KEY_EVENTS[getKey(x,y)] == curEvent) {
		    g.setColor(Color.green);
		    g.fillRect(x*60,y*60, 60,60);
		} else if(OptionManager.KEY_EVENTS[getKey(x,y)] != -1) {
		    g.setColor(Color.red);
		    g.fillRect(x*60,y*60, 60,60);
		}
		
		//Draws the key in the box
		g.setColor(Color.black);
		g.drawString(getKeyString(x,y), 30 + 60*x,30 + y*60);
	    }
	}
	g.dispose();    //Disposes graphics object to free memory
	Graphics screen = (Graphics)keyPanel.getGraphics(); //Gets graphics from keyPanel
	screen.drawImage(bi, 0, 0, null);   //Draws bufferedimage to keyPanel
	screen.dispose();   //Disposes graphics for memory
    }
    
    private String getKeyString(int x, int y) {
	//Returns the corresponding key
	if (x == 0 && y == 0)
	    return "1";
	else if (x == 1 && y == 0)
	    return "2";
	else if (x == 2 && y == 0)
	    return "3";
	else if (x == 3 && y == 0)
	    return "4";
	else if (x == 4 && y == 0)
	    return "5";
	else if (x == 5 && y == 0)
	    return "6";
	else if (x == 6 && y == 0)
	    return "7";
	else if (x == 7 && y == 0)
	    return "8";
	else if (x == 8 && y == 0)
	    return "9";
	else if (x == 9 && y == 0)
	    return "0";
	else if (x == 0 && y == 1)
	    return "Q";
	else if (x == 1 && y == 1)
	    return "W";
	else if (x == 2 && y == 1)
	    return "E";
	else if (x == 3 && y == 1)
	    return "R";
	else if (x == 4 && y == 1)
	    return "T";
	else if (x == 5 && y == 1)
	    return "Y";
	else if (x == 6 && y == 1)
	    return "U";
	else if (x == 7 && y == 1)
	    return "I";
	else if (x == 8 && y == 1)
	    return "O";
	else if (x == 9 && y == 1)
	    return "P";
	else if (x == 0 && y == 2)
	    return "A";
	else if (x == 1 && y == 2)
	    return "S";
	else if (x == 2 && y == 2)
	    return "D";
	else if (x == 3 && y == 2)
	    return "F";
	else if (x == 4 && y == 2)
	    return "G";
	else if (x == 5 && y == 2)
	    return "H";
	else if (x == 6 && y == 2)
	    return "J";
	else if (x == 7 && y == 2)
	    return "K";
	else if (x == 8 && y == 2)
	    return "L";
	else if (x == 9 && y == 2)
	    return ";";
	else if (x == 0 && y == 3)
	    return "Z";
	else if (x == 1 && y == 3)
	    return "X";
	else if (x == 2 && y == 3)
	    return "C";
	else if (x == 3 && y == 3)
	    return "V";
	else if (x == 4 && y == 3)
	    return "B";
	else if (x == 5 && y == 3)
	    return "N";
	else if (x == 6 && y == 3)
	    return "M";
	else if (x == 7 && y == 3)
	    return ",";
	else if (x == 8 && y == 3)
	    return ".";
	else if (x == 9 && y == 3)
	    return "/";
	else
	    return null;
    }
    
    public void mousePressed(MouseEvent me) {}
    public void mouseClicked(MouseEvent me) {
	for (int y = 0; y<4; y++) {
	    for (int x = 0; x<10; x++) {
		if (me.getX() > x * 60 && me.getX() < (x+1) * 60 && me.getY() > y * 60 && me.getY() < (y+1) * 60) {
		    //Checks which box was clicked and changes the corresponding key's event
		    OptionManager.changeKeyEvent (getKey(x, y), curEvent);
		}
	    }
	}
	
	//updateMenu();
    }
    
    private byte getEvent(int i) {
	//Returns the corresponding event
	switch(i) {
	    case 0: return 13;
	    case 1: return 14;
	    case 2: return 15;
	    case 3: return 16;
	    case 4: return 17;
	    case 5: return 18;
	    case 6: return 19;
	    case 7: return 20;
	    case 8: return 21;
	    case 9: return 22;
	    case 10: return 23;
	    case 11: return 24;
	    case 12: return 25;
	    case 13: return 26;
	    case 14: return 27;
	    case 15: return 5;
	    case 16: return 4;
	    case 17: return 7;
	    case 18: return 2;
	    case 19: return 3;
	    case 20: return 1;
	    default: return 28;
	}
    }
    
    private byte getKey(int x, int y) {
	//Returns the corresponding key
	if (x == 0 && y == 0)
	    return GameConstants.KEY_1;
	else if (x == 1 && y == 0)
	    return GameConstants.KEY_2;
	else if (x == 2 && y == 0)
	    return GameConstants.KEY_3;
	else if (x == 3 && y == 0)
	    return GameConstants.KEY_4;
	else if (x == 4 && y == 0)
	    return GameConstants.KEY_5;
	else if (x == 5 && y == 0)
	    return GameConstants.KEY_6;
	else if (x == 6 && y == 0)
	    return GameConstants.KEY_7;
	else if (x == 7 && y == 0)
	    return GameConstants.KEY_8;
	else if (x == 8 && y == 0)
	    return GameConstants.KEY_9;
	else if (x == 9 && y == 0)
	    return GameConstants.KEY_0;
	else if (x == 0 && y == 1)
	    return GameConstants.KEY_Q;
	else if (x == 1 && y == 1)
	    return GameConstants.KEY_W;
	else if (x == 2 && y == 1)
	    return GameConstants.KEY_E;
	else if (x == 3 && y == 1)
	    return GameConstants.KEY_R;
	else if (x == 4 && y == 1)
	    return GameConstants.KEY_T;
	else if (x == 5 && y == 1)
	    return GameConstants.KEY_Y;
	else if (x == 6 && y == 1)
	    return GameConstants.KEY_U;
	else if (x == 7 && y == 1)
	    return GameConstants.KEY_I;
	else if (x == 8 && y == 1)
	    return GameConstants.KEY_O;
	else if (x == 9 && y == 1)
	    return GameConstants.KEY_P;
	else if (x == 0 && y == 2)
	    return GameConstants.KEY_A;
	else if (x == 1 && y == 2)
	    return GameConstants.KEY_S;
	else if (x == 2 && y == 2)
	    return GameConstants.KEY_D;
	else if (x == 3 && y == 2)
	    return GameConstants.KEY_F;
	else if (x == 4 && y == 2)
	    return GameConstants.KEY_G;
	else if (x == 5 && y == 2)
	    return GameConstants.KEY_H;
	else if (x == 6 && y == 2)
	    return GameConstants.KEY_J;
	else if (x == 7 && y == 2)
	    return GameConstants.KEY_K;
	else if (x == 8 && y == 2)
	    return GameConstants.KEY_L;
	else if (x == 9 && y == 2)
	    return GameConstants.KEY_SEMICOLON;
	else if (x == 0 && y == 3)
	    return GameConstants.KEY_Z;
	else if (x == 1 && y == 3)
	    return GameConstants.KEY_X;
	else if (x == 2 && y == 3)
	    return GameConstants.KEY_C;
	else if (x == 3 && y == 3)
	    return GameConstants.KEY_V;
	else if (x == 4 && y == 3)
	    return GameConstants.KEY_B;
	else if (x == 5 && y == 3)
	    return GameConstants.KEY_N;
	else if (x == 6 && y == 3)
	    return GameConstants.KEY_M;
	else if (x == 7 && y == 3)
	    return GameConstants.KEY_COMMA;
	else if (x == 8 && y == 3)
	    return GameConstants.KEY_PERIOD;
	else if (x == 9 && y == 3)
	    return GameConstants.KEY_SLASH;
	else return -1;
    }
    
    public void mouseReleased(MouseEvent me) {}
    public void mouseEntered(MouseEvent me){}
    public void mouseExited(MouseEvent me) {}
    
    public void actionPerformed(ActionEvent ae) {
	for (int i = 0; i<itemEvents.length; i++) {
	    if(ae.getSource() == itemEvents[i]) {
		//Loads the event from the option selected
		curEvent = getEvent(i);
		return;
	    }
	}
    }
}
