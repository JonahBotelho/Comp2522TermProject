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
 * Represents a player's score in the word game.
 * Stores statistics and provides methods for file I/O.
 */
public final class Score
{
    private static final int NOTHING = 0;
    private static final int MIN_YEAR = 2000;
    private static final int MAX_YEAR = 2100;
    private static final int POINTS_FOR_FIRST_ATTEMPT = 2;
    private static final int POINTS_FOR_SECOND_ATTEMPT = 1;
    private static final int LINES_PER_SCORE_OBJECT = 4;
    private static final int WORDS_PER_SCORE_FIRST_LINE = 2;
    private static final int FIRST_INDEX_OF_DATE_IN_SCORE_LINE = 15;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

        this.currentTime = currentTime;
        this.gamesPlayed = gamesPlayed;
        this.correctOnFirstAttempt = correctOnFirstAttempt;
        this.correctOnSecondAttempt = correctOnSecondAttempt;
        this.incorrectOnSecondAttempt = incorrectOnSecondAttempt;

        this.score = correctOnFirstAttempt * POINTS_FOR_FIRST_ATTEMPT +
                correctOnSecondAttempt * POINTS_FOR_SECOND_ATTEMPT;
    }

    /**
     * Validates that the current date is between 2000 and 2025, and that it is not null.
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

        if (Files.notExists(filePath))
        {
            Files.createFile(filePath);
        }

        Files.writeString(filePath, score + System.lineSeparator(), StandardOpenOption.APPEND);
    }

    /**
     * Reads scores from a file and returns a list of Score objects.
     *
     * @param scoreFile The file path from which scores will be read.
     * @return A list of Score objects.
     */
    public static List<Score> readScoresFromFile(final String scoreFile) throws IOException
    {
        validateString(scoreFile);

        final Path filePath;
        final List<Score> scores;
        final List<String> lines;

        filePath = Path.of(scoreFile);

        if (Files.notExists(filePath))
        {
            return null;
        }

        lines = Files.readAllLines(filePath);

        scores = new ArrayList<>();

        for (int i = 0; i < lines.size(); i += 7)
        {
            final String currentTimeLine;
            final LocalDateTime currentTime;
            String[][] scoreLines;
            final int[] scoreValues;

            scoreLines = new String[LINES_PER_SCORE_OBJECT][WORDS_PER_SCORE_FIRST_LINE];
            scoreValues = new int[LINES_PER_SCORE_OBJECT];

            currentTimeLine = lines.get(i).substring(FIRST_INDEX_OF_DATE_IN_SCORE_LINE);
            currentTime = LocalDateTime.parse(currentTimeLine, formatter);
            for (int j = 0; j < LINES_PER_SCORE_OBJECT; j++)
            {
                scoreLines[j] = lines.get(i + j + 1).split(":");
                scoreValues[j] = Integer.parseInt(scoreLines[j][1].trim());
            }

            scores.add(new Score(currentTime, scoreValues[0], scoreValues[1], scoreValues[2], scoreValues[3]));
        }

        return scores;
    }

    /**
     * Returns a string representation of the Score object.
     *
     * @return A formatted string representing the score details.
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
     * Returns the highest average score value from a list of Score objects.
     *
     * @param scores Score object list.
     * @return highest average score as a double
     */
    public static double getHighScore(final List<Score> scores)
    {
        if (scores == null)
        {
            return NOTHING;
        }

        final double highScore;
        final Optional<Score> optionalHighScore;

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
     * Returns the timestamp when the score was recorded.
     *
     * @return The timestamp of the score.
     */
    public LocalDateTime getCurrentTime()
    {
        return this.currentTime;
    }

    /**
     * Returns the total number of games played.
     *
     * @return The number of games played.
     */
    public int getGamesPlayed()
    {
        return this.gamesPlayed;
    }

    /**
     * Returns the number of correct answers on the first attempt.
     *
     * @return The count of correct first attempts.
     */
    public int getCorrectOnFirstAttempt()
    {
        return this.correctOnFirstAttempt;
    }

    /**
     * Returns the number of correct answers on the second attempt.
     *
     * @return The count of correct second attempts.
     */
    public int getCorrectOnSecondAttempt()
    {
        return this.correctOnSecondAttempt;
    }

    /**
     * Returns the number of incorrect answers on the second attempt.
     *
     * @return The count of incorrect second attempts.
     */
    public int getIncorrectOnSecondAttempt()
    {
        return this.incorrectOnSecondAttempt;
    }

    /**
     * Returns the total score based on correct answers.
     *
     * @return The total score.
     */
    public int getScore()
    {
        return this.score;
    }

    /**
     * Calculates the average score per game.
     *
     * @return The average score per game.
     */
    public double getAverageScore()
    {
        return (double) this.getScore() / this.getGamesPlayed();
    }
}
