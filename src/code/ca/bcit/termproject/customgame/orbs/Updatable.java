package ca.bcit.termproject.customgame.orbs;

/**
 * Functional interface.
 * Describes a class that must define the update() void method.
 *
 * @author Jonah Botelho
 * @version 1.0
 */
@FunctionalInterface
public interface Updatable
{
    /**
     * Update void method that must be implemented.
     */
    void update();
}
