/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

import java.util.ArrayList;
/**
 * FXML Controller class
 *
 * @author Asus
 */

public class EnderBackpackController extends database_item1 implements Initializable{

    @FXML
    private TilePane tilePane = new TilePane();
    @FXML
    private Button selectLastItemButton = new Button();
    @FXML
    private Button increaseCapacityButton = new Button();
    @FXML
    private Button reduceCapacityButton = new Button();
    @FXML
    private Button addItem = new Button();

    @FXML
    private Label labelCapacity = new Label();

    @FXML
    private Label labelItemNo = new Label();

    @FXML
    private Label showCapacity = new Label();

    @FXML
    private Label showItemNo = new Label();

    private int maxCapacity;

    private EnderBackpackItem selectedItem = null;

    private ObservableList<String> items = FXCollections.observableArrayList();

    public static EnderBackpackImplementation backpack;

    public static ItemBox box;

    private static String username;//=LoginPageController.username
    

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        updateDisplay();
        updateIncreaseCapacityButton();
        updateReduceCapacityButton();
        increaseCapacityButton.setOnAction(event -> handleIncreaseCapacityAction());
        reduceCapacityButton.setOnAction((event -> handleReduceCapacityAction()));
        addItem.setOnAction(event -> {
            try {
                handleAddItemAction(event);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        selectLastItemButton.setOnAction(event -> selectLastItem());
    }

    private void updateDisplay(){
        tilePane.getChildren().clear();
        ArrayList<String> retrieve = new ArrayList<String>();
        try {
            backpack = new EnderBackpackImplementation("defaultUser"); 
            box = new ItemBox("defaultUser");
            retrieve = backpack.getItemsName();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        items.addAll(retrieve);
        updateMaxCapacity();
        labelCapacity.setText("Backpack Capacity: ");
        labelItemNo.setText("Number of items: ");
        showCapacity.setText(String.valueOf(backpack.getCapacity()));
        showItemNo.setText(String.valueOf(backpack.getCurrentStock()));
        for (int i=0;i<backpack.getNumOfItem();i++) {
            HBox itemBox = new HBox(-10);
            // Label label = new Label(backpack.list.get(i).getName());
            // label.setMinWidth(100);

            ImageView imageView = new ImageView(new Image(getClass()
                                                .getResourceAsStream("/minecraft/icon/" + 
                                                "Minecraft" + ".PNG")));//backpack.list.get(i).getName()
            imageView.setFitWidth(50);
            imageView.setFitHeight(50);
            imageView.setPreserveRatio(true);
            Button button = new Button();
            button.setGraphic(imageView);
            button.setTooltip(new Tooltip(backpack.list.get(i).getName() + "\nType: " + 
                                          backpack.list.get(i).getType() + "\nFunction: " + 
                                          backpack.list.get(i).getFunction() + "\nQuantity: " + 
                                          backpack.list.get(i).getQuantity()));
            button.setPrefSize(50, 50); // Sets the preferred size of the button
            button.setMinSize(50, 50);  // Ensures the button does not shrink below the desired size
            button.setMaxSize(50, 50);  // Ensures the button does not grow above the desired size
            button.setStyle("-fx-background-color: transparent;");

            if (backpack.list.get(i).equals(selectedItem)) {
                itemBox.setStyle("-fx-background-color: yellow;");
            } else {
                itemBox.setStyle("-fx-background-color: transparent;");
            }

            Button removeButton = new Button("-");
            removeButton.setStyle("-fx-background-color: lightgrey;"+
                                  "-fx-text-fill: black;-fx-font-size: 8px;");
            removeButton.setPrefSize(20,20);
            removeButton.setMinSize(20,20);
            removeButton.setMaxSize(20,20);
            // removeButton.setStyle("-fx-text-fill: black;-fx-font-weight: bold;");

            EnderBackpackItem item = backpack.list.get(i);
            removeButton.setOnAction(event -> handleRemove(item));

            tilePane.setHgap(5);
            itemBox.setOnMouseClicked(e -> selectItem(item));
            // itemBox.getChildren().addAll(label, removeButton);
            itemBox.getChildren().addAll(button, removeButton);
            tilePane.getChildren().add(itemBox);
        }
        updateIncreaseCapacityButton();
    }

    private void selectItem(EnderBackpackItem item) {
        selectedItem = item;
        updateDisplay();
    }

    private void selectLastItem() {
        if (!backpack.list.isEmpty()) {
            selectedItem = backpack.list.get(backpack.list.size()-1);
            updateDisplay();
        }
    }

    private void handleRemove(EnderBackpackItem item){
        Dialog<Integer> dialog = new Dialog<>();
        dialog.setTitle("Remove Item from Backpack");
        dialog.setHeaderText("How much do you want to remove from backpack for this item?");

        ButtonType okButtonType = new ButtonType("Confirm", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(okButtonType, ButtonType.CANCEL);

        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        TextField removeItemField = new TextField();
        removeItemField.setPromptText("Quantity");

        Label warningLabel = new Label(); // Create a label for warning messages
        warningLabel.setTextFill(Color.RED); // Set the text color to red

        grid.add(new Label("Quantity:"), 0, 0);
        grid.add(removeItemField, 1, 0);
        grid.add(warningLabel, 0, 1, 2, 1); // Add warning label below the capacity field, spanning 2 columns

        dialog.getDialogPane().setContent(grid);

        // Request focus on the field by default
        Platform.runLater(removeItemField::requestFocus);

        // Add a listener to the text property of the TextField
        removeItemField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new value is not a number, show the warning message
                warningLabel.setText("Warning: Please enter a valid number!");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            } 
            else if (Integer.parseInt(newValue)>item.quantity){
                warningLabel.setText("Warning: Quantity > number of items in backpack.");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
            else {
                // If the new value is a number, clear the warning message
                warningLabel.setText("");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(false);
            }
        });


        // Convert the result when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    int itemReduction = Integer.parseInt(removeItemField.getText());
                    return itemReduction; // Return null to indicate invalid input
                } catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(quantitytoremove -> {
            backpack.removeItem(item, quantitytoremove);
            try {
                box.addItem(item.name, quantitytoremove);
                database_item1.removeItem(item.getName(), "defaultUser", quantitytoremove);
                database_itemBox.addItem("defaultUser", item.getName(), item.getType(),
                                        item.getFunction(),quantitytoremove);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            if (item.equals(selectedItem)) {
                selectedItem = null;
            }
            
            updateDisplay();
            updateIncreaseCapacityButton(); 
            updateReduceCapacityButton();
        });
    }

    private void updateMaxCapacity(){
        maxCapacity = backpack.getCapacity();
    }

    private void updateIncreaseCapacityButton(){
        double currentCapacityPercentage = (double) backpack.getCurrentStock() / maxCapacity * 100;
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

        Label warningLabel = new Label(); // Create a label for warning messages
        warningLabel.setTextFill(Color.RED); // Set the text color to red

        grid.add(new Label("Capacity:"), 0, 0);
        grid.add(capacityField, 1, 0);
        grid.add(warningLabel, 0, 1, 2, 1); // Add warning label below the capacity field, spanning 2 columns

        dialog.getDialogPane().setContent(grid);

        // Request focus on the field by default
        Platform.runLater(capacityField::requestFocus);

        capacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new value is not a number, show the warning message
                warningLabel.setText("Warning: Please enter a valid number!");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            } 
            else if (Integer.parseInt(newValue)<0){
                warningLabel.setText("Warning: A negative number entered.");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
            else {
                // If the new value is a number, clear the warning message
                warningLabel.setText("");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(false);
            }
        });

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
            try {
                backpack.addCapacity(capacityIncrease);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            updateDisplay();  // Update the UI
            updateIncreaseCapacityButton();
            updateReduceCapacityButton();
        });
    }

    private void updateReduceCapacityButton() {
        reduceCapacityButton.setDisable(backpack.getCurrentStock() >= maxCapacity);
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

        Label warningLabel = new Label(); // Create a label for warning messages
        warningLabel.setTextFill(Color.RED); // Set the text color to red

        grid.add(new Label("Capacity:"), 0, 0);
        grid.add(capacityField, 1, 0);
        grid.add(warningLabel, 0, 1, 2, 1); // Add warning label below the capacity field, spanning 2 columns

        dialog.getDialogPane().setContent(grid);

        Platform.runLater(capacityField::requestFocus);

        // Add a listener to the text property of the TextField
        capacityField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.matches("\\d*")) {
                // If the new value is not a number, show the warning message
                warningLabel.setText("Warning: Please enter a valid number!");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            } 
            else if (Integer.parseInt(newValue)>(maxCapacity - backpack.getCurrentStock())){
                warningLabel.setText("Quantity cannot be more than the extra spaces.");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(true);
            }
            else {
                // If the new value is a number, clear the warning message
                warningLabel.setText("");
                dialog.getDialogPane().lookupButton(okButtonType).setDisable(false);
            }
        });

        // 结果转换

        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == okButtonType) {
                try {
                    return Integer.parseInt(capacityField.getText()); // Return null to indicate invalid input
                } 
                catch (NumberFormatException e) {
                    return null;
                }
            }
            return null;
        });

        Optional<Integer> result = dialog.showAndWait();
        result.ifPresent(capacityReduction -> {
            System.out.println("Capacity reduced by: " + capacityReduction);

            maxCapacity -= capacityReduction; 
            try {
                backpack.reduceCapacity(capacityReduction);
            } catch (SQLException e) {
                e.printStackTrace();
            } 
            updateDisplay();  

            updateIncreaseCapacityButton();
            updateReduceCapacityButton();
        });
    }
    
    @FXML
    private void handleAddItemAction(ActionEvent event) throws IOException {
        URL fxmlUrl = null;
        try {
            // FXMLLoader loader = new FXMLLoader(getClass().getResource("AddItemBackpack.fxml"));
            // Parent root = loader.load();
            // Stage stage = new Stage();
            // stage.setTitle("Add Item into Backpack");
            // stage.setScene(new Scene(root));
            // stage.show();
            
            fxmlUrl = getClass().getResource("AddItemBackpack.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find FXML file. Please check the file path.");
            }

            Parent oldPage = FXMLLoader.load(fxmlUrl);
            Stage appStage = new Stage();
            appStage.setOnCloseRequest(e->{
                new MainSceneController().switchToEnderBackpack(event);
            });
            appStage.setTitle("Add Item into Backpack");

            Scene oldScene = new Scene(oldPage);
//            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            appStage.setScene(oldScene);
            appStage.show();
        } catch (Exception e) {
            System.err.println("Failed to load the FXML file.");
            if (fxmlUrl != null) {
                System.err.println("Attempted to load from URL: " + fxmlUrl.toExternalForm());
            } else {
                System.err.println("URL was null, check the path to the FXML file.");
            }
            e.printStackTrace();
        }
        
        // Parent dialogRoot = fxmlLoader.load();
        // Stage dialogStage = new Stage();
        // dialogStage.setTitle("Add Item into Backpack");
        // dialogStage.initModality(Modality.APPLICATION_MODAL);
        // dialogStage.setScene(new Scene(dialogRoot));
        // dialogStagen.show();
    }
    
    

}
