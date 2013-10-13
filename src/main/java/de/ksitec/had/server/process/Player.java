/**
 * 
 */
package de.ksitec.had.server.process;

import de.ksitec.had.server.Config;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public class Player extends HadProcess {
	
	private String masterNodeIP;
	
	
	@Override
	protected String[] getStartCommandArgs() {
		return new String[] {this.masterNodeIP};
	}
	
	@Override
	protected String getStartScript() {
		return Config.get("player_start");
	}
	
	@Override
	protected String getStopScript() {
		return Config.get("player_stop");
	}
	
	/**
	 * @return the ip of the master node
	 */
	public String getMasterNodeIP() {
		return this.masterNodeIP;
	}
	
	/**
	 * @param masterNodeIP the ip of the master node
	 */
	public void setMasterNodeIP(String masterNodeIP) {
		this.masterNodeIP = masterNodeIP;
	}
	
}
