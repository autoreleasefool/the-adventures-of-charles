/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     December 25, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Item
 *
 * Purpose:     Defines the values and methods used by Items
 *
 **/

package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.Engine;
import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.map.Map;
import charles.resources.Art;

public class Item extends Entity {

    int type;
    int id;

    boolean falling;
    int safeBlock;
    
    int coinValue;
    
    public Item(Engine engine, int x, int y, int id, int coinValue) {
	super(engine, x, y, 16, 16);    //Sends these values to the super class
	this.id = id;
	
	if (id < 80)
	    type = 0;           //Miscellaneous drops
	else if (id < 160)
	    type = 1;           //Useable items
	else if (id < 237)
	    type = 2;           //Equippable items
	else {
	    type = 3;           //Coins
	    this.coinValue = coinValue;
	}
	
	moveOutOfGround();      //If the item is in the ground on the map, it is moved up
	falling = true;         //Sets the item to be falling
	dy = -5;                //Sets it to fly up in the air at first
	frame = (int)(Math.random() * 30);  //Randomly calculates what it's y offset will start as
    }
    
    public void tick() {
	if (++frame == 30)  //Increases frame by 1 then checks if it equals 30
	    frame = 0;      //If so, it is set to 0
	
	if (!falling)       //If the item is not falling, the method exits
	    return;

	y += dy;            //Increases the y of the item
	
	//Keeps the item within the map's boundaries
	if (x < 0)
	    x = 0;
	else if (x + width > engine.getMap().getMapWidth())
	    x = engine.getMap().getMapWidth() - width;
	
	//Gets the layout of the map
	int[][] layout = engine.getMap().getLayout();
	
	//If the block the item is in is solid
	if ((Map.tileHasTop((y+height) / MapConstants.TILE_SIZE, x / MapConstants.TILE_SIZE, layout) || Map.tileHasTop((y+height) / MapConstants.TILE_SIZE, (x+width-1) / MapConstants.TILE_SIZE, layout)) && dy > 0 && (y+height) / MapConstants.TILE_SIZE != safeBlock) {
	    int hitBlock = (y+height) / MapConstants.TILE_SIZE; //Saves the block the item is in
	    dy = 0;                                             //Stops the item from moving vertically
	    if (falling) falling = false;                       //Stops the item from falling
	    while((y+height) / MapConstants.TILE_SIZE == hitBlock)  //While the item is in the solid block
		y -= 1;                                         //It is moved 1 pixel up
	} else if ((!Map.tileHasTop((y+height+1) / MapConstants.TILE_SIZE, x / MapConstants.TILE_SIZE, layout) && !Map.tileHasTop((y+height+1) / MapConstants.TILE_SIZE, (x+width-1) / MapConstants.TILE_SIZE, layout)) || falling) {
	    //If the pixel below the item is empty
	    
	    //The vertical speed is increased to a maximum of 10
	    dy = Math.min(dy + 1, 10);
	    
	    //Saves the row the item is in as a "safe block" to fall through
	    safeBlock = (y+height) / MapConstants.TILE_SIZE;
	}
    }
    
    private void moveOutOfGround() {
	//While the item is below the map's limit
	while((y + height) / MapConstants.TILE_SIZE >= engine.getMap().getLayout().length-4)
	    y--;    //Decreases the y location
    }
    
    public boolean isCurrency() {   //Checks if the item is currency
	return type == 3;
    }
    
    public int getID() {            //Returns the id of the item
	return id;
    }
    
    public int getCoinValue() {     //Gets the coin value of the item
	if (isCurrency())           //If it is currency
	    return coinValue;       //Returns the coin value
	else                        //If it is not money
	    return 0;               //Returns 0
    }
    
    public void render(Graphics g) {
	//Draws the image of the item to the screen, moving up and down with the frame
	g.drawImage(getImage(), x, y + ((frame < 15) ? -frame:-(30 - frame)), null);
    }
    
    public Image getImage() {
	//Gets the location of the image's sprite in the spritesheet
	int xx = id % 16;
	int yy = id / 16;
	
	//Gets the subimage and returns it
	return (Image) (Art.itemSheet.getSubimage(xx * GameConstants.ITEM_SIZE, yy * GameConstants.ITEM_SIZE, GameConstants.ITEM_SIZE, GameConstants.ITEM_SIZE));
    }
    
