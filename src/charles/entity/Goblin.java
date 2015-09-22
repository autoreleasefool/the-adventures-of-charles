/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 24, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Goblin
 *
 * Purpose:     Defines the values related to the enemy Goblin
 *
 **/

package charles.entity;

import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Goblin extends Enemy {
    public Goblin(Engine engine, int x, int y) {
	super(engine, x, y, 16, 61, GameConstants.ENEMY_GOBLIN, 260, 112, 115, 54);
	//Calls the super class' constructor with these values
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dy != 0 || jumping)
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 4) + width * 3, damDisplace + 180, width, height));
	else if (dx != 0)
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 4) + (frame / 5 + 1) * width, damDisplace + 180, width, height));
	else
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 4), damDisplace + 180, width, height));
    }
    
    int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 239: return (int)(Math.random() * 32 + 72);
	    default: return 0;
	}
    }
}
