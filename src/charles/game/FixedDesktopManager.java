package charles.game;

import javax.swing.DefaultDesktopManager;
import javax.swing.JComponent; 

class FixedDesktopManager extends DefaultDesktopManager {

    CharlesGame cg;
    
    public FixedDesktopManager(CharlesGame cg) {
	this.cg = cg;   //Saves this object in a class variable
    }

    public void dragFrame(JComponent jc, int x, int y) {
	//If the frame being dragged is the main frame or the menus are popped out
	if (jc == cg.getInternalFrame() || jc == cg.gc.getEngine().getKeyConfig() || jc == cg.gc.getEngine().getSaveGame())
	    super.dragFrame(jc, x, y);  //The frame drags
	    
	//Otherwise, the menu frames cannot be dragged on their own
    }
}
