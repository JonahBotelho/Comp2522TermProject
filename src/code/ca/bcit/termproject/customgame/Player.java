package ca.bcit.termproject.customgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * Represents the player in the game, which is a rectangular shape.
 * The player can move in four directions: left, right, up, and down.
 */
public class Player extends Rectangle
{
    private static final int NOTHING = 0;  // Boundary check constant
    private static final double speed = 3;  // Movement speed of the player

    private boolean movingLeft = false;
    private boolean movingRight = false;
    private boolean movingUp = false;
    private boolean movingDown = false;



    /**
     * Constructs a Player object at the specified position with the given size.
     *
     * @param x    The x-coordinate of the player.
     * @param y    The y-coordinate of the player.
     * @param size The width and height of the player.
     */
    public Player(final double x, final double y, final double size)
    {
        super(x, y, size, size);
        setFill(Color.BLACK);
    }

    /**
     * Updates the player's position based on the movement flags.
     * Ensures the player stays within the window boundaries.
     */
    public void update()
    {
        if (movingLeft && getX() > NOTHING)
        {
            setX(getX() - speed);
        }
        if (movingRight && getX() < MainGame.WINDOW_WIDTH - getWidth())
        {
            setX(getX() + speed);
        }
        if (movingUp && getY() > NOTHING)
        {
            setY(getY() - speed);
        }
        if (movingDown && getY() < MainGame.WINDOW_HEIGHT - getHeight())
        {
            setY(getY() + speed);
        }
    }

    /**
     * Sets the movement flag for moving left.
     *
     * @param movingLeft {@code true} to move left, {@code false} otherwise.
     */
    public void setLeft(final boolean movingLeft)
    {
        this.movingLeft = movingLeft;
    }

    /**
     * Sets the movement flag for moving right.
     *
     * @param movingRight {@code true} to move right, {@code false} otherwise.
     */
    public void setRight(final boolean movingRight)
    {
        this.movingRight = movingRight;
    }

    /**
     * Sets the movement flag for moving up.
     *
     * @param movingUp {@code true} to move up, {@code false} otherwise.
     */
    public void setUp(final boolean movingUp)
    {
        this.movingUp = movingUp;
    }

    /**
     * Sets the movement flag for moving down.
     *
     * @param movingDown {@code true} to move down, {@code false} otherwise.
     */
    public void setDown(final boolean movingDown)
    {
        this.movingDown = movingDown;
    }
}
