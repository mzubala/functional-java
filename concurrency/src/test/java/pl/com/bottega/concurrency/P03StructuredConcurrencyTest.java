package pl.com.bottega.concurrency;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class P03StructuredConcurrencyTest {

    private final P03StructuredConcurrency sut = new P03StructuredConcurrency();

    @Test
    void testHappyFlow() {
        sut.happyFlow();
    }

    @Test
    void failWhenOneTaskFails() {
        assertThatThrownBy(sut::whatHappensWhenOneTaskFails)
                .isInstanceOf(IllegalStateException.class);
    }

    @Test
    void finishAsSoonAsFirstTaskCompletesSleep() {
        sut.finishingAfterFirstSuccessfulTaskWithJustSleeping();
    }

    @Test
    void finishAsSoonAsFirstTaskCompletesHttp() {
        sut.finishingAfterFirstSuccessfulTaskWithHttp();
    }

    @Test
    void finishAsSoonAsFirstTaskCompletesInfiniteLoop() {
        sut.finishingAfterFirstSuccessfulTaskWithInfiniteLoop();
    }

}
