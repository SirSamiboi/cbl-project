import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Orc type enemies.
 * Orcs are a more powerful variant of ogres.
 */

public class Orc extends Enemy {

    /**
     * Constructor for the Orc class at the starting position.
     */
    public Orc(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 40;
        this.imageHeight = 60;
        this.imageOffsetX = 0;
        this.imageOffsetY = -7;
        this.maxHp = 200;
        this.hp = maxHp;
        this.damage = 3;
        this.speed = 3;
        this.money = 50;
        this.distanceTraveled = 0;

        try {
            String fileSeparator = File.separator;
            this.image = ImageIO.read(new File(String.format("assets%sorc.png", fileSeparator)));
        } catch (IOException e) {
            System.out.println("Couldn't load the Orc texture");
            this.image = null;
        }
    }
}