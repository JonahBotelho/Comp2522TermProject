package ca.bcit.termproject.wordgame;

import java.util.Random;
import java.util.Scanner;

public class WordGame
{
    private static final int QUESTIONS_PER_GAME = 10;
    private static final int NOTHING = 0;

    public static void main(String[] args)
    {
        final Random ran;
        final Scanner scan;
        String choice;

        int gamesPlayed;
        int correctOnFirstAttempt;
        int correctOnSecondAttempt;
        int incorrectOnSecondAttempt;

        ran = new Random();
        scan = new Scanner(System.in);
        choice = "yes";

        gamesPlayed = NOTHING;
        correctOnFirstAttempt = NOTHING;
        correctOnSecondAttempt = NOTHING;
        incorrectOnSecondAttempt = NOTHING;

        // Play again loop
        while(choice.equalsIgnoreCase("yes"))
        {

            //Gameplay loop
            for (int i = NOTHING; i < QUESTIONS_PER_GAME; i++)
            {

            }
            System.out.print("Do you want to play again? (yes/no): ");
            choice = scan.nextLine();

            while (!(choice.equalsIgnoreCase("yes") ||
                     choice.equalsIgnoreCase("no")))
            {
                System.out.print("Invalid choice. Please try again (yes/no): ");
                choice = scan.nextLine();
            }
        }
        System.out.println("\nThank you for playing!");
    }
}
