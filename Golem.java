import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Golem type enemies.
 * Golems are very slow, but have a very large amount of health.
 */

public class Golem extends Enemy {

    /**
     * Constructor for the Golem class at the starting position.
     */
    public Golem(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 45;
        this.imageHeight = 60;
        this.imageOffsetX = 0;
        this.imageOffsetY = -6;
        this.maxHp = 400;
        this.hp = maxHp;
        this.damage = 5;
        this.speed = 2;
        this.money = 100;
        this.distanceTraveled = 0;

        try {
            String fileSeparator = File.separator;
            this.image = ImageIO.read(new File(String.format("assets%sgolem.png", fileSeparator)));
        } catch (IOException e) {
            System.out.println("Couldn't load the Golem texture");
            this.image = null;
        }
    }
}