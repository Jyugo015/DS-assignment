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
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import minecraft.Potions.Potion;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class PotionSatchelController implements Initializable {

    @FXML
    private GridPane potionGrid;
    @FXML
    private GridPane selectedPotionGrid;
    private Button addButton;

    private int totalPotionsAdded = 0;
    private final int MAX_POTIONS = 9;
    private int potionsToAdd = 0;

    private TreeMap<String, Potion> potionMap;
    private List<Potion> selectedPotions = new ArrayList<>();
    private List<Potion> tempSelectedPotions = new ArrayList<>();
    private Map<Button, Potion> buttonPotionMap = new HashMap<>();
    private Map<Button, Potion> selectedPotionMap = new HashMap<>();
    private Button currentlySelectedButton = null;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        Potions potions = new Potions();
        potionMap = potions.initializePotions();
        populatePotionGrid();
    }

    private void populatePotionGrid() {
        potionGrid.setHgap(10);
        potionGrid.setVgap(10);
        potionGrid.setPadding(new Insets(10, 10, 10, 10));
        int row = 0, col = 0;
        for (Map.Entry<String, Potion> entry : potionMap.entrySet()) {
            Potion potion = entry.getValue();
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/minecraft/icon/" + potion.getName() + ".PNG")));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            Button button = new Button();
            button.setGraphic(imageView);
            button.setTooltip(new Tooltip(potion.getName() + "\nPotency: " + potion.getPotency() + "\nEffect: " + potion.getEffect()));
            button.setMaxSize(60, 60);
            buttonPotionMap.put(button, potion);
            button.setOnAction(e -> handlePotionSelection(button));
            potionGrid.add(button, col++, row);
            if (col > 8) {
                col = 0;
                row++;
            }
        }
    }

    @FXML
    private void handleAddButtonClick(ActionEvent event) {
        TextInputDialog dialog = new TextInputDialog("1");
        dialog.setTitle("Add Potion");
        dialog.setHeaderText("Select number of potions to add");
        dialog.setContentText("Enter number of potions:");
        Optional<String> result = dialog.showAndWait();
        result.ifPresent(number -> {
            int numToAdd = Integer.parseInt(number);
            if (numToAdd > 0 && (totalPotionsAdded + numToAdd <= MAX_POTIONS)) {
                potionsToAdd = numToAdd;
            } else {
                showAlert("Limit Reached", "Adding this amount would exceed the maximum number of potions.");
            }
        });
    }

    private void handlePotionSelection(Button button) {
        if (tempSelectedPotions.size() < potionsToAdd && !tempSelectedPotions.contains(buttonPotionMap.get(button))) {
            tempSelectedPotions.add(buttonPotionMap.get(button));
            button.setDisable(true);  // Disable the button immediately to prevent re-selection
        }
    }

    @FXML
    private void handleDoneButtonClick(ActionEvent event) {
        if (tempSelectedPotions.size() != potionsToAdd) {
            showAlert("Selection Incomplete", "Please select the number of potions you specified.");
            return;
        }
        selectedPotions.addAll(tempSelectedPotions);
        totalPotionsAdded += tempSelectedPotions.size();
        updateSelectedPotionsGrid();
        tempSelectedPotions.clear();
        potionsToAdd = 0;  // Reset the count after adding potions
    }

    private void updateSelectedPotionsGrid() {
        selectedPotionGrid.getChildren().clear();
        int col = 0;
        for (Potion potion : selectedPotions) {
            ImageView imageView = new ImageView(new Image(getClass().getResourceAsStream("/minecraft/icon/" + potion.getName() + ".PNG")));
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            Button button = new Button();
            button.setGraphic(imageView);
            button.setMaxSize(60, 60);
            button.setTooltip(new Tooltip("Selected: " + potion.getName()));
            button.setOnAction(e -> selectPotionForRemoval(button, potion));
            selectedPotionGrid.add(button, col++, 0);
            selectedPotionMap.put(button, potion);
        }
    }

    private void selectPotionForRemoval(Button button, Potion potion) {
        if (currentlySelectedButton != null) {
            currentlySelectedButton.setStyle("");
        }
        button.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
        currentlySelectedButton = button;
    }

    @FXML
    private void handleRemoveButtonClick() {
        if (currentlySelectedButton == null) {
            showAlert("No Selection", "Please select a potion to remove.");
            return;
        }
        Potion potion = selectedPotionMap.get(currentlySelectedButton);
        if (potion != null) {
            selectedPotions.remove(potion);
            totalPotionsAdded--;
            buttonPotionMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(potion))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .ifPresent(button -> button.setDisable(false));
            selectedPotionGrid.getChildren().remove(currentlySelectedButton);
            selectedPotionMap.remove(currentlySelectedButton);
            currentlySelectedButton = null;
            updateSelectedPotionsGrid();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void resetPotionButtons() {
        potionGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                node.setDisable(false);
            }
        });
    }
}
