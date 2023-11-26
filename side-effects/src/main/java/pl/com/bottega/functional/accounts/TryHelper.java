package pl.com.bottega.functional.accounts;

import io.vavr.control.Try;
import reactor.core.publisher.Mono;

class TryHelper {
    // TODO use this util method in handlers instead of Try.get
    static <T> Mono<T> toMono(Try<T> aTry) {
        // TODO create a mono with an object or error
        return Mono.empty();
    }
}
