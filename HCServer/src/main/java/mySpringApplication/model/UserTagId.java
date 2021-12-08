package mySpringApplication.model;

import java.io.Serializable;
import java.util.Objects;

public class UserTagId implements Serializable {
    private String username;
    private String tag;

    //default constructor is required for this class to be used by java spring
    public UserTagId(){

    }

    public UserTagId(String username, String tag){
        this.username = username;
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }

    public String getTag() {
        return tag;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserTagId userTagId = (UserTagId) o;
        return Objects.equals(username, userTagId.username) && Objects.equals(tag, userTagId.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(username, tag);
    }
}
