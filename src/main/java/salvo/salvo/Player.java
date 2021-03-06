package salvo.salvo;

import javax.persistence.*;
import java.util.List;
import java.util.Set;

// @SomeWord order, is only affecting at the next line of code
@Entity // Tells Spring to create a Player table for this class
public class Player {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy=GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    private String name;
    private String userName;
    private String password;

    // 1 player can have n gameplays
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    // Set<Type> is a group of elements with this type
    Set<GamePlayer> gameplayers;

    // 1 player can has n socres
    @OneToMany(mappedBy="player", fetch=FetchType.EAGER)
    Set<Score> scores;

    public Player() { } // We need an empty constructor for the internal uses

    public Player(String name, String userName, String password) {
        this.name = name;
        this.userName = userName;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Set<GamePlayer> getGameplayers() {
        return gameplayers;
    }

    public String getUserName() {
        return userName;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public void setGameplayers(Set<GamePlayer> gameplayers) {
        this.gameplayers = gameplayers;
    }

    public Set<Score> getScores() {
        return scores;
    }

    public Score getScore(Game game) {
        return this.getScores().stream()
                        .filter(score -> score.getGame().equals(game))
                        .findFirst()
                        .orElse(null);
    }

}
