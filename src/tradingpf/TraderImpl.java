/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.Serializable;
import java.rmi.RemoteException;
import se.kth.id2212.bankrmi.Account;
import se.kth.id2212.bankrmi.Bank;
import tradinggui.WindowAvailableNotify;

/**
 *
 * @author fingolfin
 */
public class TraderImpl implements TraderItf, Serializable{

    private String clientName;
    private Bank bankServer;
    private MarketItf tradePfServer;


    public TraderImpl(String clientName) {
        this.clientName = clientName;
        //this.clientBankAccount = clientBankAccount;
    }

    
    public void main(){
        //TraderImpl trader = new TraderImpl(clientName, clientBankAccount)
    }


        @Override
    public void notifyBuy(String itemName) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public synchronized void notifyAvailable(String itemName) throws RemoteException {
        //WindowAvailableNotify popUp = new WindowAvailableNotify(itemName);
        //popUp.setVisible(true);
        System.out.println("item : " + itemName + "Available");
    }
    
    public String getName() {
        return clientName;
    }


}
