package userinterface;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class GUI extends Application {
    public BorderPane rootPane;

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

    private Pane createRootPane() {
        rootPane = new BorderPane();
        return rootPane;
    }
}
