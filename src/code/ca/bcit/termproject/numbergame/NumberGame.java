package ca.bcit.termproject.numbergame;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.Window;

import java.util.Objects;
import java.util.Random;
import java.text.DecimalFormat;

/**
 * The {@code NumberGame} class represents a number placement game in which the player must
 * place randomly generated numbers in a grid in ascending order. The game consists of a 5x4 grid
 * where the player places a number, and the objective is to keep the numbers in strictly increasing
 * order. The game ends when the player can no longer place a number without disrupting the order.
 * Upon losing, the player can choose to either retry the game or quit. The game tracks statistics
 * such as the number of games played, successful placements, and calculates the average placements
 * per game.
 * <p>
 * This class extends {@link javafx.application.Application} and implements the {@link RandomNumberGenerator}
 * interface to provide the game's functionality. It utilizes JavaFX components for the user interface,
 * including buttons for placing numbers and alert dialogs for game interaction. The game also generates
 * random numbers within a specified range for placement in the grid.
 * <p>
 * The game flow is as follows:
 * 1. Player starts the game by clicking "OK" in a welcome message.
 * 2. Player places numbers on the grid, trying to maintain ascending order.
 * 3. If the order is violated, the game ends and the player is prompted with options to retry or quit.
 * 4. The game tracks the total number of successful placements and games played.
 *
 * @author Jonah Botelho
 * @version 1.0
 */

