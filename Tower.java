import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;

/**
 * Class used for handling all tower variables and interactions.
 */

public class Tower {
    protected int posX;
    protected int posY;
    protected int damage;
    protected int range;
    protected int level;
    protected int maxLevel;
    protected int upgradeCost;
    protected int timer;
    protected int cooldown;
    protected BufferedImage image;
    protected int imageWidth;
    protected int imageHeight;
    protected int imageOffsetX;
    protected int imageOffsetY;

    /**
     * Constructor.
     */
    public Tower(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        this.level = 0;
        this.maxLevel = 0;
        this.imageWidth = 62;
        this.imageHeight = 62;
        this.imageOffsetX = 2;
        this.imageOffsetY = 2;

        try {
            this.image = ImageIO.read(new File("assets/tower0.png"));
        
        } catch (IOException e) {
            System.out.println("ERROR: Empty plot image could not be loaded");
            System.out.println(e);
            this.image = null;
        }
    }

    
    int getPosX() {
        return posX;
    }

    int getPosY() {
        return posY;
    }

    // int getDamage() {
    //     return damage;
    // }

    int getRange() {
        return range;
    }

    int getLevel() {
        return level;
    }

    int getMaxLevel() {
        return maxLevel;
    }

    int getUpgradeCost() {
        return upgradeCost;
    }

    BufferedImage getImage() {
        return image;
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
    
    void levelUp() { }

    void tick(ArrayList<Enemy> enemyList, ArrayList<Animation> animationList) { }

    /**
     * Places a basic tower on an empty plot.
     */
    void placeBasic(ArrayList<Tower> towerList) {
        for (int i = 0; i < towerList.size(); i++) {
            Tower tower = towerList.get(i);
            if (tower.getPosX() == posX && tower.getPosY() == posY) {
                towerList.set(i, new BasicTower(posX, posY));
            }
        }
    }

    /**
     * Places a fireball tower on an empty plot.
     */
    void placeFireball(ArrayList<Tower> towerList) {
        for (int i = 0; i < towerList.size(); i++) {
            Tower tower = towerList.get(i);
            if (tower.getPosX() == posX && tower.getPosY() == posY) {
                towerList.set(i, new FireballTower(posX, posY));
            }
        }
    }

    /**
     * Checks if the tower has been selected.
     */
    boolean isClicked(int lastClickX, int lastClickY) {
        return (lastClickX >= getImageX() && lastClickX <= getImageX() + imageWidth
            && lastClickY >= getImageY() && lastClickY <= getImageY() + imageHeight);
    }
}