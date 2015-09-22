package charles.entity;

import charles.game.Engine;

public abstract class NPC extends LivingEntity {

    public NPC(Engine engine, int x, int y) {
	//Sends these values to the super class
	super(engine, x, y, 32, 32, 1000000, 1000000, 1, 0xffffd800, 1000000, 1000000);
    }
    
    public void knockback(int source){} //NPC cannot be knocked back, but it must implement this method
    
    //Returns the name of the NPC depending on the given id
    public static String getName(int id) {
	switch(id) {
	    case 1: return "Princess Charlotte";
	    case 2: return "King Chaz";
	    default: return "";
	}
    }
    
    //Returns the gender of the NPC using "his" or "her" depending on the id
    public static String getGender_HisHer(int id, boolean caps) {
	String s = "";
	switch(id) {
	    //default: return "";
	}
	
	//If the string should be capitalized
	if (caps)   //Returns the name with the first letter capitalized
	    return s.substring(0, 1).toUpperCase() + s.substring(1);
	else
	    return s;
    }
    
    //Returns the gender of the NPC using "he" or "she" depending on the id
    public static String getGender_HeShe(int id, boolean caps) {
	String s = "";
	switch(id) {
	    //default: return "";
	}
	
	//If the string should be capitalized
	if (caps)       //Returns the name with the first letter capitalized
	    return s.substring(0, 1).toUpperCase() + s.substring(1);
	else
	    return s; 
    }
}
