import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class FastEnemy extends Enemy {
    /**
     * Constructor for the Goblin class at the starting position.
     */
    public FastEnemy(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 36;
        this.imageHeight = 47;
        this.imageOffsetX = 0;
        this.imageOffsetY = 0;
        this.maxHp = 15;
        this.hp = maxHp;
        this.damage = 1;
        this.speed = 15;
        this.money = 10;
        this.distanceTraveled = 0;

        try {
            this.image = ImageIO.read(new File("assets/fast.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the Fast Enemy texture");
            this.image = null;
        }
    }
}