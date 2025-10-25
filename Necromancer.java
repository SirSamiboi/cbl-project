import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Class that saves the data of Necromancer type enemies.
 * Necromancers periodically summon skeletons.
 */

public class Necromancer extends Enemy {
    private int summonTimer; // Ticks until next skeleton is summoned
    private int summonCooldown = 60;

    /**
     * Constructor for the Necromancer class at the starting position.
     */
    public Necromancer(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.imageWidth = 36;
        this.imageHeight = 50;
        this.imageOffsetX = 0;
        this.imageOffsetY = -2;
        this.maxHp = 50;
        this.hp = maxHp;
        this.damage = 1;
        this.speed = 2;
        this.money = 50;
        this.distanceTraveled = 0;
        this.summonTimer = summonCooldown;

        try {
            String fileSeparator = File.separator;
            this.image = ImageIO.read(new File(
                String.format("assets%snecromancer.png", fileSeparator)));
        } catch (IOException e) {
            System.out.println("Couldn't load the Necromancer texture");
            this.image = null;
        }
    }

    /**
     * The override implements the necromancer's summoning ability.
     */
    @Override
    public void tick(ArrayList<Enemy> enemyList, Player player) {
        move();
        chillTimer -= 1;

        if (summonTimer == 0) {
            enemyList.add(new Skeleton(posX, posY));
            enemyList.get(enemyList.size() - 1).summon(posX, posY, distanceTraveled, facingRight);
            summonTimer = summonCooldown;
        }
        summonTimer -= 1;

        if (this.posX >= 800) { // if it passed the entire track
            player.setPlayerHp((player.getPlayerHp() - damage)); // Deal 1 damage to the player
            this.hp = 0;
            player.setMoney(player.getMoney() - this.money);

            System.out.println("Another one lost to The Zone");
            System.out.println("Player HP: " + player.getPlayerHp());
        }
    }
}