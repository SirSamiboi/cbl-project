/**
 * Framework for handling all animations.
 * 
 * Animation objects perform calculations for things like line width, text color and opacity.
 * They are controlled by their own timer that tracks time until the set duration has passed.
 * On each tick, the timer increments, the animation performs calculations for its values,
 * and it returns its values for the tick as an integer array. Depending on the animation's ID,
 * the paintComponent function in GamePanel will use the given values in different ways.
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
