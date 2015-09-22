/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO 
 * Teacher:     Mr. Byers
 * Created:     September 24, 2012
 *
 * Application: Tiny Town
 * Class:       TinyGame
 *
 * Purpose:     Contains the main method and JFrame which will
 *              hold the entirety of the program
 *
 **/
package charles.game;

import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import javax.swing.JFrame;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JLayeredPane;
import javax.swing.JPopupMenu;
import javax.swing.ToolTipManager;
import javax.swing.UIManager;
import javax.swing.JOptionPane;

import charles.menu.CharlesMenuBar;

public class CharlesGame implements ComponentListener {

    GameCanvas gc;
    JFrame f;
    JDesktopPane desktop;
    JInternalFrame internal;
    
    int oldX, oldY;

    public static void main(String[] args) {    //Main method
    
	//Loops until user gets the password right or quits
	String s = "";
	do {
	    s = JOptionPane.showInputDialog(null, "Please enter your authentication key", "Check the back of your manual!", JOptionPane.QUESTION_MESSAGE);
	    if (s == null) System.exit(0);
	} while (!s.equals("1234"));
	javax.swing.JOptionPane.showMessageDialog(null, "The following keys are important! X is your starting attack, Z is to pick up items","IMPORTANT",javax.swing.JOptionPane.WARNING_MESSAGE);
	javax.swing.JOptionPane.showMessageDialog(null, "The red and white menu you will see is your key customization. Choose an event from the dropdown list and click which keys you want to perform this action","IMPORTANT",javax.swing.JOptionPane.WARNING_MESSAGE);  
    
	JPopupMenu.setDefaultLightWeightPopupEnabled(false);    //Sets JMenuBar to appear over canvas
	new CharlesGame();                         //Calls constructor
    }
    
    public CharlesGame() {
	gc = new GameCanvas(this);                                  //Initiates GameCanvas object
	internal = new JInternalFrame("The Adventures of Charles"); //Creates a new JInternalFrame
	desktop = new JDesktopPane();                               //Creates a new desktop
	desktop.setDesktopManager(new FixedDesktopManager(this));   //Sets the DesktopManager of the desktop
	internal.getContentPane().add(gc);                          //Adds the GameCanvas to internal
	internal.addComponentListener(this);                        //Adds this class as component listener to internal
	
	internal.pack();                                        //Fits internal to its contents
	internal.setVisible(true);                              //Sets internal visibility to true
	desktop.add(internal, JLayeredPane.MODAL_LAYER);        //Adds internal to the desktop, to the modal (top) layer
    
	f = new JFrame("Tiny Town");                            //Creates a new JFrame with "Tiny Town" as title
	f.getContentPane().add(desktop);                        //Adds the GameCanvas to the frame
	f.setJMenuBar(gc.getMenuBar());                         //Adds a menu bar to the frame
	f.setResizable(false);                                  //Disables resizing of the frame
	f.setUndecorated(true);                                 //Removes the header and borders from JFrame
	f.setSize(GameConstants.SCREEN_DISPLAY_MODE.getWidth(), GameConstants.SCREEN_DISPLAY_MODE.getHeight()); //Sets the size of the JFrame
	f.setVisible(true);                                     //Shows the JFrame
	internal.setLocation(f.getWidth() / 2 - internal.getWidth() / 2, f.getHeight() / 2 - internal.getHeight() / 2);
	
	oldX = internal.getX();                           //Saves the starting x of the main internal frame
	oldY = internal.getY();                           //Saves the starting y of the main internal frame
	
	gc.start();     //Begins the game
    }
    
    public JFrame getFrame() {
	return f;   //Returns the frame containing the programs
    }
    
    public JDesktopPane getDesktop() {
	return desktop; //Returns the desktop pane containing all the internal frames
    }
    
    public JInternalFrame getInternalFrame() {
	return internal;    //Returns the internal frame
    }
    
    public void componentMoved(ComponentEvent ce) {
	try {
	    gc.getEngine().updateMenuPositions(internal.getX() - oldX, internal.getY() - oldY); //Moved all the frames to stay consistent with the main internal frame
	    oldX = internal.getX(); //Saves new x position of this frame
	    oldY = internal.getY(); //Saves new y position of this frame
	    desktop.repaint();      //Forces desktop pane to redraw
	} catch (Exception e) {}    //Used to prevent crashing on NullPointerException if the menus have not been initialized yet
    }
    public void componentHidden(ComponentEvent ce) {}   //Not used, required by interface
    public void componentResized(ComponentEvent ce) {}  //Not used, required by interface
    public void componentShown(ComponentEvent ce) {}    //Not used, required by interface
}
