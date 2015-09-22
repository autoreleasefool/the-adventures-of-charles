/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 14, 2012
 *
 * Application: Tiny Town
 * Class:       Map
 *
 * Purpose:     Methods and values used to render the map to the screen for entities to interact with
 *
 **/
package charles.map;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.game.Engine;
import charles.resources.Art;
import charles.resources.FontRenderer;
import charles.entity.*;
import charles.entity.ability.*;

public abstract class Map {

    protected BufferedImage[] map = null;
    protected Engine engine;
    protected MiniMap minimap;
    private Enemy[] enemies;
    private NPC[] npc;
    
    private ArrayList<Item> items = new ArrayList<Item>();
    private ArrayList<Ability> abilities = new ArrayList<Ability>();
    
    protected String name;
    protected int rows, columns;
    protected int[][][] layout;
    protected boolean decor;
    protected boolean tiled;
    
    protected ArrayList<Integer> spawnableX = new ArrayList<Integer>(), spawnableY = new ArrayList<Integer>();
    protected byte[] spawnableEnemies = {};
    
    protected ArrayList<Particle> particles = new ArrayList<Particle>();
    
    private BufferedImage imgDamageMessages;
    private boolean messageLoaded = false;
    private String[] damageMessages = new String[GameConstants.MESSAGES_DAMAGE_MAX];
    private float[] damageMessageX = new float[GameConstants.MESSAGES_DAMAGE_MAX];
    private float[] damageMessageY = new float[GameConstants.MESSAGES_DAMAGE_MAX];
    private float[] damageMessageDX = new float[GameConstants.MESSAGES_DAMAGE_MAX];
    private float[] damageMessageDY = new float[GameConstants.MESSAGES_DAMAGE_MAX];
    private float[] damageMessageMaxY = new float[GameConstants.MESSAGES_DAMAGE_MAX];
    private int[] damageMessageCol = new int[GameConstants.MESSAGES_DAMAGE_MAX];
    private int[] damageMessageBounces = new int[GameConstants.MESSAGES_DAMAGE_MAX];
    private int[] damageMessageSafe = new int[GameConstants.MESSAGES_DAMAGE_MAX];
    private float damageMessageGravity = 0.75f;
    private int damageMessagesInQueue = 0;
    
    int waitingToSpawn;
    int spawnTimer;
    boolean canSpawn;
    
    public Map(String name, int columns, int rows, int[][][] layout, boolean tiled, boolean decor, Engine engine) {
	this(name, columns, rows, layout, tiled, decor, new byte[]{}, engine);
    }
    
    public Map(String name, int columns, int rows, int[][][] layout, boolean tiled, boolean decor, byte[] spawnableEnemies, Engine engine) {
	//Saves these values
	this.name = name;
	this.columns = columns;
	this.rows = rows;
	this.layout = layout;
	this.tiled = tiled;
	this.decor = decor;
	this.spawnableEnemies = spawnableEnemies;
	this.engine = engine;
	
	//Adds message of where the user is
	engine.addLocationMessage(name);
	//Creates a mini map
	minimap = new MiniMap(columns, rows, layout, tiled, decor);
	loadSpawnableLocations();   //Loads areas that enemies can spawn
	resetEntities();    //Generates NPCs and enemies
    }
    
    public String getSaveInfo() {
	String s = getName()+System.getProperty("line.separator");
	return s;
    }
    
    //Map must implement these methods
    public abstract String getSignMessage(int y, int x);
    public abstract String getDoorMessage(int y, int x);
    public abstract void enterDoorway(int x, int y, int width, int height);
    public abstract Enemy[] initEnemies();
    public abstract NPC[] initNPC();
    
    //Not all maps generate enemies or NPC so these may be unnecessary and are intentionally blank
    protected void generateEnemies() {}
    protected void generateNPC() {}
    protected void spawnEnemy(int index) {}
    
    protected void spawnEnemy() {
	//Chooses a random spawnable enemy from the map
	int type = (int)(Math.random() * spawnableEnemies.length);
	spawnEnemy(spawnableEnemies[type]); //Spawns the enemy
    }
    
