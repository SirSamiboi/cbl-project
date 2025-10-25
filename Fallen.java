import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Fallen type enemies.
 * The Fallen are very powerful enemies that gain speed when near death.
 */

public class Fallen extends Enemy {
    private boolean transformed; // The Fallen transforms when at half health

    /**
     * Constructor for the Fallen class at the starting position.
     */
    public Fallen(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 50;
        this.imageHeight = 74;
        this.imageOffsetX = 0;
        this.imageOffsetY = -14;
        this.maxHp = 1000;
        this.hp = maxHp;
        this.damage = 10;
        this.speed = 1;
        this.money = 10;
        this.distanceTraveled = 0;
        this.transformed = false;

        try {
            String fileSeparator = File.separator;
            this.image = ImageIO.read(new File(String.format("assets%sfallen.png", fileSeparator)));
        } catch (IOException e) {
            System.out.println("Couldn't load the Fallen texture");
            this.image = null;
        }
    }

    /**
     * The override handles the Fallen's speed change at low health.
     */
    @Override
    public void tick(ArrayList<Enemy> enemyList, Player player) {
        // Transform into fast state when half health is reached
        if (!transformed && hp < maxHp / 2) {
            try {
                String fileSeparator = File.separator;
                this.image = ImageIO.read(new File(String.format("assets%sfallenFast.png", fileSeparator)));
            } catch (IOException e) {
                System.out.println("Couldn't load the Fallen texture");
                this.image = null;
            }

            transformed = true;
        }

        if (transformed) {
            speed = (int) (2 + 3 * (1 - (double) (hp / ((double) maxHp / 2))));
        }

        move();
        chillTimer -= 1;

        if (this.posX >= 800) { // if it passed the entire track
            player.setPlayerHp((player.getPlayerHp() - damage)); // Deal 1 damage to the player
            this.hp = 0;
            player.setMoney(player.getMoney() - this.money);

            System.out.println("Another one lost to The Zone");
            System.out.println("Player HP: " + player.getPlayerHp());
        }
    }
}