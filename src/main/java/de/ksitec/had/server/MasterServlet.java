package de.ksitec.had.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.ksitec.had.server.networking.Messages;
import de.ksitec.had.server.networking.Networking;

/**
 * 
 * Copyright KSiTec GbR 2013<br>
 * <br>
 * 
 * @author y
 * 
 */
public class MasterServlet extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	@Override
	protected void service(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html");
		response.setStatus(HttpServletResponse.SC_OK);
		
		Map<String, String> replacements = new HashMap<String, String>();
		
		if (request.getParameter("triggerMaster") != null) {
			Networking.sendDatagramBroadcast(Messages.I_AM_HAD_MASTER);
		}
		
		if (request.getParameter("allstop") != null) {
			Networking.sendDatagramBroadcast(Messages.STOP_ALL);
		}
		
		PrintWriter writer = response.getWriter();
		Collection<NodeInfo> nodes = NodeDirectory.getInstance().getNodes();
		
		StringBuilder items = new StringBuilder();
		for (NodeInfo node : nodes) {
			if (node.isMaster()) {
				items.append("<li data-theme=\"b\">");
				items.append("<b>" + node.getName() + "</b>");
			} else {
				items.append("<li>");
				items.append("<a href=\"http://" + node.getIp() + ":" + ServerStarter.SERVER_PORT + "/web?triggerMaster\" title=\"Click to set new master\">" + node.getName() + "</a>");
			}
			items.append("</li>");
		}
		
		replacements.put("ITEMS", items.toString());
		writer.print(TemplateUtil.getTemplate("templates/index.html", replacements));
	}
}
