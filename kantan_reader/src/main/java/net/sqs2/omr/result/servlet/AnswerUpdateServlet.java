/*

 AnswerUpdateServlet.java

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
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.result.model.RowAccessor;
import net.sqs2.omr.result.model.TextAreaAnswer;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.util.StringUtil;

public class AnswerUpdateServlet extends HttpServlet {
	private static final long serialVersionUID = 0L;
	
	public AnswerUpdateServlet() throws ServletException{
		super();
	}
	
	public static String getContextString(){
		return "u";
	}
	
	boolean isMarked(float markRecogThreshold, MarkAreaAnswerItem item){
		return markRecogThreshold < item.getDensity();
	}
	
	@Override
	public void service(HttpServletRequest req, HttpServletResponse res)throws IOException,ServletException{
		if(! HttpUtil.isLocalNetworkAccess(req)){
			res.sendError(403, "Forbidden");
			return;
		}
		String key = req.getParameter("key");
		String value = req.getParameter("value");
		float markRecogThreshold = 0.0f;
		if(req.getParameter("threshold") != null){
			try{
				markRecogThreshold = Float.parseFloat(req.getParameter("threshold"));
			}catch(NumberFormatException ignore){
			}
		}
		
		List<String> args = StringUtil.split(key,  ',');
		if(4 != args.size() && 5 != args.size()){
			res.sendError(400, "Bad request");
			return;
		}
		
		long sessionID = Long.parseLong(req.getParameter("sid"));
		int masterIndex = Integer.parseInt(args.get(0));
		int tableIndex = Integer.parseInt(args.get(1));
		int rowIndex = Integer.parseInt(args.get(2));
		int columnIndex = Integer.parseInt(args.get(3));
		int formAreaIndex = -1;
		if(5 == args.size()){
			formAreaIndex = Integer.parseInt(args.get(4));
		}
		
		SessionSource sessionSource = SessionSources.get(sessionID);
		RowAccessor rowAccessor = sessionSource.getSessionSourceContentAccessor().getRowAccessor();
		FormMaster master = (FormMaster)sessionSource.getSessionSourceContentIndexer().getPageMasterList().get(masterIndex);

		String masterPath = master.getRelativePath();
		SourceDirectory sourceDirectory = sessionSource.getSessionSourceContentIndexer().getSourceDirectoryList(master).get(tableIndex);
		String sourceDirectoryPath = sourceDirectory.getPath();
		
		List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
		FormArea defaultFormArea = formAreaList.get(0);

		Row row = (Row)rowAccessor.get(masterPath, sourceDirectoryPath, rowIndex);
		if(row == null){
			res.sendError(404, "Not found");
			return;
		}
		
		setFormAreaValue(row, columnIndex, defaultFormArea, formAreaIndex, value, markRecogThreshold);
		rowAccessor.put(row);
		rowAccessor.flush();

		writeBackJavaScript(res, key, defaultFormArea);
	}

	private void setFormAreaValue(Row row, int columnIndex, FormArea defaultFormArea, 
			int formAreaIndex, String value, float markRecogThreshold) {
		
		if(defaultFormArea.isTextArea()){
			setTextAreaValue(row, columnIndex, value);
		}else if(defaultFormArea.isMarkArea()){
			MarkAreaAnswer answer = (MarkAreaAnswer)row.getAnswer(columnIndex);
			MarkAreaAnswerItem[] markAreaAnswerItemArray = answer.getMarkAreaAnswerItemArray();
			if(defaultFormArea.isSelect1()){
				setSelect1AnswerValue(answer, markAreaAnswerItemArray, formAreaIndex, markRecogThreshold);					
			}else if(defaultFormArea.isSelect()){
				setSelectAnswerValue(answer, markAreaAnswerItemArray, formAreaIndex, value, markRecogThreshold);
			}
		}
	}

	private void setSelectAnswerValue(MarkAreaAnswer answer,
			MarkAreaAnswerItem[] markAreaAnswerItemArray, int formAreaIndex,
			String value, float markRecogThreshold) {
		MarkAreaAnswerItem selectedItem = markAreaAnswerItemArray[formAreaIndex];
		if("1".equals(value)){
			if(isMarked(markRecogThreshold, selectedItem)){
				selectedItem.setManualSelected(null);
			}else{
				selectedItem.setManualSelected(Boolean.TRUE);
			}
		}else{
			if(isMarked(markRecogThreshold, selectedItem)){
				selectedItem.setManualSelected(Boolean.FALSE);
			}else{
				selectedItem.setManualSelected(null);
			}
		}
		
		answer.setManualMode(false);
		for(MarkAreaAnswerItem item: markAreaAnswerItemArray){
			if(item.isManualMode()){
				answer.setManualMode(true);
				break;
			}
		}
	}

	private void setSelect1AnswerValue(MarkAreaAnswer answer,
			MarkAreaAnswerItem[] markAreaAnswerItemArray, int formAreaIndex,
			float markRecogThreshold) {
		if(formAreaIndex != -1){
			// "another answer item" was specified
			int numMarkedItems = 0;
			for(int i=0; i < markAreaAnswerItemArray.length; i++){
				MarkAreaAnswerItem item = markAreaAnswerItemArray[i];
				if(isMarked(markRecogThreshold, item)){
					item.setManualSelected(null);
				}else{
					numMarkedItems++;
					item.setManualSelected(Boolean.FALSE);
				}
			}
			MarkAreaAnswerItem selectedItem = markAreaAnswerItemArray[formAreaIndex];
			if(isMarked(markRecogThreshold, selectedItem)){ //not marked
				selectedItem.setManualSelected(Boolean.TRUE); 
				answer.setManualMode(true);
			}else{  //already marked
				if(1 < numMarkedItems){
					selectedItem.setManualSelected(Boolean.TRUE); 
					answer.setManualMode(true);
				}else if(1 == numMarkedItems){
					answer.setManualMode(false);
				}
			}
		}else{
			// "no answer" was specified 
			boolean hasNoAnswer = true;
			for(int i=0; i < markAreaAnswerItemArray.length; i++){
				MarkAreaAnswerItem item = markAreaAnswerItemArray[i];
				if(item.isManualSelected() || ! isMarked(markRecogThreshold, item)){
					hasNoAnswer = false;
					item.setManualSelected(false);
				}else{
					item.setManualSelected(null);
				}
			}
			if(hasNoAnswer){
				answer.setManualMode(false);
			}else{
				answer.setManualMode(true);
			}
		}
	}

	private void setTextAreaValue(Row row, int columnIndex, String value) {
		TextAreaAnswer answer = (TextAreaAnswer)row.getAnswer(columnIndex);
		if(answer == null){
			answer = new TextAreaAnswer();
		}
		answer.setValue(value);
	}

	private void writeBackJavaScript(HttpServletResponse res,
			String key, FormArea defaultFormArea) throws IOException {
		res.setContentType("text/javascript");
		PrintWriter w = res.getWriter();
		if(defaultFormArea.isMarkArea()){
			w.print("$('"+key+"').parentNode.parentNode.className='hasChanged';");
		}else if(defaultFormArea.isTextArea()){
			w.print("$('"+key+"').parentNode.className='hasChanged';");
		}
		w.flush();
	}
	
	boolean isMSIE(HttpServletRequest req){
		return HttpUtil.isMSIE(req);
	}
}
