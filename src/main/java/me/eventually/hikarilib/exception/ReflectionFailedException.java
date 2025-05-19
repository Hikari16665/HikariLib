package me.eventually.hikarilib.exception;

/**
 * An exception that is thrown when a reflection operation fails.
 * <p>
 * Use getCause() to get the Original Exception
 */
public class ReflectionFailedException extends RuntimeException {
    public ReflectionFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
