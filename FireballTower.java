import java.io.File;
import java.io.IOException;
import java.util.*;
import javax.imageio.ImageIO;

/**
 * Fireball tower type, which deals damage to a group of enemies.
 * These are best suited to clear large hordes of enemies.
 */

class FireballTower extends Tower {
    private final int[] damageLevel = {8, 12, 18, 24}; // Index 0 holds damage dealt at level 1
    private final int[] rangeLevel = {150, 160, 170, 180};
    private final int[] cooldownLevel = {60, 50, 40, 30};
    private final int[] upgradeCostLevel = {150, 250, 500};
    private final int[] imageWidthLevel = {64, 64, 64, 64};
    private final int[] imageHeightLevel = {128, 128, 128, 128};
    private final int[] imageOffsetXLevel = {0, 0, 0, 0};
    private final int[] imageOffsetYLevel = {-30, -30, -30, -30};

    private final int attackRadius = 80; // Radius of the area in which the attack deals damage

    /**
     * Constructor.
     */
    public FireballTower(int posX, int posY) {
        super(posX, posY);
        this.damage = damageLevel[0]; // Damage dealt per hit
        this.range = rangeLevel[0]; // Radius of attack area in pixels
        this.maxLevel = 3;
        this.cooldown = cooldownLevel[0]; // Cooldown in ticks
        this.upgradeCost = upgradeCostLevel[0];
        this.timer = 0;
        updateImage();
        attackClip = loadSound("fireballAttack.wav");
    }

    /**
     * Loads the tower's image and relevant information to be used when it is drawn.
     */
    private void updateImage() {
        try {
            String fileSeparator = File.separator;
            String pathname = String.format("assets%stower1-%d.png", fileSeparator, level);
            image = ImageIO.read(new File(pathname));
        
        } catch (IOException e) {
            System.out.println("ERROR: Level " + level + " tower image could not be loaded");
            System.out.println(e);
            image = null;
        }

        imageWidth = imageWidthLevel[level];
        imageHeight = imageHeightLevel[level];
        imageOffsetX = imageOffsetXLevel[level];
        imageOffsetY = imageOffsetYLevel[level];
    }

    /**
     * Increases the level of a tower, and updates its statistics and image.
     */
    @Override
    public void levelUp() {
        if (level == maxLevel) {
            return;
        }

        level++;

        damage = damageLevel[level];
        range = rangeLevel[level];
        cooldown = cooldownLevel[level];
        upgradeCost = upgradeCostLevel[Math.min(level, maxLevel - 1)];
        timer -= cooldownLevel[level - 1] - cooldown; // Account for cooldown reduction
        updateImage();
    }

    /**
     * Handles the tower's actions over the next tick.
     */
    @Override
    public void tick(ArrayList<Enemy> enemyList, ArrayList<Animation> animationList) {
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

        Enemy targetEnemy = enemyList.get(targetIndex);
        int targetX = enemyList.get(targetIndex).getPosX();
        int targetY = enemyList.get(targetIndex).getPosY();

        timer = cooldown;
        // Create attack animation
        animationList.add(new FireballAnimation(posX, posY - 64, targetEnemy, level));
        // Play attack sound
        playAttackSound();

        // Deal damage to all enemies in area of attack
        for (Enemy enemy : enemyList) {
            int enemyX = enemy.getPosX();
            int enemyY = enemy.getPosY();

            if (inArea(enemyX, enemyY, targetX, targetY)) {
                enemy.dealDamage(damage);
            }
        }
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

    /**
     * Used by tick() to check if an enemy is within the area of effect of an attack.
     */
    boolean inArea(int enemyX, int enemyY, int targetX, int targetY) {
        double distanceX = Math.abs(enemyX - targetX);
        double distanceY = Math.abs(enemyY - targetY);
        double distance = Math.pow(Math.pow(distanceX, 2) + Math.pow(distanceY, 2), 0.5);
        return distance <= attackRadius;
    }
}