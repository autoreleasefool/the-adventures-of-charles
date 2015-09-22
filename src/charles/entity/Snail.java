package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.GameConstants;
import charles.resources.Art;
import charles.game.Engine;

public class Snail extends Enemy {

    int snailCol;

    public Snail(Engine engine, int x, int y, int snailCol) {
	//Sends these values to the superclass
	super(engine, x, y, 16, 12, GameConstants.ENEMY_SNAIL, 18 + 2 * snailCol, 5 + snailCol, 14 + 2 * snailCol, 1 + snailCol);
	this.snailCol = snailCol;   //Saves the color of the snail
	
	//Disables jumping and dropping down for this enemy and slows their speed
	this.jumpHeight = 0;
	this.canDrop = false;
	this.canJump = false;
	this.speed = 1;
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes an amount of coins to be dropped
	switch(id) {
	    case 237: return (int)(Math.random() * 8 + 5);
	    default: return 0;
	}
    }

    public Image getImage() {
	//Checks to see if this enemy is damaged and offsets the image return if it is
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the enemy is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dx != 0)
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + (frame / 5) * width + getXColorOffset(), getYColorOffset() + damDisplace + 56, width, height));
	else
	    return (Image) (Art.enemySide.getSubimage(direction * (width * 2) + getXColorOffset(), getYColorOffset() + damDisplace + 56, width, height));
    }
    
    private int getYColorOffset() {
	//Offsets the getSubimage method to return a different colored sprite
	switch (snailCol) {
	    case 0: case 1: case 2: case 3: return 0;
	    case 4: case 5: return 24;
	    default: throw new RuntimeException();
	}
    }
    
    private int getXColorOffset() {
	switch (snailCol) {
	    //Offsets the getSubimage method to return a different colored sprite
	    case 0: case 4: return 0;
	    case 1: case 5: return 64;
	    case 2: return 128;
	    case 3: return 192;
	    default: throw new RuntimeException();
	}
    }
    
    void dropItems() {
	
	for (int i = 0; i<GameConstants.ENEMY_DROPS[id].length; i++) {  //Loops through each item the enemy can drop
	    int chance = (int)(Math.random() * 100);                    //Randomizes a number from 0-99
	    if (chance < GameConstants.ITEM_DROP_CHANCE[id][i]) {       //If the number is less than the provided drop chance of the item
		int idOffset = (GameConstants.ENEMY_DROPS[id][i] == 1) ? snailCol:0;    //Changes the color of dropped items
		int coinValue = randomizeCoinValue(GameConstants.ENEMY_DROPS[id][i]);   //Calculates the coin value of the item
		Item it = new Item(engine, x + width / 2 - 8, y + width / 2 - 8, GameConstants.ENEMY_DROPS[id][i] + idOffset, coinValue);   //Creates a new Item Object
		engine.getMap().addEntity(it);                          //Adds the item object to the map
		
		if (GameConstants.ENEMY_DROPS[id][i] >= 237)            //If the item was a coin, only one can be dropped so the method exits
		    return;
	    }
	}
    }
}
