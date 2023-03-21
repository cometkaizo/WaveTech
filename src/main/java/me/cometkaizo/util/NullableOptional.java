package me.cometkaizo.util;

import org.jetbrains.annotations.Nullable;

import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class NullableOptional<T> {
    private static final NullableOptional<?> EMPTY = new NullableOptional<>();
    private static final NullableOptional<?> NULL = new NullableOptional<>(null);

    private final boolean isPresent;
    @Nullable
    private final T value;

    private NullableOptional() {
        isPresent = false;
        value = null;
    }

    private NullableOptional(@Nullable T value) {
        this.isPresent = true;
        this.value = value;
    }

    @SuppressWarnings("unchecked")
    public static <T> NullableOptional<T> empty() {
        return (NullableOptional<T>) EMPTY;
    }

    @SuppressWarnings("unchecked")
    public static <T> NullableOptional<T> nullValue() {
        return (NullableOptional<T>) NULL;
    }

    public static <T> NullableOptional<T> of(T value) {
        return value == null ? nullValue()
                             : new NullableOptional<>(value);
    }


    public boolean isPresent() {
        return isPresent;
    }

    public boolean isEmpty() {
        return !isPresent;
    }

    public boolean isNull() {
        return isPresent && value == null;
    }

    public boolean isNonNull() {
        return isPresent && value != null;
    }

    public void ifPresent(Consumer<? super T> action) {
        if (isPresent) action.accept(value);
    }

    public void ifNonNull(Consumer<? super T> action) {
        if (isNonNull()) action.accept(value);
    }

    public void ifPresentOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (isPresent) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public void ifNonNullOrElse(Consumer<? super T> action, Runnable emptyAction) {
        if (isNonNull()) {
            action.accept(value);
        } else {
            emptyAction.run();
        }
    }

    public NullableOptional<T> filter(Predicate<? super T> predicate) {
        Objects.requireNonNull(predicate);
        if (!isPresent) {
            return this;
        } else {
            return predicate.test(value) ? this : empty();
        }
    }

    public <U> NullableOptional<U> map(Function<? super T, ? extends U> mapper) {
        Objects.requireNonNull(mapper);
        if (!isPresent) {
            return empty();
        } else {
            return of(mapper.apply(value));
        }
    }

    public Optional<T> toOptional() {
        return Optional.ofNullable(value);
    }

    public NullableOptional<T> or(Supplier<? extends NullableOptional<? extends T>> supplier) {
        Objects.requireNonNull(supplier);
        if (isPresent()) {
            return this;
        } else {
            @SuppressWarnings("unchecked")
            NullableOptional<T> r = (NullableOptional<T>) supplier.get();
            return Objects.requireNonNull(r);
        }
    }

    public Stream<T> stream() {
        if (!isPresent) {
            return Stream.empty();
        } else {
            return Stream.of(value);
        }
    }

    public T orElse(T other) {
        return value != null ? value : other;
    }

    public T orElseGet(Supplier<? extends T> supplier) {
        return value != null ? value : supplier.get();
    }

    public T orElseThrow() {
        if (!isPresent) {
            throw new NoSuchElementException("No value present");
        }
        return value;
    }

    public <X extends Throwable> T orElseThrow(Supplier<? extends X> exceptionSupplier) throws X {
        if (value != null) {
            return value;
        } else {
            throw exceptionSupplier.get();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NullableOptional<?> that = (NullableOptional<?>) o;
        return isPresent == that.isPresent && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(isPresent, value);
    }

    @Override
    public String toString() {
        return isPresent
                ? ("NullableOptional[" + value + "]")
                : "NullableOptional.empty";
    }

}
