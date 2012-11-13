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
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.kth.id2212.bankrmi.Account;
import se.kth.id2212.bankrmi.Bank;
import se.kth.id2212.bankrmi.RejectedException;

/**
 *
 * @author fingolfin
 */
public class MarketImpl extends UnicastRemoteObject implements MarketItf {
    
    private Bank bank;
    private String bankName;
    private Map<String, TraderItf> registeredClients;
    private Map<String, Item> itemList;
    private String marketName;

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
        this.itemList = new HashMap<String, Item>();
    }

    @Override
    public void register(String name, TraderItf client) throws RemoteException, RejectedException {
        registeredClients.put(name, client);
    }

    @Override
    public void unregister(String name) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void sell(String name, Item item) throws RemoteException {
        itemList.put(name, item) ;
    }

    @Override
    public void buy(String clientName, Item item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void inspect() throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void wish(String name, String itemName, Integer itemPrice) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    /* renvoi si le client est enregistré ou pas
     * public boolean register(String name) {
     * 
     * }
     */
    
    /*
     * public Iterator<Item> getItemList() {
        throw new UnsupportedOperationException("Not supported yet.");
    }*/
}
