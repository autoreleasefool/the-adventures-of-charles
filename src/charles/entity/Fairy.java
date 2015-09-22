/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 27, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Fairy
 *
 * Purpose:     Defines the values related to the enemy Fairy
 *
 **/

package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Fairy extends Enemy {
    public Fairy(Engine engine, int x, int y) {
	super(engine, x, y, 32, 32, GameConstants.ENEMY_FAIRY, 200, 62, 75, 51);
	//Calls the super class' constructor with these values
	
	//Sets this enemy able to fly and lowers its speed
	this.flying = true;
	this.speed = 2;
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a constantly changing image, to simulate flying/flapping wings
	return (Image) (Art.enemySide2.getSubimage((frame / 5) * width + 288, damDisplace, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 238: return (int)(Math.random() * 16 + 44);
	    default: return 0;
	}
    }
}
