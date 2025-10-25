/**
 * Button used to begin playing the game.
 */

public class PlayButton extends Button {
    /**
     * Constructor.
     * @param posX 
     *      X-position of top-left corner.
     * @param posY
     *      Y-position of top-left corner.
     */
    public PlayButton(int posX, int posY) {
        this.posX = posX;
        this.posY = posY;
        
        this.text = "Start the Game";
    }
}
