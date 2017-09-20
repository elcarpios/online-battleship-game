package salvo.salvo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


// @SomeWord order, is only affecting at the next line of code
@Entity // Tells Spring to create a Game table for this class
public class GamePlayer {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy=GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    // N gameplays only can have 1 player
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "player_id") // We create a new column for that fetch with name 'player_id'
    private Player player; // Save all the info of the player

    // N gameplays only can have 1 game
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id") // We create a new column for that fetch with name 'game_id'
    private Game game; // Save all the info of the game

    // 1 gamePlayer has n ships
    @OneToMany(mappedBy = "gameplayer", fetch=FetchType.EAGER)
    private Set<Ship> ships;

    public GamePlayer() { }

    public GamePlayer(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.ships = new HashSet<>();
    }

    public long getId() {
        return id;
    }

    public Player getPlayer() {
        return player;
    }

    public Game getGame() {
        return game;
    }

    public Set<Ship> getShips() {
        return ships;
    }

    public void setShips(Set<Ship> ships) {
        this.ships = ships;
    }

    public void addShip(Ship ship) {
        ship.setGameplayer(this);
        this.ships.add(ship);
    }
}
