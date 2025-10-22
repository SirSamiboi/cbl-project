/**
 * Framework for handling all animations.
 */

public class Animation {
    protected int timer = 0; // Number of ticks elapsed since animation start
    protected int duration; // Duration of animation in ticks
    protected String id; // ID of animation

    int getTimer() {
        return timer;
    }

    int getDuration() {
        return duration;
    }

    String getId() {
        return id;
    }

    /**
     * Updates the animation to the next frame.
     */
    int[] step() {
        timer += 1;
        return new int[] {};
    }
}
