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
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import minecraft.CreatureEncyclopedia.Creature;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class CreatureEncyclopediaController implements Initializable {

    @FXML
    private TextField searchField;
    @FXML
    private ListView<String> creatureListView;
    @FXML
    private TextArea creatureDetails;

    private CreatureEncyclopedia encyclopedia;

    public CreatureEncyclopediaController() {
        this.encyclopedia = new CreatureEncyclopedia();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateCreatureListView();
    }

    @FXML
    private void handleSearch(ActionEvent event) {
        String searchTerm = searchField.getText().toLowerCase();
        ObservableList<String> results = FXCollections.observableArrayList();
        for (String creatureName : encyclopedia.encyclopedia.keySet()) {
            Creature creatureInfo = encyclopedia.getCreatureInfo(creatureName);
            if (creatureName.toLowerCase().contains(searchTerm)
                    || creatureInfo.getType().toLowerCase().contains(searchTerm)
                    || creatureInfo.getSpecies().toLowerCase().contains(searchTerm)
                    || creatureInfo.getBehavior().toLowerCase().contains(searchTerm)
                    || creatureInfo.getHabitat().toLowerCase().contains(searchTerm)
                    || creatureInfo.getDrops().toLowerCase().contains(searchTerm)
                    || creatureInfo.getWeakness().toLowerCase().contains(searchTerm)) {
                results.add(creatureName);
            }
        }
        creatureListView.setItems(results);
    }
    
    @FXML
    private void handleFilter(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Filter by Type");
        dialog.setHeaderText("Enter the type to filter by:");
        dialog.setContentText("Type:");

        String type = dialog.showAndWait().orElse(null);
        if (type == null || type.isEmpty()) {
            return;
        }

        List<Creature> filteredCreatures = encyclopedia.filterByType(type);
        ObservableList<String> filteredList = FXCollections.observableArrayList();
        for (Creature creature : filteredCreatures) {
            filteredList.add(creature.getSpecies());
        }
        creatureListView.setItems(filteredList);
    }
    
    @FXML
    private void handleSortAZ(ActionEvent event) {
        List<Creature> sortedCreatures = encyclopedia.sortCreaturesAZ();
        updateCreatureListView(sortedCreatures);
    }
    
    @FXML
    private void handleSortZA(ActionEvent event) {
        List<Creature> sortedCreatures = encyclopedia.sortCreaturesZA();
        updateCreatureListView(sortedCreatures);
    }

    private void updateCreatureListView() {
        updateCreatureListView(encyclopedia.encyclopedia.values().stream().toList());
    }

    private void updateCreatureListView(List<Creature> creatures) {
        ObservableList<String> creatureNames = FXCollections.observableArrayList();
        for (Creature creature : creatures) {
            creatureNames.add(creature.getSpecies());
        }
        creatureListView.setItems(creatureNames);
    }

    @FXML
    private void handleCreatureSelection() {
        String selectedCreature = creatureListView.getSelectionModel().getSelectedItem();
        if (selectedCreature != null) {
            Creature creature = encyclopedia.getCreatureInfo(selectedCreature);
            StringBuilder details = new StringBuilder();
            details.append("Type: ").append(creature.getType()).append("\n");
            details.append("Species: ").append(creature.getSpecies()).append("\n");
            details.append("Behavior: ").append(creature.getBehavior()).append("\n");
            details.append("Habitat: ").append(creature.getHabitat()).append("\n");
            details.append("Drops: ").append(creature.getDrops()).append("\n");
            details.append("Weakness: ").append(creature.getWeakness()).append("\n");
            details.append("Community Contributions: ").append(String.join(", ", creature.getCommunityContributions())).append("\n");
            creatureDetails.setText(details.toString());
        }
    }

    @FXML
    private void handleAddNote() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Add Note");
        dialog.setHeaderText("Enter the name of the creature you want to add a note to:");
        dialog.setContentText("Creature Name:");

        String creatureName = dialog.showAndWait().orElse(null);
        if (creatureName == null || creatureName.isEmpty() || !encyclopedia.encyclopedia.containsKey(creatureName)) {
            showAlert("Creature not found", "The creature " + creatureName + " does not exist in the encyclopedia.");
            return;
        }

        TextInputDialog noteDialog = new TextInputDialog();
        noteDialog.setTitle("Add Note");
        noteDialog.setHeaderText("Enter your note for " + creatureName + ":");
        noteDialog.setContentText("Note:");

        String note = noteDialog.showAndWait().orElse(null);
        if (note != null && !note.isEmpty()) {
            encyclopedia.addNoteToCreature(creatureName, note);
            showAlert("Note added", "Note added successfully for " + creatureName + ".");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

}
