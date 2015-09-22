/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 15, 2012
 *
 * Application: Tiny Town
 * Class:       Player2D
 *
 * Purpose:     The Player's character used in the 2D section of the game
 *
 **/
package charles.entity;

import java.awt.Graphics;
import java.awt.Image;

import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.game.OptionManager;
import charles.game.InputHandler;
import charles.map.Map;
import charles.game.Engine;
import charles.resources.Art;
import charles.resources.Sound;
import charles.entity.ability.*;

public class Player2D extends Player {

    boolean jumping;
    boolean crouching;
    boolean knocked;
    int jumpFrame;
    int safeBlock;
    int knockTime;
    int knockHeight = 5;
    
    Thread gangnamThread;
    boolean gangnamStyle;
    int[] gangnamJumps = {1,0,1,1,0,1,0,0};
    int gangnamCount = 0;
    
    public Player2D(Engine engine, PlayerOH player) {
	//Sends these values to the super class
	super(engine, 0, 0, 16, 62, player.getHealth(), player.getFullHealth(), player.getMana(), player.getFullMana(), player.getExperience(), player.getFullExperience(), player.getLevel(), 0, player.getHealerTimer(), player.getRevMedTimer(), player.getPoisonTimer(), player.getManaTimer(), player.getWindTimer(), player.getAbilities(), player.getAbilityLevels(), player.getAbilityTimers(), player.getAbilityPoints());
	
	//Stores these values from the Player paramter
	this.inventory = player.getInventory();
	this.wallet = player.getWallet();
	this.jewelryTimer = player.getJewelryTimer();
    }
    
    public Player2D(Engine engine, String[] loaded) {
	super(engine, Integer.parseInt(loaded[0]), Integer.parseInt(loaded[1]),16,62,Integer.parseInt(loaded[2]),Integer.parseInt(loaded[3]),Integer.parseInt(loaded[4]),Integer.parseInt(loaded[5]),Integer.parseInt(loaded[6]),Integer.parseInt(loaded[7]),Integer.parseInt(loaded[8]),0,0,0,0,0,0,Player.ARCHMAGE_ABILITIES,new byte[]{Byte.parseByte(loaded[9]),Byte.parseByte(loaded[10]),Byte.parseByte(loaded[11]),Byte.parseByte(loaded[12]),Byte.parseByte(loaded[13]),Byte.parseByte(loaded[14]),Byte.parseByte(loaded[15]),Byte.parseByte(loaded[16]),Byte.parseByte(loaded[17]),Byte.parseByte(loaded[18]),Byte.parseByte(loaded[19]),Byte.parseByte(loaded[20]),Byte.parseByte(loaded[21]),Byte.parseByte(loaded[22]),Byte.parseByte(loaded[23]),Byte.parseByte(loaded[24])}, null,new int[]{0,0,0});
    }
    
    public void render(Graphics g) {
	//Renders the player to the provided Graphics in the center of the screen
	g.drawImage(getImage(), GameConstants.GRAPHICS_WIDTH / 2 - width / 2 + xDrawOffset, GameConstants.GRAPHICS_HEIGHT / 2 - height / 2 + yDrawOffset, null);
    }
    
    public Image getImage() {
	//Checks if the player is damaged
	int damDisplace = (damageCounter>0) ? (damageCounter % 10) / 5 * 64: 0;
	
	//Gets the image of the player, jumping, running, crouching or damaged
	if (dy!= 0 || jumping) {
	    int xShift = (jumpFrame < 2 && jumping && dy<0) ? width * 3:width * 4;
	    return (Image) (Art.charSide.getSubimage(direction * (width * 6) + xShift, 2 + damDisplace, width, height));
	} else if (crouching)
	    return (Image) (Art.charSide.getSubimage(direction * (width * 6) + width * 5, 2 + damDisplace, width, height));
	else if (dx!= 0)
	    return (Image) (Art.charSide.getSubimage(direction * (width * 6) + (frame / 5 + 1) * width, 2 + damDisplace, width, height));
	else
	    return (Image) (Art.charSide.getSubimage(direction * (width * 6),damDisplace + 2,width, height));
    }
    
