/**
 * 
 */
package de.ksitec.had.server;

import java.util.Date;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public final class NodeInfo {
	
	private boolean masterState = false;
	
	private String ip;
	
	private String name;
	
	private Date lastUpdate;
	
	
	/**
	 * @return true, if this node is master, false otherwise
	 */
	public boolean isMaster() {
		return this.masterState;
	}
	
	/**
	 * @param master set true if the node is the master
	 */
	public void setMaster(boolean master) {
		this.masterState = master;
	}
	
	/**
	 * @return the ip of this node
	 */
	public String getIp() {
		return this.ip;
	}
	
	/**
	 * @param ip the ip of this node
	 */
	public void setIp(String ip) {
		this.ip = ip;
	}
	
	/**
	 * @return the lastUpdate
	 */
	public Date getLastUpdate() {
		return this.lastUpdate;
	}
	
	/**
	 * @param lastUpdate the lastUpdate to set
	 */
	public void setLastUpdate(Date lastUpdate) {
		this.lastUpdate = lastUpdate;
	}
	
	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}
	
	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
	
}
