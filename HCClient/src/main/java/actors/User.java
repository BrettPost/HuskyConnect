package actors;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.util.JSONPObject;
import databaseconnections.HttpCon;
import javafx.beans.binding.DoubleExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.apache.http.HttpResponse;
import pages.ProfilePage;
import userinterface.GUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class User {
    private String username;
    private String email;
    private String full_name;
    private String bio;
    private GUI gui;
    private HashSet<String> tags;
    private Image icon;
    private List<User> connectedUsers;
    private ProfilePage linkedPage;

    @SuppressWarnings("This constructor is nessisary for jackson-databind in getUser")
    public User(){
        //TODO add this default populated information to database
        this.connectedUsers = new ArrayList<>();
        this.tags = new HashSet<>();

        this.gui = GUI.DEFUALT_GUI;

    }

    /**
     * Create a user object
     * @param username the user's username
     * @param email the user's email
     * @param bio the user's bio
     * @param tags the user's tags
     */
    public User(String username, String email, String bio, Image icon, GUI gui, String... tags) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.icon = icon;
        this.gui = gui;

        this.tags = new HashSet<>();
        this.tags.addAll(Arrays.asList(tags));

        this.connectedUsers = new ArrayList<>();
        if (!username.equals("HuskyConnect")) {
            connectedUsers.add(GUI.huskyConnectUser);
        }
    }

    public static User getUser(String username, Long token){
        try{
            HttpResponse response = HttpCon.getUser(username,token);

            //Tests to see if the status of the http was a success
            assert response != null;
            if(response.getStatusLine().getStatusCode() == 200){

                //maps the response to a user
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("200");
                return mapper.readValue(response.getEntity().getContent(), User.class);
            }else {
                //TODO separate out the different possible errors to display what went wrong to the user
                //Http failed in some way (could simply be invalid token or username)
                System.out.println("invalid token or username");
                return null;
            }

        }catch (Exception e){
            return null;
        }

    }

    //TODO remove this as you should never need to get all users
    public static User[] getUsers(Long token){
        try{
            HttpResponse response = HttpCon.getUsers(token);

            //Tests to see if the status of the http was a success
            assert response != null;
            if(response.getStatusLine().getStatusCode() == 200){

                //maps the response to a user
                ObjectMapper mapper = new ObjectMapper();
                System.out.println("200");
                return mapper.readValue(response.getEntity().getContent(), User[].class);
            }else {
                //TODO separate out the different possible errors to display what went wrong to the user
                //Http failed in some way (could simply be invalid token or username)
                System.out.println("invalid token or username");
                return null;
            }

        }catch (Exception e){
            return null;
        }

    }

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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

    public Image getIcon() {
        return icon;
    }

    public void setIcon(Image icon) {
        this.icon = icon;
    }

    public HashSet<String> getTags() {
        return tags;
    }

    /**
     * Add a tag to the user
     * @param tag the tag to add
     */
    public void addTag(String tag) {
        tags.add(tag);
    }

    /**
     * Add several tags to the user
     * @param tags the tags to add
     */
    public void addTags(String... tags) {
        this.tags.addAll(Arrays.asList(tags));
    }

    /**
     * Remove a tag from the user
     * @param tag the tag to remove
     * @return true if successful, false if failed
     */
    public boolean removeTag(String tag) {
        return tags.remove(tag);
    }

    /**
     * Check if a user has a specific tag
     * @param tag the tag to check
     * @return true if the tag exists, false if it doesn't
     */
    public boolean userHasTag(String tag) {
        return tags.contains(tag);
    }

    /**
     * Add a user connection
     * @param connection the user to connect
     */
    public void addConnection(User connection) {
        connectedUsers.add(connection);
    }

    public BorderPane generateCard() {
        BorderPane card = new BorderPane();
        Circle logoCircle = new Circle(1, 1, 1);
        if (icon == null || icon.isError())
            icon = GUI.loadImageResource("\\src\\main\\resources\\default-user-icon.png");

        logoCircle.setFill(new ImagePattern(icon));

        VBox imageBox = new VBox(logoCircle);

        Label name = GUI.scaleableText(username, gui.rootPane.heightProperty(), card.widthProperty(), 25.);

        User userInst = this;

        HBox area = new HBox(name);
        area.setAlignment(Pos.CENTER);
        Button profile = new Button("Profile");

        card.setLeft(imageBox);
        card.setRight(profile);
        card.setCenter(area);

        //styling
        logoCircle.radiusProperty().bind(gui.rootPane.heightProperty().divide(18));
        profile.prefHeightProperty().bind(card.heightProperty());
        profile.prefWidthProperty().bind(card.widthProperty().divide(6));

        DoubleExpression userWidthBaseBind = gui.rootPane.widthProperty().divide(2);

        card.prefWidthProperty().bind(userWidthBaseBind);
        card.prefHeightProperty().bind(gui.rootPane.heightProperty().divide(9));

        // TO DO look into making profiles look distinct
        profile.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (linkedPage == null) {
                    linkedPage = new ProfilePage(userInst, gui);
                }

                gui.rootPane.setCenter(linkedPage.generatePage());
            }
        });
        return card;
    }

    public VBox generateUserFeed() {
        VBox feed = new VBox();
        DoubleExpression userWidthBaseBind = gui.rootPane.widthProperty().divide(2);

        // title the user feed
        Label userFeed = GUI.scaleableText("User feed", userWidthBaseBind, gui.rootPane.heightProperty().multiply(2).divide(3).divide(25));
        VBox userList = new VBox(); // create a list of users in form of vbox

        // make it so the user list is a scrollable entity
        ScrollPane scrollPane = new ScrollPane(userList);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.fitToWidthProperty().setValue(true);

        // add all of the connected users to the user feed
        for (var user : connectedUsers) {
            System.out.println(user.username);
            userList.getChildren().add(user.generateCard());
        }

        // add the user feed and scroll pane to the feed
        feed.getChildren().addAll(userFeed, scrollPane);

        // set the max width and height properties to their respective heights, and the feed to be centered
        feed.maxWidthProperty().bind(userWidthBaseBind);
        feed.maxHeightProperty().bind(gui.rootPane.heightProperty().multiply(2).divide(3));
        feed.setAlignment(Pos.CENTER);

        // set the spacing to default
        userList.setSpacing(GUI.DEFAULT_SPACING);

        return feed;
    }
}