    public void move(Map map) {
	int[][] layout = map.getLayout();   //Gets the layout of the map
    
	oldX = x;   //Stores the player's old X
	oldY = y;   //Stores the player's Old Y
	x += dx + ((dx > 0) ? spdBoost:((dx < 0) ? -spdBoost:0)); //Increases x by current speed plus a speed boost
	y += dy;    //Increases y

	
	//Keeps player withing map boundaries
	if (x < 0)
	    x = 0;
	else if (x + width >= map.getMapWidth())
	    x = map.getMapWidth() - width;

	//Checks if the current tile the player is in has a top and is not the safe block
	if ((Map.tileHasTop((y+height) / MapConstants.TILE_SIZE, x / MapConstants.TILE_SIZE, layout) || Map.tileHasTop((y+height) / MapConstants.TILE_SIZE, (x+width-1) / MapConstants.TILE_SIZE, layout)) && dy > 0 && (y+height) / MapConstants.TILE_SIZE != safeBlock) {
	    int hitBlock = (y+height) / MapConstants.TILE_SIZE; //Gets the current block
	    dy = 0; //Stops the player's vertical movement
	    if (jumping) jumping = false;   //Stops the player from jumping
	    while((y+height) / MapConstants.TILE_SIZE == hitBlock)  //While the player is in the solid block
		y -= 1;                                             //Subtracts 1 from their position
		
		//If the tile below the player is not solid
	} else if ((!Map.tileHasTop((y+height+1) / MapConstants.TILE_SIZE, x / MapConstants.TILE_SIZE, layout) && !Map.tileHasTop((y+height+1) / MapConstants.TILE_SIZE, (x+width-1) / MapConstants.TILE_SIZE, layout)) || jumping) {
	    dy = Math.min(dy + 1, 10);  //Increases the player's DY
	    safeBlock = (y+height) / MapConstants.TILE_SIZE;    //Stores the block as a safe block as the player is now in it
		    
	    if (!jumping) {     //If the player is not jumping
		safeBlock = (y+height+1) / MapConstants.TILE_SIZE;  //Stores the tile below as the safe block
		jumping = true; //Starts the player jumping
		jumpFrame = 2;  //Sets a jumpframe for the player to skip animation
	    }
	    
	}
	    
	//Keeps the player within map boundaries again
	if (dx > 0) 
	    direction = 0;
	else if (dx < 0)
	    direction = 1;

	//Offsets the graphics if the player nears the end of the map
	if (x <= 192)
	    xDrawOffset = x - GameConstants.GRAPHICS_WIDTH / 2 + width / 2;
	else if (x + width >= map.getMapWidth() - 192)
	    xDrawOffset = x - GameConstants.GRAPHICS_WIDTH / 2 + width / 2 - 240;
	else
	    xDrawOffset = 0;
    }
    
