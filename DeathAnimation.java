/**
 * Animation for the death of an enemy.
 * Consists of a fading text pop-up showing the money earned by the enemy's defeat.
 */

public class DeathAnimation extends Animation {
    private final int posX;
    private final int posY;
    private int money;
    private int size;
    private int opacity; // as %

    /**
     * Constructor.
     */
    public DeathAnimation(int posX, int posY, int money) {
        this.duration = 50;
        this.id = "X";
        this.posX = posX;
        this.posY = posY;
        this.money = money;
    }

    /**
     * Updates the animation to the next frame.
     */
    @Override
    int[] step() {
        size = (int) (7 + Math.log(money) * 2);
        opacity = (int) 100 - 2 * timer;
        timer += 1;

        return new int[] {posX, posY, money, size, opacity};
    }
}
