package charles.entity.ability;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.resources.Art;

public class Teleport extends Ability {
    public Teleport(Engine engine, int x, int y) {
	//Sends these values to the super class
	super(engine, x, y, 16, 64, 0, 30, 0, 0, false, false);
    }
    
    public void tick() {
	super.tick();       //Calls the super class' tick method
	
	animFrame++;        //Increases the animation frame
	
	x = engine.getPlayer().getX();  //Moves animation over the player
	y = engine.getPlayer().getY();  //Moves animation over the player
    }
    
    public void render(Graphics g, int bgDrawn) {
	//Draws the ability to the screen
	g.drawImage(getImage(), x, y, null);
    }
    
    //Returns image of the ability from the spritesheet
    public Image getImage() {
	return (Image)(Art.abilitySheet.getSubimage((animFrame / 6) * 16 + 656, 0, 16, 64));
    }
}
