/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     November 25, 2012
 *
 * Application: The Adventures of Charles
 * Class:       Player
 *
 * Purpose:     Defines the values and methods used by the Player
 *
 **/

package charles.entity;

import java.util.ArrayList;
import javax.swing.JOptionPane;

import charles.game.GameConstants;
import charles.game.Engine;
import charles.map.Map;
import charles.resources.Sound;

public abstract class Player extends LivingEntity {

    int oldX, oldY;
    int experience, fullExperience;
    int strengthStat, intelligenceStat, dexterityStat, luckStat;
    int traitPoints;
    int[] abilityPoints;
    int level;
    
    int wallet;
    int[] inventory = new int[237];
    boolean[] equipped = new boolean[77];
    byte[] abilityLevels;
    boolean[] abilities;
    int[] abilityTimer = new int[23];
    int[] jewelryTimer = new int[4];
    
    int windTimer;
    int healerTimer;
    int revMedTimer;
    int poisonTimer;
    int manaTimer;

    //TODO Delete any that aren't used
    int spdBoost;
    int defBoost;
    int attBoost;
    int strBoost;
    int intBoost;
    int lukBoost;
    int dexBoost;
    int attackSpeed;
    
    int xDrawOffset = 0, yDrawOffset = 0;
    
    private ArrayList<Quest> questsAvailable = new ArrayList<Quest>();
    private ArrayList<Quest> questsInProgress = new ArrayList<Quest>();
    private ArrayList<Quest> questsCompleted = new ArrayList<Quest>();
    private boolean updateQuests;

    public Player(Engine engine, int x, int y, int width, int height, int health, int fullHealth, int mana, int fullMana, int experience, int fullExperience, int level, int direction, int healerTimer, int revMedTimer, int poisonTimer, int manaTimer, int windTimer, boolean abilities[], byte[] abilityLevels, int[] abilityTimer, int[] abilityPoints) {
	super(engine, x, y, width, height, fullHealth, fullMana, 0, 0xffff0000, 0, 0);
	//Sends the values to the super class
	
	//Saves these parameters in class variables
	this.oldX = x;
	this.oldY = y;
	this.health = health;
	this.mana = mana;
	this.experience = experience;
	this.fullExperience = fullExperience;
	this.level = level;
	this.direction = direction;
	this.abilities = abilities;
	this.abilityLevels = abilityLevels;
	this.abilityPoints = abilityPoints;
	this.healerTimer = healerTimer;
	this.revMedTimer = revMedTimer;
	this.poisonTimer = poisonTimer;
	this.manaTimer = manaTimer;
	this.windTimer = windTimer;
	if (abilityTimer != null)
	    this.abilityTimer = abilityTimer;
	
	this.abilities = Player.ARCHMAGE_ABILITIES;
    }
    
    public String getSaveInfo() {
	String s = getX() + System.getProperty("line.separator");
	s+= getY() + System.getProperty("line.separator");
	s+=getHealth() + System.getProperty("line.separator");
	s += getFullHealth() +System.getProperty("line.separator");
	s += getMana() +System.getProperty("line.separator");
	s += getFullMana()+System.getProperty("line.separator");
	s+=getExperience()+System.getProperty("line.separator");
	s+=getFullExperience()+System.getProperty("line.separator");
	s+=getLevel()+System.getProperty("line.separator");
	for (int i = 0; i<abilityLevels.length;i++)
	    s+=abilityLevels[i]+System.getProperty("line.separator");
	    
	return s;
    }
    
    //Any class which extends Player must implement these methods
    public abstract void move(charles.map.Map map);
    public abstract java.awt.Image getImage();
    public abstract void useAbility(int index);
    public abstract void updateInput(charles.game.InputHandler input);
    
