package mySpringApplication.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(ConnectionId.class)
@Table(name = "connection")
public class Connection {

    private @Id String user1;
    private @Id String user2;
    private boolean accepted;

    public Connection(){

    }

    public Connection(String user1, String user2, boolean accepted){
        this.user1 = user1;
        this.user2 = user2;
        this.accepted = accepted;
    }

    public String getUser1() {
        return user1;
    }

    public void setUser1(String user1) {
        this.user1 = user1;
    }

    public String getUser2() {
        return user2;
    }

    public void setUser2(String user2) {
        this.user2 = user2;
    }

    public boolean getAccepted() {
        return accepted;
    }

    public void setAccepted(boolean accepted) {
        this.accepted = accepted;
    }

}
