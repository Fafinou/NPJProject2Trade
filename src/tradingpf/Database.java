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
    private PreparedStatement insertUser;
    private PreparedStatement removeUser;
    private PreparedStatement getUser;
    private PreparedStatement loginUser;
    private PreparedStatement logoutUser;
    private PreparedStatement updateSoldItem;
    private PreparedStatement updateBoughtItem;
    private PreparedStatement insertItem;
    private PreparedStatement listItem;
    private PreparedStatement removeItem;
    private PreparedStatement insertCallBack;
    private PreparedStatement removeCallBack;
    
    
    
     private void createDatabase() throws SQLException, ClassNotFoundException{
            getConnection();
            createUser();
            createItem();
            createCallBack();
            createFollowedItem();
            createFollowing();
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
        
        public void insertUser(String userName, String Password){
        }
        
        public void removeUser(){
        
        }
        
        public void getUser(){
            
        }
        
        public void loginUser(){
            
        }
        
        public void logoutUser(){
            
        }
        
        public void updateSoldItem(){
            
        }
    
    
    
    
    
    private PreparedStatement updateBoughtItem;
    private PreparedStatement insertItem;
    private PreparedStatement listItem;
    private PreparedStatement removeItem;
    private PreparedStatement insertCallBack;
    private PreparedStatement removeCallBack;
}
