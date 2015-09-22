/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 3, 2012
 *
 * Application: Tiny Town
 * Class:       Art
 *
 * Purpose:     To load and store images
 *
 **/

package charles.resources;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.IOException;

import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.game.OptionManager;
import charles.entity.Item;

public class Art {

    public static final BufferedImage font = loadBufferedImage("/charles/_images/font.png");
    public static final BufferedImage terrainTiled = loadBufferedImage("/charles/_images/terrain/terrain_tiled.png");
    public static final BufferedImage decorTiled = loadBufferedImage("/charles/_images/terrain/decor_tiled.png");
    public static final BufferedImage terrain2D = loadBufferedImage("/charles/_images/terrain/terrain_side.png");;
    public static final BufferedImage minimapTerrain_tiled = loadBufferedImage("/charles/_images/terrain/minimap_terrain_tiled.png");
    public static final BufferedImage minimapTerrain_side = loadBufferedImage("/charles/_images/terrain/minimap_terrain_side.png");
    public static final BufferedImage minimapDecor = loadBufferedImage("/charles/_images/terrain/minimap_decor.png");
    public static final BufferedImage hudComp = loadBufferedImage("/charles/_images/GUI/hud.png");
    public static final BufferedImage charSide_Base = loadBufferedImage("/charles/_images/sprites/side/character.png");
    public static final BufferedImage charTiled_Base = loadBufferedImage("/charles/_images/sprites/tile/character.png");
    public static final BufferedImage enemySide = loadBufferedImage("/charles/_images/sprites/side/enemy.png");
    public static final BufferedImage enemySide2 = loadBufferedImage("/charles/_images/sprites/side/enemy2.png");
    public static final BufferedImage enemySide3 = loadBufferedImage("/charles/_images/sprites/side/enemy3.png");
    public static final BufferedImage enemyDragon = loadBufferedImage("/charles/_images/sprites/side/dragon.png");
    public static final BufferedImage abilitySheet = loadBufferedImage("/charles/_images/sprites/side/ability.png");
    public static final BufferedImage charSide = new BufferedImage(charSide_Base.getWidth(), charSide_Base.getHeight(), BufferedImage.TYPE_INT_ARGB);
    public static final BufferedImage charTiled = new BufferedImage(charTiled_Base.getWidth(), charTiled_Base.getHeight(), BufferedImage.TYPE_INT_ARGB);
    public static final BufferedImage itemSheet = loadBufferedImage("/charles/_images/sprites/items.png");
    public static final BufferedImage[] itemNames = generateItemNames();
    
    public static final Image messageBack_LEFT = loadImage("/charles/_images/GUI/messages/message_left.png");
    public static final Image messageBack_MIDDLE = loadImage("/charles/_images/GUI/messages/message_middle.png");
    public static final Image messageBack_RIGHT = loadImage("/charles/_images/GUI/messages/message_right.png");
    public static final Image locationBack_LEFT = loadImage("/charles/_images/GUI/messages/location_left.png");
    public static final Image locationBack_MIDDLE = loadImage("/charles/_images/GUI/messages/location_middle.png");
    public static final Image locationBack_RIGHT = loadImage("/charles/_images/GUI/messages/location_right.png");
    
    public static Image loadImage(String path) {
	Image img = null;  //Creates a new Image
	try {
	    //Tries to load the image from the specified path
	    img = ImageIO.read(Art.class.getResource(path));
	} catch(IOException io) {
	    io.printStackTrace();   //Prints an error if the image is not found
	}
	
	return img; //Returns the Image
    }
    
    public static BufferedImage loadBufferedImage(String path) {
	BufferedImage img = null;  //Creates a new BufferedImage
	try {
	    //Tries the load the image from the specified path
	    img = ImageIO.read(Art.class.getResource(path));
	} catch(IOException io) {
	    io.printStackTrace();   //Prints an error if the image is not found
	}

	return img; //Returns the BufferedImage
    }
    
