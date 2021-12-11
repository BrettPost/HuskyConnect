package pages;

import actors.User;
import databaseconnections.HttpUser;
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

        // gets the search results from users in the database
        User[] users = HttpUser.getUsers();
        if(users != null)
            for (User user :
                    users) {
                if(stringCompare(user.getUsername(),search) <= 2 ){
                    ProfilePage profilePage = user.getLinkedPage(guiInstance);
                    userList.getChildren().add(user.generateCard(guiInstance));
                }
            }

        // add the user feed and scroll pane to the feed
        results.getChildren().addAll(searchLbl, scrollPane);

        // set the max width property to its respective heights, and the feed to be centered
        results.maxWidthProperty().bind(userWidthBaseBind);
        results.setAlignment(Pos.CENTER);

        // set the spacing to default
        userList.setSpacing(GUI.DEFAULT_SPACING);

        return results;

    }

    /**
     * returns the number of chars that are different between the 2 strings
     * @param str1 first string
     * @param str2 second string
     * @return int for the number of chars that are different
     */
    private static int stringCompare(String str1, String str2)
    {

        int l1 = str1.length();
        int l2 = str2.length();
        int lmin = Math.min(l1, l2);
        int lmax = Math.max(l1, l2);

        int count = 0;
        for (int i = 0; i < lmin; i++) {
            int str1_ch = (int)str1.charAt(i);
            int str2_ch = (int)str2.charAt(i);

            if (str1_ch != str2_ch) {
                count++;
            }
        }
        count += lmax - lmin;
        return count;
    }

}


