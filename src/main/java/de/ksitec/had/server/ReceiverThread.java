/**
 * 
 */
package de.ksitec.had.server;

import de.ksitec.had.server.networking.DatagramPacketInfo;
import de.ksitec.had.server.networking.Messages;
import de.ksitec.had.server.networking.Networking;
import de.ksitec.had.server.process.Player;

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
				
				if (messageContent.endsWith(NodeDirectory.getInstance().getUUID())) {
					NodeDirectory.getInstance().registerMyself(senderIP);
				}
				
				if (messageContent.startsWith(Messages.I_AM_HAD_CLIENT)) {
					System.out.println("Client bei: " + senderIP);
					NodeDirectory.getInstance().register(senderIP, false);
				} else if (messageContent.startsWith(Messages.I_AM_HAD_MASTER)) {
					System.out.println("Master bei: " + senderIP);
					NodeInfo currentMaster = NodeDirectory.getInstance().getMaster();
					if ((currentMaster == null) || !senderIP.equals(currentMaster.getIp())) {
						System.out.println("NEUER Master");
						HadProcessManager.getInstance().getFFmpeg().doStop();
						
						Player ffplay = (Player) HadProcessManager.getInstance().getFFplay();
						ffplay.doStop();
						
						ffplay.setMasterNodeIP(senderIP);
						ffplay.doStart();
					}
					NodeDirectory.getInstance().register(senderIP, true);
				}
			}
		}
	}
}
