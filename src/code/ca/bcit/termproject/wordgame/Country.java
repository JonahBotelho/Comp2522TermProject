package ca.bcit.termproject.wordgame;

import java.util.Arrays;

/**
 * Represents a country with its name, capital city, and some facts about it.
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
     * Gets the name of the country.
     *
     * @return The name of the country.
     */
    public String getName()
    {
        return name;
    }

    /**
     * Gets the name of the capital city.
     *
     * @return The name of the capital city.
     */
    public String getCapitalCityName()
    {
        return capitalCityName;
    }

    /**
     * Gets the facts about the country.
     *
     * @return An array of facts about the country.
     */
    public String[] getFacts()
    {
        return facts;
    }
}
