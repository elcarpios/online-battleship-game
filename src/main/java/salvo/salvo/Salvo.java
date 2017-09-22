package salvo.salvo;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Salvo {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy= GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    private int turn;

    @ElementCollection
    private List<String> locations = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gameplayer;

    public Salvo() {}

    public Salvo(int turn, List<String> locations, GamePlayer gameplayer) {
        this.turn = turn;
        this.locations = locations;
        this.gameplayer = gameplayer;
    }

    public int getTurn() {
        return turn;
    }

    public void setTurn(int turn) {
        this.turn = turn;
    }

    public List<String> getLocations() {
        return locations;
    }

    public void setLocations(List<String> locations) {
        this.locations = locations;
    }

    public GamePlayer getGameplayer() {
        return gameplayer;
    }

    public void setGamePlayer(GamePlayer gameplayer) {
        this.gameplayer = gameplayer;
    }
}
