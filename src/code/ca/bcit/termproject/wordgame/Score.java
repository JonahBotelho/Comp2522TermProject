package ca.bcit.termproject.wordgame;

import java.io.IOException;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

/**
 * The {@code Score} class represents a player's score in the word game, storing various statistics
 * such as the number of games played, the number of correct answers on the first and second attempts,
 * and the number of incorrect answers on the second attempt. It calculates the total score based on
 * correct answers and provides functionality for file input/output operations related to the player's scores.
 * <p>
 * This class offers validation for all input data, including checking that the current time is within
 * a valid range (2000-2100), ensuring that integers are non-negative, and validating strings. It also
 * provides methods to append scores to a file, read scores from a file, and generate a formatted string
 * representation of the score.
 * <p>
 * In addition, the class includes a method to determine the highest average score from a list of scores.
 * It calculates the average score per game based on the total score and the number of games played.
 * <p>
 * The class is designed to store immutable score data and is suitable for file-based persistence of game
 * statistics. All methods related to file I/O ensure that scores are correctly appended to or read from files
 * with appropriate validation.
 *
 * @author Jonah Botelho
 * @version 1.0
 */

public final class Score
{
    private static final int NOTHING                            = 0;
    public static final int FIRST_INDEX                         = 0;
    public static final int SECOND_INDEX                        = 1;
    public static final int THIRD_INDEX                         = 2;
    public static final int FOURTH_INDEX                        = 3;

    private static final int MIN_YEAR                           = 2000;
    private static final int MAX_YEAR                           = 2100;
    private static final int POINTS_FOR_FIRST_ATTEMPT           = 2;
    private static final int POINTS_FOR_SECOND_ATTEMPT          = 1;
    private static final int LINES_PER_SCORE_OBJECT             = 4;
    private static final int WORDS_PER_SCORE_FIRST_LINE         = 2;
    private static final int FIRST_INDEX_OF_DATE_IN_SCORE_LINE  = 15;
    private static final DateTimeFormatter formatter            = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime currentTime;
    private final int gamesPlayed;
    private final int correctOnFirstAttempt;
    private final int correctOnSecondAttempt;
    private final int incorrectOnSecondAttempt;
    private final int score;

    /**
     * Constructs a Score object with given game statistics.
     *
     * @param currentTime              The timestamp when the score was recorded.
     * @param gamesPlayed              The total number of games played.
     * @param correctOnFirstAttempt    The number of correct answers on the first attempt.
     * @param correctOnSecondAttempt   The number of correct answers on the second attempt.
     * @param incorrectOnSecondAttempt The number of incorrect answers on the second attempt.
     */
    public Score(final LocalDateTime currentTime,
                 final int gamesPlayed,
                 final int correctOnFirstAttempt,
                 final int correctOnSecondAttempt,
                 final int incorrectOnSecondAttempt)
    {
        validateCurrentTime(currentTime);
        validateIntegers(gamesPlayed);
        validateIntegers(correctOnFirstAttempt);
        validateIntegers(correctOnSecondAttempt);
        validateIntegers(incorrectOnSecondAttempt);

        this.currentTime                = currentTime;
        this.gamesPlayed                = gamesPlayed;
        this.correctOnFirstAttempt      = correctOnFirstAttempt;
        this.correctOnSecondAttempt     = correctOnSecondAttempt;
        this.incorrectOnSecondAttempt   = incorrectOnSecondAttempt;

        this.score = correctOnFirstAttempt * POINTS_FOR_FIRST_ATTEMPT +
                    correctOnSecondAttempt * POINTS_FOR_SECOND_ATTEMPT;
    }

    /**
     * Validates that the current date is between MIN_YEAR and MAX_YEAR, and that it is not null.
     *
     * @param currentTime the LocalDateTime to validate.
     */
    private static void validateCurrentTime(final LocalDateTime currentTime)
    {
        if (currentTime == null)
        {
            throw new IllegalArgumentException("Current time cannot be null");
        }

        if (currentTime.getYear() > MAX_YEAR || currentTime.getYear() < MIN_YEAR)
        {
            throw new IllegalArgumentException("Current date must be between 2000 and 2100");
        }
    }

    /**
     * Validates that an integer is non-negative.
     *
     * @param integer The integer to validate.
     */
    private static void validateIntegers(final int integer)
    {
        if (integer < NOTHING)
        {
            throw new IllegalArgumentException("integer cannot be negative");
        }
    }

    /**
     * Validates that a string is not null or empty.
     *
     * @param s The string to validate.
     */
    private static void validateString(final String s)
    {
        if (s == null || s.isBlank())
        {
            throw new IllegalArgumentException("Invalid String");
        }
    }

    /**
     * Validates that a Score object is not null.
     *
     * @param score The Score object to validate.
     */
    private static void validateScore(final Score score)
    {
        if (score == null)
        {
            throw new IllegalArgumentException("Score cannot be null");
        }
    }

