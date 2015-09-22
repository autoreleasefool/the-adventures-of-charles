package charles.map.side;

import java.awt.Graphics;

import charles.map.Map;
import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.game.Engine;
import charles.resources.Art;
import charles.entity.*;

public abstract class SideMap extends Map {

    public SideMap(String name, int columns, int rows, int[][][] layout, byte[] spawnableEnemies, Engine engine) {
	//Sends these values to the super class
	super(name, columns, rows, layout, false, false, spawnableEnemies, engine);
	setImage(Art.loadSideMap(columns, rows, layout));   //Loads the map image
    }
    
    protected void spawnEnemy(int index) {
	//Creates a new enemy object
	Enemy en = null;
	
	//Randomizes location, size and color
	int spawnLoc = (int)(Math.random() * spawnableX.size());
	int slimeSize = (int)(Math.random() * 3) + 1;
	int col = (int)(Math.random() * 6);
	
	//Stores the x and y it will spawn at
	int xx = spawnableX.get(spawnLoc).intValue() * MapConstants.TILE_SIZE;
	int yy = spawnableY.get(spawnLoc).intValue() * MapConstants.TILE_SIZE;
	
	//Creates a new enemy depending on the parameter
	switch (index) {
	    case GameConstants.ENEMY_SLUG: en = new Slug(engine, xx, yy + 20); break;
	    case GameConstants.ENEMY_SNAIL: en = new Snail(engine, xx, yy + 19, col); break;
	    case GameConstants.ENEMY_SLIME: en = new Slime(engine, xx, yy + (31 - slimeSize * 14), col, slimeSize); break;
	    case GameConstants.ENEMY_SPIDER: en = new Spider(engine, xx, yy + 18); break;
	    case GameConstants.ENEMY_BEE: en = new Bee(engine, xx, yy); break;
	    case GameConstants.ENEMY_FLY: en = new Fly(engine, xx, yy); break;
	    case GameConstants.ENEMY_ALLIGATOR: en = new Alligator(engine, xx, yy + 9); break;
	    case GameConstants.ENEMY_SNOWMAN: en = new Snowman(engine, xx, yy - 30); break;
	    case GameConstants.ENEMY_TURTLE: en = new Turtle(engine, xx, yy + 17); break;
	    case GameConstants.ENEMY_FOX: en = new Fox(engine, xx, yy - 11); break;
	    case GameConstants.ENEMY_FOX_ARCTIC: en = new ArcticFox(engine, xx, yy - 11); break;
	    case GameConstants.ENEMY_BEAR: en = new Bear(engine, xx, yy - 14); break;
	    case GameConstants.ENEMY_BEAR_POLAR: en = new PolarBear(engine, xx, yy - 14); break;
	    case GameConstants.ENEMY_PIG: en = new Pig(engine, xx, yy + 7); break;
	    case GameConstants.ENEMY_BOAR: en = new Boar(engine, xx, yy + 7); break;
	    case GameConstants.ENEMY_PENGUIN: en = new Penguin(engine, xx, yy + 1); break;
	    case GameConstants.ENEMY_GOBLIN: en = new Goblin(engine, xx, yy - 30); break;
	    case GameConstants.ENEMY_OGRE: en = new Ogre(engine, xx, yy - 43); break;
	    case GameConstants.ENEMY_FAIRY: en = new Fairy(engine, xx, yy - 17); break;
	    case GameConstants.ENEMY_DRAGON: en = new Dragon(engine, xx, yy - 31, col); break;
	    case GameConstants.ENEMY_ROBOT: en = new Robot(engine, xx, yy - 30); break;
	    case GameConstants.ENEMY_ROBOT_WHEELED: en = new WheeledRobot(engine, xx, yy - 30); break;
	    default: throw new RuntimeException(index + "");
	}
	
	addEntity(en);  //Adds the entity to the map
    }
}
