import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Class used for handling all tower interactions.
 */
public class Tower {
    private int posX;
    private int posY;
    private int damage;
    private int range;
    private int level;
    private BufferedImage image;
    private int timer; // Counts up by 1 every frame
    private int timerLimit; // Number of frames between each attack

    /**
     * Constructor.
     */
    public Tower(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.timer = 0;

        try {
            image = ImageIO.read(new File("assets/tower0.png"));
        
        } catch (IOException e) {
            System.out.println("Womp womp");
            System.out.println(e);
            image = null;
        }
    }

    
    int getPosX() {
        return posX;
    }

    int getPosY() {
        return posY;
    }

    int getDamage() {
        return damage;
    }

    int getRange() {
        return range;
    }

    int getLevel() {
        return level;
    }

    BufferedImage getImage() {
        return image;
    }



}

/**
 * Basic tower type.
 */
// class BasicTower extends Tower {

// }