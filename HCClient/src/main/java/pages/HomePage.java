package pages;

import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import userinterface.GUI;


public class HomePage {

    public static GridPane loadHomePage(GUI guiInstance) {
        // create a grid pane
        GridPane homePage = new GridPane();

        // set the first 2 rows to take up the relevant amount of space and add them to the homepage
        for (int i = 0; i < 2; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100f/8f);
            rowConst.setValignment(VPos.BOTTOM);
            homePage.getRowConstraints().add(rowConst);
        }

        // create a single column constraint, setting it so that it'll center anything inside of it
        ColumnConstraints ccConst = new ColumnConstraints();
        ccConst.setPercentWidth(100);
        ccConst.setHalignment(HPos.CENTER);
        homePage.getColumnConstraints().add(ccConst);

        // get the username of the logged in user and construct the welcome label to scale
        String user = guiInstance.loginInstance.loggedInUser.getUsername(); // retrieve this information from the database when the database is live
        Label welcomeLabel = GUI.scaleableText("Welcome to HuskyConnect, " + user, homePage.widthProperty(), homePage.heightProperty(), 50.);
        // place it in a box
        VBox labelBox = new VBox(welcomeLabel);
        labelBox.setAlignment(Pos.CENTER);

        // retrieve the user's feed
        VBox userFeed = guiInstance.loginInstance.loggedInUser.generateUserFeed(guiInstance);

        // add all three to the home page.
        // column 0, row 1 will set the welcome message to be 1/8 down the page
        homePage.add(labelBox, 0, 1);
        // column 0, row 2 will set the user feed to be 1/4 down the page
        homePage.add(userFeed, 0, 2);

        // bind the home page and user feed to the correct height
        homePage.prefWidthProperty().bind(guiInstance.rootPane.widthProperty().divide(2));
        userFeed.maxHeightProperty().bind(guiInstance.rootPane.heightProperty().multiply(2).divide(3));
        labelBox.prefWidthProperty().bind(guiInstance.rootPane.widthProperty());

        return homePage;
    }
}
