package ca.bcit.termproject.customgame.orbs;

import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Abstract base class for Orbs in the game.
 * Extends JavaFX Circle and handles basic movement.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public abstract class Orb extends Circle implements Updatable
{

    protected final double speedX;
    protected final double speedY;

    private static final double INITIAL_POSITION_X = 0.0;
    private static final double INITIAL_POSITION_Y = 0.0;
    private static final double INITIAL_RADIUS = 1.0;
    private static final double SPEED_MULTIPLIER = 1.0;

    /**
     * Constructs a basic Orb with specified parameters.
     *
     * @param x          initial x-coordinate center
     * @param y          initial y-coordinate center
     * @param radius     orb radius
     * @param fillPaint  the Paint (Color or ImagePattern) for the orb's fill
     * @param baseSpeedX base horizontal speed
     * @param baseSpeedY base vertical speed
     * @param speedMod   multiplier for base speeds
     */
    protected Orb(final double x,
                  final double y,
                  final double radius,
                  final Paint fillPaint,
                  final double baseSpeedX,
                  final double baseSpeedY,
                  final double speedMod)
    {
        super(INITIAL_POSITION_X, INITIAL_POSITION_Y, INITIAL_RADIUS);

        final double calculatedSpeedX;
        final double calculatedSpeedY;

        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setFill(fillPaint);

        calculatedSpeedX = baseSpeedX * speedMod;
        calculatedSpeedY = baseSpeedY * speedMod;

        this.speedX = calculatedSpeedX;
        this.speedY = calculatedSpeedY;
    }

    /**
     * Updates the orb's position based on its current speed.
     */
    @Override
    public final void update()
    {
        final double newCenterX;
        final double newCenterY;

        newCenterX = getCenterX() + this.speedX;
        newCenterY = getCenterY() + this.speedY;

        setCenterX(newCenterX);
        setCenterY(newCenterY);
    }
}