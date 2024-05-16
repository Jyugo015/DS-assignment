/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class AdventurerDiaryController implements Initializable {

    private AdventurerDiary diary;

    @FXML
    private TextField searchBar;

    @FXML
    private Button searchButton;

    @FXML
    private VBox entriesContainer;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.println("Initializing controller...");
        diary = new AdventurerDiary();
        searchButton.setOnAction(event -> searchEvent());

        // Add some sample entries
        diary.logEvent("Player joined the game");
        diary.logEvent("Achievement earned: DIAMONDS!");
        diary.logEvent("Discovered a village");
        diary.logEvent("Completed the Ender Dragon challenge");
        displayEntries();
    }

    private void searchEvent() {
        String searchText = searchBar.getText();
        if (!searchText.isEmpty()) {
            // Clear previous search results
            entriesContainer.getChildren().clear();

            // Display entries that match the search text
            for (Map.Entry<Integer, String> entry : diary.getEntries().entrySet()) {
                if (entry.getValue().toLowerCase().contains(searchText.toLowerCase())) {
                    addEntryToContainer(entry.getKey(), entry.getValue());
                }
            }
        }
    }

    private void addEntryToContainer(int entryId, String entryText) {
        HBox entryBox = new HBox(10);
        entryBox.setStyle("-fx-padding: 10; -fx-border-color: lightgrey; -fx-border-width: 1; -fx-border-radius: 5;");
        VBox detailsBox = new VBox(5);
        detailsBox.setStyle("-fx-padding: 5;");

        Label timestamp = new Label("Timestamp: " + entryText.split(" - ")[0]);
        timestamp.setStyle("-fx-font-weight: bold;");
        Label description = new Label("Description: " + entryText.split(" - ")[1]);

        detailsBox.getChildren().addAll(timestamp, description);

        VBox buttonsBox = new VBox(5);
        buttonsBox.setStyle("-fx-padding: 5;");

        Button viewDetailsButton = new Button("View Details");
        Button editEntryButton = new Button("Edit Entry");
        Button deleteEntryButton = new Button("Delete Entry");
        Button shareEntryButton = new Button("Share Entry");

        // Set actions for buttons
        viewDetailsButton.setOnAction(event -> viewDetails(entryId));
        editEntryButton.setOnAction(event -> editEntry(entryId));
        deleteEntryButton.setOnAction(event -> deleteEntry(entryId));
        shareEntryButton.setOnAction(event -> shareEntry(entryId));

        buttonsBox.getChildren().addAll(viewDetailsButton, editEntryButton, deleteEntryButton, shareEntryButton);

        entryBox.getChildren().addAll(detailsBox, buttonsBox);

        entriesContainer.getChildren().add(entryBox);
    }

    private void viewDetails(int entryId) {
        String entryText = diary.getEntries().get(entryId);
        if (entryText != null) {
            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("View Details");

            // Set the content
            Label detailsLabel = new Label(entryText);

            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().add(detailsLabel);

            dialog.getDialogPane().setContent(dialogVbox);

            // Add OK button
            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().add(okButtonType);

            dialog.showAndWait();
        } else {
            System.out.println("Invalid entry ID.");
        }
    }

    private void editEntry(int entryId) {
        String entryText = diary.getEntries().get(entryId);
        if (entryText != null) {
            String timestamp = entryText.split(" - ")[0];
            String description = entryText.split(" - ")[1];

            Dialog<String> dialog = new Dialog<>();
            dialog.setTitle("Edit Entry");

            // Create text area for editing the description
            TextArea descriptionTextArea = new TextArea(description);

            VBox dialogVbox = new VBox(20);
            dialogVbox.getChildren().addAll(
                    new Label("Timestamp: " + timestamp),
                    new Label("Description:"),
                    descriptionTextArea
            );

            dialog.getDialogPane().setContent(dialogVbox);

            // Add OK and Cancel buttons
            ButtonType okButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
            ButtonType cancelButtonType = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            dialog.getDialogPane().getButtonTypes().addAll(okButtonType, cancelButtonType);

            // Convert the result to a string when OK is clicked
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButtonType) {
                    return descriptionTextArea.getText();
                }
                return null;
            });

            // Show dialog and capture the result
            Optional<String> result = dialog.showAndWait();
            result.ifPresent(newDescription -> {
                // Update the entry with the new description
                diary.getEntries().put(entryId, timestamp + " - " + newDescription);
                displayEntries(); // Refresh the displayed entries
            });
        } else {
            System.out.println("Invalid entry ID.");
        }
    }

    private void deleteEntry(int entryId) {
        diary.getEntries().remove(entryId);
        displayEntries();
    }

    private void shareEntry(int entryId) {
        String sharedEvent = diary.shareEntry(entryId);
        System.out.println("Shared Entry: " + sharedEvent);
    }

    private void displayEntries() {
        entriesContainer.getChildren().clear();
        for (Map.Entry<Integer, String> entry : diary.getEntries().entrySet()) {
            addEntryToContainer(entry.getKey(), entry.getValue());
        }
    }
}
