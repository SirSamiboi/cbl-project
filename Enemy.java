import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;


/**
 * Class that stores all enemies interractions and stores data about the instance
 * enemy.
 */

public class Enemy {
    protected int posX;
    protected int posY;
    protected byte type; // 0 - standard, 1 - tank etc.
    protected int hp; // amount of lives the enemy has.
    protected int speed; // displacement per frame? in pixels?
    protected int money; // amount of money the enemy awards upon defeat
    protected int distanceTraveled; // used to determine, how far did the enemy travel
    // protected boolean ground; 
    // used to determine if the troop is a ground troop, or a flying troop.

    protected BufferedImage image; // Enemy texture.
    protected int timer; // Counts up by 1 every frame
    protected int timerLimit; // Number of frames between each attack

    
    public BufferedImage getImage() {
        return image;
    }

    public int getHp() {
        return hp;
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
     * TODO: Plays the death animation
     * TODO?: Realocates memory(deletes the object entirely)?
     */

    public void die(ArrayList<Enemy> enemyList) {
        this.speed = 0;
        enemyList.remove(this);
        
        try {
            this.image = ImageIO.read(new File("assets/Explosion_5.png"));
        } catch (IOException e) {
            System.out.println("Couldn't load the death texture");
            System.out.println(e);
            image = null;
        }
        //TODO: play the death animation, relocate memory.
    }

    /**
     * Handles the enemy's movement along the path.
     * 
     * Yes, it is butt-ugly.
     * No, I do not know, how to make it better.
     * > I do :)
     * 
     * Also, it updates this.distanceTraveled to keep the distance this enemy passed
     */

    // TODO: Replace += version with = version (always calculating final position)
    // Reasoning: To avoid enemies being misaligned at high speeds
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
            player.setPlayerHp((player.getPlayerHp() - 1)); //deal 1 damage to the player
            this.hp = 0;

            System.out.println("Another one lost to The Zone");
            System.out.println("Player HP: " + player.getPlayerHp());
        }
    }
}
