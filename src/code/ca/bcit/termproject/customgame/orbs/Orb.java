package ca.bcit.termproject.customgame.orbs;

import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

/**
 * Represents an orb in the game. This is an abstract class that serves as the base for different types of orbs.
 * Each orb has a position, size, color, and speed components for both horizontal and vertical movement.
 */
public abstract class Orb
        extends Circle
        implements Updatable
{
    private final double speedX;
    private final double speedY;

    /**
     * Constructs a new Orb at the specified coordinates with the given properties.
     *
     * @param x      The x-coordinate of the orb's initial position.
     * @param y      The y-coordinate of the orb's initial position.
     * @param radius The radius of the orb.
     * @param color  The color of the orb.
     * @param speedX The horizontal speed component of the orb.
     * @param speedY The vertical speed component of the orb.
     */
    public Orb(final double x,
               final double y,
               final double radius,
               final Color color,
               final double speedX,
               final double speedY,
               final double speedModifier)
    {
        super(x, y, radius, color);
        this.speedX = speedX * speedModifier;
        this.speedY = speedY * speedModifier;
       this.getStyleClass().add("orb");
    }

    /**
     * Updates the position of the orb based on its speed components.
     */
    public void update()
    {
        setCenterX(getCenterX() + speedX);
        setCenterY(getCenterY() + speedY);
    }
}