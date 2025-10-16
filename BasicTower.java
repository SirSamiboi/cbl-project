import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

/**
 * Basic tower type.
 */
class BasicTower extends Tower {
    private final int[] damageLevel = {2, 3, 4, 5}; // Index 0 holds damage dealt at level 1
    private final int[] rangeLevel = {150, 175, 200, 225};
    private final int[] cooldownLevel = {15, 12, 10, 8};
    private final int[] imageWidthLevel = {62, 0, 0, 0};
    private final int[] imageHeightLevel = {62, 0, 0, 0};
    private final int[] imageOffsetXLevel = {2, 0, 0, 0};
    private final int[] imageOffsetYLevel = {2, 0, 0, 0};
    // Add arrays for image info (width, height, x/y offset) to be used when loading images

    /**
     * Constructor (indentical to Tower).
     */
    public BasicTower(int posX, int posY) {
        super(posX, posY);
        damage = damageLevel[0]; // Damage dealt per hit
        range = rangeLevel[0]; // Radius of attack area in pixels
        cooldown = cooldownLevel[0]; // Cooldown in ticks (33ms, 30 ticks = 1 second)
        timer = cooldown;
        updateImage();
    }

    /**
     * Loads the tower's image and relevant information to be used when it is drawn.
     */
    private void updateImage() {
        try {
            // The formatting is not necessary since the level starts at 0; just for consistency
            String pathname = String.format("assets/tower0-%d.png", level);
            image = ImageIO.read(new File(pathname));
        
        } catch (IOException e) {
            System.out.println("Womp womp");
            System.out.println(e);
            image = null;
        }

        imageWidth = imageWidthLevel[level];
        imageHeight = imageHeightLevel[level];
        imageOffsetX = imageOffsetXLevel[level];
        imageOffsetY = imageOffsetYLevel[level];
    }

    /**
     * Handles the tower's actions over the next tick.
     */
    @Override
    public void tick(ArrayList<Enemy> enemyList) {
        // Check if the tower will attack this tick
        if (timer > 0) {
            timer -= 1;
            return;
        }

        int targetIndex = -1;
        int longestDistanceTraveled = -1; // Tracks the most advanced enemy in the tower's range

        for (int i = 0; i < enemyList.size(); i++) {
            Enemy enemy = enemyList.get(i);
            int enemyX = enemy.getPosX();
            int enemyY = enemy.getPosY();
            int enemyHp = enemy.getHp();
            int enemyDistanceTraveled = enemy.getDistanceTraveled();

            if (inRange(enemyX, enemyY) && enemyHp > 0
                && enemyDistanceTraveled > longestDistanceTraveled) {
                targetIndex = i;
                longestDistanceTraveled = enemyDistanceTraveled;
            }
        }

        // Skip enemy attack logic if no enemy in range
        if (targetIndex == -1) {
            return;
        }

        timer = cooldown;
        // Damage logic occurs inside Enemy object
        enemyList.get(targetIndex).dealDamage(damage);
        System.out.println("Dealt " + damage + " damage");
    }

    /**
     * Used by tick() to check if an enemy is within the tower's attack area.
     */
    boolean inRange(int enemyX, int enemyY) {
        double distanceX = Math.abs(enemyX - posX);
        double distanceY = Math.abs(enemyY - posY);
        double distance = Math.pow(Math.pow(distanceX, 2) + Math.pow(distanceY, 2), 0.5);
        return distance <= range;
    }
}