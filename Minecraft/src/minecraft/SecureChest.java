/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

/**
 *
 * @author USER
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class SecureChest {
    private String owner;
    private Map<String, Integer> accessPermissions; // Map to store access permissions for players
    private SecurityLevel securityLevel; // Enum to represent different security levels
    private Contents content;
    private Inventory inventory;
    private Set<String> accessRequests; // Set to store pending access requests
    private final int NO_ACCESS = 0;
    private final int VIEW_ONLY = 1;
    private final int FULL_ACCESS = 2;

    public SecureChest(String owner) {
        this.owner = owner;
        accessPermissions = new HashMap<>();
        accessPermissions.put(owner,2);
        securityLevel = SecurityLevel.PRIVATE; // Default security level
        content = new Contents();
        inventory = new Inventory();
        accessRequests = new HashSet<>();
    }

    public String getOwner() {
        return owner;
    }

    // Enum to represent different security levels
    public enum SecurityLevel {
        PUBLIC, // Accessible to all players
        PRIVATE, // Accessible only to the owner 
        SELFDEFINED; // Accessible to authorized players
    }

    // Method to get the security level of the chest (in String dt)
    public String getSecurityLevel() {
        return securityLevel.toString();
    }

    // Method to set the security level of the chest
    public void setSecurityLevel(SecurityLevel level, String username) {
        if (username.equals(getOwner()))
            securityLevel = level;
        else
            System.out.println("You do not have the right to change Security Level");
    }

    // Method to grant access to a player: owner wants to add / remove someone to access this chest
    public void editAccess(String username, int type) {
        if (securityLevel == SecurityLevel.PUBLIC || securityLevel == SecurityLevel.SELFDEFINED) {
            accessPermissions.put(username, type);
        }
    }

    // Method to check if a player has access to the chest
    public boolean hasAccess(String playerName) {
        if (securityLevel == SecurityLevel.PUBLIC || securityLevel == SecurityLevel.SELFDEFINED) {
            return accessPermissions.containsKey(playerName) && (accessPermissions.get(playerName) == VIEW_ONLY || accessPermissions.get(playerName) == FULL_ACCESS);
        } else {
            return false; // If security level is PRIVATE, only the owner has access
        }
    }

    // Method to display the access permissions for all players
    public void displayAccessPermissions() {
        System.out.println("Access Permissions:");
        for (Map.Entry<String, Integer> entry : accessPermissions.entrySet()) {
            System.out.println(entry.getKey() + ": " + (entry.getValue() == 1 || entry.getValue() == 2 ? "Granted" : "Revoked"));
        }
    }
    
    // Get the list of players with access to the chest
    public Set<String> getAuthorizedPlayers() {
        return accessPermissions.keySet();
    }
    
    public void logAccessRequest(String username) {
        try (FileWriter writer = new FileWriter("access_log.txt", true)) {
            String logEntry = String.format("[%s] Player '%s' requested access to the chest.\n", LocalDateTime.now(), username);
            writer.write(logEntry);
        } catch (IOException e) {
            System.err.println("Error writing to access log: " + e.getMessage());
        }
    }
    
    private void displayAccessRequests() {
        StringBuilder accessLog = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader("access_log.txt"))) {
            String line;
            while ((line = reader.readLine()) != null) {
                accessLog.append(line).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println(accessLog.toString());
    }
    
    // Method to approve access request
    public void approveRequest(String username, int type) {
        if (accessRequests.contains(username)) {
            accessPermissions.put(username, type);
            accessRequests.remove(username);
            updateAccessLog(); // Update access log after approval
            System.out.println("Access request approved for " + username);
        } else {
            System.out.println("Access request not found for " + username);
        }
    }

    // Method to reject access request
    public void rejectRequest(String username) {
        if (accessRequests.contains(username)) {
            accessRequests.remove(username);
            updateAccessLog(); // Update access log after rejection
            System.out.println("Access request rejected for " + username);
        } else {
            System.out.println("Access request not found for " + username);
        }
    }

    // Method to update access log after approval or rejection
    private void updateAccessLog() {
        // Read the existing access log and remove the line corresponding to the approved/rejected request
        try (BufferedReader reader = new BufferedReader(new FileReader("access_log.txt"));
             FileWriter writer = new FileWriter("access_log_temp.txt")) {

            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.contains("requested access to the chest")) {
                    writer.write(line + System.lineSeparator());
                }
            }

        } catch (IOException e) {
            System.err.println("Error updating access log: " + e.getMessage());
        }

        // Replace the original log file with the updated log file
        File originalFile = new File("access_log.txt");
        File tempFile = new File("access_log_temp.txt");
        if (tempFile.renameTo(originalFile)) {
            System.out.println("Access log updated successfully.");
        } else {
            System.err.println("Failed to update access log.");
        }
    }
    
    // Method to send an access request
    public void sendRequest(String username) {
        if (!accessPermissions.containsKey(username)) { // Check if the player is not already authorized
            if (securityLevel == SecurityLevel.PUBLIC || securityLevel == SecurityLevel.SELFDEFINED) {
                accessRequests.add(username);
                logAccessRequest(username); // Log the access request
                System.out.println("Access request sent to the owner.");
            } else {
                System.out.println("Access request cannot be sent. This chest is private.");
            }
        } else {
            System.out.println("You already have access to the chest.");
        }
    }
    
    public void view (String username) {
        if (hasAccess(username)) {
            content.displayContents();
        } else {
            System.out.println("You are not able to view this chest.");
        }
    }
    
    public void deposit (String username, String item, int quantity) {
        if (hasAccess(username)) {
            if (accessPermissions.get(username) == FULL_ACCESS) {
                inventory.displayInventory();
                content.deposit(item, quantity);
            } else {
                System.out.println("You are able to view only");
            }
            
        } else {
            System.out.println("You are not able to deposit items to this chest.");
        }
    }
    
    public void withdraw (String username, String item, int quantity) {
        if (hasAccess(username)) {
            if (accessPermissions.get(username) == FULL_ACCESS) {
                inventory.displayInventory();
                content.withdraw(item, quantity);
            } else {
                System.out.println("You are able to view only");
            }
            
        } else {
            System.out.println("You are not able to withdraw items from this chest.");
        }
    }
}
