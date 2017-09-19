package salvo.salvo;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity // Tells Spring to create a Game table for this class
public class Ship {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy= GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    // Ship Type: Cruiser ...
    private String type;

    // List of locations
    @ElementCollection
    private Set<String> locations = new HashSet<>();

    // n ship can have 1 gameplayer
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gameplayer;// Declare one because is manyToOne

    public Ship(String type, Set<String> locations){
        this.type = type;
        this.locations = locations;
    }

    public Ship() {
    }

    public GamePlayer getGameplayer() {
        return gameplayer;
    }

    public void setGameplayer(GamePlayer gameplayer) {
        this.gameplayer = gameplayer;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Set<String> getLocations() {
        return locations;
    }

    public void setLocations(Set<String> locations) {
        this.locations = locations;
    }
}
