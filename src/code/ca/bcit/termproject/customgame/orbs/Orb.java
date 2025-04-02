package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
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
    
    private static final int NOTHING = 0;
    
    private static final double INITIAL_POSITION_X = 0.0;
    private static final double INITIAL_POSITION_Y = 0.0;
    private static final double INITIAL_RADIUS = 1.0;
    
    // validation
    public static final double MAX_BASE_SPEED = 1000.0;
    public static final double MIN_BASE_SPEED = -1000.0;
    public static final double MAX_SPEED_MODIFIER = 1000.0;
    public static final double MIN_SPEED_MODIFIER = -1000.0;
    
    /**
     * Constructs a basic Orb with specified parameters.
     *
     * @param x             initial x-coordinate center
     * @param y             initial y-coordinate center
     * @param radius        orb radius
     * @param fillPaint     the Paint (Color or ImagePattern) for the orb's fill
     * @param baseSpeedX    base horizontal speed
     * @param baseSpeedY    base vertical speed
     * @param speedModifier multiplier for base speeds
     */
    protected Orb(final double x,
                  final double y,
                  final double radius,
                  final Paint fillPaint,
                  final double baseSpeedX,
                  final double baseSpeedY,
                  final double speedModifier)
    {
        super(INITIAL_POSITION_X, INITIAL_POSITION_Y, INITIAL_RADIUS);
        
        validateX(x);
        validateY(y);
        validateRadius(radius);
        validateFillPaint(fillPaint);
        validateBaseSpeed(baseSpeedX);
        validateBaseSpeed(baseSpeedY);
        validateSpeedModifier(speedModifier);
        
        
        final double calculatedSpeedX;
        final double calculatedSpeedY;
        
        setCenterX(x);
        setCenterY(y);
        setRadius(radius);
        setFill(fillPaint);
        
        calculatedSpeedX = baseSpeedX * speedModifier;
        calculatedSpeedY = baseSpeedY * speedModifier;
        
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
    
    /**
     * Validates the x-coordinate to ensure it is within the allowed bounds.
     *
     * @param x the x-coordinate to validate
     */
    public static final void validateX(final double x)
    {
        if (x < NOTHING || x > MainGame.WINDOW_WIDTH)
        {
            throw new IllegalArgumentException("x out of bounds");
        }
    }
    
    /**
     * Validates the y-coordinate to ensure it is within the allowed bounds.
     *
     * @param y the y-coordinate to validate
     */
    public static final void validateY(final double y)
    {
        if (y < NOTHING || y > MainGame.WINDOW_WIDTH)
        {
            throw new IllegalArgumentException("y out of bounds");
        }
    }
    
    /**
     * Validates the radius to ensure it is a positive number.
     *
     * @param radius the radius to validate
     */
    public static final void validateRadius(final double radius)
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
    public static final void validateFillPaint(final Paint fill)
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
    public static final void validateBaseSpeed(final double baseSpeed)
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
    public static final void validateSpeedModifier(final double speedModifier)
    {
        if (speedModifier > MAX_SPEED_MODIFIER || speedModifier < MIN_SPEED_MODIFIER)
        {
            throw new IllegalArgumentException("invalid speed modifier");
        }
    }
}