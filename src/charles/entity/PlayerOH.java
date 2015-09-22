/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 15, 2012
 *
 * Application: Tiny Town
 * Class:       PlayerOH
 *
 * Purpose:     The Player's character used in the overhead section of the game
 *
 **/
package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.game.InputHandler;
import charles.game.OptionManager;
import charles.map.Map;
import charles.game.Engine;
import charles.resources.Art;
import charles.resources.Sound;

public class PlayerOH extends Player {

    public PlayerOH(Engine engine, int x, int y) {
	//Sends these values to the superclass
	super (engine, x, y, 16, 30, 50, 50, 100, 100, 0, 10, 1, 0, 0, 0, 0, 300, 0, Player.BEGINNER_ABILITIES, Player.BEGINNING_ABILITY_LEVELS, null, new int[3]);
    }
    
    public PlayerOH(Engine engine, String[] loaded) {
	
	super(engine, Integer.parseInt(loaded[0]), Integer.parseInt(loaded[1]),16,30,Integer.parseInt(loaded[2]),Integer.parseInt(loaded[3]),Integer.parseInt(loaded[4]),Integer.parseInt(loaded[5]),Integer.parseInt(loaded[6]),Integer.parseInt(loaded[7]),Integer.parseInt(loaded[8]),0,0,0,0,0,0,Player.ARCHMAGE_ABILITIES,new byte[]{Byte.parseByte(loaded[9]),Byte.parseByte(loaded[10]),Byte.parseByte(loaded[11]),Byte.parseByte(loaded[12]),Byte.parseByte(loaded[13]),Byte.parseByte(loaded[14]),Byte.parseByte(loaded[15]),Byte.parseByte(loaded[16]),Byte.parseByte(loaded[17]),Byte.parseByte(loaded[18]),Byte.parseByte(loaded[19]),Byte.parseByte(loaded[20]),Byte.parseByte(loaded[21]),Byte.parseByte(loaded[22]),Byte.parseByte(loaded[23]),Byte.parseByte(loaded[24])}, null,new int[]{0,0,0});
	System.out.println(loaded[0]);
    }
    
    public PlayerOH(Engine engine, Player2D player) {
	//Sends these values from the Player2D parameter to the superclass
	super(engine, 0, 0, 16, 30, player.getHealth(), player.getFullHealth(), player.getMana(), player.getFullMana(), player.getExperience(), player.getFullExperience(), player.getLevel(), 0, player.getHealerTimer(), player.getRevMedTimer(), player.getPoisonTimer(), player.getManaTimer(), player.getWindTimer(), player.getAbilities(), player.getAbilityLevels(), player.getAbilityTimers(), player.getAbilityPoints());
	this.inventory = player.getInventory();         //Saves the inventory from the Player2D parameter
	this.wallet = player.getWallet();               //Saves the wallet from the Player2D parameter
	this.jewelryTimer = player.getJewelryTimer();   //Saves the jewelryTimer from the Player2D parameter
    }
    
    public void render(Graphics g) {
	//Draws the image of the player to the Graphics object
	g.drawImage(getImage(), GameConstants.GRAPHICS_WIDTH / 2 - width / 2, GameConstants.GRAPHICS_HEIGHT / 2 - height / 2, null);
    }
    
    public Image getImage() {
	//Checks if the enemy has been damaged
	int damDisplace = (damageCounter>0) ? (damageCounter % 10) / 5 * 32: 0;
	
	//Returns a moving image if the player is walking or a faded image if it is hurt
	//Uses these conditions to calculate where it will load its image from
	if (dx!=0 || dy!=0)
	    return (Image) (Art.charTiled.getSubimage(direction * (width * 3) + (frame / 5 + 1) * width,damDisplace + 2,width, height));
	else
	    return (Image) (Art.charTiled.getSubimage(direction * (width * 3),damDisplace + 2,width, height));
    }
    
