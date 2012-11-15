/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
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
    private ArrayList<Wish> wishesList;
    private ArrayList<Item> itemList;
    private String marketName;

    /**
     * Creates a new market implementation.
     *
     * @param marketName Name of the newly created market
     * @param bankName Name of the bank used for account management. Must exist.
     * @throws RemoteException
     */
    public MarketImpl(String marketName, String bankName) throws RemoteException {
        this.bankName = bankName;
        try {
            this.bank = (Bank) Naming.lookup(bankName);
        } catch (Exception ex) {
            System.out.println("Cannot get the bank " + ex);
            System.exit(1);
        }
        this.marketName = marketName;
        this.registeredClients = new HashMap<String, TraderItf>();

        this.wishesList = new ArrayList<Wish>();
        this.itemList = new ArrayList<Item>();

    }

    @Override
    public synchronized void register(String name, TraderItf client) throws RemoteException, RejectedException {
        registeredClients.put(name, client);
    }

    @Override
    public synchronized void unregister(String name) throws RemoteException {
        registeredClients.remove(name);
        for (Iterator<Wish> it = wishesList.iterator(); it.hasNext();) {
            Wish wish = it.next();
            if (name.equals(wish.getFollowerName())) {
                wishesList.remove(wishesList.indexOf(wish));
            }
        }
        for (Iterator<Item> it = itemList.iterator(); it.hasNext();) {
            Item item = it.next();
            if (item.getSellerName().equals(name)) {
                itemList.remove(itemList.indexOf(item));
            }
        }
    }

    @Override
    public synchronized void sell(Item item) throws RemoteException {
        itemList.add(item);
        lookupFollower(item);
    }

    @Override
    public synchronized void buy(String clientName, Item item) throws RemoteException {
        /*
         * Payment
         */
        String sellerName = item.getSellerName();
        Account clientAccount = null;
        Account sellerAccount = null;
        try {
            clientAccount = bank.getAccount(clientName);
            sellerAccount = bank.getAccount(sellerName);
        } catch (Exception ex) {
            Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        Integer price = item.getPrice();
        if (clientAccount.getBalance() < price) {
            //the client do not have enough money.
            registeredClients.get(clientName).notifyNotEnoughMoney();
        } else {
            try {
                clientAccount.withdraw(price);
                sellerAccount.deposit(price);
            } catch (RejectedException ex) {
                Logger.getLogger(MarketImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

            /*
             * Remove item of itemList
             */
            itemList.remove(itemList.indexOf(item));
            /*
             * Notify to the seller
             */
            TraderItf seller = registeredClients.get(sellerName);
            seller.notifyBuy(item.getName());
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
    public ArrayList<Item> getItemList() throws RemoteException {
        return itemList;

    }
}
