package ca.bcit.termproject.wordgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class World
{
    private static final int FACTS_LENGTH = 3;
    private static final int ALPHABET_LENGTH = 26;
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int THIRD_INDEX = 2;


    private HashMap<String, Country> world;

    public World() {
        int index;

        world = new HashMap<>();
        char fileIndex = 'a';
        index = 0;

        for (int i = 0; i < ALPHABET_LENGTH; i++)
        {
            final String fileName;
            final Scanner fileScanner;

            fileName = "\\res\\" + fileIndex + ".txt";
            fileScanner = new Scanner(fileName);
            fileScanner.useDelimiter(";|\\n");

            fileIndex++;

            while (fileScanner.hasNextLine())
            {
                final String name;
                final String capitalCityName;
                final String[] facts;
                final Country currentCountry;

                facts = new String[FACTS_LENGTH];

                name = fileScanner.next();
                capitalCityName = fileScanner.next();
                facts[FIRST_INDEX] = fileScanner.nextLine();
                facts[SECOND_INDEX] = fileScanner.nextLine();
                facts[THIRD_INDEX] = fileScanner.nextLine();

                currentCountry = new Country(name, capitalCityName, facts);
                world.put(name, currentCountry);
            }

        }

    }

    public HashMap<String, Country> getWorld()
    {
        return this.world;
    }
}
