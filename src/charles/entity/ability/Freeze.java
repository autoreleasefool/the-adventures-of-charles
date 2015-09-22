package charles.entity.ability;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.resources.Art;

public class Freeze extends Ability {

    private static final byte ICE_AMOUNT = 20;

    int[] iceX = new int[Freeze.ICE_AMOUNT];
    int[] iceY = new int[Freeze.ICE_AMOUNT];
    boolean iced = false;
    int iceTime = 30;
    boolean timeAdded = false;
    int freezeTime;

    public Freeze (Engine engine, int x, int y, int width, int height, int time) {
	//Sends these values to the super class
	super(engine, x, y, width, height, 0, 30, engine.getMap().getEnemies().length, 0, true, true);
	
	//This ability freezes enemies for specified time
	freezeTime = time;
	freeze = true;
    }
    
    public void tick() {
	super.tick();   //Calls super class' tick method
	
	//If the ice time is greater than 0
	if (iceTime > 0)
	    iceTime--;  //Decreases ice time
    }
    
    public void render(Graphics g, int bgDrawn) {
	if (!iced) {    //If the positions for ice have not been calculated
	    iced = true;
	    //Randomizes x and y to draw ice particles at
	    for (int i= 0; i<Freeze.ICE_AMOUNT; i++) {
		iceX[i] = (int)(Math.random() * width + x);
		iceY[i] = (int)(Math.random() * height + y);
	    }
	}
	
	//If the iceTime is greater than 0
	if (iceTime > 0) {
	    //Draws ice to the screen
	    for (int i = 0; i<Freeze.ICE_AMOUNT; i++)
		g.drawImage(getIceImage(), iceX[i], iceY[i], null);
	}
	
	//If the damage timer is greater than 
	if (damageTimer > 0) {
	    if(!timeAdded) {    //If timer has not been added yet
		damageTimer = freezeTime;   //Sets the damage timer to the freeze time
		timeAdded = true;
	    }
	    
	    for (int e = 0; e<targets.length; e++) {    //Loops through each target
		if (targets[e] != null)     //If the target is not dead
		    //Draws an animation over the enemy
		    g.drawImage(getImage(), targets[e].getX() + targets [e].getWidth() / 2 - 16, targets[e].getY() + targets[e].getHeight() - 32, null);
	    }
	}
    }
    
    //Gets an image of the ice from the spritesheet
    private Image getIceImage() {
	return(Image)(Art.abilitySheet.getSubimage((iceTime - 30) / -3 * 4 + 736, 8, 4, 8));
    }
    
    //Gets animaton from the spritesheet - moves up then down
    public Image getImage() {
	int xx = 0;
	if (damageTimer > freezeTime - 18)
	    xx = (damageTimer - freezeTime) / -6 * 32;
	else if (damageTimer < 18)
	    xx = (damageTimer - 30) / -6 * 32;
	else
	    xx = 64;
	    
	return(Image)(Art.abilitySheet.getSubimage(xx, 192, 32, 32));
    }
    
    //When this ability expires, it unfreezes all remaining targets
    void expire() {
	for (int i = 0; i<targets.length; i++) {
	    if (targets[i] != null)
		targets[i].unfreeze();
	}
	engine.getMap().removeEntity(this);
    }
}
