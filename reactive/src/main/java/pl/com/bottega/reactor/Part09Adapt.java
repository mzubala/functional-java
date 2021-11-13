/*
 * Copyright 2002-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package pl.com.bottega.reactor;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.reactivestreams.Publisher;
import pl.com.bottega.reactor.domain.User;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

public class Part09Adapt {

//========================================================================================

	// TODO Adapt Flux to RxJava Flowable
	Flowable<User> fromFluxToFlowable(Flux<User> flux) {
		return null;
	}

	// TODO Adapt RxJava Flowable to Flux
	Flux<User> fromFlowableToFlux(Flowable<User> flowable) {
		return null;
	}

//========================================================================================

	// TODO Adapt Flux to RxJava Observable
	Observable<User> fromFluxToObservable(Flux<User> flux) {
		return null;
	}

	// TODO Adapt RxJava Observable to Flux
	Flux<User> fromObservableToFlux(Observable<User> observable) {
		return null;
	}

//========================================================================================

	// TODO Adapt Mono to RxJava Single
	Single<User> fromMonoToSingle(Mono<User> mono) {
		return null;
	}

	// TODO Adapt RxJava Single to Mono
	Mono<User> fromSingleToMono(Single<User> single) {
		return null;
	}

//========================================================================================

	// TODO Adapt Mono to Java 8+ CompletableFuture
	CompletableFuture<User> fromMonoToCompletableFuture(Mono<User> mono) {
		return null;
	}

	// TODO Adapt Java 8+ CompletableFuture to Mono
	Mono<User> fromCompletableFutureToMono(CompletableFuture<User> future) {
		return null;
	}

}
