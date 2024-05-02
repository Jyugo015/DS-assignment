package code;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.LinkedHashMap;
// import java.util.Map;
// import java.util.ArrayList;
// import java.util.LinkedHashMap;

public class database {
    public static void main(String[] args){
        try {
            // Establish a JDBC connection
            getConnection();
            createTable();
            // // Close the connection
            // connection.close();
            } 
        catch (Exception e) {
            e.printStackTrace();
        }    
    }

    public static Connection getConnection() throws Exception{
        try{
            String driver = "com.mysql.cj.jdbc.Driver";
            String url = "jdbc:mysql://localhost:3306/minecraft";
            String username = "root";
            String password = "dbqLb1234!";
            Class.forName(driver);
            Connection connection = DriverManager.getConnection(url, username, password);
            return connection;
        }
        catch(Exception e){
            System.out.println(e);
        }
        return null;
    }

    public static void createTable() throws Exception{
        try{
            Connection connection = getConnection();
            
            String statement = "CREATE TABLE IF NOT EXISTS ItemWikipedia"
                    + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Type VARCHAR(255), Name VARCHAR(255), 'Function' VARCHAR(255))";
            PreparedStatement create = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS ItemList"
                    + "(ItemID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, Name VARCHAR(255), Type VARCHAR(255), Quantity INT)";
            PreparedStatement create2 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS ToolList"
                    + "(ToolID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, Name VARCHAR(255), Type VARCHAR(255), Function VARCHAR(255), Grade INT)";
            PreparedStatement create3 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS MultiTool"
                    + "(ToolID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, Name VARCHAR(255), Type VARCHAR(255), Function VARCHAR(255), Grade INT)";
            PreparedStatement create4 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS Potion"
                    + "(PotionID INT PRIMARY KEY AUTO_INCREMENT, "
                    + "Name VARCHAR(255), Effect VARCHAR(255), NextPotion VARCHAR(255))";
            PreparedStatement create5 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS PotionSatchel"
                    + "(PotionID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, Name VARCHAR(255), Effect VARCHAR(255), NextPotion VARCHAR(255))";
            PreparedStatement create6 = connection.prepareStatement(statement);
            
            statement = "CREATE TABLE IF NOT EXISTS AutoFarmScheduling"
                    + "(TaskID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, BlockPos INT, Task VARCHAR(255), Status VARCHAR(255), Duration VARCHAR(255), ResourceUsed VARCHAR(255), ResourceQuantity INT)";
            PreparedStatement create7 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS AutoFarmResource"
                    + "(ResourceID INT PRIMARY KEY AUTO_INCREMENT, Username VARCHAR(255) COLLATE utf8_bin, Resource VARCHAR(255), Quantity INT";
            PreparedStatement create8 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS SecureChest"
                    + "(ChestID INT PRIMARY KEY AUTO_INCREMENT, ChestName VARCHAR(255), SecurityLevel VARCHAR(255), Owner VARCHAR(255) COLLATE utf8_bin, ApprovedUser VARCHAR(255) COLLATE utf8_bin, PermissionType VARCHAR(255))";
            PreparedStatement create9 = connection.prepareStatement(statement);

            statement = "CREATE TABLE IF NOT EXISTS SecureChestRequest"
                    + "(ChestID INT PRIMARY KEY AUTO_INCREMENT, ChestName VARCHAR(255), Owner VARCHAR(255) COLLATE utf8_bin, Requestor VARCHAR(255) COLLATE utf8_bin, Group VARCHAR(255) COLLATE utf8_bin, RequestStatus VARCHAR(255), Purpose VARCHAR(255))";
            PreparedStatement create10 = connection.prepareStatement(statement);

            create.executeUpdate();
            create2.executeUpdate();
            create3.executeUpdate();
            create4.executeUpdate();
            create5.executeUpdate();
            create6.executeUpdate();
            create7.executeUpdate();
            create8.executeUpdate();
            create9.executeUpdate();
            create10.executeUpdate();
        }
        catch(Exception e){
            System.out.println(e);
        }
    }

    public static boolean readDatabase(String user, String password) throws Exception{
        try {
            Connection connection = getConnection();
            String statement;
            //readDatabase(username, password)
            if (user!=null && password!=null)
                statement = "SELECT * FROM userlist WHERE Username = '"+user+"' AND Password = '"+password+"'";
            //readDatabase(username, null)
            else if(password==null)
                statement = "SELECT * FROM userList WHERE Username = '"+user+"'";
            //readDatabase(null, password)
            else 
                statement = "SELECT * FROM userList WHERE Password = '"+password+"'";
            PreparedStatement search = connection.prepareStatement(statement);
            ResultSet result = search.executeQuery();
            return result.next();
        }
        catch(Exception e){
            System.out.println(e);
        }
        return false;
    }

    public static void insertData(String a, String b, String c, String d) throws Exception{
        try{
            Connection connection = getConnection();
            String statement ="";
            int frequency=1;
            switch(a){
                //insertData("userlist", username, password, null)
                case "userlist":
                    statement = "SELECT * FROM userlist WHERE Username ='"+b+"'";
                    PreparedStatement check = connection.prepareStatement(statement);
                    ResultSet result = check.executeQuery();
                    if (result.next())
                        return;
                    else 
                        statement = "INSERT INTO userlist (Username, Password) VALUES ('"+b+"','"+c+"')";
                    break;
                
                //insertData("result", username, Double.toString(wpm), Double.toString(accuracy))
                case "result":
                    statement="INSERT INTO result (Username,Wpm, Accuracy) VALUES ('"+b+"',?,?)";
                    break;
                    
                //insertData("misspelled", username, word, null)
                case "misspelled":
                    statement = "SELECT * FROM misspelled WHERE Username ='"+b+"' AND Word ='"+c+"'";
                    PreparedStatement check1 = connection.prepareStatement(statement);
                    ResultSet result1 = check1.executeQuery();
                    if (result1.next()){
                        frequency = result1.getInt("Frequency") +1;
                        statement = "UPDATE misspelled SET Frequency= ? WHERE Username ='"+b+"' AND Word ='"+c+"'";
                    }
                    else
                        statement="INSERT INTO misspelled (Username,Word, Frequency) VALUES ('"+b+"','"+c+"', ?)";
                    break;
                
                //insertData("suddenDeath", username, Double.toString(suddenDeathScore), null)
                case "suddenDeath":
                    statement = "INSERT INTO suddenDeath (Username,Score) VALUES (?,?)";
                    break;
            }            
            PreparedStatement insert = connection.prepareStatement(statement);
            if (a.equals("result")){
                insert.setFloat(1,Float.parseFloat(c));
                insert.setFloat(2,Float.parseFloat(d));
            }
            else if (a.equals("suddenDeath")){
                insert.setString(1,b);
                insert.setFloat(2,Float.parseFloat(c));
            }
            else if(a.equals("misspelled"))
                insert.setInt(1,frequency);
            insert.executeUpdate();
        }      
        catch(Exception e){
            System.out.println(e);
        }
    }
    
    public static LinkedHashMap<String,String> sort(String mode, String username) throws Exception{
        LinkedHashMap <String, String> selected = new LinkedHashMap();
        String key="", value="", statement="";
        try{
            Connection connection = getConnection();
            switch(mode) {
                //sort("leaderboard", null)
                case "leaderboard":
                    statement = "SELECT * FROM leaderboard ORDER BY AvWpmLast10 DESC LIMIT 10";
                    key = "Username";
                    value = "AvWpmLast10";
                    break;
                
                //sort("misspelled", username)
                case "misspelled":
                    statement = "SELECT * FROM misspelled WHERE Username = '"+username+"' ORDER BY Frequency DESC LIMIT 10";
                    key = "Word";
                    value = "Frequency";
                    break;
                
                //sort("suddenDeath", username)
                case "suddenDeath":
                    statement = "CREATE TEMPORARY TABLE temp AS SELECT Username, Score FROM suddenDeath WHERE Username = '"+username+"'";
                    PreparedStatement select = connection.prepareStatement(statement);
                    select.executeUpdate();
                    statement = "ALTER TABLE temp ADD id INT PRIMARY KEY AUTO_INCREMENT";
                    PreparedStatement add = connection.prepareStatement(statement);
                    add.executeUpdate();
                    key = "id";
                    value = "Score";
                    statement = "SELECT * FROM temp ORDER BY id DESC";
                    break;                
            }
            PreparedStatement sort = connection.prepareStatement(statement);
            ResultSet result = sort.executeQuery();
            while (result.next()){
                selected.put(result.getString(key),
                        String.valueOf(mode.equals("misspelled")?result.getInt(value):result.getFloat(value)));
            }                            
        }
        catch(Exception e){
            System.out.println(e);
        }
        return selected;
    }

    public static void resetIndex() throws Exception{
       try{
           Connection connection = getConnection();
           String statement ="ALTER TABLE result AUTO_INCREMENT =1";
//            ALTER TABLE `table` AUTO_INCREMENT = number
           PreparedStatement change = connection.prepareStatement(statement);
           change.executeUpdate();
       }
       catch(Exception e){
           e.printStackTrace();
       }
   }
   
   public static void deleteUser(String username) throws Exception{
       try{
           Connection connection = getConnection();
           String statement ="DELETE FROM userlist WHERE Username =?";
           PreparedStatement delete = connection.prepareStatement(statement);
           delete.setString(1,username);
           delete.executeUpdate();
//            String statement ="SELECT username, password FROM userlist AS a,b";
//            PreparedStatement delete = connection.prepareStatement(statement);
//            delete.setString(1,username);
//            delete.executeUpdate();
//            if (row>0){
//                System.out.println("Deleted");
//            }
       }
       catch(Exception e){
           e.printStackTrace();
       }
   }
   
   public static void deleteResult(String username) throws Exception{
       try{
           Connection connection = getConnection();
           String statement ="DELETE FROM result WHERE Username =?";
           PreparedStatement delete = connection.prepareStatement(statement);
           delete.setString(1,username);
           delete.executeUpdate();
//            String statement ="SELECT username, password FROM userlist AS a,b";
//            PreparedStatement delete = connection.prepareStatement(statement);
//            delete.setString(1,username);
//            delete.executeUpdate();
//            if (row>0){
//                System.out.println("Deleted");
//            }
       }
       catch(Exception e){
           e.printStackTrace();
       }
   }
    public static void deleteTable() throws Exception{
       try{
           Connection connection = getConnection();
           String statement ="DROP TABLE misspelled";
           PreparedStatement drop = connection.prepareStatement(statement);
           statement ="DROP TABLE leaderboard";
           PreparedStatement drop1 = connection.prepareStatement(statement);
           statement ="DROP TABLE result";
           PreparedStatement drop2 = connection.prepareStatement(statement);
           statement ="DROP TABLE userlist";
           PreparedStatement drop3 = connection.prepareStatement(statement);
           statement ="DROP TABLE suddenDeath";
           PreparedStatement drop4 = connection.prepareStatement(statement);
//            drop.executeUpdate();
           drop1.executeUpdate();
//            drop2.executeUpdate();
//            drop3.executeUpdate();
//            drop4.executeUpdate();
       }
       catch(Exception e){
           e.printStackTrace();
       }
   }
}
