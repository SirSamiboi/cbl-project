import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Ogre type enemies.
 * Ogres are a stronger, slower version of goblins.
 */
// TODO: Use new file loading approach
public class Ogre extends Enemy {

    /**
     * Constructor for the Ogre class at the starting position.
     */
    public Ogre(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 38;
        this.imageHeight = 55;
        this.imageOffsetX = 0;
        this.imageOffsetY = -6;
        this.maxHp = 80;
        this.hp = maxHp;
        this.damage = 2;
        this.speed = 3;
        this.money = 25;
        this.distanceTraveled = 0;

        try {
            this.image = ImageIO.read(new File("assets/ogre.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the Ogre texture");
            this.image = null;
        }
    }
}