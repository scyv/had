/**
 * 
 */
package de.ksitec.had.server;

import java.io.File;
import java.io.FileReader;
import java.util.Properties;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public final class Config {
	
	private static Properties properties;
	
	
	/**
	 * Load properties in the given properties file
	 * 
	 * @param propertiesFile the properties file
	 * @return true if loading was successful, false otherwise
	 */
	public static boolean load(String propertiesFile) {
		Config.properties = new Properties();
		try {
			Config.properties.load(new FileReader(new File(propertiesFile)));
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
	
	/**
	 * @param key the key
	 * @return The Value of a given property key
	 */
	public static String get(String key) {
		return String.valueOf(Config.properties.get(key));
	}
}
