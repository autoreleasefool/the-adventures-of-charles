package charles.map;

import java.awt.Graphics;
import java.awt.Color;

import charles.game.MapConstants;

class Particle {

    private static final float PARTICLE_GRAVITY = 0.75f;

    Map map;

    float x, y;
    float dx, dy;
    float maxY;
    
    int col;
    int size;
    int safe;
    int bounces;
    
    boolean falling;
    
    public Particle(Map map, float x, float y, float dx, int col, int size) {
	//Saves the values for the particle
	this.map = map;
	this.x = x;
	this.y = y;
	this.dx = dx;
	this.dy = 0f;
	this.col = col;
	this.size = size;
	
	//Sets the particle to falling and ensure it will fall through it current block
	falling = true;
	safe = (int)((y + size) / MapConstants.TILE_SIZE);
	
	//Removes any particles which lie flat on the ground as they will not bounce
	if ((this.y) % 32 == 0)
	    map.removeParticle(this);
    }
    
    void render(Graphics g) {
	//Sets the color of the graphics object
	g.setColor(new Color(col, true));
	
	//Draws the particle to the screen
	g.fillRect((int)x, (int)y, size, size);
    }
    
    void tick() {
	//Moves the particle
	x += dx;
	y += dy;
	dy += Particle.PARTICLE_GRAVITY;
	
	//Keeps withing bounds of the map
	if (x + size > map.getMapWidth())
	    x = map.getMapWidth() - size;
	else if (x < 0)
	    x = 0;

	if (map.isTiled()) {
	    //Bounces at a maximum height on tiled maps up to 1 time then disappears
	    if (y >= maxY) {
		if (bounces > 0) {
		    map.removeParticle(this);
		    return;
		} else {
		    dy = -(dy - 1.5f);
		    bounces++;
		}
	    }
	} else {
	    //Checks if the pixel hits a block with a top
	    if (Map.tileHasTop((int)((y+size) / MapConstants.TILE_SIZE), (int)(x / MapConstants.TILE_SIZE), map.getLayout()) && dy > 0 && (int)((y + size) / MapConstants.TILE_SIZE) != safe) {
		int hitBlock = (int)((y + size) / MapConstants.TILE_SIZE);  //Saves the block which it has entered
		while ((int)((y + size) / MapConstants.TILE_SIZE) == hitBlock)  //Moves it up until it is out of that block
		    y -= 1;
		    
		    
		if (falling) falling = false;   //No longer falling
		if (bounces > 0) {
		    map.removeParticle(this);   //Bounces one then is removed from the map
		    return;
		} else {
		    if (dy > 5) //If it is falling quickly, randomizes it to a slower speed and bounces it
			dy= -(float)(Math.random() * (dy - 4) + 2);
		    else    //If it is not falling quickly, subtracts a small amount from its speed and bounces it
			dy = -(dy - 1.5f);
		    bounces++;  //Increases number of bounces occurred
		}
	    } else if (!Map.tileHasTop((int)((y + size + 1) / MapConstants.TILE_SIZE), (int)(x / MapConstants.TILE_SIZE), map.getLayout()) || falling) {
		//Stops particle from falling too fast
		if (dy > 10f)
		    dy = 10f;
		    
		if (!falling) falling = true;   //Sets particle to falling
		safe = (int)((y + size) / MapConstants.TILE_SIZE);  //Sets its current block as a safe falling zone
	    }
	}
    }
}
