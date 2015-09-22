package charles.menu;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.JDesktopPane;
import javax.swing.JInternalFrame;

import charles.game.GameConstants;
import charles.game.Engine;
import charles.map.MiniMap;

public class MiniMapMenu extends GameMenu {
    
    private static final int CANVAS_SCALE = 4;
    Canvas canvas;
    
    public MiniMapMenu(JDesktopPane desktop, JInternalFrame internal) {
	super(desktop, internal, "Mini map", 2);    //Sends these values to the super class
	canvas = new Canvas();  //Creates a new canvas and sets size
	canvas.setSize(new Dimension(100, 100));    
	
	//Adds the canvas to the center of the panel and packs it
	getContentPane().add(canvas, BorderLayout.CENTER);
	pack();
	
	//Adds this frame to the desktoppane
	addToDesktop();
    }
    
    public void updateComponents(Engine engine) {
	//Gets the image of the minimap
	MiniMap map = engine.getMap().getMiniMap();
	
	//Updates size of the canvas if the map has changed
	if (canvas.getWidth() / map.getMapImage().getWidth(null) != MiniMapMenu.CANVAS_SCALE || canvas.getHeight() / map.getMapImage().getHeight(null) != MiniMapMenu.CANVAS_SCALE)
	    updateCanvasSize(map);

	//Gets graphics of the canvas and draws minimap to it
	Graphics g = canvas.getGraphics();
	g.drawImage(map.getMapImage(), 0, 0, canvas.getWidth(), canvas.getHeight(), null);
	g.dispose();
    }
    
    private void updateCanvasSize(MiniMap map) {
	//Resizes the canvas and packs frame
	canvas.setSize(new Dimension(map.getMapImage().getWidth(null) * MiniMapMenu.CANVAS_SCALE, map.getMapImage().getHeight(null) * MiniMapMenu.CANVAS_SCALE));
	pack();
    }
    
    public void resetLocation() {
	//Sets menu to the its default, hidden location
	setLocation(internal.getX() + internal.getWidth() - getWidth(), internal.getY());
    }
    
    public void actionPerformed(ActionEvent ae) {
	super.actionPerformed(ae);
    }
    
    public byte[] getConflictingMenus() {
	return new byte[]{GameConstants.MENU_INVENTORY_ID}; //Closes these menus when this one opens
    }
}
