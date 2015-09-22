/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 8, 2012
 *
 * Application: Tiny Town
 * Class:       GameConstants
 *
 * Purpose:     Globally available constants for consistency throughout program
 *
 **/

package charles.game;

import java.awt.GraphicsEnvironment;
import java.awt.DisplayMode;

import charles.resources.Art;

public interface GameConstants {

    /*
    This interface declares all public static final variables that the entire
    program can access so there is consistency in using these values throughout
    the program.

    The key constants are for determining which key was pressed and allows the use
    of a smaller, more contained array than using the KeyEvent constants would
    */

    //CANVAS
    public static final int CANVAS_WIDTH = 800;
    public static final int CANVAS_HEIGHT = 600;
    public static final int GRAPHICS_WIDTH = 400;
    public static final int GRAPHICS_HEIGHT = 300;
    public static final double CANVAS_SCALE = (double)CANVAS_HEIGHT / GRAPHICS_HEIGHT;
    
    //SCREEN
    public static final DisplayMode SCREEN_DISPLAY_MODE = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDisplayMode();
    
    //MESSAGES CONSTANTS
    public static final byte MESSAGES_MAX = 5;
    public static final byte MESSAGES_DAMAGE_MAX = 100;
    
    //IN-GAME MENU CONSTANTS
    public static final byte MENU_TRAITS_ID = 0;
    public static final byte MENU_MINIMAP_ID = 1;
    public static final byte MENU_CUSTOMIZE_ID = 2;
    public static final byte MENU_INVENTORY_ID = 3;
    public static final byte MENU_QUESTS_ID = 4;
    public static final byte MENU_INFO_ID = 5;
    public static final byte MENU_ABILITIES_ID = 6;
    public static final byte MENU_KEY_CONFIG_ID = 7;
    public static final byte MENU_OPTIONS_ID = 8;
    public static final byte MENU_WORLD_MAP_ID = 9;
    
    //ITEM CONSTANTS
    public static final byte ITEM_SIZE = 16;
    
    //ABILITY CONSTANTS
    public static final String[] ABILITY_NAMES = {"Call of the Wind", "Double Swipe", "Healer's Touch", "Magic Armor", "MP Guard", "Energy Bolt", "Shockwave", "Teleport", "Wind Blast", "Earthquake", "Blaze", "Flamethrower", "Revitalize", "Meditate", "Freeze"};
    public static final byte[] ABILITY_MAX_LEVEL = {3, 3, 3, 10, 10, 20, 20, 15, 15, 15, 15, 15, 15, 15, 15, 20};
    
    public static final int[][] ABILITY_MANA_COST = {
						/*Wind Call*/{10, 8, 6},
						/*Double Swipe*/{5, 6, 7},
						/*Healer's Touch*/{8, 10, 12},
						/*Magic Armor*/{15,17,19,21,23,25,27,29,31,33},
						/*MP Guard*/{11, 12, 13, 14, 15, 16, 17, 18, 19, 20},
						/*Energy Bolt*/{10, 10, 10, 10, 10, 10, 9, 9, 9, 9, 9, 9, 9, 8, 8, 8, 8, 8, 8, 8},
						/*Shockwave*/{14, 14, 14, 14, 14, 14, 17, 17, 17, 17, 17, 17, 17, 20, 20, 20, 20, 20, 20, 20},
						/*Teleport*/{30,29,28,27,26,25,24,23,22,21,20,19,18,17,15},
						/*Wind Blast*/{30,29,28,27,26,25,24,23,22,21,20,19,18,17,15},
						/*Earthquake*/{22,25,28,31,34,37,40,43,46,49,52,55,58,61,64},
						/*Rock Wall*/{30,32,34,36,38,40,42,44,46,48,50,52,54,56,58},
						/*Blaze*/{30,29,28,27,26,25,24,23,22,21,20,19,18,17,15},
						/*Flamethrower*/{30,29,28,27,26,25,24,23,22,21,20,19,18,17,15},
						/*Revitalize*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
						/*Meditate*/{0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},
						/*Freeze*/{80,78,76,74,72,70,68,66,64,62,60,58,56,54,52,50,48,46,44,42},
						};
    
