/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.Remote;
import java.rmi.RemoteException;
import se.kth.id2212.bankrmi.RejectedException;

/**
 *
 * @author fingolfin
 */
public interface MarketItf extends Remote {
    public void register(String name, TraderItf client) throws RemoteException, RejectedException;
    public void unregister(String name) throws RemoteException;
    public void sell(String name, Item item) throws RemoteException;
    public void buy(String name, Item item) throws RemoteException;
    public void inspect() throws RemoteException;
    public void wish(TraderItf follower, String itemName, Integer itemPrice) throws RemoteException;
}
