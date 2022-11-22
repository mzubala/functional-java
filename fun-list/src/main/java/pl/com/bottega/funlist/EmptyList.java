package pl.com.bottega.funlist;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;

class EmptyList<T> implements FunList<T> {

    private static class Holder {
        static final EmptyList EMPTY_LIST = new EmptyList();
    }

    private EmptyList() {}

    static <T> EmptyList<T> getInstance() {
        return Holder.EMPTY_LIST;
    }

    @Override
    public FunList<T> append(T element) {
        return new NonEmptyList(element);
    }

    @Override
    public FunList<T> prepend(T element) {
        return append(element);
    }

    @Override
    public FunList<T> concat(FunList<T> other) {
        return other;
    }

    @Override
    public FunList<T> reverse() {
        return getInstance();
    }

    @Override
    public FunList<T> remove(T element) {
        return getInstance();
    }

    @Override
    public Optional<T> find(Predicate<T> predicate) {
        return Optional.empty();
    }

    @Override
    public Integer size() {
        return 0;
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
        return getInstance();
    }

    @Override
    public <S> FunList<S> map(Function<T, S> mapper) {
        return getInstance();
    }

    @Override
    public <S> FunList<S> flatMap(Function<T, FunList<S>> mapper) {
        return getInstance();
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
}
