/*

 ResultBrowserServlet.java

 Copyright 2008 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2008/01/13

 */
package net.sqs2.omr.result.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.MarkReaderJarURIContext;
import net.sqs2.omr.httpd.SQSHttpdManager;
import net.sqs2.omr.httpd.VelocityViewServlet;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.result.contents.ErrorFormAreaContentsJSONFactory;
import net.sqs2.omr.result.contents.ErrorPageContentsJSONFactory;
import net.sqs2.omr.result.contents.FormAreaFocusedContentsJSONFactory;
import net.sqs2.omr.result.contents.MasterJSONFactory;
import net.sqs2.omr.result.contents.QuestionJSONFactory;
import net.sqs2.omr.result.contents.RowJSONFactory;
import net.sqs2.omr.result.contents.StatisticsContentsJSONFactory;
import net.sqs2.omr.result.contents.TableJSONFactory;
import net.sqs2.omr.result.contents.ViewMode;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.AbstractTask;
import net.sqs2.omr.task.PageAreaCommand;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.sound.SoundManager;
import net.sqs2.util.Resource;

import org.apache.velocity.VelocityContext;

public class ResultBrowserServlet extends VelocityViewServlet {
	private static final long serialVersionUID = 0L;
	static private Resource resource = new Resource("messages");
	URL beepSoundURL;
	
	public ResultBrowserServlet()throws ServletException{
		super();
		try{
			this.beepSoundURL = new URL(MarkReaderJarURIContext.getSoundBaseURI()+"pi77.wav");
		}catch(MalformedURLException ex){
			ex.printStackTrace();
		}
	}

	public static String getContextString(){
		return "e";
	}

	@Override
	protected String getTemplateResourceName() {
		return "vm/result.vm";
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if(! req.getMethod().equals("GET") && ! req.getMethod().equals("POST")){
			res.sendError(405, "Method Not Allowed");
			return;
		}
		if(! HttpUtil.isLocalNetworkAccess(req)){
			res.sendError(403, "Forbidden");
			return;
		}
			
		long sessionID = Long.parseLong(req.getParameter("sid"));
		SessionSource sessionSource = SessionSources.get(sessionID);
		ResultBrowserServletParam param = ResultBrowserServletParamBuilder.build(new ResultBrowserServletParam(), req);
		debugOut(req);

		if(sessionSource != null){//&& 0 < sessionSource.getPageMasterList().size()
			if(param.getSessionID() != -1L && param.getSessionID() != sessionID){
				res.sendError(404, "Not found(Invalid Session ID)");
				return;
			}
			if(param.getUpdater() == null){
				// send HTML
				super.service(req, res);
			}else{
				// send JavaScript
				res.setContentType("text/javascript; charset=UTF-8");
				PrintWriter w = new PrintWriter(res.getWriter());
				String updater = param.getUpdater();
				if(updater.equals("t") || updater.equals("m")){
					w.println(new RowJSONFactory(sessionSource, this.resource).create(param));
				}
				printBodyAsJSON(w, sessionSource, param);
			}
		}else{
			res.setContentType("text/plain; charset=UTF-8");
			PrintWriter w = new PrintWriter(res.getWriter());
			w.println("ERROR_SESSION_HAS_NOT_STARTED");
		}
	}

