package mySpringApplication.model;

import java.io.Serializable;
import java.util.Objects;

public class ConnectionId implements Serializable {
    private String user1;
    private String user2;

    //default constructor is required for this class to be used by java spring
    public ConnectionId(){

    }

    public ConnectionId(String user1, String user2){
        this.user1 = user1;
        this.user2 = user2;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConnectionId userTagId = (ConnectionId) o;
        return Objects.equals(user1, userTagId.user1) && Objects.equals(user2, userTagId.user2);
    }

    @Override
    public int hashCode() {
        return Objects.hash(user1, user2);
    }
}
