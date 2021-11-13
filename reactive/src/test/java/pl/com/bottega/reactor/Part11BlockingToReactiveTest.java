package pl.com.bottega.reactor;

import java.util.Iterator;

import pl.com.bottega.reactor.domain.User;
import pl.com.bottega.reactor.repository.BlockingUserRepository;
import pl.com.bottega.reactor.repository.ReactiveRepository;
import pl.com.bottega.reactor.repository.ReactiveUserRepository;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

/**
 * Learn how to call blocking code from Reactive one with adapted concurrency strategy for
 * blocking code that produces or receives data.
 * <p>
 * For those who know RxJava:
 * - RxJava subscribeOn = Reactor subscribeOn
 * - RxJava observeOn = Reactor publishOn
 *
 * @author Sebastien Deleuze
 * @see Flux#subscribeOn(Scheduler)
 * @see Flux#publishOn(Scheduler)
 * @see Schedulers
 */
public class Part11BlockingToReactiveTest {

    Part11BlockingToReactive workshop = new Part11BlockingToReactive();

//========================================================================================

    @Test
    public void slowPublisherFastSubscriber() {
        BlockingUserRepository repository = new BlockingUserRepository();
        Flux<User> flux = workshop.blockingRepositoryToFlux(repository);
        assertThat(repository.getCallCount()).isEqualTo(0)
            .withFailMessage("The call to findAll must be deferred until the flux is subscribed");
        StepVerifier.create(flux)
            .expectNext(User.SKYLER, User.JESSE, User.WALTER, User.SAUL)
            .verifyComplete();
    }

//========================================================================================

    @Test
    public void fastPublisherSlowSubscriber() {
        ReactiveRepository<User> reactiveRepository = new ReactiveUserRepository();
        BlockingUserRepository blockingRepository = new BlockingUserRepository(new User[]{});
        Mono<Void> complete = workshop.fluxToBlockingRepository(reactiveRepository.findAll(), blockingRepository);
        assertEquals(0, blockingRepository.getCallCount());
        StepVerifier.create(complete)
            .verifyComplete();
        Iterator<User> it = blockingRepository.findAll().iterator();
        assertEquals(User.SKYLER, it.next());
        assertEquals(User.JESSE, it.next());
        assertEquals(User.WALTER, it.next());
        assertEquals(User.SAUL, it.next());
        assertFalse(it.hasNext());
    }

}
