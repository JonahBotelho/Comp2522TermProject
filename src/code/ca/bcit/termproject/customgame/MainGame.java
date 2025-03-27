package ca.bcit.termproject.customgame;

import ca.bcit.termproject.customgame.orbs.BlueOrb;
import ca.bcit.termproject.customgame.orbs.GreenOrb;
import ca.bcit.termproject.customgame.orbs.Orb;
import ca.bcit.termproject.customgame.orbs.RedOrb;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.Random;

import java.io.IOException;
import java.util.Iterator;

/**
 * The MainGame class is the entry point for the Bullet Hell game.
 * It initializes the game window, handles game logic, and manages user input.
 */
public class MainGame extends Application
{
    private static final int NOTHING = 0;
    private static final String POINTS_NAME = "Score"; //TODO think of better name
    private static final String GAME_NAME = "I'll think of something"; //TODO think of name
    private static final int START_SCORE = 100;
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int PLAYER_SIZE = 30;
    private static final int ORB_SIZE = 20;
    private static final int CANNON_X = WINDOW_WIDTH / 2;
    private static final int CANNON_Y = 50;
    private static final int PLAYER_START_X = WINDOW_WIDTH / 2;
    private static final int PLAYER_START_Y = WINDOW_HEIGHT - 50;
    private static final int BLUE_ORB_POINTS = 1;
    private static final int GREEN_ORB_POINTS = 3;
    private static final int SCORE_LABEL_FONT_SIZE = 20;
    private static final int SCORE_LABEL_X = 10;
    private static final int SCORE_LABEL_Y = 10;
    private static final String SCORE_LABEL_FONT_NAME = "Arial";
    private static final String SCORE_LABEL_INITIAL_TEXT = POINTS_NAME + ": " + START_SCORE;
    private static final int SCORE_DECREASE_RANDOM_MIN = 1;
    private static final int SCORE_DECREASE_RANDOM_MAX = 1000;
    private static final int SCORE_DECREASE_PROBABILITY = 5; // percent
    private static final int RANDOM_NUMBER_OFFSET = 1;
    private static final int MINIMUM_SCORE_TO_SURVIVE = 1;

    private final Pane root = new Pane();
    private Player player;
    private Cannon cannon;
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
        final Scene scene;
        scene = new Scene(root, WINDOW_WIDTH, WINDOW_HEIGHT);

        primaryStage.setTitle("Bullet Hell Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        showWelcomeMessage();

        setupGame();
        startGameLoop();
        setupKeyHandlers(scene);
    }

    /**
     * Initializes the game by setting up the player, cannon, and score label.
     */
    private void setupGame()
    {
        player = new Player(PLAYER_START_X, PLAYER_START_Y, PLAYER_SIZE);
        cannon = new Cannon(CANNON_X, CANNON_Y);
        score = START_SCORE;

        scoreLabel = new Label(SCORE_LABEL_INITIAL_TEXT);
        scoreLabel.setFont(new Font(SCORE_LABEL_FONT_NAME, SCORE_LABEL_FONT_SIZE));
        scoreLabel.setLayoutX(SCORE_LABEL_X); // Position in the top-left corner
        scoreLabel.setLayoutY(SCORE_LABEL_Y);

        root.getChildren().addAll(player, scoreLabel);
    }

    /**
     * Starts the game loop, which updates the game state continuously.
     */
    private void startGameLoop()
    {
        gameLoop = new AnimationTimer()
        {
            @Override
            public void handle(final long now)
            {
                cannon.shootOrb(root);
                player.update();
                updateOrbs();
                checkCollisions();
                checkScore();
            }
        };
        gameLoop.start();
    }

    /**
     * Updates the position of all orbs in the game.
     */
    private void updateOrbs()
    {
        cannon.getOrbs().forEach(Orb::update);
    }

    /**
     * Sets up key handlers for player movement.
     *
     * @param scene The scene to which the key handlers are attached.
     */
    private void setupKeyHandlers(final Scene scene)
    {
        scene.setOnKeyPressed(event ->
        {
            switch (event.getCode())
            {
                case LEFT:
                    player.setLeft(true);
                    break;
                case RIGHT:
                    player.setRight(true);
                    break;
                case UP:
                    player.setUp(true);
                    break;
                case DOWN:
                    player.setDown(true);
                    break;
            }
        });

        scene.setOnKeyReleased(event ->
        {
            switch (event.getCode())
            {
                case LEFT:
                    player.setLeft(false);
                    break;
                case RIGHT:
                    player.setRight(false);
                    break;
                case UP:
                    player.setUp(false);
                    break;
                case DOWN:
                    player.setDown(false);
                    break;
            }
        });
    }

    /**
     * Checks for collisions between the player and orbs.
     */
    private void checkCollisions()
    {
        final Iterator<Orb> iterator;
        iterator = cannon.getOrbs().iterator();
        while (iterator.hasNext())
        {
            final Orb orb;
            orb = iterator.next();
            if (player.getBoundsInParent().intersects(orb.getBoundsInParent()))
            {
                if (orb instanceof RedOrb)
                {
                    gameOver("Game over! You hit a red orb!");
                }
                else if (orb instanceof GreenOrb)
                {
                    score += GREEN_ORB_POINTS;
                }
                else if (orb instanceof BlueOrb)
                {
                    score += BLUE_ORB_POINTS;
                }
                iterator.remove();
                root.getChildren().remove(orb);
                updateScore();
            }
        }
    }

