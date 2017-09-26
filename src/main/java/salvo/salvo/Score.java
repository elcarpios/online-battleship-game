package salvo.salvo;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Score {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy= GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    private int score;

    private Date finishDate;

    // 1 Game has 2 scores
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="game_id")
    private Game game;

    // 1 Player can has n scores
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name="player_id")
    private Player player;

    public Score(int score, Date finishDate, Game game, Player player) {
        this.score = score;
        this.finishDate = finishDate;
        this.game = game;
        this.player = player;
    }

    public Score() { }

    public long getId() {
        return id;
    }

    public int getScore() {
        return score;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public Game getGame() {
        return game;
    }

    public Player getPlayer() {
        return player;
    }
}
