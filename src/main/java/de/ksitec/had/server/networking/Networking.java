/**
 * 
 */
package de.ksitec.had.server.networking;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public final class Networking {
	
	private static final int BROADCAST_PORT = 1337;
	private static final String BROADCAST_ADDRESS = "255.255.255.255";
	
	
	/**
	 * Broadcasts a datagram packet to the local network
	 * 
	 * @param message the message to send
	 */
	public static void sendDatagramBroadcast(String message) {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket();
			socket.setBroadcast(true);
			byte[] data = message.getBytes();
			DatagramPacket sendPacket = new DatagramPacket(data, data.length, InetAddress.getByName(Networking.BROADCAST_ADDRESS), Networking.BROADCAST_PORT);
			// System.out.println("Sending: " + message);
			socket.send(sendPacket);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ((socket != null) && !socket.isClosed()) {
				socket.close();
			}
		}
		
	}
	
	/**
	 * Opens a socket and waits for a datagram packet (Blocking!)
	 * 
	 * @return the received message
	 */
	public static DatagramPacketInfo receiveDatagramMessage() {
		DatagramSocket socket = null;
		try {
			socket = new DatagramSocket(Networking.BROADCAST_PORT);
			
			socket.setBroadcast(true);
			
			byte[] data = new byte[15000];
			DatagramPacket receivePacket = new DatagramPacket(data, data.length);
			socket.receive(receivePacket);
			DatagramPacketInfo info = new DatagramPacketInfo();
			info.setMessage(new String(receivePacket.getData()).trim());
			info.setSender(receivePacket.getAddress().getHostAddress());
			System.out.println("Received from: " + info.getSender() + " - " + info.getMessage());
			return info;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if ((socket != null) && !socket.isClosed()) {
				socket.close();
			}
		}
		return null;
	}
}
