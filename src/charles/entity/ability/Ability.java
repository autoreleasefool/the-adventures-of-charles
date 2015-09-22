package charles.entity.ability;

import java.awt.Rectangle;

import charles.game.Engine;
import charles.entity.Entity;
import charles.entity.Enemy;

public abstract class Ability extends Entity {

    int time;
    int animFrame;
    int maxHits;
    int attack;
    int direction;
    int hits;
    int damageTimer;
    boolean area;
    boolean damaging;
    boolean targetFound;
    boolean freeze;
    
    Enemy[] targets = null;

    public Ability(Engine engine, int x, int y, int width, int height, int attack, int time, int maxHits, int direction, boolean area, boolean damaging) {
	super(engine, x, y, width, height); //Sends these values to the super class
	this.time = time;               //Saves duration of the ability
	this.attack = attack;           //Saves strength of the ability
	this.maxHits = maxHits;         //Saves max targets of the ability
	this.direction = direction;     //Sets direction of the ability
	this.area = area;               //Saves whether the ability does area damage or not
	this.damaging = damaging;       //Saves whether the ability does damage or not
	
	if (damaging)                       //If the ability does damage
	    targets = new Enemy[maxHits];   //Initialises the targets array
    }
    
    public void render(java.awt.Graphics g) {throw new RuntimeException("Cannot render ability");}  //Abilities are not rendered with this method
    public abstract void render(java.awt.Graphics g, int bgDrawn);  //They are rendered with an overloaded version
    
    public boolean hasBackground() {
	return !damaging;   //Returns whether or not the ability has an icon
    }
    
    public void tick() {
	super.tick();   //Calls the super method's tick
	
	if (damageTimer > 0) {  //If the damage timer is greater than 0
	    damageTimer--;      //Subtracts from the damage timer
	    return;             //Exits the method
	} else if (targetFound) {   //If the damage timer hits 0 and there was a target
	    expire();       //This ability is terminated
	    return;         //Method exits
	}
    
	if (time > 0)   //If the duration is greater than 0
	    time--;     //Subtracts from the time
	else if (!damaging) {   //If the ability is non-damaging
	    expire();   //This ability expires
	    return;     //Exits method
	} else {
	    //Checks to see if the attack has any targets
	    boolean noTarget = true;
	    for (int i = 0; i<targets.length; i++) {
		if (targets[i] != null)
		    noTarget = false;
	    }
	    
	    if (noTarget) { //If there is no target for the attack still alive
		expire();   //This ability expires
		return;     //Method exits
	    }
	}
	    
	if (!damaging)  //If the attack doesn't do damage, the method exits
	    return;
	
	if (area) { //If the attack does area damage, calls tick_area() and exits this method
	    tick_area();
	    return;
	}
   
	Enemy[] enemies = engine.getMap().getEnemies(); //Gets the enemies from the map
	boolean[] enemyHit = new boolean[enemies.length];   //Boolean for if an enemy has already been hit
	for (int xx = 0; xx<width; xx+=width / 5) { //Starts integer at 0 and goes through the width of the attack
	    for (int e = 0; e<enemies.length; e++) {    //Loops through each enemy
		if (enemyHit[e] || enemies[e] == null)  //If the enemy is null or has already been attacked
		    continue;                           //Goes to the next enemy
		    
		//Checks if this section of the ability intersects the enemy (does this in sections so the ability's range starts close and extends outwards
		if (new Rectangle(x + ((direction == 0) ? xx:-xx), y, width / 5, height).intersects(enemies[e].getRectangle())) {
		    targets[hits] = enemies[e];                 //Saves this enemy as a target for animation
		    int source = (engine.getPlayer().getX() + engine.getPlayer().getWidth() / 2 < enemies[e].getX() + enemies[e].getWidth() / 2) ? -1: 1;   //Calculate where the player is in relation to the enemy
		    enemies[e].takeDamage(attack, source);      //Does damage to the enemy
		    enemyHit[e] = true;                         //This enemy cannot be hit again
		    targetFound = true;                         //Sets target found to true
		    damageTimer = 30;                           //Sets timer for animation to 1 second
		
		    if (++hits == maxHits)                      //Increases the hits of the attack and if the limit was reached
			return;                                 //Method exits
		}
	    }
	}
    }
    
    private void tick_area() {
	Enemy[] enemies = engine.getMap().getEnemies(); //Gets the enemies on the map
	boolean[] enemyHit = new boolean[enemies.length];   //Ensures enemies are not hit twice
	for (int xx = 0; xx < width / 2; xx += width / 6) { //Starts in middle of the ability and extends outwards
	    for (int e = 0; e<enemies.length; e++) {        //Loops through each enemy
		if (enemies[e] == null || enemyHit[e])      //If the enemy is null or was already hit
		    continue;                               //Goes to the next enemy
		    
		//Checks if the ability intersects the enemy
		if ((new Rectangle(x + width / 2 + xx, y, width / 6, height).intersects(enemies[e].getRectangle()) || new Rectangle(x + width / 2 - xx - width / 6, y, width / 6, height).intersects(enemies[e].getRectangle())) && !enemyHit[e]) {
		    targets[hits] = enemies[e]; //Saves this enemy as a target for animation
		    if (freeze) //If it is the freeze ability
			enemies[e].freeze();    //Freezes the enemy
		    else {
			//Calculate where the player is in relation to the enemy
			int source = (engine.getPlayer().getX() + engine.getPlayer().getWidth() / 2 < enemies[e].getX() + enemies[e].getWidth() / 2) ? -1: 1;
			enemies[e].takeDamage(attack, source);  //Does damage to the enemy
		    }
		    enemyHit[e] = true; //This enemy cannot be hit again
		    damageTimer = 30;   //Sets timer for animation to 1 second
		    targetFound = true; //Sets target found to true
		
		    if (++hits >= maxHits)  //Increases the hits of the attack and if the limit was reached
			return;             //Method exits
		}
	    }
	}
    }
    
    //Removes this ability from the map
    void expire() {
	engine.getMap().removeEntity(this);
    }
}
