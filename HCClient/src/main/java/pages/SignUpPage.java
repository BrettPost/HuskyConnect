package pages;

import actors.User;
import databaseconnections.HttpCon;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import userinterface.GUI;

import javax.swing.*;
import java.io.File;
import java.util.Arrays;

import static pages.HomePage.loadHomePage;

public class SignUpPage {
    public static VBox createSignUpPageAccountDetail(GUI gui, String usernameFill, String emailFill) {
        VBox signUpPage = new VBox();

        ImageView logo = new ImageView(
                GUI.loadImageResource("\\src\\main\\resources\\temp-logo.png")
        );

        VBox imageBox = new VBox(logo);

        // text fields for user data entry
        Label emailText = new Label("Email");
        TextField email;
        if (emailFill != null) {
            email = new TextField(emailFill);
        } else {
            email = new TextField();
            email.setPromptText("email");

        }
        VBox emailBox = new VBox(emailText, email);

        TextField username;
        if (usernameFill != null) {
            username = new TextField(usernameFill);
        } else {
            username = new TextField();
            username.setPromptText("username");
        }
        Label usernameText = new Label("Username");
        VBox usernameBox = new VBox(usernameText, username);

        Label passwordText = new Label("Password");
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        VBox passwordBox = new VBox(passwordText, password);

        Label confirmText = new Label("Confirm Password");
        PasswordField passwordConfirm = new PasswordField();
        passwordConfirm.setPromptText("confirm password");
        VBox confirmBox = new VBox(confirmText, passwordConfirm);

        VBox fieldContainers = new VBox(emailBox, usernameBox, passwordBox, confirmBox);

        Button back = new Button ("Back");
        Button next = new Button("Next");
        HBox buttonBox = new HBox(back, next);

        signUpPage.getChildren().addAll(logo, fieldContainers, buttonBox);

        // STYLING COMPONENTS
        signUpPage.setSpacing(GUI.DEFAULT_SPACING);
        fieldContainers.setSpacing(GUI.DEFAULT_SPACING);

        signUpPage.maxWidthProperty().bind(gui.rootPane.widthProperty().divide(2));
        signUpPage.maxHeightProperty().bind(fieldContainers.heightProperty().add(next.heightProperty()));

        buttonBox.maxWidthProperty().bind(signUpPage.widthProperty().divide(2));
        next.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        back.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        buttonBox.spacingProperty().bind(signUpPage.widthProperty().divide(6));
        signUpPage.setAlignment(Pos.CENTER);

        logo.preserveRatioProperty().setValue(true);
        logo.fitWidthProperty().bind(next.widthProperty().multiply(3));
        imageBox.maxWidthProperty().bind(logo.fitWidthProperty());

        // BUTTON FUNCTIONALITY
        next.setOnAction(event -> gui.rootPane.setCenter(createSignUpPageProfileDetail(gui, username.getText(), email.getText(), password)));

        back.setOnAction(event -> gui.rootPane.setCenter(gui.loginInstance.createLoginPage()));
        return signUpPage;
    }


