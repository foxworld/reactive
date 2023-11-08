package hello.reactive;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import hello.reactive.domain.Player;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;
import reactor.test.StepVerifier;

@SpringBootTest
@Slf4j
public class ReactiveMapping {

	@Test
	public void map() {
		Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
				.map(n -> {
					String[] split = n.split("\\s");
					return new Player(split[0], split[1]);
				});
		StepVerifier.create(playerFlux)
		.expectNext(new Player("Michael", "Jordan"))
		.expectNext(new Player("Scottie", "Pippen"))
		.expectNext(new Player("Steve", "Kerr"))
		.verifyComplete();
	}
	
	@Test
	public void flatMap() {
		Flux<Player> playerFlux = Flux.just("Michael Jordan", "Scottie Pippen", "Steve Kerr")
				.flatMap(n -> Mono.just(n)
						.map(p -> {
							String[] split = n.split("\\s");
							return new Player(split[0], split[1]);
						})
						.subscribeOn(Schedulers.parallel())
				);
		

		List<Player> playerList = Arrays.asList(
				new Player("Scottie", "Pippen"),
				new Player("Michael", "Jordan"),
				new Player("Steve", "Kerr"));
				
		
		StepVerifier.create(playerFlux)
		.expectNextMatches(p -> playerList.contains(p))
		.expectNextMatches(p -> playerList.contains(p))
		.expectNextMatches(p -> playerList.contains(p))
		.verifyComplete();
		
	}
	
}
