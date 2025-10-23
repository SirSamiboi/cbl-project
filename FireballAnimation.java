/**
 * Attack animation values for basic towers.
 * The animation consists of a colored border
 */

public class FireballAnimation extends Animation {
    // RGB color values for each tower level
    private int[] outerRList = {255, 255, 255, 255};
    private int[] outerGList = {196, 128, 0, 0};
    private int[] outerBList = {0, 0, 0, 0};
    
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
    private int circleSize;

    /**
     * Constructor.
     */
    public FireballAnimation(int startX, int startY, Enemy targetEnemy, int level) {
        this.duration = 15;
        this.id = "1-" + level;
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
            outerWidth = 6;
        } else {
            outerWidth = (int) (6 * (1 - Math.pow((double) (timer - 5) / 10, 3)));
        }
        innerWidth = Math.max(0, outerWidth - 3);
        circleSize = (int) (150 * (1 - Math.pow((double) timer / 15, 3)));

        endX = targetEnemy.getPosX();
        endY = targetEnemy.getPosY();

        timer += 1;
        return new int[] {startX, startY, endX, endY, outerWidth, innerWidth,
            outerR, outerG, outerB, circleSize};
    }
}