    /**
     * Appends a Score object to a file.
     *
     * @param score     The Score object to append.
     * @param scoreFile The file path where the score will be stored.
     */
    public static void appendScoreToFile(final Score score,
                                         final String scoreFile) throws IOException
    {
        validateString(scoreFile);
        validateScore(score);

        Path filePath;
        filePath = Paths.get(scoreFile);

        // handles score file not existing
        if (Files.notExists(filePath))
        {
            Files.createFile(filePath);
        }

        // appends score to file
        Files.writeString(filePath, score + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    /**
     * Reads scores from a specified file and parses them into a list of {@link Score} objects.
     * <p>
     * This method reads the contents of a file, where each score entry is assumed to consist of
     * 7 lines. The first line contains a timestamp, and the following 6 lines contain score-related
     * data. Each score entry is parsed and stored as a {@link Score} object, which is added to a list
     * that is returned at the end. If the file does not exist, the method returns {@code null}.
     *
     * @param scoreFile The path to the score file to read.
     * @return A list of {@link Score} objects parsed from the file, or {@code null} if the file does not exist.
     */
    public static List<Score> readScoresFromFile(final String scoreFile) throws IOException
    {
        validateString(scoreFile);

        final Path filePath;
        final List<Score> scores;
        final List<String> lines;

        filePath = Path.of(scoreFile);

        // handles score file not existing
        if (Files.notExists(filePath))
        {
            return null;
        }

        lines = Files.readAllLines(filePath);

        scores = new ArrayList<>();

        // iterates through score file
        for (int i = 0; i < lines.size(); i += 7)
        {
            final String currentTimeLine;
            final LocalDateTime currentTime;
            String[][] scoreLines;
            final int[] scoreValues;

            scoreLines = new String[LINES_PER_SCORE_OBJECT][WORDS_PER_SCORE_FIRST_LINE];
            scoreValues = new int[LINES_PER_SCORE_OBJECT];

            // retrieve time
            currentTimeLine = lines.get(i).substring(FIRST_INDEX_OF_DATE_IN_SCORE_LINE);
            currentTime = LocalDateTime.parse(currentTimeLine, formatter);

            // retrieve the remaining score info
            for (int j = NOTHING; j < LINES_PER_SCORE_OBJECT; j++)
            {
                scoreLines[j] = lines.get(i + j + SECOND_INDEX).split(":");
                scoreValues[j] = Integer.parseInt(scoreLines[j][1].trim());
            }

            scores.add(new Score(currentTime,
                                 scoreValues[FIRST_INDEX],
                                 scoreValues[SECOND_INDEX],
                                 scoreValues[THIRD_INDEX],
                                 scoreValues[FOURTH_INDEX]));
        }

        return scores;
    }

    /**
     * Generates a formatted string representation of the {@link Score} object.
     * <p>
     * The string includes all score metrics in a human-readable format with labels:
     * - Date and Time (formatted using the class's DateTimeFormatter)
     * - Games Played
     * - Correct First Attempts
     * - Correct Second Attempts
     * - Incorrect Attempts
     * - Total Score (with "points" suffix)
     * <p>
     * Each metric appears on its own line with proper labeling. The format is consistent
     * and suitable for display to users or logging purposes.
     *
     * @return Formatted string containing all score information
     */
    @Override
    public String toString()
    {
        final StringBuilder result;
        final String returnString;
        result = new StringBuilder();

        result.append("Date and Time: ");
        result.append(this.getCurrentTime().format(formatter));
        result.append("\nGames Played: ");
        result.append(this.getGamesPlayed());
        result.append("\nCorrect First Attempts: ");
        result.append(this.getCorrectOnFirstAttempt());
        result.append("\nCorrect Second Attempts: ");
        result.append(this.getCorrectOnSecondAttempt());
        result.append("\nIncorrect Attempts: ");
        result.append(this.getIncorrectOnSecondAttempt());
        result.append("\nScore: ");
        result.append(this.getScore());
        result.append(" points\n");

        returnString = result.toString();
        return returnString;
    }

    /**
     * Determines the highest average score from a list of Score objects.
     * <p>
     * Processes the input list to find the maximum average score value:
     * - Returns {@code NOTHING} constant (typically 0.0) if input list is null or empty
     * - Uses Java Streams to efficiently find the maximum average score
     * - Compares scores using {@code Score::getAverageScore} as the sorting key
     * - Safely handles empty Optional results from the stream operation
     * <p>
     * The method is null-safe and will not throw exceptions for null input,
     * making it suitable for use with potentially uninitialized score lists.
     *
     * @param scores List of Score objects to evaluate (may be null or empty)
     * @return The highest average score found, or {@code NOTHING} if no valid scores exist
     */
    public static double getHighScore(final List<Score> scores)
    {
        if (scores == null)
        {
            return NOTHING;
        }

        final double highScore;
        final Optional<Score> optionalHighScore;

        // retrieve high score as optional double
        optionalHighScore = scores.stream()
                .max(Comparator.comparingDouble(Score::getAverageScore));

        if (optionalHighScore.isPresent())
        {
            highScore = optionalHighScore.get().getAverageScore();
        }
        else
        {
            highScore = NOTHING;
        }

        return highScore;
    }

    /**
     * Returns the timestamp when the score was recorded as a LocalDateTime.
     *
     * @return The timestamp of the score.
     */
    public LocalDateTime getCurrentTime()
    {
        return this.currentTime;
    }

    /**
     * Returns the total number of games played as an int.
     *
     * @return The number of games played.
     */
    public int getGamesPlayed()
    {
        return this.gamesPlayed;
    }

    /**
     * Returns the number of correct answers on the first attempt as an int.
     *
     * @return The count of correct first attempts.
     */
    public int getCorrectOnFirstAttempt()
    {
        return this.correctOnFirstAttempt;
    }

    /**
     * Returns the number of correct answers on the second attempt as an int.
     *
     * @return The count of correct second attempts.
     */
    public int getCorrectOnSecondAttempt()
    {
        return this.correctOnSecondAttempt;
    }

    /**
     * Returns the number of incorrect answers on the second attempt as an int.
     *
     * @return The count of incorrect second attempts.
     */
    public int getIncorrectOnSecondAttempt()
    {
        return this.incorrectOnSecondAttempt;
    }

    /**
     * Returns the total score based on correct answers as an int.
     *
     * @return The total score.
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Calculates the average score per game as a double.
     *
     * @return The average score per game.
     */
    public double getAverageScore()
    {
        return (double) this.getScore() / this.getGamesPlayed();
    }
}
