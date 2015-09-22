/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 24, 2012
 *
 * Application: The Adventures of Charles
 * Class:       ArcticFox
 *
 * Purpose:     Defines the values related to the enemy ArcticFox
 *
 **/

package charles.entity;

import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class ArcticFox extends Enemy {
    public ArcticFox(Engine engine, int x, int y) {
	super(engine, x, y, 54, 42, GameConstants.ENEMY_FOX_ARCTIC, 100, 49, 71, 33);
	//Calls the super class' constructor with these values
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dy != 0 || jumping)
	    return (Image) (Art.enemySide2.getSubimage(direction * (width * 4) + width * 3, damDisplace + 206, width, height));
	else if (dx != 0)
	    return (Image) (Art.enemySide2.getSubimage(direction * (width * 4) + (frame / 5 + 1) * width, damDisplace + 206, width, height));
	else
	    return (Image) (Art.enemySide2.getSubimage(direction * (width * 4), damDisplace + 206, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 238: return (int)(Math.random() * 18 + 34);
	    default: return 0;
	}
    }
}
