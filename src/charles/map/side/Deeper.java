package charles.map.side;

import charles.game.MapConstants;
import charles.game.GameConstants;
import charles.game.Engine;
import charles.entity.*;

public class Deeper extends SideMap {
    
    public Deeper(Engine engine) {
	//Sends these values to the super class
	super("The Deep", MapConstants.DEEPER_LAYOUT[0][0].length, MapConstants.DEEPER_LAYOUT[0].length, MapConstants.DEEPER_LAYOUT, MapConstants.SPAWNABLE_DEEPER, engine);
    }
    
    public String getSignMessage(int y, int x) {
	return null;    //No signs, no messages
    }
    
    public String getDoorMessage(int y, int x) {
	if (x == 0 && y == 12)
	    return "Press ENTER to go to the Wilderness";

	return null;    //No doors, no messages
    }
    
    public void enterDoorway(int x, int y, int width, int height) { //Opens a doorway
	//Checks where the player is
	int x_scaled = x / MapConstants.TILE_SIZE;
	int y_scaled = y / MapConstants.TILE_SIZE;
	int x_width_scaled = (x + width) / MapConstants.TILE_SIZE;
	int y_height_scaled = (y + height) / MapConstants.TILE_SIZE;
	
	//If the player is at the doorway to charles' house
	if ((y_scaled == 12 && x_scaled == 0) || (y_scaled == 6 && x_width_scaled == 0) || (y_height_scaled == 6 && x_scaled == 0) || (y_height_scaled == 6 && x_width_scaled == 0)) {
	    engine.setMap(new Wilderness(engine));    //Sends player into the house
	    engine.getPlayer().setXTile(19, 0); //Sets their position and the direction they are facing
	    engine.getPlayer().setYTile(11, 0);
	    engine.getPlayer().setDirection(1);
	}
    }
    
    public Enemy[] initEnemies() {
	//Sets max number of enemies that can spawn
	return new Enemy[MapConstants.DEEPER_MAX_ENEMIES];
    }
    
    public NPC[] initNPC() {
	return new NPC[]{}; //Sets max number of NPCs that can spawn
    }
    
    protected void generateEnemies() {
	//Generates a bunch of snail enemies
	for (int i = 0; i<MapConstants.DEEPER_MAX_ENEMIES; i++) {
	    spawnEnemy(GameConstants.ENEMY_SNAIL);
	}
    }
}
