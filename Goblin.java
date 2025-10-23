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
        this.imageWidth = 36;
        this.imageHeight = 48;
        this.imageOffsetX = 0;
        this.imageOffsetY = 0;
        this.maxHp = 15;
        this.hp = maxHp;
        this.damage = 1;
        this.speed = 4;
        this.money = 10;
        this.distanceTraveled = 0;

        try {
            this.image = ImageIO.read(new File("assets/goblin.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the Goblin texture");
            this.image = null;
        }
    }
}