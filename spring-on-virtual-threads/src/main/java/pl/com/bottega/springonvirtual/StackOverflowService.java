package pl.com.bottega.springonvirtual;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import pl.com.bottega.springonvirtual.stackoverflow.StackOverflowClient;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
class StackOverflowService {

    private final StackOverflowClient client;

    @Async
    CompletableFuture<String> getLatestHeadlineAbout(String tag) {
        var result = client.mostRecentQuestionAbout(tag);
        log.info("Am I run on a virtual thread? {}", Thread.currentThread().isVirtual() ? "yes ;)" : "no ;/");
        return CompletableFuture.completedFuture(result);
    }

}
