package charles.entity;

class Quest {

    private int[] requiredItems;
    private int[] requiredAmounts;
    private int NPC_id;
    private boolean complete;
    private int type;

    public Quest(int[] requiredItems, int[] requiredAmounts, int NPC_id) {  
	this.requiredItems = requiredItems;     //Saves the required items
	this.requiredAmounts = requiredAmounts; //Saves the required amounts of each item
	this.NPC_id = NPC_id;                   //Saves the NPC's id
	
	if (requiredItems.length == 1)                  //Randomizes the quest's description depending on how many items there are
	    this.type = (int)(Math.random() * 5 + 10);
	else
	    this.type = (int)(Math.random() * 10);
    }
    
    int[] getRequiredItems() {
	return requiredItems;   //Returns the required items
    }
    int[] getRequiredAmounts() {
	return requiredAmounts; //Returns the required amounts of each item
    }
    boolean isCompleted() {
	return complete;    //Returns if the quest was completed
    }
    public void completeQuest() {
	complete = true;    //Sets the quest to completed
    }
    int getNPC_id() {
	return NPC_id;      //Returns the NPC's id
    }
    
    public String getName() {
	switch(type) {
	    //Returns the name of the quest
	    case 0: return NPC.getName(NPC_id) + "'s lost items";
	    case 1: return NPC.getName(NPC_id) + "'s research";
	    case 2: return NPC.getName(NPC_id) + "'s debt";
	    case 3: return NPC.getName(NPC_id) + "'s curious items";
	    case 4: return NPC.getName(NPC_id) + "'s cookes!";
	    case 5: return NPC.getName(NPC_id) + "'s new kitten";
	    case 6: return NPC.getName(NPC_id) + "'s in love!";
	    case 7: return NPC.getName(NPC_id) + " is bored";
	    case 8: return NPC.getName(NPC_id) + "'s home repairs";
	    case 9: return NPC.getName(NPC_id) + "'s journey";
	    case 10: return NPC.getName(NPC_id) + "'s robbery!";
	    case 11: return NPC.getName(NPC_id) + "'s collection";
	    case 12: return NPC.getName(NPC_id) + "'s magical item?";
	    case 13: return NPC.getName(NPC_id) + "'s investment";
	    case 14: return NPC.getName(NPC_id) + "'s addiction";
	    default: return null;
	}
    }
    
