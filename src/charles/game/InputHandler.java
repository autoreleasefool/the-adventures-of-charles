/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 8, 2012
 *
 * Application: Tiny Town
 * Class:       InputHandler
 *
 * Purpose:     To handle all input within the program
 *
 **/

package charles.game;

import java.awt.event.KeyListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import charles.entity.Player2D;

public class InputHandler implements KeyListener, MouseListener, MouseMotionListener {

    GameCanvas gc;

    public void tick() {
	for (int k = 0;k<keys.length;k++) { //Loops through each key
	    keys[k].tick();                 //Updates the key
	}
    }

    public Key[] keys = new Key[GameConstants.TOTAL_KEY_CODES];
    
    public InputHandler(GameCanvas gc) {
	gc.addKeyListener(this);            //Adds this class as KeyListener to main canvas
	gc.addMouseListener(this);          //Adds this class as MouseListener to main canvas
	gc.addMouseMotionListener(this);    //Adds this class as MouseMotionListener to main canvas
	this.gc = gc;
    
	for (int k = 0; k<keys.length;k++)  //Initialises array of keys
	    keys[k] = new Key();
    }

    public class Key {
	public boolean down, clicked;
	public int presses, absorbs;
	
	public void toggle(boolean pressed) {
	    if (down!=pressed)      //If the parameter does not equal the class' down boolean
		down = pressed;     //Sets the down boolean to the parameter
	    
	    if (pressed)            //If the parameter is true
		presses++;          //Increases the number of pressed
	}
	
	public void tick() {
	    if (absorbs<presses) {  //If the number of presses is greater than the number of ticks
		absorbs++;          //Increases tick counter
		clicked = true;     //Sets clicked boolean to true
	    } else {                //If tick counter equals presses
		clicked = false;    //Sets clicked boolean to false
	    }
	}
    }
    
    public void keyPressed(KeyEvent ke) {
	toggle(ke, true);   //Calls the toggle method
	ke.consume();       //Stops windows from using the KeyEvent
    }
    public void keyReleased(KeyEvent ke) {
	toggle(ke, false);  //Calls the toggle method
	ke.consume();       //Stops windows from using the KeyEvent
    }
    public void keyTyped(KeyEvent ke) {
	ke.consume();       //Stops windows from using the KeyEvent
    }
    
