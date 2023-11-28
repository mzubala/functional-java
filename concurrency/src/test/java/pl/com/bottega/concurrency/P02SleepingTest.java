package pl.com.bottega.concurrency;

import org.junit.jupiter.api.Test;

class P02SleepingTest {

    private final P02Sleeping sut = new P02Sleeping();

    @Test
    void testPlatform() {
        sut.checkSleepingWithPlatformThreads();
    }

    @Test
    void testVirtual() {
        sut.checkSleepingWithVirtualThreads();
    }

}
