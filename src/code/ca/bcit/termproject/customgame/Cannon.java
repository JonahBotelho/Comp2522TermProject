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
 * Represents a cannon that shoots orbs at random angles. The cannon is positioned at the top of the screen
 * and shoots orbs downward at varying angles.
 */
public class Cannon
{
    // Orb Shooting Configuration
    private static final int ORB_SHOOT_PROBABILITY = 1;
    private static final double ORB_SPEED          = 3;

    // Angle Configuration
    private static final double MIN_ANGLE          = -45;  // 45 degrees to the left
    private static final double MAX_ANGLE          = 45;   // 45 degrees to the right

    // Cannon State
    private final double x;
    private final double y;
    private final List<Orb> orbs;
    private final Random random;

    /**
     * Constructs a new Cannon at the specified coordinates.
     *
     * @param x The x-coordinate of the cannon's position.
     * @param y The y-coordinate of the cannon's position.
     */
    public Cannon(final double x, final double y)
    {
        this.x = x;
        this.y = y;
        this.orbs = new ArrayList<>();
        this.random = new Random();
    }

    /**
     * Shoots an orb at a random angle if the probability condition is met.
     *
     * @param root The root pane to which the orb will be added.
     */
    public void shootOrb(final Pane root, final double speedModifier)
    {
        if (random.nextInt(100) < ORB_SHOOT_PROBABILITY)
        {
            // Generate a random angle between MIN_ANGLE and MAX_ANGLE
            double angle = MIN_ANGLE + (MAX_ANGLE - MIN_ANGLE) * random.nextDouble();

            // Convert the angle from degrees to radians
            double angleInRadians = Math.toRadians(angle);

            // Calculate the horizontal (x) and vertical (y) components of the speed
            double speedX = ORB_SPEED * Math.sin(angleInRadians);
            double speedY = ORB_SPEED * Math.cos(angleInRadians);

            // Create the orb with the calculated speed components
            Orb orb = createRandomOrb(speedX, speedY, speedModifier);

            // Add the orb to the list and the scene graph
            orbs.add(orb);
            root.getChildren().add(orb);
        }
    }

    /**
     * Creates a random orb with the specified speed components.
     *
     * @param speedX The horizontal speed component of the orb.
     * @param speedY The vertical speed component of the orb.
     * @return A new orb of a random type.
     */
    private Orb createRandomOrb(final double speedX, final double speedY, final double speedModifier)
    {
        int orbType = random.nextInt(3);
        switch (orbType)
        {
            case 0:
                return new RedOrb(x, y, speedX, speedY, speedModifier);
            case 1:
                return new GreenOrb(x, y, speedX, speedY, speedModifier);
            case 2:
                return new BlueOrb(x, y, speedX, speedY, speedModifier);
            default:
                return new RedOrb(x, y, speedX, speedY, speedModifier);
        }
    }

    /**
     * Returns the list of orbs currently in the game.
     *
     * @return The list of orbs.
     */
    public List<Orb> getOrbs()
    {
        return orbs;
    }
}