package ca.bcit.termproject.customgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Objects;
import java.util.OptionalDouble;
import java.util.OptionalInt;

/**
 * The {@code ClockStormScore} class provides functionality for managing and retrieving high scores in the game.
 * It allows adding new scores, retrieving the highest score, and calculating the average score.
 * Scores are stored in a text file located at {@code "src/data/clockstorm_score.txt"}.
 * The class handles reading from and writing to this file, ensuring the file is created if it doesn't exist.
 * It also provides validation to ensure that scores added are non-null and non-negative.
 *
 * Key Features:
 * - Retrieve the highest score from the score file.
 * - Add a new score to the score file, appending it without overwriting existing scores.
 * - Calculate the average of all previous scores stored in the file.
 * - Validate that the score being added is a non-null, non-negative integer.
 *
 * The class ensures the creation of necessary directories and the score file if they do not already exist.
 * If any issues arise (e.g., the file is empty or does not exist), it will return default values such as 0.
 *
 * This class is essential for tracking the game's scores and provides an interface for managing player scores.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class ClockStormScore
{
    private static final int NOTHING    = 0;
    private final static Path filePath  = Paths.get(
            "src",
            "data",
            "clockstorm_score.txt"
    );

    /**
     * Retrieves the highest score from the score file. If the file does not exist, is empty,
     * or contains no valid scores, this method returns 0.
     *
     * This method reads the score file, processes the lines, and attempts to extract the highest
     * score. If any issues occur (e.g., file does not exist, file is empty, or no valid integers
     * are found), it will return a default value of 0.
     *
     * @return The highest score stored in the file, or 0 if no valid scores are found.
     */
    public static int getHighScore() throws IOException
    {
        if (Files.notExists(filePath.getParent())) {
            Files.createDirectories(filePath.getParent()); // creates parent directories if needed
        }

        if (Files.notExists(filePath))
        {
            return NOTHING;
        }

        final List<String> lines;
        lines = Files.readAllLines(filePath);

        // removes any lines that are not integer values
        OptionalInt highScore = lines.stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.isBlank())
                .filter(s -> s.matches("-?\\d+"))
                .mapToInt(Integer::parseInt)
                .max();

        if (highScore.isPresent())
        {
            return highScore.getAsInt();
        }

        return NOTHING;
    }


    /**
     * Adds a score to the score file. If the file does not exist, it will be created.
     *
     * This method writes a given score to a file. If the file or its parent directory does not exist,
     * it will be created. The score is appended to the file, ensuring that previous scores are not overwritten.
     *
     * @param score The score to be added to the file.
     */
    public static void addScore(final Integer score)
            throws IOException
    {
        validateScore(score);

        if (Files.notExists(filePath.getParent()))
        {
            Files.createDirectories(filePath.getParent());
        }

        if (Files.notExists(filePath))
        {
            Files.createFile(filePath);
        }

        Files.writeString(filePath,
                score + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }


    /**
     * Returns the average of all previous scores.
     *
     * This method reads the scores from the score file and calculates the average.
     * If the file does not exist or no valid scores are found, it returns a predefined constant value.
     *
     * @return The average of all previous scores as a double. If no valid scores are found, returns the predefined constant value.
     */
    public static double getAverageScore()
            throws IOException
    {
        if (Files.notExists(filePath))
        {
            return NOTHING;
        }

        final List<String> lines;
        final double average;

        lines = Files.readAllLines(filePath);

        OptionalDouble averageOptionalDouble = lines.stream()
                .filter(s -> s.matches("-?\\d+"))
                .mapToInt(Integer::parseInt)
                .average();

        if (averageOptionalDouble.isPresent())
        {
            average = averageOptionalDouble.getAsDouble();
            return average;
        }

        return NOTHING;
    }


    /**
     * Validates that the score Integer value is not null and is greater than or equal to 0.
     *
     * This method checks if the provided score is null or negative. If either condition is true, an
     * IllegalArgumentException is thrown with an appropriate error message.
     *
     * @param score The Integer value to validate.
     */
    private static void validateScore(final Integer score)
    {
        if (score == null)
        {
            throw new IllegalArgumentException("Score cannot be null");
        }

        if (score < NOTHING)
        {
            throw new IllegalArgumentException("Score cannot be negative");
        }
    }

}
