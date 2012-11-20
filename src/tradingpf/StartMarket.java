/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tradingpf;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author fingolfin
 */
public class StartMarket {
    	private static final String USAGE = "StartMarket <market_rmi_url> <bank_rmi_url>";
	private static final String BANK = "Nordea";
        private static final String MARKET = "Ebay";
	public StartMarket(String marketName, String bankName) {
		try {
			MarketImpl marketObj = new MarketImpl(marketName, bankName);
			// Register the newly created object at rmiregistry.
			java.rmi.Naming.rebind(marketName, marketObj);
			System.out.println(marketObj.toString() + " is ready.");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		if (args.length > 2 || (args.length > 0 && args[0].equalsIgnoreCase("-h"))) {
			System.out.println(USAGE);
			System.exit(1);
		}

                String bankName = BANK;
                String marketName = MARKET;

		new StartMarket(marketName,bankName);
	}
        
       
}
