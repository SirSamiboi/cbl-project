import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Goblin type enemies.
 * Goblins are the most basic type of enemy.
 */

public class Goblin extends Enemy {

    /**
     * Constructor for the Goblin class at the starting position.
     */
    public Goblin(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 36;
        this.imageHeight = 47;
        this.imageOffsetX = 0;
        this.imageOffsetY = 0;
        this.maxHp = 20;
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