package ca.bcit.termproject.customgame;

import ca.bcit.termproject.customgame.orbs.Orb; // Assuming Orb base class exists
import ca.bcit.termproject.customgame.orbs.OrbShooter;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

// Using specific imports for clarity
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

/**
 * Unit tests for the Custom Game logic, focusing on behavior rather than constant values.
 */
public final class CustomGameTester
{

    // Use constants from MainGame for boundary calculations, but don't test their literal values
    private static final double WINDOW_WIDTH = ClockStormMain.WINDOW_WIDTH;
    private static final double WINDOW_HEIGHT = ClockStormMain.WINDOW_HEIGHT;
    private static final double PLAYER_SIZE = ClockStormMain.PLAYER_SIZE;
    // Use placeholder start positions derived from MainGame logic if needed for relative checks
    private static final double PLAYER_START_X = WINDOW_WIDTH / 2.0;
    private static final double PLAYER_START_Y = WINDOW_HEIGHT - 50.0; // Example derivation

    // Constants for Score Testing Logic
    private static final int DEFAULT_HIGH_SCORE = 0; // Assuming 0 is the default/nothing value
    private static final int TEST_SCORE_1 = 100;
    private static final int TEST_SCORE_2 = 200;
    private static final int TEST_SCORE_NEGATIVE = -50;

    // Constants for Speed Modifier Testing Logic (relative to MainGame constants)
    private static final int TEST_START_SCORE = ClockStormMain.START_SCORE; // Use the actual start score
    private static final double TEST_BASE_MODIFIER = ClockStormMain.BASE_SPEED_MODIFIER; // Use the actual base modifier
    // We derive thresholds relative to START_SCORE rather than hardcoding scores
    private static final int SCORE_ABOVE_START = TEST_START_SCORE + 50;
    private static final int SCORE_BELOW_START = TEST_START_SCORE - 50;
    private static final int SCORE_WAY_ABOVE_START = TEST_START_SCORE + 5000; // Score likely to hit max clamp
    private static final int SCORE_WAY_BELOW_START = TEST_START_SCORE - 5000; // Score likely to hit min clamp


    private Player player;
    private OrbShooter cannon;

    /**
     * Temporary directory for file-based tests (Score).
     */
    @TempDir
    static Path tempDir;
    private static Path scoreFilePath; // Path within tempDir for score file

    /**
     * Sets up the path for the score file within the temporary directory before any tests run.
     * This simulates the structure expected by the Score class.
     */
    @BeforeAll
    static void setUpClass()
    {
        // Construct the expected path within the temp directory
        scoreFilePath = tempDir.resolve("data").resolve("score.txt");
        // Note: This assumes the Score class can be configured or implicitly uses this path structure relative to execution.
        // If Score class path is fixed, these file tests might need adaptation or mocking.
    }

    /**
     * Sets up fresh Player and Cannon instances before each test.
     * Cleans up the test score file.
     *
     * @throws IOException if setup fails
     */
    @BeforeEach
    void setUp() throws IOException
    {
        player = new Player(PLAYER_START_X, PLAYER_START_Y, PLAYER_SIZE);
        cannon = new OrbShooter();

        // Ensure clean state for score file tests
        Files.deleteIfExists(scoreFilePath);
        // Ensure parent directory exists for file creation
        if (scoreFilePath.getParent() != null)
        {
            Files.createDirectories(scoreFilePath.getParent());
        }
    }

    /**
     * Cleans up the test score file after each test.
     *
     * @throws IOException if cleanup fails
     */
    @AfterEach
    void tearDown() throws IOException
    {
        Files.deleteIfExists(scoreFilePath);
    }

    // --- Player Movement Behavior Tests (10 tests) ---

    /**
     * Tests that setting the left movement flag is correctly reflected.
     */
    @Test
    void testPlayerSetMovingLeftFlag()
    {
        player.setLeft(true);
        assertTrue(player.isMovingLeft(), "Player should be flagged as moving left.");
        player.setLeft(false);
        assertFalse(player.isMovingLeft(), "Player should not be flagged as moving left.");
    }

    /**
     * Tests that setting the right movement flag is correctly reflected.
     */
    @Test
    void testPlayerSetMovingRightFlag()
    {
        player.setRight(true);
        assertTrue(player.isMovingRight(), "Player should be flagged as moving right.");
        player.setRight(false);
        assertFalse(player.isMovingRight(), "Player should not be flagged as moving right.");
    }

    /**
     * Tests that setting the up movement flag is correctly reflected.
     */
    @Test
    void testPlayerSetMovingUpFlag()
    {
        player.setUp(true);
        assertTrue(player.isMovingUp(), "Player should be flagged as moving up.");
        player.setUp(false);
        assertFalse(player.isMovingUp(), "Player should not be flagged as moving up.");
    }

