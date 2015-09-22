/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 8, 2012
 *
 * Application: Tiny Town
 * Class:       GameCanvas
 *
 * Purpose:     The canvas to which all of the graphics are drawn to
 *
 **/

package charles.game;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Robot;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;

import charles.resources.Art;
import charles.resources.FontRenderer;
import charles.menu.CharlesMenuBar;

public class GameCanvas extends Canvas implements Runnable {

    private Thread mainThread;
    private Dimension size = new Dimension (GameConstants.CANVAS_WIDTH, GameConstants.CANVAS_HEIGHT);
    private BufferedImage bi = new BufferedImage(GameConstants.GRAPHICS_WIDTH, GameConstants.GRAPHICS_HEIGHT, BufferedImage.TYPE_INT_RGB);
    
    private CharlesGame cg;
    private CharlesMenuBar cmb;
    private Engine engine;
    public InputHandler input;
    
    private String fpsString = "FPS: 0";
    private boolean showFPS = false;
    private boolean running = false;
    public boolean startTicked;

    public GameCanvas (CharlesGame cg) {
	this.cg = cg;   //Save the Charles Game
	setSize(size);  //Sets size of the canvas
	setBackground(Color.black); //Sets background to black
	setFocusable(true); //Allows canvas to request focus
	
	cmb = new CharlesMenuBar(this); //Creates a new menubar
    } 

    public void start () {
	input = new InputHandler(this); //Creates an inputhandler
	
	engine = new Engine(this);  //Creates a new engine
	engine.init();  //Initializes the engine
	
	//If the thread is not running, starts it
	if (!running || mainThread == null) {
	    running = true;
	    mainThread = new Thread (this);
	    mainThread.start ();
	}
    }
    
    private void tick() {
	input.startTicked = true;   //Used to check if full method was run
    
	input.tick();   //Updates input
	engine.tick(input); //Updates engine
	
	//Takes a screenshot if f2 is pressed
	if (input.keys[GameConstants.KEY_F2].clicked)
	    takeScreenshot();
      
	//If the mouse is clicked and the method completed fully, makes the mouse click false
	if (input.mouseClicked && input.startTicked)
	    input.mouseClicked = false;
	    
	//Requests focus to the canvas if it can
	if (OptionManager.CANVAS_REQUEST_FOCUS) {
	    if (!hasFocus())
		requestFocus();
	}
    }
    
    private void takeScreenshot() {
	//Creates a rectangle of area to screenshot
	Rectangle area = new Rectangle((int)getLocationOnScreen().getX(), (int)getLocationOnScreen().getY(), (int)getWidth(), (int)getHeight());
	
	//Gets current date and time
	DateFormat dateFormat = new SimpleDateFormat("MM_dd_yyyy HH_mm_ss_SS");
	Date date = new Date();
	try {
	    //Creates BufferedImage using Robot class of the rectangle
	    BufferedImage screencap = new Robot().createScreenCapture(area);
	    Graphics g = (Graphics) screencap.getGraphics();    //Gets graphics from image
	    FontRenderer.drawString(dateFormat.format(date).toString(),10,560, 0xffffff00, 4, g);   //Draws date and time to image
	    g.dispose();    //Dispose graphics

	    //Creates a new file for the screenshot
	    File file = new File("C:\\temp\\" + dateFormat.format(date).toString() + ".png");
	    //Stores the image in a png at this file
	    ImageIO.write(screencap, "png", file);
	} catch (Exception e) {e.printStackTrace();}//Prints any errors

	//Adds message of screenshot taken
	engine.addMessage("Screenshot taken!", 60, true);
    }
    
    private void render() {
	//Gets graphics object from main bufferedimage 
	Graphics g = (Graphics) bi.createGraphics();
	
	//Fills it with black rectangle
	g.setColor(Color.black);
	g.fillRect(0, 0, GameConstants.GRAPHICS_WIDTH, GameConstants.GRAPHICS_HEIGHT);

	//Renders the engine
	engine.render(g);
	
	//Draws FPS and ticks per second if option is active
	if (showFPS)
	    FontRenderer.drawString(fpsString, 0, 0, 0xff000000, 1, g);
	
	//Gets graphics from canvas
	Graphics screen = getGraphics();
	//Draws bufferedimage to canvas
	screen.drawImage(bi, 0, 0, (int)(GameConstants.GRAPHICS_WIDTH * GameConstants.CANVAS_SCALE), (int)(GameConstants.GRAPHICS_HEIGHT * GameConstants.CANVAS_SCALE), null);

	//Disposes the graphics objects for memory
	g.dispose();
	screen.dispose();
    }
    
    /*
    HOW IT WORKS:
    This method is what runs the entire game. It is a constantly looping thread which updates the game, then renders the screen.
    First, it calculates how many times per second the game will update. I have set this to 30 times per second, so much of the game
    depends on there being 30 ticks per second. For example, when I say "this will take two seconds" it means 60 ticks.
    The game keeps track of how often it will need to update to reach 30 ticks per second and checks each loop whether that much time
    has passed or not. If it has, the game is ticked
    If that much time has not passed (which is often the case) then the screen is rendered. The game can be rendered upwards of 200 times per second
    on a powerful machine but only 30 frames per second is necessary as otherwise, it is rendering the exact same thing each time without any changes
    
    If the game ticks and too much time has passed since it last ticked, it will tick again and again until it is caught up to 30 times per second
    By this logic, the game will consistently achieve 30 ticks per second but will sacrifice frames per second and so the gameplay will run at a
    consistent speed but the game may not be rendered for the player as quickly.
    */
    public void run() {
	long lastTime = System.currentTimeMillis();     //Saves the start time of this loop
	double unprocessed = 0;                         //Amount of unprocessed seconds
	double msPerTick = 1000.0 / 30;                 //How long a tick will last (ticks per second - 30)
	int frames = 0;                                 //Counter for number of frames each second
	int ticks = 0;                                  //Counter for number of ticks each second
	long lastTimer = System.currentTimeMillis();    //Used to calculate each second

	while (running) {   //While the game is running
	    long now = System.currentTimeMillis();          //Stores the start time of the loop
	    unprocessed += (now - lastTime) / msPerTick;    //Adds the number of milliseconds passed since the start of the last loop
	    lastTime = now;                                 //Updates last time to start of this loop
	    while (unprocessed >= 1) {  //If unprocessed is greater than 1, it is time for another tick to achieve 30 per second
		ticks++;                //Increases the number of ticks
		tick();                 //Ticks
		unprocessed -= 1;       //Subtracts 1 waiting tick from unprocessed. If it is still greater than 0, the game will tick again
	    }

	    try {
		Thread.sleep(2);                    //Tries to sleep for 2 milliseconds to save resources
	    } catch (InterruptedException ie) {
		ie.printStackTrace();               //Prints an error message if it can't
	    }

	    frames++;   //Increases the number of frames this second
	    render();   //Renders the game

	    if (System.currentTimeMillis() - lastTimer > 1000) {        //If 1 second has passed
		lastTimer += 1000;                                      //Adds 1000 to the timer before this if statement runs again
		fpsString = "FPS: " + frames + " Ticks: " + ticks;      //Saves the frames and ticks per second in a strin
		frames = 0;                                             //Clears the frames
		ticks = 0;                                              //Clears the ticks
	    }
	}
    }
    
    public CharlesGame getGame() {
	return cg;                          //Returns the CharlesGame object
    }
    public CharlesMenuBar getMenuBar() {
	return cmb;                         //Returns the CharlesMenuBar object
    }
    public Engine getEngine() {
	return engine;                      //Returns the Engine object
    }
}

