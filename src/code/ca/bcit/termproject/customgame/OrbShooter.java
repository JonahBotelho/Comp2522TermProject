package ca.bcit.termproject.customgame;

import ca.bcit.termproject.customgame.orbs.BlueOrb;
import ca.bcit.termproject.customgame.orbs.GreenOrb;
import ca.bcit.termproject.customgame.orbs.Orb;
import ca.bcit.termproject.customgame.orbs.RedOrb;
import javafx.scene.layout.Pane;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Represents a cannon that shoots orbs from all sides of the screen toward the center.
 * The orbs are created randomly and move toward the center of the game window. The cannon
 * controls the generation and shooting of these orbs based on probability and speed.
 * <p>
 * Orbs can be generated from any of the four edges of the screen (top, right, bottom, or left).
 * The speed of the orbs can be adjusted using a speed modifier. The cannon shoots orbs based on a
 * predefined probability, and the orbs can be of different types, such as red, green, or blue.
 * </p>
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class OrbShooter
{
    // Orb Shooting Configuration
    private static final int RANDOM_ORB_GENERATION_MAX  = 5;
    private static final int ORB_SHOOT_PROBABILITY_MAX  = 100;
    private static final int ORB_SHOOT_PROBABILITY      = 2;
    private static final double ORB_SPEED               = 3;

    // Orb generation probability
    private static final int GENERATE_RED_ORB           = 0;
    private static final int GENERATE_GREEN_ORB         = 1;
    private static final int GENERATE_BLUE_ORB          = 2;

    // Screen edge positions
    private static final double RADIUS_OF_SCREEN_CONSTANT   = 2.0;
    private static final int NUMBER_OF_EDGES                = 4;
    private static final int LEFT_EDGE                      = 0;
    private static final int RIGHT_EDGE                     = ClockStormMain.WINDOW_WIDTH_PX;
    private static final int TOP_EDGE                       = 0;
    private static final int BOTTOM_EDGE                    = ClockStormMain.WINDOW_HEIGHT_PX;

    // Edge choosing probability
    private static final int CHOOSE_TOP_EDGE                = 0;
    private static final int CHOOSE_RIGHT_EDGE              = 1;
    private static final int CHOOSE_BOTTOM_EDGE             = 2;
    private static final int CHOOSE_LEFT_EDGE               = 3;

    // Cannon State
    private final List<Orb> orbs;
    private final Random random;

    /**
     * Constructs a new Cannon.
     * Initializes the list of orbs and the random number generator for shooting orbs.
     */
    public OrbShooter()
    {
        this.orbs   = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Shoots an orb from a random edge if the probability condition is met.
     * The orb is added to the provided root pane, and its speed is adjusted by the provided modifier.
     *
     * @param root The root pane to which the orb will be added.
     * @param speedModifier The modifier that scales the speed of the orb.
     */
    public void shootOrb(final Pane root,
                         final double speedModifier)
    {
        validateRoot(root);
        Orb.validateSpeedModifier(speedModifier);

        if (random.nextInt(ORB_SHOOT_PROBABILITY_MAX) < ORB_SHOOT_PROBABILITY)
        {
            // Randomly select which edge to spawn from (0=top, 1=right, 2=bottom, 3=left)
            int edge;
            double x;
            double y;
            double speedX;
            double speedY;
            double centerX;
            double centerY;

            edge    = random.nextInt(NUMBER_OF_EDGES);
            centerX = ClockStormMain.WINDOW_WIDTH_PX / RADIUS_OF_SCREEN_CONSTANT;
            centerY = ClockStormMain.WINDOW_HEIGHT_PX / RADIUS_OF_SCREEN_CONSTANT;

            switch (edge)
            {
                case CHOOSE_TOP_EDGE:
                    x = random.nextInt(ClockStormMain.WINDOW_WIDTH_PX);
                    y = TOP_EDGE;
                    speedX = (centerX - x) * ORB_SPEED / ClockStormMain.WINDOW_WIDTH_PX;
                    speedY = ORB_SPEED;
                    break;
                case CHOOSE_RIGHT_EDGE:
                    x = RIGHT_EDGE;
                    y = random.nextInt(ClockStormMain.WINDOW_HEIGHT_PX);
                    speedX = -ORB_SPEED;
                    speedY = (centerY - y) * ORB_SPEED / ClockStormMain.WINDOW_HEIGHT_PX;
                    break;
                case CHOOSE_BOTTOM_EDGE:
                    x = random.nextInt(ClockStormMain.WINDOW_WIDTH_PX);
                    y = BOTTOM_EDGE;
                    speedX = (centerX - x) * ORB_SPEED / ClockStormMain.WINDOW_WIDTH_PX;
                    speedY = -ORB_SPEED;
                    break;
                case CHOOSE_LEFT_EDGE:
                    x = LEFT_EDGE;
                    y = random.nextInt(ClockStormMain.WINDOW_HEIGHT_PX);
                    speedX = ORB_SPEED;
                    speedY = (centerY - y) * ORB_SPEED / ClockStormMain.WINDOW_HEIGHT_PX;
                    break;
                default:
                    throw new IllegalStateException("Invalid edge selection");
            }

            // Apply speed modifier
            speedX *= speedModifier;
            speedY *= speedModifier;

            // Create the orb with the calculated position and speed components
            Orb orb = createRandomOrb(x, y, speedX, speedY);

            // Add the orb to the list and the scene graph
            orbs.add(orb);
            root.getChildren().add(orb);
        }
    }

    /**
     * Creates a random orb with the specified position and speed components.
     * The orb's type is randomly selected from a set of predefined types.
     *
     * @param x The x-coordinate of the orb's position.
     * @param y The y-coordinate of the orb's position.
     * @param speedX The x-component of the orb's speed.
     * @param speedY The y-component of the orb's speed.
     * @return The created orb, which can be of different types (red, green, blue).
     */
    private Orb createRandomOrb(final double x,
                                final double y,
                                final double speedX,
                                final double speedY)
    {
        Orb.validateX(x);
        Orb.validateY(y);
        Orb.validateBaseSpeed(x);
        Orb.validateBaseSpeed(y);
        Orb.validateSpeedModifier(speedX);

        int orbType;
        orbType = random.nextInt(RANDOM_ORB_GENERATION_MAX);

        return switch (orbType)
        {
            // intentional redundancy, making it easier to switch orb probabilities
            case GENERATE_RED_ORB -> {
                yield new RedOrb(x, y, speedX, speedY);
            }
            case GENERATE_GREEN_ORB -> {
                yield new GreenOrb(x, y, speedX, speedY);
            }
            case GENERATE_BLUE_ORB -> {
                yield new BlueOrb(x, y, speedX, speedY);
            }
            default -> {
                yield new RedOrb(x, y, speedX, speedY);
            }
        };

    }

    /**
     * Returns the list of orbs currently in the game.
     *
     * @return The list of orbs that are currently in the game.
     */
    public List<Orb> getOrbs()
    {
        return orbs;
    }

    /**
     * Validates that the root pane is not null.
     *
     * @param root The root pane to validate.
     */
    private static void validateRoot(final Pane root)
    {
        if (root == null)
        {
            throw new IllegalArgumentException("Root cannot be null");
        }
    }

}
