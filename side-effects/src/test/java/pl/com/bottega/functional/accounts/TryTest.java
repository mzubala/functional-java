package pl.com.bottega.functional.accounts;

import io.vavr.control.Try;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class TryTest {

    Try<String> stringSuccess = Try.success("40");
    Try<Long> failedLong = Try.failure(new IllegalArgumentException("error"));
    Try<Long> longSuccess = Try.of(() -> 50L);
    Try<String> failedString = Try.of(() -> {
        throw new RuntimeException("error");
    });

    @Test
    void testCreate() throws Exception {
        // expect
        assertThat(stringSuccess.get()).isEqualTo("40");
        assertThat(longSuccess.get()).isEqualTo(50L);
        assertThatThrownBy(() -> failedLong.get()).isInstanceOf(IllegalArgumentException.class).hasMessage("error");
        assertThatThrownBy(() -> failedString.get()).isInstanceOf(RuntimeException.class).hasMessage("error");
        assertThat(stringSuccess.isSuccess()).isTrue();
        assertThat(failedString.isFailure()).isTrue();
    }

    @Test
    void testTransformations() {
        // when
        Try<Integer> integerSuccess = stringSuccess.map(Integer::valueOf);
        Try<Integer> integerFailed = failedString.map(Integer::valueOf);

        // then
        assertThat(integerSuccess.get()).isEqualTo(40);
        assertThatThrownBy(() -> integerFailed.get()).isInstanceOf(RuntimeException.class).hasMessage("error");

        // when
        var integerSuccess2 = stringSuccess.flatMap((s) -> Try.of(() -> Integer.valueOf(s)));
        var integerFailed2 = failedString.flatMap((s) -> Try.of(() -> Integer.valueOf(s)));

        // then
        assertThat(integerSuccess2.get()).isEqualTo(40);
        assertThatThrownBy(() -> integerFailed2.get()).isInstanceOf(RuntimeException.class).hasMessage("error");
    }

    @Test
    void testAndThen() {
        // expect
        var successAndThen = stringSuccess.andThen((value) -> System.out.println(value));
        assertThatThrownBy(() -> failedString.andThen((value) -> System.out.println(value)).get())
            .isInstanceOf(RuntimeException.class).hasMessage("error");
    }

}
