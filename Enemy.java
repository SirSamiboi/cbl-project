import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.function.ToDoubleBiFunction;
import javax.imageio.ImageIO;

/**
 * Class that stores all enemies interractions and stores data about the instance
 * enemy.
 */

public class Enemy {
    private int posX;
    private int posY;
    private byte type; // 0 - standard, 1 - tank etc.
    private int speed; // displacement per frame? in pixels?
    private int hp; // amount of lives the enemy has.
    private int distanceTraveled; // used to determine, how far did the enemy travel
    // private boolean ground; 
    // used to determine if the troop is a ground troop, or a flying troop.

    private BufferedImage image; // Enemy texture.
    private int timer; // Counts up by 1 every frame
    private int timerLimit; // Number of frames between each attack

    /**
     * Constructor.
     */

    public Enemy(int posX, int posY, byte type) {
        this.posX = posX;
        this.posY = posY;
        this.type = type;

        switch (type) {
            case 0: //default enemy, goblin
                try {
                    image = ImageIO.read(new File("assets/goblin.png"));
                }
                catch (Exception e) {
                    System.out.println("The texture is not texturing");
                    System.out.println(e);
                    image = null;
                }
                break;
            default:
                System.out.println("The enemy type is not typing.");
        }
    }
    
    public BufferedImage getImage() {
        return image;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    public int getHp() {
        return hp;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getType() {
        return type;
    }
}
