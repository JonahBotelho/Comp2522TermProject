package ca.bcit.termproject.wordgame;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.concurrent.LinkedTransferQueue;

public class Score
{
    private int score;


    public Score(final LocalDateTime currentTime,
                 final int gamesPlayed,
                 final int correctOnFirstAttempt,
                 final int correctOnSecondAttempt,
                 final int incorrectOnSecondAttempt)
    {

    }

    public static void appendScoreToFile(final Score score,
                                         final String scoreFile)
    {

    }

    public static List <Score> readScoresFromFile(final String scoreFile)
    {
        return null;
    }

    @Override
    public String toString()
    {
        return "";
    }

    public final int getScore()
    {
        return this.score;
    }

}
