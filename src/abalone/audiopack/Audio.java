package abalone.audiopack;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * An audio file that runs a clip continuously once the thread is started. 
 * @author Sam Freriks and Ayla van der Wal.
 * @version 1.0
 */
public class Audio implements Runnable {
    Clip clip;
    AudioInputStream audioInputStream;

    /** constructor that creates the URL path and opens an audio input stream in a clip that loops for an infinite time.
     * @throws UnsupportedAudioFileException Javadoc.
     * @throws IOException Javadoc.
     * @throws LineUnavailableException Javadoc.
     */
    public Audio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        
        URL path = Audio.class.getResource("AbaloneMusic.aifc");
        audioInputStream = AudioSystem.getAudioInputStream(new File(path.getFile()).getAbsoluteFile());
        clip = AudioSystem.getClip();
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    /** The run method starts the clip in a while loop.
     * @throws exception if the clip cannot start
     */
    @Override
    public void run() {
        try {
            while (true) {
                clip.start();
            }
            
        } catch (Exception e) {
            System.out.println("Error with playing sound.");
            e.printStackTrace();
        }
    }
}