    public void updateInput(InputHandler input) {
	
	//Checks if the player should be crouching
	crouching = (input.keys[GameConstants.KEY_DOWN].down);

	if (input.keys[GameConstants.KEY_LEFT].down && !crouching) {
	    //If the left key is down and the player isn't crouching
	    //They move to the left
	    setDX(-3);
	    stopGangnam();  //Stops the Gangnam style music from playing
	} else if (input.keys[GameConstants.KEY_RIGHT].down && !crouching) {
	    //If the right key is down and the player isn't crouching
	    //They move to the right
	    setDX(3);
	    stopGangnam();  //Stops the Gangnam style music from playing
	} else {            //If neither key is down
	    setDX(0);       //Stops the player from moving
	}
    
	for (int k = 0; k<GameConstants.TOTAL_KEY_CODES; k++) {
	    if (input.keys[k].clicked) {
		//Performs events depending on the user's settings
		switch(OptionManager.KEY_EVENTS[k]) {
		    case 0:
			if (crouching)
			    dropDown();
			else
			    jump();
			break;
		    case 1:
			pickUpItem();
			break;
		    case 2: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_TRAITS_ID); break;
		    case 3: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_MINIMAP_ID); break;
		    case 4: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_CUSTOMIZE_ID); break;
		    case 5: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_INVENTORY_ID); break;
		    //case 6: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_INFO_ID); break;
		    case 7: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_ABILITIES_ID); break;
		    case 8: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_QUESTS_ID); break;
		    //case 9: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_OPTIONS_ID); break;
		    //case 10: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_KEY_CONFIG_ID); break;
		    case 11: engine.getCanvas().getMenuBar().showQuitPrompt(); break;
		    //case 12: engine.getCanvas().getMenuBar().showMainMenuPrompt(); break;
		    case 13: useAbility(GameConstants.ABILITY_WIND_CALL); break;
		    case 14: useAbility(GameConstants.ABILITY_DOUBLE_SWIPE); break;
		    case 15: useAbility(GameConstants.ABILITY_HEALER_TOUCH); break;
		    case 16: useAbility(GameConstants.ABILITY_MAGIC_ARMOR); break;
		    case 17: useAbility(GameConstants.ABILITY_MP_GUARD); break;
		    case 18: useAbility(GameConstants.ABILITY_ENERGY_BOLT); break;
		    case 19: useAbility(GameConstants.ABILITY_SHOCKWAVE); break;
		    case 20: useAbility(GameConstants.ABILITY_TELEPORT); break;
		    case 21: useAbility(GameConstants.ABILITY_WIND_BLAST); break;
		    case 22: useAbility(GameConstants.ABILITY_EARTHQUAKE); break;
		    case 23: useAbility(GameConstants.ABILITY_BLAZE); break;
		    case 24: useAbility(GameConstants.ABILITY_FLAMETHROWER); break;
		    case 25: useAbility(GameConstants.ABILITY_REVITALIZE); break;
		    case 26: useAbility(GameConstants.ABILITY_MEDITATE); break;
		    case 27: useAbility(GameConstants.ABILITY_FREEZE); break;
		    case 28: if (isDancing()) goGangnam(); else stopGangnam(); break;
		    //case 35: engine.getCanvas().getMenuBar().openOrCloseGameMenu(GameConstants.MENU_WORLD_MAP_ID); break;
		    default: break; //Does nothing
		}
	    }
	}
    }
    
    public void useAbility(int index) {
	//If the ability is not available, not leveled or is not a timed ability, the method exits
	if (!abilities[index] || abilityLevels[index] == 0 || (abilityTimer[index] != 0 && index != GameConstants.ABILITY_TELEPORT && index != GameConstants.ABILITY_FREEZE && index != GameConstants.ABILITY_WIND_CALL && index != GameConstants.ABILITY_HEALER_TOUCH && index != GameConstants.ABILITY_REVITALIZE && index != GameConstants.ABILITY_MEDITATE))
	    return;
	    
	    
	//If the player does not have enough mana, displays a message
	if (mana < GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1]) {
	    engine.addMessage("Insufficient Mana!", 45, false);
	    return;
	}
    
	switch(index) {
	    case GameConstants.ABILITY_WIND_CALL:
		//If the wind call timer is at 0
		if (windTimer == 0) {
		    //Adds a new ability to the map
		    engine.getMap().addEntity(new WindCall(engine, x, y, abilityLevels[index] * 5 * 30));
		    abilityTimer[index] = abilityLevels[index] * 5 * 30;    //Sets time for how long player is boosted
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana
		    windTimer = 3600;           //Sets the player unable to use this ability for 2 minutes
		} else {
		    engine.addMessage("You must wait " + ((int)(windTimer / 30)) + "s", 45, true);  //Adds a message to the screen
		}
		break;
	    case GameConstants.ABILITY_DOUBLE_SWIPE:
		if (attackCounter == 0) {   //If the attack counter is at 0
		    attackCounter = GameConstants.ENTITY_ATTACK_TIME - attackSpeed; //Sets the atack counter
		    engine.getMap().addEntity(new DoubleSwipe(engine, x, y, 10 + (abilityLevels[index] - 1) * 10, direction));  //Adds a double swipe ability to the screen
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Takes mana from the player
		}
		break;
	    case GameConstants.ABILITY_HEALER_TOUCH:
		if (healerTimer == 0) { //If the healing timer is at 0
		    abilityTimer[index] = 300;  //Sets time player heals over to 10 seconds
		    healerTimer = 30 * 120;     //Sets time until player can use ability again (2 minutes)
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana
		    engine.getMap().addEntity(new HealerTouch(engine, x, y));   //Adds the ability to the map
		} else {
		    engine.addMessage("You must wait " + ((int)(healerTimer / 30)) + "s", 45, true);    //Adds a message to the screen
		}
		break;
	    case GameConstants.ABILITY_MAGIC_ARMOR:
		abilityTimer[index] = abilityLevels[index] * 900;   //Increases time remaining for this ability
		mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana for the ability
		engine.getMap().addEntity(new MagicArmor(engine, x, y, abilityLevels[index] * 900));    //Adds the ability to the map
		break;
	    case GameConstants.ABILITY_MP_GUARD:
		abilityTimer[index] = abilityLevels[index] * 1800;  //Increases time remaining for this ability
		mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana for the ability
		engine.getMap().addEntity(new ManaGuard(engine, x, y, abilityLevels[index] * 1800));    //Adds the ability to the map
		break;
	    case GameConstants.ABILITY_ENERGY_BOLT:
		if (attackCounter == 0) {   //If the attack counter is at 0
		    attackCounter = GameConstants.ENTITY_ATTACK_TIME - attackSpeed; //Sets the atack counter
		    engine.getMap().addEntity(new EnergyBolt(engine, x, y, 30 + (int)(Math.random() * (10 * attBoost / 2)) + abilityLevels[index], direction)); //Adds EnergyBolt ability to the map
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Takes away mana from the player
		}
		break;
	    case GameConstants.ABILITY_SHOCKWAVE:
		if (attackCounter == 0) {   //If the attack counter is at 0
		    attackCounter = GameConstants.ENTITY_ATTACK_TIME - attackSpeed; //Sets the attack counter
		    engine.getMap().addEntity(new Shockwave(engine, x, y, 10 + (int)(Math.random() * (10 * attBoost / 2)) + abilityLevels[index], direction));  //Adds Shockwave ability to the map
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana from the player
		}
		break;
	    case GameConstants.ABILITY_TELEPORT:
		if (abilityTimer[index] == 0) { //If the timer is at 0
		    x += (64 + abilityLevels[index] * 3) * ((direction == 0) ? 1:-1);   //Moves the player set distance away
		    
		    //Keeps player within map boundaries
		    if (x < 0)
			x = 0;
		    else if (x + width >= engine.getMap().getMapWidth())
			x = engine.getMap().getMapWidth() - width;
			
		    engine.getMap().addEntity(new Teleport(engine, x, y));  //Adds new teleport ability to the screen
		    abilityTimer[index] = 495 - abilityLevels[index] * 30;  //Sets the timer for the ability
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana from the plyaer
		} else {
		    engine.addMessage("You must wait " + ((int)(abilityTimer[index] / 30)) + "s", 45, true);    //Adds a message to the screen
		}
		break;
	    case GameConstants.ABILITY_WIND_BLAST:
		engine.getMap().addEntity(new WindBlast(engine, x - 80, y));    //Adds a new WindBlast ability to the map
		abilityTimer[index] = 495 - abilityLevels[index] * 30;          //Sets timer until it can be used again
		mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana from the player
		break;
	    case GameConstants.ABILITY_EARTHQUAKE:
		if (attackCounter == 0) {   //If the attack counter is 0
		    attackCounter = GameConstants.ENTITY_ATTACK_TIME - attackSpeed; //Sets the attack counter time
		    int attWidth = abilityLevels[index] / 5 * 32 + 128; //Gets the width of the attack
		    int attHeight = 64 + abilityLevels[index] / 5 * 16; //Gets the height of the attack
		    
		    //Adds an earthquake ability to the map
		    engine.getMap().addEntity(new Earthquake(engine, x + width / 2 - attWidth / 2, y - (attHeight - 64), attWidth, attHeight, 60 + (int)(Math.random() * (5 * attBoost / 2)) + abilityLevels[index] * 3));
		    
		    //Subtracts the mana from the player
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];
		}
		break;
	    case GameConstants.ABILITY_BLAZE:
		if (attackCounter == 0) {   //If the attack counter is 0
		    attackCounter = GameConstants.ENTITY_ATTACK_TIME - attackSpeed; //Sets the attack counter time
		    engine.getMap().addEntity(new Blaze(engine, x, y, 140 + (abilityLevels[index] - 1) * 5, direction));    //Adds a blaze ability to the map
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana from the player
		}
		break;
	    case GameConstants.ABILITY_FLAMETHROWER:
		if (attackCounter == 0) {   //If the attack counter is 0
		    attackCounter = GameConstants.ENTITY_ATTACK_TIME - attackSpeed; //Sets the attack counter time
		    engine.getMap().addEntity(new Flamethrower(engine, x, y, 100 + (abilityLevels[index] - 1) * 5, direction)); //Adds a flamethrower ability to the map
		    mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana from the player
		}
		break;
	    case GameConstants.ABILITY_REVITALIZE:
		if (revMedTimer == 0) { //If the revitalize / meditate timer is 0
		    revMedTimer = 9000; //Sets the timer to 5 minutes
		    abilityTimer[index] = 900;  //Increases the ability timer for time that mana is gained over (30 seconds)
		    engine.getMap().addEntity(new Revitalize(engine, x, y));    //Adds a revitalize ability to the map
		} else {
		    engine.addMessage("You must wait " + ((int)(revMedTimer / 30)) + "s", 45, true);    //If enough time hasn't passed 
		}
		break;
	    case GameConstants.ABILITY_MEDITATE:
		if (revMedTimer == 0) { //If the revitalize / meditate timer is 0
		    revMedTimer = 9000; //Sets the timer to 5 minutes
		    abilityTimer[index] = 900;  //Increases the ability timer for time that hp is gained over (30 seconds)
		    engine.getMap().addEntity(new Meditate(engine, x, y));  //Adds a meditate ability to the map
		} else {
		    engine.addMessage("You must wait " + ((int)(revMedTimer / 30)) + "s", 45, true);    //If enough time hasn't passed 
		}
		break;
	    case GameConstants.ABILITY_FREEZE:
		if (abilityTimer[index] == 0) { //If the timer is at 0
		    if (attackCounter == 0) {   //If the attack counter is at 0
			attackCounter = GameConstants.ENTITY_ATTACK_TIME - attackSpeed; //Sets the attack timer
			int attWidth = abilityLevels[index] / 5 * 32 + 128; //Calculates width of the attack
			int attHeight = 64 + abilityLevels[index] / 5 * 16; //Calculates height of the attack
			abilityTimer[index] = 960 + abilityLevels[index] * 30;  //Adds to ability timer
			engine.getMap().addEntity(new Freeze(engine, x + width / 2 - attWidth / 2, y - (attHeight - 64), attWidth, attHeight, 60 + abilityLevels[index] * 30)); //Adds freeze ability to the map
			mana -= GameConstants.ABILITY_MANA_COST[index][abilityLevels[index] - 1];   //Subtracts mana from the player
		    }
		} else {
		    engine.addMessage("You must wait " + ((int)(abilityTimer[index] / 30)) + "s", 45, true);    //Adds message to the screen
		}
		break;
	    default: throw new RuntimeException();  //Throws runtime exception if the ability does not exist
	}
    }
    
    private void jump() {
	if (!jumping) { //If the player is not jumping
	    jumping = true; //Sets the player jumping
	    dy = (gangnamStyle) ? -5:-8;    //Sets height of the jump
	    jumpFrame = 0;  //Sets the jump animation to frame 0
	}
    }
    
    private void dropDown() {
	//If the player is at the bottom of the map
	if ((y+height+1) / MapConstants.TILE_SIZE >= engine.getMap().getLayout().length-4)
	    return; //Exits the method
    
	//If the tile below the player is solid
	if ((Map.tileHasTop((y+height+1) / MapConstants.TILE_SIZE, x / MapConstants.TILE_SIZE, engine.getMap().getLayout()) || Map.tileHasTop((y+height+1) / MapConstants.TILE_SIZE, (x+width-1) / MapConstants.TILE_SIZE, engine.getMap().getLayout())) && dy == 0 && !jumping) {
	    safeBlock = (y+height+1) / MapConstants.TILE_SIZE;  //Sets the tile below as the safe block
	    jumping = true; //Sets the player to jumping
	    dy = 1; //Sets the player's to moving down
	}
    }
    
    public void goGangnam() {
	if (!gangnamStyle) {    //If the player is not dancing
	    gangnamStyle = true;        //Sets the player to dancing
	    OptionManager.setPlayerStyle(2); //Sets the player to gangnam style clothing
	    gangnamThread = new Thread() {  //Creates a new thread
		public void run() {
		    direction = 1;      //Sets the player's direction
		    gangnamCount = 1;   //Counts the current jump
		    jump();             //Makes the player jump
		    
		    while(true) {
			if (!jumping) { //If the player is not jumping
			    try {Thread.sleep(75);} //Pauses for 75 milliseconds
			    catch(InterruptedException ie) {ie.printStackTrace();}
			    direction = gangnamJumps[gangnamCount]; //Sets the direction depending on the current jump
			    if (++gangnamCount >= gangnamJumps.length)  //Adds to the jumpcount and resets it to 0 if it hits the limit
				gangnamCount = 0;
			    jump(); //Jumps the player
			}
			
			//Pauses the thread for 2 milliseconds to save resources
			try {
			    Thread.sleep(2);
			} catch (InterruptedException ie) {
			    ie.printStackTrace();
			}
		    }
		}
	    };
	    gangnamThread.start();  //Starts the thread
	    Sound.gangnamStyle_Cut.loop();  //Starts the gangnam style song
	}
    }
    
    public boolean isDancing() {
	return gangnamStyle;    //Used to check if the player is dancing
    }
    
    public void stopGangnam() {
	Sound.gangnamStyle_Cut.stop();  //Stop the music
	if (gangnamThread != null) {    //If the player is dancing
	    gangnamThread.stop();       //Stops the dancing thread
	    gangnamThread = null;       //Sets the thread to null
	    gangnamStyle = false;       //Sets the player to not dancing
	    OptionManager.setPlayerStyle(0);    //Resets the player's style
	    OptionManager.setBowtieActive(false);   //Turns off the bowtie
	}
    }
    
    public boolean isKnocked() {
	return knocked;     //Returns if the player is being knocked back
    }
    
    public void tick() {
	super.tick();       //Calls the super class' tick
	
	if (jumping)    //If the player is jumping
	    jumpFrame++;    //Increases the jumpframe
    }
    
    public void knockback(int source) {
	knocked = true;             //Sets the player to knocked
	knockTime = 15;             //Sets time that player is knocked for
	dx = 3 * -source;           //Sets the player's movement direction
	direction = (dx < 0) ? 0:1; //Changes the player's direction
	jumping = true;             //Sets the player jumping
	dy = -knockHeight;          //Makes player move upward as a jump/knock
    }
}
