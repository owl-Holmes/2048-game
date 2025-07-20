package main.java.game2048.util;

/**
 * Utility class providing argument validation methods to enforce preconditions.
 * <p>
 * This class is not intended to be instantiated or extended.
 *
 *
 * @author owl
 */
public final class Preconditions {

    /**
     * Private constructor to prevent instantiation
     */
    private Preconditions(){};

    /**
     * Validates a boolean condition typically used to check method arguments
     * <p>
     * Throws an {@link IllegalArgumentException} if the condition is false
     *
     * @param shouldBeTrue the condition that must evaluate to {@code true}
     * @throws IllegalArgumentException if {@code shouldBeTrue} is {@code false}
     */
    public static void checkArgument(boolean shouldBeTrue) throws IllegalArgumentException {
        if(!shouldBeTrue) throw new IllegalArgumentException();
    }
}