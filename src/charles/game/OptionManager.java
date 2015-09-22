package charles.game;

import java.awt.Color;

import charles.game.GameConstants;
import charles.resources.Art;

public class OptionManager {

    //Options which the entire game can access
    public static boolean QUICK_MENUS = false;
    public static int MENU_MOVE_SPEED = 4;
    public static boolean CANVAS_REQUEST_FOCUS = true;
    
    //Colors of the player's clothing and style
    public static boolean PLAYER_BOWTIE = false;
    public static Color[] playerColors = {  new Color(183, 164, 128),
					    new Color(173, 155, 121),
					    new Color(255, 255, 255),
					    new Color(0, 0, 0),
					    new Color(135, 120, 94),
					    new Color(71, 43, 25),
					    new Color(0, 0, 0),
					    new Color(250, 214, 183),
					    new Color(249, 204, 167),
					    new Color(42, 53, 91)
					};
				
    //Default styles the player can select
    private static final Color[][] playerStyles = { {new Color(183, 164, 128),
						    new Color(173, 155, 121),
						    new Color(255, 255, 255),
						    new Color(0, 0, 0),
						    new Color(135, 120, 94),
						    new Color(71, 43, 25),
						    new Color(0, 0, 0),
						    new Color(250, 214, 183),
						    new Color(249, 204, 167),
						    new Color(42, 53, 91)},
						    
						    {new Color(0, 186, 0),
						    new Color(0, 163, 0),
						    new Color(140, 237, 0),
						    new Color(0, 140, 0),
						    new Color(0, 97, 0),
						    new Color(237, 51, 23),
						    new Color(0, 0, 0),
						    new Color(255, 212, 181),
						    new Color(252, 186, 140),
						    new Color(105, 255, 0)},
						    
						    {new Color(138, 179, 236),
						    new Color(107, 162, 234),
						    new Color(255, 255, 255),
						    new Color(0, 0, 0),
						    new Color(53, 118, 204),
						    new Color(0, 0, 0),
						    new Color(0, 0, 0),
						    new Color(250, 214, 183),
						    new Color(249, 204, 167),
						    new Color(51, 51, 51)}
						};
    
    public static void setQuickMenus(boolean quick) {
	QUICK_MENUS = quick;    //Makes the menus open quickly
    }
    
    public static void setMenuSpeed(int speed) {
	MENU_MOVE_SPEED = speed;    //Makes the menus open faster
    }
    
    public static void setPlayerColor(int index, Color col) {
	//Sets the corresponding article to the color specified
	playerColors[index] = col;
	Art.updatePlayerColors(index);
    }
    
    public static void setPlayerColor(int index, int r, int g, int b) {
	//Overloaded method which creates a Color object
	setPlayerColor(index, new Color(r,g,b));
    }
    
    public static void setPlayerColor(int index, float r, float g, float b) {
	//Overloaded method which creates a color object
	setPlayerColor(index, new Color(r,g,b));
    }
    
    public static void setPlayerStyle(int index) {
	if (index == 2) //If user chose gangnam style
	    OptionManager.setBowtieActive(true);    //Bowtie is added
	    
	//Loops through each article of clothing and changes color
	for (int i = 0; i<playerColors.length;i++)
	    setPlayerColor(i, playerStyles[index][i]);
    }
    
    public static void setBowtieActive(boolean active) {
	//Adds bowtie to player
	PLAYER_BOWTIE = active;
	Art.updatePlayerColors(GameConstants.PLAYER_COLOR_UNDER_SHIRT);
    }
    
    public static void canCanvasRequestFocus(boolean bool) {
	CANVAS_REQUEST_FOCUS = bool;    //Sets whether canvas can request focus
    }
    
    //KEY CONFIGURATION
    
    //Stores the action which each key performs
    public static byte[] KEY_EVENTS = { 0,-1,-1,-1,-1,
					-1,4,-1,-1,-1,
					-1,-1,5,-1,10,
					-1,3,-1,9,-1,
					8,-1,-1,2,-1,
					-1,35,14,-1,1,
					-1,-1,-1,-1,-1,
					-1,-1,-1,-1,-1,
					-1,0,-1,-1,-1,
					-1,-1,11,-1,-1,
					-1,-1,-1,-1,6,
					-1,-1,-1,-1,-1,
					-1,-1,-1,-1,-1,
					-1,-1,-1,-1,-1,12};
    
    public static void changeKeyEvent(byte KEY, byte EVENT) {
	KEY_EVENTS[KEY] = EVENT;    //Changes a key event
    }
}