    public void renderBase(Graphics g) {
	//Renders base of the map to the screen
	g.drawImage(getImage(0), 0 - engine.getPlayer().getX() + GameConstants.GRAPHICS_WIDTH / 2 - engine.getPlayer().getWidth() / 2 + engine.getPlayer().getXDrawOffset(), 0 - engine.getPlayer().getY() + GameConstants.GRAPHICS_HEIGHT / 2 - engine.getPlayer().getHeight() / 2 - 1 + engine.getPlayer().getYDrawOffset(), null);
    }
    
    public void renderOverlay(Graphics g) {
	if (tiled)  //Only 2d maps have overlay
	    return;
	    
	//Renders overlay to the screen
	g.drawImage(getImage(1), 0 - engine.getPlayer().getX() + GameConstants.GRAPHICS_WIDTH / 2 - engine.getPlayer().getWidth() / 2 + engine.getPlayer().getXDrawOffset(), 0 - engine.getPlayer().getY() + GameConstants.GRAPHICS_HEIGHT / 2 - engine.getPlayer().getHeight() / 2 - 1 + engine.getPlayer().getYDrawOffset(), null);
    }
    
    public void tick() {
	for (int e = 0; e<enemies.length; e++) {    //Loops through enemies
	    if (enemies[e] == null) //Moves to next enemy if this one is null
		continue;
		
	    //Updates the enemy
	    enemies[e].move(this);
	    enemies[e].tick();
	    enemies[e].tryAttack();
	}
	
	for (int n = 0; n<npc.length; n++) {    //Loops through NPCs
	    if (npc[n] == null) //Moves to next NPC if this is null
		continue;
		
	    //Updates the NpC
	    npc[n].move(this);
	    npc[n].tick();
	}
	
	for (int i = 0; i<items.size(); i++)    //Updates each item
	    items.get(i).tick();
	    
	for (int a = 0; a<abilities.size(); a++)    //Updates each ability
	    abilities.get(a).tick();
	    
	for (int p = 0; p<particles.size(); p++)    //Updates each particle
	    particles.get(p).tick();
	    
	minimap.tick(engine);   //Updates minimap
	
	if (!canSpawn)  //If an enemy can't spawn yet
	    canSpawn = (--spawnTimer <= 0); //Decreases spawn timer
	    
	if (waitingToSpawn > 0) {   //If an enemy is waiting to spawn
	    if (canSpawn) {         //If an enemy can spawn
		waitingToSpawn--;   //Takes one off waiting list0
		spawnEnemy();       //Spawns the enemy
		spawnTimer = MapConstants.TIME_TO_SPAWN;    //Sets time until next enemy can spawn
	    }
	}

	loadDamageMessages();   //Updates the bouncing damage messages
    }
    
