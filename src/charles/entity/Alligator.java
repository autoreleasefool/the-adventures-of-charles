/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 24, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Alligator
 *
 * Purpose:     Defines the values related to the enemy Alligator
 *
 **/

package charles.entity;

import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Alligator extends Enemy {

    public Alligator(Engine engine, int x, int y) {
	super(engine, x, y, 48, 22, GameConstants.ENEMY_ALLIGATOR, 100, 83, 122, 40);
	//Calls the super class' constructor with these values
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dy!= 0 || dx == 0 || jumping)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3),damDisplace + 114, width, height));
	else
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3) + (frame / 5 + 1) * width,damDisplace + 114, width, height));
    }
    
    int randomizeCoinValue(int id) { 
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 238: return (int)(Math.random() * 20 + 48);
	    default: return 0;
	}
    }
}