    //Returns the name of the item depending on the provided ID
    public static String getName(int id) {
	switch(id) {
	    case 0: return "Mucus";
	    case 1: return "Green Shell";
	    case 2: return "Yellow Shell";
	    case 3: return "Blue Shell";
	    case 4: return "Purple Shell";
	    case 5: return "Red Shell";
	    case 6: return "Black Shell";
	    case 7: return "Green Slimeball";
	    case 8: return "Yellow Slimeball";
	    case 9: return "Blue Slimeball";
	    case 10: return "Purple Slimeball";
	    case 11: return "Red Slimeball";
	    case 12: return "Black Slimeball";
	    case 13: return "String";
	    case 14: return "Venom";
	    case 15: return "Beeswax";
	    case 16: return "Stinger";
	    case 17: return "Turtle Shell";
	    case 18: return "Fly Wing";
	    case 19: return "Alligator Tooth";
	    case 20: return "Alligator Skin";
	    case 21: return "Tophat";
	    case 22: return "Coal";
	    case 23: return "Red Fox Pelt";
	    case 24: return "Arctic Fox Pelt";
	    case 25: return "Brown Bear Pelt";
	    case 26: return "Polar Bear Pelt";
	    case 27: return "Porkchop";
	    case 28: return "Pig's Hoof";
	    case 29: return "Boar Pelt";
	    case 30: return "Fish";
	    case 31: return "Goblin Tooth";
	    case 32: return "Leather";
	    case 33: return "Wooden Club";
	    case 34: return "Battery";
	    case 35: return "Wheel";
	    case 36: return "Dragonbreath";
	    case 37: return "Green Dragonhide";
	    case 38: return "Yellow Dragonhide";
	    case 39: return "Blue Dragonhide";
	    case 40: return "Purple Dragonhide";
	    case 41: return "Red Dragonhide";
	    case 42: return "Black Dragonhide";
	    case 43: return "Fairy Wing";
	    case 44: return "Apple";
	    case 45: return "Mushroom";
	    case 46: return "Grape";
	    case 80: return "Health Potion I";
	    case 81: return "Health Potion II";
	    case 82: return "Health Potion III";
	    case 83: return "Mana Potion I";
	    case 84: return "Mana Potion II";
	    case 85: return "Mana Potion III";
	    case 86: return "Strength Potion";
	    case 87: return "Intelligence Potion";
	    case 88: return "Dexterity Potion";
	    case 89: return "Felix Felicis";
	    case 90: return "Beer";
	    case 91: return "Letter";
	    case 92: return "Note";
	    case 160: return "Magic Stick";
	    case 161: return "Beginner's Wand";
	    case 162: return "Quartz Wand";
	    case 163: return "Ruby Wand";
	    case 164: return "Emerald Wand";
	    case 165: return "Mysterious Staff";
	    case 166: return "Magic Staff";
	    case 167: return "Enchanted Staff";
	    case 176: return "Leather Cap";
	    case 177: return "Green Hat";
	    case 178: return "Feathered Hat";
	    case 179: return "Magic Hat";
	    case 180: return "Mage's Hat";
	    case 181: return "Archmage's Hat";
	    case 182: return "Leather Gloves";
	    case 183: return "Green Gloves";
	    case 184: return "Blue Gloves";
	    case 185: return "Silk Gloves";
	    case 186: return "Magic Gloves";
	    case 187: return "Enchanted Gloves";
	    case 192: return "Leather Boots";
	    case 193: return "Green Boots";
	    case 194: return "Blue Boots";
	    case 195: return "Silk Boots";
	    case 196: return "Magic Boots";
	    case 197: return "Enchanted Boots";
	    case 198: return "Green Shirt";
	    case 199: return "Fine Shirt";
	    case 200: return "Silk Shirt";
	    case 201: return "Blue Robes";
	    case 202: return "Mage's Robes";
	    case 203: return "Archmage's Robes";
	    case 208: return "Hermes' Amulet";
	    case 209: return "Onyx Amulet";
	    case 210: return "Amulet of Rage";
	    case 211: return "Blessed Amulet";
	    case 212: return "Healer's Amulet";
	    case 213: return "Ring of Quickness";
	    case 214: return "Stone Ring";
	    case 215: return "Burning Ring";
	    case 216: return "Wisp Ring";
	    case 217: return "Nature's Ring";
	    default: return "";
	}
    }
    
    //For equipment, returns the items which the item being equipped with interfere with
    public static int[] getConflictingEquips(int id) {
	switch(id) {
	    case 160: return new int[]{161,162,163,164,165,166,167};
	    case 161: return new int[]{160,162,163,164,165,166,167};
	    case 162: return new int[]{160,161,163,164,165,166,167};
	    case 163: return new int[]{160,161,162,164,165,166,167};
	    case 164: return new int[]{160,161,162,163,165,166,167};
	    case 165: return new int[]{160,161,162,163,164,166,167};
	    case 166: return new int[]{160,161,162,163,164,165,167};
	    case 167: return new int[]{160,161,162,163,164,165,166};
	    case 176: return new int[]{177,178,179,180,181};
	    case 177: return new int[]{176,178,179,180,181};
	    case 178: return new int[]{176,177,179,180,181};
	    case 179: return new int[]{176,177,178,180,181};
	    case 180: return new int[]{176,177,178,179,181};
	    case 181: return new int[]{176,177,178,179,180};
	    case 182: return new int[]{183,184,185,186,187};
	    case 183: return new int[]{182,184,185,186,187};
	    case 184: return new int[]{182,183,185,186,187};
	    case 185: return new int[]{182,183,184,186,187};
	    case 186: return new int[]{182,183,184,185,187};
	    case 187: return new int[]{182,183,184,185,186};
	    case 192: return new int[]{193,194,195,196,197};
	    case 193: return new int[]{192,194,195,196,197};
	    case 194: return new int[]{192,193,195,196,197};
	    case 195: return new int[]{192,193,194,196,197};
	    case 196: return new int[]{192,193,194,195,197};
	    case 197: return new int[]{192,193,194,195,196};
	    case 198: return new int[]{199,200,201,202,203};
	    case 199: return new int[]{198,200,201,202,203};
	    case 200: return new int[]{198,199,201,202,203};
	    case 201: return new int[]{198,199,200,202,203};
	    case 202: return new int[]{198,199,200,201,203};
	    case 203: return new int[]{198,199,200,201,202};
	    case 208: return new int[]{209,210,211,212};
	    case 209: return new int[]{208,210,211,212};
	    case 210: return new int[]{208,209,211,212};
	    case 211: return new int[]{208,209,210,212};
	    case 212: return new int[]{208,209,210,211};
	    case 213: return new int[]{214,215,216,217};
	    case 214: return new int[]{213,215,216,217};
	    case 215: return new int[]{213,214,216,217};
	    case 216: return new int[]{213,214,215,217};
	    case 217: return new int[]{213,214,215,216};
	    default: return new int[]{};
	}
    }
}
