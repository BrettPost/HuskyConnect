package actors;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.ObjectMapper;
import databaseconnections.HttpCon;
import databaseconnections.HttpTag;
import databaseconnections.HttpUser;
import javafx.beans.binding.DoubleExpression;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import org.apache.http.HttpResponse;
import pages.ProfilePage;
import userinterface.GUI;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URI;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

public class User {
    private String username;
    private String email = "";
    private String full_name = "";
    private String bio = "";
    @JsonIgnore
    private HashSet<String> tags;

    private byte[] img_blob;
    @JsonIgnore
    public Image img;//javafx representation of imgBlob. imgBlob is the ultimate truth for this duplicate information.
    @JsonIgnore
    private List<User> connectedUsers;
    @JsonIgnore
    private ProfilePage linkedPage;

    /**
     * default constructor for jackson databind
     */
    public User(){

        this.tags = new HashSet<>();

        //TODO make this populated from database
        this.connectedUsers = new ArrayList<>();
    }

    /**
     * Create a user object
     * @param username the user's username
     * @param email the user's email
     * @param bio the user's bio
     * @param img_blob byte array of the img
     * @param tags the user's tags
     */
    public User(String username, String email, String bio, byte[] img_blob, String... tags) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.img_blob = img_blob;

        this.tags = new HashSet<>();
        this.tags.addAll(Arrays.asList(tags));

        this.connectedUsers = new ArrayList<>();

        generateImage();
    }

    /**
     * Create a user object
     * @param username the user's username
     * @param email the user's email
     * @param bio the user's bio
     * @param filePath path to a file for the img
     * @param tags the user's tags
     */
    public User(String username, String email, String full_name, String bio, String filePath, String... tags) {
        this.username = username;
        this.email = email;
        this.full_name = full_name;
        this.bio = bio;
        setImg(filePath);

        this.tags = new HashSet<>();
        this.tags.addAll(Arrays.asList(tags));

        this.connectedUsers = new ArrayList<>();
    }

    /**
     * makes a user by pulling information from the database
     * @param username username of user being grabbed
     * @param token authentication proof
     * @return user of the username provided. Returns null if http failed to get the user for any reason
     */
    public static User getUser(String username, Long token){
        try{
            HttpResponse response = HttpUser.getUser(username,token);

            //Tests to see if the status of the http was a success
            assert response != null;
            if(response.getStatusLine().getStatusCode() == 200){
                //maps the response to a user
                ObjectMapper mapper = new ObjectMapper();
                User user = mapper.readValue(response.getEntity().getContent(), User.class);

                //get tags for this user
                user.addTags(HttpTag.getUserTags(username,token).strip().split(","));

                return user;
            }else {
                //TODO separate out the different possible errors to display what went wrong to the user
                //Http failed in some way (could simply be invalid token or username)
                System.out.println("failed to get user from database");
                System.out.println(response.getStatusLine());
                return null;
            }

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }

    }


    public byte[] getImg_blob() {
        return img_blob;
    }

    public void setImg_blob(byte[] img_blob) {
        this.img_blob = img_blob;
    }

    public Image getImg() {
        if(img == null){
            img = generateImage();
        }
        return img;
    }

    /**
     * sets imgBlob and img from a file
     * @param filePath path to the file for the img
     */
    public void setImg(String filePath) {
        try {
            String fileType = filePath.substring(filePath.indexOf(".")+1);
            BufferedImage bImage = ImageIO.read(new File(new URI(filePath)));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(bImage,fileType,bos);
            img_blob = bos.toByteArray();

            this.img = new Image(filePath);
        } catch (Exception e) {
            //TODO set image to a fallback image if this failed
            e.printStackTrace();
        }


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

    public String getFull_name() {
        return full_name;
    }

    public void setFull_name(String full_name) {
        this.full_name = full_name;
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

    /**
     * Reads the blob into a javafx image
     * @return javafx image from the imgBlob
     */
    public Image generateImage(){
        try{
            System.out.println(img_blob);
            InputStream in = new ByteArrayInputStream(img_blob);
            BufferedImage image = ImageIO.read(in);
            return SwingFXUtils.toFXImage(image,null);
        }catch (Exception e){
            //TODO handle this with a img not found image
            e.printStackTrace();
            return null;
        }
    }

    /**
     * generates a card for this users profile
     * @param gui the gui this card will be added to
     * @return the card created
     */
    public BorderPane generateCard(GUI gui) {
        BorderPane card = new BorderPane();

        Circle logoCircle = new Circle(1, 1, 1);
        logoCircle.setFill(new ImagePattern(generateImage()));

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

    /**
     * Generates a user feed for this user
     * @param gui the gui this user feed will be added to
     * @return the user feed
     */
    //TODO make this user feed smarter and not just pull from connections
    public VBox generateUserFeed(GUI gui) {
        VBox feed = new VBox();
        DoubleExpression userWidthBaseBind = gui.rootPane.widthProperty().divide(2);

        // title the user feed
        Label userFeed = GUI.scaleableText("User feed", userWidthBaseBind, gui.rootPane.heightProperty().multiply(2).divide(3).divide(25));
        VBox userList = new VBox(); // create a list of users in form of vbox

        // make it so the user list is a scrollable entity
        ScrollPane scrollPane = new ScrollPane(userList);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.fitToWidthProperty().setValue(true);

        //TODO remove this, as self shouldn be in user feed, its for testing]
        userList.getChildren().add(this.generateCard(gui));

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
