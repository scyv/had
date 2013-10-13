/**
 * 
 */
package de.ksitec.had.server;

import java.util.HashMap;
import java.util.Map;

import de.ksitec.had.server.process.SourcePlayer;
import de.ksitec.had.server.process.Player;
import de.ksitec.had.server.process.Server;
import de.ksitec.had.server.process.HadProcess;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * Singleton for managing all the needed sub processes
 * 
 * @author y
 * 
 */
public final class HadProcessManager {
	
	private static final String FFMPEG = "ffmpeg";
	private static final String FFSERVER = "ffserver";
	private static final String FFPLAY = "ffplay";
	
	private static Map<String, HadProcess> processes = new HashMap<>();
	
	private static HadProcessManager instance = new HadProcessManager();
	
	
	private HadProcessManager() {
		HadProcessManager.processes.put(HadProcessManager.FFSERVER, new Server());
		HadProcessManager.processes.put(HadProcessManager.FFMPEG, new SourcePlayer());
		HadProcessManager.processes.put(HadProcessManager.FFPLAY, new Player());
	}
	
	/**
	 * @return the instance
	 */
	public static HadProcessManager getInstance() {
		return HadProcessManager.instance;
	}
	
	/**
	 * @return Process for the ffserver
	 */
	public HadProcess getFFServer() {
		return HadProcessManager.processes.get(HadProcessManager.FFSERVER);
	}
	
	/**
	 * @return Process for the ffmpeg
	 */
	public HadProcess getFFmpeg() {
		return HadProcessManager.processes.get(HadProcessManager.FFMPEG);
	}
	
	/**
	 * @return Process for the ffplay
	 */
	public HadProcess getFFplay() {
		return HadProcessManager.processes.get(HadProcessManager.FFPLAY);
	}
	
}
