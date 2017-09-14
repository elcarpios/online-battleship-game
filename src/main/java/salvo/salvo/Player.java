package salvo.salvo;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;


// @SomeWord order, is only affecting at the next line of code
@Entity // Tells Spring to create a Player table for this class
public class Player {

    @Id //  Says that the id instance variable holds the database key for this class
    @GeneratedValue(strategy=GenerationType.AUTO) // Tells JPA to get the Id from the DBMS.
    private long id;

    private String name;
    private String userName;
    private String password;

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

    public String getUserName() {
        return userName;
    }

    public String getPassword() {
        return password;
    }
}