	private void printBodyAsJSON(PrintWriter w, SessionSource sessionSource, ResultBrowserServletParam param) throws IOException{
		int viewMode = param.getViewMode();
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(param.getSelectedMasterIndex());
		if(ViewMode.isFormEditMode(viewMode)){
			new FormAreaFocusedContentsJSONFactory(w, sessionSource, param.getAnswerItemIndex(), param.getNumMaxAnswerItems(), param.isMSIE()).create(master, param.getSelectedTableIndexSet(), param.getSelectedRowIndexSet(), param.getSelectedQuestionIndexSet());
		}else if(ViewMode.isExportMode(viewMode) || ViewMode.isSimpleChartViewMode(viewMode) || 
				ViewMode.isCrossChartViewMode(viewMode) || ViewMode.isCrossChartListViewMode(viewMode) || ViewMode.isGroupCrossChartViewMode(viewMode) || ViewMode.isGroupSimpleChartViewMode(viewMode)){
			new StatisticsContentsJSONFactory(w, sessionSource, param.getViewMode()).create(master, param.getSelectedTableIndexSet(), param.getSelectedRowIndexSet(), param.getSelectedQuestionIndexSet());
		}else if(ViewMode.isPageErrorViewMode(viewMode)){ 
			new ErrorPageContentsJSONFactory(w, sessionSource).create(master, 
					param.getSelectedTableIndexSet(), param.getSelectedRowIndexSet(), param.getSelectedQuestionIndexSet());
		}else if(ViewMode.isLowReliabilityViewMode(viewMode) || ViewMode.isNoAnswerErrorViewMode(viewMode) || ViewMode.isMultiAnswerErrorViewMode(viewMode)){
			new ErrorFormAreaContentsJSONFactory(w, sessionSource, 
					param.getAnswerItemIndex(), param.getNumMaxAnswerItems(), 
					param.isMSIE(), 
					ViewMode.isNoAnswerErrorViewMode(viewMode), 
					ViewMode.isMultiAnswerErrorViewMode(viewMode)).create(master,
							param.getSelectedTableIndexSet(), param.getSelectedRowIndexSet(), param.getSelectedQuestionIndexSet());
		}else{
			 //Logger.getAnonymousLogger().severe("ERROR: v="+viewMode);
		}
		playSound();
	}
	
	private void playSound(){
		SoundManager.getInstance().play(this.beepSoundURL);
		//Toolkit.getDefaultToolkit().beep();
	}

	@Override
	protected VelocityContext createVelocityContext(long sessionID){
		SessionSource sessionSource = SessionSources.get(sessionID);

		VelocityContext vc = new VelocityContext();
		vc.put("sourceDirectoryName",  sessionSource.getRootDirectory().getName());
		vc.put("masterOptions", new MasterJSONFactory(sessionSource).create(null));
		vc.put("tableOptionsArray",  new TableJSONFactory(sessionSource, this.resource).create(null));
		vc.put("questionOptionsArray",  new QuestionJSONFactory(sessionSource).create(null));
		vc.put("rowOptionsArray", "[]");
		vc.put("cssPath", "/jar/css");
		vc.put("imagePath", "/jar/image");
		vc.put("javaScriptPath", "/jar/js");
		vc.put("sid", sessionSource.getSessionID());
		return vc;
	}
	
	private void debugOut(HttpServletRequest req){
		char delim = '?';
		try{
			System.err.print(SQSHttpdManager.getEXIgridHttpd().getBaseURI()+req.getRequestURI()+"?");
		}catch(Exception ignore){}
		for(Object o: req.getParameterMap().entrySet()){
			Map.Entry<String,String[]> entry = (Map.Entry<String,String[]>)o;
			for(String value: entry.getValue()){
				if(delim == '&'){
					System.err.print('&');
				}
				delim = '&';
				System.err.print(entry.getKey());
				System.err.print("=");
				System.err.print(value);
			}
		}
		System.err.println();
	}

	List<PageAreaCommand> getPageAreaCommandList(SourceDirectory sourceDirectory, int columnIndex, PageMaster pageMaster, FormArea defaultFormArea, TaskAccessor pageTaskAccessor){
		PageID pageID = sourceDirectory.getPageID(columnIndex*pageMaster.getNumPages()+defaultFormArea.getPage() - 1);
		AbstractTask pageTask = pageTaskAccessor.get(pageMaster, defaultFormArea.getPage(), pageID);
		return pageTask.getTaskResult().getPageAreaCommandList();
	}

}
