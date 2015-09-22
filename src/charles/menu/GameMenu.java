package charles.menu;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;

import charles.game.OptionManager;
import charles.game.Engine;

public abstract class GameMenu extends JInternalFrame implements ActionListener, Runnable {

    JDesktopPane desktop;
    JInternalFrame internal;
    int side;
    Thread moveThread;
    boolean opening;
    
    JPanel lowerPanel;
    JButton btnMinimize;

    public GameMenu(JDesktopPane desktop, JInternalFrame internal, String name, int side) {
	//Stores these parameters and setups the frame
	this.desktop = desktop;
	this.internal = internal;
	this.side = side;
	setTitle(name);
	setFocusable(false);
	
	//Creates a JPanel with a button that will minimize the menu
	lowerPanel = new JPanel(new BorderLayout());
	switch (side) {
	    case 0:
		btnMinimize = new JButton(">>>");
		lowerPanel.add(btnMinimize, BorderLayout.LINE_END);
		break;
	    case 1:
		btnMinimize = new JButton("^^^");
		lowerPanel.add(btnMinimize, BorderLayout.LINE_END);
		break;
	    case 2:
		btnMinimize = new JButton("<<<");
		lowerPanel.add(btnMinimize, BorderLayout.LINE_START);
		break;
	    default: throw new RuntimeException();
	}
	//Adds this class as an actionlistener to the button
	btnMinimize.addActionListener(this);
	
	//Adds the panel to the meny
	getContentPane().add(lowerPanel, BorderLayout.SOUTH);
    }
    
    protected void addToDesktop() {
	desktop.add(this);  //Adds this menu to the desktoppane
    }
    
    public void openDialog() {
	if (moveThread == null) {   //If the menu is not moving
	    if (!isVisible())       //Resets the location
		resetLocation();
	    setVisible(true);       //Makes it visible
	    opening = true;         //Starts the opening thread
	    moveThread = new Thread(this);
	    moveThread.start();
	} else {                    //If the menu is moving
	    moveThread.stop();      //Stops the thread from moving
	    moveThread = null;      //Nulls the thread
	    openDialog();           //Recursion - attempts to open menu again
	}
    }
    
    public void closeDialog() {
	if (moveThread == null) {   //If the menu is no moving
	    opening = false;        //Starts the closing thread
	    moveThread = new Thread(this);
	    moveThread.start();
	} else {                    //If the menu is moving
	    moveThread.stop();      //Stops the movement
	    moveThread = null;      //Nulls the thread
	    closeDialog();          //Recursion - attempts to close menu again
	}
    }
    
    public void quickOpen() {
	//Sets the position of the frame to its extended position and displays it
	setVisible(true);
	switch (side) {
	    case 0: setLocation(internal.getX() - getWidth(), getY()); break;
	    case 1: setLocation((int)(getLocationOnScreen().getX()), internal.getY() + internal.getHeight()); break;
	    case 2: setLocation(internal.getX() + internal.getWidth(), getY()); break;
	    default: throw new RuntimeException();
	}
    }
    
    public void quickClose() {
	setVisible(false);  //Hides the frame
    }
    
    public void shift(int xShift, int yShift) {
	//Moves the frame by the shift amount
	setLocation(getX() + xShift, getY() + yShift);
    }
    
    public void run() {
	//Moves menu at set speed
	int move = OptionManager.MENU_MOVE_SPEED;
	
	if (!opening) {
	    //Moves the menu left or right to hide it
	    while ((side == 0 && getX() < internal.getX()) || (side == 1 && getY() > internal.getY() + internal.getHeight()) || (side == 2 && getX() + getWidth() > internal.getX() + internal.getWidth())) {
		switch (side) {
		    case 0: setLocation(getX() + move, getY()); break;
		    case 1: setLocation(getX(), getY() - move); break;
		    case 2: setLocation(getX() - move, getY()); break;
		    default: throw new RuntimeException();
		}
		//Pauses for 5 milliseconds to slow its speed
		try {
		    Thread.sleep(5);
		} catch (InterruptedException ie) {
		    ie.printStackTrace();
		}
	    } 
	    //Hides the frame when it is done moving
	    setVisible(false);
	} else {
	    //Moves the menu left or right to show it
	    while ((side == 0 && getX() + getWidth() > internal.getX()) || (side == 1 && getY() < internal.getY() + internal.getHeight()) || (side == 2 && getX() < internal.getX() + internal.getWidth())) {
		switch (side) {
		    case 0: setLocation(getX() - move, getY()); break;
		    case 1: setLocation(getX(), getY() + move); break;
		    case 2: setLocation(getX() + move, getY()); break;
		    default: throw new RuntimeException();
		}
		//Pauses for 5 milliseconds to slow its speed
		try {
		    Thread.sleep(5);
		} catch (InterruptedException ie) {
		    ie.printStackTrace();
		}
	    }
	}
    }

    //Menus must implement these methods
    public abstract void resetLocation();
    public abstract void updateComponents(Engine engine);
    public abstract byte[] getConflictingMenus();
    
    public void actionPerformed(ActionEvent ae) {   
	//If the minimize button was pressed, the menu is closed
	if (ae.getSource() == btnMinimize) {
	    if (OptionManager.QUICK_MENUS)
		quickClose();
	    else
		closeDialog();
	}
    }
}
