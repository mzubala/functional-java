package pl.com.bottega.functional.basics.lazy;

import org.junit.jupiter.api.Test;

import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Supplier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class LazyTest {

    private final String value;

    public LazyTest() {
        value = "1";
    }

    @Test
    void doesNotCallExpressionUponCreation() {
        var expression = mock(Supplier.class);

        var lazy = Lazy.of(expression);

        verifyNoInteractions(expression);
    }

    @Test
    void getsValueFromSupplier() {
        Supplier<String> supplier = () -> value;
        var lazy = Lazy.of(supplier);

        var result = lazy.get();

        assertThat(result).isEqualTo(value);
    }

    @Test
    void callsSupplierJustOnce() {
        var supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(value);
        var lazy = Lazy.of(supplier);

        lazy.get();
        lazy.get();

        verify(supplier, times(1)).get();
    }

    @Test
    void valueIsMemoized() {
        Supplier<String> supplier = () -> value;
        var lazy = Lazy.of(supplier);

        var result1 = lazy.get();
        var result2 = lazy.get();

        assertThat(result1).isEqualTo(value);
        assertThat(result2).isEqualTo(value);
    }

    @Test
    void gettingValueIsThreadSafe() throws InterruptedException {
        var supplier = mock(Supplier.class);
        var lazy = Lazy.of(supplier);
        when(supplier.get()).thenAnswer((invocation) -> {
            Thread.sleep(100);
            return value;
        });
        Runnable getter = () -> assertThat(lazy.get()).isEqualTo(value);

        var executor = Executors.newScheduledThreadPool(5);
        for(int i = 0; i<20; i++) {
            executor.execute(getter);
        }
        executor.shutdown();
        executor.awaitTermination(5, TimeUnit.SECONDS);

        verify(supplier, times(1)).get();
    }

    @Test
    void mapsValue() {
        Lazy<String> lazyString = Lazy.of(() -> value);

        Lazy<Integer> lazyInteger = lazyString.map(Integer::valueOf);

        assertThat(lazyInteger.get()).isEqualTo(1);
    }

    @Test
    void doesNotCallMappedExpressionsUntilGet() {
        var expression = mock(Supplier.class);
        var mapper = mock(Function.class);
        Lazy lazy = Lazy.of(expression);


        Lazy mappedLazy = lazy.map(mapper);

        verifyNoInteractions(expression);
        verifyNoInteractions(mapper);
    }

    @Test
    void callsMapperAndMappedExpressionJustOnce() {
        var expression = mock(Supplier.class);
        when(expression.get()).thenReturn(value);
        var mapper = mock(Function.class);
        when(mapper.apply(any())).thenReturn(value);
        Lazy lazy = Lazy.of(expression);
        Lazy mappedLazy = lazy.map(mapper);

        mappedLazy.get();
        mappedLazy.get();

        verify(expression, times(1)).get();
        verify(mapper, times(1)).apply(value);
    }

    @Test
    void flatMapsValue() {
        Lazy<String> lazyString = Lazy.of(() -> value);

        Lazy<Integer> lazyInteger = lazyString.flatMap((string) -> Lazy.of(() -> Integer.valueOf(string)));

        assertThat(lazyInteger.get()).isEqualTo(1);
    }

    @Test
    void doesNotCallFlatMappedExpressionsUntilGet() {
        var expression = mock(Supplier.class);
        var mapper = mock(Function.class);
        Lazy lazy = Lazy.of(expression);


        Lazy mappedLazy = lazy.flatMap(mapper);

        verifyNoInteractions(expression);
        verifyNoInteractions(mapper);
    }

    @Test
    void callsMapperAndFlatMappedExpressionJustOnce() {
        var expression = mock(Supplier.class);
        when(expression.get()).thenReturn(value);
        var mapper = mock(Function.class);
        when(mapper.apply(any())).thenReturn(Lazy.of(() -> value));
        Lazy lazy = Lazy.of(expression);
        Lazy mappedLazy = lazy.flatMap(mapper);

        mappedLazy.get();
        mappedLazy.get();

        verify(mapper, times(1)).apply(value);
        verify(expression, times(1)).get();
    }

    @Test
    void filtersValue() {
        var lazy = Lazy.of(() -> value);

        Optional<String> o1 = lazy.filter((s) -> s.length() == value.length());
        Optional<String> o2 = lazy.filter((s) -> false);

        assertThat(o1).isEqualTo(Optional.of(value));
        assertThat(o2).isEqualTo(Optional.empty());
    }

    @Test
    void callsSupplierJustOnceWhenFiltering() {
        var supplier = mock(Supplier.class);
        when(supplier.get()).thenReturn(value);
        var lazy = Lazy.of(supplier);

        lazy.filter((s) -> true);
        lazy.filter((s) -> false);

        verify(supplier, times(1)).get();
    }
}
