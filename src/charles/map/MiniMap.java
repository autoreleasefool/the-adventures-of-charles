package charles.map;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import charles.game.MapConstants;
import charles.resources.Art;
import charles.game.Engine;
import charles.entity.NPC;
import charles.entity.Enemy;

public class MiniMap {

    private Image base;
    private BufferedImage map;
    private int columns, rows;
    private int[][][] layout;
    private boolean tiled;
    private boolean decor;
    
    private Color playerCol = Color.yellow;
    private Color npcCol = Color.green;
    private Color enemyCol = Color.red;
    
    public MiniMap(int columns, int rows, int[][][] layout, boolean tiled, boolean decor) {
	this.rows = rows;
	this.columns = columns;
	this.layout = layout;
	this.tiled = tiled;
	this.decor = decor;
	
	//Loads the default image of the mibimap
	setBaseImage(Art.loadMiniMap(columns, rows, layout, tiled, decor)); 
	//Creates a new BufferedImage which will show NPC, player, enemies, etc.
	map = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_RGB);
    }

    public void tick(Engine engine) {
	if (base == null)   //If there is no base image yet,the method exits
	    return;
	
	Graphics g = (Graphics) map.getGraphics();  //Gets a graphics object from the map
	
	//Draws the base image to the map
	g.drawImage(base, 0, 0, null);
	
	//Draws the player's pixels on the map
	g.setColor(playerCol);
	g.fillRect(engine.getPlayer().getX() / MapConstants.TILE_SIZE, engine.getPlayer().getY() / MapConstants.TILE_SIZE, 1, (engine.getMap().isTiled()) ? 1:2);
	
	//Loops through the NPCs and draws their pixels on the map
	g.setColor(npcCol);
	NPC[] npc = engine.getMap().getNPC();
	for (int n = 0; n<npc.length; n++) {
	    if (npc[n] != null)
		g.fillRect(npc[n].getX() / MapConstants.TILE_SIZE, npc[n].getY() / MapConstants.TILE_SIZE, 1, 1);
	}
	
	//Loops through the NPCs and draws their pixels on the map
	g.setColor(enemyCol);
	Enemy[] enemies = engine.getMap().getEnemies();
	for (int e = 0; e<enemies.length; e++) {
	    if (enemies[e] != null)
		g.fillRect(enemies[e].getX() / MapConstants.TILE_SIZE, enemies[e].getY() / MapConstants.TILE_SIZE, 1, 1);
	}
	
	//Disposes of the graphis oject
	g.dispose();
    }
    
    public void render(Graphics g) {
	g.drawImage(map, 0, 0, null);   //Draws the map to the graphics object
    }
    
    public Image getMapImage() {    
	return (Image)map;  //Gets the created image
    }
    
    public void setBaseImage(Image base) {
	this.base = base;   //Sets the base image
    }
}