    private void toggle(KeyEvent ke, boolean pressed) {
	//Checks to see which key was pressed, the calls the key's toggle method
	if (ke.getKeyCode() == KeyEvent.VK_UP) keys[GameConstants.KEY_UP].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_DOWN) keys[GameConstants.KEY_DOWN].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_LEFT) keys[GameConstants.KEY_LEFT].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_RIGHT) keys[GameConstants.KEY_RIGHT].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_A) keys[GameConstants.KEY_A].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_B) keys[GameConstants.KEY_B].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_C) keys[GameConstants.KEY_C].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_D) keys[GameConstants.KEY_D].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_E) keys[GameConstants.KEY_E].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F) keys[GameConstants.KEY_F].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_G) keys[GameConstants.KEY_G].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_H) keys[GameConstants.KEY_H].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_I) keys[GameConstants.KEY_I].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_J) keys[GameConstants.KEY_J].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_K) keys[GameConstants.KEY_K].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_L) keys[GameConstants.KEY_L].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_M) keys[GameConstants.KEY_M].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_N) keys[GameConstants.KEY_N].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_O) keys[GameConstants.KEY_O].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_P) keys[GameConstants.KEY_P].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_Q) keys[GameConstants.KEY_Q].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_R) keys[GameConstants.KEY_R].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_S) keys[GameConstants.KEY_S].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_T) keys[GameConstants.KEY_T].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_U) keys[GameConstants.KEY_U].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_V) keys[GameConstants.KEY_V].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_W) keys[GameConstants.KEY_W].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_X) keys[GameConstants.KEY_X].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_Y) keys[GameConstants.KEY_Y].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_Z) keys[GameConstants.KEY_Z].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_0) keys[GameConstants.KEY_0].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_1) keys[GameConstants.KEY_1].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_2) keys[GameConstants.KEY_2].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_3) keys[GameConstants.KEY_3].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_4) keys[GameConstants.KEY_4].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_5) keys[GameConstants.KEY_5].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_6) keys[GameConstants.KEY_6].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_7) keys[GameConstants.KEY_7].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_8) keys[GameConstants.KEY_8].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_9) keys[GameConstants.KEY_9].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_TAB) keys[GameConstants.KEY_TAB].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_ENTER) keys[GameConstants.KEY_ENTER].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_SPACE) keys[GameConstants.KEY_SPACE].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_ALT) keys[GameConstants.KEY_ALT].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_BACK_QUOTE) keys[GameConstants.KEY_BACK_QUOTE].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_BACK_SLASH) keys[GameConstants.KEY_BACK_SLASH].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_QUOTE) keys[GameConstants.KEY_QUOTE].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_SLASH) keys[GameConstants.KEY_SLASH].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_SEMICOLON) keys[GameConstants.KEY_SEMICOLON].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_PERIOD) keys[GameConstants.KEY_PERIOD].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_COMMA) keys[GameConstants.KEY_COMMA].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_SHIFT) keys[GameConstants.KEY_SHIFT].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_CONTROL) keys[GameConstants.KEY_CONTROL].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_MINUS) keys[GameConstants.KEY_DASH].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_EQUALS) keys[GameConstants.KEY_EQUALS].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_ESCAPE) keys[GameConstants.KEY_ESCAPE].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_CLOSE_BRACKET) keys[GameConstants.KEY_RIGHT_BRACKET].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_OPEN_BRACKET) keys[GameConstants.KEY_LEFT_BRACKET].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F1) keys[GameConstants.KEY_F1].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F2) keys[GameConstants.KEY_F2].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F3) keys[GameConstants.KEY_F3].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F4) keys[GameConstants.KEY_F4].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F5) keys[GameConstants.KEY_F5].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F6) keys[GameConstants.KEY_F6].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F7) keys[GameConstants.KEY_F7].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F8) keys[GameConstants.KEY_F8].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F9) keys[GameConstants.KEY_F9].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F10) keys[GameConstants.KEY_F10].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F11) keys[GameConstants.KEY_F11].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_F12) keys[GameConstants.KEY_F12].toggle(pressed);
	else if (ke.getKeyCode() == KeyEvent.VK_BACK_SPACE) keys[GameConstants.KEY_BACKSPACE].toggle(pressed);
    }

    public int mouseX, mouseY;
    public boolean mousePressed = false;
    public boolean mouseClicked = false;
    public boolean startTicked;

    public void mouseClicked(MouseEvent me) {
	mouseClicked = true;    //The mouse is clicked
	startTicked = false;    //Used to ensure the click goes through the entire tick method
    }
    public void mousePressed(MouseEvent me) {
	mousePressed = true;    //The mouse is pressed
    }
    public void mouseReleased(MouseEvent me) {
	mousePressed = false;   //The mouse is no longer pressed
    }
    
    public void mouseMoved(MouseEvent me) {
	mouseX = (int)(me.getX() / GameConstants.CANVAS_SCALE); //Stores the mouseX relevant to the canvas
	mouseY = (int)(me.getY() / GameConstants.CANVAS_SCALE); //Stores the mouseY relevant to the canvas
   }
    public void mouseDragged(MouseEvent me) {
	mouseX = (int)(me.getX() / GameConstants.CANVAS_SCALE); //Stores the mouseX relevant to the canvas
	mouseY = (int)(me.getY() / GameConstants.CANVAS_SCALE); //Stores the mouseY relevant to the canvas
    }
    
    //Not used, required by interface
    public void mouseEntered(MouseEvent me) {}
    public void mouseExited(MouseEvent me) {}
}
