package ca.bcit.termproject.wordgame;

import java.util.Arrays;

/**
 * The {@code Country} class represents a country with its name, capital city, and a collection of facts
 * about it. This class provides functionality to retrieve the country name, capital city name, and facts
 * about the country.
 *
 * The class validates that the country name, capital city name, and facts are not null or empty. It ensures
 * that the facts are stored in a valid array and performs validation for each individual fact.
 *
 * The class offers methods to access the country’s name, capital city name, and facts through getters.
 *
 * The class is immutable, meaning once a {@code Country} object is created, its attributes cannot be changed.
 * It is designed to enforce strong validation for its input data during construction to prevent invalid values.
 *
 * This class also includes validation methods to ensure strings are non-null and non-blank, as well as to
 * confirm that the array of facts is neither null nor empty.
 *
 * @author Jonah Botelho
 * @version 1.0
 */

public final class Country
{
    private static final int NOTHING = 0;

    final String name;
    final String capitalCityName;
    final String[] facts;

    /**
     * Constructs a Country object with the specified name, capital city, and facts.
     *
     * @param name            The name of the country.
     * @param capitalCityName The name of the capital city of the country.
     * @param facts           An array of facts about the country.
     */
    public Country(final String name,
                   final String capitalCityName,
                   final String[] facts)
    {
        validateString(name);
        validateString(capitalCityName);
        validateStringArray(facts);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    /**
     * Validates that a string is neither null nor blank.
     *
     * @param s The string to validate.
     */
    public void validateString(final String s)
    {
        if (s == null || s.isBlank())
        {
            throw new IllegalArgumentException("Invalid String");
        }
    }

    /**
     * Validates that a string array is neither null nor empty.
     *
     * @param s The string array to validate.
     */
    private void validateStringArray(final String[] s)
    {
        if (s == null || s.length == NOTHING)
        {
            throw new IllegalArgumentException("Invalid String array");
        }
        
        Arrays.stream(s).forEach(this::validateString);
    }

    /**
     * Gets the name of the country as a String.
     *
     * @return The name of the country.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the name of the capital city as a String.
     *
     * @return The name of the capital city.
     */
    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Gets the facts about the country as a String array.
     *
     * @return An array of facts about the country.
     */
    public String[] getFacts()
    {
        return facts;
    }
}
