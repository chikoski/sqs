package net.sqs2.omr.result.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;

public class ExportXLSServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;

	public ExportXLSServlet() throws ServletException {
		super();
	}

	public static String getContextString() {
		return "exportXLS";
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws IOException, ServletException {
		if (!HttpUtil.isLocalNetworkAccess(req)) {
			res.sendError(403, "Forbidden");
			return;
		}

		long sessionID = Long.parseLong(req.getParameter("sid"));
		SessionSource sessionSource = SessionSources.get(sessionID);

		ResultBrowserServletParam param = ResultBrowserServletParamBuilder
				.build(new ResultBrowserServletParam(), req);

		String filename = SessionSources.get(sessionID).getRootDirectory()
				.getName()
				+ ".xls";
		res.setContentType("application/vnd.ms-excel");
		res.setHeader("Content-Disposition", "attachment; filename=\""
				+ filename + "\"");
		OutputStream out = new BufferedOutputStream(res.getOutputStream());
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(param.getSelectedMasterIndex());
		//new XLSContentsFactory(out, sessionSource).create(master, param.getSelectedTableIndexSet(), param.getSelectedRowIndexSet(),
		//		param.getSelectedQuestionIndexSet());
		out.flush();
	}

}
