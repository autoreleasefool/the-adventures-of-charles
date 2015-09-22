/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 24, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Boar
 *
 * Purpose:     Defines the values related to the enemy Boar
 *
 **/

package charles.entity;

import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Boar extends Enemy {
    public Boar(Engine engine, int x, int y) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 48, 24, GameConstants.ENEMY_BOAR, 80, 32, 56, 20);
	
	//Decreases its default speed
	this.speed = 2;
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dx != 0)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3) + (frame / 5 + 1) * width, damDisplace + 234, width, height));
	else
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3), damDisplace + 234, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 238: return (int)(Math.random() * 12 + 30);
	    default: return 0;
	}
    }
}
