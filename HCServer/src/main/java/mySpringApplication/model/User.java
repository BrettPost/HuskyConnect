package mySpringApplication.model;


import javax.persistence.*;

@Entity
@Table(name = "user")
public class User {


    private @Id String username;
    private String password;
    private String full_name;
    private String email;
    private String bio;
    private byte[] imgBlob;


    public User() {
    }

    public User(String username, String password, String full_name, String email, String bio, byte[] imgBlob) {
        this.username = username;
        this.password = password;
        this.full_name = full_name;
        this.email = email;
        this.bio = bio;
        this.imgBlob = imgBlob;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * tests if a password is the same as this users password
     * @param password password being tested
     * @return if the password is the same
     */
    public boolean passwordEquals(String password) {

        return this.password.equals(password);
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public byte[] getImgBlob() {
        return imgBlob;
    }

    public void setImgBlob(byte[] imgBlob) {
        this.imgBlob = imgBlob;
    }
}