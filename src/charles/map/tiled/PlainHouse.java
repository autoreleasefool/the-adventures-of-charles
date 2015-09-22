/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 22, 2012
 *
 * Application: Tiny Town
 * Class:       PlainHouse
 *
 * Purpose:     The city in which the player begins. Specializes methods to be particular to the landscape
 *
 **/
package charles.map.tiled;

import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.entity.Enemy;
import charles.entity.NPC;
import charles.game.Engine;

public class PlainHouse extends TiledMap {
    public PlainHouse(Engine engine, String name) {
	//Sends these values to the superclass
	super(name, MapConstants.PLAIN_HOUSE_LAYOUT[0][0].length, MapConstants.PLAIN_HOUSE_LAYOUT[0].length, MapConstants.PLAIN_HOUSE_LAYOUT, true, engine);
    }
    
    public String getSignMessage(int y, int x) {
	return "y: " + y + " x: " + x;  //No signs, so there must be an error and this prints it
    }
    
    public String getDoorMessage(int y, int x) {
	//Instructs player on how to return to Owl City
	if (y == 9 && (x == 4 || x == 5))
	    return "Press ENTER to go to Owl City";
	else
	    return "y: " + y + " x: " + x;
    }
    
    public void enterDoorway(int x, int y, int width, int height) {
	//Checks what doorway the player is one
	int x_scaled = x / MapConstants.TILE_SIZE;
	int y_scaled = y / MapConstants.TILE_SIZE;
	int x_width_scaled = (x + width) / MapConstants.TILE_SIZE;
	int y_height_scaled = (y + height) / MapConstants.TILE_SIZE;
	
	//Enters the doorway if the player is in the doorway
	if ((y_scaled == 9 && (x_scaled == 4 || x_scaled == 5)) || (y_height_scaled == 9 && (x_scaled == 4 || x_scaled == 5)) || (y_scaled == 9 && (x_width_scaled == 4 || x_scaled == 5)) || (y_height_scaled == 9 && (x_width_scaled == 4 || x_width_scaled == 5))) {
	    engine.setMap(new MainCity(engine));    //Sends player to the Main City
	    engine.getPlayer().setXTile(11, 8);     //Sets their position and the direction they face 
	    engine.getPlayer().setYTile(8, 0);
	    engine.getPlayer().setDirection(0);
	}
    }
    
    public Enemy[] initEnemies() {
	return new Enemy[]{};   //Initializes how many enemies can be spawned
    }
    
    public NPC[] initNPC() {
	return new NPC[]{}; //Initiliased how many NPCs can be spawned
    }
}
