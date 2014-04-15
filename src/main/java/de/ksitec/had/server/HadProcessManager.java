/**
 * 
 */
package de.ksitec.had.server;

import java.util.HashMap;
import java.util.Map;

import de.ksitec.had.server.process.CapturingProcess;
import de.ksitec.had.server.process.HadProcess;
import de.ksitec.had.server.process.Player;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * Singleton for managing all the needed sub processes
 * 
 * @author y
 * 
 */
public final class HadProcessManager {
	
	private static final String PLAYER = "player";
	private static final String CAPTURING = "capture";
	
	private static Map<String, HadProcess> processes = new HashMap<>();
	
	private static HadProcessManager instance = new HadProcessManager();
	
	
	private HadProcessManager() {
		HadProcessManager.processes.put(HadProcessManager.CAPTURING, new CapturingProcess());
		HadProcessManager.processes.put(HadProcessManager.PLAYER, new Player());
	}
	
	/**
	 * @return the instance
	 */
	public static HadProcessManager getInstance() {
		return HadProcessManager.instance;
	}
	
	/**
	 * @return Process of the player
	 */
	public HadProcess getPlayerProcess() {
		return HadProcessManager.processes.get(HadProcessManager.PLAYER);
	}
	
	/**
	 * @return Process of the capturing
	 */
	public HadProcess getCapturingProcess() {
		return HadProcessManager.processes.get(HadProcessManager.CAPTURING);
	}
	
}
