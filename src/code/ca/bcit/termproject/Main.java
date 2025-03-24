package ca.bcit.termproject;

import ca.bcit.termproject.numbergame.NumberGame;
import ca.bcit.termproject.wordgame.WordGame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

/**
 * @author Jonah Botelho
 * @version 1.0
 */
public class Main
{
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
                    WordGame.main(new String[0]);
                    break;
                case "N", "n":
                    NumberGame.main(new String[0]);
                    break;
                case "M", "m":

                    break;
            }

        } while (!(input.equalsIgnoreCase("q")));
    }
}