    public void updateInput(InputHandler input) {
	if (input.keys[GameConstants.KEY_UP].down)          //If the up key is down
	    setDY(-2);                                      //Sets the player to move up
	else if (input.keys[GameConstants.KEY_DOWN].down)   //If the down key is down
	    setDY(2);                                       //Sets the player to move down
	else                                                //If neither is down
	    setDY(0);                                       //The player does not move vertically
	
	if (input.keys[GameConstants.KEY_LEFT].down)         //If the left key is down
	    setDX(-2);                                      //Sets the player to move left
	else if (input.keys[GameConstants.KEY_RIGHT].down)  //If the right key is down
	    setDX(2);                                       //Sets the player to move right
	else                                                //If neither key is down
	    setDX(0);                                       //The player does not move horizontally
	    
	for (int k = 0; k<GameConstants.TOTAL_KEY_CODES; k++) {
	    if (input.keys[k].clicked) {
		switch(OptionManager.KEY_EVENTS[k]) {
		    case 2: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_TRAITS_ID); break;
		    case 3: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_MINIMAP_ID); break;
		    case 4: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_CUSTOMIZE_ID); break;
		    case 5: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_INVENTORY_ID); break;
		    //case 6: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_INFO_ID); break;
		    case 7: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_ABILITIES_ID); break;
		    case 8: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_QUESTS_ID); break;
		    //case 9: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_OPTIONS_ID); break;
		    //case 10: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_KEY_CONFIG_ID); break;
		    case 10: engine.getKeyConfig().setVisible(true); break;
		    case 11: engine.getCanvas().getMenuBar().showQuitPrompt(); break;
		    //case 12: engine.getCanvas().getMenuBar().showMainMenuPrompt(); break;
		    case 13:case 14:case 15:case 16:case 17:case 18:case 19:case 20:case 21:case 22:case 23:case 24:case 25:case 26:case 27:
			useAbility(0); break;
		    //case 35: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_WORLD_MAP_ID); break;
		    default: break; //Does nothing
		}
	    }
	}
    }
    
    public void useAbility(int index) {
	engine.addMessage("Can't use that here!", 90, true);    //Cannot use abilities in the Overhead world
    }
    
    public void move(Map map) {
	int[][] layout = map.getLayout();   //Saves the map layout
    
	oldX = x;   //Saves the player's old X
	oldY = y;   //Saves the player's old y
	
	int new_x = x + dx; //Calculates what the player's new x will be 
	int new_y = y + dy; //Calculates what the player's new y will be
	
	//Checks if the player is walking into a wall
	if (canMove(new_x / MapConstants.TILE_SIZE, y / MapConstants.TILE_SIZE, layout, map.getDecor()) && canMove((new_x + width) / MapConstants.TILE_SIZE,y / MapConstants.TILE_SIZE,layout, map.getDecor()) && canMove((new_x+width) / MapConstants.TILE_SIZE, (y+height) / MapConstants.TILE_SIZE, layout, map.getDecor()) && canMove(new_x / MapConstants.TILE_SIZE, (y+height) / MapConstants.TILE_SIZE, layout, map.getDecor()))
	    this.x += dx;   //If not, their x is increased
	if (canMove(x / MapConstants.TILE_SIZE, new_y / MapConstants.TILE_SIZE, layout, map.getDecor()) && canMove((x+width) / MapConstants.TILE_SIZE, (new_y+height) / MapConstants.TILE_SIZE, layout, map.getDecor()) && canMove((x+width) / MapConstants.TILE_SIZE, new_y / MapConstants.TILE_SIZE, layout, map.getDecor()) && canMove(x / MapConstants.TILE_SIZE, (new_y+height) / MapConstants.TILE_SIZE, layout, map.getDecor()))
	    this.y += dy;   //If not, their y is increased
	    
	//Sets the player's direction depending on how they are moving
	if (dy < 0) 
	    direction = 3;
	else if (dx < 0)
	    direction = 1;
	else if (dx > 0)
	    direction = 2;
	else if (dy > 0)
	    direction = 0;
    }
    
    public void knockback(int source){} //Player is not knocked back in overhead world
    
    //Used to check if the player can move on a certain block
    private boolean canMove(int x, int y, int[][] layout, boolean decor) {
	if (decor)
	    return canMove_Decor(x, y, layout);
    
	switch(layout[y][x]) {
	case 5:
	case 25:
	case 45:
	case 15:
	case 35:
	case 55:
	    //These blocks are signs and their message is added to the screen
	    engine.addMessage(engine.getTiledMap().getSignMessage(y, x), 90, false);
	case 4:
	case 24:
	case 44:
	case 14:
	case 34:
	case 60:
	case 61:
	case 62:
	case 63:
	case 64:
	case 65:
	case 66:
	case 67:
	case 68:
	case 69:
	case 80:
	case 81:
	case 82:
	case 84:
	case 85:
	case 86:
	case 87:
	case 89:
	case 100:
	case 101:
	case 102:
	case 104:
	case 105:
	case 106:
	case 107:
	case 109:
	case 120:
	case 121:
	case 122:
	case 124:
	case 125:
	case 126:
	case 127:
	case 129:
	case 140:
	case 141:
	case 142:
	case 144:
	case 160:
	case 161:
	case 162:
	case 164: 
	    //These blocks cannot be moved through
	    return false;
	case 83:
	case 88:
	case 103:
	case 108:
	case 123:
	case 128:
	case 143:
	case 71:
	case 72:
	case 73:
	case 74:
	case 91:
	case 92:
	case 93:
	case 94:
	case 111:
	case 112:
	case 113:
	case 114:
	case 131:
	case 132:
	case 133:
	case 134:
	    //These blocks are doors/exits so a door message is added to the screen
	    engine.addMessage(engine.getTiledMap().getDoorMessage(y, x), 1, false);
	default:
	    //These blocks can be moved through
	    return true;
	}
    }
    
    private boolean canMove_Decor(int x, int y, int[][] layout) {
	//Called if the map is decor instead of terrain
	
	switch(layout[y][x]) {
	case 5:
	case 319:
	case 23:
	case 24:
	case 25:
	case 43:
	case 44:
	case 45:
	    //These blocks cannot be moved through
	    return false;
	case 1:
	case 2:
	case 3:
	case 4:
	    //These blocks are doors/exits so a door message is added to the screen
	    engine.addMessage(engine.getTiledMap().getDoorMessage(y, x), 1, false);
	default:
	    //These blocks can be moved through
	    return true;
	}
    }
}
