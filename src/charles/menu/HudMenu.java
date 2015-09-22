package charles.menu;

import java.awt.Graphics;
import java.awt.Color;
import java.awt.image.BufferedImage;

import charles.game.Engine;
import charles.game.InputHandler;
import charles.game.GameConstants;
import charles.entity.Player;
import charles.resources.Art;
import charles.resources.FontRenderer;

public class HudMenu {

    Engine engine;
    int x, y, width, height;

    float healthPer = 0;
    float manaPer = 0;
    float expPer = 0;
    String playerLevel = "";
    
    int mX, mY;
    int health, fullHealth, mana, fullMana, experience, fullExperience;
    boolean showHealthTip, showManaTip, showExperienceTip;

    public HudMenu(Engine engine) {
	//Saves these parameters
	this.engine = engine;
	this.x = GameConstants.GRAPHICS_WIDTH / 2 - 177 / 2;
	this.y = GameConstants.GRAPHICS_HEIGHT - 57;
	this.width =  177;
	this.height = 57;
    }
    
    public void render(Graphics g) {
	//Creates a bufferedimag and gets graphics for it
	BufferedImage bi = new BufferedImage(width + 100, height, BufferedImage.TYPE_INT_ARGB);
	Graphics g2 = (Graphics) bi.createGraphics();
	
	int healthY = 0;    //Checks where the health bar is taken from 
	int healthPercent = (int)(Math.ceil(healthPer));    //Calculates how much of health bar is drawn
	int manaPercent = (int)(Math.ceil(manaPer));        //Calculates how much of mana bar is drawn
	int expPercent = (int)(Math.ceil(expPer));          //Calculates how much of experience bar is drawn
	
	//Draws green, orange or red bar depending on remaining hp
	if (healthPercent > 66)
	    healthY = 28;
	else if (healthPercent > 33)
	    healthY = 42;
	else
	    healthY = 56;

	//Gets sprites of the status bars to draw and draws them to their positions
	g2.drawImage(Art.hudComp.getSubimage(0,0,102,14), 25, 12, null);
	g2.drawImage(Art.hudComp.getSubimage(0,0,102,14), 25, 27, null);
	g2.drawImage(Art.hudComp.getSubimage(0,70,152,14), 25, 42, null);
	g2.drawImage(Art.hudComp.getSubimage(102,0,15,9), 9, 15, null);
	g2.drawImage(Art.hudComp.getSubimage(102,9,15,9), 9, 30, null);
	g2.drawImage(Art.hudComp.getSubimage(102,18,23,9), 1, 45, null);
	g2.drawImage(Art.hudComp.getSubimage(0,healthY,healthPercent + 1,14), 25, 12, null);
	g2.drawImage(Art.hudComp.getSubimage(0,14,manaPercent + 1,14), 25, 27, null);
	g2.drawImage(Art.hudComp.getSubimage(0,84,expPercent + 1,14), 25, 42, null);
	g2.drawImage(Art.hudComp.getSubimage(102, 27, 50, 29), 128, 12, null);
	FontRenderer.drawString(playerLevel, 129, 22, 0xff404040, 1, g2);
	
	if (showHealthTip) {
	    //If the health is hovered over, then the user's current health and full health are rendered
	    g2.setColor(new Color(0, 0, 0, 0.75f));
	    g2.fillRect(mX, mY - 10, Integer.toString(health).length() * 6 + Integer.toString(fullHealth).length() * 6 + 7, 10);
	    int col = (healthPercent > 66) ? 0xff4cff00:((healthPercent > 33) ? 0xffff8c3f:0xffff5959);
	    FontRenderer.drawString(health + "/" + fullHealth, mX + 1, mY - 9, col, 1, g2);
	} else if (showManaTip) {
	    //Same as above but for mana
	    g2.setColor(new Color(0, 0, 0, 0.75f));
	    g2.fillRect(mX, mY - 10, Integer.toString(mana).length() * 6 + Integer.toString(fullMana).length() * 6 + 7, 10);
	    FontRenderer.drawString(mana + "/" + fullMana, mX + 1, mY - 9, 0xff26a4ff, 1, g2);
	} else if (showExperienceTip) {
	    //Same as above but for user
	    g2.setColor(new Color(0, 0, 0, 0.75f));
	    g2.fillRect(mX, mY - 10, Integer.toString(experience).length() * 6 + Integer.toString(fullExperience).length() * 6 + 7, 10);
	    FontRenderer.drawString(experience + "/" + fullExperience, mX + 1, mY - 9, 0xffffd800, 1, g2);
	}
	
	g2.dispose();   //Disposes graphics for memory conservation
	g.drawImage(bi, x, y, null);    //Draws image to the screen
    }
    
    public void tick(InputHandler input) {
	//Gets health, mana and experience of the player
	health = engine.getPlayer().getHealth();
	mana = engine.getPlayer().getMana();
	experience = engine.getPlayer().getExperience();
	fullHealth = engine.getPlayer().getFullHealth();
	fullMana = engine.getPlayer().getFullMana();
	fullExperience = engine.getPlayer().getFullExperience();
	
	//Calculates percentages remaining of each of these amounts
	healthPer = 100f * health / fullHealth;
	manaPer = 100f * mana / fullMana;
	expPer = 150f * experience / fullExperience;
	playerLevel = "Lv. " + Integer.toString(engine.getPlayer().getLevel());
	
	//Gets x and y of the mouse relative to this menu
	mX = input.mouseX - x;
	mY = input.mouseY - y;
	
	//Displays amounts of health/mana/experience if the conditions are met
	showHealthTip = (mX >= 25 && mX <= 127 && mY >= 12 && mY <= 26);
	showManaTip = (mX >= 25 && mX <= 127 && mY >= 27 && mY <= 41);
	showExperienceTip = (mX >= 25 && mX <= 177 && mY >= 42 && mY <= 56); }
}
