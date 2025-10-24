import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Skeleton type enemies.
 * Skeletons move faster than goblins, and often come in groups.
 */

public class Skeleton extends Enemy {

    /**
     * Constructor for the Skeleton class at the starting position.
     */
    public Skeleton(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 34;
        this.imageHeight = 44;
        this.imageOffsetX = 0;
        this.imageOffsetY = 0;
        this.maxHp = 15;
        this.hp = maxHp;
        this.damage = 1;
        this.speed = 6;
        this.money = 10;
        this.distanceTraveled = 0;

        try {
            this.image = ImageIO.read(new File("assets/skeleton.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the Skeleton texture");
            this.image = null;
        }
    }
}