/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 20, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Enemy
 *
 * Purpose:     Defines the values and methods used by enemies in the game
 *
 **/

package charles.entity;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.map.Map;

public abstract class Enemy extends LivingEntity {

    int moveCounter;
    int dyMoveCounter;
    int knockTime;
    boolean jumping;
    boolean moving;
    boolean dyMoving;
    boolean pursuing;
    boolean canDrop = true;
    boolean canJump = true;
    boolean flying;
    boolean knocked;
    boolean frozen;
    int safeBlock;
    
    int id;
    
    int speed = 2;
    int jumpHeight = 8;
    int knockHeight = 5;
    int changeDirectionTimer;
    int changeTime = 15;

    public Enemy(Engine engine, int x, int y, int width, int height, int id, int fullHealth, int deathExperience, int attack, int defense) {
	super(engine, x, y, width, height, fullHealth, 0, deathExperience, 0xffffd800, attack, defense);
	//Calls the super class' constructor with these values
	
	//Stores which type of enemy this is
	this.id = id;
    }
    
    //All classes which extend Enemy must implement this, but they must all also be different
    //The abstract modifier forces the class to override this method
    abstract int randomizeCoinValue(int id);
    
    //TODO change to default accessibility
    public void die() { //Called when the enemy has 0 or less health
	dropItems();            //Randomizes which items to drop
	createParticles();      //Randomizes pixels to bounce from the enemy
	engine.getPlayer().addExperience(deathExperience);  //Adds experience to the player
	engine.getMap().removeEntity(this);                 //Removes this enemy from the map
    }
    
    private void createParticles() {
	//Gets the enemy's image
	BufferedImage bi = (BufferedImage) getImage();
	
	//Loops through the x and y of each pixel
	for (int xx = 0; xx<bi.getWidth(); xx++) {
	    for (int yy = 0; yy<bi.getHeight(); yy++) {
		//If the pixel is not see through and a random number from 0-1 is less than 0.5
		if (bi.getRGB(xx, yy) != 0x00000000 && Math.random() < 0.5)
		    //Creates a pizel to add the to map
		    engine.getMap().addParticle(x + xx, y + yy, (float)(Math.random() * 3 - 2), bi.getRGB(xx, yy), 1);
	    }
	}
    }
    
    int calcDodgeChance(int damage) {
	//TODO
	//System.out.println("calc dodge chances");
	return damage;
    }
    
    public void takeDamage (int damage, int source) {
	super.takeDamage(damage, source);   //Calls the super class' takeDamage method
	
	//If the enemy is still alive, it is set to pursue the player
	if (isAlive())
	    pursuing = true;
    }
    
    public void tick() {
	super.tick();   //Calls the super class' tick
	
	if (frozen) {
	    dx = 0;
	    dy = 0;
	    return;
	}
	
	//If the enemy flies, calls tick_flying and exits this method
	if (flying) {
	    tick_flying();
	    return;
	}

	if (knocked) {                  //If the enemy has been knocked back
	    if (--knockTime <= 0) {     //knockTime is subtracted and if it is less than 0
		knocked = false;        //knocked is false
		dx = 0;                 //Enemy stops moving
	    } else                      //If the enemy is still knocked
		return;                 //Exits the method
	}
	    
	if (pursuing) {                 //If the enemy is pursuing the plaeyer
	    if (--changeDirectionTimer <= 0) {  //Limits how quickly the enemy can change direction, so it is not perfect (reaction time)
		dx = (engine.getPlayer().getX() + engine.getPlayer().getWidth() / 2 < x) ? -speed:speed;    //Sets enemy to move in direction of the player
		if (engine.getPlayer().getY() + engine.getPlayer().getHeight() < y + height)    //If the player is above the enemy, it tries to jump
		    jump();
		
		changeDirectionTimer = changeTime;  //Sets delay before it can change direction again
	    }
	    
	    //If the player is below the enemy, it drops down
	    if (engine.getPlayer().getY() + engine.getPlayer().getHeight() > y + height) {
		dropDown();
	    }
	    
	    //Exits the method
	    return;
	}

	if (moving) {                                           //If the enemy is moving
	    int move = (int)(Math.random() * 20);               // 1/20 chance of enemy stopping every 1/30th of a second
	    moving = move != 0;                                 //Sets the enemy not moving if move == 0
	    if (!moving) {                                      //If the enemy is no longer moving
		moveCounter = (int)(Math.random() * 180);       //Randomizes time until enemy moves again
		dx = 0;                                         //Sets enemy not moving
	    }
	} else {                                                //If the enemy is not moving
	    if (--moveCounter <= 0) {                           //Subtracts one from moveCounter then checks if it is less than 0
		moving = true;                                  //The enemy starts moving
		dx = speed * ((Math.random() < 0.5) ? 1:-1);    //Randomizes direction
	    }
	}
	
	int jump = (int)(Math.random() * 100);                  // Randomizes jump variable between 0 and 99
	if (jump == 0) {                                        //If the jump variable is 0
	    if (Math.random() < 0.5)                            // 50/50 chance
		jump();                                         //Jumps
	    else                                                //or
		dropDown();                                     //Drops down
	}
    }
    
