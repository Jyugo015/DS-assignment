/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;
import javafx.scene.control.Button;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class CreatureEncyclopediaController implements Initializable {

    /**
     * Initializes the controller class.
     */
    @FXML
    private ComboBox<String> filterComboBox;
    @FXML
    private Button addNoteButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        filterComboBox.getItems().addAll("Type", "A-Z", "Z-A");
    }

    private void handleFilterChange(ActionEvent event) {
        String selectedFilter = filterComboBox.getSelectionModel().getSelectedItem();
        switch (selectedFilter) {
            case "Type":
                // Implement your type filtering logic
                break;
            case "A-Z":
                // Implement sorting logic from A to Z
                break;
            case "Z-A":
                // Implement sorting logic from Z to A
                break;
        }
    }

    @FXML
    private void onAddNoteClicked() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION, "Do you want to add a note?", ButtonType.YES, ButtonType.NO);
        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.YES) {
            TextInputDialog noteDialog = new TextInputDialog();
            noteDialog.setTitle("Add Note");
            noteDialog.setHeaderText("Enter the name of the creature:");
            noteDialog.setContentText("Creature name:");

            Optional<String> creatureName = noteDialog.showAndWait();
            creatureName.ifPresent(name -> {
                TextInputDialog noteContentDialog = new TextInputDialog();
                noteContentDialog.setTitle("Note for " + name);
                noteContentDialog.setHeaderText("Enter your note for " + name + ":");
                noteContentDialog.setContentText("Note:");

                Optional<String> noteContent = noteContentDialog.showAndWait();
                noteContent.ifPresent(note -> {
                    // Handle saving the note here
                    
                    System.out.println("Note added for " + name + ": " + note);
                });
            });
        }
    }

    private Map<String, List<String>> notesMap = new HashMap<>();

    private void saveNoteInMemory(String creatureName, String note) {
        if (!notesMap.containsKey(creatureName)) {
            notesMap.put(creatureName, new ArrayList<>());
        }
        notesMap.get(creatureName).add(note);
    }

}
