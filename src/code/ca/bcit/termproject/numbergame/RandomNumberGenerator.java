package ca.bcit.termproject.numbergame;

/**
 * An interface for classes that generate random numbers.
 * Implementing classes must provide a method to generate the next number.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public interface RandomNumberGenerator
{
    /**
     * Generates the next random number.
     * Implementing classes define the logic for number generation.
     */
    int randomNumber(final int min, final int max);
}