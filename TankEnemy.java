import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class TankEnemy extends Enemy {
    /**
     * Constructor for the Goblin class at the starting position.
     */
    public TankEnemy(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 36;
        this.imageHeight = 47;
        this.imageOffsetX = 0;
        this.imageOffsetY = 0;
        this.maxHp = 50;
        this.hp = maxHp;
        this.damage = 1;
        this.speed = 3;
        this.money = 10;
        this.distanceTraveled = 0;

        try {
            this.image = ImageIO.read(new File("assets/tank.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the Tank texture");
            this.image = null;
        }
    }
}