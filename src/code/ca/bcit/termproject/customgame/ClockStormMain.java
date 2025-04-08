package ca.bcit.termproject.customgame;

import ca.bcit.termproject.customgame.orbs.BlueOrb;
import ca.bcit.termproject.customgame.orbs.GreenOrb;
import ca.bcit.termproject.customgame.orbs.RedOrb;
import ca.bcit.termproject.customgame.orbs.Orb;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Iterator;
import java.util.Random;

/**
 * The ClockStormMain class serves as the entry point for the ClockStorm game, handling the core game logic,
 * player interaction, collision detection, scoring, and game-over conditions. It manages the initialization of
 * the game environment, including the setup of the game window, player, and orb shooter, as well as the
 * continuous updates in the game loop.
 * <p>
 * The game follows the player's interaction with falling orbs, and the goal is to avoid red orbs while
 * collecting green and blue orbs to increase the score. The player controls the character using the arrow
 * keys or WASD, and orbs are shot at random intervals with varying speeds.
 * <p>
 * Key features include:
 * - **Orb Collision Handling**: Orbs of various types (red, green, blue) affect the game differently, either
 *   ending the game or adding points to the score.
 * - **Game Over Logic**: If the score falls below a minimum threshold, the game ends, showing the final score
 *   and allowing the player to restart or exit.
 * - **Speed Modifier**: As the score increases, orbs move faster, adding difficulty over time.
 * - **Score System**: A dynamic scoring system penalizes players randomly, decreasing their score occasionally.
 * - **Player Movement**: The player is controlled through both the arrow keys and WASD for flexibility.
 * <p>
 * This class extends `javafx.application.Application` and utilizes JavaFX's `AnimationTimer` to create the
 * game loop, continuously updating the game state and checking for collisions and score updates.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class ClockStormMain
        extends Application
{
    // Game Configuration
    /** The name of the game. */
    public static final String GAME_NAME    = "ClockStorm";
    /** The width of the game window. */
    public static final int WINDOW_WIDTH    = 800;
    /** The height of the game window. */
    public static final int WINDOW_HEIGHT   = 600;

    // Player Configuration
    /** The size of the player character. */
    public static final int PLAYER_SIZE     = 30;
    /** The starting X position of the player. */
    public static final int PLAYER_START_X  = WINDOW_WIDTH / 2;
    /** The starting Y position of the player. */
    public static final int PLAYER_START_Y  = WINDOW_HEIGHT / 2;

    // Orb Configuration
    /** The size of the orbs in the game. */
    public static final int ORB_SIZE        = 20;

    private static final int BLUE_ORB_POINTS                = 1;
    private static final int GREEN_ORB_POINTS               = 3;
    /** The base speed modifier of the orbs. */
    public static final double BASE_SPEED_MODIFIER          = 0.7;
    /** The maximum speed modifier for the orbs. */
    public static final double MAX_SPEED_MODIFIER           = 2.5;
    /** The minimum speed modifier for the orbs. */
    public static final double MIN_SPEED_MODIFIER           = 0.5;
    /** The rate at which the speed modifier changes. */
    public static final double SPEED_MODIFIER_CHANGE_RATE   = 50; // lower = more chance

    // Score System
    /** The name of the score attribute. */
    public static final String POINTS_NAME              = "Score";
    /** The starting score of the player. */
    public static final int START_SCORE                 = 10;
    /** The minimum score required for the player to survive. */
    public static final int MINIMUM_SCORE_TO_SURVIVE    = 1;

    private static final int SCORE_DECREASE_RANDOM_MIN  = 1;
    private static final int SCORE_DECREASE_RANDOM_MAX  = 1000;
    private static final int SCORE_DECREASE_PROBABILITY = 5;  // percent
    private static final int RANDOM_NUMBER_OFFSET       = 1;


    private static double speedModifier = BASE_SPEED_MODIFIER;
    private final Pane root             = new Pane();
    private Player player;
    private OrbShooter cannon;
    private Label scoreLabel;
    private int score;
    private AnimationTimer gameLoop;

    /**
     * Starts the JavaFX application and initializes the game window.
     *
     * @param primaryStage The primary stage for the application.
     */
    @Override
    public void start(final Stage primaryStage)
    {
        validatePrimaryStage(primaryStage);

        final Scene scene;

        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);
        ClockStormUI.setupSceneStyles(scene);

        primaryStage.setTitle(GAME_NAME);
        primaryStage.setScene(scene);
        primaryStage.show();

        ClockStormUI.showWelcomeMessage();

        setupGame();
        startGameLoop();
        setupKeyHandlers(scene);
    }

    /**
     * Initializes the game by setting up the player, cannon, and score label.
     */
    private void setupGame()
    {
        player  = new Player(PLAYER_START_X, PLAYER_START_Y, PLAYER_SIZE);
        cannon      = new OrbShooter();
        score       = START_SCORE;
        scoreLabel  = ClockStormUI.createScoreLabel();

        player.getStyleClass().add("player");

        root.getChildren().addAll(player, scoreLabel);
    }

    /**
     * Starts the game loop, which updates the game state continuously, calling updateOrbs(), checkCollisions() and
     * checkAndUpdateScore().
     */
    private void startGameLoop()
    {
        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                cannon.shootOrb(root, speedModifier);
                player.update();

                updateOrbs();
                checkCollisions();
                checkAndUpdateScore();
            }
        };

        gameLoop.start();
    }

    /**
     * Updates the position of all orbs in the game, using the update() method in the Orb class.
     */
    private void updateOrbs()
    {
        cannon.getOrbs().forEach(Orb::update);
    }

    /**
     * Sets up key handlers for player movement. This method listens for key press and release events
     * to control the movement of the player character. It handles both arrow keys and WASD keys for
     * moving the player in all four directions (left, right, up, and down).
     * <p>
     * When a key is pressed, the corresponding movement flag for the player is set to `true`, allowing
     * the player to move in the specified direction. When the key is released, the movement flag is set to
     * `false`, stopping the player from moving further in that direction.
     * <p>
     * The supported keys for movement are:
     *     Left Arrow or 'A' key - moves the player left
     *     Right Arrow or 'D' key - moves the player right
     *     Up Arrow or 'W' key - moves the player up
     *     Down Arrow or 'S' key - moves the player down
     *
     *
     * @param scene The scene to which the key handlers are attached. This is used to register the key event
     *              handlers for movement. The scene listens for key press and key release events to update
     *              the player's movement accordingly.
     */
    private void setupKeyHandlers(final Scene scene)
    {
        validateScene(scene);

        // Register the key press event to move the player
        scene.setOnKeyPressed(event ->
        {
            switch (event.getCode())
            {
                case LEFT, A: // When the LEFT arrow or A key is pressed
                    player.setLeft(true); // Start moving the player left
                    break;
                case RIGHT, D: // When the RIGHT arrow or D key is pressed
                    player.setRight(true); // Start moving the player right
                    break;
                case UP, W: // When the UP arrow or W key is pressed
                    player.setUp(true); // Start moving the player up
                    break;
                case DOWN, S: // When the DOWN arrow or S key is pressed
                    player.setDown(true); // Start moving the player down
                    break;
            }
        });

        // Register the key release event to stop the player's movement
        scene.setOnKeyReleased(event ->
        {
            switch (event.getCode())
            {
                case LEFT, A: // When the LEFT arrow or A key is released
                    player.setLeft(false); // Stop moving the player left
                    break;
                case RIGHT, D: // When the RIGHT arrow or D key is released
                    player.setRight(false); // Stop moving the player right
                    break;
                case UP, W: // When the UP arrow or W key is released
                    player.setUp(false); // Stop moving the player up
                    break;
                case DOWN, S: // When the DOWN arrow or S key is released
                    player.setDown(false); // Stop moving the player down
                    break;
            }
        });
    }


    /**
     * Checks for collisions between the player and orbs in the game. This method iterates through all the orbs
     * in the cannon's collection and checks whether any of them intersect with the player's boundaries.
     * <p>
     * If a collision is detected, the following actions occur:
     * - If the orb is a RedOrb, the game ends with a "Game Over" message.
     * - If the orb is a GreenOrb, the score is incremented by a predefined amount (GREEN_ORB_POINTS).
     * - If the orb is a BlueOrb, the score is incremented by another predefined amount (BLUE_ORB_POINTS).
     * - Regardless of the orb type, the orb is removed from the game, and the score is updated accordingly.
     * <p>
     * After handling a collision, the game checks if the player's speed modifier needs updating and reflects
     * that in the game state.
     * <p>
     * The method also updates the display of the player's score after each interaction.
     */
    private void checkCollisions()
    {
        final Iterator<Orb> iterator;
        iterator = cannon.getOrbs().iterator();

        // Iterate through all orbs to check for collisions with the player
        while (iterator.hasNext())
        {
            final Orb orb;
            orb = iterator.next();

            // If the player collides with the orb
            if (player.getBoundsInParent().intersects(orb.getBoundsInParent()))
            {
                // Handle the collision based on the type of orb
                switch (orb)
                {
                    case RedOrb _ -> gameOver("Game over! You hit a red orb!");
                    case GreenOrb _ -> score += GREEN_ORB_POINTS;
                    case BlueOrb _ -> score += BLUE_ORB_POINTS;
                    default -> throw new IllegalStateException("Invalid orb type");
                }

                // Remove the orb from the game
                iterator.remove();
                root.getChildren().remove(orb);

                // Update the score and speed modifier
                updateScoreLabel();
                updateSpeedModifier();
            }
        }
    }


    /**
     * Handles the game-over logic by stopping the game loop, displaying a game-over alert with the final score,
     * high score, and average score, and allowing the player to either restart the game or exit.
     * <p>
     * This method performs the following actions:
     * - Stops the game loop to halt the game progress.
     * - Displays a game-over alert with the provided message, along with the current score, high score,
     *   and average score, using the `ClockStormUI.showGameOverAlert()` method.
     * - If the player chooses to restart the game by clicking "Play Again", the game scene is reset,
     *   and the game is set up again, with the game loop restarted.
     * - If the player opts to exit the game, the game window is closed.
     * - The method also attempts to add the score to the high scores and retrieve the high score and average score
     *   from the `ClockStormScore` class, which manages the game's scoring system.
     *
     * @param message The message to display in the game-over alert.
     */
    private void gameOver(final String message)
    {
        validateGameOverMessage(message);

        gameLoop.stop();

        Platform.runLater(() ->
        {
            final int highScore;
            final double averageScore;
            final ButtonType result;

            try
            {
                ClockStormScore.addScore(score);
                highScore = ClockStormScore.getHighScore();
                averageScore = ClockStormScore.getAverageScore();

                result = ClockStormUI.showGameOverAlert(message, score, highScore, averageScore);

                if (result.getText().equals("Play Again"))
                {
                    root.getChildren().clear();
                    setupGame();
                    gameLoop.start();
                }
                else
                {
                    ((Stage)scoreLabel
                            .getScene()
                            .getWindow())
                            .close();
                }
            }
            catch (final IOException e)
            {
                throw new RuntimeException(e);
            }
        });
    }


    /**
     * Checks the player's score to determine if the game should continue or end.
     * - If the score is below the minimum threshold required to survive,
     *   the game ends with a message indicating that the player has run out of points.
     * - If the score is above or equal to the minimum threshold,
     *   the method updates the score.
     * <p>
     * This method is used to manage the player's score during gameplay, ensuring
     * that if the score reaches a critical point (below the minimum), the game
     * will be over. Otherwise, it allows the score to be updated for further gameplay.
     */
    private void checkAndUpdateScore()
    {
        if (score < MINIMUM_SCORE_TO_SURVIVE)
        {
            gameOver("You have no " +
                    POINTS_NAME.toLowerCase() +
                    " left!");
        }
        else
        {
            updateScore();
        }
    }


    /**
     * Calculates a random number and decreases the player's score if that number falls within a specified range.
     * <p>
     * This method generates a random number between a defined minimum and maximum range, and if the number is
     * below a certain probability threshold, the score is decreased by 1. After decreasing the score,
     * it updates the score label and adjusts the game's speed modifier accordingly.
     * <p>
     * The method is typically used to penalize the player based on random chance, adding an element of unpredictability
     * to the game. The score will only decrease under specific conditions determined by the random number generation.
     */
    private void updateScore()
    {
        if (getRandomNumber(SCORE_DECREASE_RANDOM_MIN, SCORE_DECREASE_RANDOM_MAX) < SCORE_DECREASE_PROBABILITY)
        {
            score--;
            updateScoreLabel();
            updateSpeedModifier();
        }
    }


    /**
     * Updates the score label to reflect the current score of the player.
     * <p>
     * This method updates the text of the `scoreLabel` UI element to display the player's current score.
     * The score is shown in the format "Score: X", where X is the current value of the `score` variable.
     * The score label is typically used to provide real-time feedback to the player regarding their progress in the game.
     */
    private void updateScoreLabel()
    {
        scoreLabel.setText("Score: " + score);
    }


    /**
     * Adjusts the speed modifier for orbs based on the current score value.
     * <p>
     * The speed modifier is calculated by using the formula:
     * <pre>
     *     Speed Modifier = BASE_SPEED_MODIFIER + (score - START_SCORE) / SPEED_MODIFIER_CHANGE_RATE
     * </pre>
     * The speed modifier determines how fast the orbs move, with higher scores leading to faster-moving orbs.
     * The method ensures that the calculated speed modifier stays within predefined limits:
     * - The speed modifier is capped at a maximum value, defined by {@link #MAX_SPEED_MODIFIER}.
     * - If the calculated speed modifier falls below a minimum threshold, defined by {@link #MIN_SPEED_MODIFIER}, it will be adjusted to that minimum.
     * <p>
     * This method is called after updating the score to ensure that the game difficulty increases as the player's score rises.
     */
    private void updateSpeedModifier()
    {
        speedModifier = BASE_SPEED_MODIFIER + (score - START_SCORE) / SPEED_MODIFIER_CHANGE_RATE;

        if (speedModifier > MAX_SPEED_MODIFIER)
        {
            speedModifier = MAX_SPEED_MODIFIER;
        }
        else if (speedModifier < MIN_SPEED_MODIFIER)
        {
            speedModifier = MIN_SPEED_MODIFIER;
        }
    }

    /**
     * Generates a random integer between two specified values (inclusive).
     * <p>
     * The method uses the {@link Random} class to generate a random number in the range from {@code min} to {@code max},
     * including both boundary values. The formula used is:
     * <pre>
     *     generatedNumber = random.nextInt((max - min) + RANDOM_NUMBER_OFFSET) + min;
     * </pre>
     * This ensures that the random number is within the specified range.
     *
     * @param min The minimum value (inclusive) that can be generated.
     * @param max The maximum value (inclusive) that can be generated.
     * @return The randomly generated number between {@code min} and {@code max} (inclusive).
     */
    private static int getRandomNumber(final int min, final int max)
    {
        validateMinMaxValues(min, max);

        final Random random;
        final int generatedNumber;

        random          = new Random();
        generatedNumber = random.nextInt((max - min) + RANDOM_NUMBER_OFFSET) + min;

        return generatedNumber;
    }


    /**
     * Returns the score.
     *
     * @return current score as an int
     */
    public int getScore()
    {
        return score;
    }
    
    // Validation section

    /**
     * Validates the primary stage parameter used in setupKeyHandlers.
     * Throws an IllegalArgumentException if the Primary Stage is null.
     *
     * @param primaryStage The Scene to validate.
     */
    public static void validatePrimaryStage(final Stage primaryStage)
    {
        if (primaryStage == null)
        {
            throw new IllegalArgumentException("Primary Stage cannot be null.");
        }
    }

    /**
     * Validates the Scene parameter used in setupKeyHandlers.
     * Throws an IllegalArgumentException if the scene is null.
     *
     * @param scene The Scene to validate.
     */
    private static void validateScene(final Scene scene)
    {
        if (scene == null)
        {
            throw new IllegalArgumentException("Scene cannot be null.");
        }
    }

    /**
     * Validates the String message parameter used in gameOver.
     * Throws an IllegalArgumentException if the String message is null or blank.
     *
     * @param message The String message to validate.
     */
    private static void validateGameOverMessage(final String message)
    {
        if (message == null)
        {
            throw new IllegalArgumentException("Game over message cannot be null.");
        }
        if (message.isBlank())
        {
            throw new IllegalArgumentException("Game over message cannot be blank.");
        }
    }

    /**
     * Validates min and max int values.
     * Ensures that min is lesser than max.
     *
     * @param min min int value
     * @param max max int value
     */
    private static void validateMinMaxValues(final int min, final int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }
    }
}
