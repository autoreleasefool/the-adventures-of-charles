/**
 *
 * Name:        Joseph Roque
 * Course:      ICS4UO
 * Teacher:     Mr. Byers
 * Created:     October 3, 2012
 *
 * Application: Tiny Town
 * Class:       Sound
 *
 * Purpose:     To load and play sound
 *
 **/

package charles.resources;

import java.applet.Applet;
import java.applet.AudioClip;

public class Sound {

    public static final Sound playerHurt = new Sound("/charles/_sound/playerHurt.wav");
    public static final Sound gangnamStyle_Cut = new Sound("/charles/_sound/GangnamStyle_Cut.wav");
    
    private AudioClip clip;
    
    private Sound(String path) {
	try {
	    //Loads the audio file into an AudioClip
	    clip = Applet.newAudioClip(Sound.class.getResource(path));
	} catch (Exception e) {
	    e.printStackTrace();    //Prints an error message
	}
    }
    
    public void play() {
	try {
	    new Thread() {
		public void run() {
		    clip.play();    //Plays the song in a new thread
		}
	    }.start();
	} catch (Exception e) {
	    e.printStackTrace();    //Prints an error message
	}
    }
    
    public void stop() {
	clip.stop();    //Stops playing the song
    }
    
    public void loop() {
	try {
	    new Thread() {
		public void run() {
		    clip.loop();    //Loops the song in a new thread
		}
	    }.start();
	} catch (Exception e) {
	    e.printStackTrace();    //Prints an error message
	}
    }
}
