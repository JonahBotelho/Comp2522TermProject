package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.paint.Color;

/**
 * Represents a red orb in the game. Red orbs are dangerous and cause the player to lose the game upon collision.
 */
public final class RedOrb
        extends Orb
{
    /**
     * The color of the red orb.
     */
    private static final Color RED_ORB_COLOR = Color.RED;

    /**
     * Constructs a new RedOrb at the specified coordinates with the given speed components.
     *
     * @param x      The x-coordinate of the red orb's initial position.
     * @param y      The y-coordinate of the red orb's initial position.
     * @param speedX The horizontal speed component of the red orb.
     * @param speedY The vertical speed component of the red orb.
     */
    public RedOrb(final double x,
                  final double y,
                  final double speedX,
                  final double speedY,
                  final double speedModifier)
    {
        super(x, y, MainGame.ORB_SIZE, RED_ORB_COLOR, speedX, speedY, speedModifier);
    }
}