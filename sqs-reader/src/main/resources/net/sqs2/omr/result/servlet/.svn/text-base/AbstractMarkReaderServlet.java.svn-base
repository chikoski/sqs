package net.sqs2.omr.result.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.util.HttpUtil.NetmaskAuthenticator;

import org.apache.log4j.Logger;

import freemarker.template.utility.StringUtil;

public class AbstractMarkReaderServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public AbstractMarkReaderServlet() throws ServletException {
		super();
	}

	public static String getContextString() {
		return "s";
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		
		if (new NetmaskAuthenticator(24).isAuthorized(req)) {
			res.sendError(403, "Forbidden");
			return;
		}
		
		Logger.getLogger(getClass().getName()).info(req.getRequestURI());
		
		String args[] = StringUtil.split(req.getRequestURI(), '/');
		
		// req.getMethod();
		// GET, PUT, DELETE
		// "/s/${sessionID}/${masterID}/${tableID}/${rowID}/${columnID}/${itemID}/"
		
		// ID	           ID values.
		// ID,ID,ID        comma separated ID values.
		// ID-ID,ID,ID-ID  hyphen separated ID values as the value range.
		// -0000000        value selected status compact mode 
		// ~alias          a unique name stands for an ID
		// =method         export data type

		String masterID = args[0];
		String tableID = args[1];
		String rowID = args[2];
		String columnID = args[3];
		String itemID = args[4];

		String action = null;
		
		// FIXME!
	}

}
