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
 * The Score class provides functionality to manage and retrieve high scores for the game.
 * Scores are stored in a text file, and this class handles reading and writing scores.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class Score
{
    private static final int NOTHING    = 0;
    private final static Path filePath  = Paths.get(
            "src",
            "code",
            "ca",
            "bcit",
            "termproject",
            "customgame",
            "data",
            "score.txt"
    );

    /**
     * Retrieves the highest score from the score file. If the file does not exist or is empty,
     * this method returns 0.
     *
     * @return The highest score stored in the file, or 0 if no valid scores are found.
     * @throws IOException If an I/O error occurs while reading the file.
     */
    public static int getHighScore() throws IOException
    {
        if (Files.notExists(filePath))
        {
            return NOTHING;
        }

        final List<String> lines;
        lines = Files.readAllLines(filePath);

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
     * @param score The score to be added to the file.
     * @throws IOException If an I/O error occurs while writing to the file.
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
     * Returns the average of all previous scores
     *
     * @return double value of score average
     */
    public static final double getAverageScore()
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
     * Validates that the score Integer value is not null, and is greater than 0.
     *
     * @param score Integer value to validate.
     */
    private static final void validateScore(final Integer score)
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
