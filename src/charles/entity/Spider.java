package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.GameConstants;
import charles.game.Engine;
import charles.resources.Art;

public class Spider extends Enemy {

    public Spider(Engine engine, int x, int y) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 16, 13, GameConstants.ENEMY_SPIDER, 40, 12, 30, 10);
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dy != 0 || jumping)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + 204, damDisplace + 80, width, height));
	else if (dx != 0)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + (frame / 5) * width + 188,damDisplace + 80, width, height));
	else
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + 188,damDisplace + 80, width, height));
    
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 237: return (int)(Math.random() * 7 + 12);
	    default: return 0;
	}
    }
    
    public void tryAttack() {
	//If the enemy has not attacked recently and isn't hurt
	if (attackCounter == 0 && damageCounter == 0) {
	    //If the enemy intersects the player
	    if (getRectangle().intersects(engine.getPlayer().getRectangle())) {
		//Checks which direction the enemy will attack from, if its middle is to the left or right of the player's middle    
		int source = (engine.getPlayer().getX() + engine.getPlayer().getWidth() / 2 < x + width / 2) ? 1: -1;
		//Sets time until enemy can attack again
		attackCounter = GameConstants.ENTITY_ATTACK_TIME;
		//Takes damage from the player, using the attack with a random increase
		engine.getPlayer().takeDamage(attack + (int)(Math.random() * (attack / 5)), source);
		
		//Calculates chance of poisoning the player
		int poisonChance = (int)(Math.random() * 100);
		if (poisonChance < 8)   //If the player gets poisoned
		    //Adds time to the player's poison counter
		    engine.getPlayer().addPoison((int)(Math.random() * 300 + 31));
	    }
	}
    }
}