    private void tick_flying() {
	//If the enemy is pursuing the player
	if (pursuing) {
	    if (--changeDirectionTimer <= 0) {  //Limits how quickly the enemy can change direction, so it is not perfect (reaction time)
		dx = (engine.getPlayer().getX() + engine.getPlayer().getWidth() / 2 < x) ? -speed:speed;    //Randomizes if it is moving left or right
		dy = (engine.getPlayer().getY() + engine.getPlayer().getHeight() / 2 < y) ? -speed:speed;   //Randomizes if it is moving up or down

		changeDirectionTimer = changeTime;  //Sets delay before it can change direction again
	    }
	    return; //Exits the method
	}

	if (moving) {                                                   //If the enemy is moving
	    int move = (int)(Math.random() * 20);                       // 1/20 chance of enemy stopping every 1/30th of a second
	    moving = move != 0;                                         //Sets the enemy not moving if move == 0
	    if (!moving) {                                              //If the enemy is no longer moving
		moveCounter = (int)(Math.random() * 180);               //Randomizes time until enemy moves again
		dx = 0;                                                 //Sets enemy not moving left or right
		dy = 0;                                                 //Sets enemy not moving up or down
	    }
	} else {                                                        //If the enemy is not moving
	    if (--moveCounter <= 0) {                                   //Subtracts one from moveCounter then checks if it is less than 0
		moving = true;                                          //The enemy starts moving
		dx = speed * ((Math.random() < 0.5) ? 1:-1);            //Randomizes direction left or right
		dy = speed * ((Math.random() < 0.5) ? 1:-1);            //Randomizes direction up or down
		dy += (int)(speed * Math.random() * ((dy < 0) ? 1:-1)); //Randomly adds or subtracts up to its possible speed to its vertical speed
	    }
	}
    }
    
    public void tryAttack() {
	//If the enemy has not attacked recently and isn't hurt
	if (attackCounter == 0 && damageCounter == 0) {
	    //If the enemy intersects the player
	    if (getRectangle().intersects(engine.getPlayer().getRectangle())) {
		//Checks which direction the enemy will attack from, if its middle is to the left or right of the player's middle
		int source = (engine.getPlayer().getX() + engine.getPlayer().getWidth() / 2 < x + width / 2) ? 1: -1;
		//Sets time until enemy can attack again
		attackCounter = GameConstants.ENTITY_ATTACK_TIME;
		//Takes damage from the player, using the attack with a random increase
		engine.getPlayer().takeDamage(attack + (int)(Math.random() * (attack / 5)), source);
	    }
	}
    }
    
    public void move(Map map) {
	//If the enemy flies, calls move_flying and exits this method
	if (flying) {
	    move_flying(map);
	    return;
	}
    
	//Saves the layout from the map
	int[][] layout = map.getLayout();
    
	//Moves along the x-axis and y-axis
	x += dx;
	y += dy;
	
	//Keeps the enemy within the map's boundaries
	if (x < 0)
	    x = 0;
	else if (x + width > map.getMapWidth())
	    x = map.getMapWidth() - width;
	    
	//Checks if there is solid ground beneath the enemy 
	if ((hitGround(x / MapConstants.TILE_SIZE, (y+height) / MapConstants.TILE_SIZE, layout) || hitGround((x+width-1) / MapConstants.TILE_SIZE, (y+height) / MapConstants.TILE_SIZE, layout)) && dy > 0 && (y+height) / MapConstants.TILE_SIZE != safeBlock) {
	    int hitBlock = (y+height) / MapConstants.TILE_SIZE;     //Saves the block which the enemy hit
	    dy = 0;                                                 //Stops the enemy moving vertically 
	    if (jumping) jumping = false;                           //Stops the enemy from jumping
	    if (knocked) {                                          //If the enemy is being knocked
		knocked = false;                                    //Stops the enemy from being knocked
		dx = 0;                                             //Stops the enemy moving horizontally
		knockTime = 0;                                      //Removes remaining knockTime
	    }
	    while((y+height) / MapConstants.TILE_SIZE == hitBlock)  //While the enemy is still in the block they hit
		y -= 1;                                             //Moves the enemy up
	} else if ((!hitGround(x / MapConstants.TILE_SIZE, (y+height+1) / MapConstants.TILE_SIZE, layout) && !hitGround((x+width-1) / MapConstants.TILE_SIZE, (y+height+1) / MapConstants.TILE_SIZE, layout)) || jumping) {
	    //If the space below the enemy is not a solid blocked
	    dy = Math.min(dy + 1, 10);                          //Increases the falling speed up to a max of 10
	    safeBlock = (y+height) / MapConstants.TILE_SIZE;    //Sets the current row the enemy is in to a "safe area" meaning it will fall through it
		
	    if (!jumping) {                                         //If the enemy is not jumping
		safeBlock = (y+height+1) / MapConstants.TILE_SIZE;  //Sets the space below the enemy to its safe area
		jumping = true;                                     //Sets the enemy jumping (falling)
	    }
	}
	
	if (!knocked) {         //If the enemy is not knocked
	    if (dx > 0)         //If the enemy is moving to the right
		direction = 0;  //Sets the sprite to face right
	    else if (dx < 0)    //If the enemy is moving to the left
		direction = 1;  //Sets the sprite to face left
	}
    }
    
