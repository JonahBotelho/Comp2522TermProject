package ca.bcit.termproject.customgame;

/**
 * Class designed to hold an integer value, allowing for it to be changed without
 * the object being reassigned.
 */
public class MutableInteger
{
    private int value;

    /**
     * Constructs a MutableInteger object
     * @param value
     */
    public MutableInteger(int value)
    {
        this.value = value;
    }

    /**
     * Sets the value stored in the object
     * @param value to be stored
     */
    public void setValue(final int value)
    {
        this.value = value;
    }

    /**
     * Returns the value stored in the object.
     * @return int value stored
     */
    public int getValue()
    {
        return value;
    }
}
