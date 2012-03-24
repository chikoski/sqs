package net.sqs2.omr.result.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.logic.PageImageFactory;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectory;

public class PageImageServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;

	public static String getContextString(){
		return "p";
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)throws ServletException, IOException{
		if(! HttpUtil.isLocalNetworkAccess(req)){
			res.sendError(403, "Forbidden");
			return;
		}
		//SessionSourceContentAccessor sourceDirectoryRoot = SessionSourceContentAccessor.getInstance();
		long sessionID = Long.parseLong(req.getParameter("sid"));
		int masterIndex = Integer.parseInt(req.getParameter("m"));
		int tableIndex = Integer.parseInt(req.getParameter("t"));
		int rowIndex = Integer.parseInt(req.getParameter("r"));
		int pageIndex = Integer.parseInt(req.getParameter("p"));
		
		String mode = req.getParameter("mode");
		SessionSource sessionSource = SessionSources.get(sessionID); 
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(masterIndex);
		SourceDirectory sourceDirectory = sessionSource.getSessionSourceContentIndexer().getSourceDirectoryList(master).get(tableIndex);

		BufferedImage image = PageImageFactory.createImage(sessionID, sessionSource, master, sourceDirectory,
				rowIndex, pageIndex, mode);
		
		outputAsHttpMessage(res, image);
	}
	
	private void outputAsHttpMessage(HttpServletResponse res, BufferedImage image) throws IOException {
		String imageType = "png";
		byte[] bytes = ImageFactory.writeImage(image, imageType);
		res.setContentType("image/"+imageType);
		res.setContentLength(bytes.length);
		res.getOutputStream().write(bytes);
		image.flush();
	}
		
}
