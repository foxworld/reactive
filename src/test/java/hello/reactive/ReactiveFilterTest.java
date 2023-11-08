package hello.reactive;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
public class ReactiveFilterTest {
	@Test
	public void skipAFew() {
		Flux<String> skipFlux = Flux.just("one","two", "skip a few", "ninety nine", "one hundred").skip(3);
		
		StepVerifier.create(skipFlux)
			.expectNext("ninety nine", "one hundred")
			.verifyComplete();
	}
	
	@Test
	public void skipAFewSeconds() {
		// 4초후부터 값을 가져온다
		Flux<String> skipFlux = Flux.just("one","two", "skip a few", "ninety nine", "one hundred")
				.delayElements(Duration.ofSeconds(1))
				.skip(Duration.ofSeconds(4));
		
		
		StepVerifier.create(skipFlux)
		.expectNext("ninety nine", "one hundred")
		.verifyComplete();
	}
	
	
	@Test
	public void take1() {
		// 3개까지만 읽어온다
		Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton").take(3);

		StepVerifier.create(nationalParkFlux)
			.expectNext("Yellowstone", "Yosemite", "Grand Canyon")
			.verifyComplete();
	}
	
	@Test
	public void take2() {
		// 3초동안 읽어온다
		Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
				.delayElements(Duration.ofSeconds(1))
				.take(Duration.ofMillis(3500));

		StepVerifier.create(nationalParkFlux)
			.expectNext("Yellowstone", "Yosemite", "Grand Canyon")
			.verifyComplete();
	}
	
	@Test
	public void filter() {
		// space 는 제외하고 읽어온다
		Flux<String> nationalParkFlux = Flux.just("Yellowstone", "Yosemite", "Grand Canyon", "Zion", "Grand Teton")
				.filter(np -> !np.contains(" "));
		
		StepVerifier.create(nationalParkFlux)
			.expectNext("Yellowstone", "Yosemite", "Zion")
			.verifyComplete();
	}
	
	@Test
	public void distinct() {
		Flux<String> animalFlux = Flux.just("dog", "cat", "bird", "dog", "bird", "anteater").distinct();

		StepVerifier.create(animalFlux)
		.expectNext("dog", "cat", "bird", "anteater")
		.verifyComplete();

	}
}
