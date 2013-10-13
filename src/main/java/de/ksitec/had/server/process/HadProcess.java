/**
 * 
 */
package de.ksitec.had.server.process;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public abstract class HadProcess {
	
	private Process process;
	
	private boolean isRunning = false;
	
	
	protected abstract String getStartScript();
	
	protected abstract String getStopScript();
	
	protected String getWorkingDir() {
		return ".";
	}
	
	protected String[] getStartCommandArgs() {
		return new String[] {};
	}
	
	protected String[] getStopCommandArgs() {
		return new String[] {};
	}
	
	/**
	 * Start the process
	 */
	public final void doStart() {
		System.out.println("Starting " + this.getClass().getName());
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(new File(this.getWorkingDir()));
		pb.command().add(this.getStartScript());
		pb.command().addAll(Arrays.asList(this.getStartCommandArgs()));
		pb.inheritIO();
		try {
			this.process = pb.start();
			this.isRunning = true;
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Stop the process
	 */
	public final void doStop() {
		System.out.println("Stopping " + this.getClass().getName());
		this.doCustomStop();
		ProcessBuilder pb = new ProcessBuilder();
		pb.directory(new File(this.getWorkingDir()));
		pb.command().add(this.getStopScript());
		pb.command().addAll(Arrays.asList(this.getStopCommandArgs()));
		pb.inheritIO();
		
		try {
			pb.start();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (this.process != null) {
			// this.process.destroy();
		}
		this.isRunning = false;
	}
	
	protected void doCustomStart() {
		// nothing in default implementation
	}
	
	protected void doCustomStop() {
		// nothing in default implementation
	}
	
	/**
	 * @return true if the process has been started
	 */
	public boolean isRunning() {
		return this.isRunning;
	}
}
