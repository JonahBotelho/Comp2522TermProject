package ca.bcit.termproject.customgame;

import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Objects;
import java.text.DecimalFormat;

/**
 * The {@code ClockStormUI} class is responsible for managing the user interface components of the ClockStorm game.
 * This class handles the creation of UI elements such as labels, alerts, and styling of scenes and dialogs.
 * It is designed to facilitate the display of key game information, including the player's score,
 * game instructions, and the final game over statistics. Additionally, the class sets up the overall styling
 * of the UI by loading a custom stylesheet for consistent design throughout the game.
 * <p>
 * This class provides methods to:
 * - Set up scene styles using an external CSS file
 * - Create and configure the score label displayed during gameplay
 * - Show a welcome message with game instructions
 * - Display a game over alert with the player's final score, high score, and average score
 * - Perform validation on inputs such as score, message, and averages to ensure correctness
 * <p>
 * Note: This class is meant to be used as a utility for managing the UI elements of the game. It relies
 * on the ClockStormMain class for certain constants (e.g., game name and starting score) and assumes that
 * the necessary resources (e.g., CSS file) are available on the classpath.
 * <p>
 * Methods in this class interact with JavaFX UI elements and display important information to the player
 * during the game. It also facilitates the player's experience by offering options such as retrying or quitting
 * the game upon its completion.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class ClockStormUI
{
    private final static int NOTHING = 0;

    // UI Configuration
    private static final String SCORE_LABEL_FONT_NAME    = "Arial";
    private static final int SCORE_LABEL_FONT_SIZE       = 20;
    private static final int SCORE_LABEL_X               = 10;
    private static final int SCORE_LABEL_Y               = 10;
    private static final String SCORE_LABEL_INITIAL_TEXT = ClockStormMain.POINTS_NAME + ": " + ClockStormMain.START_SCORE;

    // Styles
    private static final String STYLESHEET_PATH = "/res/customgame/css/customGameStyles.css";

    /**
     * Sets up the scene styles by adding the stylesheet.
     * This method retrieves the stylesheet file from the classpath and applies it to the given scene.
     *
     * @param scene The scene to style. The scene's stylesheet is modified by adding the classpath stylesheet.
     */
    public static void setupSceneStyles(final Scene scene)
    {
        scene.getStylesheets().add(Objects.requireNonNull(
                ClockStormUI.class.getResource(STYLESHEET_PATH)).toExternalForm());
    }

    /**
     * Creates and configures the score label.
     * This method creates a new Label object for the score, sets the font properties, layout position,
     * and applies the "score-label" CSS class for styling.
     *
     * @return The configured score label. A Label is returned that can be added to the scene.
     */
    public static Label createScoreLabel()
    {
        final Label scoreLabel;
        scoreLabel = new Label(SCORE_LABEL_INITIAL_TEXT);

        scoreLabel.setFont(new Font(SCORE_LABEL_FONT_NAME, SCORE_LABEL_FONT_SIZE));
        scoreLabel.setLayoutX(SCORE_LABEL_X);
        scoreLabel.setLayoutY(SCORE_LABEL_Y);
        scoreLabel.getStyleClass().add("score-label");
        return scoreLabel;
    }

    /**
     * Displays the welcome message with game instructions.
     * This method shows an informational alert with details on how to play the game, including controls
     * and mechanics such as gaining and losing points.
     */
    public static void showWelcomeMessage()
    {
        final Alert welcomeMessage;
        final ButtonType playButton;
        final StringBuilder contentTextBuilder;
        final String contextTextString;

        welcomeMessage      = new Alert(Alert.AlertType.INFORMATION);
        playButton          = new ButtonType("Play");
        contentTextBuilder  = new StringBuilder();

        // Adds styling
        setUpAlert(welcomeMessage);

        contentTextBuilder.append("Welcome to ")
                .append(ClockStormMain.GAME_NAME)
                .append("!")
                .append("\n\t1. Use arrow or WASD keys for movement")
                .append("\n\t2. Catching blue orbs gains you 1 point")
                .append("\n\t3. Catching green orbs gains you 3 points")
                .append("\n\t4. Catching red orbs will make you lose")
                .append("\n\t5. Your score will naturally decrease over time")
                .append("\n\t6. If it reaches 0, you will lose")
                .append("\nGood luck!");
        contextTextString = contentTextBuilder.toString();

        welcomeMessage.getButtonTypes().setAll(playButton);
        welcomeMessage.setTitle("Welcome to " + ClockStormMain.GAME_NAME);
        welcomeMessage.setHeaderText("How to play");
        welcomeMessage.setContentText(contextTextString);

        welcomeMessage.showAndWait();
    }

    /**
     * Shows the game over alert with final score and high score.
     * This method displays a game over alert with the final score, the high score, and the average score.
     * The user is given the option to play again or quit the game.
     *
     * @param message   The game over message to display.
     * @param score     The final score to display.
     * @param highScore The highest score to display.
     * @param average   The average score to display.
     * @return The button type that was clicked by the user (either "Play Again" or "Quit").
     */
    public static ButtonType showGameOverAlert(final String message,
                                               final int score,
                                               final int highScore,
                                               final double average)
    {
        validateMessage(message);
        validateScore(score);
        validateHighScore(highScore);
        validateAverage(average);

        final DecimalFormat averageScoreFormat;
        final Alert gameOverAlert;
        final ButtonType playAgain;
        final ButtonType quit;

        averageScoreFormat  = new DecimalFormat("0.00");
        gameOverAlert       = new Alert(Alert.AlertType.INFORMATION);
        playAgain           = new ButtonType("Play Again");
        quit                = new ButtonType("Quit");

        // Adds CSS stylesheet, and removes top row from Alert
        setUpAlert(gameOverAlert);

        gameOverAlert.setHeaderText("Game Over");
        gameOverAlert.setContentText(message +
                "\nFinal Score: " + score +
                "\nHigh Score: " + highScore +
                "\nAverage Score: " + averageScoreFormat.format(average));
        gameOverAlert.getButtonTypes().setAll(playAgain, quit);

        return gameOverAlert.showAndWait().orElse(quit);
    }

    /**
     * Adds customGameStyles.css to an Alert, and removes the top row.
     * This method customizes the alert dialog by adding a stylesheet and removing the top row of the alert.
     * It also adjusts the window to a transparent style.
     *
     * @param alert alert to set up. The alert is styled by adding a CSS file and modifying its window properties.
     */
    static void setUpAlert(final Alert alert)
    {
        validateAlert(alert);

        final DialogPane dialogPane;
        final Scene scene;
        final Window window;
        final Stage stage;

        dialogPane = alert.getDialogPane();
        scene = dialogPane.getScene();
        window = scene.getWindow();
        stage = (Stage) window;

        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);

        dialogPane.getStylesheets()
                .add(Objects
                        .requireNonNull(ClockStormUI.class.getResource(STYLESHEET_PATH))
                        .toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
    }

    /**
     * Validates that the given {@link Alert} is not null.
     * This method checks that the provided alert is not null and throws a NullPointerException if it is.
     *
     * @param alert the alert to validate. The alert must not be null.
     */
    private static void validateAlert(final Alert alert)
    {
        if (alert == null)
        {
            throw new NullPointerException("alert cannot be null");
        }
    }

    /**
     * Validates that the given message is not null or empty.
     * This method checks that the provided message is not null or empty and throws an exception if it is.
     *
     * @param message the message to validate. The message must not be null or empty.
     */
    private static void validateMessage(final String message)
    {
        if (message == null || message.isBlank())
        {
            throw new NullPointerException("message cannot be null or empty");
        }
    }

    /**
     * Validates that the given score is not negative.
     * This method checks that the provided score is not negative and throws an IllegalArgumentException if it is.
     *
     * @param score the score to validate. The score must be greater than or equal to 0.
     */
    public static void validateScore(final int score)
    {
        if (score < NOTHING)
        {
            throw new IllegalArgumentException("score cannot be negative");
        }
    }

    /**
     * Validates that the given high score is not negative.
     * This method checks that the provided high score is not negative and throws an IllegalArgumentException if it is.
     *
     * @param highScore the high score to validate. The high score must be greater than or equal to 0.
     */
    public static void validateHighScore(final int highScore)
    {
        if (highScore < NOTHING)
        {
            throw new IllegalArgumentException("highScore cannot be negative");
        }
    }

    /**
     * Validates that the given average is not negative.
     * This method checks that the provided average score is not negative and throws an IllegalArgumentException if it is.
     *
     * @param average the average score to validate. The average score must be greater than or equal to 0.
     */
    public static void validateAverage(final double average)
    {
        if (average < NOTHING)
        {
            throw new IllegalArgumentException("average cannot be negative");
        }
    }

}
