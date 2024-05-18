package minecraft;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import minecraft.TeleportationNetworkController;
import minecraft.TeleportationNetworkController.Edge;
import minecraft.TeleportationNetworkController.Point;


public class TeleportationNetworkController_GUI extends Application {
    private static TeleportationNetworkController network1= new TeleportationNetworkController();;
    private static String imageFilePath = "/minecraft/icon/";
    private static Image defaultImage;
    private static Image mapImage;
    private static double r = 20;
    private static ArrayList<SingleTeleportationPoint> points = new ArrayList<>();

    private static Stage stage = new Stage(); 
    private static BorderPane pane1; 
    private static String[] nodesName = {"A","B","C","D"};
    private static String[] nodesImages = {"A.jpg", "B.jpg", "C.jpg", "D.jpg"};
    private static boolean isSelected = false;
    private static boolean selectionMode = false;
    private static boolean addingNewNode = false;
    private static ArrayList<SingleTeleportationPoint> selected = new ArrayList<>();
    private static ArrayList<Line> edges = new ArrayList<>();
    private static Text reminder = new Text();
    private static String username;
    private static Circle imaginaryCircle = new Circle(r);
    private static SingleTeleportationPoint currentPoint;
    private static SingleTeleportationPoint currentlySelected;
    private static Button backToMainPageButton = new Button("Back to main page");
    
    
    @Override
    public void start(Stage primaryStage) {
        defaultImage = new Image(getClass().getResourceAsStream(imageFilePath +"background.jpeg"));
        mapImage = new Image(getClass().getResourceAsStream(imageFilePath +"Map.jpg"));
        pane1 = new BorderPane();
        TeleportationNetworkController.Point n1 =  new TeleportationNetworkController.Point("A", "user1", 50,60);
        TeleportationNetworkController.Point n2 =  new TeleportationNetworkController.Point("B", "user1", 700, 500);
        
        TeleportationNetworkController.Point n3 =  new TeleportationNetworkController.Point("C", "user2", 400, 50);
        TeleportationNetworkController.Point n4 =  new TeleportationNetworkController.Point("D", "user2", 700, 100);
        network1.addNewNode(n1);
        network1.addNewNode(n2);
        network1.addNewNode(n3);
        network1.addNewNode(n4);
        
        n1.addNeighbour("C");
        n3.addNeighbours(new String[]{"A","B","C","D"});
        
        BackgroundSize bSize = new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false);
        Background background = new Background(new BackgroundImage(mapImage,BackgroundRepeat.NO_REPEAT,
            BackgroundRepeat.NO_REPEAT,
            BackgroundPosition.CENTER,
            bSize));
        pane1.setBackground(background);
        
        // Initiate the current point if any
        username = "user1";
        System.out.println("Username: " + username);
        
