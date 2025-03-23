package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.paint.Color;

/**
 * Represents a blue orb in the game. Blue orbs are harmless and disappear when collided with by the player.
 * This class extends the {@link Orb} class and defines specific properties for blue orbs.
 *
 * @author Jonah Botelho
 */
public class BlueOrb extends Orb
{
    /** The speed at which the blue orb moves downward. */
    private static final double BLUE_ORB_SPEED = 1;

    /** The color of the blue orb. */
    private static final Color BLUE_ORB_COLOR = Color.BLUE;

    /**
     * Constructs a new BlueOrb at the specified coordinates.
     *
     * @param x The x-coordinate of the blue orb's initial position.
     * @param y The y-coordinate of the blue orb's initial position.
     */
    public BlueOrb(final double x, final double y)
    {
        super(x, y, MainGame.ORB_SIZE, BLUE_ORB_COLOR, BLUE_ORB_SPEED);
    }
}