package pl.com.bottega.functional.basics.lazy;

import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lazy<T> {

    private volatile Supplier<? extends T> supplier;
    private T value;

    public static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return new Lazy<>(supplier);
    }

    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
    }

    public T get() {
        return supplier != null ? computeValue() : value;
    }

    private synchronized T computeValue() {
       if(supplier != null) {
            value = supplier.get();
            supplier = null;
        }
        return value;
    }

    public <S> Lazy<S> map(Function<T, S> mapper) {
        return new Lazy<>(() -> mapper.apply(get()));
    }

    public <S> Lazy<S> flatMap(Function<T, Lazy<S>> mapper) {
        return null;
    }

    public Optional<T> filter(Predicate<T> tester) {
        return Optional.empty();
    }
}
