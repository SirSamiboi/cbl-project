import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Orc type enemies.
 * Orcs are a stronger, slower version of goblins.
 */

public class Orc extends Enemy {

    /**
     * Constructor for the Orc class at the starting position.
     */
    public Orc(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 39;
        this.imageHeight = 58;
        this.imageOffsetX = 0;
        this.imageOffsetY = -6;
        this.maxHp = 80;
        this.hp = maxHp;
        this.damage = 2;
        this.speed = 3;
        this.money = 25;
        this.distanceTraveled = 0;

        try {
            this.image = ImageIO.read(new File("assets/orc.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the Orc texture");
            this.image = null;
        }
    }
}