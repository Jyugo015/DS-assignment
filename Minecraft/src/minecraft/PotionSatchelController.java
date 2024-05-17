/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.TreeMap;
import java.util.stream.Collectors;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import minecraft.Potions.Potion;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class PotionSatchelController extends database_item3 implements Initializable {

    @FXML
    private GridPane potionGrid;
    @FXML
    private GridPane selectedPotionGrid;
    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;

    private int totalPotionsAdded = 0;
    private final int MAX_POTIONS = 9;
    private int potionsToAdd = 0;

    private TreeMap<String, Potion> potionMap;
    private List<Potion> selectedPotions;
    private List<Potion> tempSelectedPotions = new ArrayList<>();
    private Map<Button, Potion> buttonPotionMap = new HashMap<>();
    private Map<Button, Potion> selectedPotionMap = new HashMap<>();
    private Button currentlySelectedButton = null;
    
    private newPotionSatchel satchel = new newPotionSatchel();
    private ArrayList<String> potionBag;

    //each time removing or adding potion into satchel will change the list of potion
    //(remove potion from list of potion if adding potion to satchel)

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Potions potions = new Potions();//initializing the potions
        try {
            potionBag = database_item3.retrievePotion("defaultUser");
            selectedPotions = database_item3.retrievePotionSatchel("defaultUser");
            selectedPotions.forEach(e->satchel.addPotionToSatchel(e));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        potionMap = new Potions().getSortedPotionMap();
        addButton.setOnAction(e->handleAddButtonClick(e));
        removeButton.setOnAction(e->{
            try {
                handleRemoveButtonClick(e);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        populatePotionGrid();
        updateSelectedPotionsGrid();
    }

    //printing list of potions can be chosen into potionsatchel
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
            
            boolean found = false;
            for (String potionName: potionBag){
                if (potionName.equals(entry.getKey()))
                found = true;
            }
            if (!found)
                button.setDisable(true);
            button.setOnAction(e -> handlePotionSelection(button));
            //select the potion and save as reference  
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
        Text warningText = new Text();
        warningText.setFill(Color.RED);

        // Add a listener to the text input field to validate the input
        TextField inputField = dialog.getEditor();
        // textinputdialog.getEditor return textfield used within the dialog
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            // Parse the input value as an integer
            try {
                // Check if the value is negative
                if (!newValue.matches("\\d*")) {
                    warningText.setText("Number of potions cannot be string");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                } 
                else if (Integer.parseInt(newValue) < 0) {
                    warningText.setText("Number of potions cannot be negative");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(true);
                }else {
                    // Clear the warning message if the value is valid
                    warningText.setText("");
                    dialog.getDialogPane().lookupButton(ButtonType.OK).setDisable(false);
                }
            } catch (NumberFormatException e) {
                // Clear the warning message if the input is not a valid integer
                warningText.setText("");
                dialog.getDialogPane();
            }
        });

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
        //tempSelectedPotions.size() < potionsToAdd, means potion selected 
        //!tempSelectedPotions.contains(buttonPotionMap.get(button), means tempselectedPotions does not contain
        //the potion that maps to the button, this is to avoid adding duplicate potion(of the same kind)
        if (tempSelectedPotions.size() < potionsToAdd && !tempSelectedPotions.contains(buttonPotionMap.get(button))) {
            tempSelectedPotions.add(buttonPotionMap.get(button));
            button.setDisable(true);  // Disable the button immediately to prevent re-selection
        }
    }

    @FXML
    private void handleDoneButtonClick(ActionEvent event){
        if (tempSelectedPotions.size() != potionsToAdd) {
            showAlert("Selection Incomplete", "Please select the number of potions you specified.");
            return;
        }
        selectedPotions.addAll(tempSelectedPotions);
        selectedPotions.forEach(e->satchel.addPotionToSatchel(e));
        tempSelectedPotions.forEach(e->
        {
            try {
                database_item3.addPotionSatchel("defaultUser", 
                e.getName(),e.getPotency(), e.getEffect());
                System.out.println(e.getName() + " " +  String.valueOf(e.getPotency())+ " " + e.getEffect());
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        });
        // totalPotionsAdded += tempSelectedPotions.size();
        totalPotionsAdded =satchel.getSize();
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
            //deselect the currentselectedbutton if another button is selected
        }
        button.setStyle("-fx-border-color: blue; -fx-border-width: 2;");
        currentlySelectedButton = button;
    }

    @FXML
    private void handleRemoveButtonClick(ActionEvent event) throws SQLException {
        if (currentlySelectedButton == null) {
            showAlert("No Selection", "Please select a potion to remove.");
            return;
        }
        Potion potion = selectedPotionMap.get(currentlySelectedButton);
        if (potion != null) {
            selectedPotions.remove(potion);
            satchel.removePotion(potion);
            selectedPotionMap.remove(currentlySelectedButton);
            database_item3.removePotionSatchel("defaultUser", potion.getName(), 
                                                potion.getPotency(), potion.getEffect());
            // totalPotionsAdded--;
            totalPotionsAdded = satchel.getSize();
            buttonPotionMap.entrySet().stream()
                    .filter(entry -> entry.getValue().equals(potion))
                    .map(Map.Entry::getKey)
                    .findFirst()
                    .ifPresent(button -> button.setDisable(false));
            selectedPotionGrid.getChildren().remove(currentlySelectedButton);
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
