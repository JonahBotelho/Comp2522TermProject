package ca.bcit.termproject;

import ca.bcit.termproject.numbergame.NumberGame;
import ca.bcit.termproject.wordgame.WordGame;
import ca.bcit.termproject.customgame.MainGame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Jonah Botelho
 * @version 1.0
 */
public class Main
{
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
            System.out.println("Press W to play the Word game.");
            System.out.println("Press N to play the Number game.");
            System.out.println("Press M to play the _____ game");
            System.out.println("Press Q to quit.");

            input = scan.nextLine();

            while (!(input.equalsIgnoreCase("W") ||
                    input.equalsIgnoreCase("N") ||
                    input.equalsIgnoreCase("M") ||
                    input.equalsIgnoreCase("Q")))
            {
                System.out.println("Invalid input. Try again.");
                input = scan.nextLine();
            }

            switch (input)
            {
                case "W", "w":
                    WordGame.main(new String[NOTHING]);
                    break;
                case "N", "n":
                    NumberGame.main(new String[NOTHING]);
                    break;
                case "M", "m":
                    MainGame.main(new String[NOTHING]);
                    break;
            }

        } while (!(input.equalsIgnoreCase("q")));
    }
}