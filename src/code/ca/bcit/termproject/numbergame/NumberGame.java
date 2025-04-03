package ca.bcit.termproject.numbergame;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Objects;
import java.util.Random;
import java.text.DecimalFormat;

/**
 * A number placement game where the player must place randomly generated numbers
 * in a grid in ascending order. The game ends when the player cannot place a number
 * without breaking the ascending order. The player can retry or quit after losing.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class NumberGame
        extends Application
        implements RandomNumberGenerator
{
    // Game Configuration Constants
    private static final int NOTHING              = 0;
    private static final int NUMBER_OF_SQUARES    = 20;
    private static final int NUMBER_OF_COLUMNS    = 5;
    private static final int WINDOW_WIDTH         = 400;
    private static final int WINDOW_HEIGHT        = 400;

    // Random Number Generation Constants
    private static final int MIN_RANDOM_NUM       = 1;
    private static final int MAX_RANDOM_NUM       = 1000;
    private static final int RANDOM_NUMBER_OFFSET = 1;

    // UI Layout Constants
    private static final int BUTTON_SIZE          = 60;
    private static final int GRID_WIDTH_GAP       = 10;
    private static final int GRID_HEIGHT_GAP      = 10;
    private static final int ROOT_PADDING         = 20;
    private static final int VBOX_SPACING         = 10;

    // Game State Fields
    private final int[] grid                     = new int[NUMBER_OF_SQUARES];
    private final Button[] buttons               = new Button[NUMBER_OF_SQUARES];
    private int currentNumber;
    private int gamesPlayed                      = NOTHING;
    private int successfulPlacements             = NOTHING;
    private Label statusLabel;

    // Styling
    private static final String STYLESHEET_PATH = "/res/numbergame/css/numberGameStyles.css";

    /**
     * Runs the program.
     */
    public static void launchGame()
    {
        Platform.runLater(() ->{
            try
            {
                new NumberGame().start(new Stage());
            }
            catch (final Exception e) {
                e.printStackTrace();
            }
        });
    }
    
    @Override
    public void start(final Stage primaryStage)
    {
        final Scene scene;
        final GridPane gridPane;
        final VBox root;

        root = new VBox(VBOX_SPACING);
        root.setPadding(new Insets(ROOT_PADDING));
        root.setAlignment(Pos.CENTER);

        gridPane = new GridPane();
        gridPane.setHgap(GRID_HEIGHT_GAP);
        gridPane.setVgap(GRID_WIDTH_GAP);
        gridPane.setAlignment(Pos.CENTER);

        initializeStatusLabel(root);
        initializeGrid(root, gridPane);
        generateNextNumber();

        scene = new Scene(root, WINDOW_HEIGHT, WINDOW_WIDTH);
        scene.getStylesheets().add(getClass().getResource(STYLESHEET_PATH).toExternalForm());

        primaryStage.setTitle("Number Game");
        primaryStage.setScene(scene);
        primaryStage.show();
        showWelcomeAlert();
    }

    /**
     * Generates a random number within the specified range (inclusive).
     *
     * @param min The minimum value of the range.
     * @param max The maximum value of the range.
     * @return A random number between min and max (inclusive).
     */
    @Override
    public int randomNumber(final int min, final int max)
    {
        validateMinMax(min, max);
        
        final Random random;
        final int num;

        random = new Random();
        num = random.nextInt((max - min) + RANDOM_NUMBER_OFFSET) + min;

        return num;
    }

    private void initializeGrid(final VBox root,
                                final GridPane gridPane)
    {
        validateRoot(root);
        validateGridPane(gridPane);
        
        for (int i = NOTHING; i < NUMBER_OF_SQUARES; i++)
        {
            // declaring a final variable to be the value of i, so I can use it in the lambda expression
            final int finalI;
            finalI = i;

            buttons[i] = new Button();
            buttons[i].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            buttons[i].setOnAction(e -> handleButtonClick(finalI));

            gridPane.add(buttons[i], i % NUMBER_OF_COLUMNS, i / NUMBER_OF_COLUMNS);
        }

        root.getChildren().add(gridPane);
    }
    
    private static final void validateGridPane(GridPane gridPane)
    {
    
    }
    
    private void initializeStatusLabel(final VBox root)
    {
        validateRoot(root);
        
        statusLabel = new Label("Next Number: ");
        root.getChildren().add(statusLabel);
    }

    private void resetGrid()
    {
        for (int i = NOTHING; i < NUMBER_OF_SQUARES; i++)
        {
            grid[i] = NOTHING;
            if (buttons[i] != null)
            {
                buttons[i].setText(""); // Clear button text
            }
        }
    }

    private void generateNextNumber()
    {
        currentNumber = randomNumber(MIN_RANDOM_NUM, MAX_RANDOM_NUM);
        statusLabel.setText("Next Number: " + currentNumber);
    }

    private void handleButtonClick(final int index)
    {
        validateIndex(index);
        
        if (grid[index] != NOTHING)
        {
            // Slot is already occupied
            showInvalidSpotAlert();
            return;
        }

        // Place the number in the grid
        grid[index] = currentNumber;
        buttons[index].setText(String.valueOf(currentNumber));
        successfulPlacements++;

        // If the grid is no longer in ascending order, the user has lost
        if (!isAscendingOrder())
        {
            gamesPlayed++;
            showGameOverAlert();
            return;
        }

        generateNextNumber();
    }

    private boolean isAscendingOrder()
    {
        int prev;
        prev = NOTHING;

        for (int num : grid)
        {
            if (num != NOTHING)
            {
                if (num < prev)
                {
                    return false;
                }
                else
                {
                    prev = num;
                }
            }
        }
        return true;
    }

    private void showGameOverAlert()
    {
        final InformationAlert alert;
        final ButtonType retryButton;
        final ButtonType quitButton;

        alert       = new InformationAlert("Game Over");
        retryButton = new ButtonType("Try Again");
        quitButton  = new ButtonType("Quit");

        setUpAlert(alert);

        alert.setHeaderText("You lost!");
        alert.setContentText("Impossible to place the next number. Try again?");
        alert.getButtonTypes().setAll(retryButton, quitButton);

        alert.showAndWait().ifPresent(response ->
        {
            if (response == retryButton)
            {
                resetGrid();
                generateNextNumber();
            }
            else
            {
                showFinalScore();
                final Stage stage;

                stage = (Stage) statusLabel.getScene().getWindow();
                stage.close();
            }
        });
    }

    private void showFinalScore()
    {
        final DecimalFormat averagePlacementsFormat;
        final StringBuilder contextText;
        final String contextTextString;
        final InformationAlert alert;

        averagePlacementsFormat = new DecimalFormat("0.00");
        contextText = new StringBuilder();

        contextText.append("Games Played: ")
                .append(gamesPlayed)
                .append("\n")
                .append("Successful Placements: ").append(successfulPlacements)
                .append("\n")
                .append("Average Placements per Game: ")
                .append(averagePlacementsFormat.format(successfulPlacements / (double) gamesPlayed));
        contextTextString = contextText.toString();

        alert = new InformationAlert("Final Score");

        setUpAlert(alert);

        alert.setHeaderText("Game Over");
        alert.setContentText(contextTextString);
        alert.showAndWait();
    }

    private void showInvalidSpotAlert()
    {
        final WarningAlert alert;

        alert = new WarningAlert("Invalid Move");
        setUpAlert(alert);

        alert.setHeaderText(null);
        alert.setContentText("This slot is already occupied. Try another slot.");
        alert.showAndWait();
    }
    
    /**
     * Displays the welcome alert, with instructions on how to proceed.
     */
    private void showWelcomeAlert()
    {
        final InformationAlert alert;
        final StringBuilder messageBuilder;
        final String messageString;

        alert           = new InformationAlert("Welcome");
        messageBuilder  = new StringBuilder();

        setUpAlert(alert);

        messageBuilder.append("Welcome to the ")
                .append(NUMBER_OF_SQUARES)
                .append(" square number game!")
                .append("\nClick OK to start!");
        messageString = messageBuilder.toString();
        alert.setHeaderText(null);
        alert.setContentText(messageString);
        alert.showAndWait();
    }

    private void setUpAlert(final Alert alert)
    {
        validateAlert(alert);
        
        final DialogPane dialogPane;
        final Scene scene;
        final Window window;
        final Stage stage;

        dialogPane  = alert.getDialogPane();
        scene       = dialogPane.getScene();
        window      = scene.getWindow();
        stage       = (Stage) window;
        
        scene.setFill(Color.TRANSPARENT);
        stage.initStyle(StageStyle.TRANSPARENT);
        
        dialogPane.getStylesheets()
                .add(Objects
                        .requireNonNull(getClass().getResource(STYLESHEET_PATH))
                        .toExternalForm());
        dialogPane.getStyleClass().add("dialog-pane");
    }
    
    private static final void validateAlert(final Alert alert)
    {
        if (alert == null)
        {
            throw new IllegalArgumentException("Alert cannot be null");
        }
    }
    
    private static void validateIndex(final int index)
    {
        if (index < NOTHING || index > NUMBER_OF_SQUARES)
        {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
    }
    
    private static void validateMinMax(final int min, final int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("Min cannot be greater than Max")
        }
    }
    
    private static void validateRoot(final VBox root)
    {
        if (root == null)
        {
            throw new IllegalArgumentException("Root cannot be null");
        }
    }
}
