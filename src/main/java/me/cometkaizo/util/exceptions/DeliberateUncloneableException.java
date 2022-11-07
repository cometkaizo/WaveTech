package me.cometkaizo.util.exceptions;

/**
 * Used to indicate that a class acknowledges that it cannot clone even though it
 * overrides the clone method and that it is intentional.
 * Usually is thrown in constant objects that cannot have copies
 */
public class DeliberateUncloneableException extends RuntimeException {
}
