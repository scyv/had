/**
 * 
 */
package de.ksitec.had.server;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * Collects information about HAD nodes in the local network. The internal list of nodes is kept up to date by a thread that periodically
 * checks the age of an entry
 * 
 * @author y
 * 
 */
public final class NodeDirectory {
	
	private Map<String, NodeInfo> nodes = new ConcurrentHashMap<>();
	
	private String myIP = "";
	
	private String uuid = UUID.randomUUID().toString();
	
	private String masterIP = "";
	
	private static NodeDirectory instance = new NodeDirectory();
	
	
	private NodeDirectory() {
		new Thread() {
			
			@Override
			public void run() {
				while (true) {
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.SECOND, -30);
					
					List<String> toRemove = new ArrayList<>();
					for (Entry<String, NodeInfo> entry : NodeDirectory.this.nodes.entrySet()) {
						Date lastUpdate = entry.getValue().getLastUpdate();
						String ip = entry.getKey();
						if (lastUpdate == null) {
							toRemove.add(ip);
						} else {
							if (lastUpdate.compareTo(cal.getTime()) < 0) {
								toRemove.add(ip);
							}
						}
					}
					
					for (String ip : toRemove) {
						NodeDirectory.this.nodes.remove(ip);
					}
					
					try {
						Thread.sleep(10000);
					} catch (InterruptedException ie) {
						return;
					}
				}
				
			}
		}.start();
		
	}
	
	/**
	 * @return the instance
	 */
	public static NodeDirectory getInstance() {
		return NodeDirectory.instance;
	}
	
	/**
	 * Registers THIS node in the directory, so the directory knows who i am. If the node information already exists, the information will
	 * be replaced.
	 * 
	 * @param ip the ip of the node
	 */
	public void registerMyself(String ip) {
		this.myIP = ip;
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setIp(ip);
		nodeInfo.setMaster(false);
		nodeInfo.setLastUpdate(new Date());
		
		this.nodes.put(ip, nodeInfo);
	}
	
	/**
	 * @return the information about my self
	 */
	public NodeInfo getMySelf() {
		return this.nodes.get(this.myIP);
	}
	
	/**
	 * @return the information about the master
	 */
	public NodeInfo getMaster() {
		return this.nodes.get(this.masterIP);
	}
	
	/**
	 * 
	 * @return unique ID of the Node
	 */
	public String getUUID() {
		return this.uuid;
	}
	
	/**
	 * @return an unmodifiable list of all registered nodes (including myself)
	 */
	public Collection<NodeInfo> getNodes() {
		return Collections.unmodifiableCollection(this.nodes.values());
	}
	
	/**
	 * Registers a node in the directory. If the node information already exists, the information will be replaced.
	 * 
	 * @param ip the ip of the node
	 * @param master true if the node is master
	 */
	public void register(String ip, boolean master) {
		
		if (master) {
			// reset all nodes if a master registers
			for (Entry<String, NodeInfo> entry : NodeDirectory.this.nodes.entrySet()) {
				entry.getValue().setMaster(false);
			}
			this.masterIP = ip;
		}
		
		NodeInfo nodeInfo = new NodeInfo();
		nodeInfo.setIp(ip);
		nodeInfo.setMaster(master);
		nodeInfo.setLastUpdate(new Date());
		
		this.nodes.put(ip, nodeInfo);
	}
	
}