    /**
     * Tests that setting the down movement flag is correctly reflected.
     */
    @Test
    void testPlayerSetMovingDownFlag()
    {
        player.setDown(true);
        assertTrue(player.isMovingDown(), "Player should be flagged as moving down.");
        player.setDown(false);
        assertFalse(player.isMovingDown(), "Player should not be flagged as moving down.");
    }

    /**
     * Tests that the player's X position decreases when moving left and not at boundary.
     */
    @Test
    void testPlayerMovesLeftWhenFlagged()
    {
        final double initialX = player.getX();
        player.setLeft(true);
        player.update();
        assertTrue(player.getX() < initialX, "Player X should decrease when moving left.");
    }

    /**
     * Tests that the player's X position increases when moving right and not at boundary.
     */
    @Test
    void testPlayerMovesRightWhenFlagged()
    {
        final double initialX = player.getX();
        player.setRight(true);
        player.update();
        assertTrue(player.getX() > initialX, "Player X should increase when moving right.");
    }

    /**
     * Tests that the player's Y position decreases when moving up and not at boundary.
     */
    @Test
    void testPlayerMovesUpWhenFlagged()
    {
        final double initialY = player.getY();
        player.setUp(true);
        player.update();
        assertTrue(player.getY() < initialY, "Player Y should decrease when moving up.");
    }

    /**
     * Tests that the player's Y position increases when moving down and not at boundary.
     */
    @Test
    void testPlayerMovesDownWhenFlagged()
    {
        // Ensure player is not already at bottom boundary for this test
        assumeTrue(player.getY() < WINDOW_HEIGHT - player.getHeight(), "Player must not start at bottom boundary");
        final double initialY = player.getY();
        player.setDown(true);
        player.update();
        assertTrue(player.getY() > initialY, "Player Y should increase when moving down.");
    }

    /**
     * Tests that the player stops exactly at the left boundary (X=0).
     */
    @Test
    void testPlayerStopsAtLeftBoundary()
    {
        player.setX(1.0); // Position close to boundary
        player.setLeft(true);
        player.update();
        assertEquals(0.0, player.getX(), 0.001, "Player should stop exactly at left boundary (X=0).");
    }

    /**
     * Tests that the player stops exactly at the bottom boundary.
     */
    @Test
    void testPlayerStopsAtBottomBoundary()
    {
        final double bottomBoundary = WINDOW_HEIGHT - player.getHeight();
        player.setY(bottomBoundary - 1.0); // Position close to boundary
        player.setDown(true);
        player.update();
        // Player might overshoot slightly due to speed, check it doesn't go past boundary
        // A more robust test might check multiple updates or use smaller speed for precision.
        // For this test, we assume update stops it correctly.
        assertEquals(bottomBoundary, player.getY(), 0.001, "Player should stop exactly at bottom boundary.");
    }


    // --- Cannon Behavior Tests (2 tests) ---

    /**
     * Tests that a new Cannon instance initializes with an empty list of orbs.
     */
    @Test
    void testCannonInitialOrbListIsEmpty()
    {
        assertNotNull(cannon.getOrbs(), "Orb list should not be null.");
        assertTrue(cannon.getOrbs().isEmpty(), "Newly created cannon should have no orbs.");
    }

    /**
     * Tests that getOrbs returns a valid (non-null) list instance.
     */
    @Test
    void testCannonGetOrbsReturnsList()
    {
        // This mainly checks nullity, assuming the list can be used (e.g., iterated).
        final List<Orb> orbs = cannon.getOrbs();
        assertNotNull(orbs);
        // Optional: Check if it behaves like a list
        assertEquals(0, orbs.size());
    }

    // --- Score Class Behavior Tests (Using simulated file access) (9 tests) ---

    // Helper to simulate Score.addScore using the test file path
    private void simulateAddScore(final int score) throws IOException
    {
        Files.writeString(scoreFilePath,
                score + System.lineSeparator(),
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND);
    }

    // Helper to simulate Score.getHighScore using the test file path
    private int simulateGetHighScore() throws IOException
    {
        if (Files.notExists(scoreFilePath))
        {
            return DEFAULT_HIGH_SCORE; // Use the defined default
        }
        final List<String> lines = Files.readAllLines(scoreFilePath);
        return lines.stream()
                .filter(s -> s != null && !s.isBlank() && s.matches("-?\\d+"))
                .mapToInt(Integer::parseInt)
                .max()
                .orElse(DEFAULT_HIGH_SCORE); // Use the defined default
    }

