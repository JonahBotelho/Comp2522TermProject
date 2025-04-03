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

/**
 * The ClockStormUI class handles all user interface components for the game.
 */
public final class ClockStormUI
{
    private final static int NOTHING = 0;

    // UI Configuration
    private static final String SCORE_LABEL_FONT_NAME = "Arial";
    private static final int SCORE_LABEL_FONT_SIZE = 20;
    static final int SCORE_LABEL_X = 10;
    private static final int SCORE_LABEL_Y = 10;
    private static final String SCORE_LABEL_INITIAL_TEXT = ClockStormMain.POINTS_NAME + ": " + ClockStormMain.START_SCORE;

    // Styles
    private static final String STYLESHEET_PATH = "/res/customgame/css/customGameStyles.css";

    /**
     * Sets up the scene styles by adding the stylesheet.
     *
     * @param scene The scene to style
     */
    public static final void setupSceneStyles(final Scene scene)
    {
        scene.getStylesheets().add(Objects.requireNonNull(
                ClockStormUI.class.getResource(STYLESHEET_PATH)).toExternalForm());
    }

    /**
     * Creates and configures the score label.
     *
     * @return The configured score label
     */
    public static final Label createScoreLabel()
    {
        final Label scoreLabel = new Label(SCORE_LABEL_INITIAL_TEXT);
        scoreLabel.setFont(new Font(SCORE_LABEL_FONT_NAME, SCORE_LABEL_FONT_SIZE));
        scoreLabel.setLayoutX(SCORE_LABEL_X);
        scoreLabel.setLayoutY(SCORE_LABEL_Y);
        scoreLabel.getStyleClass().add("score-label");
        return scoreLabel;
    }

    /**
     * Displays the welcome message with game instructions.
     */
    public static final void showWelcomeMessage()
    {
        final Alert welcomeMessage;
        final ButtonType playButton;
        final StringBuilder contentTextBuilder;
        final String contextTextString;

        welcomeMessage = new Alert(Alert.AlertType.INFORMATION);
        playButton = new ButtonType("Play");
        contentTextBuilder = new StringBuilder();

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
     *
     * @param message   The game over message
     * @param score     The final score
     * @param highScore The high score
     * @return The button type that was clicked
     */
    public static final ButtonType showGameOverAlert(final String message,
                                                     final int score,
                                                     final int highScore,
                                                     final double average)
    {
        validateMessage(message);
        validateScore(score);
        validateHighScore(highScore);
        validateAverage(average);

        final Alert gameOverAlert;
        final ButtonType playAgain;
        final ButtonType quit;

        gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
        playAgain = new ButtonType("Play Again");
        quit = new ButtonType("Quit");

        // Adds CSS stylesheet, and removes top row from Alert
        setUpAlert(gameOverAlert);

        gameOverAlert.setTitle("Game Over");
        gameOverAlert.setContentText(message +
                "\nFinal Score: " + score +
                "\nHigh Score: " + highScore +
                "\nAverage Score: " + average);
        gameOverAlert.getButtonTypes().setAll(playAgain, quit);

        return gameOverAlert.showAndWait().orElse(quit);
    }

    /**
     * Adds customGameStyles.css to an Alert, and removes the top row.
     * TODO fix corners
     * FIXME duplicate in main game
     *
     * @param alert alert to set up
     */
    static final void setUpAlert(final Alert alert)
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
     *
     * @param alert the alert to validate
     */
    private static final void validateAlert(final Alert alert)
    {
        if (alert == null)
        {
            throw new NullPointerException("alert cannot be null");
        }
    }

    /**
     * Validates that the given message is not null or empty.
     *
     * @param message the message to validate
     */
    private static final void validateMessage(final String message)
    {
        if (message == null || message.isBlank())
        {
            throw new NullPointerException("message cannot be null or empty");
        }
    }

    /**
     * Validates that the given score is not negative.
     *
     * @param score the score to validate
     */
    public static final void validateScore(final int score)
    {
        if (score < NOTHING)
        {
            throw new IllegalArgumentException("score cannot be negative");
        }
    }

    /**
     * Validates that the given high score is not negative.
     *
     * @param highScore the high score to validate
     */
    public static final void validateHighScore(final int highScore)
    {
        if (highScore < NOTHING)
        {
            throw new IllegalArgumentException("highScore cannot be negative");
        }
    }

    /**
     * Validates that the given average is not negative.
     *
     * @param average the average value to validate
     */
    public static final void validateAverage(final double average)
    {
        if (average < NOTHING)
        {
            throw new IllegalArgumentException("average cannot be negative");
        }
    }
}
