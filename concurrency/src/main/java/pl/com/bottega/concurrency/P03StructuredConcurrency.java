package pl.com.bottega.concurrency;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.datafaker.Faker;
import pl.com.bottega.concurrency.stackoverflow.*;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.ThreadFactory;
import java.util.stream.IntStream;

@Slf4j
class P03StructuredConcurrency {

    private final ThreadFactory factory = Thread.ofVirtual().name("Virtual", 1).factory();

    @SneakyThrows
    void happyFlow() {
        var client = new LoggingWrapper(new HttpStackOverflowClient());
        try (var scope = new StructuredTaskScope<>("Basic scope", factory)) {
            var javaHeadline = scope.fork(() -> headlineAbout("java", client));
            var kotlinHeadline = scope.fork(() -> headlineAbout("kotlin", client));
            var scalaHeadline = scope.fork(() -> headlineAbout("scala", client));
            scope.join();
            System.out.println(javaHeadline.get());
            System.out.println(kotlinHeadline.get());
            System.out.println(scalaHeadline.get());
        }
    }

    @SneakyThrows
    void whatHappensWhenOneTaskFails() {
        var loggingClient = new LoggingWrapper(new HttpStackOverflowClient());
        var errorThrowingClient = new InjectErrorsWrapper(loggingClient, "scala");
        try (var scope = new StructuredTaskScope<>("Failing scope", factory)) {
            var scalaHeadline = scope.fork(() -> headlineAbout("scala", errorThrowingClient));
            var javaHeadline = scope.fork(() -> headlineAbout("java", errorThrowingClient));
            var kotlinHeadline = scope.fork(() -> headlineAbout("kotlin", errorThrowingClient));
            scope.join();
            System.out.println(javaHeadline.get());
            System.out.println(kotlinHeadline.get());
            System.out.println(scalaHeadline.get());
        }
    }

    @SneakyThrows
    void finishingAfterFirstSuccessfulTaskWithJustSleeping() {
        var faker = new Faker();
        String result;
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess()) {
            IntStream.range(0, 50000).mapToObj(i -> scope.fork(() -> {
                var sleepTime = faker.random().nextInt(1000);
                log.info(STR."Starting task \{i} with sleep time \{sleepTime}");
                Thread.sleep(sleepTime);
                log.info("Ending task " + i);
                return STR."Sleep time \{sleepTime}";
            })).toList();
            scope.join();
            result = scope.result().toString();
        }
        System.out.println(result);
    }

    @SneakyThrows
    void finishingAfterFirstSuccessfulTaskWithHttp() {
        var faker = new Faker();
        var loggingClient = new LoggingWrapper(new HttpStackOverflowClient());
        var slowClient = new FallbackStubClient(new ArtificialSleepWrapper(loggingClient));
        String result;
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess()) {
            IntStream.range(0, 50000).mapToObj(i -> scope.fork(() -> {
                log.info(STR."Starting task \{i}");
                var toRet = headlineAbout(faker.animal().name(), slowClient);
                log.info("Ending task " + i);
                return STR."\{i} \{toRet}";
            })).toList();
            scope.join();
            result = scope.result().toString();
        }
        System.out.println(result);
    }

    @SneakyThrows
    void finishingAfterFirstSuccessfulTaskWithInfiniteLoop() {
        String result;
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess()) {
            for(int i = 0; i<5; i++) {
                scope.fork(() -> {
                    /*while(true) {
                        System.out.println("I'm doing it");
                    }*/
                    for(int j = 0; j<100000; j++) {
                        System.out.println("Reading");
                        try(var lines = Files.lines(Path.of(getClass().getResource("/dictionary.txt").toURI()))) {
                            lines.toList();
                        }
                        System.out.println("Reading finished");
                    }
                    return "All done";
                });
            }
            scope.fork(() -> {
               Thread.sleep(100);
               return "Done!";
            });
            scope.join();
            result = scope.result().toString();
        }
        System.out.println(result);
    }

    private String headlineAbout(String word, StackOverflowClient stackOverflowClient) {
        return STR. "Most recent question about \{ word }: \{ stackOverflowClient.mostRecentQuestionAbout(word) }" ;
    }

}
