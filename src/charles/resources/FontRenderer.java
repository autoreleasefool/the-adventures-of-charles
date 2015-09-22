/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 3, 2012
 *
 * Application: Tiny Town
 * Class:       FontRenderer
 *
 * Purpose:     Used to draw strings to the screen with a personalized font
 *
 **/

package charles.resources;

import java.awt.Graphics;
import java.awt.image.BufferedImage;

import charles.game.GameCanvas;
import charles.game.GameConstants;

public class FontRenderer
{
    private static final int CHAR_WIDTH = 6;
    private static final int CHAR_HEIGHT = 8;

    private static BufferedImage font = Art.font;
    private static int col = 0xff000000;   //Current color of the font
    
    //The string of characters that are listed in font.png, in the order they are
    static String chars = " !\"#$%&'()*+,-./0123456789:;<=>?@ABCDEFGHIJKLMNOPQRSTUVWXYZ[\\]^_`abcdefghijklmnopqrstuvwxyz{|}~";
    
    public static void drawString(String str, int x, int y, int color, int size, Graphics g) {
	if (col != color) setFontColor(color);       //Calls the setFontColor method if the specified color is different
	
	for (int i = 0; i<str.length(); i++) {      //Loops through each character in the input string
	    int ch = chars.indexOf(str.charAt(i));  //Gets the position of the current character

	    if (ch<0) return;                       //If the character was not found, the method exits
	    
	    //There are 3 rows of 32 characters so the x will be calculated as how far from a multiple of 32 it is
	    //And the y is the row, which is found by dividing the position by 32
	    int xx = ch % 32;                       
	    int yy = ch / 32;
	    
	    //Draws a sub image of the font BufferedImage to the graphics object
	    g.drawImage(font.getSubimage(xx * CHAR_WIDTH, yy * CHAR_HEIGHT, CHAR_WIDTH, CHAR_HEIGHT), x + CHAR_WIDTH * i * size, y, CHAR_WIDTH * size, CHAR_HEIGHT * size, null);
	}
    }
    
    public static void drawVertCenteredString(String str, int y, int col, int size, Graphics g) {
	//Calculates the center of the given canvas and then subtracts half the width of the string being drawn
	int x = GameConstants.GRAPHICS_WIDTH / 2 - (str.length() * 6 * size) / 2;
	drawString(str, x, y, col, size, g);    //Calls the drawString method with the calculated x
    }
    
    public static void drawVertCenteredString(String str, int y, int xOffset, int yOffset, int col, int size, Graphics g) {
	//Calculates the center of the given canvas and then subtracts half the width of the string being drawn
	int x = GameConstants.GRAPHICS_WIDTH / 2 - (str.length() * 6 * size) / 2;
	drawString(str, x + xOffset, y + yOffset, col, size, g);    //Calls the drawString method with the calculated x
    }
    
    private static void setFontColor(int color) {       //Used to change the color of the BufferedImage
	col = color;                                    //Stores the color that the font is being changed to
	for (int x = 0;x<font.getWidth();x++) {         //Loops through each pixel of the font BufferedImage
	    for (int y = 0;y<font.getHeight();y++) {
		if (font.getRGB(x, y) != 0x00000000)    //If the RGB of the pixel is not transparent
		    font.setRGB(x, y, color);           //The RGB of the pixel is set to the new color
	    }
	}
    }
}
