package ca.bcit.termproject.wordgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * Represents a collection of countries, where each country is stored in a
 * HashMap with its name as the key and the Country object as the value. The class
 * loads country data from text files and stores it for use in the word game.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
public final class World
{
    private static final int FILE_READER_START   = 1;
    private static final int FACTS_LENGTH        = 3;
    private static final int ALPHABET_LENGTH     = 26 + 'a';
    private static final int FIRST_INDEX         = 0;
    private static final int SECOND_INDEX        = 1;
    private static final int THIRD_INDEX         = 2;
    private static final int OFFSET_AT_START     = 2;
    private static final int FIRST_FACT_OFFSET   = 1;
    private static final int SECOND_FACT_OFFSET  = 2;
    private static final int THIRD_FACT_OFFSET   = 3;

    private final HashMap<String, Country> world;

    /**
     * Constructs a World object by reading country data from text files and
     * storing it in a HashMap.
     */
    public World() throws IOException
    {
        world = new HashMap<>();

        for (char fileIndex = 'a'; fileIndex < ALPHABET_LENGTH; fileIndex++)
        {
            final String fileName;
            final Path filePath;

            fileName = "src/res/wordgame/countrydata/" + fileIndex + ".txt";
            filePath = Paths.get(fileName);

            if (Files.exists(filePath))
            {
                final List<String> lines;
                lines = Files.readAllLines(filePath);

                for (int i = FILE_READER_START; i < lines.size(); i += FACTS_LENGTH + OFFSET_AT_START)
                {
                    final String[] countryAndCapitalString;
                    final String name;
                    final String capitalCityName;
                    final String[] facts;
                    final Country currentCountry;

                    countryAndCapitalString = lines.get(i).split(":");
                    name = countryAndCapitalString[FIRST_INDEX];
                    capitalCityName = countryAndCapitalString[SECOND_INDEX];

                    facts = new String[FACTS_LENGTH];
                    facts[FIRST_INDEX] = lines.get(i + FIRST_FACT_OFFSET);
                    facts[SECOND_INDEX] = lines.get(i + SECOND_FACT_OFFSET);
                    facts[THIRD_INDEX] = lines.get(i + THIRD_FACT_OFFSET);

                    currentCountry = new Country(name, capitalCityName, facts);

                    world.put(name, currentCountry);
                }
            }
        }
    }

    /**
     * Retrieves the map of countries where the key is the country's name and
     * the value is the Country object.
     *
     * @return A HashMap containing all the countries and their data.
     */
    public HashMap<String, Country> getWorld()
    {
        return this.world;
    }
}