    public void tick() {
	super.tick();
	
	//TODO Delete any that aren't used
	
	//Resets each of these bonus variables to be recalculated
	spdBoost = 0;
	defBoost = 0;
	attBoost = 0;
	lukBoost = 0;
	strBoost = 0;
	dexBoost = 0;
	intBoost = 0;

	//Goes through each hewelry timer and if it is greater than 0,
	//subtracts one from it. If it equals 0 and the item is equipped,
	//The user gains the amount of health of mana specified
	if (jewelryTimer[0] > 0)
	    jewelryTimer[0]--;
	else if (equipped[51])
	    gainMana(30);
	    
	if (jewelryTimer[1] > 0)
	    jewelryTimer[1]--;
	else if (equipped[52])
	    gainHealth(20);
	    
	if (jewelryTimer[2] > 0)
	    jewelryTimer[2]--;
	else if (equipped[56])
	    gainMana(25);
	    
	if (jewelryTimer[3] > 0)
	    jewelryTimer[3]--;
	else if (equipped[57])
	    gainHealth(15);

	if (healerTimer > 0)        //If this timer is greater than 0
	    healerTimer--;          //Subtracts one from the timer
	    
	if (revMedTimer > 0)        //If this timer is greater than 0
	    revMedTimer--;          //Subtracts one from the timer
	    
	if (windTimer > 0)
	    windTimer--;
	    
	if (poisonTimer > 0) {      //If this timer is greater than 0
	    poisonTimer--;          //Subtracts one from the timer
	    if (poisonTimer % 30 == 0)  //If one second has passed
		//Player takes damage up to 5% of full health
		takeDamage((int)(Math.random() * (fullHealth / 20) + 1), 2);    //Causes damage to player
	}
	
	if (manaTimer > 0) {    //If this timer is greater than 0
	    manaTimer--;        //Subtracts one from this timer
	} else {
	    manaTimer = 300;    //Adds 10 seconds to the timer
	    gainMana(3);        //Player heals 3 mana
	}
	
	for (int i = 0; i<abilityTimer.length; i++) {   //Loops through each ability timer
	    if (abilityTimer[i] > 0) {      //If the timer is greater than 0
		abilityTimer[i]--;          //Subtracts one from the timer
		
		//Special effects of the abilities
		//If the ability adds a bonus to the player, it is calculated here
		switch(i) {
		    case GameConstants.ABILITY_WIND_CALL: spdBoost += 1; break;
		    case GameConstants.ABILITY_MAGIC_ARMOR: defBoost += abilityLevels[GameConstants.ABILITY_MAGIC_ARMOR]; break;
		    case GameConstants.ABILITY_HEALER_TOUCH:
			if (abilityTimer[i] % 30 == 0)
			    gainHealth((abilityLevels[i] == 1) ? 1:((abilityLevels[i] == 2) ? 3: 6));
			break;
		    case GameConstants.ABILITY_MEDITATE:
			if (abilityTimer[i] % 30 == 0)
			    gainMana(abilityLevels[i]);
			break;
		    case GameConstants.ABILITY_REVITALIZE:
			if (abilityTimer[i] % 30 == 0)
			    gainHealth(abilityLevels[i]);
			break;
		    default: break;
		}
	    }
	}
	    
	//Calls loadEquipmentBoost method
	loadEquipmentBoost();
	
	//Calls loadTraitBoost method
	loadTraitBoost();
    }
    
    private void loadTraitBoost() {
	attBoost += (int)(strengthStat * 1.5d);
	defBoost += (int)(strengthStat * 1.5d);
	attackSpeed = Math.max(dexterityStat / 8, 12);
    }
    
    private void loadEquipmentBoost() {
	for (int i = 0; i<equipped.length; i++) {   //Loops through each equippable item
	    if (equipped[i]) {  //If the item is equipped
		switch(i) {     //Calculates the boost of the equipped item and adds it
		    case 0:case 1:case 2:case 3:case 4:case 5:case 6:case 7:
			attBoost += (i + 1) * 3;
			break;
		    case 16:case 17:case 18:case 19:case 20:case 21:
			defBoost += i - 14;
			break;
		    case 25:case 26:case 27:
			attBoost += i - 24;
		    case 22:case 23:case 24:
			defBoost += i - 21;
			break;
		    case 32:case 33:case 34:case 35:case 36:case 37:
			defBoost += (i - 31) * 2;
			break;
		    case 38:case 39:case 40:case 41:case 42:case 43:
			defBoost += (i - 32) + (i - 38) * 3;
			break;
		    case 48: spdBoost++; break;
		    case 49: defBoost += 4; break;
		    case 50: attBoost += 4; break;
		    case 51: if (jewelryTimer[0] == 0) jewelryTimer[0] = 1350; break;
		    case 52: if (jewelryTimer[1] == 0) jewelryTimer[1] = 1350; break;
		    case 53: spdBoost++; break;
		    case 54: defBoost += 3;
		    case 55: attBoost += 3;
		    case 56: if (jewelryTimer[2] == 0) jewelryTimer[2] = 2700; break;
		    case 57: if (jewelryTimer[3] == 0) jewelryTimer[3] = 2700; break;
		}
	    }
	}
    }
    