    public static final byte ABILITY_WIND_CALL = 0;
    public static final byte ABILITY_DOUBLE_SWIPE = 1;
    public static final byte ABILITY_HEALER_TOUCH = 2;
    public static final byte ABILITY_MAGIC_ARMOR = 3;
    public static final byte ABILITY_MP_GUARD = 4;
    public static final byte ABILITY_ENERGY_BOLT = 5;
    public static final byte ABILITY_SHOCKWAVE = 6;
    public static final byte ABILITY_TELEPORT = 7;
    public static final byte ABILITY_WIND_BLAST = 8;
    public static final byte ABILITY_EARTHQUAKE = 9;
    public static final byte ABILITY_ROCK_WALL = 10;
    public static final byte ABILITY_BLAZE = 11;
    public static final byte ABILITY_FLAMETHROWER = 12;
    public static final byte ABILITY_REVITALIZE = 13;
    public static final byte ABILITY_MEDITATE = 14;
    public static final byte ABILITY_FREEZE = 15;
    
    //ENEMY CONSTANTS
    public static final int ENTITY_ATTACK_TIME = 30;
    public static final byte ENEMY_SLUG = 0;
    public static final byte ENEMY_FLY = 1;
    public static final byte ENEMY_SNAIL = 2;
    public static final byte ENEMY_SLIME = 3;
    public static final byte ENEMY_BEE = 4;
    public static final byte ENEMY_SPIDER = 5;
    public static final byte ENEMY_TURTLE = 6;
    public static final byte ENEMY_PENGUIN = 7;
    public static final byte ENEMY_PIG = 8;
    public static final byte ENEMY_BOAR = 9;
    public static final byte ENEMY_SNOWMAN = 10;
    public static final byte ENEMY_FOX = 11;
    public static final byte ENEMY_FOX_ARCTIC = 12;
    public static final byte ENEMY_BEAR = 13;
    public static final byte ENEMY_BEAR_POLAR = 14;
    public static final byte ENEMY_FAIRY = 15;
    public static final byte ENEMY_ALLIGATOR = 16;
    public static final byte ENEMY_ROBOT = 17;
    public static final byte ENEMY_ROBOT_WHEELED = 18;
    public static final byte ENEMY_GOBLIN = 19;
    public static final byte ENEMY_OGRE = 20;
    public static final byte ENEMY_DRAGON = 21;
    
    public static final byte[][] ITEM_DROP_CHANCE = {
					{75, 10, 10, 75},
					{75, 75},
					{75, 75, 11, 11, 75},
					{75, 15, 5, 15, 5, 75},
					{60, 40, 10, 10, 75},
					{60, 40, 12, 12, 75},
					{70, 12, 12, 1, 75},
					{65, 15, 15, 75},
					{75, 15, 15, 5, 15, 5, 75},
					{15, 60, 8, 8, 75},
					{20, 65, 18, 8, 18, 8, 4, 4, 75},
					{65, 18, 18, 4, 75},
					{65, 18, 18, 4, 75},
					{65, 20, 20, 4, 75},
					{65, 20, 20, 4, 75},
					{60, 15, 3, 3, 2, 75},
					{15, 60, 20, 20, 4, 75},
					{60, 20, 5, 20, 5, 2, 75},
					{60, 10, 20, 5, 20, 5, 2, 75},
					{10, 70, 20, 20, 4, 4, 4, 5, 75},
					{70, 10, 20, 10, 20, 10, 8, 8, 8, 10, 90},
					{5, 20, 12, 12, 10, 10, 10, 10, 90}
					};
    
    public static final int[][] ENEMY_DROPS = {
					/*SLUG*/{0, 80, 83, 237},
					/*FLY*/{18, 237},
					/*SNAIL*/{0, 1, 80, 83, 237},
					/*SLIME*/{7, 80, 81, 83, 84, 237},
					/*BEE*/{15, 16, 80, 83, 237},
					/*SPIDER*/{13, 14, 80, 83, 237},
					/*TURTLE*/{17, 80, 83, 86, 237},
					/*PENGUIN*/{30, 80, 83, 237},
					/*PIG*/{27, 28, 80, 81, 83, 84, 237},
					/*BOAR*/{28, 29, 81, 84, 238},
					/*SNOWMAN*/{21, 22, 80, 81, 83, 84, 86, 87, 238},
					/*FOX*/{23, 81, 84, 88, 238},
					/*ARTIC FOX*/{24, 81, 84, 88, 238},
					/*BEAR*/{25, 81, 84, 86, 238},
					/*POLAR BEAR*/{26, 81, 84, 86, 238},
					/*FAIRY*/{43, 44, 82, 85, 89, 238},
					/*ALLIGATOR*/{19, 20, 81, 84, 87, 238},
					/*ROBOT*/{34, 81, 82, 84, 85, 88, 239},
					/*WHEELED ROBOT*/{34, 35, 81, 82, 84, 85, 88, 239},
					/*GOBLIN*/{31, 32, 81, 84, 86, 87, 88, 90, 239},
					/*OGRE*/{32, 33, 81, 82, 84, 85, 86, 87, 88, 90, 239},
					/*DRAGON*/{36, 37, 82, 85, 86, 87, 88, 89, 239},
					};
    
