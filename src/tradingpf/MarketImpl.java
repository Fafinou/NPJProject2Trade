/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.RemoteException;
import java.util.Map;
import se.kth.id2212.bankrmi.Account;
import se.kth.id2212.bankrmi.Bank;
import se.kth.id2212.bankrmi.RejectedException;

/**
 *
 * @author fingolfin
 */
public class MarketImpl implements MarketItf {
    
    private Account bankAccount;
    private Bank bank;
    private String bankName;
    private Map<String, TraderItf> registeredClients;
    private Map<String, Item> itemList;

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
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void buy(String name, Item item) throws RemoteException {
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
