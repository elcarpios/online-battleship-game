package salvo.salvo;

import java.util.Date;
import java.util.Set;
import javax.persistence.*;


// @SomeWord order, is only affecting at the next line of code
@Entity // Tells Spring to create a Game table for this class
public class Game {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy=GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    private Date creationDate;

    // 1 game can have n gameplays
    @OneToMany(mappedBy="game", fetch=FetchType.EAGER)
    // Set<Type> is a group of elements with this type
    Set<GamePlayer> gameplayers;

    public Game() {
        creationDate = new Date();
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public long getId() {
        return id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public Set<GamePlayer> getGameplayers() {
        return gameplayers;
    }

    public void setGameplayers(Set<GamePlayer> gameplayers) {
        this.gameplayers = gameplayers;
    }
}
