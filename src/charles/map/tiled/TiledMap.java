/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     
 *
 * Application: Tiny Town
 * Class:       October 18, 2012
 *
 * Purpose:     Contains general methods and values used to create overhead levels for player and entities to interact with
 *
 **/
package charles.map.tiled;

import java.awt.Rectangle;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import charles.game.GameConstants;
import charles.game.MapConstants;
import charles.game.Engine;
import charles.entity.*;
import charles.resources.Art;
import charles.map.Map;
import charles.resources.FontRenderer;

public abstract class TiledMap extends Map {

    public TiledMap(String name, int columns, int rows, int[][][] layout, boolean decor, Engine engine) {
	//Sends these values to the super class
	super(name, columns, rows, layout, true, decor, engine);
	
	//Creates an image for the map
	BufferedImage[] image = new BufferedImage[1];
	image[0] = Art.loadTiledMap(columns, rows, layout, decor);  //Loads the image of the map
	setImage(image);    //Sets the map's image
    }
}
