package me.cometkaizo.wavetech.parser.syntaxparseres;

import java.util.List;
import java.util.Objects;

public class AnyMatcher {

    private final List<Object> validObjects;
    private final boolean includesNone;

    public AnyMatcher(boolean includeNone, Object... validObjects) {
        Objects.requireNonNull(validObjects, "Valid objects cannot be null");
        this.validObjects = List.of(validObjects);
        this.includesNone = includeNone;
    }

    public boolean matches(Object candidate) {
        return validObjects.contains(candidate);
    }

    public boolean includesNone() {
        return includesNone;
    }
}
