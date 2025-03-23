package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.paint.Color;

/**
 * Represents a green orb in the game. Green orbs are harmless and disappear when collided with by the player.
 * This class extends the {@link Orb} class and defines specific properties for green orbs.
 *
 * @author Jonah Botelho
 */
public class GreenOrb extends Orb
{
    /** The speed at which the green orb moves downward. */
    private static final double GREEN_ORB_SPEED = 2;

    /** The color of the green orb. */
    private static final Color GREEN_ORB_COLOR = Color.GREEN;

    /**
     * Constructs a new GreenOrb at the specified coordinates.
     *
     * @param x The x-coordinate of the green orb's initial position.
     * @param y The y-coordinate of the green orb's initial position.
     */
    public GreenOrb(final double x, final double y)
    {
        super(x, y, MainGame.ORB_SIZE, GREEN_ORB_COLOR, GREEN_ORB_SPEED);
    }
}