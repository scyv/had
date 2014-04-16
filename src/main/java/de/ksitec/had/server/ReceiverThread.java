/**
 * 
 */
package de.ksitec.had.server;

import de.ksitec.had.server.networking.DatagramPacketInfo;
import de.ksitec.had.server.networking.Messages;
import de.ksitec.had.server.networking.Networking;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public class ReceiverThread extends Thread {
	
	@Override
	public void run() {
		while (true) {
			
			DatagramPacketInfo datagramMessage = Networking.receiveDatagramMessage();
			if (datagramMessage != null) {
				String senderIP = datagramMessage.getSender();
				String messageContent = datagramMessage.getMessage();
				
				NodeDirectory nodeDirectory = NodeDirectory.getInstance();
				if (messageContent.endsWith(nodeDirectory.getUUID())) {
					nodeDirectory.registerMyself(senderIP);
				}
				
				if (messageContent.startsWith(Messages.I_AM_HAD_CLIENT)) {
					// System.out.println("Client bei: " + senderIP);
					nodeDirectory.register(senderIP, false);
				} else if (messageContent.startsWith(Messages.I_AM_HAD_MASTER)) {
					// System.out.println("Master bei: " + senderIP);
					NodeInfo currentMaster = nodeDirectory.getMaster();
					
					if ((currentMaster == null) || !senderIP.equals(currentMaster.getIp())) {
						// System.out.println("NEUER Master");
						
						NodeInfo me = nodeDirectory.getMySelf();
						HadProcessManager pm = HadProcessManager.getInstance();
						// stop listening
						pm.getPlayerProcess().doStop();
						
						if (senderIP.equals(me.getIp())) {
							// start capturing
							pm.getCapturingProcess().doStart();
							
						} else {
							// stop all if running
							pm.getCapturingProcess().doStop();
							
							// start listening
							pm.getPlayerProcess().doStart();
						}
					}
					nodeDirectory.register(senderIP, true);
				} else if (messageContent.startsWith(Messages.STOP_ALL)) {
					HadProcessManager pm = HadProcessManager.getInstance();
					pm.getCapturingProcess().doStop();
					pm.getPlayerProcess().doStop();
					if (nodeDirectory.getMaster() != null) {
						nodeDirectory.register(nodeDirectory.getMaster().getIp(), false);
					}
				}
			}
		}
	}
}
