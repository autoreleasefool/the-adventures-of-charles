/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 27, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Dragon
 *
 * Purpose:     Defines the values related to the enemy Dragon
 *
 **/

package charles.entity;

import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.resources.Art;

public class Dragon extends Enemy {
    
    int dragonCol;

    public Dragon(Engine engine, int x, int y, int dragonCol) {
	//Calls the super class' constructor with these values
	super(engine, x, y, 204, 62, GameConstants.ENEMY_DRAGON, 500 + 100 * dragonCol, 150 + 25 * dragonCol, 170 + 10 * dragonCol, 81 + 10 * dragonCol);
	this.speed = 2;                 //Sets its default speed slower
	this.dragonCol = dragonCol;     //Stores the dragon's color
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter > 0) ? (damageCounter % 10) / 5 * height: 0;
	
	//Returns a moving image if the alligator is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dx != 0)
	    return (Image) (Art.enemyDragon.getSubimage(direction * (width * 3) + (frame / 5 + 1) * width, damDisplace + getYOffset(), width, height));
	else
	    return (Image) (Art.enemyDragon.getSubimage(direction * (width * 3), damDisplace + getYOffset(), width, height));
    }
    
    public int randomizeCoinValue(int id) {
	//Randomizes how much a coin drop is worth, then returns it
	switch(id) {
	    case 239: return (int)(Math.random() * 120 + 200);
	    default: return 0;
	}
    }
    
    private int getYOffset() {
	//Returns the position of the sprite corresponding to the dragon's color
	return dragonCol * 124;
    }
    
    void dropItems() {
	//Used to calculate which items the enemy will drop upon death
	
	//Loops through each item the enemy can possibly drop
	for (int i = 0; i<GameConstants.ENEMY_DROPS[id].length; i++) {
	    //Randomizes the chance of dropping the item
	    int chance = (int)(Math.random() * 100);
	    
	    //If the random chance is less than the predefined chance of dropping that item, the item is dropped
	    if (chance < GameConstants.ITEM_DROP_CHANCE[id][i]) {
		//Calculates the color of certain drops depending on dragon's color
		int idOffset = (GameConstants.ENEMY_DROPS[id][i] == 37) ? dragonCol:0;
		
		//Randomizes the amount of coins dropped
		int coinValue = randomizeCoinValue(GameConstants.ENEMY_DROPS[id][i]);
		
		//Creates the item and adds it to the map
		Item it = new Item(engine, x + width / 2 - 8, y + width / 2 - 8, GameConstants.ENEMY_DROPS[id][i] + idOffset, coinValue);
		engine.getMap().addEntity(it);
		
		//Only one coin type can be randomized, so once one is, the method is exited
		if (GameConstants.ENEMY_DROPS[id][i] >= 237)
		    return;
	    }
	}
    }
}
