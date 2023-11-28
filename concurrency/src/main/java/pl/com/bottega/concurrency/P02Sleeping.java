package pl.com.bottega.concurrency;

import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

@Slf4j
class P02Sleeping {

    @SneakyThrows
    void checkSleepingWithVirtualThreads() {
        var executor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Virtual-", 1).factory());
        for(int i = 0; i<100000; i++) {
            var iCopy = i;
            executor.submit(() -> this.goToSleep(iCopy));
        }
        executor.close();
        executor.awaitTermination(1100, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    void checkSleepingWithPlatformThreads() {
        var executor = Executors.newThreadPerTaskExecutor(Thread.ofPlatform().name("Platform-", 1).factory());
        for(int i = 0; i<100000; i++) {
            var iCopy = i;
            executor.submit(() -> this.goToSleep(iCopy));
        }
        executor.close();
        executor.awaitTermination(1100, TimeUnit.MILLISECONDS);
    }

    @SneakyThrows
    private void goToSleep(Integer i) {
        log.info(STR."Before going to sleep \{i}");
        Thread.sleep(1000*12);
        log.info(STR."After wake up \{i}");
    }

}
