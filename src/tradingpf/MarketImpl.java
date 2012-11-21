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
    public synchronized void login(String name) throws SQLException {
        database.loginUser(name);
        ResultSet callBack = null;
        try {
            callBack = database.listCallBack(name);
        } catch (Exception ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        while (!callBack.next()) {
            String clientName = callBack.getString("UserName");
            String itemName = callBack.getString("ItemName");
            TraderItf client = null;
            try {
                client = (TraderImpl) Naming.lookup(clientName);
            } catch (Exception ex) {
                System.out.println("Cannot get the bank " + ex);
                System.exit(1);
            }
            if (callBack.getBoolean("Type")) {
                try {
                    /*
                     * Type = sold
                     */
                    client.notifyBuy(itemName);
                } catch (RemoteException ex) {
                    Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else {
                /*
                 * Type = available
                 */
                try {
                    /*
                     * Type = sold
                     */
                    client.notifyAvailable(itemName);
                } catch (RemoteException ex) {
                    Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
    }

    @Override
    public synchronized void logout(String name) throws RemoteException, SQLException {
        database.logoutUser(name);
    }

    @Override
    public synchronized Integer getNumberSoldItem(String name) throws RemoteException, SQLException {
        ResultSet res = database.getUser(name);
        return res.getInt("NumberSold");
    }

    @Override
    public synchronized Integer getNumberBoughtItem(String name) throws RemoteException, SQLException {
        ResultSet res = database.getUser(name);
        return res.getInt("NumberBought");
    }

    @Override
    public synchronized void unregister(String name) throws RemoteException {

        /*
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
        */
        bank.deleteAccount(name);
        try {
            database.removeCallBack(name);
            database.removeFollowingByUser(name);
            database.removeItemByUser(name);
            database.removeUser(name);
        } catch (SQLException ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public synchronized void sell(Item item, Integer amount) throws RemoteException {
        try {
            database.insertItem(
                    item.getName(),
                    item.getPrice(),
                    amount,
                    item.getSellerName());
            database.updateSoldItem(item.getSellerName());
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
                ResultSet res = database.getUser(clientName);
                if (!res.getBoolean("Log")) {
                    database.insertCallBack(true, clientName, itemName);
                } else {
                    try {
                        seller = (TraderImpl) Naming.lookup(clientName);
                    } catch (Exception ex) {
                        System.out.println("Cannot get the bank " + ex);
                        System.exit(1);
                    }
                    seller.notifyBuy(itemName);
                }

                database.updateBoughtItem(clientName);
            } catch (SQLException ex) {
                Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    @Override
    public void inspect() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void wish(TraderItf follower, String followerName, String itemName, Integer itemPrice) throws RemoteException {
        /*
         * Add the followed item
         */
        try {
            database.insertFollowed(itemName, itemPrice, followerName);
        } catch (SQLException ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void lookupFollower(Item item) throws RemoteException {
        ResultSet result = null;
        ResultSet resultUser = null;
        String followerName = null;
        TraderItf client = null;
        try {
            result = database.getUserToNotify(item.getName(), item.getPrice());
            while (result.next()) {

                followerName = result.getString("Follower");
                resultUser = database.getUser(followerName);
                resultUser.next();
                if (!resultUser.getBoolean("Log")) {
                    database.insertCallBack(false, followerName, item.getName());
                } else {
                    try {
                        client = (TraderItf) Naming.lookup(followerName);
                    } catch (NotBoundException ex) {
                        Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (MalformedURLException ex) {
                        Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
                    }

                    client.notifyAvailable(item.getName());
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public Vector<Item> getItemList() throws RemoteException {
        Vector<Item> res = new Vector<Item>();
        ResultSet result = null;
        try {
            result = database.listItem();
            while (result.next()) {
                res.add(new Item(result.getString("Name"),
                        result.getInt("Price"),
                        result.getString("Seller"),
                        result.getInt("Amount"),
                        result.getInt("Id_Item")));
            }
        } catch (Exception ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        return res;
    }
}
