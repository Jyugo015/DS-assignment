/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package minecraft;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Asus
 */
public class UserDatabase {
    private static Map<String, User> usersByEmail = new HashMap<>();

    public static boolean addUser(User user) {
        if (usersByEmail.containsKey(user.getEmail())) {
            return false; 
        }
        usersByEmail.put(user.getEmail(), user);
        return true;
    }

    public static User getUserByEmail(String email) {
        return usersByEmail.get(email);
    }
    
    public static User getUserByUsernameAndPassword(String username, String hashedPassword) {
        return usersByEmail.values().stream()
                .filter(user -> user.getUsername().equals(username) && user.getHashedPassword().equals(hashedPassword))
                .findFirst()
                .orElse(null);
    }
}
