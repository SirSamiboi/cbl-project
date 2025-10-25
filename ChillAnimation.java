/**
 * Animation values for a fireball tower attack.
 * Consists of a solid beam and a circular explosion effect.
 */

public class ChillAnimation extends Animation {
    // RGB color values for each tower level
    private final int[] outerRList = {0, 0, 0, 128};
    private final int[] outerGList = {255, 192, 128, 64};
    private final int[] outerBList = {255, 255, 255, 255};
    
    private final int startX;
    private final int startY;
    private int endX;
    private int endY;
    private final Enemy targetEnemy;
    private int outerWidth;
    private int innerWidth;
    private final int outerR;
    private final int outerG;
    private final int outerB;
    private int squareSize;

    /**
     * Constructor.
     */
    public ChillAnimation(int startX, int startY, Enemy targetEnemy, int level) {
        this.duration = 15 + 4 * level;
        this.id = "2-" + level;
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
    void tick() {
        if (timer < 5) {
            outerWidth = 5;
        } else {
            outerWidth = (int) (5 * (1 - Math.pow((double) (timer - 5) / (duration - 5), 3)));
        }
        innerWidth = Math.max(0, outerWidth - 3);
        squareSize = (int) (100 * (1 - Math.pow((double) timer / duration, 3)));

        endX = targetEnemy.getPosX();
        endY = targetEnemy.getPosY();

        timer += 1;
    }

    @Override
    int[] getValues() {
        return new int[] {startX, startY, endX, endY, outerWidth, innerWidth,
            outerR, outerG, outerB, squareSize};
    }
}
