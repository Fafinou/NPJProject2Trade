/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.rmi.RemoteException;
import se.kth.id2212.bankrmi.Account;
import se.kth.id2212.bankrmi.Bank;

/**
 *
 * @author fingolfin
 */
public class TraderImpl implements TraderItf{

    private String clientName;
    private Account clientBankAccount;
    private Bank bankServer;
    private MarketItf tradePfServer;


    public TraderImpl(String clientName, Account clientBankAccount) {
        this.clientName = clientName;
        this.clientBankAccount = clientBankAccount;
    }

    public Account getClientBankAccount() {
        return clientBankAccount;
    }
    
    @Override
    public void notifyBuy(Item item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyAvailable(Item item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
    public String getName() {
        return clientName;
    }
}
