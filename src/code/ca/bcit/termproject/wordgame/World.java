package ca.bcit.termproject.wordgame;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.HashMap;

public class World
{
    private static final int FACTS_LENGTH = 3;
    private static final int ALPHABET_LENGTH = 26 + 'a';
    private static final int FIRST_INDEX = 0;
    private static final int SECOND_INDEX = 1;
    private static final int THIRD_INDEX = 2;


    private HashMap<String, Country> world;

    public World() throws FileNotFoundException
    {
        world = new HashMap<>();
        char fileIndex;

        for (fileIndex = 'a'; fileIndex < ALPHABET_LENGTH; fileIndex++)
        {
            if (fileIndex == 'w')
            {
                fileIndex += 2;
            }
            
            final String fileName;
            final Scanner fileScanner;

            fileName = "src\\res\\" + fileIndex + ".txt";
            fileScanner = new Scanner(new File(fileName));
            fileScanner.useDelimiter(":|\\n");
            
            

            while (fileScanner.hasNext())
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
                fileScanner.nextLine();
                
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
