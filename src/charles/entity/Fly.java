/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 27, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Fly
 *
 * Purpose:     Defines the values related to the enemy Fly
 *
 **/

package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Fly extends Enemy {
    public Fly(Engine engine, int x, int y) {
	super(engine, x, y, 11, 8, GameConstants.ENEMY_FLY, 10, 2, 8, 0);
	//Calls the super class' constructor with these values
	
	//Sets this enemy able to fly and lowers its speed
	this.flying = true;
	this.speed = 2;
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a constantly changing image, to simulate flying/flapping wings
	return (Image) (Art.enemySide.getSubimage((frame / 5) * width + 256, damDisplace + 56, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 237: return (int)(Math.random() * 5 + 5);
	    default: return 0;
	}
    }
}