    private static HBox createSignUpPageProfileDetail(GUI gui, String username, String email, PasswordField password) {
        HBox profileDetails = new HBox();

        Image userIcon = GUI.loadImageResource("\\src\\main\\resources\\upload-user-icon.png");

        Circle clip = new Circle(1, 1, 1);

        ImageView holding = new ImageView(
                GUI.loadImageResource("\\src\\main\\resources\\default-user-icon.png")
        ); // hold the image so it can be modified within a lambda

        clip.setFill(new ImagePattern(userIcon));
        VBox logoBox = new VBox(clip);

        //text fields for user data entry
        VBox textFields = new VBox();

        Label tagsText = new Label("What're you interested in?");
        TextField tags = new TextField();
        tags.setPromptText("Prefix your tags with a # and start typing.");
        // TextFields.bindAutoCompletion(tags, "#computer science", "#huskies", "#TSP");

        VBox tagsBox = new VBox(tagsText, tags);
        textFields.getChildren().add(tagsBox);

        Label bioText = new Label("Bio");
        TextField bio = new TextField();
        bio.setPromptText("Tell us about yourself");

        VBox bioBox = new VBox(bioText, bio);
        textFields.getChildren().add(bioBox);

        Button signUp = new Button("Sign up!");
        Button back = new Button("Back");
        HBox buttonBox = new HBox(back, signUp);
        textFields.getChildren().add(buttonBox);
        profileDetails.getChildren().addAll(logoBox, textFields);

        // STYLING COMPONENTS
        // ensure the clip circle does not grow excessively large
        clip.radiusProperty().bind(logoBox.prefWidthProperty().divide(2));
        logoBox.prefWidthProperty().bind(gui.rootPane.widthProperty().divide(10));

        // set the spacing for the required panes to default
        profileDetails.setSpacing(GUI.DEFAULT_SPACING);
        textFields.setSpacing(GUI.DEFAULT_SPACING);
        buttonBox.setSpacing(GUI.DEFAULT_SPACING);

        // allocate the correct amount of room to the panes
        profileDetails.maxWidthProperty().bind(gui.rootPane.widthProperty());
        profileDetails.maxHeightProperty().bind(bioBox.maxHeightProperty().add(tagsBox.maxHeightProperty()));

        profileDetails.setAlignment(Pos.CENTER);
        textFields.setAlignment(Pos.CENTER);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.maxWidthProperty().bind(textFields.widthProperty());

        signUp.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        back.minWidthProperty().bind(buttonBox.widthProperty().divide(3));
        buttonBox.spacingProperty().bind(textFields.widthProperty().divide(6));

        textFields.prefWidthProperty().bind(profileDetails.widthProperty().divide(3));
        logoBox.prefWidthProperty().bind(profileDetails.widthProperty().divide(6));

        bio.minHeightProperty().bind(gui.rootPane.prefHeightProperty().divide(8));
        tags.minHeightProperty().bind(gui.rootPane.prefHeightProperty().divide(8));

        // BUTTON FUNCTIONALITY
        // having problems with set on mouse clicked timing -- double click treated as two clicks
        logoBox.setOnMouseClicked(event -> {
            // create a file selection dialog.
            // (This is a swing element, because the JavaFX one doesn't fit the needs of the UI)
            JFileChooser fileChooser = new JFileChooser();
            Image error = GUI.loadImageResource("\\src\\main\\resources\\error-user-icon.png");
            // if the file chooser correctly opens
            if (fileChooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
                File file = fileChooser.getSelectedFile(); // retrieve the user selected file
                Image logo = new Image(file.toURI().toString()); // turn it into an image

                // ensure that the file was properly turned into an image (acceptable file format)
                if (logo.isError()) {
                    // if it wasn't, show the error image logo
                    clip.setFill(new ImagePattern(error));
                } else {
                    // if it was, show the file they selected as the icon
                    holding.setImage(logo);
                    clip.setFill(new ImagePattern(logo));
                }
            } else {
                // if the file chooser did not open, show the error image
                clip.setFill(new ImagePattern(error));
            }
        });

        back.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                gui.rootPane.setCenter(createSignUpPageAccountDetail(gui, username, email));
            }
        });
        signUp.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //TODO

                String[] tagsResponse = tags.getText().split(",");
                Arrays.stream(tagsResponse).forEach(e -> e.strip());
                System.out.println(holding.getImage().getUrl());
                User newUser = new User(
                        username,
                        email,
                        bio.getText(),
                        holding.getImage(),
                        gui,
                        tagsResponse);

                //Stay on the current page if the user couldn't sign up for any reason
                if(HttpCon.saveUser(newUser, password.getText())) {
                    gui.loginInstance.loggedInUser = newUser;
                    gui.rootPane.setCenter(loadHomePage(gui));
                }
            }
        });


        return profileDetails;
    }
}
