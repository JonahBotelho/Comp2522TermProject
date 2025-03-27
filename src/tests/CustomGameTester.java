import ca.bcit.termproject.customgame.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public final class CustomGameTester
{
    private MainGame game;
    private Player player;

    @BeforeEach
    void setUp()
    {
        game = new MainGame();
        player = new Player(MainGame.PLAYER_START_X, MainGame.PLAYER_START_Y, MainGame.PLAYER_SIZE);
    }

    // movement tests
    @Test
    void testMovingLeft()
    {
        player.setLeft(true);
        assertTrue(player.isMovingLeft());
    }

    @Test
    void testNotMovingLeft()
    {
        player.setLeft(false);
        assertFalse(player.isMovingLeft());
    }

    @Test
    void testMovingRight()
    {
        player.setRight(true);
        assertTrue(player.isMovingRight());
    }

    @Test
    void testNotMovingRight()
    {
        player.setRight(false);
        assertFalse(player.isMovingRight());
    }

    @Test
    void testMovingUp()
    {
        player.setUp(true);
        assertTrue(player.isMovingUp());
    }

    @Test
    void testNotMovingUp()
    {
        player.setUp(false);
        assertFalse(player.isMovingUp());
    }

    @Test
    void testMovingDown()
    {
        player.setDown(true);
        assertTrue(player.isMovingDown());
    }

    @Test
    void testNotMovingDown()
    {
        player.setDown(false);
        assertFalse(player.isMovingDown());
    }

}
