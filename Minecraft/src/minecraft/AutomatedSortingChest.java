package minecraft;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
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
import minecraft.BSTs;
import minecraft.Item;


public class AutomatedSortingChest extends Application {
    private static BSTs<Item> bst = new BSTs<>();;
    private static String imageFilePath = "/minecraft/icon/";
    
//    private String[] categories = {"Tools" , "Food", "Arrows", "Decorations", "MobEggs", "Weapons", "Armor", "Materials", "Transportations", "Potions", "Records", "Dyes"};
    static String[] itemsCollections = {"Axes", "Shovels", "Apple", "Clownfish", "Swords", "Diamond","Potion of Decay", "Potion of Invisibility" , "Bucket" , "Wood" , "Stone" , "Red stone"};
    static String[] ItemImagesCollections = {"Axes.jpeg", "Shovels.jpg", "Apple.jpg", "Clownfish.jpg", "Swords.jpg", "Diamond.jpg","Potion_of_Decay.jpg","Potion_of_Invisibility.gif","Bucket.jpg","Oak_Wood.jpg","Stone.jpg","Redstone.jpg"};
    
    private static boolean isSelected = false;
    private static SingleItemPane selected;
    private static String categoryChosen = "(Include ALL)";
    private static List<Item> allItems= bst.getParticularCategory(categoryChosen).retriveAllItems();;
    private static Image backgroundImage;
    private static Stage stage = new Stage();
     
    private static Text reminder = new Text();
    private static Button backToMainPageButton = new Button("Back to main page");
    private static Text totalNoOfItemInChest = new Text();
    private static Text totalNoOfItemOfCategory = new Text();
    private static TextField searchItemTextField = new TextField(categoryChosen);
    private static TextField quantityToAddOrRemove = new TextField();
    private static BorderPane pane1 = new BorderPane();
    
    
    private static ArrayList<String> unsortedItemNameArrayList = new ArrayList<>();
    private static ArrayList<Integer> unsortedItemQuantityArrayList = new ArrayList<>();
    private static ArrayList<String> unsortedItemCategoryArrayList = new ArrayList<>();
        
