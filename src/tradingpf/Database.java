/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
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
            updateBoughtItemStatement = conn.prepareStatement("");
            insertItemStatement = conn.prepareStatement("");
            listItemStatement = conn.prepareStatement("");
            removeItemStatement = conn.prepareStatement("");
            insertCallBackStatement = conn.prepareStatement("");
            removeCallBackStatement = conn.prepareStatement("");
        }
        
        public void insertUser(String userName, String password) throws SQLException{
            insertUserStatement.setString(1, userName);  
            insertUserStatement.setString(2, password); 
        }
        
        public void removeUser(String userName)throws SQLException{
            removeUserStatement.setString(1, userName);
        }
        
        public void getUser(String userName) throws SQLException{
            getUserStatement.setString(1, userName);  
        }
        
        public void loginUser(String userName) throws SQLException{
            loginUserStatement.setString(1, userName);  
        }
        
        public void logoutUser(String userName) throws SQLException{
            logoutUserStatement.setString(1, userName); 
        }
        
        public void updateSoldItem(String userName) throws SQLException{
            updateSoldItemStatement.setString(1, userName); 
        }
    
    
    
    
    
    private PreparedStatement updateBoughtItem;
    public void updateBoughtItem(String UserName) throws Exception{
        
    }
    private PreparedStatement insertItem;
    private PreparedStatement listItem;
    private PreparedStatement removeItem;
    private PreparedStatement insertCallBack;
    private PreparedStatement removeCallBack;
}
