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
 * The WordGame class implements a quiz game focused on world capitals and countries.
 * The game presents questions to the user, tracks their performance, and allows them
 * to play multiple rounds. It also stores high scores in a file and compares the user's
 * current score to the highest recorded score. The game is designed to be played with
 * a series of questions, with feedback provided to the user on correct or incorrect answers.
 * <p>
 * Key Features:
 * 1. Presents random questions about countries, capitals, and facts related to countries.
 * 2. Tracks user performance, including correct answers on the first and second attempts.
 * 3. Provides feedback after each attempt and shows the correct answer if the user fails twice.
 * 4. Allows the user to play multiple rounds, with the option to play again after each round.
 * 5. Saves and compares the user's score to the high score stored in a file.
 * 6. Uses `DecimalFormat` to display scores with two decimal places.
 * <p>
 * The game flow is controlled by a main game loop, which generates questions based on random
 * selections and evaluates user input. The user can play multiple rounds, and after each round,
 * their performance is recorded. The highest score is saved to a text file, and the user is notified
 * if they have achieved a new high score.
 * <p>
 * After the game, the user is prompted to return to the main menu by pressing enter.
 * <p>
 * This class is the entry point for running the word game application.
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
     * The main method for running the word game application.
     * <p>
     * This method serves as the entry point for the game, initializing key components such as the game world, the list
     * of countries, and the random number generator. It also controls the game flow, presenting the user with a series of
     * questions and collecting their answers. The user can choose to play multiple rounds, and after the game ends, the
     * method compares the user's average score to the highest score recorded in a score file. If the user's score exceeds
     * the high score, the file is updated, and a new high score is announced. The method also includes a play-again loop,
     * input validation for user choices, and displays relevant game information and statistics.
     * <p>
     * Key steps in the method:
     * 1. Initializes required variables such as the file path, world data, and game settings.
     * 2. Starts a loop for the gameplay, where a series of questions are asked based on random selection.
     * 3. Handles the user's responses to questions about countries, capitals, and facts.
     * 4. Tracks the user's performance during the game and evaluates the score at the end.
     * 5. Compares the user's score to the high score, displaying whether the user achieved a new high score.
     * 6. Provides the option to play again, and if the user chooses not to, the final score is saved to a file.
     * 7. Ends the game and prompts the user to return to the main menu.
     *
     * @param args The command-line arguments passed to the application (not used).
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

        file            = "src/data/wordgame_score.txt";
        world           = new World();
        worldHashMap    = world.getWorld();
        worldList       = new ArrayList<>(worldHashMap.entrySet());
        ran             = new Random();
        choice          = "yes";

        gamesPlayed                 = NOTHING;
        correctOnFirstAttempt       = NOTHING;
        correctOnSecondAttempt      = NOTHING;
        incorrectOnSecondAttempt    = NOTHING;

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
     * Evaluates user input against the correct answer with two attempts.
     * <p>
     * Compares user input (case-insensitive) to the expected answer:
     * - On first correct attempt: increments correctOnFirstAttempt counter
     * - On first incorrect attempt: gives one retry opportunity
     * - On second correct attempt: increments correctOnSecondAttempt counter
     * - On second incorrect attempt: reveals answer and increments incorrectOnSecondAttempt counter
     * <p>
     * Provides real-time feedback for each attempt:
     * - "Correct!" messages for successful attempts
     * - "Incorrect!" messages for failed attempts
     * - Displays correct answer after second failure
     * <p>
     * Uses standard input (System.in) via SCANNER for user interaction.
     * Validates the answer parameter before processing.
     * <p>
     * Designed to be run through MainMenu. Program may perform unexpectedly if ran separately.
     *
     * @param answer The correct answer string to compare against user input
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
     * Throws IllegalArgumentException if answer is null or blank.
     *
     * @param answer The answer to validate as a String.
     */
    private static void validateAnswer(final String answer)
    {
        if (answer == null || answer.isBlank())
        {
            throw new IllegalArgumentException("Invalid answer.");
        }
    }
}