    @Override
    public void start(Stage primaryStage) {
        
        
        Item[] items = new Item[9];
        items[0] = new Item("Axes", "Tools");
        items[1] = new Item("Shovels", "Tools");
        items[2] = new Item("Apple", "Food");
        items[3] = new Item("Clownfish", "Food");
        items[4] = new Item("Swords", "Weapons");
        items[5] = new Item("Diamond", "Materials");
        items[6] = new Item("Potion of Decay", "Potions");
        items[7] = new Item("Potion of Invisibility", "Potions");
        items[8] = new Item("Bucket", "Tools");
        
        
        for (int i = 0; i < items.length; i++) {
            bst.add(items[i]);
        }
        bst.add(items[0],100);
        
        unsortedItemNameArrayList.add("Axes");
        unsortedItemNameArrayList.add("Wood");
        unsortedItemNameArrayList.add("Stone");
        unsortedItemNameArrayList.add("Red stone");
        unsortedItemQuantityArrayList.add(10);
        unsortedItemQuantityArrayList.add(20);
        unsortedItemQuantityArrayList.add(30);
        unsortedItemQuantityArrayList.add(40);
        unsortedItemCategoryArrayList.add("Tools");
        unsortedItemCategoryArrayList.add("Materials");
        unsortedItemCategoryArrayList.add("Materials");
        unsortedItemCategoryArrayList.add("Materials");
        
        backToMainPageButton.setOnAction(e->{
            try {
                MainPage mainPage = new MainPage();
                mainPage.start((Stage) ((Button) e.getSource()).getScene().getWindow());
//                stage.close();
            } catch (IOException ex) {
                Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        backgroundImage = new Image(getClass().getResourceAsStream(imageFilePath + "background.jpeg"));
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
        
        Button addNewItemButton = new Button("Add New Item");
        Button addcurrentItemButton = new Button("Add");
        Button removeCurrentItemButton = new Button("Remove");
        
        // Background
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        Background background = new Background(new BackgroundImage(backgroundImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane1.setBackground(background);
        
        // Top pane
        VBox topPane = new VBox(); 
        topPane.getChildren().add(totalNoOfItemInChest);
        topPane.getChildren().add(categoriesLabel);
        topPane.getChildren().add(categoriesChoiceBox);
        topPane.getChildren().add(searchItemTextField);
        topPane.getChildren().add(totalNoOfItemOfCategory);
        
        // Bottom pane
        HBox bottomPane = new HBox();
        Text quantityText = new Text("Quantity: ");
        bottomPane.getChildren().add(quantityText);
        bottomPane.getChildren().add(quantityToAddOrRemove);
        bottomPane.getChildren().add(addcurrentItemButton);
        bottomPane.getChildren().add(removeCurrentItemButton);
        bottomPane.getChildren().add(reminder);
        bottomPane.getChildren().add(backToMainPageButton);
        
        
        // Right pane
        pane1.setRight(addNewItemButton);
        
        // Main pane
        pane1.setCenter(new ItemsGridPane(categoryChosen));
        pane1.setTop(topPane);
        pane1.setBottom(bottomPane);

        // Set on action
        // Select a new category
        categoriesChoiceBox.getSelectionModel().selectedIndexProperty().addListener(new ChangeListener<Number>(){
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                categoryChosen = categoriesChoiceBox.getItems().get((Integer)newValue);
                System.out.println(categoryChosen);
                searchItemTextField.setText(categoryChosen);
            }
        });
        
        // search item to display the possible item
        searchItemTextField.textProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            pane1.setCenter(new ItemsGridPane(newValue));
        });
        
        addcurrentItemButton.setOnAction(e -> {
            if (isSelected && quantityToAddOrRemove.getText() != null && ! quantityToAddOrRemove.getText().equals("")) {
                bst.add(new Item(selected.getItemName(), selected.getCategory()), Integer.valueOf(quantityToAddOrRemove.getText()));
                updateAll();
            } else if (! isSelected) {
                reminder.setText("Please select a item to add.");
            } else {
                reminder.setText("Please enter the amount to add.");
            }
        });
        
        // Remove amount of current item
        removeCurrentItemButton.setOnAction(e -> {
            if (isSelected && quantityToAddOrRemove.getText() != null && ! quantityToAddOrRemove.getText().equals("")) {
                bst.remove(new Item(selected.getItemName(), selected.getCategory()), Integer.valueOf(quantityToAddOrRemove.getText()));
                updateAll();
            } else if (! isSelected) {
                reminder.setText("Please select a item to remove.");
            } else {
                reminder.setText("Please enter the amount to remove.");
            }
        });
        
        // enter the amount of item to add or remove
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
        addNewItemButton.setOnAction(e->{stage.setScene(scene2());});
        return new Scene(pane1, 700, 450);
    }
    
    // Scene2: Add new item
    private Scene scene2 () {
        pane1 = new BorderPane();
        Text unsortedItemText = new Text("Name of new item to add: ");
        
        ObservableList<String> itemsObservableValue = FXCollections.observableArrayList();
        System.out.println("Size of list view: " + itemsObservableValue.size());
        for (int i = 0; i < unsortedItemNameArrayList.size(); i++) {
            itemsObservableValue.add(unsortedItemNameArrayList.get(i) + " (" + unsortedItemQuantityArrayList.get(i) + ")");
        }
        ListView<String> itemsListView = new ListView<>();
        itemsListView.getItems().addAll(itemsObservableValue);
        itemsListView.setPrefWidth(150);
        itemsListView.setPrefHeight(200);
        itemsListView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        
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
        centerPane.setContent(itemsListView);
        centerPane.setStyle("-fx-background-color: transparent");
        
        // Main pane
        pane1.setLeft(unsortedItemText);
        pane1.setBottom(bottomPane);
        pane1.setCenter(centerPane);

        // Set on action
        // Add item(s)
        addButton.disableProperty().bind(itemsListView.getSelectionModel().selectedItemProperty().isNull());
        addButton.setOnAction(e -> {
            ArrayList<String> itemsToBeRemoved = new ArrayList<>(itemsListView.getSelectionModel().getSelectedItems());
            System.out.println("Size" + itemsToBeRemoved.size());
            System.out.println("Items " + itemsToBeRemoved.toString());
            for (int i = 0; i < itemsToBeRemoved.size();) {
                String string = itemsToBeRemoved.get(0);
                System.out.println("String: " +string);
                String itemName = string.split("[(]")[0].trim();
                System.out.println("Item name splited = " + itemName);
               bst.add(new Item(itemName, unsortedItemCategoryArrayList.get(getIndexInUnsorted(itemName))), unsortedItemQuantityArrayList.get(getIndexInUnsorted(itemName)));
                System.out.println("Removed " + itemsToBeRemoved.remove(string));
               itemsListView.getItems().remove(string);
               int index = getIndexInUnsorted(itemName);
               unsortedItemNameArrayList.remove(index);
               unsortedItemCategoryArrayList.remove(index);
               unsortedItemQuantityArrayList.remove(index);
                System.out.println("i" + i);
                System.out.println("Size after remove: " + itemsToBeRemoved.size());
                System.out.println("i<size: " + (i<itemsToBeRemoved.size()));
            }
            centerPane.setContent(itemsListView);
            pane1.setCenter(centerPane);
        });
        
        // Add all items
        addAllButton.setOnAction(e -> {
            for (int i = 0; i < unsortedItemNameArrayList.size(); i++) {
               bst.add(new Item(unsortedItemNameArrayList.get(i), unsortedItemCategoryArrayList.get(i)), unsortedItemQuantityArrayList.get(i));
            }
            unsortedItemCategoryArrayList.clear();
            unsortedItemNameArrayList.clear();
            unsortedItemQuantityArrayList.clear();
            itemsListView.getItems().clear();
            pane1.setCenter(itemsListView);
        });
        
        // Back to previous scene
        backButton.setOnAction(e->{stage.setScene(scene1());});
        
        return new Scene(pane1, 700, 400);
    }
    
    public int getIndexInUnsorted(String itemName) {
        for (int i = 0; i < unsortedItemNameArrayList.size(); i++) {
            System.out.println(unsortedItemNameArrayList.get(i));
            if (unsortedItemNameArrayList.get(i).equals(itemName)) {
                return i;
            }
        }
        return -1;
    }
    
    public void updateAll() {
        reminder.setText("");
        totalNoOfItemInChest.setText("Total number of item in the automated sorting chest is: " + bst.getTotalQuantity());
        totalNoOfItemOfCategory.setText("Total number of item for the category is: " + bst.getQuantityOfCateogory(categoryChosen));
        pane1.setCenter(new ItemsGridPane(categoryChosen));
        quantityToAddOrRemove.clear();
        if (selected != null) {
            selected.setStyle("-fx-border-color: rgba(0,0,0,0)"); 
            isSelected = false;
            selected = null; 
        }
        
    }
    
    public static void main(String[] args) throws FileNotFoundException {
        Item[] items = new Item[9];
        items[0] = new Item("Axes", "Tools");
        items[1] = new Item("Shovels", "Tools");
        items[2] = new Item("Apple", "Food");
        items[3] = new Item("Clownfish", "Food");
        items[4] = new Item("Swords", "Weapons");
        items[5] = new Item("Diamond", "Materials");
        items[6] = new Item("Potion of Decay", "Potions");
        items[7] = new Item("Potion of Invisibility", "Potions");
        items[8] = new Item("Bucket", "Tools");
        
        
        for (int i = 0; i < items.length; i++) {
            bst.add(items[i]);
        }
        bst.add(items[0],100);
        
        unsortedItemNameArrayList.add("Axes");
        unsortedItemNameArrayList.add("Wood");
        unsortedItemNameArrayList.add("Stone");
        unsortedItemNameArrayList.add("Red stone");
        unsortedItemQuantityArrayList.add(10);
        unsortedItemQuantityArrayList.add(20);
        unsortedItemQuantityArrayList.add(30);
        unsortedItemQuantityArrayList.add(40);
        unsortedItemCategoryArrayList.add("Tools");
        unsortedItemCategoryArrayList.add("Materials");
        unsortedItemCategoryArrayList.add("Materials");
        unsortedItemCategoryArrayList.add("Materials");
        
        launch(args);
    }
    
    private class SingleItemPane extends BorderPane{
        private Image itemImage = backgroundImage;
        private Text quantityOfItem = new Text("0");
        private String category;
        private String ItemName;
        
//        private Text itemName = new Text();

        public SingleItemPane(String item, int quantity, String category) {
            this.category = category;
            this.ItemName = item;
            if (getImage(item) != null) {
                System.out.println(getImage(item));
                itemImage = new Image(getClass().getResourceAsStream(imageFilePath+ getImage(item)));
            }else {
                System.out.println("Image " + item + " not found");
            }
            quantityOfItem.setFill(Color.BLACK);
            quantityOfItem.setFont(Font.font("Verdana", 10));
            quantityOfItem.setText(quantity + "");
            
//            itemName.setFill(Color.BLACK);
//            itemName.setFont(Font.font("Verdana", 10));
//            itemName.setText(item + ": ");

            ImageView imageItem = new ImageView(itemImage);
            imageItem.setFitWidth(40);
            imageItem.setFitHeight(40);
            this.setCenter(imageItem);
            
            HBox HBbottom = new HBox();
//            HBbottom.getChildren().add(itemName);
            HBbottom.getChildren().add(quantityOfItem);
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

        public String getItemName() {
            return ItemName;
        }
        
        private String getImage(String item) {
            // get index of the item
            System.out.println(item);
            int index = -1;
            for (int i = 0; i < itemsCollections.length; i++) {
                if (itemsCollections[i].equals(item)) {
                    index = i;
//                    System.out.println("Searched item image " + item);
                    break;
                }
//                System.out.println(itemsCollections[i]);
            }
            if (index >= 0 && index < itemsCollections.length) {
                return ItemImagesCollections[index];
            }
            return null;
        }
    }
    
    private class ItemsGridPane extends GridPane{
        
        public ItemsGridPane(String input) {
            allItems = input.equals(categoryChosen) ? bst.getParticularCategory(input).retriveAllItems() : bst.getParticularCategory(categoryChosen).retrivePossibleItemsAfterwards(input);
            System.out.println("Category chosen: " + categoryChosen);
            getChildren().clear();
            int noOfColumn = 0;
            if (allItems != null) {
                int j = 0;
                for (int i = 0; i < allItems.size(); i++, noOfColumn++) {
                    String itemName  = allItems.get(i).getName();
                    SingleItemPane pane = new SingleItemPane(itemName, bst.getParticularCategory(categoryChosen).getQuantity(itemName), allItems.get(i).getCategory());
                    add(pane, noOfColumn,j);
                    if ((i+1)%10 == 0) {
                        j++;
                        noOfColumn = -1;
                    }
                        
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