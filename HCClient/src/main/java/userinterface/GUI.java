package userinterface;

import actors.User;
import instances.LoginInstance;
import javafx.application.Application;
import javafx.beans.binding.DoubleExpression;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class GUI extends Application {
    public BorderPane rootPane;

    public static User huskyConnectUser = null;
    public LoginInstance loginInstance = null;

    public static final int DEFAULT_SPACING = 10;

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
     */
    @Override
    public void start(Stage primaryStage) {
        huskyConnectUser =  new User("HuskyConnect", "hc", "The official HuskyConnect account!", loadImageResource("\\src\\main\\resources\\husky-connect-user-img.jpg"), this,"huskyconnect", "mtu", "huskies");
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

        loginInstance = new LoginInstance(this);
        Pane loginPage = loginInstance.createLoginPage();

        // set the landing page to the login page
        rootPane.setCenter(loginPage);

        return rootPane;
    }

    // PRIVATE DEVELOPMENT HELPER METHODS
    /**
     * Get an image resource from the image located in the filepath.
     * Filepath must point to a valid resource.
     * @param filePath filepath relative to directory. Should be of type
     *                 .png, .gif, or other supported by JavaFX Image.
     * @return the ImageView of the image located at the filepath
     */
    public static Image loadImageResource(String filePath) {
        // none of the other ways to get relative file path functioned with the JavaFX image class, so we've got this
        String root = System.getProperty("user.dir"); // retrieve the computer specific file path

        return new Image("file:" + root + filePath); // retrieve image
    }

    public static Label scaleableText(String content, DoubleExpression width, DoubleExpression height, Double textScale) {
        Label title = new Label(content);

        // align the title to the center of the box it's in
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);

        // bind the title to the proper width for the list
        title.prefWidthProperty().bind(width);
        title.prefHeightProperty().bind(height);

        // create a double property for the text size with a default of 20
        DoubleProperty textSize = new SimpleDoubleProperty(20);

        // set the font to the size of the property
        title.setFont(Font.font(textSize.doubleValue()));

        // scale factor of 10 to the width because it's not a perfect scale
        textSize.bind(width.divide(textScale));

        // listen for the textsize property changing and update the size of the text when it does
        textSize.addListener((observable, oldValue, newValue) -> title.setFont(Font.font(textSize.doubleValue())));

        return title;
    }
        /**
         * Create a label that scales along with the width and height
         * @param content the content you want the label to hold
         * @param width the width you want to scale the label with
         * @param height the height you want to scale the label with
         * @return a label that will scale along with the provided width and height
         */
    public static Label scaleableText(String content, DoubleExpression width, DoubleExpression height) {
        Label title = new Label(content);

        // align the title to the center of the box it's in
        title.setTextAlignment(TextAlignment.CENTER);
        title.setAlignment(Pos.CENTER);

        // bind the title to the proper width for the list
        title.prefWidthProperty().bind(width);
        title.prefHeightProperty().bind(height);

        // create a double property for the text size with a default of 20
        DoubleProperty textSize = new SimpleDoubleProperty(20);

        // set the font to the size of the property
        title.setFont(Font.font(textSize.doubleValue()));

        // scale factor of 10 to the width because it's not a perfect scale
        textSize.bind(width.divide(10));

        // listen for the textsize property changing and update the size of the text when it does
        textSize.addListener((observable, oldValue, newValue) -> title.setFont(Font.font(textSize.doubleValue())));

        return title;
    }

    /**
     * Highlight the borders of a pane. Used for debugging why certain visual elements may not be working
     * @param element the element to outline
     * @param color the color to outline it in
     */
    public static void HELP_HIGHLIGHT_PANE(Pane element, Color color) {
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
