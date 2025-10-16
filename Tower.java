import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
    protected int imageWidth;
    protected int imageHeight;
    protected int imageOffsetX;
    protected int imageOffsetY;

    /**
     * Constructor.
     */
    public Tower(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.level = 0;
        this.imageWidth = 62;
        this.imageHeight = 62;
        this.imageOffsetX = 2;
        this.imageOffsetY = 2;

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

    public void tick(ArrayList<Enemy> enemyList) { }
}