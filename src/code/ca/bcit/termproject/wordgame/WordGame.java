package ca.bcit.termproject.wordgame;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;

public class WordGame
{
    private static final int QUESTIONS_PER_GAME = 10;
    private static final int NOTHING = 0;

    private static int gamesPlayed;
    private static int correctOnFirstAttempt;
    private static int correctOnSecondAttempt;
    private static int incorrectOnSecondAttempt;

    public static void main(final String[] args) throws IOException
    {
        final String file;
        final World world;
        final HashMap<String, Country> worldHashMap;
        final List<Map.Entry<String, Country>> worldList;
        final Random ran;
        final Scanner scan;
        String choice;

        file = "score.txt";
        world = new World();
        worldHashMap = world.getWorld();
        worldList = new ArrayList<>(worldHashMap.entrySet());
        ran = new Random();
        scan = new Scanner(System.in);
        choice = "yes";
        
        gamesPlayed = NOTHING;
        correctOnFirstAttempt = NOTHING;
        correctOnSecondAttempt = NOTHING;
        incorrectOnSecondAttempt = NOTHING;

        // Play again loop
        while (choice.equalsIgnoreCase("yes"))
        {

            //Gameplay loop
            for (int i = NOTHING; i < QUESTIONS_PER_GAME; i++)
            {
                final int questionType;
                final int countryIndex;
                final Country currentCountry;
                final String input;

                questionType = ran.nextInt(3);
                countryIndex = ran.nextInt(worldList.size());
                currentCountry = worldList.get(countryIndex).getValue();

                switch (questionType)
                {
                    // The program will print a capital city, and ask the user what country it is the capital of
                    case 0:
                        System.out.println("\nWhat country is " + currentCountry.getCapitalCityName() + " the capital of?");
                        evaluateUserInput(currentCountry.getName());
                        break;
                    //The program will print the country name, and ask the user what is its capital city
                    case 1:
                        System.out.println("\nWhat is the capital of " + currentCountry.getName() + "?");
                        evaluateUserInput(currentCountry.getCapitalCityName());;
                        break;
                    // The program will print one of the three facts, and ask the user which country is being described
                    case 2:
                        int factIndex;
                        factIndex = ran.nextInt(3);

                        System.out.println("\nWhat country is being described?");
                        System.out.println(currentCountry.getFacts()[factIndex]);

                        evaluateUserInput(currentCountry.getName());;

                        break;
                }
                System.out.println("___________________________________________");
            }
            
            gamesPlayed++;
            System.out.print("Do you want to play again? (yes/no): ");
            choice = scan.nextLine();

            while (!(choice.equalsIgnoreCase("yes") ||
                    choice.equalsIgnoreCase("no")))
            {
                System.out.print("Invalid choice. Please try again (yes/no): ");
                choice = scan.nextLine();
            }
        }

        Score score = new Score(LocalDateTime.now(),
                                gamesPlayed,
                                correctOnFirstAttempt,
                                correctOnSecondAttempt,
                                incorrectOnSecondAttempt);
        
        List<Score> scores = Score.readScoresFromFile(file);

        Score highScore;
        highScore = new Score(LocalDateTime.now(), 100, NOTHING, NOTHING, NOTHING);
        for (Score currentScore : scores)
        {
            if (currentScore.getAverageScore() > highScore.getAverageScore())
            {
                highScore = currentScore;
            }
        }

        if (score.getAverageScore() > highScore.getAverageScore())
        {
            System.out.println("You have a new high score of " + score.getAverageScore() + " points per game.");
            System.out.println("The previous high score was " + highScore.getAverageScore() + " points per game.");
        }
        else {
            System.out.println("Your score of " + score.getAverageScore() + " points per game was not a high score.");
        }
        Score.appendScoreToFile(score, file);
        
        System.out.println("");
        System.out.println(score.toString());
        System.out.println("");
        System.out.println("\nThank you for playing!");

    }

    private static void evaluateUserInput(final String answer)
    {
        final Scanner scan;
        String input;

        scan = new Scanner(System.in);
        input = scan.nextLine();

        if (input.equalsIgnoreCase(answer))
        {
            System.out.printf("%s is Correct!\n", input);
            correctOnFirstAttempt++;
        }
        else
        {
            System.out.println("Incorrect!");
            System.out.print("Try again: ");
            input = scan.nextLine();
            if (input.equalsIgnoreCase(answer))
            {
                System.out.printf("%s is Correct!\n", input);
                correctOnSecondAttempt++;
            }
            else
            {
                System.out.println("Incorrect!");
                System.out.printf("The correct answer is: %s\n", answer);
                incorrectOnSecondAttempt++;
            }
        }
    }
}
