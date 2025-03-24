package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.paint.Color;

/**
 * Represents a blue orb in the game. Blue orbs are harmless and disappear when collided with by the player.
 */
public class BlueOrb extends Orb
{
    /**
     * The color of the blue orb.
     */
    private static final Color BLUE_ORB_COLOR = Color.BLUE;

    /**
     * Constructs a new BlueOrb at the specified coordinates with the given speed components.
     *
     * @param x      The x-coordinate of the blue orb's initial position.
     * @param y      The y-coordinate of the blue orb's initial position.
     * @param speedX The horizontal speed component of the blue orb.
     * @param speedY The vertical speed component of the blue orb.
     */
    public BlueOrb(final double x, final double y, final double speedX, final double speedY)
    {
        super(x, y, MainGame.ORB_SIZE, BLUE_ORB_COLOR, speedX, speedY);
    }
}