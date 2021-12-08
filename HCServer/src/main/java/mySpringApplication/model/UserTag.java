package mySpringApplication.model;

import javax.persistence.*;

@Entity
@IdClass(UserTagId.class)
@Table(name = "user_tag")
public class UserTag {

    private @Id String username;
    private @Id String tag;

    public UserTag(){

    }

    public UserTag(String username, String tag){
        this.username = username;
        this.tag = tag;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
