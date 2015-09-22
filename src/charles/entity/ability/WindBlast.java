package charles.entity.ability;

import java.awt.Graphics;
import java.awt.Image;

import charles.entity.Enemy;
import charles.game.Engine;
import charles.resources.Art;

public class WindBlast extends Ability {

    int drawX, drawY;

    public WindBlast(Engine engine, int x, int y) {
	super(engine, x, y, 192, 64, 0, 30, engine.getMap().getEnemies().length, 0, false, true);
	drawX = engine.getPlayer().getX() - 24; //Sets the x this animation is drawn at
	drawY = engine.getPlayer().getY();      //Sets the y this animation is drawn at
    }
    
    public void tick() {
	if (++frame == 10)      //Adds to its current frame and checks if it is equal to 10
	    frame = 0;          //If so, the frame is reset to 0
	    
	if (time > 0)           //If the duration is greater than 0
	    time--;             //Decreases the remaining time
	else {
	    //Loops throguh each target
	    //If the target is not null, they are stopped from being blown
	    for (int e = 0; e<targets.length; e++) {
		if (targets[e] != null)
		    targets[e].endWind();
	    }
	    
	    //This ability expires
	    expire();
	}
	    
	//Gets the drawX and drawY from the player's positions
	drawX = engine.getPlayer().getX() - 24;
	drawY = engine.getPlayer().getY();
	    
	if (targetFound) {  //If a targert was found
	    for (int e = 0; e<targets.length; e++) {    //Loops through each target
		if (targets[e] != null) {   //If the target is not null
		    
		    //Pushes the enemy away from the player
		    if (targets[e].getX() + targets[e].getWidth() / 2 > x + width / 2)
			targets[e].setX(targets[e].getX() + 12);
		    else
			targets[e].setX(targets[e].getX() - 12);
			
		    //If the ability no longer interects the enemy, they are no longer pushed
		    if (!intersects(targets[e])) {
			targets[e].endWind();
			targets[e] = null;
		    }
		}
	    }   
	    return;
	} else
	    //If no target was foundm this is made true
	    targetFound = true;
	    
	//Loops through enemies on the map
	Enemy[] enemies = engine.getMap().getEnemies();
	for (int e = 0; e<enemies.length; e++) {
	    if (enemies[e] == null) //If the enemy is null
		continue;           //Goes to the next enemy
	    if (intersects(enemies[e])) {   //If it intersects the enemy
		targets[hits++] = enemies[e];   //This enemy is saved as a target
		enemies[e].hitByWind();         //The enemy is hit by the wind (immobilized)
	    }
	}
    }
    
    public void render(Graphics g, int bgDrawn) {
	if (time > 0)   //If the time is greater than 0
	    g.drawImage(getImage(), drawX, drawY, null);    //Draws the image to the screen
    }
    
    //Gets the image of the ability from a spritesheet
    public Image getImage() {
	return (Image)(Art.abilitySheet.getSubimage((time - 30) / -6 * 64, 96, 64, 64));
    }
}
