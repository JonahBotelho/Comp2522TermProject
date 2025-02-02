package ca.bcit.termproject.wordgame;

public class Country
{
    private static final int NOTHING = 0;
    private static final int MAX_STRING_LENGTH = 50;

    final String name;
    final String capitalCityName;
    final String[] facts;

    public Country(final String name, final String capitalCityName, final String[] facts)
    {
        validateString(name);
        validateString(capitalCityName);
        validateStringArray(facts);

        this.name = name;
        this.capitalCityName = capitalCityName;
        this.facts = facts;
    }

    private void validateString(String s)
    {
        if (s == null || s.isBlank() || s.length() > MAX_STRING_LENGTH)
        {
            throw new IllegalArgumentException("Invalid String");
        }
    }

    private void validateStringArray(String[] s)
    {
        if (s == null || s.length == NOTHING) {
            throw new IllegalArgumentException("Invalid String array");
        }
    }

    public String getName()
    {
        return name;
    }

    public String getCapitalCityName()
    {
        return capitalCityName;
    }
}
