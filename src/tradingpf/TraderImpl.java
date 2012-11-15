/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import se.kth.id2212.bankrmi.Account;
import se.kth.id2212.bankrmi.Bank;
import tradinggui.WindowAvailableNotify;
import tradinggui.WindowSellNotify;

/**
 *
 * @author fingolfin
 */
public class TraderImpl extends UnicastRemoteObject implements TraderItf{

    private String clientName;
    private Bank bankServer;
    private MarketItf tradePfServer;


    public TraderImpl(String clientName) throws RemoteException{
        this.clientName = clientName;        
    }



        @Override
    public void notifyBuy(String itemName) throws RemoteException {
        WindowSellNotify window = new WindowSellNotify(itemName);
        window.setVisible(true);
        window.setAlwaysOnTop(true);
    }

    @Override
    public synchronized void notifyAvailable(String itemName) throws RemoteException {
        System.out.println("item : " + itemName + "Available");
        WindowAvailableNotify popUp = new WindowAvailableNotify(itemName, clientName);
        popUp.setVisible(true);
        popUp.setAlwaysOnTop(true);
    }
    
    public String getName() {
        return clientName;
    }


}
