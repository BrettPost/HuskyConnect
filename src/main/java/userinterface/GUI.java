package userinterface;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.io.File;
import java.net.URISyntaxException;


public class GUI extends Application {
    public BorderPane rootPane;

    private static final int DEFAULT_SPACING = 10;

    public static void start() {
        launch();
    }
    /**
     * The main entry point for all JavaFX applications.
     * The start method is called after the init method has returned,
     * and after the system is ready for the application to begin running.
     *
     * <p>
     * NOTE: This method is called on the JavaFX Application Thread.
     * </p>
     *
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     * @throws Exception if something goes wrong
     */
    @Override
    public void start(Stage primaryStage) throws Exception {
        Scene scene = new Scene(createRootPane());
        primaryStage.setScene(scene);
        primaryStage.setTitle("Husky Connect");
        primaryStage.show();
    }

    /**
     * Creates the main pane, which everything else is put in
     * @return the root pane
     */
    private Pane createRootPane() {
        rootPane = new BorderPane();
        rootPane.setPrefSize(900, 600);
        rootPane.setPadding(new Insets(DEFAULT_SPACING));

        VBox loginPage = createLoginPage();

        // set the landing page to the login page
        rootPane.setCenter(loginPage);

        return rootPane;
    }

    /**
     * Create the main login page for the application
     * @return the VBox containing the login page
     */
    private VBox createLoginPage() {
        VBox loginPage = new VBox();

        // none of the other ways to get relative file path functioned with the JavaFX image class, so we've got this
        String root = System.getProperty("user.dir"); // retrieve the computer specific file path
        String filepath = "\\src\\main\\resources\\temp-logo.png"; // the filepath to the resource
        File file = new File(root + filepath); // the file to pull the image from

        // create the image and place it into a view
        Image logo = new Image(file.toURI().toString());
        ImageView logoView = new ImageView();
        logoView.setImage(logo);
        logoView.setCache(true); // cache it for quicker loading time

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
        loginPage.setSpacing(DEFAULT_SPACING);
        userPassContainer.setSpacing(DEFAULT_SPACING);

        // Set the max width of the login page to 1/2 of the root pane, so that it will scale with
        // the window and also be centered.
        loginPage.maxWidthProperty().bind(rootPane.widthProperty().divide(2));
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
        // lock the imagebox width onto the logo
        imageBox.maxWidthProperty().bind(logoView.fitWidthProperty());


        // BUTTON FUNCTIONALITY
        logInButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // TODO
            }
        });

        signUpButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

            }
        });

        return loginPage;
    }

    // PRIVATE DEVELOPMENT HELPER METHODS
    /**
     * Highlight the borders of a pane. Used for debugging why certain visual elements may not be working
     * @param element the element to outline
     * @param color the color to outline it in
     */
    private void HELP_HIGHLIGHT_PANE(Pane element, Color color) {
        element.setBorder(
                new Border(
                        new BorderStroke(
                                color,
                                BorderStrokeStyle.SOLID,
                                CornerRadii.EMPTY,
                                BorderWidths.DEFAULT)
                )
        );

    }
}
