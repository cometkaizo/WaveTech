package me.cometkaizo.util;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

public class PeekingIterator<T> implements Iterator<T> {

    private final List<T> list;
    private int index = -1;



    private PeekingIterator(List<T> list) {
        this.list = list;
    }

    public static <E> PeekingIterator<E> of(List<E> list) {
        return new PeekingIterator<>(list);
    }


    public List<T> toList() {
        return new ArrayList<>(list);
    }
    public List<T> toListOfRemaining() {
        return list.subList(cursor() + 1, list.size());
    }

    @NotNull
    public T peek() {
        return peek(1);
    }

    @NotNull
    public T peek(int amt) {
        if (!hasNext( amt - 1)) throw new NoSuchElementException("No element to peek at index " + index + " + amount " + amt);
        return list.get(index + amt);
    }

    @Override
    @NotNull
    public T next() {
        advance();
        return current();
    }
    @NotNull
    public T current() {
        return list.get(index);
    }

    public int cursor() {
        return index;
    }

    @Override
    public boolean hasNext() {
        return index < list.size() - 1;
    }
    public boolean hasNext(int amt) {
        return index + amt < list.size() - 1;
    }


    public void advance() {
        if (!hasNext()) throw new NoSuchElementException("No element to advance");
        index ++;
    }

    public void advance(int amt) {
        if (!hasNext(amt - 1)) throw new NoSuchElementException("No element at index " + index + " + amount " + amt);
        index += amt;
    }

}
