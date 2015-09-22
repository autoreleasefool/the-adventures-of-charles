package charles.entity.ability;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.resources.Art;

public class Blaze extends Ability {

    public Blaze (Engine engine, int x, int y, int attack, int direction) {
	//Sends these values to the super class
	super(engine, x, y, 64, 64, attack, 1, 1, direction, false, true);
    }

    public void render(Graphics g, int bgDrawn) {
	int width = 32, height = 32;
	if (damageTimer > 0) {  //If the ability is damaging an enemy
	    for(int e = 0; e < targets.length; e++) {   //Loops through the targets
		if (targets[e] != null) //If the target is not dead
		    //Draws an animation over the enemy
		    g.drawImage(getImage(), targets[e].getX() + targets[e].getWidth() / 2 - width / 2, targets[e].getY() + targets[e].getHeight() / 2 - height / 2, null);
	    }
	}
    }
    
    //Gets the image of the ability from the spritesheet
    public Image getImage() {
	return (Image) (Art.abilitySheet.getSubimage((damageTimer - 30) / -6 * 32, 160, 32, 32));
    }
}