    public void renderEntities(Graphics g) {
	//Creates a bufferedimage for all entities and gets graphics object
	BufferedImage entities = new BufferedImage(getImage().getWidth(null), getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
	Graphics g2 = (Graphics) entities.getGraphics();
	
	//Renders items, then enemies, then NPCs
	for (int i = 0; i<items.size(); i++)
	    items.get(i).render(g2);
	
	for (int e = 0; e<enemies.length; e++) {
	    if (enemies[e] == null)
		continue;
	    enemies[e].render(g2);
	}
	
	for (int n = 0; n<npc.length; n++) {
	    if (npc[n] == null)
		continue;
	    npc[n].render(g2);
	}

	//Disposes of graphics and draws BufferedImage to the screen
	g2.dispose();
	g.drawImage(entities, 0 - engine.getPlayer().getX() + GameConstants.GRAPHICS_WIDTH / 2 - engine.getPlayer().getWidth() / 2 + engine.getPlayer().getXDrawOffset(), 0 - engine.getPlayer().getY() + GameConstants.GRAPHICS_HEIGHT / 2 - engine.getPlayer().getHeight() / 2 + engine.getPlayer().getYDrawOffset(), null);
    }
    
    public void renderAbilities(Graphics g) {
	///Creates BufferedIMage to render abilities and gets graphics
	BufferedImage imgAbility = new BufferedImage(getImage().getWidth(null), getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
	Graphics g2 = (Graphics) imgAbility.getGraphics();
	
	int backDrawn = 0;  //Counts how many passive icons have been drawn
	for (int a = 0; a<abilities.size(); a++) {
	    abilities.get(a).render(g2, backDrawn); //Renders the ability
	    if (abilities.get(a).hasBackground())   //Increases count if it has a background
		backDrawn++;
	}
	
	g2.dispose();   //Disposes of the graphics and draws the BufferedImage to the screen
	g.drawImage(imgAbility, 0 - engine.getPlayer().getX() + GameConstants.GRAPHICS_WIDTH / 2 - engine.getPlayer().getWidth() / 2 + engine.getPlayer().getXDrawOffset(), 0 - engine.getPlayer().getY() + GameConstants.GRAPHICS_HEIGHT / 2 - engine.getPlayer().getHeight() / 2 + engine.getPlayer().getYDrawOffset(), null);
    }
    
    private void loadSpawnableLocations() {
	//Loops through each block on map and makes sure it is either not solid or has solid ground beneath it. Then adds to a a list of possible spawn locations 
	for (int y = 0; y<layout[0].length; y++) {
	    for (int x = 0; x<layout[0][0].length; x++) {
		if (tiled) {
		    if (!Map.isTileSolid(y, x, getLayout(), getDecor())) {
			spawnableX.add(new Integer(x));
			spawnableY.add(new Integer(y));
		    }
		} else {
		    if (y < layout[0].length - 1) {
			if (Map.tileHasTop(y + 1, x, getLayout())) {
			    spawnableX.add(new Integer(x));
			    spawnableY.add(new Integer(y));
			}
		    }
		}
	    }
	}
    }
    
    public void renderParticles(Graphics g) {
	//Creates BufferedImage for particles and gets graphics
	BufferedImage imgParticles = new BufferedImage(getImage().getWidth(null), getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
	Graphics g2 = (Graphics) imgParticles.getGraphics();

	//Renders the particles
	for (int p = 0; p<particles.size(); p++)
	    particles.get(p).render(g2);

	    //Diposes of the graphics and draws BufferedImage to the screen
	g2.dispose();
	g.drawImage(imgParticles, 0 - engine.getPlayer().getX() + GameConstants.GRAPHICS_WIDTH / 2 - engine.getPlayer().getWidth() / 2 + engine.getPlayer().getXDrawOffset(), 0 - engine.getPlayer().getY() + GameConstants.GRAPHICS_HEIGHT / 2 - engine.getPlayer().getHeight() / 2 + engine.getPlayer().getYDrawOffset(), null);
    }
    
    public void addEntity(Entity ent) {
	//Checks what kind of entity it is and adds it to corresponding list
	if (ent instanceof Enemy)
	    addEnemy((Enemy)ent);
	else if (ent instanceof NPC)
	    addNPC((NPC)ent);
	else if (ent instanceof Item)
	    addItem((Item)ent);
	else if (ent instanceof Ability)
	    addAbility((Ability) ent);
    }
    
    public void removeEntity(Entity ent) {
	//Checks what kind of entity it is and removes it from corresponding list
	if (ent instanceof Enemy)
	    removeEnemy((Enemy)ent);
	else if (ent instanceof NPC)
	    removeNPC((NPC)ent);
	else if (ent instanceof Item)
	    removeItem((Item)ent);
	else if (ent instanceof Ability)
	    removeAbility((Ability) ent);
    }
    
    public void addParticle(float x, float y, float dx, int col, int size) {    
	//Adds a particle to the map
	Particle p = new Particle(this, x, y, dx, col, size);
	particles.add(p);
    }
    
    public void removeParticle(Particle p) {
	//Removes a particle from the map
	particles.remove(p);
    }
    
    private void addEnemy(Enemy en) {
	//Adds an enemy at a null spot
	for (int e = 0; e<enemies.length; e++) {
	    if (enemies[e] == null) {
		enemies[e] = en;
		return;
	    }
	}
	
	//If this is reached,then no new enemies can spawn
	waitingToSpawn = 0;
    }
    
    private void removeEnemy(Enemy en) {
	//Removes an enemy from the map and increases waiting list for spawn
	for (int e = 0; e<enemies.length; e++) {
	    if (enemies[e] == en) {
		enemies[e] = null;
		waitingToSpawn++;
		return;
	    }
	}
    }
    
    private void addItem(Item it) {
	items.add(it);  //Adds item to map
    }
    
    private void removeItem(Item it) {
	items.remove(it);   //Removes item from map
    }
    
    private void addAbility(Ability ab) {
	abilities.add(ab);  //Adds ability to map
    }
    
    private void removeAbility(Ability ab) {
	abilities.remove(ab);   //Removes ability from map
    }
    
    private void addNPC(NPC n) {
	//Adds NPC to the map
	System.out.println("addNPC - TODO");
	return;
    }
    
    private void removeNPC(NPC n) {
	//Removes NPC from the map
	System.out.println("removeNPC - TODO");
	return;
    }
    
    public void resetEntities() {
	enemies = initEnemies();    //Initialises size of Enemy array
	npc = initNPC();            //Initialises size of NPC array
	generateEnemies();  //Generates enemies
	generateNPC();      //Generates NPCs
    }
    
    public void addDamageMessage(Entity entity, String damage, int col) {
	//Adds a damage message to the screen by giving it random movement and saving position to variables
	Rectangle rect = entity.getRectangle();

	for (int i = 0; i<GameConstants.MESSAGES_DAMAGE_MAX; i++) {
	    if (damageMessages[i] == null) {
		damageMessages[i] = damage;
		damageMessageX[i] = (float)(rect.getX() + rect.getWidth() / 2);
		damageMessageY[i] = (float)(rect.getY() + rect.getHeight() / 4);
		damageMessageDX[i] = (float)(Math.random() * 2 - 1);
		damageMessageDY[i] = (float)(Math.random() * 3);
		damageMessageMaxY[i] = (int)(rect.getY() + rect.getHeight());
		damageMessageBounces[i] = 0;
		damageMessageCol[i] = col;
		damageMessagesInQueue++;
		return;
	    }
	}
	
	//If no spot was empty, a damage message is bumped out
	removeDamageMessage(0);
	addDamageMessage(entity, damage, col);  //Recursion - method is recalled to add
    }
    
    private void removeDamageMessage(int index) {
	//Loops through damage messages and bumps each one up an index
	for (int i = index; i<GameConstants.MESSAGES_DAMAGE_MAX - 2; i++) {
	    damageMessages[i] = damageMessages[i + 1];
	    damageMessageX[i] = damageMessageX[i + 1];
	    damageMessageY[i] = damageMessageY[i + 1];
	    damageMessageDX[i] = damageMessageDX[i + 1];
	    damageMessageDY[i] = damageMessageDY[i + 1];
	    damageMessageMaxY[i] = damageMessageMaxY[i + 1];
	    damageMessageBounces[i] = damageMessageBounces[i + 1];
	    damageMessageCol[i] = damageMessageCol[i + 1];
	    damageMessageSafe[i] = damageMessageSafe[i + 1];
	}
	
	//Empties the final index
	damageMessages[GameConstants.MESSAGES_DAMAGE_MAX - 1] = null;
	damageMessagesInQueue--;
    }
    
    private void loadDamageMessages() {
	if (damageMessagesInQueue > 0) {
	    //Creates BufferediMage for the messages and gets graphics
	    imgDamageMessages = new BufferedImage(getImage().getWidth(null), getImage().getHeight(null), BufferedImage.TYPE_INT_ARGB);
	    Graphics g = (Graphics) imgDamageMessages.createGraphics();
	    
	    for (int i = 0; i<GameConstants.MESSAGES_DAMAGE_MAX - 1; i++) {
		if (damageMessages[i] == null) {    //Exits loop if there are no more damage messages
		    break;
		} else {
		    //Draws the message to the screen
		    FontRenderer.drawString(damageMessages[i], (int)damageMessageX[i], (int)damageMessageY[i], damageMessageCol[i], 1, g);
		    
		    //Updates position of message
		    damageMessageX[i] += damageMessageDX[i];
		    damageMessageY[i] -= damageMessageDY[i];
		    damageMessageDY[i] -= damageMessageGravity;
		    
		    //Keeps it within bounds of map
		    if (damageMessageX[i] + damageMessages[i].length() * 6 - 1 > getMapWidth())
			damageMessageX[i] = getMapWidth() - (damageMessages[i].length() * 6 + 1);
		    else if (damageMessageX[i] < 0)
			damageMessageX[i] = 0;
		    if (damageMessageY[i] < 0)
			damageMessageY[i] = 0;
		    
		    if (tiled) {
			//Checks if the message should bounce, then bounces it if so
			if (damageMessageY[i] >= damageMessageMaxY[i]) {
			    if (damageMessageBounces[i] > 1) {  //Removes the message after two bounces
				removeDamageMessage(i);
				continue;
			    } else {
				damageMessageDY[i] = -damageMessageDY[i] - 2;
				damageMessageBounces[i]++;
			    }
			}
		    } else {
			//Checks if the tile it has entered has a top
			if ((Map.tileHasTop((int)((damageMessageY[i] + 8) / MapConstants.TILE_SIZE), (int)(damageMessageX[i] / MapConstants.TILE_SIZE), getLayout()) || Map.tileHasTop((int)((damageMessageY[i] + 8) / MapConstants.TILE_SIZE), (int)((damageMessageX[i] + damageMessages[i].length() * 6 - 1) / MapConstants.TILE_SIZE), getLayout()) && damageMessageDY[i] > 0 && (int)((damageMessageY[i] + 8) / MapConstants.TILE_SIZE) != damageMessageSafe[i])) {
			    int hitBlock = (int)((damageMessageY[i] + 8) / MapConstants.TILE_SIZE); //Saves the block it has entered
			    while((int)((damageMessageY[i] + 8) / MapConstants.TILE_SIZE) == hitBlock)  //Moves it up until it leaves the solid block
				damageMessageY[i] -= 0.5F;
				
			    //Bounces the message and removes it if necessary
			    if (damageMessageBounces[i] > 1) {  //Removes the message after two bounces
				removeDamageMessage(i);
				continue;
			    } else {
				damageMessageDY[i] = -damageMessageDY[i] - 2;
				damageMessageBounces[i]++;
			    }
			    
			    //If there is an empty block below the message
			} else if (!Map.tileHasTop((int)((damageMessageY[i] + 9) / MapConstants.TILE_SIZE), (int)(damageMessageX[i] / MapConstants.TILE_SIZE), getLayout()) && !Map.tileHasTop((int)((damageMessageY[i] + 9) / MapConstants.TILE_SIZE), (int)((damageMessageX[i] + damageMessages[i].length() * 6 - 1) / MapConstants.TILE_SIZE), getLayout())) {
			    //damageMessageDY[i] -= damageMessageGravity;
			    //Keeps it from going too fast
			    if (damageMessageDY[i] > 10F)
				damageMessageDY[i] = 10F;
			    //Stores current block as safe so it doesn't bounce up through it
			    damageMessageSafe[i] = (int)((damageMessageY[i] + 8) / MapConstants.TILE_SIZE);
			}
		    }
		}
	    }
	    g.dispose();    //Disposes the graphics
	    messageLoaded = true;   //Sets the messages loaded to true so they are drawn
	} else {
	    messageLoaded = false;  //If no messages were drawn, then they don't need to be drawn to the screen
	}
    }
    
    public void renderDamageMessages(Graphics g) { 
	//If there were damage messages, then they are drawn to the screen
	if (messageLoaded)
	    g.drawImage(imgDamageMessages, 0 - engine.getPlayer().getX() + GameConstants.GRAPHICS_WIDTH / 2 - engine.getPlayer().getWidth() / 2 + engine.getPlayer().getXDrawOffset(), 0 - engine.getPlayer().getY() + GameConstants.GRAPHICS_HEIGHT / 2 - engine.getPlayer().getHeight() / 2 + engine.getPlayer().getYDrawOffset(), null);
    }
    
    public Image getImage() {
	return getImage(0); //Gets the base image of the map
    }
    
    public Image getImage(int index) {
	return (Image) map[index];  //Gets requested image of the map - base or overla
    }
    
    public String getName() {
	return name;    //Gets the name of the map
    }
    
    public void setImage(BufferedImage map[]) {
	this.map = map; //Sets the image of the map
    }
    
    public boolean isTiled() {
	return tiled;   //Checks if the map is tiled
    }
    
    public BufferedImage getDamageImage() {
	return imgDamageMessages;   //Gets the damage messages image of the map
    }
    
    public MiniMap getMiniMap() {
	return minimap; //Gets the minimap
    }
    
    public int getTileAt(int x, int y) {
	//Gets the value of the tile at the specified location
	return layout[0][x / MapConstants.TILE_SIZE][y / MapConstants.TILE_SIZE];
    }
    
    public int[][] getLayout() {
	return layout[0];   //Gets the main layout of the map
    }
    
    public int getMapWidth() {
	return layout[0][0].length * MapConstants.TILE_SIZE;    //Gets width of map in pixels
    }
    
    public int getMapHeight() {
	return layout[0].length * MapConstants.TILE_SIZE;   //Gets height of map in pixels
    }
    
    public boolean getDecor() {
	return decor;   //Checks if map is decor
    }
    
    public Enemy[] getEnemies() {
	return enemies; //Gets enemies array of the map
    }
    
    public NPC[] getNPC() {
	return npc;     //Gets NPC array of the map
    }
    
    public Item[] getItems() {
	//Creates an item array from the array list of the map
	Item[] itemArray = new Item[items.size()];
	items.toArray(itemArray);
	return itemArray;
    }
    
    public Ability[] getAbilities() {
	//Creates an ability array from the arraylist of the map
	Ability[] abilityArray = new Ability[abilities.size()];
	abilities.toArray(abilityArray);
	return abilityArray;
    }
    
    public static boolean isTileSolid(int y, int x, int[][] layout, boolean decor) {
	if (decor)
	    return isTileSolid_decor(y, x, layout); //Checks decor tiles instead of terrain
	    
	//Checks if the specified tile in the layout is solid or not
	switch(layout[y][x]) {
	case 5:
	case 25:
	case 45:
	case 15:
	case 35:
	case 55:
	case 4:
	case 24:
	case 44:
	case 14:
	case 34:
	case 60:
	case 61:
	case 62:
	case 63:
	case 64:
	case 65:
	case 66:
	case 67:
	case 68:
	case 69:
	case 80:
	case 81:
	case 82:
	case 84:
	case 85:
	case 86:
	case 87:
	case 89:
	case 100:
	case 101:
	case 102:
	case 104:
	case 105:
	case 106:
	case 107:
	case 109:
	case 120:
	case 121:
	case 122:
	case 124:
	case 125:
	case 126:
	case 127:
	case 129:
	case 140:
	case 141:
	case 142:
	case 144:
	case 160:
	case 161:
	case 162:
	case 164: 
	    //These cannot be moved though
	    return false;
	default:    //Everything else can
	    return true;
	}
    }
    
    public static boolean isTileSolid_decor(int y, int x, int[][] layout) {
	//Checks if the tile can be moved through
	switch(layout[y][x]) {
	case 5:
	case 319:
	case 23:
	case 24:
	case 25:
	case 43:
	case 44:
	case 45:
	    //These cannot be moved through
	    return false;
	default:
	    //Everything else can
	    return true;
	}
    }
    
    public static boolean tileHasTop(int y, int x, int[][] layout) {
	switch (layout[y][x]) {
	    //Checks if the tile has a solid top
	    case 0:
		//These have solid tops
		return true;
	    default:
		//Everything else can be passed through
		return false;
	}
    }
}
