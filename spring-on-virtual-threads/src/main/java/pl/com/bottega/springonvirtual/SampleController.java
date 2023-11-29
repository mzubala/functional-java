package pl.com.bottega.springonvirtual;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.com.bottega.springonvirtual.stackoverflow.StackOverflowClient;

@RestController
@Slf4j
@RequiredArgsConstructor
class SampleController {

    private final StackOverflowService service;

    @GetMapping("/sample")
    @SneakyThrows
    String getSample() {
        log.info("Am I run on a virtual thread? {}", Thread.currentThread().isVirtual() ? "yes ;)" : "no ;/");
        var future = service.getLatestHeadlineAbout("java");
        future.join();
        return future.get();
    }

}