    //PLAYER CONSTANTS
    public static final String PLAYER_NAME_FIRST = "Charles";
    public static final String PLAYER_NAME_LAST = "Potato";
    public static final byte PLAYER_COLOR_SHIRT = 0;
    public static final byte PLAYER_COLOR_SHIRT_SHADOW = 1;
    public static final byte PLAYER_COLOR_UNDER_SHIRT = 2;
    public static final byte PLAYER_COLOR_PANT = 3;
    public static final byte PLAYER_COLOR_BUTTON = 4;
    public static final byte PLAYER_COLOR_HAIR = 5;
    public static final byte PLAYER_COLOR_EYE = 6;
    public static final byte PLAYER_COLOR_SKIN = 7;
    public static final byte PLAYER_COLOR_SKIN_SHADOW = 8;
    public static final byte PLAYER_COLOR_SHOE = 9;

    //KEY CONSTANTS
    public static final byte TOTAL_KEY_CODES = 71;
    public static final byte KEY_UP = 0;
    public static final byte KEY_DOWN = 1;
    public static final byte KEY_LEFT = 2;
    public static final byte KEY_RIGHT = 3;
    public static final byte KEY_A = 4;
    public static final byte KEY_B = 5;
    public static final byte KEY_C = 6;
    public static final byte KEY_D = 7;
    public static final byte KEY_E = 8;
    public static final byte KEY_F = 9;
    public static final byte KEY_G = 10;
    public static final byte KEY_H = 11;
    public static final byte KEY_I = 12;
    public static final byte KEY_J = 13;
    public static final byte KEY_K = 14;
    public static final byte KEY_L = 15;
    public static final byte KEY_M = 16;
    public static final byte KEY_N = 17;
    public static final byte KEY_O = 18;
    public static final byte KEY_P = 19;
    public static final byte KEY_Q = 20;
    public static final byte KEY_R = 21;
    public static final byte KEY_S = 22;
    public static final byte KEY_T = 23;
    public static final byte KEY_U = 24;
    public static final byte KEY_V = 25;
    public static final byte KEY_W = 26;
    public static final byte KEY_X = 27;
    public static final byte KEY_Y = 28;
    public static final byte KEY_Z = 29;
    public static final byte KEY_0 = 30;
    public static final byte KEY_1 = 31;
    public static final byte KEY_2 = 32;
    public static final byte KEY_3 = 33;
    public static final byte KEY_4 = 34;
    public static final byte KEY_5 = 35;
    public static final byte KEY_6 = 36;
    public static final byte KEY_7 = 37;
    public static final byte KEY_8 = 38;
    public static final byte KEY_9 = 39;
    public static final byte KEY_ENTER = 40;
    public static final byte KEY_SPACE = 41;
    public static final byte KEY_CONTROL = 42;
    public static final byte KEY_SHIFT = 43;
    public static final byte KEY_ALT = 44;
    public static final byte KEY_TAB = 45;
    public static final byte KEY_BACK_QUOTE = 46;
    public static final byte KEY_ESCAPE = 47;
    public static final byte KEY_RIGHT_BRACKET = 48;
    public static final byte KEY_LEFT_BRACKET = 49;
    public static final byte KEY_SEMICOLON = 50;
    public static final byte KEY_QUOTE = 51;
    public static final byte KEY_PERIOD = 52;
    public static final byte KEY_COMMA = 53;
    public static final byte KEY_SLASH = 54;
    public static final byte KEY_BACK_SLASH = 55;
    public static final byte KEY_EQUALS = 56;
    public static final byte KEY_DASH = 57;
    public static final byte KEY_F1 = 58;
    public static final byte KEY_F2 = 59;
    public static final byte KEY_F3 = 60;
    public static final byte KEY_F4 = 61;
    public static final byte KEY_F5 = 62;
    public static final byte KEY_F6 = 63;
    public static final byte KEY_F7 = 64;
    public static final byte KEY_F8 = 65;
    public static final byte KEY_F9 = 66;
    public static final byte KEY_F10 = 67;
    public static final byte KEY_F11 = 68;
    public static final byte KEY_F12 = 69;
    public static final byte KEY_BACKSPACE = 70;
}
