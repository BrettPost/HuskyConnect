package actors;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import userinterface.GUI;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class User {
    private String username;
    private String email;
    private String bio;
    private GUI guiInstance;
    private HashSet<String> tags;
    private Image icon;
    private List<User> connectedUsers;

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
        this.guiInstance = gui;

        this.tags = new HashSet<>();
        this.tags.addAll(Arrays.asList(tags));

        this.connectedUsers = new ArrayList<>();
        connectedUsers.add(GUI.huskyConnectUser);
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

    public BorderPane generateCardOnDoubleExpression(DoubleExpression scrollPaneBinding) {
        BorderPane card = new BorderPane();
        Circle logoCircle = new Circle(1, 1, 1);
        if (icon != null && !icon.isError())
            logoCircle.setFill(new ImagePattern(icon));
        else
            logoCircle.setFill(new ImagePattern(GUI.loadImageResource("\\src\\main\\resources\\error-user-icon.png")));

        VBox imageBox = new VBox(logoCircle);

        Label name = GUI.scaleableText(username, guiInstance.rootPane.heightProperty(), card.widthProperty(), 25.);

        HBox area = new HBox(name);
        area.setAlignment(Pos.CENTER);
        Button profile = new Button("Profile");

        card.setLeft(imageBox);
        card.setRight(profile);
        card.setCenter(area);

        //styling
        logoCircle.radiusProperty().bind(guiInstance.rootPane.heightProperty().divide(18));
        profile.prefHeightProperty().bind(card.heightProperty());
        profile.prefWidthProperty().bind(card.widthProperty().divide(8));

        DoubleExpression userWidthBaseBind = guiInstance.rootPane.widthProperty().divide(2);

        card.prefWidthProperty().bind(userWidthBaseBind);
        card.prefHeightProperty().bind(guiInstance.rootPane.heightProperty().divide(9));

        // TO DO look into making profiles look distinct

        return card;
    }

    public VBox generateUserFeed() {
        VBox feed = new VBox();
        DoubleExpression userWidthBaseBind = guiInstance.rootPane.widthProperty().divide(2);

        // title the user feed
        Label userFeed = GUI.scaleableText("User feed", userWidthBaseBind, guiInstance.rootPane.heightProperty().multiply(2).divide(3).divide(25));
        VBox userList = new VBox(); // create a list of users in form of vbox

        // make it so the user list is a scrollable entity
        ScrollPane scrollPane = new ScrollPane(userList);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.fitToWidthProperty().setValue(true);

        // add all of the connected users to the user feed
        for (var user : connectedUsers) {
            userList.getChildren().add(user.generateCardOnDoubleExpression(scrollPane.widthProperty()));
        }

        // add the user feed and scrollpane to the feed
        feed.getChildren().addAll(userFeed, scrollPane);

        // set the max width and height properties to their respective heights, and the feed to be centered
        feed.maxWidthProperty().bind(userWidthBaseBind);
        feed.maxHeightProperty().bind(guiInstance.rootPane.heightProperty().multiply(2).divide(3));
        feed.setAlignment(Pos.CENTER);

        // set the spacing to default
        userList.setSpacing(GUI.DEFAULT_SPACING);

        return feed;
    }
}
