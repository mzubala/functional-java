package pl.com.bottega.funlist;

import java.util.Arrays;
import java.util.Optional;
import java.util.function.*;

sealed public interface FunList<T> {

    static <T> FunList<T> of(T... elements) {
        return Arrays.stream(elements).reduce(FunList.empty(), FunList::append, FunList::concat);
    }

    static <T> FunList<T> empty() {
        return EmptyList.getInstance();
    }

    FunList<T> append(T element);

    FunList<T> prepend(T element);

    FunList<T> concat(FunList<T> other);

    FunList<T> reverse();

    FunList<T> remove(T element);

    Optional<T> find(Predicate<T> predicate);

    Integer size();

    Optional<T> get(Integer index);

    FunList<T> filter(Predicate<T> predicate);

    Optional<T> first();

    Optional<T> last();

    void foreach(Consumer<T> consumer);

    FunList<T> slice(Integer start, Integer end);

    <S> FunList<S> map(Function<T, S> mapper);

    <S> FunList<S> flatMap(Function<T, FunList<S>> mapper);

    <S> S foldLeft(S initial, BiFunction<S, T, S> op);

    <S> S foldRight(S initial, BiFunction<S, T, S> op);

    Optional<T> foldLeft(BinaryOperator<T> op);

    Optional<T> foldRight(BinaryOperator<T> op);
}

final class EmptyList<T> implements FunList<T> {

    private static class SingletonHolder {
        private static final EmptyList INSTANCE = new EmptyList();
    }

    private EmptyList() {
    }

    public static <T> FunList<T> getInstance() {
        return SingletonHolder.INSTANCE;
    }

    @Override
    public FunList<T> append(T element) {
        return new NonEmptyList<>(element);
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
        return this;
    }

    @Override
    public FunList<T> remove(T element) {
        return this;
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
        return this;
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
        return this;
    }

    @Override
    public <S> FunList<S> map(Function<T, S> mapper) {
        return (FunList<S>) this;
    }

    @Override
    public <S> FunList<S> flatMap(Function<T, FunList<S>> mapper) {
        return (FunList<S>) this;
    }

    @Override
    public <S> S foldLeft(S initial, BiFunction<S, T, S> op) {
        return initial;
    }

    @Override
    public <S> S foldRight(S initial, BiFunction<S, T, S> op) {
        return initial;
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

record NonEmptyList<T>(T head, FunList<T> tail) implements FunList<T> {

    NonEmptyList(T element) {
        this(element, EmptyList.getInstance());
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
        return switch (other) {
            case EmptyList<?> ignored -> this;
            case NonEmptyList<?> ignored -> new NonEmptyList<>(head, tail.concat(other));
        };
    }

    @Override
    public boolean equals(Object obj) {
        return switch (obj) {
            case NonEmptyList(var otherHead, var otherTail) -> head.equals(otherHead) && tail.equals(otherTail);
            default -> false;
        };
    }

    @Override
    public FunList<T> reverse() {
        return tail.reverse().append(head);
    }

    @Override
    public FunList<T> remove(T element) {
        if (element.equals(head)) {
            return tail;
        } else {
            return new NonEmptyList<>(head, tail.remove(element));
        }
    }

    @Override
    public Optional<T> find(Predicate<T> predicate) {
        if (predicate.test(head)) {
            return Optional.of(head);
        } else {
            return tail.find(predicate);
        }
    }

    @Override
    public Integer size() {
        return 1 + tail.size();
    }

    @Override
    public Optional<T> get(Integer index) {
        if (index < 0) {
            return Optional.empty();
        } else if (index == 0) {
            return Optional.of(head);
        } else {
            return tail.get(index - 1);
        }
    }

    @Override
    public FunList<T> filter(Predicate<T> predicate) {
        if (predicate.test(head)) {
            return new NonEmptyList<>(head, tail.filter(predicate));
        } else {
            return tail.filter(predicate);
        }
    }

    @Override
    public Optional<T> first() {
        return Optional.of(head);
    }

    @Override
    public Optional<T> last() {
        if (tail == EmptyList.getInstance()) {
            return Optional.of(head);
        } else {
            return tail.last();
        }
    }

    @Override
    public void foreach(Consumer<T> consumer) {
        consumer.accept(head);
        tail.foreach(consumer);
    }

    @Override
    public FunList<T> slice(Integer start, Integer end) {
        var size = size();
        if (start > end || (start < 0 && end < 0) || (start >= size && end >= size)) {
            return EmptyList.getInstance();
        }
        var startNormalized = Math.min(Math.max(start, 0), size - 1);
        var endNormalized = Math.min(Math.max(end, 0), size - 1);
        return sliceRec(startNormalized, endNormalized);


    }

    private FunList<T> sliceRec(Integer start, Integer end) {
        if (end == 0) {
            return new NonEmptyList<T>(head);
        } else if (start == 0) {
            var tail = (NonEmptyList<T>) this.tail;
            return new NonEmptyList<T>(head, tail.sliceRec(0, end - 1));
        } else {
            var tail = (NonEmptyList<T>) this.tail;
            return tail.sliceRec(start - 1, end - 1);
        }
    }

    @Override
    public <S> FunList<S> map(Function<T, S> mapper) {
        return new NonEmptyList<>(mapper.apply(head), tail.map(mapper));
    }

    @Override
    public <S> FunList<S> flatMap(Function<T, FunList<S>> mapper) {
        var headMapped = mapper.apply(head);
        var tailFlatMapped = tail.flatMap(mapper);
        return headMapped.concat(tailFlatMapped);
    }

    @Override
    public <S> S foldLeft(S initial, BiFunction<S, T, S> op) {
        return tail.foldLeft(op.apply(initial, head), op);
    }

    @Override
    public <S> S foldRight(S initial, BiFunction<S, T, S> op) {
        return op.apply(tail.foldRight(initial, op), head);
    }

    @Override
    public Optional<T> foldLeft(BinaryOperator<T> op) {
        if(tail == EmptyList.getInstance()) {
            return Optional.of(head);
        } else {
            return Optional.of(tail.foldLeft(head, op));
        }
    }

    @Override
    public Optional<T> foldRight(BinaryOperator<T> op) {
        return tail.foldRight(op).map((tailMapped) -> op.apply(tailMapped, head)).or(() -> Optional.of(head));
    }

    @Override
    public String toString() {
        return head.toString() + " " + tail.toString();
    }
}
