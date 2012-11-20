/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.Remote;
import java.rmi.RemoteException;
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
     *
     * @param name
     * @param password
     * @return
     * @throws SQLException
     * @throws RemoteException
     */
    public boolean verifyName(String name) throws SQLException, RemoteException;
    
    
    public boolean verifyPassword(String password) throws SQLException, RemoteException;
    
    public boolean verifyPasswordForAUser(String name, String password) throws SQLException, RemoteException;
    
    public void login(String name) throws SQLException, RemoteException;
    
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
    public void sell(Item item) throws RemoteException;
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
     * @return the count of available items all over the platform.
     * @throws RemoteException 
     */
    public Integer getNumberItem()throws RemoteException;
    /**
     * 
     * @return a list of all items on the platform.
     * @throws RemoteException 
     */
    public Vector<Item> getItemList() throws RemoteException;
}
