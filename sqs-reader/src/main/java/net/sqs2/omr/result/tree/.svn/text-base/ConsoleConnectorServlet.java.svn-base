package net.sqs2.omr.result.tree;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map.Entry;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.result.model.ModelItemMap;
import net.sqs2.util.HTMLUtil;

public abstract class ConsoleConnectorServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public ConsoleConnectorServlet() {
		super();
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)throws IOException{
		String dir = req.getParameter("dir");
		long sessionID = Long.parseLong(req.getParameter("sid"));
		res.setContentType("text/html; charset=UTF-8");
		PrintWriter w = res.getWriter();
		w.print("<ul class=\"jqueryFileTree\" style=\"display: none;\">");
		printPathTreeItems(w, sessionID, dir);
		w.print("</ul>");
	}
	
	protected abstract void printPathTreeItems(PrintWriter w, long sessionID, String dir); 
	
	protected static class ListItem{
		public static enum State {COLLAPSED_BRANCH, BRANCH, LEAF}
		String name;
		String path;
		String type;
		State state;
		ModelItemMap modelItemMap;
		
		public ListItem(String name, String path, String type, State state){
			this.name = name;
			this.path = path;
			this.type = type;
			this.state = state;
		}
		public ListItem(String name, String path, String type, State state, ModelItemMap modelItemMap){
			this(name, path, type, state);
			this.modelItemMap = modelItemMap;
		}
		public String getName() {
			return name;
		}
		public String getPath() {
			return path;
		}
		public String getType() {
			return type;
		}
		public State getState() {
			return state;
		}
		
		public ModelItemMap getModelItemMap(){
			return this.modelItemMap;
		}
	}
	
	protected void printListItems(PrintWriter w, String rootPath, ListItem listItem){
		String clazz = null;
		switch(listItem.getState()){
		case COLLAPSED_BRANCH:
			clazz = "directory collapsed";
			break;				
		case BRANCH:
			clazz = "directory";
			break;				
		case LEAF:
			clazz = "file ext_"+HTMLUtil.escapeHTML(listItem.getType());
			break;				
		}
		w.print("<li class=\""+clazz+"\">");
		w.print("<a href=\"#\" rel=\""+HTMLUtil.escapeHTML(rootPath+listItem.getPath())+"\">"+HTMLUtil.escapeHTML(listItem.getName())+"</a>");
		
		if(listItem.getModelItemMap() != null){
			//printModelItemMap(w, listItem);
		}
		
		w.print("</li>");
	}

	private void printModelItemMap(PrintWriter w, ListItem listItem) {
		w.print("<dl style='display:none'>");
		for(Entry entry: listItem.getModelItemMap().entrySet()){
			w.print("<dt>");
			w.print(HTMLUtil.escapeHTML(entry.getKey().toString()));
			w.print("</dt>");
			w.print("<dd>");
			w.print(HTMLUtil.escapeHTML(entry.getValue().toString())); // JSON
			w.print("</dd>");
		}
		w.print("</dl>");
	}
	
	protected interface ModelToPathItemFactory<I>{
		public ListItem create(int index, I src);
	}
}