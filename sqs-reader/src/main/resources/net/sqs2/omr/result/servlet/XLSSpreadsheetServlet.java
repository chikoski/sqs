package net.sqs2.omr.result.servlet;

import java.io.BufferedOutputStream;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.export.spreadsheet.ExcelExportModule;
import net.sqs2.omr.result.path.MultiRowPath;
import net.sqs2.omr.result.path.MultiRowSelection;
import net.sqs2.omr.session.event.MasterEvent;
import net.sqs2.omr.session.report.InsideSpreadSheetResultEventFilter;
import net.sqs2.omr.session.report.ResultReportGenerator;
import net.sqs2.omr.session.report.ResultReportStopException;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.util.HttpUtil.NetmaskAuthenticator;

public class XLSSpreadsheetServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;

	public XLSSpreadsheetServlet() throws ServletException {
		super();
	}

	public static String getContextString() {
		return "exportXLS";
	}

	@Override
	public void service(HttpServletRequest req, HttpServletResponse res) throws IOException, ServletException {
		if (! new NetmaskAuthenticator(24).isAuthorized(req)) {
			res.sendError(403, "Forbidden");
			return;
		}

		/*
		Map<String,String> params = req.getParameterMap();
		String userAgnetName = req.getHeader("User-Agent");
		ResultBrowserContext resultBrowserContext = 
			new ResultBrowserContext(req.getPathInfo(), params, userAgnetName); 
		*/
		MultiRowPath multiRowPath = new MultiRowPath(req.getPathInfo());
		MultiRowSelection multiRowSelection = new MultiRowSelection(multiRowPath);
		SessionSource sessionSource = multiRowSelection.getSessionSource();
		String filename = sessionSource.getRootDirectory().getName() + ".xls";
		res.setContentType("application/vnd.ms-excel");
		res.setHeader("Content-Disposition", "attachment; filename=\"" + filename + "\"");
		OutputStream outputStream = new BufferedOutputStream(res.getOutputStream());

		final FormMaster master = multiRowSelection.getFormMaster();

		ResultReportGenerator sessionResultWalker = new ResultReportGenerator(sessionSource, new InsideSpreadSheetResultEventFilter(){
			public boolean accept(MasterEvent sessionEvent) {
				return master.equals(sessionEvent.getFormMaster());
			}
		});
		sessionResultWalker.addEventConsumer(new ExcelExportModule(outputStream));
		//sessionResultWalker.addEventConsumer(new DummyExportModule());
		try{
			sessionResultWalker.call();
		}catch(ResultReportStopException ex){
			ex.printStackTrace();
		}
		outputStream.flush();
	}

}