    public String[] getDescription(boolean notStarted) {
	if (notStarted) //If the quest has not been started
	    return new String[]{"I wonder if " + NPC.getName(NPC_id) + " could use some help..."};  //Sends a simple message informing user of what to do to start quest
    
	String s[] = new String[requiredItems.length + 1];
	
	
	//Returns an array of strings containing a randomized description of the quest
	switch(type) {
	    case 0:
		s[0] = NPC.getName(NPC_id) + " needs your help! " + NPC.getGender_HeShe(NPC_id, true) + "'s lost some items and can't get them back from the monster who took them!";
		s[0] += " It's looking like " + NPC.getGender_HeShe(NPC_id, false) + "'s lost the following items:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 1:
		s[0] = NPC.getName(NPC_id) + " needs some help with " + NPC.getGender_HisHer(NPC_id, false) + " research! You've offered to help so see if you can find some items they've asked for.";
		s[0] += " For " + NPC.getGender_HisHer(NPC_id, false) + " research, " + NPC.getName(NPC_id) + " is going to need:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 2:
		s[0] = "Well, " + NPC.getName(NPC_id) + " has gotten into some trouble with the wrong crowd. " + NPC.getGender_HeShe(NPC_id, true) + " needs to come up with some items fast or face the consequences.";
		s[0] += " Help them by finding these items so they can keep their kneecaps:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 3:
		s[0] = NPC.getName(NPC_id) + " needs some stuff. " + NPC.getGender_HeShe(NPC_id, true) + " won't tell you much of why but I'm sure they'll appreciate your efforts?";
		s[0] += " Seems a bit shady, but you might as well help out by getting the following items:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 4:
		s[0] = NPC.getName(NPC_id) + " is making cookies! Sounds delicious, doesn't it? And " + NPC.getGender_HeShe(NPC_id, false) + " has asked for your help in getting some ingredients.";
		s[0] += " Okay, here's what they'll need... Oh. Cookies... with these? Interesting recipe...";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 5:
		s[0] = NPC.getName(NPC_id) + " has a new kitten! Aww, how precious. And " + NPC.getGender_HeShe(NPC_id, false) + " wants to get it some new toys, but needs help finding something to make them with.";
		s[0] += " " + NPC.getName(NPC_id) + " has asked that you please get them the following items:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 6:
		s[0] = NPC.getName(NPC_id) + " has found love and needs your help! " + NPC.getGender_HeShe(NPC_id, true) + " wants to show their affections with a gift for their loved one! How sweet.";
		s[0] += " Well, isn't " + NPC.getName(NPC_id) + " romantic? They need some... Oh well, I guess these are romantic:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 7:
		s[0] = NPC.getName(NPC_id) + " is pretty bored. Why don't you bring them something to entertain themselves with?";
		s[0] += " A couple of these will keep them occupied:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 8:
		s[0] = NPC.getName(NPC_id) + " is doing some repairs around the house and could use your help finding some of the items they need.";
		s[0] += " Not sure exactly what they're planning on fixing with these:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 9:
		s[0] = NPC.getName(NPC_id) + " is on a journey to self discovery and needs to offer up some items to some different deities, see if anything happens.";
		s[0] += " As a god, I wouldn't really be interested in this, but to each his own:";
		for (int i = 0; i<requiredItems.length; i++)
		    s[i+1] = (i+1) + ". " + requiredAmounts[i] + " " + Item.getName(requiredItems[i]);
		break;
	    case 10:
		s[0] = "Uh oh. Looks like " + NPC.getName(NPC_id) + " is in a bit of trouble! Somebody's stolen " + NPC.getGender_HisHer(NPC_id, false) + " " + Item.getName(requiredItems[0]) + "! Hurry and get it back.";
		s[0] += " It's looking like the following item was stolen:";
		s[1] = "1. " + requiredAmounts[0] + " " + Item.getName(requiredItems[0]);
		break;
	    case 11:
		s[0] = NPC.getName(NPC_id) + " is an avid collector of strange items. This week, " + NPC.getGender_HeShe(NPC_id, false) + "'s set sights on collecting some " + Item.getName(requiredItems[0]) + ".";
		s[0] += " So, be a dear and collect some items for them, 'kay?";
		s[1] = "1. " + requiredAmounts[0] + " " + Item.getName(requiredItems[0]);
		break;
	    case 12:
		s[0] = NPC.getName(NPC_id) + " seems to be convinced that a " + Item.getName(requiredItems[0]) + " has some magical properties? Not sure where they got their information from, but you might as well help them get some.";
		s[0] += " Anyway, if it wan't obvious enough, they'll be needing:";
		s[1] = "1. " + requiredAmounts[0] + " " + Item.getName(requiredItems[0]);
		break;
	    case 13:
		s[0] = NPC.getName(NPC_id) + " has been dominating the stock markets and seems to think " + NPC.getGender_HeShe(NPC_id, false) + " knows the next big thing. Help out and maybe you'll get a cut of the profits.";
		s[0] += " Sell " + Item.getName(requiredItems[0]) + "! Here's what's hot:";
		s[1] = "1. " + requiredAmounts[0] + " " + Item.getName(requiredItems[0]);
		break;
	    case 14:
		s[0] = NPC.getName(NPC_id) + " has been abusing " + Item.getName(requiredItems[0]) + ". It's so sad, but there doesn't seem to be anything you can do. So, you might as well help feed " + NPC.getGender_HisHer(NPC_id, false) + " habit!";
		s[0] = "They're going to need some of these before they go into withdrawal:";
		s[1] = "1. " + requiredAmounts[0] + " " + Item.getName(requiredItems[0]);
		break;
	    default: throw new RuntimeException("Quest type does not exist: " + type);
	}
	
	return s;
    }
}
