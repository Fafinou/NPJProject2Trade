/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import se.kth.id2212.bankrmi.Bank;
import tradinggui.WindowAvailableNotify;
import tradinggui.WindowMoneyNotify;
import tradinggui.WindowSellNotify;

/**
 *
 * @author Simon Cathébras and Zoé Bellot
 */
public class TraderImpl extends UnicastRemoteObject implements TraderItf{

    private String clientName;
    private Bank bankServer;
    private MarketItf tradePfServer;


    /**
     * Creates a new client
     * 
     * @param clientName : the name of the client
     * @throws RemoteException 
     */
    public TraderImpl(String clientName) throws RemoteException{
        this.clientName = clientName;        
    }


        @Override
    public void notifyBuy(String itemName) throws RemoteException {
        WindowSellNotify window = new WindowSellNotify(itemName, clientName);
        window.setVisible(true);
        window.setAlwaysOnTop(true);
    }

    @Override
    public synchronized void notifyAvailable(String itemName) throws RemoteException {
        WindowAvailableNotify popUp = new WindowAvailableNotify(itemName, clientName);
        popUp.setVisible(true);
        popUp.setAlwaysOnTop(true);
    }
    
    @Override
    public void notifyNotEnoughMoney() throws RemoteException {
        WindowMoneyNotify popUp = new WindowMoneyNotify(clientName);
        popUp.setVisible(true);
        popUp.setAlwaysOnTop(true);
    }
    
    /**
     * 
     * @return the name of the client
     */
    public String getName() {
        return clientName;
    }


}
