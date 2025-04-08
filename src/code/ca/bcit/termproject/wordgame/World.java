package ca.bcit.termproject.wordgame;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

/**
 * The World class represents a collection of countries, each stored in a HashMap with the country's name as the key
 * and a corresponding Country object as the value. The class is responsible for loading country data from text files,
 * parsing the data, and organizing it into a structured format for use in the WordGame.
 * <p>
 * Key Features:
 * 1. Loads country information from alphabetically organized text files (a.txt, b.txt, etc.).
 * 2. Each file contains country records in a specific format: country name and capital, followed by three facts.
 * 3. Validates file existence and reads data line by line to extract relevant country details.
 * 4. Creates Country objects based on the parsed data and stores them in a HashMap for efficient access during gameplay.
 * <p>
 * The constructor of the class iterates over files named after each letter of the alphabet (a.txt, b.txt, etc.) and processes
 * each file to extract the country's name, capital city, and three interesting facts. This data is used to create instances
 * of the Country class and stored in the `world` HashMap, which can later be accessed for the game.
 * <p>
 * The class also provides a method to retrieve the `world` HashMap, which contains all countries' data, for use in the WordGame.
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
     * Constructs a World object by loading and parsing country data from text files.
     * <p>
     * Initializes the world HashMap by reading country information from alphabetically organized
     * data files (a.txt, b.txt, etc.). Each file contains country records in a specific format:
     * <p>
     * For each country:
     * - First line: "CountryName:CapitalCity"
     * - Next 3 lines: Interesting facts about the country
     * <p>
     * The constructor:
     * - Processes files for each letter of the alphabet (a-z)
     * - Validates file existence before reading
     * - Parses each country's name, capital, and facts
     * - Creates Country objects and stores them in the world HashMap
     * <p>
     * Expected file structure example:
     * "Canada:Ottawa
     *  Fact 1 about Canada
     *  Fact 2 about Canada
     *  Fact 3 about Canada"
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
