package ca.bcit.termproject.customgame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.util.BitSet;

public class Player extends Rectangle
{
    private boolean movingLeft = false;
    private boolean movingRight = false;
    private double speed = 3;

    public Player(double x, double y, double size)
    {
        super(x, y, size, size);
        setFill(Color.BLACK);
    }

    public void update()
    {
        if (movingLeft && getX() > 0)
        {
            setX(getX() - speed);
        }
        if (movingRight && getX() < MainGame.WINDOW_WIDTH - getWidth())
        {
            setX(getX() + speed);
        }
    }

    public void setLeft(boolean movingLeft)
    {
        this.movingLeft = movingLeft;
    }

    public void setRight(boolean movingRight)
    {
        this.movingRight = movingRight;
    }

}