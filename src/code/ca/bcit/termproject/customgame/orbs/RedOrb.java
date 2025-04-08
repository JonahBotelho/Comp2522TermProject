package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.ClockStormMain;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.util.Objects;

/**
 * Represents a Red Orb in the game, which uses an image for its visual appearance.
 * If the image fails to load, it falls back to a default red color.
 * This orb adheres to the movement and collision rules defined in the parent Orb class.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class RedOrb extends Orb
{
    private static final String IMAGE_PATH          = "/res/customgame/images/red_orb_clock.png";
    private static final Paint  ORB_PAINT           = loadOrbPaint();

    // Image pattern parameters
    private static final double IMAGE_PATTERN_ANCHOR_HORIZONTAL = 0.0;
    private static final double IMAGE_PATTERN_ANCHOR_VERTICAL   = 0.0;
    private static final double IMAGE_PATTERN_WIDTH_RATIO       = 1.0;
    private static final double IMAGE_PATTERN_HEIGHT_RATIO      = 1.0;
    private static final boolean IMAGE_PATTERN_PROPORTIONAL     = true;

    /**
     * Constructs a new RedOrb with the specified position and movement parameters.
     * Validates all parameters using Orb's validation methods before initialization.
     * The orb's appearance is determined by the loaded image or fallback color.
     *
     * @param x      the initial x-coordinate of the orb's center in pixels
     * @param y      the initial y-coordinate of the orb's center in pixels
     * @param speedX the base horizontal speed of the orb in pixels per frame
     * @param speedY the base vertical speed of the orb in pixels per frame
     */
    public RedOrb(final double x,
                  final double y,
                  final double speedX,
                  final double speedY)
    {
        Orb.validateX(x);
        Orb.validateY(y);
        Orb.validateBaseSpeed(speedX);
        Orb.validateBaseSpeed(speedY);

        super(x, y, ClockStormMain.ORB_SIZE, ORB_PAINT, speedX, speedY);
    }

    /**
     * Loads and prepares the paint object for the red orb's appearance.
     * Attempts to load the image from the specified path. If successful, creates an ImagePattern
     * with the configured anchor points and scaling. If loading fails, silently returns a
     * fallback red color.
     *
     * @return a Paint object representing either the loaded image pattern or fallback color
     */
    private static Paint loadOrbPaint()
    {
        final Image orbImage;
        Paint       result;

        try
        {
            orbImage = new Image(Objects.requireNonNull(RedOrb.class.getResourceAsStream(IMAGE_PATH)));

            if (!orbImage.isError())
            {
                result = new ImagePattern(
                        orbImage,
                        IMAGE_PATTERN_ANCHOR_HORIZONTAL,
                        IMAGE_PATTERN_ANCHOR_VERTICAL,
                        IMAGE_PATTERN_WIDTH_RATIO,
                        IMAGE_PATTERN_HEIGHT_RATIO,
                        IMAGE_PATTERN_PROPORTIONAL
                );
                return result;
            }
        }
        catch (final Exception ignored)
        {
            // Silently fall through to return default color
        }

        return Color.RED;
    }
}