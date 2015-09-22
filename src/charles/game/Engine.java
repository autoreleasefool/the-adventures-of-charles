/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 15, 2012
 *
 * Application: Tiny Town
 * Class:       Gameplay
 *
 * Purpose:     The class which handles most gameplay
 *
 **/
package charles.game;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.AlphaComposite;

import charles.game.GameCanvas;
import charles.game.GameConstants;
import charles.game.InputHandler;
import charles.game.OptionManager;
import charles.resources.Art;
import charles.resources.FontRenderer;
import charles.map.*;
import charles.map.tiled.*;
import charles.map.side.*;
import charles.menu.*;
import charles.entity.*;

public class Engine {

    private GameCanvas gc;
    private boolean initComplete = false;

    private String[] messages = new String[GameConstants.MESSAGES_MAX];
    private int[] messageRemoveDelayStart = new int[GameConstants.MESSAGES_MAX];
    private int[] messageRemoveDelay = new int[GameConstants.MESSAGES_MAX];
    private int messagesInQueue = 0;
    private BufferedImage imgMessages;
    
    private String locationMessage;
    private int locationMessageRemoveDelay = 0;
    int locationChangeTimer = 0;
    private boolean messageLoaded = false;

    private Map map;
    private HudMenu hud;
    private GameMenu[] menus = new GameMenu[5];
    private Player player;
    private KeyConfiguration keyconfig;
    private SaveGame save;
    
    public Engine(GameCanvas gc) {
	this.gc = gc;   //Saves this object in the class variable
    }
    
    public void init() {
	OptionManager.setPlayerStyle(0);    //Sets the player to the default style
    
	setMap(new MainCity(this));             //Sets the starting map
	player = new PlayerOH(this, 220, 180);  //Creates a new player
	hud = new HudMenu(this);                //Initializes the HudMenu
	
	//Initializes each GameMenu
	menus[0] = new TraitsMenu(gc.getGame().getDesktop(), gc.getGame().getInternalFrame());
	menus[1] = new MiniMapMenu(gc.getGame().getDesktop(), gc.getGame().getInternalFrame());
	menus[2] = new CustomizeCharacterMenu(gc.getGame().getDesktop(), gc.getGame().getInternalFrame());
	menus[3] = new InventoryMenu(gc.getGame().getDesktop(), gc.getGame().getInternalFrame());
	menus[4] = new QuestMenu(gc.getGame().getDesktop(), gc.getGame().getInternalFrame());
	
	keyconfig = new KeyConfiguration(gc.getGame().getDesktop());
	save = new SaveGame(gc.getGame().getDesktop(), this);
	
	//Resets the location of each GameMenu
	for (int i = 0; i<menus.length; i++)
	    menus[i].resetLocation();
	    
	//Allows other methods to continue
	initComplete = true;
    }
    
    public void loadGame(String[] loaded) {
	if (loaded[loaded.length - 1].equals("Owl City")) {
	    setMap(new MainCity(this));
	    player = new PlayerOH(this, loaded);
	} else if (loaded[loaded.length - 1].equals("Wilderness")) {
	    setMap(new Wilderness(this));
	    player = new Player2D(this, loaded);
	} else if (loaded[loaded.length - 1].equals("Charles' House")) {
	    setMap(new PlainHouse(this, "Charles' House"));
	    player = new PlayerOH(this, loaded);
	}
    }
    
    public KeyConfiguration getKeyConfig() {
	return keyconfig;       //Returns the key config menu
    } 
    public SaveGame getSaveGame() {
	return save;
    }
    
    public void tick(InputHandler input) {
	if (!initComplete) return;  //If the init method has not completed, this method exits

	if (input.keys[GameConstants.KEY_F12].clicked) {
	    save.openMenu();
	}
	//if (!gamePaused) {          //If the game is not paused
	    hud.tick(input);        //Updates the hud menu

	    if (map.isTiled())          //Checks if the map is tiled
		tick_overhead(input);   //Updates overhead components
	    else                        //If the map is side
		tick_2D(input);         //Updates side components
	    
	    map.tick();                 //Updates the map
	    
	    //If there are any messages to be printed or there is a location message
	    if (messagesInQueue > 0 || locationMessageRemoveDelay > 0) {
		loadMessages();         //Loads the messages
		messageLoaded = true;   //Sets messages to draw
	    } else {                    //If there are no messages
		messageLoaded = false;  //Sets the messages to not draw
	    }
	    
	    //Updates each meny
	    for (int i = 0; i<menus.length; i++)
		menus[i].updateComponents(this);
	//}
    }
    