    public static BufferedImage loadTiledMap (int columns, int rows, int[][][] layout, boolean useDecor) {
	BufferedImage tiles;    //Creates a BufferedImage
    
	if (useDecor)   //If the map is Decor
	    tiles = Art.decorTiled; //Loads the decor tiles
	else            //If the map is not Decor
	    tiles = Art.terrainTiled;   //Loads the terrain tiles
    
	//Creates a BufferedImage the size of the map
	BufferedImage image = new BufferedImage(columns * MapConstants.TILE_SIZE, rows * MapConstants.TILE_SIZE, BufferedImage.TYPE_INT_RGB);
	Graphics g = (Graphics) image.getGraphics(); //Creates a Graphics object from the BufferedImage
	for (int x = 0; x < rows; x++) {    //Loops through each row of the map
	    for (int y = 0; y<columns; y++) {   //Loops through each column of the map
		int xx = layout[0][x][y] % 20;  //Calculates the x position to draw the tile at
		int yy = layout[0][x][y] / 20;  //Calculates the y position to draw the tile at
		
		//Draws the tile at the specified location
		g.drawImage(tiles.getSubimage(xx * MapConstants.TILE_SIZE, yy * MapConstants.TILE_SIZE, MapConstants.TILE_SIZE, MapConstants.TILE_SIZE), y * MapConstants.TILE_SIZE, x * MapConstants.TILE_SIZE, null);
	    }
	}
	
	return image;   //Returns the completed map
    }
    
    public static BufferedImage[] loadSideMap (int columns, int rows, int[][][] layout) {
	BufferedImage tiles = Art.terrain2D;        //Loads the 2D terrain
	    
	BufferedImage image[] = new BufferedImage[layout.length];   //Creates an array of BufferedImages
	for (int z = 0; z<layout.length;z++) {  //Loops through each array of images
	    //Creates a new BufferedImage at the specified index
	    image[z] = new BufferedImage(columns * MapConstants.TILE_SIZE, rows * MapConstants.TILE_SIZE, BufferedImage.TYPE_INT_ARGB);
	    Graphics g = (Graphics) image[z].getGraphics();  //Creates a Graphics object from the BufferedImage
	
	    for (int x = 0; x<rows; x++) {          //Loops through each row of the map
		for (int y = 0; y<columns;y++) {    //Loops through each column of the map
		    int xx = layout[z][x][y] % 20;  //calculates the x position to draw at
		    int yy = layout[z][x][y] / 20;  //Calculates the y position to draw at
		    
		    //Draws the tile at the specified location
		    g.drawImage(tiles.getSubimage(xx * MapConstants.TILE_SIZE, yy * MapConstants.TILE_SIZE, MapConstants.TILE_SIZE, MapConstants.TILE_SIZE), y * MapConstants.TILE_SIZE, x * MapConstants.TILE_SIZE, null);
		}
	    }
	}
	
	return image;   //Returns the array of completed maps
    }
    
    public static BufferedImage[] generateItemNames() {
	BufferedImage[] image = new BufferedImage[237]; //Creates an array of BufferedImages
	for (int i = 0; i<image.length; i++) {          //Loops through each image
	    String name = Item.getName(i);              //Gets the name of the item
	    
	    if (!name.equals("")) {                     //If the name is not blank
		image[i] = new BufferedImage(name.length() * 12 + 2, 20, BufferedImage.TYPE_INT_ARGB);  //Creates a new BufferedImage
		Graphics g = (Graphics) image[i].getGraphics();                                         //Creates a graphics object from the image
		g.setColor(new Color(0, 0, 0, 0.75f));                                                  //Sets the color to a see-through black
		g.fillRect(0, 0, image[i].getWidth(), image[i].getHeight());                            //Fills the image with see-through black
		FontRenderer.drawString(name, 1, 1, 0xffffffff, 2, g);                                  //Draws the item's name to the image
		g.dispose();                                                                            //Disposes of the Graphics object
	    }
	}
	
	return image;   //Returns the BufferedImage array
    }
    
