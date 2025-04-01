package ca.bcit.termproject.customgame.orbs;

import ca.bcit.termproject.customgame.MainGame;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.paint.Paint;

import java.util.Objects;

/**
 * Simplified Blue Orb implementation using an image.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class BlueOrb extends Orb
{

    private static final String IMAGE_PATH = "/res/blue_orb_clock.png";;
    private static final Paint ORB_PAINT = loadOrbPaint();;

    // Image pattern parameters
    private static final double IMAGE_PATTERN_ANCHOR_X = 0.0;
    private static final double IMAGE_PATTERN_ANCHOR_Y = 0.0;
    private static final double IMAGE_PATTERN_WIDTH = 1.0;
    private static final double IMAGE_PATTERN_HEIGHT = 1.0;
    private static final boolean IMAGE_PATTERN_PROPORTIONAL = true;

    /**
     * Loads the blue orb image and creates an ImagePattern, or returns a fallback blue color.
     *
     * @return Paint object for the blue orb
     */
    private static Paint loadOrbPaint()
    {
        final Image orbImage;
        Paint result;

        try
        {
            orbImage = new Image(Objects.requireNonNull(BlueOrb.class.getResourceAsStream(IMAGE_PATH)));

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

            System.err.println("Warning: BlueOrb image failed to load: " + IMAGE_PATH
                    + ". Error: " + orbImage.getException() + ". Using fallback color.");
        } catch (Exception e)
        {
            System.err.println("Error loading BlueOrb image resource: "
                    + IMAGE_PATH + ". Using fallback color.");
        }

        result = Color.DEEPSKYBLUE;
        return result;
    }

    /**
     * Constructs a new BlueOrb with specified position and movement parameters.
     *
     * @param x        initial x-coordinate center
     * @param y        initial y-coordinate center
     * @param speedX   base horizontal speed
     * @param speedY   base vertical speed
     * @param speedMod multiplier for base speeds
     */
    public BlueOrb(final double x,
                   final double y,
                   final double speedX,
                   final double speedY,
                   final double speedMod)
    {
        super(x, y, MainGame.ORB_SIZE, ORB_PAINT, speedX, speedY, speedMod);
    }
}