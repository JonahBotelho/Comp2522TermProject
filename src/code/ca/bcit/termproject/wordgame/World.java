package ca.bcit.termproject.wordgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

/**
 * Represents a collection of countries, where each country is stored in a
 * HashMap with its name as the key and the Country object as the value. The class
 * loads country data from text files and stores it for use in the word game.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public class World
{
    private static final int FACTS_LENGTH       = 3;
    private static final int ALPHABET_LENGTH    = 26 + 'a';
    private static final int FIRST_INDEX        = 0;
    private static final int SECOND_INDEX       = 1;
    private static final int THIRD_INDEX        = 2;

    private final HashMap<String, Country> world;

    /**
     * Constructs a World object by reading country data from text files and
     * storing it in a HashMap.
     */
    public World() throws FileNotFoundException
    {
        world = new HashMap<>();
        char fileIndex;

        // Loop through letters 'a' to 'z' to load country data
        for (fileIndex = 'a'; fileIndex < ALPHABET_LENGTH; fileIndex++)
        {
            // Skip the letter 'w'
            if (fileIndex == 'w')
            {
                fileIndex += 2;
            }

            final String fileName;
            final Scanner fileScanner;

            // Construct the file path for each letter-based file
            fileName = "src\\res\\" + fileIndex + ".txt";
            fileScanner = new Scanner(new File(fileName));
            fileScanner.useDelimiter("[:\\n]");

            // Read the country data from the file
            while (fileScanner.hasNext())
            {
                final String countryAndCapitalLine;
                final String[] countryAndCapitalString;
                final String name;
                final String capitalCityName;
                final String[] facts;
                final Country currentCountry;

                facts = new String[FACTS_LENGTH];

                // Skip the first line of (empty)
                fileScanner.nextLine();

                // Read country data
                countryAndCapitalLine = fileScanner.nextLine();
                countryAndCapitalString = countryAndCapitalLine.split(":");

                name = countryAndCapitalString[FIRST_INDEX];
                capitalCityName = countryAndCapitalString[SECOND_INDEX];

                facts[FIRST_INDEX] = fileScanner.nextLine();
                facts[SECOND_INDEX] = fileScanner.nextLine();
                facts[THIRD_INDEX] = fileScanner.nextLine();

                currentCountry = new Country(name, capitalCityName, facts);
                world.put(name, currentCountry);
            }
        }
    }

    /**
     * Retrieves the map of countries where the key is the country's name and
     * the value is the Country object.
     *
     * @return A HashMap containing all the countries and their data.
     */
    public final HashMap<String, Country> getWorld()
    {
        return this.world;
    }
}
