package net.sqs2.omr.result.servlet;

import java.io.BufferedOutputStream;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.result.export.spreadsheet.CSVExportModule;
import net.sqs2.omr.result.path.MultiRowPath;
import net.sqs2.omr.result.path.MultiRowSelection;
import net.sqs2.omr.session.event.MasterEvent;
import net.sqs2.omr.session.report.InsideSpreadSheetResultEventFilter;
import net.sqs2.omr.session.report.ResultReportGenerator;
import net.sqs2.omr.session.report.ResultReportStopException;
import net.sqs2.omr.util.HttpUtil.NetmaskAuthenticator;

public class CSVSpreadsheetServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;

	public CSVSpreadsheetServlet() throws ServletException {
		super();
	}

	public static String getContextString() {
		return "exportCSV";
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		if (! new NetmaskAuthenticator(24).isAuthorized(req)) {
			res.sendError(403, "Forbidden");
			return;
		}
		
		MultiRowPath multiRowPath = new MultiRowPath(req.getPathInfo());
		final MultiRowSelection multiRowSelection = new MultiRowSelection(multiRowPath);
		/*
		String userAgnetName = req.getHeader("User-Agent");
		Map<String,String> params = req.getParameterMap();
		ResultBrowserContext resultBrowserContext = 
			new ResultBrowserContext(req.getPathInfo(), params, userAgnetName); 
		*/
		String title = multiRowSelection.getTitle();
		
		String filename = title + ".csv";

		res.setContentType("text/plain; charset="+CSVExportModule.DEFAULT_ENCODING);
		res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		PrintWriter w = new PrintWriter(new OutputStreamWriter(
				new BufferedOutputStream(res.getOutputStream()), CSVExportModule.DEFAULT_ENCODING));
		
		ResultReportGenerator sessionResultWalker = new ResultReportGenerator(multiRowSelection.getSessionSource(), new InsideSpreadSheetResultEventFilter(){
			public boolean accept(MasterEvent sessionEvent) {
				return multiRowSelection.getFormMaster().equals(sessionEvent.getFormMaster());
			}
		});
		sessionResultWalker.addEventConsumer(new CSVExportModule(w));
		//sessionResultWalker.addEventConsumer(new DummyExportModule());
		try{
			sessionResultWalker.call();
		}catch(ResultReportStopException ex){
			ex.printStackTrace();
		}
		w.flush();
	}	
}
