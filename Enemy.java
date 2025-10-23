import java.awt.image.BufferedImage;
import java.util.*;


/**
 * Class that stores all enemies interactions and stores data about the instance enemy.
 */

public class Enemy {
    protected int posX;
    protected int posY;
    protected byte type; // 0 - standard, 1 - tank etc.
    protected int hp; // amount of lives the enemy has
    protected int maxHp; // initial amount of lives
    protected int damage; // damage dealt by enemy upon reaching end of track
    protected int speed; // displacement per frame? in pixels?
    protected int money; // amount of money the enemy awards upon defeat
    protected int distanceTraveled; // used to determine, how far did the enemy travel
    // protected boolean ground; 
    // used to determine if the troop is a ground troop, or a flying troop.

    protected BufferedImage image; // Enemy texture.
    protected int imageWidth;
    protected int imageHeight;
    protected int imageOffsetX;
    protected int imageOffsetY;

    protected int timer; // Counts up by 1 every frame
    protected int timerLimit; // Number of frames between each attack

    
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

    /**
     * Substracts the tower's damage from the Enemy's HP.
     * @param damage
     *     The (positive integer) damage that the enemy must take.
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
     * We were thinking of replacing the += approach with a = approach instead,
     * which would mean always calculating the final position of the enemy,
     * as this would prevent enemies from being misaligned at high speeds,
     * but this would cause an issue with the randomized enemy spawn positions
     * and is not necessary for the enemies we have included in this game.
     */

    public void move() {
        if (distanceTraveled <= 640 - 48) { // first horisontal stretch
            this.posX += this.speed;

        } else if (distanceTraveled <= 640 + 48) { // first turn
            this.posX += this.speed;
            this.posY += this.speed;
            this.distanceTraveled += this.speed;

        } else if (distanceTraveled <= 640 + 192 - 48) { // first vertical stretch
            this.posY += this.speed;

        } else if (distanceTraveled <= 640 + 192 + 48) { // second turn
            this.posX -= this.speed;
            this.posY += this.speed;
            this.distanceTraveled += this.speed;

        } else if (distanceTraveled <= 640 + 192 + 480 - 48) {
            this.posX -= this.speed;

        } else if (distanceTraveled <= 640 + 192 + 480 + 48) { // third turn
            this.posX -= this.speed;
            this.posY += this.speed;
            this.distanceTraveled += this.speed;

        } else if (distanceTraveled <= 640 + 192 + 480 + 192 - 48) { // second vertical stretch
            this.posY += this.speed;

        } else if (distanceTraveled <= 640 + 192 + 480 + 192 + 48) { // fourth turn 
            this.posX += this.speed;
            this.posY += this.speed;
            this.distanceTraveled += this.speed;

        } else { // last horisontal stretch
            this.posX += this.speed;
        }
        this.distanceTraveled += this.speed;
    }


    /**
     * Handles the enemy's actions over the next tick.
     * @param enemyList
     *     Global list where all alive enemies are stored.
     */
    public void tick(ArrayList<Enemy> enemyList, Player player) {
        move();

        if (this.posX >= 800) { // if it passed the entire track
            player.setPlayerHp((player.getPlayerHp() - damage)); // Deal 1 damage to the player
            this.hp = 0;

            System.out.println("Another one lost to The Zone");
            System.out.println("Player HP: " + player.getPlayerHp());
        }
    }
}
