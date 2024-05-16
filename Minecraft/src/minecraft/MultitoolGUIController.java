/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.net.URL;
import java.sql.SQLException;
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
public class MultitoolGUIController extends database_item2 implements Initializable {

    @FXML
    private ListView<Tool> toolList;
    @FXML
    private ListView<Tool> multiToolList;
    @FXML
    private Label sizeLabel;

    private MultipleTool multipleTools = new MultipleTool();

    private static String username= "defaultUser";//=LoginPageController.username

    // 静态数组，初始化工具集合
    private List<Tool> toolslist = new ArrayList<Tool>();
    //  = new ArrayList<>();
    // Arrays.asList(
    //         new Tool("Hammer", "Hand Tool", "Hammering nails", 5),
    //         new Tool("Screwdriver", "Hand Tool", "Turning screws", 3),
    //         new Tool("Wrench", "Hand Tool", "Gripping and turning nuts", 4)
    // )

    @FXML
    private Button addButton;
    @FXML
    private Button removeButton;

    @Override
    public void initialize(URL url, ResourceBundle rb){
        // 初始化 toolList
        try {
            multipleTools = new MultipleTool("defaultUser");
            toolslist = database_item2.retrieveTool("defaultUser");
            toolslist.forEach(e->System.out.println(e));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        ObservableList<Tool> tools = FXCollections.observableArrayList(toolslist);
        toolList.setItems(tools);
        // 初始化 multiToolList
        try {
            updateToolListView();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        updateMultiToolListView();
    }

    private void updateToolListView() throws SQLException {
        toolslist = database_item2.retrieveTool("defaultUser");
        ObservableList<Tool> observableTools = FXCollections.observableArrayList(toolslist);
        toolList.setItems(observableTools);
        
        // 强制刷新 ListView 来显示最新的数据
        toolList.refresh();
    }

    private void updateMultiToolListView() {
        multiToolList.setItems(FXCollections.observableArrayList(multipleTools.getAllTools()));
        updateSizeLabel();
    }

    @FXML
    private void handleAddToMultiTool() throws SQLException {
        Tool selectedTool = toolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            multipleTools.addTool(selectedTool);
            database_item2.addTool(username, selectedTool.getName(), selectedTool.getType(),
                                   selectedTool.getFunction(), selectedTool.getGrade());
            database_itemBox.removeItem(selectedTool.getName(), username, 1);
            updateMultiToolListView();
        }
    }

    @FXML
    private void handleRemoveFromMultiTool() throws SQLException {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            multipleTools.removeTool(selectedTool);
            database_item2.removeTool(selectedTool.getName(), username);
            database_itemBox.addItem(username, selectedTool.getName(), "Tool", 
            selectedTool.getFunction(), 1);
            updateMultiToolListView();
        }
    }

    @FXML
    private void handleClearList() throws SQLException {
        multipleTools.clear();
        database_item2.clearTool(username);
        updateMultiToolListView();
    }

    private void updateSizeLabel() {
        sizeLabel.setText("Size: " + multiToolList.getItems().size());
    }

    @FXML
    private void handleUpgradeTool(ActionEvent event) throws SQLException {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            try {
                selectedTool.setGrade(selectedTool.getGrade() + 1); // 直接在选中的工具上升级
                database_item2.upgradeTool("defaultUser", selectedTool.getName());
                updateToolListView();  // 刷新toolList视图
                updateMultiToolListView();  // 刷新multiToolList视图
            } catch (IllegalStateException e) {
                showErrorDialog("Error", "Upgrade failed: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleDowngradeTool(ActionEvent event) throws SQLException {
        Tool selectedTool = multiToolList.getSelectionModel().getSelectedItem();
        if (selectedTool != null) {
            try {
                selectedTool.setGrade(selectedTool.getGrade() - 1); // 直接在选中的工具上降级
                database_item2.downgradeTool("defaultUser", selectedTool.getName());
                updateToolListView();  // 刷新toolList视图
                updateMultiToolListView();  // 刷新multiToolList视图
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
