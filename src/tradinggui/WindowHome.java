/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradinggui;

import java.rmi.RemoteException;
import java.util.logging.Level;
import java.util.logging.Logger;
import se.kth.id2212.bankrmi.Account;
import tradingpf.MarketItf;
import tradingpf.TraderImpl;
import tradingpf.TraderItf;

/**
 *
 * @author Simon Cathébras and Zoé Bellot
 */
public class WindowHome extends javax.swing.JFrame {

    private Account account;
    private MarketItf server;
    private TraderItf client;

    /**
     * Creates new form WindowHome
     */
    public WindowHome() {
        initComponents();
    }

    /**
     * Creates new form WindowHome
     * 
     * @param account: the account of the client
     * @param server : the market
     * @param client 
     */
    public WindowHome(Account account, MarketItf server, TraderItf client) {
        initComponents();
        this.account = account;
        this.server = server;
        this.client = client;
        try {
            accountLabel.setText(Float.toString(account.getBalance()));
        } catch (RemoteException ex) {
            Logger.getLogger(WindowHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        clientNameLabel.setText(((TraderImpl) client).getName());   
    }
    

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnSell = new javax.swing.JButton();
        btnBuy = new javax.swing.JButton();
        btnUnregister = new javax.swing.JButton();
        jLabel4 = new javax.swing.JLabel();
        accountLabel = new javax.swing.JLabel();
        btnFollow = new javax.swing.JButton();
        clientNameLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        btnSell.setText("Sell an item");
        btnSell.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSellActionPerformed(evt);
            }
        });

        btnBuy.setText("Buy an item");
        btnBuy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuyActionPerformed(evt);
            }
        });

        btnUnregister.setText("Unregister");
        btnUnregister.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUnregisterActionPerformed(evt);
            }
        });

        jLabel4.setText("Total of your account :");

        accountLabel.setText("jLabel5");

        btnFollow.setText("Follow");
        btnFollow.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnFollowActionPerformed(evt);
            }
        });

        clientNameLabel.setText("jLabel1");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(btnSell)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(124, 124, 124)
                                        .addComponent(jLabel4))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGap(176, 176, 176)
                                        .addComponent(accountLabel)))
                                .addGap(79, 79, 79)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnUnregister)
                                    .addComponent(btnFollow))))
                        .addGap(5, 5, 5))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(31, 31, 31)
                        .addComponent(clientNameLabel)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnBuy)))
                .addContainerGap(29, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnBuy)
                            .addComponent(clientNameLabel))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnSell)
                        .addGap(12, 12, 12)
                        .addComponent(btnUnregister))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(52, 52, 52)
                        .addComponent(jLabel4)
                        .addGap(32, 32, 32)
                        .addComponent(accountLabel)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnFollow)
                .addContainerGap(42, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSellActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSellActionPerformed
        // TODO add your handling code here:
        WindowSell nextWindow = new WindowSell(account, server, client);
        this.setVisible(false);
        nextWindow.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnSellActionPerformed

    private void btnBuyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuyActionPerformed
        // TODO add your handling code here:
        WindowListItem nextWindow = new WindowListItem(account, server, client);
        this.setVisible(false);
        nextWindow.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnBuyActionPerformed

    private void btnUnregisterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUnregisterActionPerformed
        WindowConfig nextWindow = new WindowConfig();
        try {
            server.unregister(((TraderImpl)client).getName());
        } catch (RemoteException ex) {
            Logger.getLogger(WindowHome.class.getName()).log(Level.SEVERE, null, ex);
        }
        nextWindow.setVisible(true);
        this.setVisible(false);
        this.dispose();
    }//GEN-LAST:event_btnUnregisterActionPerformed

    private void btnFollowActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnFollowActionPerformed
        // TODO add your handling code here:
        WindowFollow nextWindow = new WindowFollow(account, server, client);
        this.setVisible(false);
        nextWindow.setVisible(true);
        this.dispose();
    }//GEN-LAST:event_btnFollowActionPerformed

     private void buttonSetVisible(Boolean visible) {
        btnBuy.setVisible(visible);
        btnSell.setVisible(visible);
        btnUnregister.setVisible(visible); 
    }
     
    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(WindowHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(WindowHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(WindowHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(WindowHome.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new WindowHome().setVisible(true);
            }
        });
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel accountLabel;
    private javax.swing.JButton btnBuy;
    private javax.swing.JButton btnFollow;
    private javax.swing.JButton btnSell;
    private javax.swing.JButton btnUnregister;
    private javax.swing.JLabel clientNameLabel;
    private javax.swing.JLabel jLabel4;
    // End of variables declaration//GEN-END:variables
}
