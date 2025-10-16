import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of a goblin.
 */

public class Goblin extends Enemy {
    /**
     * Constructor for the Goblin class at the starting position.
     * @param posX
     *     Starting position X.
     * @param posY
     *     Starting position Y.
     */

    public Goblin(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        hp = 15;
        speed = 5;
        distanceTraveled = 0;
        try {
            image = ImageIO.read(new File("assets/goblin.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the Goblin texture");
            image = null;
        }
    }
}