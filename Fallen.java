import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Class that saves the data of Fallen type enemies.
 * The Fallen are very powerful enemies that gain a health and speed boost when near death.
 */

public class Fallen extends Enemy {
    private boolean transformed; // The Fallen transforms when at half health

    /**
     * Constructor for the Fallen class at the starting position.
     */
    public Fallen(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 50;
        this.imageHeight = 74;
        this.imageOffsetX = 0;
        this.imageOffsetY = -14;
        this.maxHp = 1000;
        this.hp = maxHp;
        this.damage = 10;
        this.speed = 1;
        this.money = 1000;
        this.distanceTraveled = 0;
        this.transformed = false;

        try {
            String fileSeparator = File.separator;
            this.image = ImageIO.read(new File(String.format("assets%sfallen.png", fileSeparator)));
        } catch (IOException e) {
            System.out.println("Couldn't load the Fallen texture");
            this.image = null;
        }
    }

    /**
     * The override handles the Fallen's speed change at low health.
     */
    @Override
    public void tick(ArrayList<Enemy> enemyList, Player player) {
        // Transform to recover health and gain speed when half health is reached
        if (!transformed && hp < maxHp / 2) {
            maxHp = 1200;
            hp = maxHp;
            transformed = true;

            playTransformationSound();

            try {
                String fileSeparator = File.separator;
                this.image = ImageIO.read(new File(String.format(
                    "assets%sfallenTransformed.png", fileSeparator)));
            } catch (IOException e) {
                System.out.println("Couldn't load the Fallen texture");
                this.image = null;
            }
        }

        if (transformed) {
            speed = (int) (2 + 2 * Math.pow(1 - (double) hp / maxHp, 2));
        }

        move();
        chillTimer -= 1;

        if (this.posX >= 800) { // If the enemy has passed the end of the track
            player.setPlayerHp((player.getPlayerHp() - damage)); // Deal damage to the player
            this.hp = 0;
            player.setMoney(player.getMoney() - this.money);

            System.out.println("Another one lost to The Zone");
            System.out.println("Player HP: " + player.getPlayerHp());
        }
    }

    /**
     * Plays a sound upon the Fallen's transformation.
     */
    void playTransformationSound() {
        // Get URL for sound file (sound effects found in sounds folder)
        String fileSeparator = File.separator;
        URL soundUrl = getClass().getResource(String.format("%ssounds%s", fileSeparator, 
            fileSeparator) + "fallenTransformation.wav");

        if (soundUrl == null) {
            System.err.println("Fallen transformation sound file not found");
            return;
        }

        try (InputStream is = soundUrl.openStream();
            var audioStream = AudioSystem.getAudioInputStream(is)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();

        } catch (Exception e) {
            System.err.println("Failed to play fallen transformation sound: " + e.getMessage());
        }
    }
}