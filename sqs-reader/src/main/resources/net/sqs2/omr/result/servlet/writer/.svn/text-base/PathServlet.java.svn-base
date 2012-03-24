/*

 PathServlet.java

 Copyright 2010 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2010/04/12

 */
package net.sqs2.omr.result.servlet.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.AppConstants;
import net.sqs2.omr.MarkReaderJarURIContext;
import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.model.AbstractPageTask;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.result.path.SingleMasterPath;
import net.sqs2.omr.result.servlet.ViewModeUtil;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.util.FreeMarkerHandler;
import net.sqs2.omr.util.HttpUtil;
import net.sqs2.sound.SoundManager;
import net.sqs2.util.Resource;

public class PathServlet extends HttpServlet {
		
	/*
	 * b browse 0 selectIitemRange Selector: ... > MultiRowMultiQuestionPagePath
	 ** i image 3 selectSingleImage Selector: ... > SingleImagePath,SingleImageMultiQuestionPath  
	 ** t target 4 targetSingleItem Selector: ... > SingleRowSingleQuestionPath,AnswerItemPath	 
	 * c chart 2 statQuestionSelector: ... > MultiRowSingleQuestionPath  
	 * r range 1 statIitemRange Selector: ... > MultiRowMultiQuestionPath  
	 * C groupChart 2 statQuestionSelector: ... > GroupedMultiRowSingleQuestionPath  
	 * R groupRange 1 statIitemRange Selector: ... > GroupedMultiRowMultiQuestionPath  
	 */
	
	private  static final long serialVersionUID = 0L;
	 static private Resource resource = new Resource("messages");
	
	String skinName;
	URL beepSoundURL;

	public PathServlet() throws ServletException {
		super();
		try {
			this.beepSoundURL = new URL(MarkReaderJarURIContext.getSoundBaseURI() + "pi77.wav");
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		}
	}

	public static String getContextString() {
		return "p";
	}

	@Override
	protected void service(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
		if (!HttpUtil.isLocalNetworkAccess(req)) {
			res.sendError(403, "Forbidden:"+req.getRemoteHost()+"("+req.getRemoteAddr()+")");
			return;
		}
		SingleMasterPath masterPath = new SingleMasterPath(req.getPathInfo());
		ResultBrowserContext contentSelection = new ResultBrowserContext(masterPath, req.getParameterMap(), req.getHeader("User-Agent"));
		String skinName = AppConstants.GROUP_ID;
		Map<String,Object> map = new HashMap<String,Object>();
		FreeMarkerHandler freeMarkerHandler =
			new FreeMarkerHandler(AppConstants.USER_CUSTOMIZED_CONFIG_DIR, skinName, "result.ftl");
		
		HttpUtil.logRequestedURI(req);
		SessionSource sessionSource = contentSelection.getSessionSource();

		if (sessionSource == null) {// && 0 <
			res.sendError(404, "Not Found");
			//res.setContentType("text/plain; charset=UTF-8");
			//PrintWriter w = new PrintWriter(res.getWriter());
			//w.println("ERROR_SESSION_HAS_NOT_STARTED");
			return;

		}else{
			if (!req.getMethod().equals("GET") && !req.getMethod().equals("POST")&& 
					!req.getMethod().equals("DELETE") && !req.getMethod().equals("PUT")) {
				res.sendError(405, "Method Not Allowed");
				return;
			}
			String updater = contentSelection.getResultBrowserParam().getUpdater();
			if (updater == null) {
				// send HTML
				super.service(req, res);
			} else {
				// send JavaScript
				res.setContentType("text/javascript; charset=UTF-8");
				PrintWriter w = new PrintWriter(res.getWriter());
				if (updater.equals("t") || updater.equals("m")) {
					printRowsAsJSON(w, contentSelection);
				}
				
				//map
				
				printBodyAsJSON(w, contentSelection);
			}
		}
	}

	private void printRowsAsJSON(PrintWriter w, ResultBrowserContext contentSelection) {
		w.println(new RowJSONWriter(contentSelection.getSessionSource(), PathServlet.resource).parseAnswer(param));
	}

	private void printBodyAsJSON(PrintWriter w, ResultBrowserContext contentSelection)throws IOException {
		String viewMode = contentSelection.getResultBrowserParam().getViewMode();
		FormMaster master = (FormMaster) contentSelection.getFormMaster();
		if (ViewModeUtil.isFormEditMode(viewMode)) {
			new FocusedFormAreaContentsJSONWriter(w, contentSelection);
		} else if (ViewModeUtil.isExportMode(viewMode) || ViewModeUtil.isSimpleChartViewMode(viewMode)
				|| ViewModeUtil.isCrossChartViewMode(viewMode) || ViewModeUtil.isCrossChartListViewMode(viewMode)
				|| ViewModeUtil.isGroupCrossChartViewMode(viewMode)
				|| ViewModeUtil.isGroupSimpleChartViewMode(viewMode)) {
			new StatisticsContentsJSONWriter(w, contentSelection);
		} else if (ViewModeUtil.isPageErrorViewMode(viewMode)) {
			new ErrorPageContentsJSONWriter(w, contentSelection);
		} else if (ViewModeUtil.isLowReliabilityViewMode(viewMode) || ViewModeUtil.isNoAnswerErrorViewMode(viewMode)
				|| ViewModeUtil.isMultiAnswerErrorViewMode(viewMode)) {
			new ErrorFormAreaContentsJSONWriter(w, contentSelection);
		} else {
			// Logger.getLogger(getClass().getName()).severe("ERROR: v="+viewMode);
		}
		playSound();
	}

	private void playSound() {
		SoundManager.getInstance().play(this.beepSoundURL);
	}

	protected Map<String,Object> createMap(long sessionID) {
		SessionSource sessionSource = SessionSources.get(sessionID);

		Map<String,Object> map = new HashMap<String,Object>();
		map.put("title", "SQSResultBrowser:"+sessionSource.getRootDirectory().getName());
		map.put("sourceDirectoryName", sessionSource.getRootDirectory().getName());
		map.put("masterOptions", new MasterJSONWriter(sessionSource).create(null));
		map.put("tableOptionsArray", new TableJSONWriter(sessionSource, PathServlet.resource).create(null));
		map.put("questionOptionsArray", new QuestionJSONWriter(sessionSource).create(null));
		map.put("rowOptionsArray", "[]");
		map.put("cssPath", "/jar/css");
		map.put("imagePath", "/jar/image");
		map.put("javaScriptPath", "/jar/js");
		map.put("sid", Long.toString(sessionSource.getSessionID()));
		map.put("language", Locale.getDefault().getLanguage());
		return map;
	}

	@SuppressWarnings("unused")
	private List<PageAreaResult> getPageAreaResultList(SourceDirectory sourceDirectory, int columnIndex, PageMaster pageMaster, FormArea primaryFormArea, PageTaskAccessor pageTaskAccessor) {
		PageID pageID = sourceDirectory.getPageID(columnIndex * pageMaster.getNumPages()
				+ primaryFormArea.getPage() - 1);
		AbstractPageTask pageTask = pageTaskAccessor.get(pageMaster, primaryFormArea.getPage(), pageID);
		return pageTask.getPageTaskResult().getPageAreaResultList();
	}
}
