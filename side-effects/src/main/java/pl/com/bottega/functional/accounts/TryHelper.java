package pl.com.bottega.functional.accounts;

import io.vavr.control.Try;
import reactor.core.publisher.Mono;

class TryHelper {
    static <T> Mono<T> toMono(Try<T> aTry) {
        return aTry.fold(Mono::error, Mono::just);
    }
}
