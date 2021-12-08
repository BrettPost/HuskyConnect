package pages;

import actors.User;
import javafx.beans.binding.DoubleExpression;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import userinterface.GUI;

public class NotificationPage {
    public static GridPane NotificationPage(GUI guiInstance) {
        GridPane notificationPage = new GridPane();

        // set the first 2 rows to take up the relevant amount of space and add them to the homepage
        for (int i = 0; i < 2; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100f/8f);
            rowConst.setValignment(VPos.BOTTOM);
            notificationPage.getRowConstraints().add(rowConst);
        }

        // create a single column constraint, setting it so that it'll center anything inside of it
        ColumnConstraints ccConst = new ColumnConstraints();
        ccConst.setPercentWidth(100);
        ccConst.setHalignment(HPos.CENTER);
        notificationPage.getColumnConstraints().add(ccConst);

        Label welcomeLabel = GUI.scaleableText("Notifications", notificationPage.widthProperty(), notificationPage.heightProperty(), 50.);
        // place it in a box
        VBox labelBox = new VBox(welcomeLabel);
        labelBox.setAlignment(Pos.CENTER);

        // retrieve the notification feed
        VBox notifFeed = notificationFeed(guiInstance, guiInstance.loginInstance.loggedInUser);

        // add both to the home page.
        // column 0, row 1 will set the welcome message to be 1/8 down the page
        notificationPage.add(labelBox, 0, 1);
        // column 0, row 2 will set the notif feed to be 1/4 down the page
        notificationPage.add(notifFeed, 0, 2);

        // bind the notif page and notif feed to the correct height
        notificationPage.prefWidthProperty().bind(guiInstance.rootPane.widthProperty().divide(2));
        notifFeed.maxHeightProperty().bind(guiInstance.rootPane.heightProperty().multiply(2).divide(3));
        labelBox.prefWidthProperty().bind(guiInstance.rootPane.widthProperty());

        return notificationPage;
    }

    public static BorderPane generateNotification(User actionUser, User friendUser, GUI gui) {
        BorderPane notif = new BorderPane();
        Circle logoCircle = new Circle(1, 1, 1);
        logoCircle.setFill(new ImagePattern(friendUser.generateImage()));

        VBox imageBox = new VBox(logoCircle);

        Label text = GUI.scaleableText(friendUser.getUsername() + " is requesting to connect with you!", gui.rootPane.heightProperty(), notif.widthProperty(), 12.5);
        HBox textBox = new HBox(text);
        Button accept = new Button("Accept");
        Button deny = new Button("Deny");
        VBox acceptDeny = new VBox(accept, deny);

        notif.setLeft(imageBox);
        notif.setCenter(textBox);
        notif.setRight(acceptDeny);

        logoCircle.radiusProperty().bind(gui.rootPane.heightProperty().divide(18));
        DoubleExpression userWidthBaseBind = gui.rootPane.widthProperty().divide(2);

        notif.prefWidthProperty().bind(userWidthBaseBind);
        notif.prefHeightProperty().bind(gui.rootPane.heightProperty().divide(9));

        acceptDeny.prefHeightProperty().bind(notif.heightProperty());
        acceptDeny.prefWidthProperty().bind(notif.widthProperty().divide(6));
        accept.prefHeightProperty().bind(acceptDeny.heightProperty().divide(2));
        deny.prefHeightProperty().bind(acceptDeny.heightProperty().divide(2));
        accept.prefWidthProperty().bind(acceptDeny.widthProperty());
        deny.prefWidthProperty().bind(acceptDeny.widthProperty());

        accept.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notif.setVisible(false);
                actionUser.notifications.remove(this);
                actionUser.addConnection(friendUser);
            }
        });

        deny.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                notif.setVisible(false);
                actionUser.notifications.remove(this);
            }
        });


        return notif;
    }

    public static VBox notificationFeed(GUI gui, User actionUser) {
        VBox feed = new VBox();
        DoubleExpression userWidthBaseBind = gui.rootPane.widthProperty().divide(2);

        // title the user feed
        Label notifFeed = GUI.scaleableText("Notification feed", userWidthBaseBind, gui.rootPane.heightProperty().multiply(2).divide(3).divide(25));
        VBox notifList = new VBox(); // create a list of users in form of vbox

        // make it so the user list is a scrollable entity
        ScrollPane scrollPane = new ScrollPane(notifList);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.fitToWidthProperty().setValue(true);

        //TODO remove this, as self is only added for testing
        notifList.getChildren().add(generateNotification(gui.loginInstance.loggedInUser, gui.loginInstance.loggedInUser, gui));
        // add the user feed and scroll pane to the feed
        feed.getChildren().addAll(notifFeed, scrollPane);

        // set the max width and height properties to their respective heights, and the feed to be centered
        feed.maxWidthProperty().bind(userWidthBaseBind);
        feed.maxHeightProperty().bind(gui.rootPane.heightProperty().multiply(2).divide(3));
        feed.setAlignment(Pos.CENTER);

        // set the spacing to default
        notifList.setSpacing(GUI.DEFAULT_SPACING);

        return feed;
    }
}
