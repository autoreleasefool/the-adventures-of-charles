package charles.entity;

import java.awt.Image;

import charles.game.GameConstants;
import charles.game.Engine;
import charles.resources.Art;

public class Turtle extends Enemy {

    public Turtle(Engine engine, int x, int y) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 33, 14, GameConstants.ENEMY_TURTLE, 60, 16, 22, 31);
	
	//Disables jumping for this enemy and slows their speed
	this.jumpHeight = 0;
	this.canDrop = false;
	this.canJump = false;
	this.speed = 1;
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dx != 0)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3) + (frame / 5 + 1) * width,damDisplace + 158, width, height));
	else
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3),damDisplace + 158, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 237: return (int)(Math.random() * 8 + 15);
	    default: return 0;
	}
    }
}
