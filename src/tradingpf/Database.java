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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Simon Cathébras and Zoé Bellot
 */
public class Database {

    private Connection conn;
    private Statement statement;
    private boolean debug = true;
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
    private PreparedStatement getItemStatement;
    private PreparedStatement getUserToNotifyStatement;
    private PreparedStatement insertFollowedStatement;
    private PreparedStatement insertFollowingStatement;

    private void createDatabase() throws SQLException, ClassNotFoundException {
        getConnection();
        if (debug){
            dropAllTable();
        }
        createUser();
        createItem();
        createCallBack();
        createFollowedItem();
        createFollowing();
        initOperations();
    }

    private void getConnection() throws SQLException, ClassNotFoundException {
        Class.forName("com.mysql.jdbc.Driver");
        conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/Market", "id2212", "javajava");
        statement = conn.createStatement();
    }

    public Database() {
        try {
            this.createDatabase();
        } catch (SQLException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(Database.class.getName()).log(Level.SEVERE, null, ex);
        }        
    }

    
    private void createUser() throws SQLException {
        ResultSet result = conn.getMetaData().
                getTables(null, null, "User", null);
        if (result.next()) {
            dropTableUser();
        }

        String Request;
        Request = "create table User ("
                + "Name VARCHAR(30) NOT NULL PRIMARY KEY, "
                + "Password VARCHAR(8) NOT NULL,"
                + "NumberSold INTEGER,"
                + "NumberBought INTEGER,"
                + "Log BOOLEAN)";
        statement.executeUpdate(Request);
    }

    private void createItem() throws SQLException {
        ResultSet result = conn.getMetaData().
                getTables(null, null, "Item", null);
        if (result.next()) {
            dropTableItem();
        }
        String Request;
        Request = "create table Item ("
                + "Id_Item INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + "Name VARCHAR(30) NOT NULL, "
                + "Price INTEGER NOT NULL,"
                + "Amount INTEGER NOT NULL,"
                + "Seller VARCHAR(30) NOT NULL,"
                + "FOREIGN KEY (Seller) REFERENCES User(Name))";
        statement.executeUpdate(Request);
    }

    private void createCallBack() throws SQLException {
        ResultSet result = conn.getMetaData().
                getTables(null, null, "CallBack", null);
        if (result.next()) {
            dropTableCallBack();
        }
        String Request;
        Request = "create table CallBack ("
                + "Id_CallBack INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + "Type BOOLEAN NOT NULL, "
                + /*
                 * Type = 0 if it is an available callback Type = 1 if it is a
                 * sold callback
                 */ "ItemName VARCHAR(30) NOT NULL,"
                + "UserName VARCHAR(30) NOT NULL,"
                + "FOREIGN KEY (UserName) REFERENCES User(Name))";
        statement.executeUpdate(Request);
    }

    private void createFollowedItem() throws SQLException {
        ResultSet result = conn.getMetaData().
                getTables(null, null, "FollowedItem", null);
        if (result.next()) {
            dropTableFollowedItem();
        }
        String Request;
        Request = "create table FollowedItem ("
                + "Id_Item INTEGER UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,"
                + "Name VARCHAR(30) NOT NULL,"
                + "PrixMax INTEGER UNSIGNED NOT NULL)";
        statement.executeUpdate(Request);
    }

    private void createFollowing() throws SQLException {
        ResultSet result = conn.getMetaData().
                getTables(null, null, "Following", null);
        if (result.next()) {
            dropTableFollowing();
        }
        String Request;
        Request = "create table Following ("
                + "Item VARCHAR(30) NOT NULL,"
                + "Follower VARCHAR(30) NOT NULL,"
                + "FOREIGN KEY (Item) REFERENCES FollowedItem(Id_Item),"
                + "FOREIGN KEY (Follower) REFERENCES User(Name))";
        statement.executeUpdate(Request);
    }

