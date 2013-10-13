/**
 * 
 */
package de.ksitec.had.server;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

/**
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public class ServerStarter {
	
	/** The Server Port **/
	public static int SERVER_PORT = 1337;
	
	
	/**
	 * @param args -
	 * @throws Exception -
	 */
	public static void main(String[] args) throws Exception {
		new ServerStarter().init();
	}
	
	private void init() throws Exception {
		
		if (!Config.load("config.properties")) {
			return;
		}
		
		Server server = new Server(ServerStarter.SERVER_PORT);
		ServerStarter.configureServer(server);
		server.start();
		
		// NodeDirectory.getInstance().registerMyself(InetAddress.getByName("localhost").getHostAddress());
		
		HadProcessManager.getInstance().getFFServer().doStart();
		
		ReceiverThread ut = new ReceiverThread();
		ut.start();
		
		DiscoveryThread dt = new DiscoveryThread();
		dt.start();
		
		// do the hucklebuck
		server.join();
	}
	
	private static void configureServer(Server server) {
		ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
		context.setContextPath("/");
		server.setHandler(context);
		context.addServlet(new ServletHolder(new MasterServlet()), "/*");
	}
	
}
