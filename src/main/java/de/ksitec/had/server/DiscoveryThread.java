/**
 * 
 */
package de.ksitec.had.server;

import de.ksitec.had.server.networking.Messages;
import de.ksitec.had.server.networking.Networking;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public class DiscoveryThread extends Thread {
	
	private static final int DISCOVERY_INTERVALL_MS = 15000;
	
	
	@Override
	public void run() {
		
		try {
			
			while (true) {
				NodeInfo mySelf = NodeDirectory.getInstance().getMySelf();
				if ((mySelf != null) && mySelf.isMaster()) {
					Networking.sendDatagramBroadcast(Messages.I_AM_HAD_MASTER + "_" + NodeDirectory.getInstance().getUUID());
				} else {
					Networking.sendDatagramBroadcast(Messages.I_AM_HAD_CLIENT + "_" + NodeDirectory.getInstance().getUUID());
				}
				
				Thread.sleep(DiscoveryThread.DISCOVERY_INTERVALL_MS);
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
}
