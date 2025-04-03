package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.util.Objects;

/**
 * Green Orb implementation using an image.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class GreenOrb extends Orb
{

    private static final String IMAGE_PATH = "/res/customgame/images/green_orb_clock.png";;
    private static final Paint ORB_PAINT = loadOrbPaint();;

    // Image pattern configuration constants
    private static final double IMAGE_PATTERN_ANCHOR_X = 0.0;
    private static final double IMAGE_PATTERN_ANCHOR_Y = 0.0;
    private static final double IMAGE_PATTERN_WIDTH = 1.0;
    private static final double IMAGE_PATTERN_HEIGHT = 1.0;
    private static final boolean IMAGE_PATTERN_PROPORTIONAL = true;

    // Error message constants
    private static final String IMAGE_LOAD_WARNING = "Warning: GreenOrb image failed to load: ";
    private static final String IMAGE_LOAD_ERROR = "Error loading GreenOrb image resource: ";
    private static final String FALLBACK_MESSAGE = ". Using fallback color.";

    /**
     * Loads the image and creates an ImagePattern, or returns a fallback color.
     *
     * @return Paint object for the orb
     */
    private static Paint loadOrbPaint()
    {
        final Image orbImage;
        Paint result;

        try
        {
            orbImage = new Image(Objects.requireNonNull(GreenOrb.class.getResourceAsStream(IMAGE_PATH)));

            if (!orbImage.isError())
            {
                result = new ImagePattern(
                        orbImage,
                        IMAGE_PATTERN_ANCHOR_X,
                        IMAGE_PATTERN_ANCHOR_Y,
                        IMAGE_PATTERN_WIDTH,
                        IMAGE_PATTERN_HEIGHT,
                        IMAGE_PATTERN_PROPORTIONAL
                );
                return result;
            }

            System.err.println(IMAGE_LOAD_WARNING + IMAGE_PATH + FALLBACK_MESSAGE);
        } catch (Exception e)
        {
            System.err.println(IMAGE_LOAD_ERROR + IMAGE_PATH + FALLBACK_MESSAGE);
        }

        result = Color.LIMEGREEN;
        return result;
    }

    /**
     * Constructs a new GreenOrb with specified position and movement parameters.
     *
     * @param x        initial x-coordinate center
     * @param y        initial y-coordinate center
     * @param speedX   base horizontal speed
     * @param speedY   base vertical speed
     * @param speedMod multiplier for base speeds
     */
    public GreenOrb(final double x,
                    final double y,
                    final double speedX,
                    final double speedY,
                    final double speedMod)
    {
        super(x, y, MainGame.ORB_SIZE, ORB_PAINT, speedX, speedY, speedMod);
    }
}