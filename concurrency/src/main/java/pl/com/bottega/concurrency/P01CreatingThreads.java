package pl.com.bottega.concurrency;

import lombok.SneakyThrows;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

class P01CreatingThreads {
    Thread createPlatofrmThread(Runnable task) {
        return Thread.ofPlatform().name("Platform").start(task);
    }

    public Thread createVirtualThread(Runnable task) {
        return Thread.ofVirtual().name("Virtual").start(task);
    }

    private ExecutorService platformExecutor = Executors.newFixedThreadPool(10);

    public void submitTaskToPlatformThreadPool(Runnable task) {
        platformExecutor.execute(task);
    }

    @SneakyThrows
    public void waitForPlatformTasksToComplete() {
        platformExecutor.close();
        platformExecutor.awaitTermination(1000, TimeUnit.MILLISECONDS);
    }

    private ExecutorService virtualExecutor = Executors.newThreadPerTaskExecutor(Thread.ofVirtual().name("Virtual-", 1).factory());

    public void submitTaskToVirtualThreadPool(Runnable task) {
        virtualExecutor.submit(task);
    }

    @SneakyThrows
    public void waitForVirtualTasksToComplete() {
        virtualExecutor.close();
        virtualExecutor.awaitTermination(10000, TimeUnit.MILLISECONDS);
    }

}