    private void move_flying(Map map) {
	int[][] layout = map.getLayout();   //Gets the layout of the map
    
	x += dx;    //Moves the enemy horizontally
	y += dy;    //Moves the enemy vertically
	
	//Keeps the enemy within the map's boundaries
	if (x < 0)
	    x = 0;
	else if (x + width > map.getMapWidth())
	    x = map.getMapWidth() - width;

	//Keeps the enemy within the map's vertical boundaries
	if (y < 0)
	    y = 0;
	else if (y + height > (map.getLayout().length - 4) * MapConstants.TILE_SIZE - 2)
	    y = (map.getLayout().length - 4) * MapConstants.TILE_SIZE - height - 2;
	
	if (!knocked) {         //If the enemy is not knocked
	    if (dx > 0)         //If the enemy is moving to the right
		direction = 0;  //Sets the sprite to face right
	    else if (dx < 0)    //If the enemy is moving to the left
		direction = 1;  //Sets the sprite to face left
	}
    }
    
    //Used to render the enemy
    public void render(Graphics g) {
	g.drawImage(getImage(), x, y, null);            //Draws the image of the enemy to the provided Graphics Object
	if (pursuing)                                   //If the enemy has been attacked and is following the player
	    g.drawImage(getHPBar(), x, y - 6, null);    //Draws the enemy's hp bar above it
    }
    
    private boolean hitGround (int x, int y, int[][] layout) {
	return Map.tileHasTop(y, x, layout);                    //Checks if the provided block is a solid block
    }
    
    void dropItems() {
	int total = 0;
	for (int i = 0; i<GameConstants.ENEMY_DROPS[id].length; i++) {  //Loops through each item the enemy can drop
	    int chance = (int)(Math.random() * 100);                    //Randomizes a number from 0-99
	    if (chance < GameConstants.ITEM_DROP_CHANCE[id][i]) {       //If the number is less than the provided drop chance of the item
		int coinValue = randomizeCoinValue(GameConstants.ENEMY_DROPS[id][i]);   //Calculates the coin value of the item
		Item it = new Item(engine, x + width / 2 - 16 + total++ * 8, y + width / 2 - 8, GameConstants.ENEMY_DROPS[id][i], coinValue);   //Creates a new Item Object
		engine.getMap().addEntity(it);                          //Adds the item object to the map
		if (GameConstants.ENEMY_DROPS[id][i] >= 237)            //If the item was a coin, only one can be dropped so the method exits
		    return;
	    }
	}
    }
    
    void jump() {                                   //Used to make the enemy jump
	if (!jumping && canJump && !knocked) {      //If the enemy is not already jumping and it can jump and is not knocked
	    jumping = true;                         //The enemy is set to jumping
	    dy = -jumpHeight;                       //The enemy's vertical speed is set to how high it can jump
	}
    }
    
    void dropDown() {                               //Used to drop the enemy down a level
	if ((y+height+1) / MapConstants.TILE_SIZE >= engine.getMap().getLayout().length-4 || !canDrop || knocked)   //If the map ends or the enemy can't drop or it is knocked
	    return;                                                                                                 //The method exits
	
	//Checks if there is a block below the enemy
	if ((hitGround(x / MapConstants.TILE_SIZE, (y+height+1) / MapConstants.TILE_SIZE, engine.getMap().getLayout()) || hitGround((x+width-1) / MapConstants.TILE_SIZE, (y+height+1) / MapConstants.TILE_SIZE, engine.getMap().getLayout())) && dy == 0 && !jumping) {
	    safeBlock = (y+height+1) / MapConstants.TILE_SIZE;  //Sets this block to "safe" so it falls through
	    jumping = true;                                     //Sets the enemy to jumping (falling)
	    dy = 1;                                             //Starts the enemy moving down
	}
    }
    
    public void knockback(int source) { //Used to knockback the enemy after an attack
	knocked = true;                 //Enemy is set to knocked
	knockTime = 15;                 //Enemy is knocked for 1/2 a second
	dx = speed * -source;           //The speed of the enemy is set to it's speed times negative direction,so it moves away from the source
	direction = (dx < 0) ? 0:1;     //The direction of the enemy is set to face the opposite of the direction it is moving
	
	if (canJump) {                  //If the enemy can jump
	    jumping = true;             //It is set to jump
	    dy = -knockHeight;          //It is knocked into the air and does a hop
	}
    }
    
    public void hitByWind() {           //Used when enemy gets hit by wind
	knocked = true;                 //The enemy cannot move
	knockTime = 10000;              //They are knocked until they are freed of the wind
	dx = 0;                         //Stops their movement
    }
    
    public void endWind() {
	knocked = false;                //They are no longer knocked by the wind
	knockTime = 0;                  //Their knockedTime ends
	pursuing = true;                //They begin chasing the player
    }
    
    public void freeze() {
	frozen = true;
    }
    
    public void unfreeze() {
	frozen = false;
    }
}
