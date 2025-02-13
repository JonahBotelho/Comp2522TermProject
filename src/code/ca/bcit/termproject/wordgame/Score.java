package ca.bcit.termproject.wordgame;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Score
{
    private static final int NOTHING = 0;
    private static final int POINTS_FOR_FIRST_ATTEMPT = 2;
    private static final int POINTS_FOR_SECOND_ATTEMPT = 1;
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private final LocalDateTime currentTime;
    private final int gamesPlayed;
    private final int correctOnFirstAttempt;
    private final int correctOnSecondAttempt;
    private final int incorrectOnSecondAttempt;
    private final int score;

    public Score(final LocalDateTime currentTime,
                 final int gamesPlayed,
                 final int correctOnFirstAttempt,
                 final int correctOnSecondAttempt,
                 final int incorrectOnSecondAttempt)
    {
        //TODO validate time
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

    private static final void validateIntegers(final int integer)
    {
        if (integer < NOTHING)
        {
            throw new IllegalArgumentException("integer cannot be negative");
        }
    }

    private static final void validateString(final String s)
    {
        if (s == null || s.isBlank())
        {
            throw new IllegalArgumentException("Invalid String");
        }
    }

    private static final void validateScore(final Score score)
    {
        if (score == null)
        {
            throw new IllegalArgumentException("Score cannot be null");
        }
    }

    public static void appendScoreToFile(final Score score,
                                         final String scoreFile) throws IOException
    {
        validateString(scoreFile);
        validateScore(score);
        
        File file;
        file = new File(scoreFile);
        if (!file.exists())
        {
            file.createNewFile();
        }
        final FileWriter writer;
        writer = new FileWriter(scoreFile, true);

        writer.write(score.toString());
        writer.write("\n");
        writer.close();
    }

    public static List <Score> readScoresFromFile(final String scoreFile) throws FileNotFoundException
    {
        validateString(scoreFile);

        final Scanner scan;
        final List<Score> scores;

        scan = new Scanner(new File(scoreFile));
        scores = new ArrayList<Score>();

        while (scan.hasNext())
        {
            final String currentTimeLine;
            final LocalDateTime currentTime;
            String[][] scoreLines;
            final int[] scoreValues;

            scoreLines = new String[4][2];
            scoreValues = new int[4];

            currentTimeLine = scan.nextLine().substring(15);
            currentTime = LocalDateTime.parse(currentTimeLine, formatter);
            for (int i = 0; i < 4; i++)
            {
                scoreLines[i] = scan.nextLine().split(":");
                scoreValues[i] = Integer.parseInt(scoreLines[i][1].trim());
            }

            scores.add(new Score(currentTime,scoreValues[0], scoreValues[1], scoreValues[2], scoreValues[3]));
            scan.nextLine();
            scan.nextLine();
        }

        return scores;
    }

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

    public LocalDateTime getCurrentTime()
    {
        return this.currentTime;
    }

    public final int getGamesPlayed()
    {
        return this.gamesPlayed;
    }

    public final int getCorrectOnFirstAttempt()
    {
        return this.correctOnFirstAttempt;
    }

    public final int getCorrectOnSecondAttempt()
    {
        return this.correctOnSecondAttempt;
    }

    public final int getIncorrectOnSecondAttempt()
    {
        return this.incorrectOnSecondAttempt;
    }

    public final int getScore()
    {
        return this.score;
    }
    
    
    public final double getAverageScore()
    {
        return (double) this.getScore() / this.getGamesPlayed();
    }
}
