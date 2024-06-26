/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package minecraft;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author Asus
 */
public class MainSceneController implements Initializable {

    @FXML
    private Button btn;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    @FXML
    private void switchToMultitool(ActionEvent event) {
        URL fxmlUrl = null;
        try {
            fxmlUrl = getClass().getResource("MultitoolGUI.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find FXML file. Please check the file path.");
            }

            Parent oldPage = FXMLLoader.load(fxmlUrl);
            Stage appStage = new Stage();
            appStage.setTitle("Multitool");
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
    }

    @FXML
    public void switchToEnderBackpack(ActionEvent event) {
        URL fxmlUrl = null;
        try {
            fxmlUrl = getClass().getResource("EnderBackpack.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find FXML file. Please check the file path.");
            }

            Parent oldPage = FXMLLoader.load(fxmlUrl);
            Stage appStage = new Stage();
            appStage.setTitle("Ender Backpack");

            Scene oldScene = new Scene(oldPage, Color.BURLYWOOD);
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
    }

    @FXML
    private void switchToPotionSatchel(ActionEvent event) {
        URL fxmlUrl = null;
        try {
            fxmlUrl = getClass().getResource("PotionSatchel.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find FXML file. Please check the file path.");
            }

            Parent oldPage = FXMLLoader.load(fxmlUrl);
            Stage appStage = new Stage();
            appStage.setTitle("PotionSatchel.fxml");
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
    }

    @FXML
    private void switchToCreatureEncyclopedia(ActionEvent event) {
        URL fxmlUrl = null;
        try {
            fxmlUrl = getClass().getResource("CreatureEncyclopedia.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find FXML file. Please check the file path.");
            }

            Parent oldPage = FXMLLoader.load(fxmlUrl);
            Stage appStage = new Stage();
            appStage.setTitle("CreatureEncyclopedia.fxml");
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
    }
    @FXML
    private void switchToTeleportationNetwork(ActionEvent event) {
         TeleportationNetworkController_GUI teleportationNetwork = new TeleportationNetworkController_GUI();
        try {
            teleportationNetwork.start((Stage) ((Button) event.getSource()).getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void switchToAutomatedSortingChest(ActionEvent event) {
         AutomatedSortingChest sortedChest = new AutomatedSortingChest();
        try {
            sortedChest.start((Stage) ((Button) event.getSource()).getScene().getWindow());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @FXML
    private void switchToAdventurerDiary(ActionEvent event) {
        URL fxmlUrl = null;
        try {
            fxmlUrl = getClass().getResource("AdventurerDiary.fxml");
            if (fxmlUrl == null) {
                throw new RuntimeException("Cannot find FXML file. Please check the file path.");
            }

            Parent oldPage = FXMLLoader.load(fxmlUrl);
            Stage appStage = new Stage();
            appStage.setTitle("AdventurerDiary.fxml");
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
    }
    
    
    

//    @FXML
    private void switchToAutoFarmerBlock(ActionEvent event) {
//        URL fxmlUrl = null;
//        try {
//            fxmlUrl = getClass().getResource("AutoFarmerBlock.fxml");
//            if (fxmlUrl == null) {
//                throw new RuntimeException("Cannot find FXML file. Please check the file path.");
//            }
//
//            Parent oldPage = FXMLLoader.load(fxmlUrl);
//            Stage appStage = new Stage();
//            appStage.setTitle("AutoFarmerBlock.fxml");
//            Scene oldScene = new Scene(oldPage);
////            Stage appStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
//            appStage.setScene(oldScene);
//            appStage.show();
//        } catch (Exception e) {
//            System.err.println("Failed to load the FXML file.");
//            if (fxmlUrl != null) {
//                System.err.println("Attempted to load from URL: " + fxmlUrl.toExternalForm());
//            } else {
//                System.err.println("URL was null, check the path to the FXML file.");
//            }
//            e.printStackTrace();
//        }

    }
    
}