    public static Image loadMiniMap(int columns, int rows, int[][][] layout, boolean tiled, boolean useDecor) {
	BufferedImage tiles;    //Creates a BufferedImage

	if (tiled) {        //If the minimap is for a tiled map
	    if (useDecor)   //If the map is decor
		tiles = Art.minimapDecor;   //Loads the decor tiles
	    else            //If the map is not decor
		tiles = Art.minimapTerrain_tiled;   //Loads the terrain tiles
	} else {            //If the minimap is for a 2D map
	    tiles = Art.minimapTerrain_side;    //Loads the 2D terrain
	}
	    
	//Creates a bufferedimage and gets the graphics object
	BufferedImage image = new BufferedImage(columns, rows, BufferedImage.TYPE_INT_RGB);
	Graphics g = (Graphics) image.createGraphics();
	
	//Goes through each tile of the map and draws the tile to the location
	for (int x = 0; x < rows; x++) {
	    for (int y = 0; y<columns; y++) {
		int xx = layout[0][x][y] % 20;
		int yy = layout[0][x][y] / 20;
		
		g.drawImage(tiles.getSubimage(xx, yy, 1, 1), y, x, null);
	    }
	}
	
	//Returns the image
	return (Image)image;
    }
    
    public static void updatePlayerColors(int index) {
	//Loads the colors to draw over and the colors being drawn
	Color base = new Color(index * 20, index * 20, index * 20, 255);
	Color full = new Color(OptionManager.playerColors[index].getRed(),OptionManager.playerColors[index].getGreen(),OptionManager.playerColors[index].getBlue(),255);
	Color fade = new Color(OptionManager.playerColors[index].getRed(),OptionManager.playerColors[index].getGreen(),OptionManager.playerColors[index].getBlue(),170);
	//Loops through the images and checks for the corresponding RBGs in the base image, then draws the proper colours in a separate image
	for (int x = 0; x<192; x++) {
	    for (int y = 0; y<32; y++) {
		if (Art.charTiled_Base.getRGB(x, y) == base.getRGB()) {
		    Art.charTiled.setRGB(x, y, full.getRGB());
		    Art.charTiled.setRGB(x, y + 32, fade.getRGB());
		}
	    }
	}
	
	//Same as above, for the 2D character
	for (int x = 0; x<192; x++) {
	    for (int y = 0; y<64; y++) {
		if (Art.charSide_Base.getRGB(x, y) == base.getRGB()) {
		    Art.charSide.setRGB(x, y, full.getRGB());
		    Art.charSide.setRGB(x, y + 64, fade.getRGB());
		}
	    }
	}
	
	//Adds a black bowtie to the player if it is turned on
	if (OptionManager.PLAYER_BOWTIE && index == GameConstants.PLAYER_COLOR_UNDER_SHIRT) {
	    for (int i = 0; i<3; i++) {
		for (int j = 0; j<2; j++) {
		    Color c = new Color(0, 0, 0, (j == 0) ? 255:170);
		    Art.charTiled.setRGB(7 + i * 16, 16 + j * 32, c.getRGB());
		    Art.charTiled.setRGB(8 + i * 16, 16 + j * 32, c.getRGB());
		    Art.charTiled.setRGB(55 + i * 16, 15 + j * 32, c.getRGB());
		    Art.charTiled.setRGB(56 + i * 16, 15 + j * 32, c.getRGB());
		    Art.charTiled.setRGB(103 + i * 16, 15 + j * 32, c.getRGB());
		    Art.charTiled.setRGB(104 + i * 16, 15 + j * 32, c.getRGB());
		}
	    }
	    
	    for (int j = 0; j<2; j++) {
		Color c = new Color(0, 0, 0, (j == 0) ? 255:170);
		for (int k = 0; k<2; k++) {
		    for (int i = 0; i<4; i++) {
			Art.charSide.setRGB(6 + i * 16 + k * 98, 24 + 64 * j, c.getRGB());
			Art.charSide.setRGB(7 + i * 16 + k * 98, 24 + 64 * j, c.getRGB());
		    }
		    Art.charSide.setRGB(70 + k * 98, 23 + 64 * j, c.getRGB());
		    Art.charSide.setRGB(71 + k * 98, 23 + 64 * j, c.getRGB());
		    Art.charSide.setRGB(86 + k * 98, 31 + 64 * j, c.getRGB());
		    Art.charSide.setRGB(87 + k * 98, 31 + 64 * j, c.getRGB());
		}
	    }
	}
    }
}
