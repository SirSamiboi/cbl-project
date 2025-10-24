import java.io.IOException;
import java.net.URL;
import javax.sound.sampled.*;

/**
 * Handles the use of sound effects.
 */

public class SoundEffectPlayer {

    /**
     * Plays a sound file from the sounds folder.
     * @param soundFileName The filename of the sound file, e.g. boom.wav
     */

    public void playSound(String soundFileName) {
        try {
            // Get URL for sound file (sound effects found in assets folder)
            URL soundUrl = getClass().getResource("/sounds/" + soundFileName);

            if (soundUrl == null) {
                System.err.println("Sound file not found: " + soundFileName);
                return;
            }

            // Get an audio stream
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundUrl);

            // Get, open and play clip
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("ERROR: Unsupported audio file");
        }
    }
}