    public void takeDamage (int damage, int source) {
	if (damageCounter > 0)  //If the damage counter is greater than 0
	    return;             //The method exits, the attack does nothing
    
	if (source != 2) {      //If the source of the attack is not 2 (poison)
	    damage = Math.max(damage - defense, 1); //The damage is reduced by the entity's defense
	    damage = calcDodgeChance(damage);       //The damage is also possibly reduced by a dodge chance
	}
	
	if (abilityTimer[GameConstants.ABILITY_MP_GUARD] > 0) {
	    int startDamage = damage;
	    int startMana = mana;
	    int displace = (int)(damage * (0.08D * abilityLevels[GameConstants.ABILITY_MP_GUARD]));
	    mana = Math.max(0, mana - displace);
	    displace -= (startMana - mana);
	    damage -= displace;
	}
	    
	playDamageSound();  //Plays the entity's damage sound
	
	//Adds the damage taken to the map, set to the color of the entity's damage (red for player, yellow for enemies)
	engine.getMap().addDamageMessage((Entity)this, ((damage>0) ? damage+"":"Dodged!"), damageCol);
	health = Math.max(health - damage, 0);    //Decreases the entity's health by the damage done
	
	if(health == 0) {
	    //If the player dies, the game ends
	    javax.swing.JOptionPane.showMessageDialog(null, "You have died! The game will now be closed.","Awwww",javax.swing.JOptionPane.WARNING_MESSAGE);
	    System.exit(0);
	}

	//If the damage was greater than 0, the damage counter is set to 1 second
	if (damage>0 && source != 2)
	    damageCounter = 30;

	if (health <= 0) {      //If the entity's health is now 0
	    playDeathSound();   //Entity's death sound is played
	    die();              //The entity dies
	}
	
	if (source != 2)        //If the source is not 2 (poison)
	    knockback(source);  //The entity is knocked back
    }
    
    public void useItem(int id) {
	if (id >= 160) {    //If the item is equippable, it is equipped
	    equipItem(id);
	    return;
	}
    
	boolean used = true;    //Boolean to see if the item is used
	switch(id) {
	    case 80: gainHealth(50); break;     //Health Potion I
	    case 81: gainHealth(150); break;    //Health Potion II
	    case 82: gainHealth(300); break;    //Health Potion III
	    case 83: gainMana(50); break;       //Mana Potion I
	    case 84: gainMana(150); break;      //Mana Potion II
	    case 85: gainMana(300); break;      //Mana Potion III
	    default: used = false; break;       //The item was not useable
	}
	
	if (used)           //If the item was used, it is subtracted from the inventory
	    removeItem(id);
    }
    
    public void equipItem(int id) {
	//Loads the conflicting equippable items
	int[] conflict = Item.getConflictingEquips(id);
	for (int i = 0; i<conflict.length; i++)         //Loops through each conflicting item
	    deEquipItem(conflict[i]);                   //De-equips the item
	    
	equipped[id - 160] = !equipped[id - 160];       //Removes or equips the item
	
	//Begins jewelryTimer if jewelry was equipped
	if (equipped[id - 160]) {
	    switch(id - 160) {
		case 51: jewelryTimer[0] = 1350; break;
		case 52: jewelryTimer[1] = 1350; break;
		case 56: jewelryTimer[2] = 2700; break;
		case 57: jewelryTimer[3] = 2700; break;
		default: return;
	    }
	}
    }
    
    public void deEquipItem(int id) {
	equipped[id - 160] = false; //De- equips item
    }
    
    public int calcDodgeChance (int damage) {
	//TODO
	//System.out.println("calc dodge chances");
	return damage;
    }
    
