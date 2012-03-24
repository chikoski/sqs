package net.sqs2.omr.result.servlet;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectory;

public class ExportCSVServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;
	
	public ExportCSVServlet()throws ServletException{
		super();
	}

	public static String getContextString(){
		return "exportCSV";
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException,ServletException{
		if(! HttpUtil.isLocalNetworkAccess(req)){
			res.sendError(403, "Forbidden");
			return;
		}
		SessionSource sessionSource = SessionSources.get(Long.parseLong(req.getParameter("sid")));
		int masterIndex = Integer.parseInt(req.getParameter("m"));
		int sourceDirectoryIndex = Integer.parseInt(req.getParameter("t"));
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(masterIndex);
		ResultBrowserServletParam param = ResultBrowserServletParamBuilder.build(new ResultBrowserServletParam(), req);
		SourceDirectory sourceDirectory = sessionSource.getSessionSourceContentIndexer().getSourceDirectoryList(master).get(sourceDirectoryIndex);
		String filename = sourceDirectory.getDirectory().getName()+".tsv";
		//res.setContentType("application/vnd.ms-excel");
		res.setContentType("text/plain; charset=Shift-JIS");
		res.setHeader("Content-Disposition", "attachment; filename=\""+filename+"\"");
		PrintWriter w = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(res.getOutputStream()), "MS932"));
		//new CSVContentsFactory(w, sessionSource).create(master, param.getSelectedTableIndexSet(), param.getSelectedRowIndexSet(), param.getSelectedQuestionIndexSet());
		w.flush();
	}
}
