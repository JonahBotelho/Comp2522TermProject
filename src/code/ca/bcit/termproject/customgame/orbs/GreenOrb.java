package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.paint.Color;

/**
 * Represents a green orb in the game. Green orbs are harmless and disappear when collided with by the player.
 */
public final class GreenOrb extends Orb
{
    /**
     * The color of the green orb.
     */
    private static final Color GREEN_ORB_COLOR = Color.GREEN;

    /**
     * Constructs a new GreenOrb at the specified coordinates with the given speed components.
     *
     * @param x      The x-coordinate of the green orb's initial position.
     * @param y      The y-coordinate of the green orb's initial position.
     * @param speedX The horizontal speed component of the green orb.
     * @param speedY The vertical speed component of the green orb.
     */
    public GreenOrb(final double x,
                    final double y,
                    final double speedX,
                    final double speedY,
                    final double speedModifier)
    {
        super(x, y, MainGame.ORB_SIZE, GREEN_ORB_COLOR, speedX, speedY, speedModifier);
    }
}