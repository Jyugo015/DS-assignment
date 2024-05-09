/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.TilePane;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class EnderBackpackController implements Initializable {

    @FXML
    private TilePane tilePane;
    @FXML
    private Button selectLastItemButton;
    @FXML
    private Button increaseCapacityButton;
    @FXML
    private Button reduceCapacityButton;

    private int maxCapacity = 6;

    private String selectedItem = null;

    private ObservableList<String> items = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        items.addAll("Apple", "Banana", "Cherry", "Date", "Elderberry", "Orange");
        updateDisplay();
        updateIncreaseCapacityButton();
        updateReduceCapacityButton();
        increaseCapacityButton.setOnAction(event -> handleIncreaseCapacityAction());
        reduceCapacityButton.setOnAction((event -> handleReduceCapacityAction()));
        selectLastItemButton.setOnAction(event -> selectLastItem());
    }

    private void updateDisplay() {
        tilePane.getChildren().clear();
        for (String item : items) {
            HBox itemBox = new HBox(5);
            Label label = new Label(item);
            label.setMinWidth(100);

            if (item.equals(selectedItem)) {
                itemBox.setStyle("-fx-background-color: yellow;");
            } else {
                itemBox.setStyle("-fx-background-color: lightgrey;");
            }

            Button removeButton = new Button("-");
            removeButton.setOnAction(event -> {
                items.remove(item);
                if (item.equals(selectedItem)) {
                    selectedItem = null;
                }
                updateDisplay();
                updateIncreaseCapacityButton(); 
                updateReduceCapacityButton();
            });

            itemBox.setOnMouseClicked(e -> selectItem(item));
            itemBox.getChildren().addAll(label, removeButton);
            tilePane.getChildren().add(itemBox);

        }
        updateIncreaseCapacityButton();
    }

    private void selectItem(String item) {
        selectedItem = item;
        updateDisplay();
    }

    private void selectLastItem() {
        if (!items.isEmpty()) {
            selectedItem = items.get(items.size() - 1);
            updateDisplay();
        }
    }

    private void updateIncreaseCapacityButton() {
        double currentCapacityPercentage = (double) items.size() / maxCapacity * 100;
        System.out.println("Current capacity percentage: " + currentCapacityPercentage);
        increaseCapacityButton.setDisable(currentCapacityPercentage < 75);
    }

    @FXML
    private void handleIncreaseCapacityAction() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Increase Capacity");
        dialog.setHeaderText("How much do you want to add to your backpack capacity?");

        ButtonType okButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        grid.add(new Label("Capacity:"), 0, 0);
        grid.add(capacityField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        // Request focus on the field by default
        Platform.runLater(capacityField::requestFocus);

        // Convert the result when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    return Integer.parseInt(capacityField.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(capacityIncrease -> {
            System.out.println("Capacity increased by: " + capacityIncrease);
            maxCapacity += capacityIncrease;  // Increase the max capacity
            updateDisplay();  // Update the UI
            updateIncreaseCapacityButton();
            updateReduceCapacityButton();
        });
    }

    private void updateReduceCapacityButton() {
        reduceCapacityButton.setDisable(items.size() >= maxCapacity);
    }

    @FXML
    private void handleReduceCapacityAction() {
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Reduce Capacity");
        dialog.setHeaderText("How much do you want to reduce your backpack capacity?");

        ButtonType okButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField capacityField = new TextField();
        capacityField.setPromptText("Capacity");

        grid.add(new Label("Capacity:"), 0, 0);
        grid.add(capacityField, 1, 0);

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(capacityField::requestFocus);

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    return Integer.parseInt(capacityField.getText());
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(capacityReduction -> {
            System.out.println("Capacity reduced by: " + capacityReduction);
            maxCapacity -= capacityReduction;  
            updateDisplay();  
            updateIncreaseCapacityButton();
            updateReduceCapacityButton();
        });
    }

}
