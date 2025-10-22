/**
 * Attack animation values for basic towers.
 * The animation consists of a colored border
 */

public class BasicAnimation extends Animation {
    // RGB color values for each tower level
    private int[] outerRList = {0, 255, 128};
    private int[] outerGList = {255, 128, 255};
    private int[] outerBList = {255, 0, 0};
    
    private int startX;
    private int startY;
    private int endX;
    private int endY;
    private Enemy targetEnemy;
    private int outerWidth;
    private int innerWidth;
    private int outerR;
    private int outerG;
    private int outerB;

    /**
     * Constructor.
     */
    public BasicAnimation(int startX, int startY, Enemy targetEnemy, int level) {
        this.duration = 15;
        this.id = "0-" + level;
        this.startX = startX;
        this.startY = startY;
        this.endX = targetEnemy.getPosX();
        this.endY = targetEnemy.getPosY();
        this.targetEnemy = targetEnemy;
        this.outerR = outerRList[level];
        this.outerG = outerGList[level];
        this.outerB = outerBList[level];
    }

    /**
     * Updates the animation to the next frame.
     */
    @Override
    int[] step() {
        if (timer < 5) {
            outerWidth = 10;
        } else {
            outerWidth = (int) (10 * (1 - Math.pow((double) (timer - 5) / 10, 3)));
        }
        innerWidth = Math.max(0, outerWidth - 5);

        endX = targetEnemy.getPosX();
        endY = targetEnemy.getPosY();

        timer += 1;
        return new int[] {startX, startY, endX, endY, outerWidth, innerWidth,
            outerR, outerG, outerB};
    }
}