        backToMainPageButton.setOnAction(e->{
            try {
                MainPage mainPage = new MainPage();
                mainPage.start((Stage) ((Button) e.getSource()).getScene().getWindow());
//                stage.close();
            } catch (IOException ex) {
                Logger.getLogger(AutomatedSortingChest.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        stage = primaryStage;
        stage.setTitle("Teleportation Control Network");
        stage.setScene(new Scene(pane1, 1500,800));
        stage.show();
        
        mainScene();
        
    }

    // Scene 1: main scene
    public static void mainScene() {
         
        selectionMode = false;
        currentlySelected = null;
        Button addNewPointButton = new Button("Add a new teleportation point");
        Button removePointButton = new Button("Remove a teleportation point");
        reset();
        // Set on action
        addNewPointButton.setOnAction(e->{
            selectNewNodeLocation();
        });
        
        removePointButton.setOnAction(e->{
            removePoint();
        });
        
        // Bottom pane
        HBox bottomPane = new HBox(addNewPointButton, removePointButton);
        bottomPane.setPadding(new Insets(10,10,10,10));
        pane1.setBottom(bottomPane);
        
    }
    
    
    public static void removePoint() {
        reminder.setText("Choose the teleportation point you wish to remove.");
        reset();
        Button cancelButton = new Button("Cancel");
        Button confirmRemoveButton = new Button("Confirm");        
        canAllBeSelected(false);
        selectionMode = true;
        
        // dim the node that cannot be removed as not belong to the user
        for (SingleTeleportationPoint p : points) {
            if (p.getOwnerName().equals(username)) {
                p.canSelect = true;
            } else {
                p.circleFrame.setOpacity(0.5);
            }
        }
        
        HBox bottomPane = new HBox(cancelButton, confirmRemoveButton);
        pane1.setBottom(bottomPane); // make user choose to cancel or confirm

        confirmRemoveButton.setOnAction(e->{
            if (isSelected) {
                for (SingleTeleportationPoint point : selected) {
                    network1.removeNode(network1.getNode(point.getNodeName()));
                    points.remove(point);
                }
                reminder.setText("You have remove " + selected.size() + " nodes.");
                if (! points.contains(currentPoint))  
                    currentPoint = null;
                mainScene();
            } else {
                reminder.setText("Please select the point you wish to remove.");
            }
            
        });

        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
    }
    
    public static void neighbourSelection () {
        reminder.setText("Who do you want to be neighbour with?");
        reset();
        if (addingNewNode) {
            currentlySelected = points.get(points.size()-1);
            System.out.println("Currently selected : " + currentlySelected);
            addingNewNode = false;
        }
        selectionMode = true;
        canAllBeSelected(true); // default all can be selected
        Button confirmButton = new Button("Let's become neighbours");
        Button cancelButton = new Button("Cancel");
        // check if the point is current point OR neighbour already, cannot be chosen to add anymore
        for (SingleTeleportationPoint point : points) {
            // Highlight the current point
            if (point.getNodeName().equals(currentlySelected.getNodeName())) {
                point.canSelect = false;
                point.circleFrame.setOpacity(0.8);
                point.filter.setFill(Color.rgb(255,0,0,0.3));
                System.out.println(point + " can be selected? " + point.canSelect);
            } else if (currentPoint != null && point.getNodeName().equals(currentPoint.getNodeName())) {
                point.filter.setFill(Color.TRANSPARENT);
                point.circleFrame.setOpacity(1);
            }
            for (int i = 0; i < currentlySelected.getNeighbours().size(); i++) {
                System.out.println(currentlySelected.getNeighbours().get(i));
                if (point.getNodeName().equals(currentlySelected.getNeighbours().get(i)) || point.getNodeName().equals(currentlySelected.getNodeName())) {
                    point.canSelect = false;
                    System.out.println(point +" can't be selected");
                    point.circleFrame.setOpacity(0.5);
                    break;
                }
            }
        }
        
        confirmButton.setOnAction(e->{
            if (isSelected) {
                int size = selected.size();
                String text = "Yeah! Your are now the neighbour of ";
                for (int i = 0; i< size-1; i++) {
                    System.out.println("added neighbour with " + selected.get(i) + "? " + network1.getNode(currentlySelected.getNodeName()).addNeighbour(selected.get(i).getNodeName()));;
                    text += selected.get(i).getNodeName() + ", ";
                }
                System.out.println("added neighbour with " + selected.get(size-1) + "? " + network1.getNode(currentlySelected.getNodeName()).addNeighbour(selected.get(size-1).getNodeName()));;
                text += (size>1) ? " and "+ selected.get(size-1) : ""  + selected.get(size-1);
                selected.clear();
                isSelected = false;
                reminder.setText(text);
                mainScene();
            } else {
                reminder.setText("Please select the new neighbour");
            }
            
        });
        
        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
        
        HBox bottomPane = new HBox(cancelButton, confirmButton);
        bottomPane.setPadding(new Insets(10,10, 10,10));
        pane1.setBottom(bottomPane);
        
    }
    
    public static void removeNeighbourSelection () {
        reminder.setText("Which neighbour do you wish to remove? (Argue ? lmao!)");
        reset();
        isSelected = false;
        selectionMode = true;
        canAllBeSelected(false);
        Button confirmButton = new Button("REMOVE!");
        Button cancelButton = new Button("Cancel. i have cooled down");
        for (SingleTeleportationPoint point : points) {
            // Highlight the current point
            if (point.getNodeName().equals(currentlySelected.getNodeName())) {
                point.circleFrame.setOpacity(0.8);
                point.filter.setFill(Color.rgb(255,0,0,0.3));
                System.out.println(point + " can be selected? here " + point.canSelect);
            } else if (currentPoint != null && point.getNodeName().equals(currentPoint.getNodeName())) {
                point.circleFrame.setOpacity(1);
                point.filter.setFill(Color.TRANSPARENT);
            }
            for (int i = 0; i < currentlySelected.getNeighbours().size(); i++) {
                System.out.println(currentlySelected.getNeighbours().get(i));
                if (point.getNodeName().equals(currentlySelected.getNeighbours().get(i))) {
                    point.canSelect = true;
                    System.out.println(point +" can be selected");
                    break;
                }
            }
            
            if (! point.canSelect) {
                System.out.println(point +" can't be selected");
                point.circleFrame.setOpacity(0.5);
            }
        }
        
        confirmButton.setOnAction(e->{
            if (isSelected) {
                String text = "Wooohh! You are now no longer connected to ";
                int size = selected.size();
                for (int i = 0; i< size-1; i++) {
                    System.out.println("remove neighbour with " + selected.get(i) + "? " + network1.getNode(currentlySelected.getNodeName()).removeNeighbour(selected.get(i).getNodeName()));
                    text += selected.get(i).getNodeName() + ", ";
                }
                System.out.println("remove neighbour with " + selected.get(size-1) + "? " + network1.getNode(currentlySelected.getNodeName()).removeNeighbour(selected.get(size-1).getNodeName()));
                text += (size>1) ? " and " + selected.get(size-1) : "" + selected.get(size-1);
                selected.clear();
                isSelected = false;
                reminder.setText(text);
                mainScene();
            } else {
                reminder.setText("Please select the neighbour(s) to remove (sad");
            }
            
        });
        
        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
        
        HBox bottomPane = new HBox(cancelButton, confirmButton);
        bottomPane.setPadding(new Insets(10,10, 10,10));
        pane1.setBottom(bottomPane);
    }    
    
    // Popout: to insert the information of the new node
    public static void newPointInformationWindow() {
        pane1.setDisable(true);
        Button cancelButton = new Button("Cancel");
        Button confirmAddButton = new Button("Confirm");
        Text text = new Text("Your new teleportation point's name: ");
        Text warningText = new Text("");
        TextField TPnameTextField = new TextField();
        Stage stage = new Stage();
        stage.setWidth(400);
        stage.setHeight(200);
        BorderPane pane = new BorderPane();
        
        // Center pane
        HBox centerPane = new HBox(text, TPnameTextField);
        centerPane.setPadding(new Insets(10,10,10,10));

        //Bottom pane
        HBox bottomPane = new HBox(cancelButton, confirmAddButton);
        bottomPane.setPadding(new Insets(10,10,10,10));

        pane.setCenter(centerPane);
        pane.setBottom(bottomPane);
        pane.setTop(warningText);
        
        
        confirmAddButton.setOnAction(e->{
            pane1.setDisable(false);
            if (TPnameTextField.getText()!=null && ! TPnameTextField.getText().equals("")) {
                if (network1.addNewNode(new Point(TPnameTextField.getText(), username, imaginaryCircle.getLayoutX(), imaginaryCircle.getLayoutY()))) {
                    stage.close();
                    addingNewNode = true;
                    pane.getChildren().remove(imaginaryCircle);
                    enquiryAddNeighoursWindow();
                } else {
                    warningText.setText("Sorry, the teleportation point with same name exist already. Please use anthor name.");
                }
            } else {
                warningText.setText("Please give a name to your teleportation point");
            }
            
        });
        
        TPnameTextField.textProperty().addListener(new ChangeListener<String>(){
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue == null || newValue.equals("")){
                    reminder.setText("Let's give your teleportation point a new name");
                    TPnameTextField.setStyle("-fx-border-color : blue");
                } else {
                    reminder.setText("");
                    TPnameTextField.setStyle("-fx-border-color : rgba(0,0,0,0)");
                }
            }

        });
        
        cancelButton.setOnAction(e->{
            pane1.setDisable(false);
            stage.close();
            reminder.setText("Opps! You failed in creating a new point!");
            mainScene();
        });
        
        stage.setScene(new Scene(pane));
        stage.setTitle("Construct your new teleportation point");
        stage.show();
    }
    
    // Popout: to ask if the new node want to add a new neighbour
    public static void enquiryAddNeighoursWindow() {
        Stage stage = new Stage();
        BorderPane pane = new BorderPane();
        Text text = new Text("Do you want to add neighbours? ");
        Button yesButton = new Button("Yes! Let's join!");
        Button noButton = new Button("Nope! I want to be alone!");
        
        // Bottom pane 
        HBox bottomPane = new HBox(yesButton, noButton);
        
        pane.setCenter(text);
        pane.setBottom(bottomPane);
        
        // Button Action
        yesButton.setOnAction(e->{
            stage.close();
            neighbourSelection();
        });
        
        noButton.setOnAction(e->{
            stage.close();
            reminder.setText("Congratulation " + username + "! You have sucessfully create a new point!");
            mainScene();
        });
        
        stage.setScene(new Scene(pane));
        stage.show();
        
    }
    
    // Set all node to be can be selected or not at one time
    public static void canAllBeSelected(Boolean value) {
        for (SingleTeleportationPoint p : points) {
            p.canSelect = value;
            System.out.println(p + " can select? " + p.canSelect);
        }
    }
    
    public static void selectNewNodeLocation() {
        selectionMode = true; // when set to true, only the permitted points can be selected, but all are disabled in the next line
        canAllBeSelected(false);
        Button cancelButton = new Button("Cancel");
        Button confirmAddButton = new Button("Confirm");
        HBox bottomPane = new HBox(cancelButton, confirmAddButton);
        bottomPane.setPadding(new Insets(10,10,10,10));
        
        reminder.setText("Choose a new location for your teleportation point. ");
        pane1.setBottom(bottomPane);      
        // Click action to add a new node
        Pane pane = new Network();
        pane.setOnMouseClicked(e->{
            double x = e.getX();
            double y = e.getY();
            if (x>= r && x <= pane.getWidth()-r && y>= r && y <= pane.getHeight()-r) {
                boolean canBeAdded = true;
                
                for (SingleTeleportationPoint child : points) {
                    System.out.println("x: " + x);
                    System.out.println("y: " + y);
                    double distance = Math.pow(Math.pow((child.getLayoutX()-x), 2) + Math.pow((child.getLayoutY()-y), 2), 0.5);
                    System.out.println("point: " + child + " distance: " + distance);
                    if ( distance < r*2) {
                        canBeAdded = false;
                        break;
                    }
                }
                if (canBeAdded && pane.getChildren().contains(imaginaryCircle)) {
                    System.out.println("here1");
                    imaginaryCircle.setLayoutX(e.getX());
                    imaginaryCircle.setLayoutY(e.getY());
                } else if (canBeAdded) {
                    System.out.println("here2");
                    pane.getChildren().add(imaginaryCircle);
                    imaginaryCircle.setLayoutX(e.getX());
                    imaginaryCircle.setLayoutY(e.getY());
                } else {
                    System.out.println("here3");
                }
            }
        });
        
        confirmAddButton.setOnAction(e->{
            reminder.setText("Wow! Fill in the information to create it instantly!");
            pane1.setBottom(null); // remove the options button
            newPointInformationWindow();
        });
        
        cancelButton.setOnAction(e->{
            reminder.setText("");
            mainScene();
        });
        
        pane1.setCenter(pane);
    }
    
    private static class SingleTeleportationPoint extends Pane{
        Image image = defaultImage;
        Point point;
        boolean canSelect;
        Button goButton = new Button("GO!!!");
        Button noButton = new Button("Nope, just take a look"); 
        
        Circle circleFrame = new Circle(r);
        Circle filter = new Circle(r);
        public SingleTeleportationPoint(Point point) {
            this.point = point;
            
            if (getImage(getNodeName()) != null) {
                image = getImage(getNodeName());
            }

            circleFrame.setFill(new ImagePattern(image));
                filter.setFill(Color.rgb(255, 0, 0, this.equals(currentlySelected)? 0.3 : 0));
            
            
            getChildren().addAll(circleFrame, filter);
            
            setOnMouseClicked ( e -> {
                if (selectionMode) {
                    System.out.println("is selection mode");
                    if (canSelect) {
                        System.out.println(this + " can select");
                        // if previously selected, unselect it
                        if (selected.contains(this)) {
                            filter.setFill(Color.TRANSPARENT); 
                            selected.remove(this);
                            isSelected = ! selected.isEmpty();
                        } else {
                            // if haven't selected yet, select it
                            filter.setFill(Color.rgb(0, 0,255, 0.3));
                            selected.add(this);
                            isSelected = true;
                        }    
                    }
                } else {
                    System.out.println("not selection mode");
                    pane1.setRight(new VBox());
                    pane1.setBottom(new HBox());
                    if (currentlySelected != null) {
                        currentlySelected.filter.setFill(Color.TRANSPARENT); 
                        currentlySelected.circleFrame.setOpacity(1);
                    }
                    currentlySelected = this;
                    filter.setFill(Color.rgb(255, 0, 0,0.3));
                    System.out.println("current point: " + currentPoint);
                    if (username.equals(getOwnerName())) { // this node is owned by the current user
                        reviewNode();
                    } else if (currentPoint == null) { // user haven't create any nodes
                        reminder.setText("Wanna go to other points? Create/select one teleportation point & join them now!");
                        mainScene();
                    } else if (! getNeighbours().isEmpty()) {
                        reset();
                        ArrayList<Point> shortestPath = network1.shortestPath(currentPoint.getNodeName(), getNodeName());
                        if (shortestPath != null) {
                            if (currentPoint != null && currentPoint.getNodeName().equals(this.getNodeName())) {
                                reminder.setText("You are at this point already!");
                            } else {
                                String text = "The shortest path from your current point " + currentPoint  + " to " + this + " is " ;
                                System.out.println("Size: " + shortestPath.size());
                                for (int i = (shortestPath.size()-1); i>0; i--) {
                                    System.out.println("i: " + i);
                                    text += (shortestPath.get(i)+" -> ");
                                }
                                text += shortestPath.get(0) + " with length of " + network1.shortestDistance(currentPoint.getNodeName(), getNodeName())+ ". Do you want to go? ";
                                reminder.setText(text);
                                System.out.println("text: " + text);
                                pane1.setTop(reminder);
                                HBox bottomPane = new HBox(goButton, noButton);
                                bottomPane.setPadding(new Insets(10,10,10,10));
                                pane1.setBottom(bottomPane);    
                            }
                        } else {
                            reminder.setText("There is no way to go from " + currentPoint + " to " + this);
                            mainScene();
                        }
                    } else {
                        reminder.setText("There is no way to go from " + currentPoint + " to " + this);
                        mainScene();
                    }
                }
                }
            );
            
            goButton.setOnAction(e->{
                goNewPoint();
            });
            
            noButton.setOnAction(e->{
                reminder.setText("");
                mainScene();
            });
        }
        
        public void goNewPoint() {
            currentPoint = this;
            reminder.setText("You are now at " + currentPoint + "! Wow!");
            mainScene();
        }
        
        public void reviewNode() {
            reminder.setText("");
            reset();
            
            Text nameText = new Text("Name: " + getNodeName());
            Text neighbourText = new Text("Neighbour: " + getNeighbours().toString());
            Button addNeigbourButton = new Button("Add neighbour");
            Button removeNeigbourButton = new Button("Remove neighbour");
            Button updateImageButton = new Button("Update point's image");
            Button goButton = new Button("GO!!!");
            Button backButton = new Button("Review done!");
            VBox pane = new VBox(nameText, neighbourText,addNeigbourButton,removeNeigbourButton,goButton,updateImageButton, backButton);
            pane1.setRight(pane);
            
            addNeigbourButton.setOnAction(e->{
                neighbourSelection();
            });
            
            removeNeigbourButton.setOnAction(e->{
                removeNeighbourSelection();
            });
            
            backButton.setOnAction(e->{
                reminder.setText("");
                mainScene();
            });
            
            goButton.setOnAction(e->{
                goNewPoint();
            });
            
            //---------------------------------------------------------------------------
            updateImageButton.setOnAction(e->{
                
            });
            //---------------------------------------------------------------------------
        }

        public String getNodeName() {
            return point.getNameOfTeleportationPoint();
        }

        public ArrayList<String> getNeighbours() {
            return point.getNeighboursInString();
        }

        public String getOwnerName() {
            return point.getOwner();
        }
        
        private Image getImage(String nodeName) {
            // get index of the item
            System.out.println(nodeName);
            int index = -1;
            for (int i = 0; i < nodesName.length; i++) {
                if (nodesName[i].equals(nodeName)) {
                    index = i;
                    System.out.println("Searched item image " + nodeName);
                    break;
                }
//                System.out.println(itemsCollections[i]);
            }
            if (index >= 0 && index < nodesName.length) {
                return new Image(getClass().getResourceAsStream(imageFilePath +nodesImages[index]));
            }
            
            return null;
        }

        @Override
        public String toString() {
            return point.getNameOfTeleportationPoint();
        }
        
    }
    
    private static class Network extends Pane{

        public Network() {
            canAllBeSelected(false);
            this.getChildren().clear();
            points.clear();
            imaginaryCircle.setFill(Color.rgb(230, 150, 0));
            
            // Add edges
            List<Edge> edges = network1.getEdges();
            for (Edge edge : edges) {
                Line line = new Line(edge.getN1().getX(), edge.getN1().getY(), edge.getN2().getX(), edge.getN2().getY());
                getChildren().add(line);
            }
            
            // Add nodes
            List<Point> nodesList = network1.getNodes();
            for (Point node : nodesList) {
                SingleTeleportationPoint point = new SingleTeleportationPoint(node);
                getChildren().add(point);
                point.setLayoutX(node.getX());
                point.setLayoutY(node.getY());
                points.add(point);
                System.out.println("point size: " + points.size());
                if (currentPoint != null && point.getNodeName().equals(currentPoint.getNodeName())) {
                    point.filter.setFill(Color.rgb(0, 255, 0,0.5));
                    point.circleFrame.setOpacity(0.8);
                } else if (currentlySelected != null && point.getNodeName().equals(currentlySelected.getNodeName())) {
                    point.filter.setFill(Color.rgb(0, 0, 255,0.5));
                    point.canSelect  = false;
                    point.circleFrame.setOpacity(0.8);
                }
            }
//            // default current point
//            if (currentPoint == null) {
//                for (SingleTeleportationPoint point : points) {
//                    if (point.getOwnerName().equals(username)) {
//                        currentPoint = point;
//                        point.filter.setFill(Color.rgb(0, 255, 0,0.5));
//                        point.circleFrame.setOpacity(0.8);
//                        System.out.println("Current point is " + currentPoint);
//                        break;
//                    }
//                }
//            }
        }
    }
    
    public static void reset() {
        pane1.setCenter(new Network());
        pane1.setTop(new HBox(backToMainPageButton, reminder));
        pane1.setBottom(new HBox());
        pane1.setRight(new VBox());
        selectionMode = false;
        isSelected = false;
        selected.clear();
    }
    
    public static void main(String[] args) {
       
        launch(args);
    }
}

