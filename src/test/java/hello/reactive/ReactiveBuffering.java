package hello.reactive;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
public class ReactiveBuffering {
	@Test
	public void buffer1() {
		Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Banana", "Kiwi", "Strawberry");
		Flux<List<String>> bufferedFlux = fruitFlux.buffer(3);
		
		StepVerifier
			.create(bufferedFlux)
			.expectNext(Arrays.asList("Apple", "Orange", "Banana"))
			.expectNext(Arrays.asList("Kiwi", "Strawberry"))
			.verifyComplete();
	}
	
	@Test
	public void buffer2() {
		Flux.just("Apple", "Orange", "Banana", "Kiwi", "Strawberry")
				.buffer(3)
				.flatMap(x -> 
					Flux.fromIterable(x)
						.map(y -> y.toUpperCase())
						.subscribeOn(Schedulers.parallel())
						.log()
						).subscribe();
	}
	
	@Test
	public void collectList() {
		Flux<String> fruitFlux = Flux.just("Apple", "Orange", "Banana", "Kiwi", "Strawberry");
		
		Mono<List<String>> fruiltListMono = fruitFlux.collectList();
		
		StepVerifier
			.create(fruiltListMono)
			.expectNext(Arrays.asList("Apple", "Orange", "Banana", "Kiwi", "Strawberry"))
			.verifyComplete();
	}
	
	@Test
	public void collectMap() {
		Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
		
		Mono<Map<Character, String>> animalMapMono = animalFlux.collectMap(a -> a.charAt(0)).log();
		
		StepVerifier
			.create(animalMapMono)
			.expectNextMatches(map -> {
				return map.size() == 3 &&
						map.get('a').equals("aardvark") &&
						map.get('e').equals("eagle") &&
						map.get('k').equals("kangaroo");
			})
			.verifyComplete();
	}
	
	@Test
	public void all() {
		Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");
		
		// 모든 값에 'a' 가 있으므로 true
		Mono<Boolean> hasAMono = animalFlux.all(a -> a.contains("a"));
		StepVerifier
			.create(hasAMono)
			.expectNext(true)
			.verifyComplete();
		
		// 전체 값중에 'k' 미존재하는 값이 있으로 false 
		Mono<Boolean> hasKMono = animalFlux.all(a -> a.contains("k"));
		StepVerifier
			.create(hasKMono)
			.expectNext(false)
			.verifyComplete();
	}
	
	@Test
	public void any() {
		Flux<String> animalFlux = Flux.just("aardvark", "elephant", "koala", "eagle", "kangaroo");

		Mono<Boolean> hasAMono = animalFlux.any(a -> a.contains("t"));
		StepVerifier
			.create(hasAMono)
			.expectNext(true)
			.verifyComplete();
		
		Mono<Boolean> hasKMono = animalFlux.any(a -> a.contains("z"));
		StepVerifier
			.create(hasKMono)
			.expectNext(false)
			.verifyComplete();
		
	}
}
