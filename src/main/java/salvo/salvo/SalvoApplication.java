package salvo.salvo;

import java.util.Arrays;
import java.util.Date;
import java.time.Instant;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@SpringBootApplication // Tells Spring to look for cases in the code that need instances of bean
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean // Mark a method that returns an instance of a Java bean
	public CommandLineRunner newPlayer(PlayerRepository playerRepo,
									   GameRepository gameRepo,
									   GamePlayerRepository gamePlayerRepo,
									   ShipRepository shipRepo) {
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


			// Create GamePlayer(s) and Ships


			// Create GamePlayer 1
			GamePlayer gP1 = new GamePlayer(g1,p1);
			gamePlayerRepo.save(gP1);
			// Create Ships
			Ship destroyer1 = new Ship("Destroyer", new HashSet<>(Arrays.asList("H2","H3","H4")));
			Ship submarine1 = new Ship("Submarine", new HashSet<>(Arrays.asList("E1","F1","G1")));
			Ship patrolBoat1 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("B4","B5")));
			// Add the ships
			gP1.addShip(destroyer1);
			gP1.addShip(submarine1);
			gP1.addShip(patrolBoat1);
			// Save the ships into the Repository
			shipRepo.save(destroyer1);
			shipRepo.save(submarine1);
			shipRepo.save(patrolBoat1);


			// Create GamePlayer 2
			GamePlayer gP2 = new GamePlayer(g1,p2);
			gamePlayerRepo.save(gP2);
			// Create Ships
			Ship destroyer2 = new Ship("Destroyer", new HashSet<>(Arrays.asList("B5","C5","D5")));
			Ship patrolBoat2 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("F1","F2")));
			// Add the ships
			gP2.addShip(destroyer2);
			gP2.addShip(patrolBoat2);
			// Save the ships into the Repository
			shipRepo.save(destroyer2);
			shipRepo.save(patrolBoat2);


			// Create GamePlayer 3
			GamePlayer gP3 = new GamePlayer(g2,p4);
			gamePlayerRepo.save(gP3);
			// Create Ships
			Ship destroyer3 = new Ship("Destroyer", new HashSet<>(Arrays.asList("B5","C5","D5")));
			Ship patrolBoat3 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("C6","C7")));
			// Add the ships
			gP3.addShip(destroyer3);
			gP3.addShip(patrolBoat3);
			// Save the ships into the Repository
			shipRepo.save(destroyer3);
			shipRepo.save(patrolBoat3);


			// Create GamePlayer 4
			GamePlayer gP4 = new GamePlayer(g2,p1);
			gamePlayerRepo.save(gP4);
			// Create Ships
			Ship destroyer4 = new Ship("Destroyer", new HashSet<>(Arrays.asList("A2","A3","A4")));
			Ship patrolBoat4 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("G6","H6")));
			// Add the ships
			gP4.addShip(destroyer4);
			gP4.addShip(patrolBoat4);
			// Save the ships into the Repository
			shipRepo.save(destroyer4);
			shipRepo.save(patrolBoat4);

		};
	}
}

// To ignore instances of methods in Spring we use @JsonIgnore