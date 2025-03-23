package ca.bcit.termproject.customgame.orbs;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents an orb in the game. This is an abstract class that serves as the base for different types of orbs.
 * Each orb has a position, size, color, and speed. The orb moves downward at its specified speed.
 *
 * @author Jonah Botelho
 */
public abstract class Orb extends Circle
{
    /** The speed at which the orb moves downward. */
    private final double speed;

    /**
     * Constructs a new Orb at the specified coordinates with the given properties.
     *
     * @param x      The x-coordinate of the orb's initial position.
     * @param y      The y-coordinate of the orb's initial position.
     * @param radius The radius of the orb.
     * @param color  The color of the orb.
     * @param speed  The speed at which the orb moves downward.
     */
    public Orb(final double x, final double y, final double radius, final Color color, final double speed)
    {
        super(x, y, radius, color);
        this.speed = speed;
    }

    /**
     * Updates the position of the orb, moving it downward based on its speed.
     */
    public void update()
    {
        setCenterY(getCenterY() + speed);
    }
}