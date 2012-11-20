/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.kth.id2212.bankrmi.Account;
import se.kth.id2212.bankrmi.Bank;
import se.kth.id2212.bankrmi.RejectedException;

/**
 * Implementation of the market.
 *
 * @author Zoé Bellot
 * @author Simon Cathébras
 */
public class MarketImpl extends UnicastRemoteObject implements MarketItf {

    private Bank bank;
    private String bankName;
    private Map<String, TraderItf> registeredClients;
    private Vector<Wish> wishesList;
    private Vector<Item> itemList;
    private String marketName;
    private Database database;

    /**
     * Creates a new market implementation.
     *
     * @param marketName Name of the newly created market
     * @param bankName Name of the bank used for account management. Must exist.
     * @throws RemoteException
     */
    public MarketImpl(String marketName, String bankName) throws RemoteException, SQLException {
        this.bankName = bankName;
        try {
            this.bank = (Bank) Naming.lookup(bankName);
        } catch (Exception ex) {
            System.out.println("Cannot get the bank " + ex);
            System.exit(1);
        }
        this.marketName = marketName;
        this.registeredClients = new HashMap<String, TraderItf>();

        this.wishesList = new Vector<Wish>();
        this.itemList = new Vector<Item>();
        this.database = new Database();
    }

    @Override
    public synchronized void register(String name, String password) throws SQLException, RemoteException {
        //registeredClients.put(name, client);
        database.insertUser(name, password);
    }

    @Override
    public synchronized boolean verifyName(String name) throws SQLException, RemoteException {
        ResultSet result = database.getUser(name);
        return (!result.next());
    }

    @Override
    public synchronized boolean verifyPassword(String password) throws SQLException, RemoteException {
        return (password.length() > 7);
    }

    @Override
    public synchronized boolean verifyPasswordForAUser(String name, String password) throws SQLException, RemoteException {
        ResultSet result = database.getUser(name);
        if (!result.next()) {
            return false;
        } else {
            return (password.equals(result.getString("Password")));
        }

    }

    @Override
    public synchronized void login(String name) throws SQLException, RemoteException {
        database.loginUser(name);
    }

    @Override
    public synchronized void unregister(String name) throws RemoteException {
        registeredClients.remove(name);
        Wish wish = null;
        for (int i = 0; i < wishesList.size(); i++) {
            wish = wishesList.get(i);
            if (name.equals(wish.getFollowerName())) {
                wishesList.remove(wishesList.indexOf(wish));
            }
        }

        Item item = null;
        for (int i = 0; i < itemList.size(); i++) {
            item = itemList.get(i);
            if (item.getSellerName().equals(name)) {
                itemList.remove(itemList.indexOf(item));
            }
        }
    }

    @Override
    public synchronized void sell(Item item, Integer amount) throws RemoteException {
        itemList.add(item);
        try {
            database.insertItem(
                    item.getName(), 
                    item.getPrice(), 
                    amount, 
                    item.getSellerName());
        } catch (SQLException ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        lookupFollower(item);
    }

    @Override
    public synchronized void buy(String clientName, Integer itemId) throws RemoteException {
        /*
         * Payment
         */
        ResultSet result = null;
        Account clientAccount = null;
        Account sellerAccount = null;
        String sellerName = null;
        Integer itemPrice = null;
        String itemName = null;
        try {
            result = database.getItem(itemId);
            result.next();
            sellerName = result.getString("Seller");
            itemPrice = result.getInt("Price");
            itemName = result.getString("Name");
        } catch (SQLException ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            clientAccount = bank.getAccount(clientName);
            sellerAccount = bank.getAccount(sellerName);
        } catch (Exception ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (clientAccount.getBalance() < itemPrice) {
            //the client do not have enough money.
            registeredClients.get(clientName).notifyNotEnoughMoney();
        } else {
            try {
                clientAccount.withdraw(itemPrice);
                sellerAccount.deposit(itemPrice);
            } catch (RejectedException ex) {
                Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                /*
                 * Remove item of itemList
                 */
                database.removeItem(itemId);
            } catch (SQLException ex) {
                Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            /*
             * Notify to the seller
             */
            TraderItf seller = null;
            try {
                seller = (TraderItf) Naming.lookup(sellerName);
            } catch (NotBoundException ex) {
                Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (MalformedURLException ex) {
                Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            seller.notifyBuy(itemName);
        }
    }

    @Override
    public void inspect() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void wish(TraderItf follower, String followerName, String itemName, Integer itemPrice) throws RemoteException {
        Wish toAdd = new Wish(follower, followerName, itemName, itemPrice);
        this.wishesList.add(toAdd);
        if (wishFound(toAdd)) {
            follower.notifyAvailable(itemName);
        }
    }

    /*
     * renvoi si le client est enregistré ou pas public boolean register(String
     * name) {
     *
     * }
     */
    /*
     * public Iterator<Item> getItemList() { throw new
     * UnsupportedOperationException("Not supported yet."); }
     */
    private boolean wishFound(Wish wish) {
        for (Iterator<Item> it = itemList.iterator(); it.hasNext();) {
            Item item = it.next();
            if (item.getName().equals(wish.getObjectName())) {
                if (item.getPrice() <= wish.getObjectPrice()) {
                    return true;
                }
            }

        }
        return false;
    }

    private void lookupFollower(Item item) throws RemoteException {
        for (Iterator<Wish> it = wishesList.iterator(); it.hasNext();) {
            Wish wish = it.next();
            if (wish.getObjectName().equals(item.getName())) {
                if (wish.getObjectPrice() >= item.getPrice()) {
                    wish.getFollower().notifyAvailable(item.getName());
                }
            }
        }
    }

    @Override
    public Integer getNumberItem() throws RemoteException {
        return itemList.size();
    }

    @Override
    public Vector<Item> getItemList() throws RemoteException {
        return itemList;

    }
}