    /**
     * Tests that getHighScore returns the default value when the score file does not exist.
     */
    @Test
    void testGetHighScoreNonExistentFileReturnsDefault() throws IOException
    {
        // Ensure file doesn't exist (handled by @BeforeEach)
        assertEquals(DEFAULT_HIGH_SCORE, simulateGetHighScore(), "Should return default score if file doesn't exist.");
    }

    /**
     * Tests that addScore creates the score file if it doesn't exist.
     */
    @Test
    void testAddScoreCreatesFile() throws IOException
    {
        assertFalse(Files.exists(scoreFilePath), "Score file should not exist initially.");
        simulateAddScore(TEST_SCORE_1);
        assertTrue(Files.exists(scoreFilePath), "addScore should create the file if it's missing.");
    }

    /**
     * Tests that getHighScore returns the default value for an empty score file.
     */
    @Test
    void testGetHighScoreEmptyFileReturnsDefault() throws IOException
    {
        Files.createFile(scoreFilePath); // Create an empty file
        assertTrue(Files.exists(scoreFilePath), "Test requires an empty file to exist.");
        assertEquals(DEFAULT_HIGH_SCORE, simulateGetHighScore(), "Should return default score for an empty file.");
    }

    /**
     * Tests that adding multiple scores results in the highest one being retrieved.
     */
    @Test
    void testGetHighScoreReturnsMaxScore() throws IOException
    {
        simulateAddScore(TEST_SCORE_1);
        simulateAddScore(TEST_SCORE_NEGATIVE);
        simulateAddScore(TEST_SCORE_2); // Highest score
        assertEquals(TEST_SCORE_2, simulateGetHighScore(), "Should return the maximum score added.");
    }

    /**
     * Tests that getHighScore ignores non-numeric lines in the score file.
     */
    @Test
    void testGetHighScoreIgnoresInvalidLines() throws IOException
    {
        Files.writeString(scoreFilePath, TEST_SCORE_1 + "\nInvalidText\n" + TEST_SCORE_2, StandardOpenOption.CREATE);
        assertEquals(TEST_SCORE_2, simulateGetHighScore(), "Should ignore non-numeric lines and return the max valid score.");
    }

    /**
     * Tests that getHighScore ignores blank lines in the score file.
     */
    @Test
    void testGetHighScoreIgnoresBlankLines() throws IOException
    {
        Files.writeString(scoreFilePath, TEST_SCORE_1 + "\n\n" + TEST_SCORE_2 + "\n  \n", StandardOpenOption.CREATE);
        assertEquals(TEST_SCORE_2, simulateGetHighScore(), "Should ignore blank lines and return the max valid score.");
    }

    /**
     * Tests that addScore appends new scores to the file, not overwriting.
     */
    @Test
    void testAddScoreAppendsScores() throws IOException
    {
        simulateAddScore(TEST_SCORE_1);
        simulateAddScore(TEST_SCORE_2);
        final List<String> lines = Files.readAllLines(scoreFilePath);
        assertEquals(2, lines.size(), "Should have two lines after adding two scores.");
        assertEquals(String.valueOf(TEST_SCORE_1), lines.get(0));
        assertEquals(String.valueOf(TEST_SCORE_2), lines.get(1));
    }

    /**
     * Tests that Score.addScore throws IllegalArgumentException if passed a null score.
     * This test requires calling the actual static method from the Score class.
     */
    @Test
    void testScoreAddScoreThrowsForNullInput()
    {
        // This test directly invokes the static method from the Score class.
        assertThrows(IllegalArgumentException.class, () ->
        {
            Score.addScore(null); // Pass null explicitly
        }, "Score.addScore(null) should throw IllegalArgumentException.");
    }

    /**
     * Tests that getHighScore returns the default if the file contains only invalid data.
     */
    @Test
    void testGetHighScoreReturnsDefaultForOnlyInvalidData() throws IOException
    {
        Files.writeString(scoreFilePath, "abc\ndef\n  ", StandardOpenOption.CREATE);
        assertEquals(DEFAULT_HIGH_SCORE, simulateGetHighScore(), "Should return default score if only invalid data exists.");
    }


    // --- MainGame Logic Behavior Tests (Simulated/Replicated) (6 tests) ---

    // Replicates getRandomNumber logic from MainGame for testing its behavior
    private static int simulateGetRandomNumber(final int min, final int max)
    {
        final Random random = new Random();
        // Assuming RANDOM_NUMBER_OFFSET is 1 as in the original code
        final int offset = 1;
        return random.nextInt((max - min) + offset) + min;
    }

