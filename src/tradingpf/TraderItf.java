/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author fingolfin
 */
public interface TraderItf extends Remote{
    public void notifyBuy(Item item) throws RemoteException;
    public void notifyAvailable(Item item) throws RemoteException;
}
