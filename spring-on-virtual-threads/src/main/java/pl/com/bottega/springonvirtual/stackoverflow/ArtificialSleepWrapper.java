package pl.com.bottega.springonvirtual.stackoverflow;

import lombok.SneakyThrows;

import java.util.Random;

public class ArtificialSleepWrapper implements StackOverflowClient {

    private static final Random RANDOM = new Random();

    private final StackOverflowClient target;

    public ArtificialSleepWrapper(StackOverflowClient target) {
        this.target = target;
    }

    @Override
    public String mostRecentQuestionAbout(String tag) {
        final long start = System.currentTimeMillis();
        final String result = target.mostRecentQuestionAbout(tag);
        artificialSleep(1000 - (System.currentTimeMillis() - start));
        return result;
    }
    @SneakyThrows
    protected static void artificialSleep(long expected) {
        Thread.sleep((long) (expected + RANDOM.nextGaussian() * expected / 2));
    }

}