    private void tick_overhead(InputHandler input) {
	
	if (locationChangeTimer > 0)    //If the location timer is greater than 0
	    locationChangeTimer--;      //Decreases the location timer
	
	if (input.keys[GameConstants.KEY_ENTER].clicked && locationChangeTimer == 0) {                  //If the enter key is pressed
	    if (player.checkForDoorway(map.getLayout(), map.getDecor(), true)) {                              //Checks if the player is on a doorway
		map.enterDoorway(player.getX(), player.getY(), player.getWidth(), player.getHeight());  //Enters the doorway
		locationChangeTimer = 30;                                                               //Sets the location change timer to 1 second
		clearMessages();                                                                        //Clears the messages on the screen
		addLocationMessage();                                                                   //Adds the location message of the new map
	    }
	}
	
	player.updateInput(input);  //Updates the input
	player.move(map);           //Moves the player
	player.tick();              //Updates the player
    }
    
    private void tick_2D(InputHandler input) {

	if (locationChangeTimer > 0)        //If the location timer is greater than 0
	    locationChangeTimer--;          //Decreases the location timer
	
	if (input.keys[GameConstants.KEY_ENTER].clicked && locationChangeTimer == 0) {  //If the enter key is pressed
	    if (player.checkForDoorway(map.getLayout(), false, false)) {                       //Checks if the player is on a doorway
		map.enterDoorway(player.getX(), player.getY(), player.getWidth(), player.getHeight());  //Enters the doorway
		locationChangeTimer = 30;   //Sets the location change timer to 1 second
		clearMessages();            //Clears the messages on the screen
		addLocationMessage();       //Adds the location message of the new map
	    }
	}

	player.updateInput(input);  //Updates user input
	player.move(map);           //Moves the player
	player.tick();              //Ticks the player
    }
    
    public void render(Graphics g) {
	if (!initComplete) return;  //If initialisation is not complete, this method exits
	
	map.renderBase(g);      //Renders the base of the map
	map.renderEntities(g);  //Renders the entities on the map
	player.render(g);       //Renders the player
	map.renderAbilities(g); //Renders the abilities
	map.renderParticles(g); //Renders the particles
	map.renderOverlay(g);   //Renders the map overlay

	if (messageLoaded)      //If there are any messages to display
	    g.drawImage(imgMessages, 0, 0, null);   //Renders the messages
	map.renderDamageMessages(g);    //Renders the damage message
	
	hud.render(g);  //Renders the heads-up display
    }
    
    //Moves all menus by the specified amount if it is visible
    public void updateMenuPositions(int xShift, int yShift) {
	for (int i = 0; i<menus.length; i++) {
	    if (menus[i] != null) {
		if (menus[i].isVisible())
		    menus[i].shift(xShift, yShift);
	    }
	}
    }
    
    //Opens the specified menu
    public void openMenu(int index) {
	if (OptionManager.QUICK_MENUS)
	    menus[index].quickOpen();
	else
	    menus[index].openDialog();
    }
    
    //Closes the specified menu
    public void closeMenu(int index) {
	if (OptionManager.QUICK_MENUS)
	    menus[index].quickClose();
	else
	    menus[index].closeDialog();
    }
    
    //Closes all menus
    public void closeAllMenus() {
	for (int i = 0; i<menus.length; i++)
	    menus[i].closeDialog();
    }
    
    //Closes all menus using the quick method
    public void closeAllMenusQuick() {
	for (int i = 0; i<menus.length; i++)
	    menus[i].quickClose();
    }
    
    //Adds a location message to the screen with a set delay
    public void addLocationMessage(String name) {
	locationMessage = name;
	locationMessageRemoveDelay = 70;
    }
    
    //Adds the name of the current map as a location message
    public void addLocationMessage() {
	addLocationMessage(map.getName());
    }
    
    //Removes the current location message
    public void removeLocationMessage() {
	locationMessage = null;
    }
    
    
    //Adds a regular message to the screen
    public void addMessage(String str, int delay, boolean repeatMessage) {
	int messageSpot = -1;
	for (int i = 0; i<messages.length;i++) {    //Loops through each message spot
	    if (messages[i] != null) {              //If the message is full
		if (messages[i].equals(str) && !repeatMessage)  //If the messages are the same and non-repeatable
		    return;                         //The method exits
	    }
	    if (messages[i] == null && messageSpot == -1)   //If the message is null and no spot has been found yet
		messageSpot = i;                            //This is saved as a spot
	}
	
	if (messageSpot != -1) {    //If a spot was found
	    messages[messageSpot] = str;    //Saves the message
	    messageRemoveDelay[messageSpot] = delay;    //Saves the delay
	    messageRemoveDelayStart[messageSpot] = delay;   //Saves initial length of the delay
	    messagesInQueue++;      //Increases number of messages in the queue
	} else {    //If there were no empty messages
	    for (int i = 0; i < messages.length - 1; i++) {     //All of the messages are moved up
		messages[i] = messages[i + 1];
		messageRemoveDelay[i] = messageRemoveDelay[i + 1];
		messageRemoveDelayStart[i] = messageRemoveDelayStart[i + 1];
	    }
	    
	    //The final spot, now empty, is filled with the new message
	    messages[GameConstants.MESSAGES_MAX - 1] = str;
	    messageRemoveDelay[GameConstants.MESSAGES_MAX - 1] = delay;
	    messageRemoveDelayStart[GameConstants.MESSAGES_MAX - 1] = delay;
	}
    }
    
