package hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.stream.IntStream;

@SpringBootApplication
@RestController
public class Application {

  @Value("${random-num-url}")
  private String randomNumUrl;

  @Value("${random-word-url}")
  private String randomWordUrl;

  public static void main(String[] args) {
    SpringApplication.run(Application.class, args);
  }

  @RequestMapping("/")
  public Mono<String> index() {
    return randomNum().flatMap(num -> IntStream
              .range(0, num)
              .mapToObj(i -> randomWord())
              .reduce((a, b) -> a.zipWith(b, (s1, s2) -> s1 + " " + s2))
              .orElse(Mono.error(new Exception("Could not fetch words")))
    );
  }

  private WebClient webClient = WebClient.create();

  private Mono<Integer> randomNum() {
    return webClient
            .get()
            .uri(randomNumUrl)
            .retrieve()
            .bodyToMono(String.class)
            .map(Integer::parseInt);
  }

  private Mono<String> randomWord() {
    return webClient
            .get()
            .uri(randomWordUrl)
            .retrieve()
            .bodyToMono(String.class);
  }
}