    public void pickUpItem() {
	Map map = engine.getMap();  //Gets the current map
	
	//Gets the items on the map
	Item[] mapItems = map.getItems();
	
	for (int i = mapItems.length - 1; i >= 0; i--) {
	    Item it = mapItems[i];                  //Gets an item object from the map
	    if (this.intersects(it)) {              //If the player is touching the item
		if (it.isCurrency()) {              //If the item is currency
		    addWallet(it.getCoinValue());   //Adds the value to the player's wallet
		    map.removeEntity(it);           //Removes the item from the map
		} else {
		    byte index = (byte)(it.getID() / 80);   //Gets the item type
		    int itemCount = 0;                      //Counter for number of items
		    for (int j = index * 80; j < (index + 1) * 80 && j<inventory.length; j++) {
			if (inventory[j] > 0 && j == it.getID())    //If the picked up item is in the inventory
			    itemCount = -100;                       //Ensures the item is added
			else if (inventory[j] > 0)                  //Counts the full inventory slots
			    itemCount++;
		    }
		    
		    if (itemCount < 40) {           //If there is room in the inventory
			addInventory(it.getID());   //Adds the item to the inventory
			map.removeEntity(it);       //Removes the item from the map
		    } else {                        //If there is no room in the inventory
			engine.addMessage("Inventory full!", 60, true); //Shows an "Inventory full" messages
		    }
		}
		
		return;  //Exits the method so only one item can be picked up at a time
	    }
	}
    }
    
    public void addExperience(int amount) {
	experience += amount;                   //Adds the experience to the player
	while (experience >= fullExperience) {  //If the player has enough experience to level up
	    level++;                            //Increases the player's level
	    experience -= fullExperience;       //Removes the experience needed to level up
	    int rand = (int)(Math.random() * 4);    //Randomly chooses stat to increase
	    switch (rand) {
		case 0: strengthStat++; break;
		case 1: intelligenceStat++; break;
		case 2: dexterityStat++; break;
		default: luckStat++; break;
	    }
	    
	    //Doesn't rerandomize before leveling ability so the first abilities maxed are the first four abilities
	    while(abilityLevels[rand] == GameConstants.ABILITY_MAX_LEVEL[rand])   //If the ability randomized is already at it's full level
		rand = (int)(Math.random() * 16);                           //Randomizes a new ability
	    abilityLevels[rand]++;  //Increases the level of the ability.
	    
	    //Increases experience, health and mana
	    if (level <= 10) {                    //If the player's less than level 10
		fullExperience *= 1.5;          //Increases the necessary experience for next level by 50%
		fullHealth *= 1.5;
		health = fullHealth;
		fullMana *= 1.5;
		mana = fullMana;
	    } else if (level <= 25) {               //If the player's less than level 25
		fullExperience *= 1.25;         //Increases the necessary experience for next level by 25%
		fullHealth *= 1.25;
		health = fullHealth;
		fullMana*= 1.25;
		mana = fullMana;
	    } else {                              //If the player is any other level
		fullExperience *= 1.1;          //Increases the necessary experience for the next level by 10%
		fullHealth *= 1.1;
		health = fullHealth;
		fullMana *= 1.1;
		mana = fullMana;
	    }
	}
    }
    