    private void initOperations() throws SQLException {



        insertUserStatement = conn.prepareStatement("INSERT INTO User "
                + "                                 VALUES (?,?,0,0,true)");
        removeUserStatement = conn.prepareStatement("DELETE FROM User "
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
        getItemStatement = conn.prepareStatement("SELECT * FROM Item WHERE Id_Item=?");
        getUserToNotifyStatement =
                conn.prepareStatement("SELECT Follower FROM Following WHERE Item IN "
                + "(SELECT Id_Item FROM FollowedItem WHERE Name=? AND PrixMax>=?)");
        insertFollowedStatement = 
                conn.prepareStatement("INSERT INTO FollowedItem VALUES (? ,?)"); 
        insertFollowingStatement =
                conn.prepareStatement("INSERT INTO Following VALUES (?,?)");
    }

    public void insertUser(String userName, String password) throws SQLException {
        insertUserStatement.setString(1, userName);
        insertUserStatement.setString(2, password);
        insertUserStatement.executeUpdate();
    }

    public void removeUser(String userName) throws SQLException {
        removeUserStatement.setString(1, userName);
        removeUserStatement.executeUpdate();
    }

    public ResultSet getUser(String userName) throws SQLException {
        getUserStatement.setString(1, userName);
        return getUserStatement.executeQuery();
    }

    public void loginUser(String userName) throws SQLException {
        loginUserStatement.setString(1, userName);
        loginUserStatement.executeUpdate();
    }

    public void logoutUser(String userName) throws SQLException {
        logoutUserStatement.setString(1, userName);
        logoutUserStatement.executeUpdate();
    }

    public void updateSoldItem(String userName) throws SQLException {
        updateSoldItemStatement.setString(1, userName);
        updateSoldItemStatement.executeUpdate();
    }

    public void updateBoughtItem(String UserName) throws SQLException {
        updateBoughtItemStatement.setString(1, UserName);
        updateBoughtItemStatement.executeUpdate();
    }

    public void insertItem(String itemName,
            Integer itemPrice,
            Integer amount,
            String sellerName) throws SQLException {

        insertItemStatement.setString(1, itemName);
        insertItemStatement.setInt(2, itemPrice);
        insertItemStatement.setInt(3, amount);
        insertItemStatement.setString(4, sellerName);
        insertItemStatement.executeUpdate();

    }

    public ResultSet listItem() throws SQLException {
        ResultSet to_return = null;
        to_return = listItemStatement.executeQuery();
        return to_return;
    }

    public void removeItem(Integer itemId) throws SQLException {
        removeItemStatement.setInt(1, itemId);
        removeItemStatement.executeUpdate();
    }

    public void insertCallBack(Boolean isSold,
            String subjectName,
            String itemName) throws SQLException {
        insertCallBackStatement.setBoolean(1, isSold);
        insertCallBackStatement.setString(2, itemName);
        insertCallBackStatement.setString(3, subjectName);
        insertCallBackStatement.executeUpdate();
    }

    public void removeCallBack(Integer idCallBack) throws SQLException {
        removeCallBackStatement.setInt(1, idCallBack);
        removeCallBackStatement.executeUpdate();
    }

    public ResultSet listCallBack(String userName) throws SQLException {
        ResultSet to_return = null;
        listCallBackStatement.setString(1, userName);
        to_return = listCallBackStatement.executeQuery();
        return to_return;
    }

    public ResultSet getItem(Integer itemId) throws SQLException {
        ResultSet to_return = null;
        getItemStatement.setInt(1, itemId);
        to_return = getItemStatement.executeQuery();
        return to_return;
    }
    
    public ResultSet getUserToNotify(
            String itemName, 
            Integer itemPrice) 
            throws SQLException {
        ResultSet to_return = null;
        getUserToNotifyStatement.setString(1, itemName);
        getUserToNotifyStatement.setInt(2, itemPrice);
        to_return = getUserToNotifyStatement.executeQuery();
        return to_return;
    }

    private void dropTableUser() throws SQLException {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE User");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");
    }

    private void dropTableItem() throws SQLException {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE Item");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");
    }

    private void dropTableCallBack() throws SQLException {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE CallBack");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");
    }

    private void dropTableFollowedItem() throws SQLException {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE FollowedItem");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");
    }

    private void dropTableFollowing() throws SQLException {
        int NoOfAffectedRows = statement.executeUpdate("DROP TABLE Following");
        System.out.println();
        System.out.println("Table dropped, " + NoOfAffectedRows + " row(s) affected");
    }


    private void dropAllTable() throws SQLException{
        dropTableFollowing();
        dropTableFollowedItem();
        
    }
    
    public void insertFollowed(String itemName, Integer priceMax, String userName) throws SQLException{
        insertFollowedStatement.setString(1, itemName);
        insertFollowedStatement.setInt(2, priceMax); 
        insertFollowedStatement.executeUpdate();
        insertFollowingStatement.setString(1, itemName); 
        insertFollowingStatement.setString(2, userName); 
        insertFollowingStatement.executeUpdate();   }

}
