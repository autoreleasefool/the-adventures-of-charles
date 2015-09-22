/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 15, 2012
 *
 * Application: Tiny Town
 * Class:       MainCity
 *
 * Purpose:     The city in which the player begins. Specializes methods to be particular to the landscape
 *
 **/
package charles.map.tiled;

import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.game.Engine;
import charles.entity.Enemy;
import charles.entity.NPC;
import charles.map.side.*;

public class MainCity extends TiledMap {

    public MainCity(Engine engine) {
	//Sends these values to the super class
	super("Owl City", MapConstants.MAIN_CITY_LAYOUT[0][0].length, MapConstants.MAIN_CITY_LAYOUT[0].length, MapConstants.MAIN_CITY_LAYOUT, false, engine);
    }
    
    public String getSignMessage(int y, int x) {
	//Shows a sign's message if a player gets close
	if (y == 9 && x == 12)
	    return "Charles' House";
	else
	    return "y: " + y + " x: " + x;
    }
    
    public String getDoorMessage(int y, int x) {
	//Prompts player to enter houses
	if (y == 8 && x == 11)
	    return "Press ENTER to open Charles' House";
	else if (y == 12 && x == 16)
	    return "Sorry! Gary's house is locked!";
	else if (y == 12 && x == 21)
	    return "Sorry! Ash's house is locked";
	else if (y == 19 && (x == 17 || x == 18))
	    return "Press ENTER to go to the Wilderness";
	else
	    return "y: " + y + " x: " + x;
    }
    
    public void enterDoorway(int x, int y, int width, int height) {
	//Checks where the player is
	int x_scaled = x / MapConstants.TILE_SIZE;
	int y_scaled = y / MapConstants.TILE_SIZE;
	int x_width_scaled = (x + width) / MapConstants.TILE_SIZE;
	int y_height_scaled = (y + height) / MapConstants.TILE_SIZE;
	
	//If the player is at the doorway to charles' house
	if ((y_scaled == 8 && x_scaled == 11) || (y_scaled == 8 && x_width_scaled == 11) || (y_height_scaled == 8 && x_scaled == 11) || (y_height_scaled == 8 && x_width_scaled == 11)) {
	    engine.setMap(new PlainHouse(engine, "Charles' House"));    //Sends player into the house
	    engine.getPlayer().setXTile(4, 24); //Sets their position and the direction they are facing
	    engine.getPlayer().setYTile(9, 0);
	    engine.getPlayer().setDirection(3);
	    
	//If the player is a the doorway to the wilderness
	} else if ((y_scaled == 19 && x_scaled == 17) || (y_scaled == 19 && x_width_scaled == 17) || (y_height_scaled == 19 && x_scaled == 17) || (y_height_scaled == 19 && x_width_scaled == 17) || (y_scaled == 19 && x_scaled == 18) || (y_scaled == 19 && x_width_scaled == 18) || (y_height_scaled == 19 && x_scaled == 18) || (y_height_scaled == 19 && x_width_scaled == 18)) {
	    engine.setMap(new Wilderness(engine));  //Sends player into the wilderness
	    engine.getPlayer().setXTile(0, 0);      //Sets their position and the direction they are facing
	    engine.getPlayer().setYTile(5, 0);
	}
    }
    
    public Enemy[] initEnemies() {
	return new Enemy[]{};       //Initializes how many enemies can spawn
    }
    
    public NPC[] initNPC() {
	return new NPC[]{}; //Initializes how many NPCs can spawn
    }
}
