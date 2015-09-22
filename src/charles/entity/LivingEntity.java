/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 25, 2012
 *
 * Application: The Adventures of Charles
 * Class:       LivingEntity
 *
 * Purpose:     Defines the values related to Living Entities
 *
 **/
 
package charles.entity;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import charles.game.Engine;
import charles.game.MapConstants;

public abstract class LivingEntity extends Entity {

    int health, fullHealth;
    int mana, fullMana;
    int deathExperience;
    
    int attack, defense;

    int attackCounter = 0;
    int damageCounter = 0;
    int damageCol = 0xffffffff;
    
    public LivingEntity(Engine engine, int x, int y, int width, int height, int fullHealth, int fullMana, int deathExperience, int damageCol, int attack, int defense) {
	super(engine, x, y, width, height); //Sends these values to the super class
	
	//Stores the parameters for the LivingEntity
	this.health = fullHealth;
	this.fullHealth = fullHealth;
	this.mana = mana;
	this.fullMana = fullMana;
	this.deathExperience = deathExperience;
	this.damageCol = damageCol;
	this.attack = attack;
	this.defense = defense;
    }

    //Any class which extends LivingEntity must implement these methods
    public abstract void move(charles.map.Map map);
    public abstract void knockback(int source);
    abstract int calcDodgeChance(int damage);
    abstract void die();
    
    /*These are to provide death, damage and attack sounds but only for entities 
    that have them. Any entities that don't, call these methods which do nothing
    */
    public void playDamageSound() {}
    public void playDeathSound() {}

    public void tick() {
	super.tick();   //Calls the super class' tick method

	if (attackCounter > 0)  //If the attack counter is greater than 0
	    attackCounter--;    //It is decreased by 1
	if (damageCounter > 0)  //If the damage counter is greater than 0
	    damageCounter--;    //It is decreased by 1
    }

    public void takeDamage (int damage, int source) {
	if (damageCounter > 0)  //If the damage counter is greater than 0
	    return;             //The method exits, the attack does nothing
    
	if (source != 2) {      //If the source of the attack is not 2 (poison)
	    damage = Math.max(damage - defense, 1); //The damage is reduced by the entity's defense
	    damage = calcDodgeChance(damage);       //The damage is also possibly reduced by a dodge chance
	}
	    
	playDamageSound();  //Plays the entity's damage sound
	
	//Adds the damage taken to the map, set to the color of the entity's damage (red for player, yellow for enemies)
	engine.getMap().addDamageMessage((Entity)this, ((damage>0) ? damage+"":"Dodged!"), damageCol);
	health = Math.max(health - damage, 0);    //Decreases the entity's health by the damage done
	//TODO

	//If the damage was greater than 0, the damage counter is set to 1 second
	if (damage>0 && source != 2)
	    damageCounter = 30;

	if (health <= 0) {      //If the entity's health is now 0
	    playDeathSound();   //Entity's death sound is played
	    die();              //The entity dies
	}
	
	if (source != 2)        //If the source is not 2 (poison)
	    knockback(source);  //The entity is knocked back
    }
    
    public Image getHPBar() {   //Gets an image of the entity's HP bar
	//Creates a new BufferedImage
	BufferedImage bar = new BufferedImage(width+2, 4, BufferedImage.TYPE_INT_RGB);
	Graphics g = (Graphics) bar.getGraphics();          //Gets a graphics object from the image
	g.setColor(Color.black);                            //Sets the color to black
	g.fillRect(0, 0, bar.getWidth(), bar.getHeight());  //Fills the image with black
	g.setColor(Color.red);                              //Sets the color to red
	g.fillRect(1, 1, width, 2);                         //Fills the bar with red
	g.setColor(Color.green);                            //Sets the color to green
	float hpPer = health / (float)fullHealth;           //Calculates percentage of health remaining
	g.fillRect(1, 1, (int)(width * hpPer), 2);          //Fills percent of the bar with green, to represent remaining health
	g.dispose();                                        //Frees up memory from the graphics object
	return (Image) bar;                                 //Returns the image
    }

    public void gainHealth (int amount) {
	int origHealth = health;                        //Saves the entity's health
	health = Math.min(fullHealth, health + amount); //Adds gained amount to it's health
	if (health - origHealth > 0)                    //If the health was not full when it started
	    engine.getMap().addDamageMessage(this, Integer.toString(health - origHealth), 0xff009600);  //Adds a healing message to the screen (green)
    }
    
    public void gainMana (int amount) {
	int origMana = mana;                            //Saves the entity's mana
	mana = Math.min(fullMana, mana + amount);       //Adds gained amount to it's mana
	if (mana - origMana > 0)                        //If the mana was not full when it started
	    engine.getMap().addDamageMessage(this, Integer.toString(mana - origMana), 0xff1596f3);      //Adds a mana regen message to the screen (blue)
    }
    
    public boolean isAlive() {      //Returns if the entity is alive
	return health > 0;
    }
    public int getHealth() {        //Returns the health of the entity
	return health;
    }
    public int getFullHealth() {    //Returns the full health of the entity
	return fullHealth;
    }
    public int getMana() {          //Returns the mana of the entity
	return mana;
    }
    public int getFullMana() {      //Returns the full mana of the entity
	return fullMana;
    }

    //TODO need to delete these? Are they used?
    
    //Checks for a specific tile
    public boolean checkForTile(int tile, int[][]layout) {
	//Returns if the entity's location is on that tile type
	return (layout[y / MapConstants.TILE_SIZE][x / MapConstants.TILE_SIZE] == tile || layout[(y+height) / MapConstants.TILE_SIZE][x / MapConstants.TILE_SIZE] == tile || layout[y / MapConstants.TILE_SIZE][(x+width-1) / MapConstants.TILE_SIZE] == tile || layout[(y+height) / MapConstants.TILE_SIZE][(x+width-1) / MapConstants.TILE_SIZE] == tile);
    }

    //Checks for a doorway tile
    public boolean checkForDoorway(int[][] layout, boolean decor, boolean tiled) {
	//Gets each tile that can be a door

	if (tiled) {
	int[] doorTiles = (!decor) ? MapConstants.DOOR_TILES : MapConstants.DOOR_TILES_DECOR;
	for (int i = 0; i<doorTiles.length; i++) {
	    //Loops through each type of door tile and returns true
	    if (checkForTile(doorTiles[i], layout))
		return true;
	}
	} else {
	  int[] doorTiles = MapConstants.DOOR_TILES_SIDE;
	for (int i = 0; i<doorTiles.length; i++) {
	    //Loops through each type of door tile and returns true
	    if (checkForTile(doorTiles[i], layout))
		return true;
	}  
	}
	
	
	
	//If none of the tiles are there, returns false
	return false;
    }
}
