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
public class SourcePlayer extends HadProcess {
	
	@Override
	protected String getStartScript() {
		return Config.get("sourceplayer_start");
	}
	
	@Override
	protected String getStopScript() {
		return Config.get("sourceplayer_stop");
	}
	
}