    public void dropMoney(int amount) {
	if (!engine.getMap().isTiled()) {   //If the map is not tiled
	    //Creates a new item with the amount of coins dropped
	    Item it = new Item(engine, x + width / 2 - 8, y + height / 2 - 8, (amount < 30) ? 237:((amount < 100) ? 238:239), amount);
	    engine.getMap().addEntity(it);  //Adds the coins to the map
	} else {
	    //Informs player cannot retrieve the dropped money
	    int response = JOptionPane.showConfirmDialog(engine.getCanvas().getGame().getFrame(), "If you drop this money, you will not be able to retrieve it. Are you sure you wish to continue?", "Drop " + amount + " coins?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    
	    //If they chose to not drop the item, the method exits
	    if (response != JOptionPane.OK_OPTION)
		return;
	}
	
	wallet -= amount;                   //Subtracts the amount from the wallet
    }
    
    public void spendMoney(int amount) {
	wallet -= amount;   //Removes this money from the inventory            
    }
    
    public void dropItem(int id) {
	//If the map is not tiled
	if (!engine.getMap().isTiled()) {
	    //Creates a new item of the dropped id
	    Item it = new Item(engine, x + width / 2 - 8, y + height / 2 - 8, id,  0);
	    engine.getMap().addEntity(it);  //Adds the item to the map
	} else {
	    //Informs player they cannot retrieve the dropped item
	    int response = JOptionPane.showConfirmDialog(engine.getCanvas().getGame().getFrame(), "If you drop this item, you will not be able to retrieve it. Are you sure you wish to continue?", "Drop " + Item.getName(id) + "?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);
	    
	    //If they chose to not drop the item, the method exits
	    if (response != JOptionPane.OK_OPTION)
		return;
	}
	
	removeItem(id); //Removes the item from the inventory
    }
    
    //TODO when player dies
    public void die() {}
    
    public void completeQuest(Quest quest) {
	questsInProgress.remove(quest);     //Removes the quest from in progress
	questsCompleted.add(quest);         //Adds the quest to completed
	quest.completeQuest();
    }
    
    public void beginQuest(Quest quest) {   
	questsAvailable.remove(quest);      //Removes the quest from available
	questsInProgress.add(quest);        //Adds the quest to in progress
    }
    
    public void newQuest(Quest quest) {
	questsAvailable.add(quest);         //Adds the quest to available
    }
    
    private void addInventory(int id) {
	inventory[id]++;    //Increases the amount of the item in inventory
	engine.addMessage("Picked up " + Item.getName(id), 60, true);   //Adds a message describing the item added
    }
    
    private void removeItem(int id) {
	inventory[id]--;    //Decreases the amount of the item in inventory
	
	if (isItemEquipped(id) && inventory[id] == 0) //If the item is equipped
	    deEquipItem(id);    //De-equips the item
    }
    
    private void addWallet(int add) {
	wallet += add;      //Increases the amount of money in wallet
	engine.addMessage("Picked up " + add + " coins", 60, true); //Adds a message for the number of coins picked up
    }
    
    public void addPoison(int time) {
	this.poisonTimer += time;   //Increases the time the player is poisoned for
    }
    
    public void playDamageSound() {
	//Plays a damage sound when the player is hurt
	Sound.playerHurt.play();
    }
    public int getOldX() {
	return oldX;        //Returns the player's old X
    }
    public int getOldY() {
	return oldY;        //Returns the player's old Y
    }
    public int getExperience() {
	return experience;  //Returns the player's experience
    }
    public int getFullExperience() {
	return fullExperience;  //Returns the player's experience needed to level up
    }
    public int getLevel() {
	return level;       //Returns the player's level
    }
    public int getStrength() { 
	return strengthStat;    //Returns the player's strength
    }
    public int getDexterity() {
	return dexterityStat;   //Returns the player's dexterity
    }
    public int getIntelligence() {
	return intelligenceStat;    //Returns the player's intelligence
    }
    public int getLuck() {
	return luckStat;    //Returns the player's luck
    }
    public int getTraitPoints() {
	return traitPoints; //Returns the player's number of trait points
    }
    public int getXDrawOffset() {
	return xDrawOffset; //Returns the x Draw Offset
    }
    public int getYDrawOffset() {
	return yDrawOffset; //Returns the y Draw Offset
    }
    public int getWallet() {
	return wallet;      //Returns the player's wallet
    }
    public int[] getInventory() {   
	return inventory;   //Returns the player's inventory
    }
    public boolean[] getAbilities() {
	return abilities;   //Returns the player's abilities
    }
    public byte[] getAbilityLevels() {
	return abilityLevels;   //Returns the player's ability levels
    }
    public int getItemCount(int id) {
	return inventory[id];   //Returns the amount of the item given
    }
    private int getClassValue() {
	//Returns the player's class level
	if (level<10)
	    return 0;
	else if (level<30)
	    return 1;
	else
	    return 2;
    }
    public int[] getAbilityPoints() {
	return abilityPoints;   //Returns the player's ability points
    }
    int getPoisonTimer() {
	return poisonTimer; //Returns the player's poison timer
    }
    int getHealerTimer() {
	return healerTimer; //Returns the player's healer timer
    }
    int getRevMedTimer() {
	return revMedTimer; //Returns the player's revitalize/meditate timer
    }
    int getManaTimer() {
	return manaTimer;   //Returns the player's mana timer
    }
    int getWindTimer() {
	return windTimer;
    }
    int[] getAbilityTimers() {
	return abilityTimer;    //Returns the player's ability timers
    }
    int[] getJewelryTimer() {
	return jewelryTimer;    //Returns the player's jewelry timers
    }
    
    public boolean isItemEquipped(int id) {    //Checks if the item is equipped
	if (id < 160 || id > 236)   //If the item is not equippable
	    return false;           //Returns false
	
	return equipped[id - 160];  //Returns if the item is equipped or not
    }

    public Quest[] getQuestsAvailable() {
	updateQuests = true;                                    //Sets quest menu to update
	Quest[] quests = new Quest[questsAvailable.size()];     //Creates a new quest array
	questsAvailable.toArray(quests);                        //Loads the arraylist into the array
	return quests;                                          //Returns the available quests
    }
    
    public Quest[] getQuestsInProgress() {
	updateQuests = true;                                    //Sets quest menu to update
	Quest[] quests = new Quest[questsInProgress.size()];    //Creates a new quest array
	questsInProgress.toArray(quests);                       //Loads the arraylist into the array
	return quests;                                          //Returns the in progress quests
    }
    
    public Quest[] getQuestsCompleted() {
	updateQuests = true;                                    //Sets quest menu to update
	Quest[] quests = new Quest[questsCompleted.size()];     //Creates a new quest array
	questsCompleted.toArray(quests);                        //Loads the arraylist into the array
	return quests;                                          //Returns the completed quests
    }
    
    public String[] getQuestNames(int index) {
	//Gets the array list to loop through depending on parameter
	int size = (index == 0) ? questsAvailable.size():((index == 1) ? questsInProgress.size():questsCompleted.size());
	String[] names = new String[size];  //Creates a string array for the names
	
	
	//Loops through the arraylist
	for (int i = 0; (i<questsAvailable.size() && index == 0) || (i<questsInProgress.size() && index == 1) || (i<questsCompleted.size() && index == 2); i++) {
	    switch(index) {
		//Adds the name of the quest to the array
		case 0: names[i] = questsAvailable.get(i).getName(); break;
		case 1: names[i] = questsInProgress.get(i).getName(); break;
		case 2: names[i] = questsCompleted.get(i).getName(); break;
	    }
	}
	
	return names;   //Returns the string array
    }
    
    public String[][] getQuestItems(int index) {
	//Counts how many total items there are for the current array list
	int size = 0;
	for (int i = 0; (i<questsAvailable.size() && index == 0) || (i<questsInProgress.size() && index == 1) || (i<questsCompleted.size() && index == 2); i++) {
	    switch(index) {
		case 0: size += questsAvailable.get(i).getRequiredItems().length; break;
		case 1: size += questsInProgress.get(i).getRequiredItems().length; break;
		case 2: size += questsCompleted.get(i).getRequiredItems().length; break;
	    }
	}
	
	if (size == 0)  //If there are none
	    return new String[][]{{}};  //Returns an empty array

	String[][] names = new String[size][];  //Creates a new 2D array - first dimension is the quest, second is the items
	for (int i = 0; (i<questsAvailable.size() && index == 0) || (i<questsInProgress.size() && index == 1) || (i<questsCompleted.size() && index == 2); i++) {
	    //Loops through each quest and gets an array of the items required
	    int items[];
	    switch(index) {
		case 0: 
		    items = questsAvailable.get(i).getRequiredItems();
		    names[i] = new String[items.length];
		    for (int j = 0; j<items.length; j++)
			names[i][j] = Item.getName(items[j]);
		    break;
		case 1:
		    items = questsInProgress.get(i).getRequiredItems();
		    names[i] = new String[items.length];
		    for (int j = 0; j<items.length; j++)
			names[i][j] = Item.getName(items[j]);
		    break;
		case 2:
		    items = questsCompleted.get(i).getRequiredItems();
		    names[i] = new String[items.length];
		    for (int j = 0; j<items.length; j++)
			names[i][j] = Item.getName(items[j]);
		    break;
	    }
	}
	
	return names;   //Returns the array of strings
    }
    
    public boolean shouldUpdateQuests() {
	return updateQuests;                //Whether or not quest menu should update
    }
    
    public void questsUpdated() {
	updateQuests = false;               //Called when quest menu has been updated
    }
    
    static byte[] BEGINNING_ABILITY_LEVELS = {0,1,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
    //static byte[] BEGINNING_ABILITY_LEVELS = {1,1,1,1,1,1,1,1,1,1,1,1,1,1,1,1};
    public static final boolean[] BEGINNER_ABILITIES = {true, true, true, false, false, false, false, false, false, false, false, false, false, false, false, false};
    public static final boolean[] MAGE_ABILITIES = {true, true, true, true, true, true, true, true,false, false, false, false, false, false, false, false, false};
    public static final boolean[] ARCHMAGE_ABILITIES = {true, true, true, true, true, true, true, true, true, true, true, true, true, true, true, true};
}
