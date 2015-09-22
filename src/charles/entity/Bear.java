/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 24, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Bear
 *
 * Purpose:     Defines the values related to the enemy Bear.
 *
 **/

package charles.entity;

import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Bear extends Enemy {
    public Bear(Engine engine, int x, int y) {
	super(engine, x, y, 84, 45, GameConstants.ENEMY_BEAR, 140, 60, 82, 38);
	//Calls the super class' constructor with these values
	
	//Disables jumping for this enemy, slows its moving speed
	this.jumpHeight = 0;
	this.canJump = false;
	this.speed = 1;
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dx != 0)
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 3) + (frame / 5 + 1) * width, damDisplace, width, height));
	else
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 3), damDisplace, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 238: return (int)(Math.random() * 22 + 41);
	    default: return 0;
	}
    }
}
