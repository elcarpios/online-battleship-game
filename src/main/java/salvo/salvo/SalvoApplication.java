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
			// Create unique variables to store locations and ships
			Set<String> locations = new HashSet<>();
			Set<Ship> ships = new HashSet<>();

			// GamePlayer1 (Way-to-do-it 1)
			Ship destroyer = new Ship();
			destroyer.setType("Destroyer");
			locations.addAll(Arrays.asList("H2","H3","H4"));
			destroyer.setLocations(locations);
			shipRepo.save(destroyer);
			locations.clear();

			Ship submarine = new Ship();
			submarine.setType("Submarine");
			locations.addAll(Arrays.asList("E1","F1","G1"));
			submarine.setLocations(locations);
			shipRepo.save(submarine);
			locations.clear();

			Ship patrolBoat = new Ship();
			patrolBoat.setType("Patrol Boat");
			locations.addAll(Arrays.asList("B4","B5"));
			patrolBoat.setLocations(locations);
			shipRepo.save(patrolBoat);
			locations.clear();

			// Add all the ships into the set
			ships.addAll(Arrays.asList(destroyer,submarine,patrolBoat));

			// Create GamePlayer 1
			GamePlayer gP1 = new GamePlayer(g1,p1);
			gP1.setShips(ships);
			gamePlayerRepo.save(gP1);
			ships.clear();


			// GamePlayer 2 (Way-to-do-it 2)
			locations.addAll(Arrays.asList("B5","C5","D5"));
			Ship destroyer2 = new Ship("Destroyer",locations);
			shipRepo.save(destroyer2);
			locations.clear();

			locations.addAll(Arrays.asList("F1","F2"));
			Ship patrolBoat2 = new Ship("Patrol Boat", locations);
			shipRepo.save(patrolBoat2);
			locations.clear();

			// Add all the ships into the set
			ships.addAll(Arrays.asList(destroyer2,patrolBoat2));

			// Create GamePlayer 2
			GamePlayer gP2 = new GamePlayer(g1,p2);
			gP2.setShips(ships);
			gamePlayerRepo.save(gP2);
			ships.clear();


			// GamePlayer 3
			locations.addAll(Arrays.asList("B5","C5","D5"));
			Ship destroyer3 = new Ship("Destroyer",locations);
			shipRepo.save(destroyer3);
			locations.clear();

			locations.addAll(Arrays.asList("C6","C7"));
			Ship patrolBoat3 = new Ship("Patrol Boat", locations);
			shipRepo.save(patrolBoat3);
			locations.clear();

			// Add all the ships into the set
			ships.addAll(Arrays.asList(destroyer3,patrolBoat3));

			// Create GamePlayer 3
			GamePlayer gP3 = new GamePlayer(g2,p4);
			gP3.setShips(ships);
			gamePlayerRepo.save(gP3);
			ships.clear();
/*
			// GamePlayer 4
			locations.addAll(Arrays.asList("A2","A3","A4"));
			destroyer.setLocations(locations);
			locations.clear();

			locations.addAll(Arrays.asList("G6","H6"));
			patrolBoat.setLocations(locations);
			locations.clear();

			// Add all the ships into the set
			ships.addAll(Arrays.asList(destroyer,patrolBoat));

			// Create GamePlayer 4
			GamePlayer gP4 = new GamePlayer(g2,p1);
			gP3.setShips(ships);
			gamePlayerRepo.save(gP4);
			ships.clear();
*/
		};
	}
}

// To ignore instances of methods in Spring we use @JsonIgnore