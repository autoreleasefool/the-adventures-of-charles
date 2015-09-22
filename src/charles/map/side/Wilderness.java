package charles.map.side;

import charles.game.MapConstants;
import charles.game.GameConstants;
import charles.game.Engine;
import charles.entity.*;
import charles.map.tiled.MainCity;

public class Wilderness extends SideMap {
    
    public Wilderness(Engine engine) {
	//Sends these values to the super class
	super("The Wilderness", MapConstants.WILDERNESS_LAYOUT[0][0].length, MapConstants.WILDERNESS_LAYOUT[0].length, MapConstants.WILDERNESS_LAYOUT, MapConstants.SPAWNABLE_WILDERNESS, engine);
    }
    
    public String getSignMessage(int y, int x) {
	return null;    //No signs, no messages
    }
    
    public String getDoorMessage(int y, int x) {
	if (x == 0 && y == 6)
	    return "Press ENTER to return to Owl City";
	else if (x == 19 && y == 12)
	    return "Press ENTER to go deeper";
	return null;    //No doors, no messages
    }
    
    public void enterDoorway(int x, int y, int width, int height) { //Opens a doorway
	//Checks where the player is
	int x_scaled = x / MapConstants.TILE_SIZE;
	int y_scaled = y / MapConstants.TILE_SIZE;
	int x_width_scaled = (x + width) / MapConstants.TILE_SIZE;
	int y_height_scaled = (y + height) / MapConstants.TILE_SIZE;
	
	System.out.println(x_scaled);
	System.out.println(y_scaled);
	System.out.println(x_width_scaled);
	System.out.println(y_height_scaled);
	
	//If the player is at the doorway to charles' house
	if ((y_scaled == 6 && x_scaled == 0) || (y_scaled == 6 && x_width_scaled == 0) || (y_height_scaled == 6 && x_scaled == 0) || (y_height_scaled == 6 && x_width_scaled == 0)) {
	    engine.setMap(new MainCity(engine));    //Sends player into the house
	    engine.getPlayer().setXTile(17, 24); //Sets their position and the direction they are facing
	    engine.getPlayer().setYTile(19, 0);
	    engine.getPlayer().setDirection(1);
	} else if ((y_scaled == 12 && x_scaled == 19) || (y_scaled == 12 && x_width_scaled == 19) || (y_height_scaled == 12 && x_scaled == 19) || (y_height_scaled == 12 && x_width_scaled == 19)) {
	    engine.setMap(new Deeper(engine));    //Sends player into the house
	    engine.getPlayer().setXTile(0, 0); //Sets their position and the direction they are facing
	    engine.getPlayer().setYTile(11, 0);
	    engine.getPlayer().setDirection(0);
	}
    }
    
    public Enemy[] initEnemies() {
	//Sets max number of enemies that can spawn
	return new Enemy[MapConstants.WILDERNESS_MAX_ENEMIES];
    }
    
    public NPC[] initNPC() {
	return new NPC[]{}; //Sets max number of NPCs that can spawn
    }
    
    protected void generateEnemies() {
	//Generates a bunch of snail enemies
	for (int i = 0; i<MapConstants.WILDERNESS_MAX_ENEMIES; i++) {
	    spawnEnemy(GameConstants.ENEMY_SLUG);
	}
    }
}
