/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author Simon Cathébras and Zoé Bellot
 */
public class Database {
    
    private Connection conn;
    private Statement statement;
    private PreparedStatement insertUserStatement;
    private PreparedStatement removeUserStatement;
    private PreparedStatement getUserStatement;
    private PreparedStatement loginUserStatement;
    private PreparedStatement logoutUserStatement;
    private PreparedStatement updateSoldItemStatement;
    private PreparedStatement updateBoughtItemStatement;
    private PreparedStatement insertItemStatement;
    private PreparedStatement listItemStatement;
    private PreparedStatement removeItemStatement;
    private PreparedStatement insertCallBackStatement;
    private PreparedStatement removeCallBackStatement;
    private PreparedStatement listCallBackStatement;
    
    
    
     private void createDatabase() throws SQLException, ClassNotFoundException{
            getConnection();
            createUser();
            createItem();
            createCallBack();
            createFollowedItem();
            createFollowing();
            initOperations();
        }
     
        private void getConnection() throws SQLException, ClassNotFoundException{          
            Class.forName("com.mysql.jdbc.Driver");
            conn = DriverManager.getConnection("jdbc:mysql://localhost:");
            statement = conn.createStatement();
        }
        
        private void createUser() throws SQLException {
            String Request;
            Request = "create table User (" +
                                "Name VARCHAR(30) NOT NULL PRIMARY KEY, " +
				"Password VARCHAR(8) NOT NULL," +
                                "NumberSold INTEGER," +
                                "NumberBought INTEGER," +
                                "Log BOOLEAN)";
            statement.executeUpdate(Request);   
        }
        
        
        private void createItem() throws SQLException {
            String Request;
            Request = "create table Item (" +
                                "Id_Item INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY" +
                                "Name VARCHAR(30) NOT NULL, " +
				"Price INTEGER NOT NULL," +
                                "Amount INTEGER NOT NULL," +
                                "FOREIGN KEY (Seller) REFERENCES User(Name) NOT NULL)";
            statement.executeUpdate(Request);   
        }
        
        
        private void createCallBack() throws SQLException {
            String Request;
            Request = "create table CallBack (" +
                                "Id_CallBack INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY" +
                                "Type BOOLEAN NOT NULL, " + 
                                /* Type = 0 if it is an available callback
                                 * Type = 1 if it is a sold callback
                                 */
				"NameItem VARCHAR(30) NOT NULL," +
                                "FOREIGN KEY (UserName) REFERENCES User(Name) NOT NULL)";
            statement.executeUpdate(Request);   
        }
        
        private void createFollowedItem() throws SQLException {
            String Request;
            Request = "create table FollowedItem (" +
                                "Id_Item INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY" +
                                "Name VARCHAR(30) NOT NULL," +
				"PrixMax INTEGER UNSIGNED NOT NULL)";
            statement.executeUpdate(Request);   
        }
        
        
        private void createFollowing() throws SQLException {
            String Request;
            Request = "create table Following (" + 
                                "FOREIGN KEY (Item) REFERENCES FollowedItem(Id_Item) NOT NULL," +
				"FOREIGN KEY (Follower) REFERENCES User(Name) NOT NULL)";
            statement.executeUpdate(Request);   
        }
        
        private void initOperations() throws SQLException {


            insertUserStatement = conn.prepareStatement("INSERT INTO User " 
                    + "                                 VALUES (?,?,0,0,true)");
            removeUserStatement= conn.prepareStatement("DELETE FROM User "
                    + "                                 WHERE Name = ?");
            getUserStatement = conn.prepareStatement("SELECT * FROM User "
                    + "                                 WHERE Name= ?");
            loginUserStatement = conn.prepareStatement("UPDATE User "
                    + "                                 SET Log=true "
                    + "                                 WHERE Name = ?");
            logoutUserStatement = conn.prepareStatement("UPDATE User "
                    + "                                 SET Log=false "
                    + "                                 WHERE Name = ?");
            updateSoldItemStatement = conn.prepareStatement("UPDATE User "
                    + "                                     SET NumberSold = NumberSold + 1 " 
                    + "                                     WHERE Name = ?");
                        
            updateBoughtItemStatement = 
                                    conn.prepareStatement(
                                           "UPDATE User "
                                         + "SET NumberBought = NumberBought + 1 "
                                         + "WHERE Name=?");
            insertItemStatement = conn.prepareStatement("INSERT INTO Item"
                                                        + " VALUES (?,?,?,?)");
            listItemStatement = conn.prepareStatement("SELECT * FROM Item");
            removeItemStatement = conn.prepareStatement("DELETE FROM Item "
                                                        + "WHERE Id_Item=?");
            insertCallBackStatement = conn.prepareStatement("INSERT INTO CallBack"
                                                        + " VALUES (?,?,?)");
            removeCallBackStatement = conn.prepareStatement("DELETE FROM CallBack"
                    + " WHERE Id_CallBack=?");
            listCallBackStatement = conn.prepareStatement("SELECT * FROM CallBack"
                    + " WHERE UserName=?");
        }
        
        public void insertUser(String userName, String password) throws SQLException{
            insertUserStatement.setString(1, userName);  
            insertUserStatement.setString(2, password); 
            insertUserStatement.executeUpdate();
        }
        
        public void removeUser(String userName)throws SQLException{
            removeUserStatement.setString(1, userName);
            removeUserStatement.executeUpdate();
        }
        
        public ResultSet getUser(String userName) throws SQLException{
            getUserStatement.setString(1, userName); 
            return getUserStatement.executeQuery();
        }
        
        public void loginUser(String userName) throws SQLException{
            loginUserStatement.setString(1, userName);
            loginUserStatement.executeUpdate();
        }
        
        public void logoutUser(String userName) throws SQLException{
            logoutUserStatement.setString(1, userName); 
            logoutUserStatement.executeUpdate();
        }
        
        public void updateSoldItem(String userName) throws SQLException{
            updateSoldItemStatement.setString(1, userName); 
            updateSoldItemStatement.executeUpdate();
        }
    
    
    
    
    
    public void updateBoughtItem(String UserName) throws Exception{
        updateBoughtItemStatement.setString(1, UserName);
        updateBoughtItemStatement.executeUpdate();
    }
    
    public void insertItem(String itemName,
            Integer itemPrice, 
            Integer amount, 
            String sellerName) throws Exception{
        
        insertItemStatement.setString(1, itemName);
        insertItemStatement.setInt(2, itemPrice);
        insertItemStatement.setInt(3, amount);
        insertItemStatement.setString(4, sellerName);
        insertItemStatement.executeUpdate();
        
    }
    
    public ResultSet listItem() throws Exception {
        ResultSet to_return = null;
        to_return = listItemStatement.executeQuery();
        return to_return;
    }

    public void removeItem(Integer itemId) throws Exception {
        removeItemStatement.setInt(1, itemId);
        removeItemStatement.executeUpdate();
    }

    public void insertCallBack(Boolean isSold,
                    String subjectName,
                    String itemName) throws Exception{
        insertCallBackStatement.setBoolean(1, isSold);
        insertCallBackStatement.setString(2, itemName);
        insertCallBackStatement.setString(3, subjectName);
        insertCallBackStatement.executeUpdate();
    }

    public void removeCallBack(Integer idCallBack) throws Exception{
        removeCallBackStatement.setInt(1, idCallBack);
        removeCallBackStatement.executeUpdate();
    }
   
    public ResultSet listCallBack(String userName) throws Exception {
        ResultSet to_return = null;
        listCallBackStatement.setString(1, userName);
        to_return = listCallBackStatement.executeQuery();
        return to_return;
    }
}
