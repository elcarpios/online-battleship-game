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
	public CommandLineRunner newPlayer(PlayerRepository playerRepo, GameRepository gameRepo, GamePlayerRepository gamePlayerRepo) {
		return (args) -> {

			// Create players
			Player p1 = new Player("Jack Bauer","j.bauer@ctu.gov","24");
			playerRepo.save(p1);

			Player p2 = new Player("Chloe O'Brian","c.obrian@ctu.gov","42");
			playerRepo.save(p2);

			Player p3 = new Player("Kim Bauer","kim_bauer@gmail.com","kb");
			playerRepo.save(p3);

			Player p4 = new Player("Tony Almeida","t.almeida@ctu.gov","mole");
			playerRepo.save(p4);

			// Create games
			Game g1 = new Game();
			gameRepo.save(g1);
			Game g2 = new Game();
			Date date = new Date();
			g2.setCreationDate(Date.from(date.toInstant().plusSeconds(3600)));
			gameRepo.save(g2);
			Game g3 = new Game();
			g3.setCreationDate(Date.from(date.toInstant().plusSeconds(7200)));
			gameRepo.save(g3);

			// Create GamePlayers
			GamePlayer gP1 = new GamePlayer(g1,p1);
			GamePlayer gP2 = new GamePlayer(g1,p2);
			gamePlayerRepo.save(gP1);
			gamePlayerRepo.save(gP2);

			GamePlayer gP3 = new GamePlayer(g2,p4);
			GamePlayer gP4 = new GamePlayer(g2,p1);
			gamePlayerRepo.save(gP3);
			gamePlayerRepo.save(gP4);
		};
	}
}

// To ignore instances of methods in Spring we use @JsonIgnore