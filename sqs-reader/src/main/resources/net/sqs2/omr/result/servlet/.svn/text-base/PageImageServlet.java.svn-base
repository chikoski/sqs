package net.sqs2.omr.result.servlet;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.config.ConfigSchemeException;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.session.logic.PageImageRenderer;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.util.HttpUtil.NetmaskAuthenticator;

public class PageImageServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;

	public static String getContextString() {
		return "p";
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (! new NetmaskAuthenticator(24).isAuthorized(req)) {
			res.sendError(403, "Forbidden");
			return;
		}
		
		//ResultBrowserSessionParam param = ResultBrowserSessionParamParser.parse(new ResultBrowserSessionParam(), req);
		long sessionID = Long.parseLong(req.getParameter("sid"));
		int masterIndex = Integer.parseInt(req.getParameter("m"));
		int tableIndex = Integer.parseInt(req.getParameter("t"));
		int rowIndex = Integer.parseInt(req.getParameter("r"));
		int pageIndex = Integer.parseInt(req.getParameter("p"));

		SessionSource sessionSource = SessionSources.get(sessionID);
		
		if(sessionSource == null){
			res.sendError(404, "Not Found");
			return;
		}
		
		FormMaster master = (FormMaster) sessionSource.getContentIndexer().getFormMasterList()
				.get(masterIndex);
		SourceDirectory sourceDirectory = sessionSource.getContentIndexer()
				.getFlattenSourceDirectoryList(master).get(tableIndex);

		try{
			BufferedImage image = PageImageRenderer.createImage(sessionID, sessionSource, master, sourceDirectory,
					rowIndex, -1, pageIndex, null);

			outputAsHttpMessage(res, image);
		}catch(ConfigSchemeException ignore){
		}
	}

	private void outputAsHttpMessage(HttpServletResponse res, BufferedImage image) throws IOException {
		String imageType = "png";
		byte[] bytes = ImageFactory.writeImage(imageType, image);
		res.setContentType("image/" + imageType);
		res.setContentLength(bytes.length);
		res.getOutputStream().write(bytes);
		image.flush();
	}

}