    public void removeMessage(int index) {
	//Removes the message at the specified index
	
	for (int i = index; i<messages.length - 1;i++) {    //Loops through each message afterwards and moves it up
	    messages[i] = messages[i + 1];
	    messageRemoveDelay[i] = messageRemoveDelay[i + 1];
	}
	
	//The final message is set to null and removed
	messages[GameConstants.MESSAGES_MAX - 1] = null;
	messageRemoveDelay[GameConstants.MESSAGES_MAX - 1] = 0;
	messageRemoveDelayStart[GameConstants.MESSAGES_MAX - 1] = 0;
	messagesInQueue--;  //One less message in the queue
    }
    
    public void clearMessages() {   //Clears all messages on the screen
	for (int i = 0; i<messages.length; i++) {   //Loops through each message and gets rid of its values
	    messages[i] = null;
	    messageRemoveDelay[i] = 0;
	    messageRemoveDelayStart[i] = 0;
	}
	
	messagesInQueue = 0;    //Clears messages in the queue
    }
    
    private void loadMessages() {
	//Creates a BufferedImage
	imgMessages = new BufferedImage(GameConstants.GRAPHICS_WIDTH, GameConstants.GRAPHICS_HEIGHT, BufferedImage.TYPE_INT_ARGB);
	Graphics g = (Graphics) imgMessages.getGraphics();   //Gets a graphics object from the BufferedImage
	Graphics2D g2d = (Graphics2D) g;    //Gets a Graphics2D object from the BufferedImage
    
	if (messagesInQueue > 0) {
	    int messageCount = 0;
	    for (int i = 0; i<messages.length;i++) {
		if (messages[i] != null) {
		    float alpha;
		    if (messageRemoveDelayStart[i] != 1)
			alpha = (messageRemoveDelay[i] > messageRemoveDelayStart[i] - 15) ? (messageRemoveDelay[i] - messageRemoveDelayStart[i]) / -15f:((messageRemoveDelay[i] < 15) ? messageRemoveDelay[i] / 15f:1f);
		    else
			alpha = 1f;
		    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, alpha));
		    g2d.drawImage(Art.messageBack_LEFT, GameConstants.GRAPHICS_WIDTH - (messages[i].length() * 6 + 10), 230 - 20 * messageCount, null);
		    g2d.drawImage(Art.messageBack_MIDDLE, GameConstants.GRAPHICS_WIDTH - (messages[i].length() * 6 + 10) + 2, 230 - 20 * messageCount, messages[i].length() * 6 + 6, 20, null);
		    g2d.drawImage(Art.messageBack_RIGHT, GameConstants.GRAPHICS_WIDTH - 2, 230 - 20 * messageCount, null);
		    FontRenderer.drawString(messages[i], GameConstants.GRAPHICS_WIDTH - (messages[i].length() * 6 + 5), 236 - 20 * messageCount++, 0xffffffff, 1, g2d);
		    //if (!gamePaused) {
			if (--messageRemoveDelay[i] <= 0)
			    removeMessage(i);
		    //}
		}
	    }
	}
	g2d = null;
	
	if (locationMessageRemoveDelay > 0) {
	    int y = 0;
	    if (locationMessageRemoveDelay > 50)
		y = (locationMessageRemoveDelay - 70) * -1 - 20;
	    else if (locationMessageRemoveDelay < 20)
		y = locationMessageRemoveDelay - 20;
	    else
		y = 0;
	    g.drawImage(Art.locationBack_LEFT, 20, y, null);
	    g.drawImage(Art.locationBack_MIDDLE, 22, y, locationMessage.length() * 6 + 6, 20, null);
	    g.drawImage(Art.locationBack_RIGHT, locationMessage.length() * 6 + 28, y, null);
	    FontRenderer.drawString(locationMessage, 25, y + 6, 0xff000000, 1, g);
	    if (--locationMessageRemoveDelay < 0)
		removeLocationMessage();
	}
	
	g.dispose();
    }

    public GameCanvas getCanvas() {
	return gc;      //Returns the canvas
    }
    public GameMenu getMenu(int index) {
	return menus[index];    //Returns the menu at the index
    }
    public Map getMap() {
	return map;     //Returns the current map
    }
    public TiledMap getTiledMap() {
	if (map.isTiled())
	    return (TiledMap) map;
	else
	    return null;
    }
    public void setMap(Map map) {
	if (this.map instanceof TiledMap && map instanceof SideMap)         //If the map type is changing from Tiled to Side
	    player = new Player2D(this, (PlayerOH)player);                  //Updates the player object
	else if (this.map instanceof SideMap && map instanceof TiledMap)    //If the map type is changing from Side to Tiled
	    player = new PlayerOH(this, (Player2D)player);                  //Updates the player object
	
	this.map = map; //Sets the new map
    }
    public Player getPlayer() {
	return player;  //Returns the player
    }
}
