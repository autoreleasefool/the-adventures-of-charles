package charles.entity.ability;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.resources.Art;

public class Meditate extends Ability {
    
    public Meditate(Engine engine, int x, int y) {
	//Sends these values to the super class
	super(engine, x, y, 16, 64, 0, 900, 0, 0, false, false);
    }
    
    public void tick() {
	super.tick();       //Calls the super class' tick method
	
	animFrame++;        //Increases the animation frame
	
	x = engine.getPlayer().getX();  //Moves animation over the player
	y = engine.getPlayer().getY();  //Moves animation over the player
    }
    
    public void render(Graphics g, int bgDrawn) {
	if (animFrame < 30) //If the time is at less than 30 frames
	    g.drawImage(getImage(), x, y, null);    //Draws animation to the screen
	    
	//Draws an "active ability" icon to the screen which blinks when it is almost up
	if ((time <= 90 && time % 10 >= 5) || time > 90)
	    g.drawImage(getPassiveImage(), engine.getPlayer().getX() - bgDrawn * 36 - engine.getPlayer().getXDrawOffset() + 175, engine.getPlayer().getY() - engine.getPlayer().getYDrawOffset() - 118, null);
    }
    
    //Returns image of the ability from the spritesheet
    public Image getImage() {
	return (Image)(Art.abilitySheet.getSubimage(animFrame / 6 * 16 + 464, 0, 16, 64));
    }
    
    //Returns "active ability" icon of the ability from the spritesheet
    private Image getPassiveImage() {
	return (Image)(Art.abilitySheet.getSubimage(544, 0, 32, 32));
    }
}
