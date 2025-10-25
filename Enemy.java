import java.awt.image.BufferedImage;
import java.util.*;

/**
 * Class that stores all enemies interactions and stores data about the instance enemy.
 */

public class Enemy {
    protected int posX;
    protected int posY;
    protected byte type; // 0 - standard, 1 - tank etc.
    protected int hp; // Amount of health points the enemy has
    protected int maxHp; // Initial amount of health points
    protected int damage; // Damage dealt by enemy upon reaching end of track
    protected int speed; // Displacement per tick in pixels
    protected int money; // Amount of money awarded by enemy upon defeat
    protected int distanceTraveled; // Stores total distance travelled

    protected BufferedImage image; // Enemy texture
    protected int imageWidth;
    protected int imageHeight;
    protected int imageOffsetX;
    protected int imageOffsetY;

    protected boolean facingRight = true; // Tracks if enemy should be facing right (t) or left (f)

    protected int timer; // Counts up by 1 every frame
    protected int timerLimit; // Number of frames between each attack

    protected int chillTimer = 0; // Remaining ticks of chill tower's slowing effect
    protected int chillDuration = 1; // Total ticks of chill tower's slowing effect

    
    public BufferedImage getImage() {
        return image;
    }

    public int getHp() {
        return hp;
    }

    public int getMaxHp() {
        return maxHp;
    }

    public int getDamage() {
        return damage;
    }

    public int getSpeed() {
        return speed;
    }

    public int getMoney() {
        return money;
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    public int getPosX() {
        return posX;
    }

    public int getPosY() {
        return posY;
    }

    public int getType() {
        return type;
    }

    int getImageWidth() {
        return imageWidth;
    }

    int getImageHeight() {
        return imageHeight;
    }

    // Returns the x position of the tower's image
    int getImageX() {
        return posX - imageWidth / 2 + imageOffsetX;
    }

    // Returns the y position of the tower's image
    int getImageY() {
        return posY - imageHeight / 2 + imageOffsetY;
    }

    boolean getFacingRight() {
        return facingRight;
    }

    // int getChillTimer() {
    //     return chillTimer;
    // }

    /**
     * Substracts the tower's damage from the Enemy's HP.
     */

    public void dealDamage(int damage) {
        this.hp -= damage;
    }

    /**
     * Removes the Enemy object from the ArrayList of all enemies.
     */

    public void die(ArrayList<Enemy> enemyList, ArrayList<Animation> animationList) {
        this.speed = 0;
        animationList.add(new DeathAnimation(posX, posY, money));
        enemyList.remove(this);
    }

    /**
     * Handles the enemy's movement along the path, and updates
     * this.distanceTraveled to store the distance this enemy has travelled.
     * 
     * We were thinking of replacing the += approach with a = approach instead,
     * which would mean always calculating the final position of the enemy,
     * as this would prevent enemies from being misaligned at high speeds,
     * but this would cause an issue with the randomized enemy spawn positions
     * and is not necessary for the enemies we have included in this game.
     */

    public void move() {
        int normalSpeed = speed;

        // The enemy moves much slower while affected by the chill tower's attack
        if (chillTimer > 0) {
            // 1/5th speed at start of chill, then speeds up
            speed = (int) ((double) normalSpeed / 4 + (double) normalSpeed * 3 / 4
                * Math.pow((1 - (double) chillTimer / chillDuration), 3));
        }

        if (distanceTraveled <= 640 - 48) { // first horisontal stretch
            posX += speed;

        } else if (distanceTraveled <= 640 + 48) { // first turn
            posX += speed;
            posY += speed;
            distanceTraveled += speed;

        } else if (distanceTraveled <= 640 + 192 - 48) { // first vertical stretch
            posY += speed;

        } else if (distanceTraveled <= 640 + 192 + 48) { // second turn
            posX -= speed;
            posY += speed;
            distanceTraveled += speed;
            facingRight = false;

        } else if (distanceTraveled <= 640 + 192 + 480 - 48) {
            posX -= speed;

        } else if (distanceTraveled <= 640 + 192 + 480 + 48) { // third turn
            posX -= speed;
            posY += speed;
            distanceTraveled += speed;

        } else if (distanceTraveled <= 640 + 192 + 480 + 192 - 48) { // second vertical stretch
            posY += speed;

        } else if (distanceTraveled <= 640 + 192 + 480 + 192 + 48) { // fourth turn 
            posX += speed;
            posY += speed;
            distanceTraveled += speed;
            facingRight = true;

        } else { // last horisontal stretch
            posX += speed;
        }
        distanceTraveled += speed;

        // Restore original enemy speed
        speed = normalSpeed;
    }

    /**
     * Applies the chill tower's effect on the enemy,
     * causing it to slow down for a given number of ticks.
     */
    public void chill(int chillDuration) {
        this.chillDuration = chillDuration;
        chillTimer = chillDuration;
    }

    // Used by summoner enemies to modify the enemy's initial position
    public void summon(int posX, int posY, int distanceTraveled, boolean facingRight) { }
    
    /**
     * Handles the enemy's actions over the next tick.
     */
    public void tick(ArrayList<Enemy> enemyList, Player player) {
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
