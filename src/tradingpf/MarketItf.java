/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Vector;
import se.kth.id2212.bankrmi.RejectedException;

/**
 *
 * @author Zoé Bellot
 * @author Simon Cathébras
 */
public interface MarketItf extends Remote {
    /**
     * Registers a new client. The client must not be registered yet
     * @param name name of the client.
     * @param client Client's remote interface.
     * @throws RemoteException
     * @throws RejectedException 
     */
    public void register(String name, String password) throws SQLException, RemoteException;
    
    
    /**
     * Check if the name is unique
     * 
     * @param name : the name of the user
     * @return if the name is unique or no 
     * @throws SQLException
     * @throws RemoteException
     */
    public boolean verifyName(String name) throws SQLException, RemoteException;
    
    /**
     * Check the password
     * 
     * @param password : the password of the user
     * @return if the password lengh is longer than 8 characters 
     * @throws SQLException
     * @throws RemoteException 
     */
    public boolean verifyPassword(String password) throws SQLException, RemoteException;
    
    /**
     * Check if the password corresponds to the name
     * 
     * @param name : the name of the user
     * @param password : the password of the user
     * @return if the password corresponds to the name
     * @throws SQLException
     * @throws RemoteException 
     */
    public boolean verifyPasswordForAUser(String name, String password) throws SQLException, RemoteException;
    
    /**
     * Log in the user
     * 
     * @param name : name of the user
     * @throws SQLException
     * @throws RemoteException 
     */
    public void login(String name) throws SQLException, RemoteException;
    
    /**
     * Log out the user
     * 
     * @param name : nme of the user
     * @throws RemoteException
     * @throws SQLException 
     */
    public void logout(String name) throws RemoteException, SQLException ;
    
    /**
     * Get the sold activity of the user
     * 
     * @param name : name of the user
     * @return the number of sold item
     * @throws RemoteException
     * @throws SQLException 
     */
    public Integer getNumberSoldItem(String name) throws RemoteException, SQLException;
    
    /**
     * Get the bought activity of the user
     * 
     * @param name : name of the user
     * @return the number of bought item
     * @throws RemoteException
     * @throws SQLException 
     */
    public Integer getNumberBoughtItem(String name) throws RemoteException, SQLException;
    
    /**
     * Unregisters the client from the market. Client must be registered
     * @param name Name of the client soon to be unregistered
     * @throws RemoteException 
     */
    public void unregister(String name) throws RemoteException;
    
    /**
     * Sell an item.
     * @param item Item soon to be sold.
     * @throws RemoteException 
     */
    public void sell(Item item, Integer amount) throws RemoteException;
    
    /**
     * Buy an item.
     * @param clientName buyer of the item.
     * @param item Item soon to be bought.
     * @throws RemoteException 
     */
    public void buy(String clientName, Integer itemId) throws RemoteException;
    
    /**
     * Lists all the items availables on the platform.
     * @throws RemoteException 
     */
    public void inspect() throws RemoteException;
    
    /**
     * Allows a client to bind a wish on a specific item.
     * @param follower Client interface of the wisher.
     * @param followerName Name of the follower
     * @param itemName Name of the Item
     * @param itemPrice Maximum price of the item
     * @throws RemoteException 
     */
    public void wish(TraderItf follower, String followerName, String itemName, Integer itemPrice) throws RemoteException;

    /**
     * 
     * @return a list of all items on the platform.
     * @throws RemoteException 
     */
    public Vector<Item> getItemList() throws RemoteException;
}
