package pages;

import javafx.beans.binding.DoubleExpression;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.geometry.VPos;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;
import userinterface.GUI;


public class SearchPage {

    public static VBox loadSearchPage(GUI guiInstance, String search) {
        VBox searchPage = getSearchResults(guiInstance, search);

        // bind the search page to the correct height
        searchPage.prefWidthProperty().bind(guiInstance.rootPane.widthProperty().divide(2));
        searchPage.maxHeightProperty().bind(guiInstance.rootPane.heightProperty().multiply(2).divide(3));
        searchPage.setAlignment(Pos.TOP_CENTER);

        return searchPage;
    }

    /**
     * Creates a list (similar to user feed) of all the profile cards that match the search
     * NOT FULLY IMPLEMENTED: Need to get users from the database
     *
     * @param guiInstance The instance of the gui
     * @param search The string we need to search for
     * @return a feed of all profile cards that matched the search
     */
    private static VBox getSearchResults(GUI guiInstance, String search) {

        VBox results = new VBox();
        DoubleExpression userWidthBaseBind = guiInstance.rootPane.widthProperty().divide(2);

        // title the search results
        Label searchLbl = GUI.scaleableText("Search Results for '" + search + "'", userWidthBaseBind, guiInstance.rootPane.heightProperty().multiply(2).divide(125));
        VBox userList = new VBox(); // create a list of users in form of vbox

        // make it so the user list is a scrollable entity
        ScrollPane scrollPane = new ScrollPane(userList);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.fitToWidthProperty().setValue(true);

        //TODO: replace adding current user with retrieving and adding search results from database
        userList.getChildren().add(guiInstance.loginInstance.loggedInUser.generateCard(guiInstance));

        // add the user feed and scroll pane to the feed
        results.getChildren().addAll(searchLbl, scrollPane);

        // set the max width property to its respective heights, and the feed to be centered
        results.maxWidthProperty().bind(userWidthBaseBind);
        results.setAlignment(Pos.CENTER);

        // set the spacing to default
        userList.setSpacing(GUI.DEFAULT_SPACING);

        return results;

    }

}