public final class NumberGame
        extends Application
        implements RandomNumberGenerator
{
    // Game Configuration Constants
    private static final int NOTHING = 0;
    private static final int NUMBER_OF_SQUARES = 20;
    private static final int NUMBER_OF_COLUMNS = 5;
    private static final int WINDOW_WIDTH = 400;
    private static final int WINDOW_HEIGHT = 400;

    // Random Number Generation Constants
    private static final int MIN_RANDOM_NUM = 1;
    private static final int MAX_RANDOM_NUM = 1000;
    private static final int RANDOM_NUMBER_OFFSET = 1;

    // UI Layout Constants
    private static final int BUTTON_SIZE = 60;
    private static final int GRID_WIDTH_GAP = 10;
    private static final int GRID_HEIGHT_GAP = 10;
    private static final int ROOT_PADDING = 20;
    private static final int VBOX_SPACING = 10;

    // Game State Fields
    private final int[] grid = new int[NUMBER_OF_SQUARES];
    private final Button[] buttons = new Button[NUMBER_OF_SQUARES];
    private int currentNumber;
    private int gamesPlayed = NOTHING;
    private int successfulPlacements = NOTHING;
    private Label statusLabel;

    // Styling
    private static final String STYLESHEET_PATH = "/res/numbergame/css/numberGameStyles.css";

    /**
     * Starts the JavaFX application, initializes the UI, and sets up the game window.
     * <p>
     * This method is responsible for configuring and displaying the initial user interface
     * of the Number Game. It creates and configures a {@link Scene} and {@link VBox} layout
     * with appropriate padding and spacing. The method also initializes UI elements like labels
     * and the game grid, generates the next number for the game, and sets the scene for the primary stage.
     * Additionally, it loads the stylesheets and displays a welcome alert to the user.
     *
     * @param primaryStage The primary stage for the application. This is where the scene is displayed.
     */
    @Override
    public void start(final Stage primaryStage)
    {
        final Scene scene;
        final GridPane gridPane;
        final VBox root;

        root = new VBox(VBOX_SPACING);
        gridPane = new GridPane();

        // Set padding and alignment for the root VBox
        root.setPadding(new Insets(ROOT_PADDING));
        root.setAlignment(Pos.CENTER);

        // Configure the grid pane for the number grid
        gridPane.setHgap(GRID_HEIGHT_GAP);
        gridPane.setVgap(GRID_WIDTH_GAP);
        gridPane.setAlignment(Pos.CENTER);

        // Initialize status label and grid
        initializeStatusLabel(root);
        initializeGrid(root, gridPane);

        // Generate the next random number to be displayed
        generateNextNumber();

        // Create the scene and apply the stylesheet
        scene = new Scene(root, WINDOW_HEIGHT, WINDOW_WIDTH);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource(STYLESHEET_PATH)).toExternalForm());

        // Set up the stage with the scene and display it
        primaryStage.setTitle("Number Game");
        primaryStage.setScene(scene);
        primaryStage.show();

        // Show the welcome alert to the user
        showWelcomeAlert();
    }


    /**
     * Generates a random number within the specified range (inclusive).
     * <p>
     * This method generates a random integer between the provided minimum and maximum values,
     * including both the min and max values in the range. It uses the {@link Random} class to
     * produce the random number and ensures the validity of the range before generating the number.
     * The random number is then returned to the caller.
     *
     * @param min The minimum value of the range. This value is inclusive.
     * @param max The maximum value of the range. This value is also inclusive.
     * @return A random number between {@code min} and {@code max} (inclusive).
     */
    @Override
    public int randomNumber(final int min, final int max)
    {
        // Validate the provided range to ensure min is not greater than max
        validateMinMax(min, max);

        final Random random;
        final int num;

        random = new Random();

        // Generate a random number in the specified range
        num = random.nextInt((max - min) + RANDOM_NUMBER_OFFSET) + min;

        // Return the randomly generated number
        return num;
    }


    /**
     * Initializes the grid layout and button components.
     * <p>
     * This method sets up the grid layout by arranging buttons within a {@link GridPane} and adding them
     * to the provided root container. Each button is configured with a preferred size and an action listener
     * that responds to user clicks. The grid is populated with the buttons, placed in a specific number of
     * columns and rows, and then added to the root container for display. The method ensures that all buttons
     * are properly initialized and linked to their respective actions.
     *
     * @param root     The root container for the UI layout. It holds the grid pane and all UI elements.
     * @param gridPane The grid pane used to arrange the buttons in rows and columns.
     */
    private void initializeGrid(final VBox root,
                                final GridPane gridPane)
    {
        // Validate the root and grid pane to ensure they are not null or empty
        validateRoot(root);
        validateGridPane(gridPane);

        // Initialize buttons and add them to the grid
        for (int i = NOTHING; i < NUMBER_OF_SQUARES; i++)
        {
            // Declare a final variable to capture the value of i for use in lambda expressions
            final int finalI;
            finalI = i;

            // Create a new button for each grid cell
            buttons[i] = new Button();
            buttons[i].setPrefSize(BUTTON_SIZE, BUTTON_SIZE);

            // Set the action for button clicks
            buttons[i].setOnAction(e -> handleButtonClick(finalI));

            // Add the button to the grid at the correct row and column position
            gridPane.add(buttons[i], i % NUMBER_OF_COLUMNS, i / NUMBER_OF_COLUMNS);
        }

        // Add the grid pane to the root container
        root.getChildren().add(gridPane);
    }


    /**
     * Validates the given grid pane object.
     * This method checks whether the provided grid pane is null. If it is,
     * a {@link NullPointerException} is thrown to indicate that the grid pane
     * is required for further processing.
     *
     * @param gridPane The grid pane to validate.
     */
    private static void validateGridPane(final GridPane gridPane)
    {
        if (gridPane == null)
        {
            throw new NullPointerException("gridPane is null");
        }
    }


    /**
     * Initializes the status label for displaying the next number to be placed.
     * This method creates a label that shows the next number to be placed and adds it
     * to the provided root container for the UI layout.
     *
     * @param root The root container for the UI layout.
     */
    private void initializeStatusLabel(final VBox root)
    {
        validateRoot(root);

        statusLabel = new Label("Next Number: ");
        root.getChildren().add(statusLabel);
    }


    /**
     * Resets the game grid, clearing all numbers from the buttons.
     * This method iterates through all the grid buttons, resetting their values
     * and clearing any displayed numbers by setting their text to an empty string.
     * It ensures that the grid is empty and ready for a new game or round.
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
     * Generates the next random number to be placed in the grid.
     * <p>
     * This method generates a random number within the specified range, using
     * the {@link #randomNumber(int, int)} method, and updates the status label
     * to display the next number that will be placed in the grid.
     * The random number is stored in {@code currentNumber} for further use in the game.
     */
    private void generateNextNumber()
    {
        currentNumber = randomNumber(MIN_RANDOM_NUM, MAX_RANDOM_NUM);
        statusLabel.setText("Next Number: " + currentNumber);
    }


    /**
     * Handles the button click event to place the current number in the grid.
     * <p>
     * This method processes the click event for a button in the grid. It checks if
     * the selected grid slot is empty, and if so, it places the current number in that
     * spot. The button's label is updated to display the number, and the count of
     * successful placements is incremented. After placing the number, it checks if
     * the grid is still in ascending order. If the order is disrupted, the game ends
     * and an alert is shown. If the grid is still valid, a new random number is generated
     * for the next placement.
     *
     * @param index The index of the button clicked, representing the grid position.
     */
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


    /**
     * Checks if all non-empty numbers in the grid are in strictly increasing order.
     * Empty cells (represented by NOTHING) are skipped during comparison.
     * The comparison starts from the first non-empty number found in the grid.
     * <p>
     * Returns true if either:
     * - All non-empty numbers are in increasing order (each number > previous number)
     * - The grid contains zero or one non-empty numbers
     * <p>
     * Returns false if any non-empty number is smaller than or equal to a previous non-empty number.
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
     * Displays and handles the game-over alert when the player can no longer place numbers.
     * <p>
     * The alert includes:
     * - "Game Over" title
     * - "You lost!" header
     * - Explanation message about being unable to place the next number
     * - Two action buttons: "Try Again" and "Quit"
     * <p>
     * If "Try Again" is selected:
     * - Resets the game grid to initial state
     * - Generates a new starting number
     * <p>
     * If "Quit" is selected:
     * - Displays the final score
     * - Closes the game window
     * <p>
     * The alert is modal and must be dismissed before continuing.
     */
    private void showGameOverAlert()
    {
        final InformationAlert alert;
        final ButtonType retryButton;
        final ButtonType quitButton;

        alert = new InformationAlert("Game Over");
        retryButton = new ButtonType("Try Again");
        quitButton = new ButtonType("Quit");

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
                ((Stage) statusLabel
                        .getScene()
                        .getWindow())
                        .close();
            }
        });
    }

    /**
     * Displays the player's final statistics in a modal dialog when the game ends.
     * <p>
     * The dialog shows:
     * - Total number of games played
     * - Total successful number placements across all games
     * - Average successful placements per game (formatted to 2 decimal places)
     * <p>
     * The statistics are formatted as:
     * "Games Played: X
     * Successful Placements: Y
     * Average Placements per Game: Z.ZZ"
     * <p>
     * The dialog has:
     * - "Final Score" as the window title
     * - "Game Over" as the header text
     * - A single OK button to dismiss the dialog
     * <p>
     * The dialog is modal and must be acknowledged before continuing.
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

        setUpAlert(alert);

        alert.setHeaderText("Game Over");
        alert.setContentText(contextTextString);
        alert.showAndWait();
    }

    /**
     * Displays a warning alert when the player attempts to place a number in an occupied slot.
     * <p>
     * The alert includes:
     * - "Invalid Move" as the title
     * - No header text (set to null)
     * - A clear message indicating the slot is already occupied
     * - A suggestion to try another slot
     * <p>
     * The alert contains a single OK button to acknowledge the warning.
     * It is modal and must be dismissed before the player can continue.
     * <p>
     * Example displayed message:
     * "This slot is already occupied. Try another slot."
     */
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
     * Displays the welcome message when the game first launches.
     * <p>
     * Shows a modal dialog containing:
     * - "Welcome" as the title
     * - A greeting message that dynamically includes the number of squares (using NUMBER_OF_SQUARES)
     * - Instructions to click OK to begin the game
     * - No header text (set to null)
     * <p>
     * The message format is:
     * "Welcome to the X square number game!
     * Click OK to start!"
     * <p>
     * The alert contains a single OK button and must be dismissed before gameplay begins.
     * This
     */
    private void showWelcomeAlert()
    {
        final InformationAlert alert;
        final StringBuilder messageBuilder;
        final String messageString;

        alert = new InformationAlert("Welcome");
        messageBuilder = new StringBuilder();

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

    /**
     * Configures the visual appearance and behavior of game alert dialogs.
     * <p>
     * Performs the following setup operations:
     * - Validates the alert parameter is not null
     * - Makes the alert background transparent
     * - Removes default window decorations (title bar, borders)
     * - Applies the game's custom stylesheet to the dialog
     * - Adds the "dialog-pane" style class for CSS targeting
     * <p>
     * The method expects an initialized Alert object and modifies its:
     * - Scene background (transparent)
     * - Window style (undecorated)
     * - Stylesheet reference
     * <p>
     * This ensures all game alerts maintain consistent visual styling.
     *
     * @param alert Alert object to be configured
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
     * Validates that the alert object is not null.
     *
     * @param alert The alert to validate.
     */
    private static void validateAlert(final Alert alert)
    {
        if (alert == null)
        {
            throw new IllegalArgumentException("Alert cannot be null");
        }
    }

    /**
     * Validates the index to ensure it is within the valid range.
     *
     * @param index The index to validate.
     */
    private static void validateIndex(final int index)
    {
        if (index < NOTHING || index > NUMBER_OF_SQUARES)
        {
            throw new IllegalArgumentException("Invalid index: " + index);
        }
    }

    /**
     * Validates min and max values, ensuring that the max is greater than the min.
     *
     * @param min int value
     * @param max int value
     */
    private static void validateMinMax(final int min, final int max)
    {
        if (min > max)
        {
            throw new IllegalArgumentException("Min cannot be greater than Max");
        }
    }

    /**
     * Validates the root layout object.
     *
     * @param root The root layout to validate.
     */
    private static void validateRoot(final VBox root)
    {
        if (root == null)
        {
            throw new IllegalArgumentException("Root cannot be null");
        }
    }
}
