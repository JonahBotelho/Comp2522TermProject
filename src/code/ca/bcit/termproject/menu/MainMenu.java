package ca.bcit.termproject.menu;

import ca.bcit.termproject.numbergame.NumberGame;
import ca.bcit.termproject.wordgame.WordGame;
import ca.bcit.termproject.customgame.ClockStormMain;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Scanner;

/**
 * Launches a menu interface for choosing between multiple games.
 * Provides options to play Word Game, Number Game, or ClockStorm Game.
 * Runs as a JavaFX application.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class MainMenu extends Application
{
    private static final String WORD_GAME_LETTER    = "W";
    private static final String NUMBER_GAME_LETTER  = "N";
    private static final String CLOCK_STORM_LETTER  = "C";
    private static final String QUIT_LETTER         = "Q";
    
    private static final int NOTHING = 0;
    
    
    /**
     * Entry point of the program.
     *
     * @param args unused.
     */
    public static void main(String[] args)
    {
        launch(args);
    }

    /**
     * Starts the JavaFX application by launching the game selection menu
     * in a separate thread. Handles user input and starts the selected game.
     *
     * @param stage the primary JavaFX stage
     */
    @Override
    public void start(final Stage stage)
    {
        final Thread menuThread;

        menuThread = new Thread(() ->{
            while(true)
            {
                final Scanner scan;
                String input;
                
                scan = new Scanner(System.in);
                
                System.out.println("Press " + WORD_GAME_LETTER + " to play the Word game.");
                System.out.println("Press " + NUMBER_GAME_LETTER + " to play the Number game.");
                System.out.println("Press " + CLOCK_STORM_LETTER + " to play the ClockStorm game");
                System.out.println("Press " + QUIT_LETTER + " to quit.");
                
                input = scan.nextLine();
                
                while (!(input.equalsIgnoreCase(WORD_GAME_LETTER) ||
                        input.equalsIgnoreCase(NUMBER_GAME_LETTER) ||
                        input.equalsIgnoreCase(CLOCK_STORM_LETTER) ||
                        input.equalsIgnoreCase(QUIT_LETTER)))
                {
                    System.out.println("Invalid input. Try again.");
                    input = scan.nextLine();
                }
                
                switch (input.toUpperCase())
                {
                    case WORD_GAME_LETTER:
                        try
                        {
                            WordGame.main(new String[NOTHING]);
                        }
                        catch (IOException e)
                        {
                            throw new RuntimeException(e);
                        }
                        break;
                    case NUMBER_GAME_LETTER:
                        Platform.runLater(() -> new NumberGame().start(stage));
                        Platform.setImplicitExit(false);
                        break;
                    case CLOCK_STORM_LETTER:
                        Platform.runLater(() -> new ClockStormMain().start(stage));
                        Platform.setImplicitExit(false);
                        break;
                }
                if (input.equalsIgnoreCase(QUIT_LETTER))
                {
                    return;
                }
            }
        });
        menuThread.start();
    }
}
