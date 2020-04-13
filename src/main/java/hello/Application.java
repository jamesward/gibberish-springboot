package hello;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.List;
import java.util.stream.Collectors;
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
  public String index() {
    int num = randomNum();

    List<String> words = IntStream
            .range(0, num)
            .mapToObj(i -> randomWord())
            .collect(Collectors.toList());

    return String.join(" ", words);
  }

  @Autowired
  private RestTemplateBuilder builder;

  private int randomNum() {
    RestTemplate restTemplate = builder.build();
    String body = restTemplate.getForEntity(randomNumUrl, String.class).getBody();
    return Integer.parseInt(body);
  }

  private String randomWord() {
    RestTemplate restTemplate = builder.build();
    return restTemplate.getForEntity(randomWordUrl, String.class).getBody();
  }
}

