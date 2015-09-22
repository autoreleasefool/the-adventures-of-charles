/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 21, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Bee
 *
 * Purpose:     Defines the values related to the enemy Bee
 *
 **/

package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Bee extends Enemy {
    public Bee(Engine engine, int x, int y) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 13, 5, GameConstants.ENEMY_BEE, 30, 8, 24, 4);
	
	//Enables flying for this enemy, so it can roam the map, increases its speed
	this.flying = true;
	this.speed = 4;
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a constantly changing image, to simulate flying/flapping wings
	return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + (frame / 5) * width, damDisplace + 104, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 237: return (int)(Math.random() * 5 + 10);
	    default: return 0;
	}
    }
}
