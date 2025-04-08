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
    private static final Scanner SCANNER = new Scanner(System.in);
    private static final int QUESTIONS_PER_GAME = 10;
    private static final int NOTHING = 0;
    private static final int QUESTION_TYPE_ONE = 0;
    private static final int QUESTION_TYPE_TWO = 1;
    private static final int QUESTION_TYPE_THREE = 2;
    private static final int FACTS_PER_COUNTRY = 3;
    private static final int TYPES_OF_QUESTIONS = 3;
    private static final String PLAY_AGAIN_TRUE = "yes";
    private static final String PLAY_AGAIN_FALSE = "no";

    private static int gamesPlayed;
    private static int correctOnFirstAttempt;
    private static int correctOnSecondAttempt;
    private static int incorrectOnSecondAttempt;

    /**
     * Entry point for the capital cities word game application.
     * Initializes tracking variables and starts the main gameplay loop, where the user answers
     * a series of randomly selected questions about countries and their capital cities.
     * <p>
     * After each round of {@value QUESTIONS_PER_GAME} questions, the user is prompted to decide
     * whether to play another round. Input is validated to accept only "yes" or "no" (case-insensitive).
     * <p>
     * Once the user chooses not to continue, the game ends and a summary is displayed, including
     * whether a new high score was achieved.
     * <p>
     *
     * @param args command-line arguments (not used)
     */
    public static void main(final String[] args) throws IOException
    {
        String choice;

        choice = "yes";
        gamesPlayed = NOTHING;
        correctOnFirstAttempt = NOTHING;
        correctOnSecondAttempt = NOTHING;
        incorrectOnSecondAttempt = NOTHING;

        // Play again loop
        while (choice.equalsIgnoreCase(PLAY_AGAIN_TRUE))
        {
            // Gameplay loop
            for (int i = NOTHING; i < QUESTIONS_PER_GAME; i++)
            {
                askQuestion();
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

        gameOver();
    }

    /**
     * Prompts the user with a random trivia question about countries and evaluates their input.
     * The type of question is selected randomly from three options:
     * - Given a capital city, identify the country.
     * - Given a country, identify the capital city.
     * - Given a fact, identify the country.
     * <p>
     * This method initializes a new World object to access country data,
     * randomly selects a question type and a country from the data,
     * then uses the relevant fields to generate the question. The user's input
     * is evaluated using the evaluateUserInput method.
     * <p>
     * If an unknown question type is somehow generated, an IllegalStateException
     * will be thrown. After each question, a visual divider is printed to separate
     * the output from the next round.
     */
    private static void askQuestion() throws IOException
    {
        final World world;
        final HashMap<String, Country> worldHashMap;
        final List<Map.Entry<String, Country>> worldList;
        final Random ran;
        final int questionType;
        final int countryIndex;
        final Country currentCountry;

        world = new World();
        worldHashMap = world.getWorld();
        worldList = new ArrayList<>(worldHashMap.entrySet());
        ran = new Random();
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

    /**
     * Ends the game session and calculates the player's performance.
     * Displays whether the player has achieved a new high score based on average points per game.
     * The player's score is calculated, compared against the existing high score from a file,
     * and then appended to the file regardless of outcome.
     * <p>
     * The score is formatted to two decimal places for display. The method also prints
     * the final score summary and thanks the player for participating.
     * <p>
     * The player is prompted to press Enter to return to the main menu.
     */
    private static void gameOver() throws IOException
    {
        final DecimalFormat scoreFormat;
        final String file;
        final Score userScoreScore;
        final List<Score> scoresList;
        final double highScore;
        final double userScoreDouble;

        scoreFormat = new DecimalFormat("0.00");
        file = "src/data/wordgame_score.txt";

        userScoreScore = new Score(LocalDateTime.now(),
                gamesPlayed,
                correctOnFirstAttempt,
                correctOnSecondAttempt,
                incorrectOnSecondAttempt);

        userScoreDouble = userScoreScore.getAverageScore();
        scoresList = Score.readScoresFromFile(file);
        highScore = Score.getHighScore(scoresList);

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
