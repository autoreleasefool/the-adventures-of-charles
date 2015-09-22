package charles.entity;

import java.awt.Image;

import charles.game.GameConstants;
import charles.game.Engine;
import charles.resources.Art;

public class Robot extends Enemy {
    public Robot(Engine engine, int x, int y) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 16, 61, GameConstants.ENEMY_ROBOT, 200, 94, 98, 63);
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dy != 0 || jumping)
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 4) + width * 3 + 368, damDisplace + 180, width, height));
	else if (dx != 0)
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 4) + (frame / 5 + 1) * width + 368, damDisplace + 180, width, height));
	else
	    return (Image) (Art.enemySide3.getSubimage(direction * (width * 4) + 368, damDisplace + 180, width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 239: return (int)(Math.random() * 28 + 64);
	    default: return 0;
	}
    }
}
