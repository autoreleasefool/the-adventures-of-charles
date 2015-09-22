package charles.entity;

import java.awt.Image;

import charles.game.GameConstants;
import charles.resources.Art;
import charles.game.Engine;

public class Snowman extends Enemy {
    public Snowman(Engine engine, int x, int y) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 28, 61, GameConstants.ENEMY_SNOWMAN, 100, 40, 50, 26);
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dy != 0 || jumping)
	    return (Image) (Art.enemySide2.getSubimage(direction * (width * 3) + width * 2, damDisplace, width, height));
	else if (dx != 0)
	    return (Image) (Art.enemySide2.getSubimage(direction * (width * 3) + (frame / 5) * width, damDisplace, width, height));
	else
	    return (Image) (Art.enemySide2.getSubimage(direction * (width * 3), damDisplace, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 238: return (int)(Math.random() * 15 + 34);
	    default: return 0;
	}
    }
}
