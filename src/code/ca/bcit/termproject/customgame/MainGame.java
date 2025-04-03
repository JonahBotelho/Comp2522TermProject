package ca.bcit.termproject.customgame;

import ca.bcit.termproject.customgame.orbs.*;
import ca.bcit.termproject.numbergame.NumberGame;
import javafx.animation.AnimationTimer;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Objects;
import java.util.Random;

import java.io.IOException;
import java.util.Iterator;

/**
 * The MainGame class is the entry point for the Bullet Hell game.
 * It initializes the game window, handles game logic, and manages user input.
 */
public final class MainGame
        extends Application
{
    // Game Configuration
    public static final String GAME_NAME = "I'll think of something";  // TODO: think of name
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    private static final int NOTHING = 0;
    
    // Player Configuration
    public static final int PLAYER_SIZE = 30;
    public static final int PLAYER_START_X = WINDOW_WIDTH / 2;
    public static final int PLAYER_START_Y = WINDOW_HEIGHT - 50;
    
    // Orb Configuration
    public static final int ORB_SIZE = 20;
    private static final int BLUE_ORB_POINTS = 1;
    private static final int GREEN_ORB_POINTS = 3;
    public static final double BASE_SPEED_MODIFIER = 1.2;
    public static final double MAX_SPEED_MODIFIER = 2.5;
    public static final double MIN_SPEED_MODIFIER = 1;
    public static final double SPEED_MODIFIER_CHANGE_RATE = 50; // lower = more chance
    private static double speedModifier = BASE_SPEED_MODIFIER;
    
    // Score System
    public static final String POINTS_NAME = "Score";  // TODO: think of better name
    public static final int START_SCORE = 10;
    public static final int MINIMUM_SCORE_TO_SURVIVE = 1;
    private static final int SCORE_DECREASE_RANDOM_MIN = 1;
    private static final int SCORE_DECREASE_RANDOM_MAX = 1000;
    private static final int SCORE_DECREASE_PROBABILITY = 10;  // percent
    private static final int RANDOM_NUMBER_OFFSET = 1;
    
    // UI Configuration
    private static final String SCORE_LABEL_FONT_NAME = "Arial";
    private static final int SCORE_LABEL_FONT_SIZE = 20;
    static final int SCORE_LABEL_X = 10;
    private static final int SCORE_LABEL_Y = 10;
    private static final String SCORE_LABEL_INITIAL_TEXT = POINTS_NAME + ": " + START_SCORE;
    
    // Styles
    private static final String STYLESHEET_PATH = "/res/customgame/css/customGameStyles.css";
    
    private final Pane root = new Pane();
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
        scene.getStylesheets().add(getClass().getResource(STYLESHEET_PATH).toExternalForm());
        
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
        cannon = new OrbShooter();
        score = START_SCORE;
        
        scoreLabel = new Label(SCORE_LABEL_INITIAL_TEXT);
        scoreLabel.setFont(new Font(SCORE_LABEL_FONT_NAME, SCORE_LABEL_FONT_SIZE));
        scoreLabel.setLayoutX(SCORE_LABEL_X);
        scoreLabel.setLayoutY(SCORE_LABEL_Y);
        scoreLabel.getStyleClass().add("score-label");
        
        player.getStyleClass().add("player");
        
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
        validateScene(scene);
        
        scene.setOnKeyPressed(event ->
                              {
                                  switch (event.getCode())
                                  {
                                      case LEFT, A:
                                          player.setLeft(true);
                                          break;
                                      case RIGHT, D:
                                          player.setRight(true);
                                          break;
                                      case UP, W:
                                          player.setUp(true);
                                          break;
                                      case DOWN, S:
                                          player.setDown(true);
                                          break;
                                  }
                              });
        
        scene.setOnKeyReleased(event ->
                               {
                                   switch (event.getCode())
                                   {
                                       case LEFT, A:
                                           player.setLeft(false);
                                           break;
                                       case RIGHT, D:
                                           player.setRight(false);
                                           break;
                                       case UP, W:
                                           player.setUp(false);
                                           break;
                                       case DOWN, S:
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
                updateScoreLabel();
                updateSpeedModifier();
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
        validateGameOverMessage(message);
        
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
    
    /**
     * Checks the users score, and ends the game if it is below 0.
     * If it is above MINIMUM_SCORE_TO_SURVIVE, calls the updateScore() method.
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
     * Calculates a random number, and decreases the score if that number is in a given range.
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
     * Updates the score label with the player's
     */
    private void updateScoreLabel()
    {
        scoreLabel.setText("Score: " + score);
    }
    
    /**
     * Adjusts the speed modifier of the orbs, based on the current score value.
     * Speed Modifier =
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
     * Generates a number between two given numbers (inclusive)
     *
     * @param min minimum number to be generated
     * @param max maximum number to be generated
     * @return generated number
     */
    private static int getRandomNumber(final int min,
                                       final int max)
    {
        validateMinMaxValues(min, max);
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
        
        // Adds styling
        setUpAlert(welcomeMessage);
        
        contentTextBuilder.append("Welcome to ")
                .append(GAME_NAME)
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
     * Adds customGameStyles.css to an Alert, and removes the top row.
     * TODO fix corners
     * FIXME duplicate in main game
     *
     * @param alert alert to set up
     */
    private void setUpAlert(final Alert alert)
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
                             .requireNonNull(getClass().getResource(STYLESHEET_PATH))
                             .toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
    }
    
    /**
     * Runs the program.
     */
    public static void launchGame()
    {
        Platform.runLater(() ->
                          {
                              try
                              {
                                  new NumberGame().start(new Stage());
                              } catch (final Exception e)
                              {
                                  e.printStackTrace();
                              }
                          });
    }
    
    
    // Validation section
    
    /**
     * Validates the primary stage parameter used in setupKeyHandlers.
     * Checks if the primary stage is null.
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
     * Checks if the Scene is null.
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
     * Checks if the message is null or blank (empty or whitespace).
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
     * Validates the Alert parameter used in setUpAlert.
     * Checks if the Alert is null.
     *
     * @param alert The Alert to validate.
     */
    static void validateAlert(final Alert alert)
    {
        if (alert == null)
        {
            throw new IllegalArgumentException("Alert cannot be null.");
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
        if (min < max)
        {
            throw new IllegalArgumentException("Min cannot be greater than max");
        }
    }
}
