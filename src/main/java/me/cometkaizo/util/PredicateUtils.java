package me.cometkaizo.util;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Predicate;

public final class PredicateUtils {

    public static Predicate<Object> notNull() {
        return Objects::nonNull;
    }

    public static Predicate<Object> isNull() {
        return Objects::isNull;
    }

    public static Predicate<Object> isInstanceOf(Class<?> type) {
        return o -> o != null && type.isAssignableFrom(o.getClass());
    }

    public static Predicate<Object> notInstanceOf(Class<?> type) {
        return o -> o == null || !type.isAssignableFrom(o.getClass());
    }

    public static Predicate<Class<?>> isSubClassOf(Class<?> type) {
        return type::isAssignableFrom;
    }

    public static Predicate<Class<?>> notSubClassOf(Class<?> type) {
        return Predicate.not(isSubClassOf(type));
    }

    public static Predicate<Object> isEqualTo(Object ref) {
        return ref::equals;
    }

    public static Predicate<Object> notEqualTo(Object ref) {
        return Predicate.not(isEqualTo(ref));
    }

    public static Predicate<String> isEqualIgnoreCase(String ref) {
        return ref::equalsIgnoreCase;
    }

    public static Predicate<String> notEqualIgnoreCase(String ref) {
        return Predicate.not(isEqualIgnoreCase(ref));
    }

    public static Predicate<Object> isIdenticalTo(Object ref) {
        return o -> o == ref;
    }

    public static Predicate<Object> notIdenticalTo(Object ref) {
        return o -> o != ref;
    }

    public static <T> Predicate<Comparable<T>> isLessThan(T pivot) {
        return candidate -> candidate.compareTo(pivot) < 0;
    }

    public static <T> Predicate<Comparable<T>> isGreaterThan(T pivot) {
        return candidate -> candidate.compareTo(pivot) > 0;
    }

    public static Predicate<String> isEmpty() {
        return String::isEmpty;
    }

    public static Predicate<String> notEmpty() {
        return Predicate.not(String::isEmpty);
    }

    public static Predicate<String> isBlank() {
        return String::isBlank;
    }

    public static Predicate<String> notBlank() {
        return Predicate.not(String::isBlank);
    }

    public static Predicate<Optional<?>> isPresent() {
        return Optional::isPresent;
    }

    public static Predicate<Optional<?>> notPresent() {
        return Predicate.not(Optional::isPresent);
    }

    private PredicateUtils() {
        throw new AssertionError("No PredicateUtils instances for you!");
    }
}
