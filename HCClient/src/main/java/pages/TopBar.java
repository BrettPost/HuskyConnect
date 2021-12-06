package pages;


import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import userinterface.GUI;

import static pages.HomePage.loadHomePage;
import static pages.SearchPage.loadSearchPage;


public class TopBar {



    public static HBox createTopBar(GUI gui) {

        //Top Bar is broken up into two halves
        HBox leftTopBar = new HBox();   //left side: home button, search bar
        HBox rightTopBar = new HBox();  //right side: inbox, profile, exit

        Pane spacer = new Pane();   //spacer is used to make sure left side stays all the way left and right side all the way to the right

        HBox topBar = new HBox(leftTopBar, spacer, rightTopBar);
        HBox.setHgrow(spacer, Priority.ALWAYS);

        //Home Icon
        ImageView homeIconView = new ImageView(
                GUI.loadImageResource("\\src\\main\\resources\\home-icon3.0.png")
        );
        Label homeLbl = new Label("Home"); //home label
        VBox homeBox = new VBox(homeIconView, homeLbl);


        //Search Bar and Icon
        TextField searchInput = new TextField();
        searchInput.setPromptText("Search . . . .");
        ImageView searchIconView = new ImageView(
                GUI.loadImageResource("\\src\\main\\resources\\search-icon.png")
        );
        Button searchButton = new Button();
        searchButton.setGraphic(searchIconView);
        HBox searchBox = new HBox(searchInput, searchButton);


        //Circular profile icon
        //TODO: get user's profile picture
        Image userIcon = GUI.loadImageResource("\\src\\main\\resources\\default-user-icon.png");
        Circle userIconCircle = new Circle(1, 1, 1);
        userIconCircle.setFill(new ImagePattern(userIcon));
        Label profileLbl = new Label("Profile");
        VBox profileBox = new VBox(userIconCircle, profileLbl);


        //inbox icon
        ImageView inboxIconView = new ImageView(
                GUI.loadImageResource("\\src\\main\\resources\\inbox-icon.png")
        );
        Label inboxLbl = new Label("Inbox");
        VBox inboxBox = new VBox(inboxIconView, inboxLbl);

        //exit button
        Button exit = new Button ("Exit");
        VBox exitBox = new VBox(exit);


        //add all children to their respective HBox
        leftTopBar.getChildren().addAll(homeBox, searchBox);
        rightTopBar.getChildren().addAll(inboxBox, profileBox, exitBox);





        //STYLING

        //HOME STYLING
        homeIconView.preserveRatioProperty().setValue(true);
        homeIconView.fitHeightProperty().bind(topBar.heightProperty().multiply(5).divide(8)); //bind icon height proportional to top bar height
        homeBox.minWidthProperty().bind(homeLbl.widthProperty()); //box will be at least as large as the label
        homeLbl.setFont(new Font("Arial", 11));
        HBox.setMargin(homeBox, new Insets(3, 16, 3, 13));
        homeBox.setAlignment(Pos.CENTER);


        //SEARCH BAR STYLING
        searchInput.prefWidthProperty().bind(topBar.widthProperty()); //search field will grow with the window size
        searchIconView.preserveRatioProperty().setValue(true);
        searchIconView.fitHeightProperty().bind(searchInput.heightProperty());
        searchButton.maxHeightProperty().bind(searchInput.heightProperty());
        searchBox.maxWidthProperty().bind(topBar.widthProperty().multiply(5).divide(8)); //search bar width maxes out
        searchBox.setAlignment(Pos.CENTER);


        //INBOX STYLING
        inboxIconView.preserveRatioProperty().setValue(true);
        inboxIconView.fitHeightProperty().bind(topBar.heightProperty().divide(2)); //bind icon height proportional to top bar height
        inboxBox.minWidthProperty().bind(inboxLbl.widthProperty()); //box will be at least as large as the label
        inboxLbl.setFont(new Font("Arial", 11));
        HBox.setMargin(inboxBox, new Insets(3, 13, 3, 20));
        inboxBox.setAlignment(Pos.CENTER);


        //PROFILE PIC STYLING
        userIconCircle.radiusProperty().bind(topBar.heightProperty().multiply(5).divide(16)); //bind circle radius proportional to top bar height
        profileBox.minWidthProperty().bind(profileLbl.widthProperty()); //box will be at least as large as the label
        profileLbl.setFont(new Font("Arial", 11));
        HBox.setMargin(profileBox, new Insets(3, 13, 3, 13));
        profileBox.setAlignment(Pos.CENTER);


        //EXIT BUTTON STYLING
        exit.prefWidthProperty().bind(topBar.heightProperty().multiply(3).divide(4)); //button will always try to stay constant size
        exit.minWidthProperty().bind(topBar.heightProperty().multiply(3).divide(4));
        exitBox.setAlignment(Pos.CENTER);
        HBox.setMargin(exitBox, new Insets(3, 13, 3, 13));


        //TOP BAR STYLING
        topBar.setStyle("-fx-background-color: #F2E000;" +  //yellow background for top bar
                        "-fx-border-color: black;");        //black border for top bar
        topBar.setSpacing(10);
        topBar.setMaxHeight(18);
        topBar.setAlignment(Pos.TOP_LEFT);

        leftTopBar.setAlignment(Pos.CENTER_LEFT);
        rightTopBar.setAlignment(Pos.CENTER_RIGHT);

        topBar.prefWidthProperty().bind(gui.rootPane.widthProperty());



        //ACTIONS:

        //home icon
        homeBox.setOnMouseClicked( event -> {
            gui.rootPane.setCenter(loadHomePage(gui));
        });
        //search button
        searchButton.setOnAction( event -> {
            String search = searchInput.getText();
            searchInput.clear();
            gui.rootPane.setCenter(loadSearchPage(gui, search));
        });

        //inbox icon
        inboxBox.setOnMouseClicked( event -> {
            //TODO: Go To Inbox Page
            System.out.println("Inbox Icon Clicked");
        });

        //inbox icon
        profileBox.setOnMouseClicked( event -> {
            ProfilePage profilePage = gui.loginInstance.loggedInUser.getLinkedPage(gui);
            gui.rootPane.setCenter(profilePage.generatePage());
        });

        //exit button
        exit.setOnAction( event -> {
            Platform.exit();  //close application on exit button press
        });



        return topBar;
    }






}
