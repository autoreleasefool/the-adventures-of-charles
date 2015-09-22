package charles.entity.ability;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import charles.entity.Enemy;
import charles.game.Engine;
import charles.resources.Art;

public class EnergyBolt extends Ability {

    int moveTimer;

    public EnergyBolt (Engine engine, int x, int y, int attack, int direction) {
	//Sends these values to the super class
	super(engine, x, y, 128, 64, attack, 1, 1, direction, false, true);
    }
    
    public void render(Graphics g, int bgDrawn) {
	int width = 32, height = 32;
	if (moveTimer > 0) {
	    //If the attack is moving
	    if (targets[0] != null)
		//Draws the moving image to the screen
		g.drawImage(getMovingImage(), x, y, null);
	} else if (damageTimer > 0) {
	    for(int e = 0; e < targets.length; e++) {   //Loops through each target
		if (targets[e] != null) //If the target was not killed
		    //Draws an animation over the enemy
		    g.drawImage(getImage(), targets[e].getX() + targets[e].getWidth() / 2 - width / 2, targets[e].getY() + targets[e].getHeight() / 2 - height / 2, null);
	    }
	}
    }
    
    //Returns image of the ability from spritesheet
    public Image getImage() {
	return (Image) (Art.abilitySheet.getSubimage((damageTimer - 30) / -6 * 32 + 288, 64, 32, 32));
    }
    
    //Returns moving animation of the ability from spritesheet
    private Image getMovingImage() {
	return (Image) (Art.abilitySheet.getSubimage(160 + (frame / 5) * 32 + 64 * direction, 64, 32, 32));
    }
    
    public void tick() {
	if (++frame == 10)      //Adds to its current frame and checks if it is equal to 10
	    frame = 0;          //If so, the frame is reset to 0
	    
	if (moveTimer > 0) {    //If the animation is moving
	    moveTimer--;        //Decreases the timer
	    x += 8 * ((direction == 0) ? 1:-1); //Moves the ability
	    if (targets[0] != null) {   //If the target is still alive
		//If the ability passes the targer
		if ((x + 28 > targets[0].getX() + targets[0].getWidth() / 2 && direction == 0) || (x + 4 < targets[0].getX() + targets[0].getWidth() / 2 && direction == 1) || moveTimer == 0) {
		    damageTimer = 30;   //The damageTimer is set to 1 second
		    moveTimer = 0;      //Move timer is stopped
		    
		    //Calculates position of player relative to enemy
		    int source = (engine.getPlayer().getX() + engine.getPlayer().getWidth() / 2 < targets[0].getX() + targets[0].getWidth() / 2) ? -1: 1;
		    targets[0].takeDamage(attack, source);  //Deals damage to enemy
		}
	    } else
		expire();   //If the enemies are all gone this ability expires
	    return; //Exits the method
	} else if (damageTimer > 0) {   //If the damageTimer is greater than 0
	    damageTimer--;  //Decreases damage timer
	    return;         //Exits method
	} else if (targetFound) {   //If a target was found
	    expire();       //Ability expires
	    return;         //Exits the method
	}
    
	if (time > 0)       //If there is duration remaining
	    time--;         //Decreases time
	else if (targets[0] == null) {  //If there is no target
	    expire();       //The ability expires
	    return;         //Exits the method
	}
   
	Enemy[] enemies = engine.getMap().getEnemies(); //Gets the enemies on the map
	boolean[] enemyHit = new boolean[enemies.length];   //Ensures enemies are not hit twice
	for (int xx = 0; xx<width; xx+=width / 5) {         //Starts in middle of the ability and extends outwards
	    for (int e = 0; e<enemies.length; e++) {        //Loops through each enemy
		if (enemyHit[e] || enemies[e] == null)      //If the enemy is null or was already hit
		    continue;                               //Goes to the next enemy
		    
		//Checks if the ability intersects the enemy
		if (new Rectangle(x + ((direction == 0) ? xx:-xx), y, width / 5, height).intersects(enemies[e].getRectangle())) {
		    y = enemies[e].getY() + enemies[e].getHeight() / 2 - 16;    //Moves the ability to the enemy's y
		    enemyHit[e] = true;         //Sets the enemy to already hit
		    targets[hits] = enemies[e]; //Saves the enemy as a target
		    targetFound = true;         //Sets target found to true
		    moveTimer = 32;             //Sets move timer to 1 second
		
		    if (++hits == maxHits)      //If the number of hits equals the max hits
			return;                 //Method exits
		}
	    }
	}
    }
}
