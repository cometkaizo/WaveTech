package me.cometkaizo.util;

import me.cometkaizo.logging.LogUtils;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;

public class PeekingIterator<T> implements Iterator<T> {

    @NotNull
    protected final T[] array;
    protected int index = 0;



    protected PeekingIterator(@NotNull T[] array) {
        Objects.requireNonNull(array, "Array cannot be null");
        this.array = array;
    }

    @SuppressWarnings("unchecked")
    public static <E> PeekingIterator<E> of(@NotNull List<E> list) {
        Objects.requireNonNull(list, "List cannot be null");
        return new PeekingIterator<>((E[]) list.toArray());
    }
    public static <E> PeekingIterator<E> of(@NotNull E[] array) {
        return new PeekingIterator<>(array);
    }
    public static <E> PeekingIterator<E> copyOf(PeekingIterator<E> peekingIterator) {
        var result = new PeekingIterator<>(Arrays.copyOf(peekingIterator.array, peekingIterator.array.length));
        result.index = peekingIterator.index;
        return result;
    }


    @Contract(" -> new")
    public List<T> toList() {
        return List.of(array);
    }

    @Contract(" -> new")
    public T[] toArray() {
        return Arrays.copyOf(array, array.length);
    }

    public List<T> getListRange(int start, int end) {
        return toList().subList(start, end);
    }
    public T[] getRange(int start, int end) {
        return Arrays.copyOfRange(array, start, end);
    }

    public List<T> getListRange(int start) {
        int min = Math.min(start, cursor());
        int max = Math.max(start, cursor());
        return getListRange(min, max);
    }
    public T[] getRange(int start) {
        int min = Math.min(start, cursor());
        int max = Math.max(start, cursor());
        return getRange(min, max);
    }


    /**
     * Returns an unmodifiable list of all remaining elements.
     * <p>
     * Does not include current element.
     * @return an unmodifiable list of all remaining elements
     */
    public List<T> remainingList() {
        return List.of(remainingArray());
    }

    /**
     * Returns an array of all remaining elements.
     * <p>
     * Does not include current element.
     * @return an array of all remaining elements
     */
    public T[] remainingArray() {
        return Arrays.copyOfRange(array, cursor() + 1, array.length);
    }

    public T peek() {
        return peek(1);
    }

    public T peek(int amt) {
        if (!hasElementAt(index + amt)) throw new NoSuchElementException("No element to peek at index " + index + " + amount " + amt);
        return array[index + amt];
    }

    @Override
    public T next() {
        T last = current();
        advance();
        return last;
    }

    public T previous() {
        back();
        return current();
    }

    public T current() {
        return array[index];
    }

    public T elementAt(int index) {
        return array[index];
    }

    public int cursor() {
        return index;
    }

    @Override
    public boolean hasNext() {
        return hasNext(1);
    }
    public boolean hasNext(int amt) {
        return isValidIndex(index + amt);
    }

    public boolean hasCurrent() {
        return hasElementAt(index);
    }
    public boolean hasPrevious() {
        return hasPrevious(1);
    }
    public boolean hasPrevious(int amt) {
        return hasNext(-amt);
    }

    public boolean hasElementAt(int index) {
        return index >= 0 && index < array.length;
    }

    public boolean isValidIndex(int index) {
        return index >= 0 && index <= array.length;
    }



    public void advance() {
        advance(1);
    }

    public void advance(int amt) {
        throwIfIllegalAdvance(amt);
        LogUtils.info("index changed from {} to {}", index, index + amt);
        index += amt;
    }

    protected void throwIfIllegalAdvance(int amt) {
        if (!isValidIndex(index + amt))
            throw new IllegalArgumentException("Invalid index " + index + " + amount " + amt);
    }

    protected void throwIfIllegalIndex(int index) {
        if (!isValidIndex(index))
            throw new IllegalArgumentException("Invalid index " + index);
    }


    public void back() {
        back(1);
    }

    public void back(int amt) {
        advance(-amt);
    }

    public void jumpTo(int index) {
        throwIfIllegalIndex(index);

        LogUtils.info("index jumped from {} to {}", this.index, index);
        this.index = index;
    }

    public void reset() {
        index = 0;
    }

    public int size() {
        return array.length;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PeekingIterator<?> that = (PeekingIterator<?>) o;
        return index == that.index && Arrays.equals(array, that.array);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(index);
        result = 31 * result + Arrays.hashCode(array);
        return result;
    }
}
