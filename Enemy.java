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
    protected int speed; // displacement per frame? in pixels?
    protected int hp; // amount of lives the enemy has.
    protected int distanceTraveled; // used to determine, how far did the enemy travel
    // protected boolean ground; 
    // used to determine if the troop is a ground troop, or a flying troop.

    protected BufferedImage image; // Enemy texture.
    protected int timer; // Counts up by 1 every frame
    protected int timerLimit; // Number of frames between each attack

    
    public BufferedImage getImage() {
        return image;
    }

    public int getSpeed() {
        return speed;
    }

    public int getDistanceTraveled() {
        return distanceTraveled;
    }

    public int getHp() {
        return hp;
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
        enemyList.remove(this);
        try{
            this.image = ImageIO.read(new File("assets/Explosion_5.png"));
        } catch (IOException e) {
            
        }
        //TODO: play the death animation, relocate memory.
    }


    /**
     * Handles the enemy's actions over the next tick.
     * @param enemyList
     *     Global list where all alive enemies are stored.
     */
    public void tick(ArrayList<Enemy> enemyList) {
        if (this.hp <= 0) {
            this.die(enemyList);
            return;
        }
    }


}
