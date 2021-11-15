package pl.com.bottega.funlist;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

public class NonEmptyList<T> implements FunList<T> {

    private final T head;
    private final FunList<T> tail;

    public NonEmptyList(T head) {
        this(head, EmptyList.INSTANCE);
    }

    public NonEmptyList(T head, FunList<T> tail) {
        this.head = head;
        this.tail = tail;
    }

    @Override
    public FunList<T> append(T element) {
        return new NonEmptyList<>(head, tail.append(element));
    }

    @Override
    public FunList<T> prepend(T element) {
        return new NonEmptyList<>(element, tail.prepend(head));
    }

    @Override
    public FunList<T> concat(FunList<T> other) {
        return null;
    }

    @Override
    public FunList<T> reverse() {
        return null;
    }

    @Override
    public FunList<T> remove(T element) {
        return null;
    }

    @Override
    public Optional<T> find(Predicate<T> predicate) {
        return Optional.empty();
    }

    @Override
    public Integer size() {
        return null;
    }

    @Override
    public Optional<T> get(Integer index) {
        return Optional.empty();
    }

    @Override
    public FunList<T> filter(Predicate<T> predicate) {
        return null;
    }

    @Override
    public Optional<T> first() {
        return Optional.empty();
    }

    @Override
    public Optional<T> last() {
        return Optional.empty();
    }

    @Override
    public void foreach(Consumer<T> consumer) {

    }

    @Override
    public FunList<T> slice(Integer start, Integer end) {
        return null;
    }

    @Override
    public <S> FunList<S> map(Function<T, S> mapper) {
        return null;
    }

    @Override
    public <S> FunList<S> flatMap(Function<T, FunList<S>> mapper) {
        return null;
    }

    @Override
    public <S> S foldLeft(S initial, BiFunction<S, T, S> op) {
        return null;
    }

    @Override
    public <S> S foldRight(S initial, BiFunction<S, T, S> op) {
        return null;
    }

    @Override
    public Optional<T> foldLeft(BinaryOperator<T> op) {
        return Optional.empty();
    }

    @Override
    public Optional<T> foldRight(BinaryOperator<T> op) {
        return Optional.empty();
    }

    @Override
    public boolean equals(Object obj) {
        if(!(obj instanceof NonEmptyList)) {
            return false;
        }
        var objAsList = (NonEmptyList) obj;
        return head.equals(objAsList.head) && tail.equals(objAsList.tail);
    }
}
