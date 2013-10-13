package de.ksitec.had.server;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.ksitec.had.server.networking.Messages;
import de.ksitec.had.server.networking.Networking;
import de.ksitec.had.server.process.HadProcess;

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
		
		if (request.getParameter("triggerMaster") != null) {
			System.out.println("New Master triggered");
			HadProcess ffmpeg = HadProcessManager.getInstance().getFFmpeg();
			if (ffmpeg.isRunning()) {
				ffmpeg.doStop();
			}
			
			// ffmpeg starten und zeuch in feed reinklopfen
			ffmpeg.doStart();
			// ProcessBuilder pb = new ProcessBuilder();
			// pb.directory(new File("."));
			// System.out.println(this.ffmpegBinary + " " + this.sourceFile);
			// pb.command(this.ffmpegBinary, "-re", "-i", this.sourceFile, "http://localhost:12090/feed1.ffm");
			// this.ffmpegProcess = pb.start();
			
			// an alle senden "Ich bin der Master"
			Networking.sendDatagramBroadcast(Messages.I_AM_HAD_MASTER);
		}
		
		PrintWriter writer = response.getWriter();
		writer.print("<html>");
		writer.print("<body>");
		writer.print("<ul>");
		
		Collection<NodeInfo> nodes = NodeDirectory.getInstance().getNodes();
		for (NodeInfo node : nodes) {
			writer.print("<li>");
			if (node.isMaster()) {
				writer.print("<b>" + node.getIp() + "</b>");
			} else {
				writer.print("<a href=\"http://" + node.getIp() + ":" + ServerStarter.SERVER_PORT + "?triggerMaster\" title=\"Click to set new master\">" + node.getIp() + "</a>");
			}
			writer.print("</li>");
		}
		writer.print("</ul>");
		
		writer.print("</body>");
		writer.print("</html>");
	}
}
