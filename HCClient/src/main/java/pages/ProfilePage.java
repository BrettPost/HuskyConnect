package pages;

import actors.User;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import userinterface.GUI;

public class ProfilePage {
    private User linkedUser;
    private GUI gui;

    TextField nameField = new TextField();
    TextField tags = new TextField();
    TextField description = new TextField();
    Circle icon = new Circle(1, 1, 1);

    VBox userFeed;


    public ProfilePage(User linkedUser, GUI gui) {
        this.linkedUser = linkedUser;
        this.gui = gui;
        nameField.setText(linkedUser.getUsername());

        String tagsText = "";
        for (var tag : linkedUser.getTags()) {
            tagsText += "#" + tag + ", ";
        }

        if(tagsText.length() > 2){
            tags.setText(tagsText.substring(0, tagsText.length()-2));
        }else{
            tags.setText("");
        }

        description.setText(linkedUser.getBio());
        userFeed = linkedUser.generateUserFeed();
        icon.setFill(new ImagePattern(linkedUser.getIcon()));

        tags.setEditable(false);
        description.setEditable(false);
        nameField.setEditable(false);
    }

    public HBox generatePage() {
        // DEFINITIONS
        HBox page = new HBox();


        Button editButton = new Button("Edit");
        VBox buttonBox = new VBox(editButton);
        VBox logoBox = new VBox(icon, buttonBox);
        buttonBox.setAlignment(Pos.CENTER);
        logoBox.setSpacing(GUI.DEFAULT_SPACING);

        Label userNameText = new Label("Username");
        Label tagsText = new Label("Tags");
        Label bioText = new Label("Bio");

        VBox userBox = new VBox(userNameText, nameField);
        VBox tagsBox = new VBox(tagsText, tags);
        VBox bioBox = new VBox(bioText, description);
        VBox textFieldsBox = new VBox(userBox, tagsBox, bioBox, userFeed);

        page.getChildren().addAll(logoBox, textFieldsBox);

        //STYLING COMPONENTS
        icon.radiusProperty().bind(logoBox.prefWidthProperty().divide(2));
        logoBox.prefWidthProperty().bind(gui.rootPane.widthProperty().divide(10));

        // set the spacing for the required panes to default
        page.setSpacing(GUI.DEFAULT_SPACING);
        textFieldsBox.setSpacing(GUI.DEFAULT_SPACING);

        // allocate the correct amount of room to the panes
        page.maxWidthProperty().bind(gui.rootPane.widthProperty());
        page.maxHeightProperty().bind(bioBox.maxHeightProperty().add(tagsBox.maxHeightProperty()));

        page.setAlignment(Pos.CENTER);
        textFieldsBox.setAlignment(Pos.CENTER);


        textFieldsBox.prefWidthProperty().bind(page.widthProperty().divide(3));
        logoBox.prefWidthProperty().bind(page.widthProperty().divide(6));

        description.minHeightProperty().bind(gui.rootPane.prefHeightProperty().divide(8));
        tags.minHeightProperty().bind(gui.rootPane.prefHeightProperty().divide(8));

        userFeed.minHeightProperty().bind(gui.rootPane.heightProperty().divide(4));


        return page;
    }

}
