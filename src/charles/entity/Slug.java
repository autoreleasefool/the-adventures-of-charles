package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.GameConstants;
import charles.game.Engine;
import charles.resources.Art;

public class Slug extends Enemy {

    public Slug(Engine engine, int x, int y) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 15, 11, GameConstants.ENEMY_SLUG, 15, 2, 12, 0);
	
	//Disables this enemy from jumping and slows their speed
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
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + (frame / 5) * width + 128,damDisplace + 80, width, height));
	else
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + 128,damDisplace + 80, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 237: return (int)(Math.random() * 5 + 5);
	    default: return 0;
	}
    }
}
