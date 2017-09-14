package salvo.salvo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication // Tells Spring to look for cases in the code that need instances of bean
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean // Mark a method that returns an instance of a Java bean
	public CommandLineRunner initData(PlayerRepository repository) {
		return (args) -> {
			repository.save(new Player("Jack Bauer", "j.bauer@ctu.gov", "24"));
			repository.save(new Player("Chloe O'Brian", "c.obrian@ctu.gov", "42"));
			repository.save(new Player("Kim Bauer", "kim_bauer@gmail.com", "kb"));
			repository.save(new Player("Tony Almeida", "t.almeida@ctu.gov", "mole"));
		};
	}
}
