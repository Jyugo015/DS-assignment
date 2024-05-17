package minecraft;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.SourceDataLine;

public class database_item2 {
    public static Connection getConnection() throws SQLException{
        String driver = "com.mysql.cj.jdbc.Driver";
        String url = "jdbc:mysql://localhost:3306/minecraft";
        String username = "root";
        String password = "dbqLb1234!";
        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Class not found for driver", e);
        }
        Connection connection = DriverManager.getConnection(url, username, password);
        return connection;
    }
    
    // public int getSize(String username) throws SQLException{
    //     int record=0;
    //     Connection connection = getConnection();
    //     String statement = "SELECT COUNT(?) AS count FROM ? WHERE Username =?";
    //     PreparedStatement count = connection.prepareStatement(statement);
    //     count.setString(1, "Name");
    //     count.setString(2, "multitool");
    //     count.setString(3, username);
    //     ResultSet result = count.executeQuery();
    //     if (result.next())
    //         return result.getInt("count");
    //     return record;
    // }

    public static ArrayList<Tool> retrieveMultitool(String user) throws SQLException{
        Connection connection = getConnection();
        ArrayList<Tool> list = new ArrayList<Tool>();
        String statement = "SELECT * FROM multitool WHERE Username = ?";
        PreparedStatement retrieve = connection.prepareStatement(statement);
        retrieve.setString(1, user);
        ResultSet result = retrieve.executeQuery();
        String name, type, function;
        int grade;
        while (result.next()){
            name = result.getString("Name");
            type = result.getString("Type");
            function = result.getString("Funtions");
            grade = result.getInt("Grade");
            list.add(new Tool(name, type, function, grade));
        }
        return list;
    }

    public static List<Tool> retrieveTool(String user) throws SQLException{
        Connection connection = getConnection();
        List<Tool> list = new ArrayList<Tool>();
        String statement = "SELECT * FROM ItemBox WHERE Username = ? and Type = ?";
        PreparedStatement filter = connection.prepareStatement(statement);
        filter.setString(1, user);
        filter.setString(2, "Tool");
        ResultSet result = filter.executeQuery();
        String name, type, function;
        int grade = 2, quantity;//initialize the grade of each tool =2
        while(result.next()){
            name = result.getString("Name");
            type = result.getString("Type");
            function = result.getString("Functions");
            quantity = result.getInt("Quantity");
            for (int i=0;i<quantity;i++)
                list.add(new Tool(name, type, function, grade));
        }
        list.forEach(e-> System.out.println(e));
        return list;
    }

    public static List<Tool> retrieveMultipleTool(String user) throws SQLException{
        Connection connection = getConnection();
        List<Tool> list = new ArrayList<Tool>();
        String statement = "SELECT * FROM multitool WHERE Username = ? and Type = ?";
        PreparedStatement filter = connection.prepareStatement(statement);
        filter.setString(1, user);
        filter.setString(2, "Tool");
        ResultSet result = filter.executeQuery();
        String name, type, function;
        int grade;
        while(result.next()){
            name = result.getString("Name");
            type = result.getString("Type");
            function = result.getString("Functions");
            grade = result.getInt("Grade");
            list.add(new Tool(name, type, function, grade));
        }
        list.forEach(e-> System.out.println(e));
        return list;
    }
    
    public static void addTool(String username, String toolname, String type, String function, 
    int grade) throws SQLException{   
        //int quantitytoadd - parameter removed, back up for future use if needed
        Connection connection = getConnection();
        // String statement = "SELECT * FROM multitool WHERE Username = ? AND Name = ?";
        // PreparedStatement check = connection.prepareStatement(statement);
        // check.setString(1, username);
        // check.setString(2,toolname);
        // ResultSet result = check.executeQuery();
        // int Itemquantity;
        // if (result.next()){
        //     Itemquantity = result.getInt("Quantity") + quantitytoadd;
        //     statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
        //     PreparedStatement update = connection.prepareStatement(statement);
        //     update.setInt(1, Itemquantity);
        //     update.setString(2,username);
        //     update.setString(3, itemname);
        //     update.executeUpdate();
        // }
        // else{
        //     Itemquantity = quantitytoadd;
            String statement = "INSERT INTO multitool(username, name, type, functions, grade)"
                                + " VALUES (?,?,?,?,?)";
            PreparedStatement insert = connection.prepareStatement(statement);
            insert.setString(1,username);
            insert.setString(2,toolname);
            insert.setString(3,type);
            insert.setString(4, function);
            insert.setInt(5, grade);
            // insert.setInt(5,quantitytoadd);
            insert.executeUpdate();
        // }
    }

    public static void removeTool(String toolName, String username) throws SQLException{
        // int quantitytoreduce - parameter removed, back up for future use 
        Connection connection = getConnection();
        // String statement = "SELECT Quantity FROM itemBackpack WHERE Username = ? AND Name = ?";
        // PreparedStatement check = connection.prepareStatement(statement);
        // check.setString(1, username);
        // check.setString(2,itemName);
        // ResultSet result = check.executeQuery();
        // // int quantity;
        // // if (result.next()){
        //     quantity = result.getInt("Quantity") - quantitytoreduce;
        //     if (quantity==0){
                String statement = "DELETE FROM multitool WHERE Username = ? AND Name = ? LIMIT 1";
                PreparedStatement delete = connection.prepareStatement(statement);
                delete.setString(1, username);
                delete.setString(2,toolName);
                delete.executeUpdate();
            // }
            // else{
            //     statement = "UPDATE itemBackpack SET Quantity = ? WHERE Username = ? AND Name =?";
            //     PreparedStatement update = connection.prepareStatement(statement);
            //     update.setInt(1, quantity);
            //     update.setString(2,username);
            //     update.setString(3, itemName);
            //     update.executeUpdate();
            // }
        // }
    }
    
    public static void clearTool(String username) throws SQLException{
        Connection connection = getConnection();
        String statement = "DELETE FROM multitool WHERE Username = ?";
        PreparedStatement delete = connection.prepareStatement(statement);
        delete.setString(1, username);
        delete.executeUpdate();
    }

    public static void upgradeTool(String username, String toolName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM multitool WHERE Username = ? AND Name = ?";
        PreparedStatement filter = connection.prepareStatement(statement);
        filter.setString(1, username);
        filter.setString(2,toolName);
        ResultSet result = filter.executeQuery();
        int grade = 0;
        if(result.next()){
            grade = result.getInt("grade");
            statement = "UPDATE multitool SET grade= ? WHERE Username =? AND Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1,grade+1);
            update.setString(2, username);
            update.setString(3,toolName);
            update.executeUpdate();
        }
    }

    public static void downgradeTool(String username, String toolName) throws SQLException{
        Connection connection = getConnection();
        String statement = "SELECT * FROM multitool WHERE Username = ? AND Name = ?";
        PreparedStatement filter = connection.prepareStatement(statement);
        filter.setString(1, username);
        filter.setString(2,toolName);
        ResultSet result = filter.executeQuery();
        int grade = 0;
        if(result.next()){
            grade = result.getInt("grade");
            statement = "UPDATE multitool SET grade= ? WHERE Username =? AND Name = ?";
            PreparedStatement update = connection.prepareStatement(statement);
            update.setInt(1,grade-1);
            update.setString(2, username);
            update.setString(3,toolName);
            update.executeUpdate();
        }
    }
}
