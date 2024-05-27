package minecraft;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class AutomatedSortingChest extends Application {
    private static BSTs<EnderBackpackItem> bst = new BSTs<>();;
    private final static String imageFilePath = "/minecraft/icon/";
    
//    private String[] categories = {"Tools" , "Food", "Arrows", "Decorations", "MobEggs", "Weapons", "Armor", "Materials", "Transportations", "Potions", "Records", "Dyes"};
    static ArrayList<String> EnderBackpackItemsCollections = new ArrayList<>(Arrays.asList(new String[]{"Axes", "Shovels", "Apple", "Clownfish", "Swords", "Diamond","Potion of Decay", "Potion of Invisibility" , "Bucket" , "Wood" , "Stone" , "Red stone"}));
    static ArrayList<String> EnderBackpackItemImagesCollections = new ArrayList<>(Arrays.asList(new String[]{"Axes.jpeg", "Shovels.jpg", "Apple.jpg", "Clownfish.jpg", "Swords.jpg", "Diamond.jpg","Potion_of_Decay.jpg","Potion_of_Invisibility.gif","Bucket.jpg","Oak_Wood.jpg","Stone.jpg","Redstone.jpg"}));
    
    private static boolean isSelected = false;
    private static SingleEnderBackpackItemPane selected;
    private static String categoryChosen = "(Include ALL)";
    private static List<EnderBackpackItem> allSortedItems;
    private static Image backgroundImage;
    private static Stage stage = new Stage();
     
    private static Text reminder = new Text();
    private static Button backToMainPageButton = new Button("Back to main page");
    private static Text totalNoOfEnderBackpackItemInChest = new Text();
    private static Text totalNoOfEnderBackpackItemOfCategory = new Text();
    private static TextField searchEnderBackpackItemTextField = new TextField(categoryChosen);
    private static TextField quantityToAddOrRemove = new TextField();
    private static BorderPane pane1 = new BorderPane();
    
    
    private static ArrayList<String> unsortedEnderBackpackItemNameArrayList = new ArrayList<>();
    private static ArrayList<Integer> unsortedEnderBackpackItemQuantityArrayList = new ArrayList<>();
    private static ItemBox unsortedBox;
    private static String username;
    
    @Override
    public void start(Stage primaryStage) throws SQLException, FileNotFoundException {
        username = "defaultUser";
        unsortedBox = new ItemBox(username);
        unsortedEnderBackpackItemNameArrayList.clear();
        unsortedEnderBackpackItemQuantityArrayList.clear();
        try {
            unsortedBox = new ItemBox(username);
            // loading unsorted item in itemBox
            for (EnderBackpackItem item: unsortedBox.list) {
                System.out.println("Adding item " + item.name + " with " + database_itemBox.retrieveQuantity(item.name , username) + " type " + item.type);
                unsortedEnderBackpackItemNameArrayList.add(item.name);
                unsortedEnderBackpackItemQuantityArrayList.add(database_itemBox.retrieveQuantity(item.name , username));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
        }
//        unsortedItemList = FXCollections.observableArrayList(unsortedBox.list);
        
        
//        for (int i = 0; i < unsortedBox.size(); i++) {
//            EnderBackpackItem item = unsortedBox.get(i);
//            bst.add(item, item.quantity);
//        }

        
        // sorted item in Automated sorting chest
        for (String itemName : database_item4.retrieveItem(username)) {
            EnderBackpackItem item = database_item4.getEnderBackpackItem(username, itemName);
            bst.add(itemName, item.quantity);
        }
        allSortedItems= bst.getParticularCategory(categoryChosen).retriveAllItems();
        
        backToMainPageButton.setOnAction(e->{
            try {
                MainPage mainPage = new MainPage();
                mainPage.start((Stage) ((Button) e.getSource()).getScene().getWindow());
//                stage.close();
            } catch (IOException ex) {
                Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        backgroundImage = new Image(getClass().getResourceAsStream("/minecraft/icon/background.jpeg"));
//        backgroundImage = new Image(new FileInputStream(new File("C:\\Users\\PC\\Favorites\\Downloads\\Powder Snow Bucket.png")));
        stage = primaryStage;
        stage.setTitle("Automated Sorting Chest");
        stage.setScene(scene1());
        stage.show();
        
    }

    // Scene 1: main scene
    private Scene scene1() {
        pane1 = new BorderPane();
        updateAll();
        Label categoriesLabel = new Label("Categories: ");
        
        ChoiceBox<String> categoriesChoiceBox = new ChoiceBox<>();
        categoriesChoiceBox.setValue("(Include ALL)");
        categoriesChoiceBox.getItems().addAll("Tools", "Food", "Arrows", "Decorations", "MobEggs", "Weapons", "Armor", "Materials", "Transportations", "Potions", "Records", "Dyes", "(Include ALL)");        
        
        Button addNewEnderBackpackItemButton = new Button("Add New EnderBackpackItem");
        Button addcurrentEnderBackpackItemButton = new Button("Add");
        Button removeCurrentEnderBackpackItemButton = new Button("Remove");
        
        // Background
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        Background background = new Background(new BackgroundImage(backgroundImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane1.setBackground(background);
        
        // Top pane
        VBox topPane = new VBox(); 
        topPane.getChildren().addAll(totalNoOfEnderBackpackItemInChest, categoriesLabel,categoriesChoiceBox,searchEnderBackpackItemTextField,totalNoOfEnderBackpackItemOfCategory);
        
        // Bottom pane
        HBox bottomPane = new HBox();
        Text quantityText = new Text("Quantity: ");
        bottomPane.getChildren().addAll(quantityText,quantityToAddOrRemove,addcurrentEnderBackpackItemButton,removeCurrentEnderBackpackItemButton,backToMainPageButton,reminder);
        
        // Right pane
        pane1.setRight(addNewEnderBackpackItemButton);
        
        // Main pane
        pane1.setCenter(new EnderBackpackItemsGridPane(categoryChosen));
        pane1.setTop(topPane);
        pane1.setBottom(bottomPane);

        // Set on action
        // Select a new category
        categoriesChoiceBox.getSelectionModel().selectedIndexProperty().addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            categoryChosen = categoriesChoiceBox.getItems().get((Integer)newValue);
            System.out.println(categoryChosen);
            searchEnderBackpackItemTextField.setText(categoryChosen);
        });
        
        // search EnderBackpackItem to display the possible EnderBackpackItem
        searchEnderBackpackItemTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            pane1.setCenter(new EnderBackpackItemsGridPane(newValue));
        });
        
        addcurrentEnderBackpackItemButton.setOnAction(e -> {
            if (isSelected && quantityToAddOrRemove.getText() != null && ! quantityToAddOrRemove.getText().equals("") && Integer.parseInt(quantityToAddOrRemove.getText()) != 0) {
                try {
                    String itemName = selected.getEnderBackpackItemName();
                    int quantity = Integer.parseInt(quantityToAddOrRemove.getText());
                    int quantityInDatabaseItembox = database_itemBox.retrieveQuantity(itemName, username);
                    System.out.println("Qauntity to add: " + quantity + ", quanitty in database: " + quantityInDatabaseItembox);
                    quantity = quantityInDatabaseItembox>=quantity ? quantity : quantityInDatabaseItembox;
                    bst.add(itemName, quantity);
                    database_item4.addItem(username, itemName, quantity);
                    reminder.setText("You have inserted " + (quantityInDatabaseItembox>=quantity ? quantity : quantityInDatabaseItembox) + " " + selected);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
                unsortedBox.clearItem(); // clear database itembox
                updateAll();
            } else if (! isSelected) {
                reminder.setText("Please select a EnderBackpackItem to add.");
            } else {
                reminder.setText("Please enter the amount to add.");
            }
        });
        
        // Remove amount of current EnderBackpackItem
        removeCurrentEnderBackpackItemButton.setOnAction(e -> {
            if (isSelected && quantityToAddOrRemove.getText() != null && ! quantityToAddOrRemove.getText().equals("") && Integer.parseInt(quantityToAddOrRemove.getText()) != 0) {
                try {
                    String itemName = selected.getEnderBackpackItemName();
                    int quantity = Integer.parseInt(quantityToAddOrRemove.getText());
                    int quantityInDatabaseSortingChest = database_item4.retrieveQuantity(itemName, username);
                    System.out.println("Quantity to remove: " + quantity + ", quantity in sortingChest: " + quantityInDatabaseSortingChest);
                    quantity = quantityInDatabaseSortingChest>=quantity ? quantity : quantityInDatabaseSortingChest;
                    System.out.println("Quantity final: " + quantity);
                    bst.remove(itemName, quantity);
                    database_item4.removeItem(itemName, username, quantity);
                    reminder.setText("You have removed " + quantity + " " + selected);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
                updateAll();
            } else if (! isSelected) {
                reminder.setText("Please select a EnderBackpackItem to remove.");
            } else {
                reminder.setText("Please enter the amount to remove.");
            }
        });
        
        // enter the amount of EnderBackpackItem to add or remove
        quantityToAddOrRemove.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && ! newValue.equals("")){
                    if (newValue.charAt(newValue.length()-1) <'0' || newValue.charAt(newValue.length()-1) >'9'){
                        System.out.println("No");
                        quantityToAddOrRemove.setText(oldValue);
                    }
                } else {
                    System.out.println("Is clear");
                }  
            } 
        });
        
        // swtich scene
        addNewEnderBackpackItemButton.setOnAction(e->{stage.setScene(scene2());});
        
        return new Scene(pane1, 700, 450);
    }
    
    // Scene2: Add new EnderBackpackItem
    private Scene scene2 () {
        unsortedEnderBackpackItemNameArrayList.clear();
        unsortedEnderBackpackItemQuantityArrayList.clear();
        try {
            unsortedBox = new ItemBox(username);
            // loading unsorted item in itemBox
            for (EnderBackpackItem item: unsortedBox.list) {
                System.out.println("Adding item " + item.name + " with " + database_itemBox.retrieveQuantity(item.name , username) + " type " + item.type);
                unsortedEnderBackpackItemNameArrayList.add(item.name);
                unsortedEnderBackpackItemQuantityArrayList.add(database_itemBox.retrieveQuantity(item.name , username));
            }
        } catch (SQLException ex) {
            Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        pane1 = new BorderPane();
        Text unsortedEnderBackpackItemText = new Text("Name of new EnderBackpackItem to add: ");
        ObservableList<String> EnderBackpackItemsObservableValue = FXCollections.observableArrayList();
        EnderBackpackItemsObservableValue.clear();
        System.out.println("Size of list view: " + EnderBackpackItemsObservableValue.size());
        for (int i = 0; i < unsortedEnderBackpackItemNameArrayList.size(); i++) {
            EnderBackpackItemsObservableValue.add(unsortedEnderBackpackItemNameArrayList.get(i) + " (" + unsortedEnderBackpackItemQuantityArrayList.get(i) + ")");
        }
        System.out.println("Size of list view after: " + EnderBackpackItemsObservableValue.size());
        ListView<String> EnderBackpackItemsListView = new ListView<>();
        
        EnderBackpackItemsListView.getItems().addAll(EnderBackpackItemsObservableValue);
        EnderBackpackItemsListView.setPrefWidth(150);
        EnderBackpackItemsListView.setPrefHeight(200);
        EnderBackpackItemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
        Button addButton = new Button("Add");
        Button addAllButton = new Button("Add ALL");
        Button backButton = new Button("Back");
        
        // Background
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        Background background = new Background(new BackgroundImage(backgroundImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane1.setBackground(background);
        
        // Bottom pane
        HBox bottomPane = new HBox(addButton, addAllButton, backButton,backToMainPageButton);
        bottomPane.setPadding(new Insets(20,20,20,20));
        
        // Center pane
        ScrollPane centerPane = new ScrollPane();
        centerPane.setContent(EnderBackpackItemsListView);
        centerPane.setStyle("-fx-background-color: transparent");
        
        // Main pane
        pane1.setLeft(unsortedEnderBackpackItemText);
        pane1.setBottom(bottomPane);
        pane1.setCenter(centerPane);

        // Set on action
        // Add EnderBackpackItem(s)
        addButton.disableProperty().bind(EnderBackpackItemsListView.getSelectionModel().selectedItemProperty().isNull());
        addButton.setOnAction(e -> {
            ArrayList<String> EnderBackpackItemsToBeRemoved = new ArrayList<>(EnderBackpackItemsListView.getSelectionModel().getSelectedItems());
            System.out.println("Size" + EnderBackpackItemsToBeRemoved.size());
            System.out.println("EnderBackpackItems " + EnderBackpackItemsToBeRemoved.toString());
            for (int i = 0; i < EnderBackpackItemsToBeRemoved.size();) {
                String string = EnderBackpackItemsToBeRemoved.get(0);
                System.out.println("String: " +string);
                String EnderBackpackItemName = string.split("[(]")[0].trim();
                System.out.println("EnderBackpackItem name splited = " + EnderBackpackItemName);
                try {
                    System.out.println(database_itemBox.retrieveQuantity(EnderBackpackItemName, username));
                    int quantity = database_itemBox.retrieveQuantity(EnderBackpackItemName, username);
                    bst.add(EnderBackpackItemName, quantity);
                    database_item4.addItem(username, EnderBackpackItemName, quantity);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Removed " + EnderBackpackItemsToBeRemoved.remove(string));
                EnderBackpackItemsListView.getItems().remove(string);
                int index = getIndexInUnsorted(EnderBackpackItemName);
                unsortedEnderBackpackItemNameArrayList.remove(index);
                unsortedEnderBackpackItemQuantityArrayList.remove(index);
                System.out.println("i" + i);
                System.out.println("Size after remove: " + EnderBackpackItemsToBeRemoved.size());
                System.out.println("i<size: " + (i<EnderBackpackItemsToBeRemoved.size()));
            }
            centerPane.setContent(EnderBackpackItemsListView);
            pane1.setCenter(centerPane);
        });
        
        // Add all EnderBackpackItems
        addAllButton.setOnAction(e -> {
            for (int i = 0; i < unsortedEnderBackpackItemNameArrayList.size(); i++) {
                String itemName = unsortedEnderBackpackItemNameArrayList.get(i);
                System.out.println("name: "+ itemName);
                try {
                    int quantity = database_itemBox.retrieveQuantity(itemName, username);
                    bst.add(itemName, quantity);
                    database_item4.addItem(username, itemName, quantity);
                } catch (SQLException ex) {
                    Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            
            unsortedBox.clearItem(); // clear database itembox
            unsortedEnderBackpackItemNameArrayList.clear();
            unsortedEnderBackpackItemQuantityArrayList.clear();
            EnderBackpackItemsListView.getItems().clear();
            pane1.setCenter(EnderBackpackItemsListView);
        });
        
        // Back to previous scene
        backButton.setOnAction(e->{stage.setScene(scene1());});
        
        return new Scene(pane1, 700, 400);
    }
    
    public int getIndexInUnsorted(String EnderBackpackItemName) {
        for (int i = 0; i < unsortedEnderBackpackItemNameArrayList.size(); i++) {
            System.out.println(unsortedEnderBackpackItemNameArrayList.get(i));
            if (unsortedEnderBackpackItemNameArrayList.get(i).equals(EnderBackpackItemName)) {
                return i;
            }
        }
        return -1;
    }
    
    public void updateAll() {
        reminder.setText("");
        totalNoOfEnderBackpackItemInChest.setText("Total number of EnderBackpackItem in the automated sorting chest is: " + bst.getTotalQuantity());
        totalNoOfEnderBackpackItemOfCategory.setText("Total number of EnderBackpackItem for the category is: " + bst.getQuantityOfCateogory(categoryChosen));
        pane1.setCenter(new EnderBackpackItemsGridPane(categoryChosen));
        quantityToAddOrRemove.clear();
        if (selected != null) {
            selected.setStyle("-fx-border-color: rgba(0,0,0,0)"); 
            isSelected = false;
            selected = null; 
        }
        
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        launch(args);
    }
    
    private class SingleEnderBackpackItemPane extends BorderPane{
        private Image EnderBackpackItemImage = backgroundImage;
        private Text quantityOfEnderBackpackItem = new Text("0");
        private String category;
        private String EnderBackpackItemName;
        
//        private Text EnderBackpackItemName = new Text();

        public SingleEnderBackpackItemPane(String EnderBackpackItem, int quantity, String category) {
            this.category = category;
            this.EnderBackpackItemName = EnderBackpackItem;
            if (getImage(EnderBackpackItem) != null) {
                System.out.println(getImage(EnderBackpackItem));
                EnderBackpackItemImage = new Image(getClass().getResourceAsStream(imageFilePath+ getImage(EnderBackpackItem)));
            }else {
                System.out.println("Image " + EnderBackpackItem + " not found");
            }
            quantityOfEnderBackpackItem.setFill(Color.BLACK);
            quantityOfEnderBackpackItem.setFont(Font.font("Verdana", 10));
            quantityOfEnderBackpackItem.setText(quantity + "");
            
//            EnderBackpackItemName.setFill(Color.BLACK);
//            EnderBackpackItemName.setFont(Font.font("Verdana", 10));
//            EnderBackpackItemName.setText(EnderBackpackItem + ": ");

            ImageView imageEnderBackpackItem = new ImageView(EnderBackpackItemImage);
            imageEnderBackpackItem.setFitWidth(40);
            imageEnderBackpackItem.setFitHeight(40);
            this.setCenter(imageEnderBackpackItem);
            
            HBox HBbottom = new HBox();
//            HBbottom.getChildren().add(EnderBackpackItemName);
            HBbottom.getChildren().add(quantityOfEnderBackpackItem);
            HBbottom.setAlignment(Pos.BOTTOM_RIGHT);
            this.setBottom(HBbottom);
            this.setPrefWidth(40);
            this.setPrefHeight(50);
            setOnMouseClicked(e->{
                if (isSelected) {
                    selected.setStyle("-fx-border-color: rgba(0,0,0,0)"); 
                    if (! selected.equals(this)) {
                        this.setStyle("-fx-border-color: red");
                        selected = this;
                    }
                } else {
                    this.setStyle("-fx-border-color: red");
                    selected = this;
                    isSelected = true;
                    }
                }
            );
        }

        public String getCategory() {
            return category;
        }

        public String getEnderBackpackItemName() {
            return EnderBackpackItemName;
        }
        
        private String getImage(String EnderBackpackItem) {
            // get index of the EnderBackpackItem
            System.out.println(EnderBackpackItem);
            int index = -1;
            for (int i = 0; i < EnderBackpackItemsCollections.size(); i++) {
                if (EnderBackpackItemsCollections.get(i).equals(EnderBackpackItem)) {
                    index = i;
//                    System.out.println("Searched EnderBackpackItem image " + EnderBackpackItem);
                    break;
                }
//                System.out.println(EnderBackpackItemsCollections[i]);
            }
            if (index >= 0 && index < EnderBackpackItemsCollections.size()) {
                return EnderBackpackItemImagesCollections.get(index);
            }
            return null;
        }
    }
    
    private class EnderBackpackItemsGridPane extends GridPane{
        
        public EnderBackpackItemsGridPane(String input) {
            allSortedItems = input.equals(categoryChosen) ? bst.getParticularCategory(input).retriveAllItems() : bst.getParticularCategory(categoryChosen).retrivePossibleEnderBackpackItemsAfterwards(input);
            System.out.println("Category chosen: " + categoryChosen);
            this.getChildren().clear();
            System.out.println("The panes in the center: " + this.getChildren().size());
//            System.out.println("allSortedItems size: " + allSortedItems.size());
            int noOfColumn = 0;
            if (allSortedItems != null) {
                int j = 0;
                for (int i = 0; i < allSortedItems.size(); i++, noOfColumn++) {
                    String EnderBackpackItemName  = allSortedItems.get(i).getName();
                    SingleEnderBackpackItemPane pane = new SingleEnderBackpackItemPane(EnderBackpackItemName, bst.getParticularCategory(categoryChosen).getQuantity(EnderBackpackItemName), allSortedItems.get(i).getType());
                    add(pane, noOfColumn,j);
                    if ((i+1)%10 == 0) {
                        j++;
                        noOfColumn = -1;
                    }
                    System.out.println("i: " + i);
                }
            } 
            setHgap(2);
            setVgap(2);
            setPadding(new Insets(5,5,5,5));
            setPrefHeight(560);
            setPrefHeight(470);
        }
    }
}