package instances;

import actors.User;
import databaseconnections.HttpCon;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.http.HttpResponse;
import pages.SignUpPage;
import userinterface.GUI;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import static pages.HomePage.loadHomePage;
import static userinterface.GUI.loadImageResource;

public class LoginInstance {
    public User loggedInUser = null;
    GUI gui;

    public Long token = null;


    public LoginInstance(GUI gui) {
        this.gui = gui;
    }

    /**
     * attempts to login, and store the token in this class
     * @param username username of user
     * @param password password of user
     * @return true if login success, false if login failed
     */
    boolean login(String username, String password){

        try{
            HttpResponse response = HttpCon.login(username,password);

            //Tests to see if the status of the http was a success
            assert response != null;
            if(response.getStatusLine().getStatusCode() == 200){

                //reads the content of the http response
                var bufReader = new BufferedReader(new InputStreamReader(
                        response.getEntity().getContent()));

                var builder = new StringBuilder();

                String line;

                while ((line = bufReader.readLine()) != null) {

                    builder.append(line);
                    builder.append(System.lineSeparator());
                }

                token = Long.parseLong( builder.toString().strip());
                return true;
            }else {
                //Http failed in some way (could simply be incorrect username/password)
                System.out.println("incorrect username or password");
                return false;
            }

        }catch (Exception e){
            return false;
        }

    }

    /**
     * Create the main login page for the application
     * @return the VBox containing the login page
     */
    public VBox createLoginPage() {
        VBox loginPage = new VBox();

        // create the image and place it into a view
        ImageView logoView = new ImageView(
                loadImageResource("\\src\\main\\resources\\temp-logo.png")
        );

        VBox imageBox = new VBox(logoView);

        // text fields for user data entry
        Label usernameText = new Label("Username");
        TextField username = new TextField();
        username.setPromptText("username");
        VBox usernameBox = new VBox(usernameText, username);

        Label passwordText = new Label("Password");
        PasswordField password = new PasswordField();
        password.setPromptText("password");
        VBox passwordBox = new VBox(passwordText, password);

        VBox userPassContainer = new VBox(usernameBox, passwordBox);

        // Buttons defining behavior
        Button logInButton = new Button("Log in");
        Button signUpButton = new Button ("Sign up");

        HBox buttonContainer = new HBox(logInButton, signUpButton);

        // add both to the main log in page
        loginPage.getChildren().addAll(logoView, userPassContainer, buttonContainer);

        // add the correct spacing for all three pages, so all elements will be affected
        loginPage.setSpacing(GUI.DEFAULT_SPACING);
        userPassContainer.setSpacing(GUI.DEFAULT_SPACING);

        // Set the max width of the login page to 1/2 of the root pane, so that it will scale with
        // the window and also be centered.
        loginPage.maxWidthProperty().bind(gui.rootPane.widthProperty().divide(2));
        // Set the max height property to the button container and user-pass container
        // so that it does not automatically bind its height to the entirety of the page
        loginPage.maxHeightProperty().bind(userPassContainer.heightProperty().add(buttonContainer.heightProperty()));

        // set the button container's max width property to be half of the login page, so the spacing
        // is roughly equivalent to what was described in the figma board
        buttonContainer.maxWidthProperty().bind(loginPage.widthProperty().divide(2));
        loginPage.setAlignment(Pos.CENTER); // align the login page and all items within it to the center
        // set each button to about a third of the button container's width, RE figma and general aesthetic
        logInButton.minWidthProperty().bind(buttonContainer.widthProperty().divide(3));
        signUpButton.minWidthProperty().bind(buttonContainer.widthProperty().divide(3));
        // set the spacing to this width as well
        buttonContainer.spacingProperty().bind(loginPage.widthProperty().divide(6));

        // set the logo to the login page width and preserve the ratio
        logoView.preserveRatioProperty().setValue(true);
        logoView.fitWidthProperty().bind(buttonContainer.widthProperty());
        // lock the image box width onto the logo
        imageBox.maxWidthProperty().bind(logoView.fitWidthProperty());


        // BUTTON FUNCTIONALITY
        logInButton.setOnAction(event -> {

            String usernameStr = username.getText();
            String passwordStr = password.getText();

            if (usernameStr.isEmpty() || passwordStr.isEmpty()) {
                //TODO: Print message to page saying user must enter username/password
                System.out.println("empty username/password field");
            }
            else if (!login(usernameStr,passwordStr)) {
                //TODO: Print message to page saying invalid username/password
                System.out.println("invalid username/password");
            }
            else {

                loggedInUser = User.getUser(usernameStr,token);
                if(loggedInUser == null){
                    new Exception("failed to get logged in user").printStackTrace();
                }else{
                    System.out.println("Successful Login!");
                }

                gui.rootPane.setCenter(loadHomePage(gui));

            }

        });

        signUpButton.setOnAction(event -> gui.rootPane.setCenter(SignUpPage.createSignUpPageAccountDetail(gui, null, null)));

        return loginPage;
    }
}
