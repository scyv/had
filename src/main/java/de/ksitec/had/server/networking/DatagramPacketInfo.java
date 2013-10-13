/**
 * 
 */
package de.ksitec.had.server.networking;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public class DatagramPacketInfo {
	
	private String message;
	private String sender;
	
	
	/**
	 * @return the message
	 */
	public String getMessage() {
		return this.message;
	}
	
	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		this.message = message;
	}
	
	/**
	 * @return the sender
	 */
	public String getSender() {
		return this.sender;
	}
	
	/**
	 * @param sender the sender to set
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}
	
}
