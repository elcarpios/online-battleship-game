package salvo.salvo;

import java.util.*;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.time.LocalDate;

@SpringBootApplication // Tells Spring to look for cases in the code that need instances of bean
public class SalvoApplication {

	public static void main(String[] args) {
		SpringApplication.run(SalvoApplication.class, args);
	}

	@Bean // Mark a method that returns an instance of a Java bean
	public CommandLineRunner newPlayer(PlayerRepository playerRepo,
									   GameRepository gameRepo,
									   GamePlayerRepository gamePlayerRepo,
									   ShipRepository shipRepo,
									   SalvoRepository salvoRepo,
									   ScoreRepository scoreRepo) {
		return (args) -> {

			// Create players
			Player p1 = new Player("Jack Bauer","j.bauer@ctu.gov","24");
			Player p2 = new Player("Chloe O'Brian","c.obrian@ctu.gov","42");
			Player p3 = new Player("Kim Bauer","kim_bauer@gmail.com","kb");
			Player p4 = new Player("Tony Almeida","t.almeida@ctu.gov","mole");


			// Create games
			Date date = new Date();

			Game g1 = new Game();
			Game g2 = new Game();
			g2.setCreationDate(Date.from(date.toInstant().plusSeconds(3600)));
			Game g3 = new Game();
			g3.setCreationDate(Date.from(date.toInstant().plusSeconds(7200)));


			// Create GamePlayer(s)
			GamePlayer gP1 = new GamePlayer(g1,p1);
			GamePlayer gP2 = new GamePlayer(g1,p2);
			GamePlayer gP3 = new GamePlayer(g2,p4);
			GamePlayer gP4 = new GamePlayer(g2,p1);


			// Create Ships
			Ship destroyer1 = new Ship("Destroyer", new HashSet<>(Arrays.asList("H2","H3","H4")));
			Ship submarine1 = new Ship("Submarine", new HashSet<>(Arrays.asList("E1","F1","G1")));
			Ship patrolBoat1 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("B4","B5")));

			Ship destroyer2 = new Ship("Destroyer", new HashSet<>(Arrays.asList("B5","C5","D5")));
			Ship patrolBoat2 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("F1","F2")));

			Ship destroyer3 = new Ship("Destroyer", new HashSet<>(Arrays.asList("B5","C5","D5")));
			Ship patrolBoat3 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("C6","C7")));

			Ship destroyer4 = new Ship("Destroyer", new HashSet<>(Arrays.asList("A2","A3","A4")));
			Ship patrolBoat4 = new Ship("Patrol Boat", new HashSet<>(Arrays.asList("G6","H6")));


			// Create Salvoes
			Salvo salvo1 = new Salvo(1, new ArrayList<>(Arrays.asList("B5","C5","F1")),gP1);
			Salvo salvo2 = new Salvo(1, new ArrayList<>(Arrays.asList("B4","B5","B6")),gP2);
			Salvo salvo3 = new Salvo(2, new ArrayList<>(Arrays.asList("F2","D5")),gP1);
			Salvo salvo4 = new Salvo(2, new ArrayList<>(Arrays.asList("E1","H3","A2")),gP2);

			Salvo salvo5 = new Salvo(1, new ArrayList<>(Arrays.asList("G6","H6","A4")),gP4);
			Salvo salvo6 = new Salvo(1, new ArrayList<>(Arrays.asList("H1","H2","H3")),gP3);
			Salvo salvo7 = new Salvo(2, new ArrayList<>(Arrays.asList("A2","A3","D8")),gP4);
			Salvo salvo8 = new Salvo(2, new ArrayList<>(Arrays.asList("E1","F2","G3")),gP3);


			// Create Scores
			Score s1 = new Score(1, new Date(), g1, p1);
			Score s2 = new Score(0, new Date(), g1, p2);
			Score s3 = new Score(0, new Date(), g2, p1);
			Score s4 = new Score(1, new Date(), g2, p4);
			Score s5 = new Score(0.5, new Date(), g3, p1);


			//Save ALL
			playerRepo.save(p1);
			playerRepo.save(p2);
			playerRepo.save(p3);
			playerRepo.save(p4);

			gameRepo.save(g1);
			gameRepo.save(g2);
			gameRepo.save(g3);

			gP1.addShip(destroyer1);
			gP1.addShip(submarine1);
			gP1.addShip(patrolBoat1);
			gP2.addShip(destroyer2);
			gP2.addShip(patrolBoat2);
			gP3.addShip(destroyer3);
			gP3.addShip(patrolBoat3);
			gP4.addShip(destroyer4);
			gP4.addShip(patrolBoat4);

			gamePlayerRepo.save(gP1);
			gamePlayerRepo.save(gP2);
			gamePlayerRepo.save(gP3);
			gamePlayerRepo.save(gP4);

			shipRepo.save(destroyer1);
			shipRepo.save(submarine1);
			shipRepo.save(patrolBoat1);
			shipRepo.save(destroyer2);
			shipRepo.save(patrolBoat2);
			shipRepo.save(destroyer3);
			shipRepo.save(patrolBoat3);
			shipRepo.save(destroyer4);
			shipRepo.save(patrolBoat4);

			salvoRepo.save(salvo1);
			salvoRepo.save(salvo2);
			salvoRepo.save(salvo3);
			salvoRepo.save(salvo4);
			salvoRepo.save(salvo5);
			salvoRepo.save(salvo6);
			salvoRepo.save(salvo7);
			salvoRepo.save(salvo8);

			scoreRepo.save(s1);
			scoreRepo.save(s2);
			scoreRepo.save(s3);
			scoreRepo.save(s4);
			scoreRepo.save(s5);
		};
	}
}

// To ignore instances of methods in Spring we use @JsonIgnore

@Configuration
class WebSecurityConfiguration extends GlobalAuthenticationConfigurerAdapter {
	@Autowired
	PlayerRepository playerRepo;

	@Override
	public void init(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService());
	}

	@Bean
	UserDetailsService userDetailsService() {
		return new UserDetailsService() {

			@Override
			public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
				List<Player> players = playerRepo.findByUserName(name);
				if (!players.isEmpty()) {
					Player player = players.get(0);
					return new User(player.getUserName(), player.getPassword(),
							AuthorityUtils.createAuthorityList("USER"));
				} else {
					throw new UsernameNotFoundException("Unknown user: " + name);
				}
			}
		};
	}
}

@EnableWebSecurity
@Configuration
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.authorizeRequests()
				.antMatchers("/api/**").hasAuthority("ADMIN")
				.antMatchers("/rest/**").hasAuthority("ADMIN")
				.antMatchers("/web/**").permitAll()
				.and()
				.formLogin();
	}
}

// TODO: https://ubiqum.socraticarts.com/ebooks/item?id=1436 -> Configuring Security for Web Services

// TODO: Test! POINT 3