package Abalone.Audiopack;

//Java program to play an Audio 
//file using Clip Object 
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

public class Audio implements Runnable {

    // to store current position
    Long currentFrame;
    Clip clip;

    // current status of clip
    String status;

    AudioInputStream audioInputStream;
    static String filePath;

    // constructor to initialize streams and clip
    public Audio() throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        // create AudioInputStream object

        URL path = Audio.class.getResource("AbaloneMusic.aifc");
        audioInputStream = AudioSystem.getAudioInputStream(new File(path.getFile()).getAbsoluteFile());
        // create clip reference
        clip = AudioSystem.getClip();
        // open audioInputStream to the clip
        clip.open(audioInputStream);
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    // Method to play the audio
    public void play() {
        // start the clip
        clip.start();

        status = "play";
    }

    @Override
    public void run() {
        try {
            filePath = "Your path for the file";
            Audio audioPlayer = new Audio();
            while (true) {
                clip.start();
            }
        }

        catch (Exception ex) {
            System.out.println("Error with playing sound.");
            ex.printStackTrace();

        }

    }
}