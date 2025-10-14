import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;


/**
 * Class used for handling all tower interactions.
 */
public class Tower {
    protected int posX;
    protected int posY;
    protected int damage;
    protected int range;
    protected int level;
    protected int timer;
    protected int cooldown;
    protected BufferedImage image;

    /**
     * Constructor.
     */
    public Tower(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.level = 0;

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