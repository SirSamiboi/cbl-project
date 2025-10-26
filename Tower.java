import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

/**
 * Class used for handling all tower variables and actions.
 */

public class Tower {
    protected int posX;
    protected int posY;
    protected int damage;
    protected int range;
    protected int level;
    protected int maxLevel;
    protected int upgradeCost;
    protected int timer; // Ticks remaining until next attack
    protected int cooldown; // Number of ticks between each attack

    protected BufferedImage image; // Tower image sprite
    protected int imageWidth;
    protected int imageHeight;
    protected int imageOffsetX;
    protected int imageOffsetY;

    protected Clip attackClip; // Used for attack sound effect
    
    /**
     * Constructor.
     */
    public Tower(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.level = 0;
        this.maxLevel = 0;
        this.imageWidth = 62;
        this.imageHeight = 62;
        this.imageOffsetX = 2;
        this.imageOffsetY = 2;

        try {
            String fileSeparator = File.separator;
            this.image = ImageIO.read(new File(String.format("assets%stower0.png", fileSeparator)));
        
        } catch (IOException e) {
            System.out.println("ERROR: Empty plot image could not be loaded");
            System.out.println(e);
            this.image = null;
        }
    }

    
    int getPosX() {
        return posX;
    }

    int getPosY() {
        return posY;
    }

    int getRange() {
        return range;
    }

    int getLevel() {
        return level;
    }

    int getMaxLevel() {
        return maxLevel;
    }

    int getUpgradeCost() {
        return upgradeCost;
    }

    BufferedImage getImage() {
        return image;
    }

    int getImageWidth() {
        return imageWidth;
    }

    int getImageHeight() {
        return imageHeight;
    }

    // Returns the x position of the tower's image
    int getImageX() {
        return posX - imageWidth / 2 + imageOffsetX;
    }

    // Returns the y position of the tower's image
    int getImageY() {
        return posY - imageHeight / 2 + imageOffsetY;
    }
    
    void levelUp() { }

    void tick(ArrayList<Enemy> enemyList, ArrayList<Animation> animationList) { }

    /**
     * Loads the tower's attack sound effect.
     */
    protected Clip loadSound(String soundFileName) {
        try {
            // Get URL for sound file (sound effects found in sounds folder)
            String fileSeparator = File.separator;
            URL soundUrl = getClass().getResource(String.format("%ssounds%s", fileSeparator, 
                    fileSeparator) + soundFileName);

            if (soundUrl == null) {
                System.err.println("Sound file not found: " + soundFileName);
                return null;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundUrl);

            // Get and open attack sound clip
            attackClip = AudioSystem.getClip();
            attackClip.open(audioStream);

        } catch (IOException | LineUnavailableException | UnsupportedAudioFileException e) {
            System.out.println("Could not load clip for tower: " + e.getMessage());
            attackClip = null;
        }

        return attackClip;
    }

    /**
     * Plays the tower's attack sound.
     */
    public void playAttackSound() {
        if (attackClip != null) {
            if (attackClip.isRunning()) {
                attackClip.stop();
            }
            attackClip.setFramePosition(0);
            attackClip.start();
        }
    }

    /**
     * Places a basic tower on an empty plot.
     */
    void placeBasic(ArrayList<Tower> towerList) {
        for (int i = 0; i < towerList.size(); i++) {
            Tower tower = towerList.get(i);
            if (tower.getPosX() == posX && tower.getPosY() == posY) {
                towerList.set(i, new BasicTower(posX, posY));
            }
        }
    }

    /**
     * Places a fireball tower on an empty plot.
     */
    void placeFireball(ArrayList<Tower> towerList) {
        for (int i = 0; i < towerList.size(); i++) {
            Tower tower = towerList.get(i);
            if (tower.getPosX() == posX && tower.getPosY() == posY) {
                towerList.set(i, new FireballTower(posX, posY));
            }
        }
    }

    /**
     * Places a chill tower on an empty plot.
     */
    void placeChill(ArrayList<Tower> towerList) {
        for (int i = 0; i < towerList.size(); i++) {
            Tower tower = towerList.get(i);
            if (tower.getPosX() == posX && tower.getPosY() == posY) {
                towerList.set(i, new ChillTower(posX, posY));
            }
        }
    }

    /**
     * Checks if the tower has been selected.
     */
    boolean isClicked(int lastClickX, int lastClickY) {
        return (lastClickX >= getImageX() && lastClickX <= getImageX() + imageWidth
            && lastClickY >= getImageY() && lastClickY <= getImageY() + imageHeight);
    }
}