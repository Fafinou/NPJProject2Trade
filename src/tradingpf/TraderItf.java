/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 *
 * @author Simon Cathébras and Zoé Bellot
 */
public interface TraderItf extends Remote{
    
    /**
     * Notify the client that his item which has the name "itemName" is sold.
     * To notify the client, the function creates a pop-up 
     * window (WindowSellNotify)
     * 
     * @param itemName : the name of the item
     * @throws RemoteException 
     */
    public void notifyBuy(String itemName) throws RemoteException;
    
    /**
     * Notify the client that the item which has the name "itemName" is available.
     * To notify the client, the function creates a pop-up 
     * window (WindowAvailableNotify)
     * 
     * @param itemName : the name of the item
     * @throws RemoteException 
     */
    public void notifyAvailable(String itemName) throws RemoteException;
}
