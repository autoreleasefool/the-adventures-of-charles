package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.GameConstants;
import charles.resources.Art;
import charles.game.Engine;

public class Slime extends Enemy {

    int slimeCol;
    int size;

    public Slime(Engine engine, int x, int y, int slimeCol, int size) {
	//Sends these values to the super class
	super(engine, x, y, 16 * size, 14 * size, GameConstants.ENEMY_SLIME, 30 + 5 * slimeCol + (size - 1) * 10, 8 + (size - 1) * 2 + 2 * slimeCol, 20 + slimeCol * 3 + 2 * (size - 1), 6 + slimeCol + (size - 1));
	this.slimeCol = slimeCol;   //Saves the color of the slime
	this.size = size;           //Saves the size of the slime
    }
    
    public void render(Graphics g) {
	//Draws the image depending on the size and width of the slime
	g.drawImage(getImage(), x, y, width, height, null);
	if (pursuing)   //If the enemy is following the player
	    g.drawImage(getHPBar(), x, y - 4, null);    //Draws the enemies hp bar
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes an amount of coins to be dropped
	switch(id) {
	    case 237: return (int)(Math.random() * 10 + 10);
	    default: return 0;
	}
    }
    
    public Image getImage() {
	int width = 16, height = 14;
	
	//Checks to see if this enemy is damaged and offsets the image return if it is
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dy != 0 || jumping)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3) + width * 2 + getXColorOffset(), getYColorOffset() + damDisplace, width, height));
	else if (dx != 0)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3) + (frame / 5 + 1) * width + getXColorOffset(), getYColorOffset() + damDisplace, width, height));
	else
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 3) + getXColorOffset(), getYColorOffset() + damDisplace, width, height));
    }
    
    private int getYColorOffset() {
	//Offsets the getSubimage method to return a different colored sprite
	switch (slimeCol) {
	    case 0: case 1: case 2: return 0;
	    case 3: case 4: case 5: return 28;
	    default: throw new RuntimeException();
	}
    }
    
    private int getXColorOffset() {
	//Offsets the getSubimage method to return a different colored sprite
	switch (slimeCol) {
	    case 0: case 3: return 0;
	    case 1: case 4: return 96;
	    case 2: case 5: return 192;
	    default: throw new RuntimeException();
	}
    }
    
    void dropItems() {
	for (int i = 0; i<GameConstants.ENEMY_DROPS[id].length; i++) {  //Loops through each item the enemy can drop
	    int chance = (int)(Math.random() * 100);                    //Randomizes a number from 0-99
	    if (chance < GameConstants.ITEM_DROP_CHANCE[id][i]) {       //If the number is less than the provided drop chance of the item
		int idOffset = (GameConstants.ENEMY_DROPS[id][i] == 7) ? slimeCol:0;    //Changes the color of dropped items
		int coinValue = randomizeCoinValue(GameConstants.ENEMY_DROPS[id][i]);   //Calculates the coin value of the item
		Item it = new Item(engine, x + width / 2 - 8, y + width / 2 - 8, GameConstants.ENEMY_DROPS[id][i] + idOffset, coinValue);   //Creates a new Item Object
		engine.getMap().addEntity(it);                          //Adds the item object to the map
		
		if (GameConstants.ENEMY_DROPS[id][i] >= 237)            //If the item was a coin, only one can be dropped so the method exits
		    return;
	    }
	}
    }
}
