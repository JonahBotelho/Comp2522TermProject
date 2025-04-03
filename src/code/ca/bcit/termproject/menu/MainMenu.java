package ca.bcit.termproject.menu;

import ca.bcit.termproject.numbergame.NumberGame;
import ca.bcit.termproject.wordgame.WordGame;
import ca.bcit.termproject.customgame.ClockStormMain;
import javafx.application.Application;

import java.io.IOException;
import java.util.Scanner;

/**
 * @author Jonah Botelho
 * @version 1.0
 */
public final class MainMenu
{
    private static final String WORD_GAME_LETTER      = "W";
    private static final String NUMBER_GAME_LETTER    = "N";
    private static final String CUSTOM_GAME_NUMBER    = "M";
    private static final String QUIT_LETTER           = "Q";

    private static final int NOTHING = 0;

    /**
     * Drives the program.
     *
     * @param args unused.
     */
    public static void main(String[] args) throws IOException
    {
        final Scanner scan;
        String input;

        scan = new Scanner(System.in);

        do
        {
            System.out.println("Press " + WORD_GAME_LETTER      + " to play the Word game.");
            System.out.println("Press " + NUMBER_GAME_LETTER    + " to play the Number game.");
            System.out.println("Press " + CUSTOM_GAME_NUMBER    + " to play the _____ game");
            System.out.println("Press " + QUIT_LETTER           + " to quit.");

            input = scan.nextLine();

            while (!(input.equalsIgnoreCase(WORD_GAME_LETTER) ||
                    input.equalsIgnoreCase(NUMBER_GAME_LETTER) ||
                    input.equalsIgnoreCase(CUSTOM_GAME_NUMBER) ||
                    input.equalsIgnoreCase(QUIT_LETTER)))
            {
                System.out.println("Invalid input. Try again.");
                input = scan.nextLine();
            }

            switch (input.toUpperCase())
            {
                case WORD_GAME_LETTER:
                    WordGame.main(new String[NOTHING]);
                    break;
                case NUMBER_GAME_LETTER:
                    new Thread(() -> Application.launch(NumberGame.class)).start();
                    break;
                case CUSTOM_GAME_NUMBER:
                    new Thread(() -> Application.launch(ClockStormMain.class)).start();
                    break;
            }

        } while (!(input.equalsIgnoreCase(QUIT_LETTER)));
    }
}
