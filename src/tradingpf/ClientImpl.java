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
public class ClientImpl implements ClientItf{

    private String clientName;
    private Account bankAccount;
    private Bank bankServer;
    private String bankName;
    private String tradePfName;
    private ServerItf tradePfServer;

    public ClientImpl(String clientName, String bankName, String tradePfName) {
        this.clientName = clientName;
        this.bankName = bankName;
        this.tradePfName = tradePfName;
    }
    
    @Override
    public void notifyBuy(Item item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public void notifyAvailable(Item item) throws RemoteException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    
}