    /**
     * Handles the game-over logic, including displaying an alert and restarting or exiting the game.
     *
     * @param message The game-over message to display.
     */
    private void gameOver(final String message)
    {
        final MutableInteger highScore;
        highScore = new MutableInteger(NOTHING);
        gameLoop.stop();

        Platform.runLater(() ->
        {
            try
            {
                Score.addScore(score);
                highScore.setValue(Score.getHighScore());
            } catch (IOException e)
            {
                throw new RuntimeException(e);
            }

            final Alert gameOverAlert = new Alert(Alert.AlertType.INFORMATION);
            final ButtonType playAgain = new ButtonType("Play Again");
            final ButtonType quit = new ButtonType("Quit");

            gameOverAlert.setTitle("Game Over");
            gameOverAlert.setContentText(message +
                    "\nFinal Score: " + score +
                    "\nHigh Score: " + highScore.getValue());
            gameOverAlert.getButtonTypes().setAll(playAgain, quit);

            gameOverAlert.showAndWait().ifPresent(response ->
            {
                if (response == playAgain)
                {
                    root.getChildren().clear();
                    setupGame();
                    gameLoop.start();
                }
                else
                {
                    System.exit(NOTHING);
                }
            });
        });
    }

    private void checkScore()
    {
        if (score < MINIMUM_SCORE_TO_SURVIVE)
        {
            gameOver("You have no " +
                    POINTS_NAME.toLowerCase() +
                    " left!");
        }
        else
        {
            if (getRandomNumber(SCORE_DECREASE_RANDOM_MIN, SCORE_DECREASE_RANDOM_MAX) < SCORE_DECREASE_PROBABILITY)
            {
                score--;
                updateScore();
            }
        }
    }

    /**
     * Updates the score label with the player's
     */
    private void updateScore()
    {
        scoreLabel.setText("Score: " + score);
    }

    /**
     * Generates a number between two given numbers (inclusive)
     *
     * @param min minimum number to be generated
     * @param max maximum number to be generated
     * @return generated number
     */
    private static int getRandomNumber(final int min, final int max)
    {
        final Random random;
        final int generatedNumber;

        random = new Random();
        generatedNumber = random.nextInt((max - min) + RANDOM_NUMBER_OFFSET) + min;

        return generatedNumber;
    }

    /**
     * Display a welcome message for the player when they enter the game.
     */
    private void showWelcomeMessage()
    {
        final Alert welcomeMessage;
        final ButtonType playButton;
        final StringBuilder contentTextBuilder;
        final String contextTextString;

        welcomeMessage = new Alert(Alert.AlertType.INFORMATION);
        playButton = new ButtonType("Play");
        contentTextBuilder = new StringBuilder();

        contentTextBuilder.append("Welcome to ")
                .append(GAME_NAME)
                .append("!")
                .append("\n\t1. Use arrow keys for movement")
                .append("\n\t2. Catching blue orbs gains you 1 point")
                .append("\n\t3. Catching green orbs gains you 3 points")
                .append("\n\t4. Catching red orbs will make you lose")
                .append("\n\t5. Your score will naturally decrease over time")
                .append("\n\t6. If it reaches 0, you will lose")
                .append("\nGood luck!");
        contextTextString = contentTextBuilder.toString();

        welcomeMessage.getButtonTypes().setAll(playButton);
        welcomeMessage.setTitle("Welcome to " + GAME_NAME);
        welcomeMessage.setHeaderText("How to play");
        welcomeMessage.setContentText(contextTextString);

        welcomeMessage.showAndWait();
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

    /**
     * Returns the width of the game window.
     *
     * @return the window width in pixels
     */
    public static int getWindowWidth()
    {
        return WINDOW_WIDTH;
    }

    /**
     * Returns the height of the game window.
     *
     * @return the window height in pixels
     */
    public static int getWindowHeight()
    {
        return WINDOW_HEIGHT;
    }

    /**
     * Returns the size (both width and height) of the player character.
     *
     * @return the player size in pixels
     */
    public static int getPlayerSize()
    {
        return PLAYER_SIZE;
    }

    /**
     * Returns the size (both width and height) of the orbs.
     *
     * @return the orb size in pixels
     */
    public static int getOrbSize()
    {
        return ORB_SIZE;
    }

    /**
     * Returns the x-coordinate of the cannon's position.
     *
     * @return the cannon's x-position in pixels
     */
    public static int getCannonX()
    {
        return CANNON_X;
    }

    /**
     * Returns the y-coordinate of the cannon's position.
     *
     * @return the cannon's y-position in pixels
     */
    public static int getCannonY()
    {
        return CANNON_Y;
    }

    /**
     * The main entry point for the application.
     *
     * @param args Command-line arguments (unused).
     */
    public static void main(final String[] args)
    {
        launch(args);
    }

}