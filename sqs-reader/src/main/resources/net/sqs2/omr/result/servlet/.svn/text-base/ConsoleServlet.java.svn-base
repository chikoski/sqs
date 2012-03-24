package net.sqs2.omr.result.servlet;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.AppConstants;
import net.sqs2.template.TemplateLoader;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ConsoleServlet extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static String getContextString(){
		return "c";
	}
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)throws IOException{
		String skin = "sqs";
		TemplateLoader templateLoader = new TemplateLoader(AppConstants.USER_CUSTOMIZED_CONFIG_DIR, "ftl", skin);
		Template template = templateLoader.getTemplate("console.ftl", "UTF-8");
		response.setContentType("text/html; charset=utf-8");
		try{
			Map<String,String> map = new HashMap<String,String>();//TODO: create map
			map.put("sessionID", request.getPathInfo().split("/")[1]);
			template.process(map, response.getWriter());
			response.getWriter().flush();
		}catch(TemplateException ex){
			ex.printStackTrace(response.getWriter());
		}
	}
	
	/*
	public void doOption(HttpServletRequest request, HttpServletResponse response)throws IOException{
		FormMasterListPathItem item = (FormMasterListPathItem)PathInfoParser.parse(request.getPathInfo());
		String context = request.getParameter("ctx");
		
		response.setContentType("text/javascript");
		PrintWriter writer = new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(response.getOutputStream()), "utf-8")); 
		if("master".equals(context)){
			JSONUtil.printAsJSON(writer, item);
		}else if("spreadSheet".equals(context)){
			JSONUtil.printAsJSON(writer, item);
		}else if("row".equals(context)){
			JSONUtil.printAsJSON(writer, item);
		}else if("question".equals(context)){
			JSONUtil.printAsJSON(writer, item);
		}
	}*/
}
