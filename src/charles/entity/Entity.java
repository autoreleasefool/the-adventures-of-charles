/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 15, 2012
 *
 * Application: Tiny Town
 * Class:       Entity
 *
 * Purpose:     Contains the general values and methods used by all entities
 *
 **/
package charles.entity;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

import charles.game.Engine;
import charles.game.GameCanvas;
import charles.game.MapConstants;
import charles.resources.Art;
import charles.resources.Sound;

public abstract class Entity {

    protected Engine engine;

    protected int x, y;
    protected int dx, dy;
    protected int width, height;
    protected int direction;
    protected int frame = 0;
    
    public Entity(Engine engine, int x, int y, int width, int height) {
	//Stores the parameters this constructor is provided with
	this.engine = engine;
	this.x = x;
	this.y = y;
	this.width = width;
	this.height = height;
	
	//Sets the direction and movement to 0
	direction = 0;
	dx = 0;
	dy = 0;
    }

    public abstract void render(Graphics g);    //All entities must implement this method
    public abstract Image getImage();           //All entities must implement this method
    
    public void tick() {        //Updates the entity
	if (++frame == 10)      //Adds to its current frame and checks if it is equal to 10
	    frame = 0;          //If so, the frame is reset to 0
    }
    
    public Rectangle getRectangle() {               //Returns a rectangle from the entity's x, y, width and height
	return new Rectangle(x, y, width, height);
    }
    
    public boolean intersects(Entity other) {       //Used to check if this entity has collided with another
	return getRectangle().intersects(other.getRectangle()); //Uses method "intersects" from the rectangle to check if the enemies are touching
    }
    
    public int getX() {         //Returns the x location of this entity
	return x;
    }
    public int getY() {         //Returns the y location of this entity
	return y;
    }
    public int getWidth() {     //Returns the width of this entity
	return width;
    }
    public int getHeight() {    //Returns the height of this entity
	return height;
    }
    public void setDX(int dx) { //Sets the horizontal movement of this entity
	this.dx = dx;
    }
    public void setDY(int dy) { //Sets the vertical movement of this entity
	this.dy = dy;
    }
    public void setX(int x) {   //Sets the x location of this entity
	this.x = x;
    }
    public void setY(int y) {   //Sets the y location of this entity
	this.y = y;
    }
    public void setXTile(int x, int offset) {       //Calculates x of the tile to put this entity there
	setX(x * MapConstants.TILE_SIZE + offset);
    }
    public void setYTile(int y, int offset) {       //Calculates y of the tile to put this entity there
	setY(y * MapConstants.TILE_SIZE + offset);
    }
    public void setDirection(int direction) {
    this.direction = direction;
    }
}
