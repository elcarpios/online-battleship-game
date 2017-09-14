package salvo.salvo;

import java.util.Date;
import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;

@SpringBootApplication // Tells Spring to look for cases in the code that need instances of bean
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean // Mark a method that returns an instance of a Java bean
	public CommandLineRunner newGame(GameRepository repository) {
		return (args) -> {
			repository.save(new Game());
			Game g1 = new Game();
			Date date = new Date();
			g1.setCreationDate(Date.from(date.toInstant().plusSeconds(3600)));
			repository.save(g1);
			Game g2 = new Game();
			g2.setCreationDate(Date.from(date.toInstant().plusSeconds(7200)));
			repository.save(g2);
		};
	}
}
