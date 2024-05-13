/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class MultitoolGUIController implements Initializable {

    @FXML
    private ListView<Tool> toolList;
    @FXML
    private ListView<Tool> multiToolList;
    @FXML
    private Label sizeLabel;

    private MultipleTool multipleTools = new MultipleTool();

    private List<Tool> toolslist = new ArrayList<>(Arrays.asList(
            new Tool("Hammer", "Hand Tool", "Hammering nails", 5),
            new Tool("Screwdriver", "Hand Tool", "Turning screws", 3),
            new Tool("Wrench", "Hand Tool", "Gripping and turning nuts", 4)
    ));

    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        ObservableList<Tool> tools = FXCollections.observableArrayList(toolslist);
        toolList.setItems(tools);
        updateMultiToolListView();
    }

    private void updateToolListView() {
        ObservableList<Tool> observableTools = FXCollections.observableArrayList(toolslist);
    toolList.setItems(observableTools);
    
    toolList.refresh();
    }

    private void updateMultiToolListView() {
        multiToolList.setItems(FXCollections.observableArrayList(multipleTools.getAllTools()));
        updateSizeLabel();
    }

    @FXML
    private void handleAddToMultiTool() {
        Tool selectedTool = toolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            multipleTools.addTool(selectedTool);
            updateMultiToolListView();
        }
    }

    @FXML
    private void handleRemoveFromMultiTool() {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            multipleTools.removeTool(selectedTool);
            updateMultiToolListView();
        }
    }

    @FXML
    private void handleClearList() {
        multipleTools.clear();
        updateMultiToolListView();
    }

    private void updateSizeLabel() {
        sizeLabel.setText("Size: " + multiToolList.getItems().size());
    }

    @FXML
    private void handleUpgradeTool(ActionEvent event) {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            try {
                selectedTool.setGrade(selectedTool.getGrade() + 1);
                updateToolListView();  
                updateMultiToolListView();  
            } catch (IllegalStateException e) {
                showErrorDialog("Error", "Upgrade failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDowngradeTool(ActionEvent event) {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            try {
                selectedTool.setGrade(selectedTool.getGrade() - 1); 
                updateToolListView();  
                updateMultiToolListView();  
            } catch (IllegalStateException e) {
                showErrorDialog("Error", "Downgrade failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleSwitchTool(ActionEvent event) {
        Tool currentTool = multiToolList.getSelectionModel().getSelectedItem();
        if (currentTool != null) {
            try {
                Tool nextTool = multipleTools.switchToolDown(currentTool);
                multiToolList.getSelectionModel().select(nextTool);
                multiToolList.scrollTo(nextTool);  // 确保新选中的工具可见
            } catch (IllegalStateException e) {
                showErrorDialog("Error", "Switching tool failed: " + e.getMessage());
            }
        }
    }

    private void showErrorDialog(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
