package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.ClockStormMain;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;

/**
 * Abstract base class for Orbs in the game.
 * Extends JavaFX Circle and handles basic movement.
 * Provides validation for coordinates, radius, paint, and speed.
 * All orbs move based on a constant speed set at construction.
 * <p>
 * Subclasses determine the orb's visual appearance and behavior.
 * Implements the Updatable interface for use in game loops.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public abstract class Orb extends Circle implements Updatable
{
    protected final double speedX;
    protected final double speedY;

    private static final int NOTHING = 0;

    private static final double INITIAL_POSITION_X = 0.0;
    private static final double INITIAL_POSITION_Y = 0.0;
    private static final double INITIAL_RADIUS     = 1.0;

    /** Maximum allowed base speed for an orb's movement. */
    public static final double MAX_BASE_SPEED       = 1000.0;
    /** Minimum allowed base speed for an orb's movement. */
    public static final double MIN_BASE_SPEED       = -1000.0;
    /** Maximum allowed speed modifier that can be applied to an orb. */
    public static final double MAX_SPEED_MODIFIER   = 1000.0;
    /** Minimum allowed speed modifier that can be applied to an orb. */
    public static final double MIN_SPEED_MODIFIER   = -1000.0;

    /**
     * Constructs a basic Orb with specified parameters.
     *
     * @param x          initial x-coordinate center
     * @param y          initial y-coordinate center
     * @param radius     orb radius
     * @param fillPaint  the Paint (Color or ImagePattern) for the orb's fill
     * @param baseSpeedX base horizontal speed
     * @param baseSpeedY base vertical speed
     */
    protected Orb(final double x,
                  final double y,
                  final double radius,
                  final Paint fillPaint,
                  final double baseSpeedX,
                  final double baseSpeedY)
    {
        super(INITIAL_POSITION_X, INITIAL_POSITION_Y, INITIAL_RADIUS);

        validateX(x);
        validateY(y);
        validateRadius(radius);
        validateFillPaint(fillPaint);
        validateBaseSpeed(baseSpeedX);
        validateBaseSpeed(baseSpeedY);

        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setFill(fillPaint);

        this.speedX = baseSpeedX;
        this.speedY = baseSpeedY;
    }

    /**
     * Updates the orb's position based on its current speed.
     */
    @Override
    public final void update()
    {
        final double newCenterX = getCenterX() + this.speedX;
        final double newCenterY = getCenterY() + this.speedY;

        setCenterX(newCenterX);
        setCenterY(newCenterY);
    }

    /**
     * Validates the x-coordinate to ensure it is within the allowed bounds.
     *
     * @param x the x-coordinate to validate
     */
    public static void validateX(final double x)
    {
        if (x < NOTHING || x > ClockStormMain.WINDOW_WIDTH_PX)
        {
            throw new IllegalArgumentException("x out of bounds");
        }
    }

    /**
     * Validates the y-coordinate to ensure it is within the allowed bounds.
     *
     * @param y the y-coordinate to validate
     */
    public static void validateY(final double y)
    {
        if (y < NOTHING || y > ClockStormMain.WINDOW_WIDTH_PX)
        {
            throw new IllegalArgumentException("y out of bounds");
        }
    }

    /**
     * Validates the radius to ensure it is a positive number.
     *
     * @param radius the radius to validate
     */
    public static void validateRadius(final double radius)
    {
        if (radius < NOTHING)
        {
            throw new IllegalArgumentException("radius must be a positive number");
        }
    }

    /**
     * Validates the fill paint object to ensure it is not null.
     *
     * @param fill the Paint object to validate
     */
    public static void validateFillPaint(final Paint fill)
    {
        if (fill == null)
        {
            throw new IllegalArgumentException("fill must not be null");
        }
    }

    /**
     * Validates the base speed to ensure it falls within the allowed range.
     *
     * @param baseSpeed the base speed to validate
     */
    public static void validateBaseSpeed(final double baseSpeed)
    {
        if (baseSpeed > MAX_BASE_SPEED || baseSpeed < MIN_BASE_SPEED)
        {
            throw new IllegalArgumentException("base speed out of bounds");
        }
    }

    /**
     * Validates the speed modifier to ensure it falls within the allowed range.
     *
     * @param speedModifier the speed modifier to validate
     */
    public static void validateSpeedModifier(final double speedModifier)
    {
        if (speedModifier > MAX_SPEED_MODIFIER || speedModifier < MIN_SPEED_MODIFIER)
        {
            throw new IllegalArgumentException("invalid speed modifier");
        }
    }
}
