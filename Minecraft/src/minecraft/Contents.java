/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author USER
 */
public class Contents {
    private Map<String,Integer> content;
    private Inventory inventory;
    private Connection connection;
    
    public Contents() {
        content = new HashMap<>();
        inventory = new Inventory();
        connectToDatabase();
        loadContentFromDatabase();
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
    
    // Method to retrieve content data from the database
    private void loadContentFromDatabase() {
        

        try {
            PreparedStatement statement = connection.prepareStatement("SELECT * FROM content");
            ResultSet resultSet = statement.executeQuery();

            // Populate the items HashMap with existing inventory data from the database
            while (resultSet.next()) {
                String itemName = resultSet.getString("item_name");
                int quantity = resultSet.getInt("quantity");
                content.put(itemName, quantity);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    
    public void deposit(String item, int quantity) {
        // Check if the item already exists in the inventory
        if (content.containsKey(item)) {
            // If the item exists, update its quantity by adding the new quantity
            int currentQuantity = content.get(item);
            content.put(item, currentQuantity + quantity);
        } else {
            // If the item doesn't exist, add it to the inventory with the given quantity
            content.put(item, quantity);
        }
        
        //update inventory
        inventory.useInventory(item, quantity);

        // Update the database with the new content data
        updateDatabase(item, content.get(item));
    }
    
    public void withdraw(String item, int quantity) {
        if (content.containsKey(item) && content.get(item) > 0) {
            // Decrease the quantity of the item by 1
            int currentQuantity = content.get(item);
            content.put(item, currentQuantity - 1);
            inventory.addInventory(item, quantity);

            // Update the database with the new content data
            updateDatabase(item, content.get(item));
        } else {
            // Print a message indicating that the item is not available
            System.out.println(item + " is not available in the inventory.");
        }
    }
    
    // Method to update the content data in the database
    private void updateDatabase(String item, int quantity) {
        try {
            PreparedStatement statement = connection.prepareStatement("UPDATE content SET quantity = ? WHERE item_name = ?");
            statement.setInt(1, quantity);
            statement.setString(2, item);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Method to check if a specific item is available in the inventory
    public boolean checkAvailability(String item) {
        return content.containsKey(item) && content.get(item) > 0;
    }


    // Method to display the contents
    public void displayContents() {
        System.out.println("Content(s):");
        for (Map.Entry<String, Integer> entry : content.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}
