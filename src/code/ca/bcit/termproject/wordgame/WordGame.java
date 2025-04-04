package ca.bcit.termproject.wordgame;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Scanner;
import java.text.DecimalFormat;

/**
 * The WordGame class implements a simple quiz game where the player is asked
 * questions about world capitals and facts related to different countries.
 * The game keeps track of scores and stores them in a file.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class WordGame
{
    private static final Scanner SCANNER            = new Scanner(System.in);
    private static final int QUESTIONS_PER_GAME     = 10;
    private static final int NOTHING                = 0;
    private static final int QUESTION_TYPE_ONE      = 0;
    private static final int QUESTION_TYPE_TWO      = 1;
    private static final int QUESTION_TYPE_THREE    = 2;
    private static final int FACTS_PER_COUNTRY      = 3;
    private static final int TYPES_OF_QUESTIONS     = 3;
    private static final String PLAY_AGAIN_TRUE     = "yes";
    private static final String PLAY_AGAIN_FALSE    = "no";

    private static int correctOnFirstAttempt;
    private static int correctOnSecondAttempt;
    private static int incorrectOnSecondAttempt;

    /**
     * The main method that runs the game.
     * It initializes the game world, starts the question loop, and handles score storage.
     *
     * @param args unused
     */
    public static void main(final String[] args) throws IOException
    {
        final String file;
        final World world;
        final HashMap<String, Country> worldHashMap;
        final List<Map.Entry<String, Country>> worldList;
        final Random ran;
        final Score userScoreScore;
        final List<Score> scoresList;
        final DecimalFormat scoreFormat;

        int gamesPlayed;
        String choice;

        final double highScore;
        final double userScoreDouble;


        file = "src/data/wordgame_score.txt";
        world = new World();
        worldHashMap = world.getWorld();
        worldList = new ArrayList<>(worldHashMap.entrySet());
        ran = new Random();
        choice = "yes";

        gamesPlayed = NOTHING;
        correctOnFirstAttempt = NOTHING;
        correctOnSecondAttempt = NOTHING;
        incorrectOnSecondAttempt = NOTHING;

        scoreFormat = new DecimalFormat("0.00");
        // Play again loop
        while (choice.equalsIgnoreCase(PLAY_AGAIN_TRUE))
        {
            // Gameplay loop
            for (int i = NOTHING; i < QUESTIONS_PER_GAME; i++)
            {
                final int questionType;
                final int countryIndex;
                final Country currentCountry;

                questionType = ran.nextInt(TYPES_OF_QUESTIONS);
                countryIndex = ran.nextInt(worldList.size());
                currentCountry = worldList.get(countryIndex).getValue();

                switch (questionType)
                {
                    // The program will print a capital city, and ask the user what country it is the capital of
                    case QUESTION_TYPE_ONE:
                        System.out.println("\nWhat country is " +
                                currentCountry.getCapitalCityName() +
                                " the capital of?");

                        evaluateUserInput(currentCountry.getName());
                        break;
                    // The program will print the country name, and ask the user what is its capital city
                    case QUESTION_TYPE_TWO:
                        System.out.println("\nWhat is the capital of " +
                                currentCountry.getName() +
                                "?");

                        evaluateUserInput(currentCountry.getCapitalCityName());
                        break;
                    // The program will print one of the three facts, and ask the user which country is being described
                    case QUESTION_TYPE_THREE:
                        int factIndex;
                        factIndex = ran.nextInt(FACTS_PER_COUNTRY);

                        System.out.println("\nWhat country is being described?");
                        System.out.println(currentCountry.getFacts()[factIndex]);

                        evaluateUserInput(currentCountry.getName());
                        break;
                    default:
                        // This should be impossible to reach
                        throw new IllegalStateException("Unexpected value: " + questionType);
                }
                System.out.println("___________________________________________");
            }

            gamesPlayed++;
            System.out.print("Do you want to play again? (yes/no): ");
            choice = SCANNER.nextLine();

            while (!(choice.equalsIgnoreCase(PLAY_AGAIN_TRUE) ||
                    choice.equalsIgnoreCase(PLAY_AGAIN_FALSE)))
            {
                System.out.print("Invalid choice. Please try again (" +
                        PLAY_AGAIN_TRUE + "/" +
                        PLAY_AGAIN_FALSE + "): ");
                choice = SCANNER.nextLine();
            }
        }

        userScoreScore = new Score(LocalDateTime.now(),
                gamesPlayed,
                correctOnFirstAttempt,
                correctOnSecondAttempt,
                incorrectOnSecondAttempt);

        userScoreDouble = userScoreScore.getAverageScore();
        scoresList      = Score.readScoresFromFile(file);
        highScore       = Score.getHighScore(scoresList);

        if (userScoreDouble > highScore)
        {
            System.out.println("You have a new high score of " +
                    scoreFormat.format(userScoreDouble) +
                    " points per game.");
            System.out.println("The previous high score was " +
                    scoreFormat.format(highScore) +
                    " points per game.");
        }
        else
        {
            System.out.println("Your score of " +
                    scoreFormat.format(userScoreDouble) +
                    " points per game was not a high score.");
        }

        Score.appendScoreToFile(userScoreScore, file);

        System.out.println();
        System.out.println(userScoreScore);
        System.out.println("Thank you for playing!");
        System.out.println("Press enter to return to the main menu.");

        SCANNER.nextLine();
    }

    /**
     * Prompts the user for an answer and evaluates their response.
     * The user has two attempts to answer correctly.
     * <p>
     * Designed to be run through MainMenu. May not work as intended if opened separately.
     *
     * @param answer The correct answer for the question.
     */
    private static void evaluateUserInput(final String answer)
    {
        validateAnswer(answer);

        String input;
        input = SCANNER.nextLine();

        if (input.equalsIgnoreCase(answer))
        {
            System.out.printf("%s is Correct!\n", input);
            correctOnFirstAttempt++;
        }
        else
        {
            System.out.println("Incorrect!");
            System.out.print("Try again: ");
            input = SCANNER.nextLine();
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

    /**
     * Validates the given answer to ensure it is not null or blank.
     *
     * @param answer The answer to validate.
     * @throws IllegalArgumentException If the answer is null or blank.
     */
    private static void validateAnswer(final String answer)
    {
        if (answer == null || answer.isBlank())
        {
            throw new IllegalArgumentException("Invalid answer.");
        }
    }
}