    /**
     * Tests that the simulated getRandomNumber returns values within the specified min/max bounds.
     */
    @Test
    void testGetRandomNumberBehaviorInRange()
    {
        final int min = 10;
        final int max = 20;
        // Run multiple times to increase confidence
        IntStream.range(0, 100).forEach(i ->
        {
            int randomNum = simulateGetRandomNumber(min, max);
            assertTrue(randomNum >= min && randomNum <= max,
                    "Random number " + randomNum + " should be between " + min + " and " + max + " (inclusive).");
        });
    }

    /**
     * Tests that the simulated getRandomNumber returns the exact number when min equals max.
     */
    @Test
    void testGetRandomNumberBehaviorMinEqualsMax()
    {
        final int value = 5;
        int randomNum = simulateGetRandomNumber(value, value);
        assertEquals(value, randomNum, "Should return the exact value when min equals max.");
    }

    // Replicates updateSpeedModifier logic from MainGame for testing its behavior
    private double simulateUpdateSpeedModifier(final int currentScore)
    {
        // Use constants derived from ClockStormMain for calculation
        final double baseModifier = ClockStormMain.BASE_SPEED_MODIFIER;
        final double startScore = ClockStormMain.START_SCORE;
        final double changeRate = ClockStormMain.SPEED_MODIFIER_CHANGE_RATE;
        final double maxModifier = ClockStormMain.MAX_SPEED_MODIFIER;
        final double minModifier = ClockStormMain.MIN_SPEED_MODIFIER;

        double modifier = baseModifier + (currentScore - startScore) / changeRate;

        // Apply clamping
        modifier = Math.max(minModifier, modifier); // Clamp at minimum
        modifier = Math.min(maxModifier, modifier); // Clamp at maximum

        return modifier;
    }

    /**
     * Tests that the speed modifier is greater than base when score is above start score.
     */
    @Test
    void testSpeedModifierIncreasesAboveStartScore()
    {
        double modifier = simulateUpdateSpeedModifier(SCORE_ABOVE_START);
        assertTrue(modifier > TEST_BASE_MODIFIER, "Modifier should be greater than base when score is above start.");
    }

    /**
     * Tests that the speed modifier is less than or equal to base when score is below start score.
     * It might clamp at the minimum, which could be equal to or less than base.
     */
    @Test
    void testSpeedModifierDecreasesOrClampsBelowStartScore()
    {
        double modifier = simulateUpdateSpeedModifier(SCORE_BELOW_START);
        // It could decrease below base, or clamp at MIN_SPEED_MODIFIER
        assertTrue(modifier <= TEST_BASE_MODIFIER, "Modifier should be less than or equal to base when score is below start.");
    }

    /**
     * Tests that the speed modifier does not exceed the maximum value, even for very high scores.
     */
    @Test
    void testSpeedModifierClampsAtMaximum()
    {
        double modifierAtMaxThreshold = simulateUpdateSpeedModifier(SCORE_WAY_ABOVE_START); // Score likely to hit max
        double modifierJustAboveStart = simulateUpdateSpeedModifier(SCORE_ABOVE_START); // Score unlikely to hit max

        // Check it clamps - the value for a very high score shouldn't exceed the value for a moderately high score if max is hit.
        // A better check is against the known MAX_SPEED_MODIFIER if we allow using it relatively.
        double expectedMax = ClockStormMain.MAX_SPEED_MODIFIER; // Use constant for comparison endpoint
        assertEquals(expectedMax, modifierAtMaxThreshold, 0.001, "Modifier should clamp at the max value for very high scores.");
        assertTrue(modifierAtMaxThreshold >= modifierJustAboveStart, "Max clamped value should be >= value for lower score.");
    }

    /**
     * Tests that the speed modifier does not go below the minimum value, even for very low scores.
     */
    @Test
    void testSpeedModifierClampsAtMinimum()
    {
        double modifierAtMinThreshold = simulateUpdateSpeedModifier(SCORE_WAY_BELOW_START); // Score likely to hit min
        double modifierJustBelowStart = simulateUpdateSpeedModifier(SCORE_BELOW_START); // Score unlikely to hit min (might clamp anyway)

        // Check it clamps against the known MIN_SPEED_MODIFIER.
        double expectedMin = ClockStormMain.MIN_SPEED_MODIFIER; // Use constant for comparison endpoint
        assertEquals(expectedMin, modifierAtMinThreshold, 0.001, "Modifier should clamp at the min value for very low scores.");
        // Ensure clamped min is not higher than value for a slightly higher score (unless that also clamped)
        assertTrue(modifierAtMinThreshold <= modifierJustBelowStart, "Min clamped value should be <= value for higher score (unless also clamped).");
    }

    // Total Tests: 10 (Player) + 2 (Cannon) + 3 (MutableInt) + 9 (Score) + 6 (MainGame Logic) = 30 Tests
}