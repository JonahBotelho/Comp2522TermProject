package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.paint.Color;

/**
 * Represents a red orb in the game. Red orbs are dangerous and cause the player to lose the game upon collision.
 * This class extends the {@link Orb} class and defines specific properties for red orbs.
 *
 * @author Jonah Botelho
 */
public class RedOrb extends Orb
{
    /** The speed at which the red orb moves downward. */
    private static final double RED_ORB_SPEED = 3;

    /** The color of the red orb. */
    private static final Color RED_ORB_COLOR = Color.RED;

    /**
     * Constructs a new RedOrb at the specified coordinates.
     *
     * @param x The x-coordinate of the red orb's initial position.
     * @param y The y-coordinate of the red orb's initial position.
     */
    public RedOrb(final double x, final double y)
    {
        super(x, y, MainGame.ORB_SIZE, RED_ORB_COLOR, RED_ORB_SPEED);
    }
}