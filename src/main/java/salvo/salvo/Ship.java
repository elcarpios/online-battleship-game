package salvo.salvo;

import javax.persistence.*;
import java.util.Set;

@Entity // Tells Spring to create a Game table for this class
public class Ship {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy= GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    // n ship can have 1 gameplayer
    @ManyToOne(fetch=FetchType.EAGER)
    @JoinColumn(name="gamePlayer_id")
    private GamePlayer gameplayer;// Declare one because is manyToOne

    public GamePlayer getGameplayer() {
        return gameplayer;
    }
}
