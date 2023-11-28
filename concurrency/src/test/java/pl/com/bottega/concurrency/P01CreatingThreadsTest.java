package pl.com.bottega.concurrency;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.assertj.core.api.Assertions.assertThat;

@Slf4j
class P01CreatingThreadsTest {

    private final P01CreatingThreads sut = new P01CreatingThreads();

    @Test
    void createsAPlatformThread() throws InterruptedException {
        Thread platofrmThread = sut.createPlatofrmThread(this::logThreadName);
        platofrmThread.join();
        assertThat(platofrmThread.isVirtual()).isFalse();
    }

    @Test
    void createsAVirtualThread() throws InterruptedException {
        Thread virtualThread = sut.createVirtualThread(this::logThreadName);
        virtualThread.join();
        assertThat(virtualThread.isVirtual()).isTrue();
    }

    @Test
    void executesTasksInAPlatformThreadPool() {
        var threadNames = new HashSet<String>();
        var taskCount = 10;
        var foundVirtual = new AtomicBoolean(false);
        for(int i = 0; i<taskCount; i++) {
            sut.submitTaskToPlatformThreadPool(() -> {
                logThreadName();
                threadNames.add(Thread.currentThread().getName());
                if(Thread.currentThread().isVirtual()) {
                    foundVirtual.set(true);
                }
            });
        }
        sut.waitForPlatformTasksToComplete();

        assertThat(foundVirtual).isFalse();
        assertThat(threadNames).hasSize(10);
    }

    @Test
    void executesTasksInAVirtualThreadPool() {
        var threadNames = new CopyOnWriteArraySet<>();
        var taskCount = 10000;
        var foundPlatform = new AtomicBoolean(false);
        for(int i = 0; i<taskCount; i++) {
            sut.submitTaskToVirtualThreadPool(() -> {
                logThreadName();
                threadNames.add(Thread.currentThread().getName());
                if(!Thread.currentThread().isVirtual()) {
                    foundPlatform.set(true);
                }
            });
        }
        sut.waitForVirtualTasksToComplete();

        assertThat(foundPlatform).isFalse();
        assertThat(threadNames).hasSize(taskCount);
    }

    private void logThreadName() {
        log.info("Hello");
    }

}
