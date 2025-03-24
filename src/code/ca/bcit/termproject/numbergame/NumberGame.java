package ca.bcit.termproject.numbergame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
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
public final class NumberGame extends Application implements RandomNumberGenerator
{
    private static final int NUMBER_OF_SQUARES    = 20;
    private static final int NOTHING              = 0;
    private static final int WINDOW_HEIGHT        = 400;
    private static final int WINDOW_WIDTH         = 400;
    private static final int MAX_RANDOM_NUM       = 1000;
    private static final int MIN_RANDOM_NUM       = 1;
    private static final int NUMBER_OF_COLUMNS    = 5;
    private static final int BUTTON_SIZE          = 60;
    private static final int GRID_HEIGHT_GAP      = 10;
    private static final int GRID_WIDTH_GAP       = 10;
    private static final int ROOT_PADDING         = 20;
    private static final int VBOX_SPACING         = 10;
    private static final int RANDOM_NUMBER_OFFSET = 1;
    
    private final int[] grid = new int[NUMBER_OF_SQUARES];
    private int currentNumber;
    private final Button[] buttons = new Button[NUMBER_OF_SQUARES];
    private Label statusLabel;
    private int gamesPlayed = NOTHING;
    private int successfulPlacements = NOTHING;
    
    @Override
    public void start(final Stage primaryStage)
    {
        final Scene scene;
        final VBox root;
        
        root = new VBox(VBOX_SPACING);
        root.setPadding(new Insets(ROOT_PADDING));
        root.setAlignment(Pos.CENTER);
        
        statusLabel = new Label("Next Number: ");
        root.getChildren().add(statusLabel);
        
        final GridPane gridPane = new GridPane();
        gridPane.setHgap(GRID_HEIGHT_GAP);
        gridPane.setVgap(GRID_WIDTH_GAP);
        gridPane.setAlignment(Pos.CENTER);
        
        initializeGrid(gridPane);
        
        root.getChildren().add(gridPane);
        
        generateNextNumber();
        
        scene = new Scene(root, WINDOW_HEIGHT, WINDOW_WIDTH);
        
        primaryStage.setTitle("Number Game");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    /**
     * Initializes the button grid.
     *
     * @param gridPane GridPane object to add buttons to
     */
    private void initializeGrid(final GridPane gridPane)
    {
        for (int i = 0; i < NUMBER_OF_SQUARES; i++)
        {
            buttons[i] = new Button();
            buttons[i].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);
            final int finalI = i;
            buttons[i].setOnAction(e -> handleButtonClick(finalI)); // Handle button clicks
            gridPane.add(buttons[i], i % NUMBER_OF_COLUMNS, i / NUMBER_OF_COLUMNS);
        }
    }
    
    /**
     * Resets the grid with zeros (empty slots).
     */
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
    
    /**
     * Generate the next random number between MIN_RANDOM_NUMBER and MAX_RANDOM_NUMBER and update the status label.
     */
    private void generateNextNumber()
    {
        currentNumber = randomNumber(MIN_RANDOM_NUM, MAX_RANDOM_NUM);
        statusLabel.setText("Next Number: " + currentNumber);
    }
    
    /**
     * Handle button clicks to place the current number in the grid.
     *
     * @param index The index of the button clicked.
     */
    private void handleButtonClick(final int index)
    {
        if (grid[index] != NOTHING)
        {
            // Slot is already occupied
            showAlert("Invalid Move", "This slot is already occupied. Try another slot.");
            return;
        }
        
        // Place the number in the grid
        grid[index] = currentNumber;
        buttons[index].setText(String.valueOf(currentNumber));
        successfulPlacements++;
        
        if (!isAscendingOrder())
        {
            gamesPlayed++;
            showGameOverAlert();
            return;
        }
        
        generateNextNumber();
    }
    
    /**
     * Check if the numbers in the grid are in ascending order.
     *
     * @return True if the numbers are in ascending order, false otherwise.
     */
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
    
    /**
     * Show a game over alert and offer the user the option to retry or quit.
     */
    private void showGameOverAlert()
    {
        final InformationAlert alert;
        final ButtonType retryButton;
        final ButtonType quitButton;
        
        alert = new InformationAlert("Game Over");
        retryButton = new ButtonType("Try Again");
        quitButton = new ButtonType("Quit");
        
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
    
    /**
     * Show the final score when the user quits the game.
     */
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
        alert.setHeaderText("Game Over");
        alert.setContentText(contextTextString);
        alert.showAndWait();
    }
    
    /**
     * Show an alert with a given title and message.
     *
     * @param title   The title of the alert.
     * @param message The message to display.
     */
    private void showAlert(final String title, final String message)
    {
        final WarningAlert alert;
        
        alert = new WarningAlert(title);
        
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    /**
     * Runs the program.
     *
     * @param args arguments passed into JavaFX launcher
     */
    public static void main(final String[] args)
    {
        launch(args);
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
        final Random random;
        random = new Random();
        
        return random.nextInt((max - min) + RANDOM_NUMBER_OFFSET) + min;
    }
}