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
 */
public final class Cannon {
    // Orb Shooting Configuration
    private static final int ORB_SHOOT_PROBABILITY_MAX = 100;
    private static final int ORB_SHOOT_PROBABILITY = 2;
    private static final double ORB_SPEED = 3;
    
    // Screen edge positions
    private static final double RADIUS_OF_SCREEN_CONSTANT = 2.0;
    private static final int NUMBER_OF_EDGES = 4;
    private static final int LEFT_EDGE = 0;
    private static final int RIGHT_EDGE = MainGame.WINDOW_WIDTH;
    private static final int TOP_EDGE = 0;
    private static final int BOTTOM_EDGE = MainGame.WINDOW_HEIGHT;
    
    // Cannon State
    private final List<Orb> orbs;
    private final Random random;
    
    /**
     * Constructs a new Cannon.
     */
    public Cannon() {
        this.orbs = new ArrayList<>();
        this.random = new Random();
    }
    
    /**
     * Shoots an orb from a random edge if the probability condition is met.
     *
     * @param root The root pane to which the orb will be added.
     */
    public void shootOrb(final Pane root, final double speedModifier) {
        if (random.nextInt(ORB_SHOOT_PROBABILITY_MAX) < ORB_SHOOT_PROBABILITY) {
            // Randomly select which edge to spawn from (0=top, 1=right, 2=bottom, 3=left)
            int edge = random.nextInt(NUMBER_OF_EDGES);
            double x, y, speedX, speedY;
            double centerX = MainGame.WINDOW_WIDTH / RADIUS_OF_SCREEN_CONSTANT;
            double centerY = MainGame.WINDOW_HEIGHT / RADIUS_OF_SCREEN_CONSTANT;
            
            switch (edge) {
                case 0: // Top edge
                    x = random.nextInt(MainGame.WINDOW_WIDTH);
                    y = TOP_EDGE;
                    speedX = (centerX - x) * ORB_SPEED / MainGame.WINDOW_WIDTH;
                    speedY = ORB_SPEED;
                    break;
                case 1: // Right edge
                    x = RIGHT_EDGE;
                    y = random.nextInt(MainGame.WINDOW_HEIGHT);
                    speedX = -ORB_SPEED;
                    speedY = (centerY - y) * ORB_SPEED / MainGame.WINDOW_HEIGHT;
                    break;
                case 2: // Bottom edge
                    x = random.nextInt(MainGame.WINDOW_WIDTH);
                    y = BOTTOM_EDGE;
                    speedX = (centerX - x) * ORB_SPEED / MainGame.WINDOW_WIDTH;
                    speedY = -ORB_SPEED;
                    break;
                case 3: // Left edge
                    x = LEFT_EDGE;
                    y = random.nextInt(MainGame.WINDOW_HEIGHT);
                    speedX = ORB_SPEED;
                    speedY = (centerY - y) * ORB_SPEED / MainGame.WINDOW_HEIGHT;
                    break;
                default:
                    throw new IllegalStateException("Invalid edge selection");
            }
            
            // Apply speed modifier
            speedX *= speedModifier;
            speedY *= speedModifier;
            
            // Create the orb with the calculated position and speed components
            Orb orb = createRandomOrb(x, y, speedX, speedY, speedModifier);
            
            // Add the orb to the list and the scene graph
            orbs.add(orb);
            root.getChildren().add(orb);
        }
    }
    
    /**
     * Creates a random orb with the specified position and speed components.
     */
    private Orb createRandomOrb(final double x, final double y,
                                final double speedX, final double speedY,
                                final double speedModifier) {
        int orbType = random.nextInt(3);
        switch (orbType) {
            case 0: return new RedOrb(x, y, speedX, speedY, speedModifier);
            case 1: return new GreenOrb(x, y, speedX, speedY, speedModifier);
            case 2: return new BlueOrb(x, y, speedX, speedY, speedModifier);
            default: return new RedOrb(x, y, speedX, speedY, speedModifier);
        }
    }
    
    /**
     * Returns the list of orbs currently in the game.
     */
    public List<Orb> getOrbs() {
        return orbs;
    }
}