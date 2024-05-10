/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author USER
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class Inventory {
    private Map<String, Integer> items; // Map to store items and their quantities
    private Connection connection;

    public Inventory() {
        items = new HashMap<>();
        connectToDatabase();
        loadInventoryFromDatabase();
    }

    // Method to establish connection with the database
    private void connectToDatabase() {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/minecraft_inventory", "root", "040917");
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to retrieve inventory data from the database
    private void loadInventoryFromDatabase() {
        // List of initial items
        String[] initialItems = {"wheat", "wheat seed", "potato", "carrot", "melon", "melon seed", 
                                 "pumpkin", "pumpkin seed", "sweet berry", "sweet berry bushes",
                                 "beetroot", "beetroot seed", "hoe", "axe"};

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM inventory");
            ResultSet resultSet = statement.executeQuery();

            // Populate the items HashMap with existing inventory data from the database
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int quantity = resultSet.getInt("quantity");
                items.put(itemName, quantity);
            }

            // Check if any items are missing in the inventory (not present in the database)
            for (String itemName : initialItems) {
                if (!items.containsKey(itemName)) {
                    // If an item is missing, add it to the inventory with an initial quantity of 0
                    items.put(itemName, 0);
                    // Also, insert the new item into the database with a quantity of 0
                    insertNewItem(itemName, 0);
                }
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to insert a new item into the database with an initial quantity of 0
    private void insertNewItem(String itemName, int quantity) {
        try {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO inventory (item_name, quantity) VALUES (?, ?)");
            statement.setString(1, itemName);
            statement.setInt(2, quantity);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to add an item to the inventory and update the database
    public void addInventory(String itemName, int quantity) {
        // Check if the item already exists in the inventory
        if (items.containsKey(itemName)) {
            // If the item exists, update its quantity by adding the new quantity
            int currentQuantity = items.get(itemName);
            items.put(itemName, currentQuantity + quantity);
        } else {
            // If the item doesn't exist, add it to the inventory with the given quantity
            items.put(itemName, quantity);
        }

        // Update the database with the new inventory data
        updateDatabase(itemName, items.get(itemName));
    }

    // Method to update the inventory data in the database
    private void updateDatabase(String itemName, int quantity) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE inventory SET quantity = ? WHERE item_name = ?");
            statement.setInt(1, quantity);
            statement.setString(2, itemName);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check if a specific item is available in the inventory
    public boolean checkAvailability(String itemName) {
        return items.containsKey(itemName) && items.get(itemName) > 0;
    }

    // Method to use an item from the inventory (decrease its quantity) and update the database
    public void useInventory(String itemName) {
        if (items.containsKey(itemName) && items.get(itemName) > 0) {
            // Decrease the quantity of the item by 1
            int currentQuantity = items.get(itemName);
            items.put(itemName, currentQuantity - 1);

            // Update the database with the new inventory data
            updateDatabase(itemName, items.get(itemName));
        } else {
            // Print a message indicating that the item is not available
            System.out.println(itemName + " is not available in the inventory.");
        }
    }

    // Method to display the contents of the inventory
    public void displayInventory() {
        System.out.println("Inventory:");
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
