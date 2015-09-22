package charles.entity.ability;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.resources.Art;

public class Earthquake extends Ability {

    private static final byte DUST_AMOUNT = 20;

    int[] dustX = new int[Earthquake.DUST_AMOUNT];
    int[] dustY = new int[Earthquake.DUST_AMOUNT];
    boolean dusted = false;
    int earthTime = 30;

    public Earthquake (Engine engine, int x, int y, int width, int height, int attack) {
	//Sends these values to the super class
	super(engine, x, y, width, height, attack, 30, engine.getMap().getEnemies().length, 0, true, true);
    }
    
    public void tick() {
	super.tick();   //Calls super class' tick method
	
	
	//If the earthTime is greater than 0
	if (earthTime > 0)
	    earthTime--;    //Decreases it by 1
	else
	    expire();   //If it isn't, the ability expires
    }
    
    public void render(Graphics g, int bgDrawn) {
	if (!dusted) {  //If the positions for dust have not been calculated
	    dusted = true;
	    //Randomizes x and y to draw dust particles at
	    for (int i= 0; i<Earthquake.DUST_AMOUNT; i++) {
		dustX[i] = (int)(Math.random() * width + x);
		dustY[i] = (int)(Math.random() * height + y);
	    }
	}
	
	//If the earthTime is greater than 0
	if (earthTime > 0) {
	    //Draws dust to the screen
	    for (int i = 0; i<Earthquake.DUST_AMOUNT; i++)
		g.drawImage(getDustImage(), dustX[i], dustY[i], null);
	}
	
	//If the damage Timer is greater than 0
	if (damageTimer > 0) {
	    for (int e = 0; e<targets.length; e++) {    //Loops through each target
		if (targets[e] != null) //If the target is not dead
		    //Draws an animation over the enemy  
		    g.drawImage(getImage(), targets[e].getX() + targets[e].getWidth() / 2 - 16, targets[e].getY() + targets[e].getHeight() / 2 - 16, null);
	    }
	}
    }
    
    //Gets image of the dust from the spritesheet
    private Image getDustImage() {
	return(Image)(Art.abilitySheet.getSubimage((earthTime - 30) / -3 * 4 + 736, 0, 4, 8));
    }
    
    //Gets image of the animation from the spritesheet
    public Image getImage() {
	return(Image)(Art.abilitySheet.getSubimage((damageTimer - 30) / -5 * 32 + 608, 64, 32, 32));
    }
}
