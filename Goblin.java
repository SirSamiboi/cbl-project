/**
 * Class that saves the data of a goblin.
 */

public class Goblin extends Enemy {
    /**
     * Constructor for the Goblin class at the starting position.
     * @param posX
     *     Starting position X.
     * @param posY
     *     Starting position Y.
     */

    public Goblin(int posX, int posY) {
        hp = 15;
        speed = 10;
        distanceTraveled = 0;
    }
}