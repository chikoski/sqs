/*

 TaskConsumerRowGenerator.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2007/01/11

 */
package net.sqs2.omr.session.service;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.AbstractPageTask;
import net.sqs2.omr.model.Answer;
import net.sqs2.omr.model.ContentAccessor;
import net.sqs2.omr.model.ContentIndexer;
import net.sqs2.omr.model.FormAreaResult;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.RowAccessor;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSources;
import net.sqs2.omr.model.TextAreaAnswer;
import net.sqs2.util.FileResourceID;

public class PageTaskCommitRowGenerateService extends AbstractPageTaskCommitService {

	private PageTaskAccessor taskAccessor;

	public PageTaskCommitRowGenerateService(MarkReaderSession session) throws IOException {
		super(session);
	}

	public void setup(File sourceDirectoryRoot) throws IOException {
		SessionSource sessionSource = markReaderSession.getSessionSource();
		ContentAccessor sessionSourceAccessor = sessionSource.getContentAccessor();
		this.taskAccessor = sessionSourceAccessor.getPageTaskAccessor(); 
	}

	@Override
	public void commit(OMRPageTask task) throws RemoteException {
		this.taskAccessor.put(task);
		try{
			setupRowAnswersFromTask(task);
		}catch(IOException ex){
			throw new RemoteException(ex.getLocalizedMessage());
		}finally{
			this.taskAccessor.flush();
		}
	}

	private void setupRowAnswersFromTask(OMRPageTask task) throws IOException{
		SessionSource sessionSource = SessionSources.getInitializedInstance(task.getSessionID());
		ContentAccessor sessionSourceAccessor = sessionSource.getContentAccessor();
		ContentIndexer sessionSourceIndexer = sessionSource.getContentIndexer();
		FormMaster master = null;
		PageTaskResult pageTaskResult = task.getPageTaskResult();
		if(pageTaskResult == null){
			throw new RuntimeException("pageTaskResult is null:"+task);
		}
		FileResourceID masterFileResourceID = pageTaskResult.getMasterFileResourceID();
		if(masterFileResourceID != null){
			master = (FormMaster) sessionSourceIndexer.getFormMaster(masterFileResourceID);
		}else{
			master = (FormMaster) sessionSourceIndexer.getFormMaster(task.getDefaultPageMasterFileResourceID());
		}
		File pageIDPath = new File(task.getPageID().getFileResourceID().getRelativePath());
		String parentPath = pageIDPath.getParent();
		if (parentPath == null) {
			parentPath = "";
		}
		
		RowAccessor rowAccessor = sessionSourceAccessor.getRowAccessor();
		int rowIndex = sessionSource.getContentIndexer().getRowIndex(task.getPageID());
		Row row = getRow(rowAccessor, task, parentPath, rowIndex, master);
		if(row.getNumAnswers() != master.getNumQuestions()){
			row = new Row(master.getRelativePath(), task.getSourceDirectoryRootPath(), rowIndex, master.getNumQuestions());
		}
		setupRow(row, task, rowAccessor, master, rowIndex, pageIDPath);
		rowAccessor.put(row);
		rowAccessor.flush();
	}

	private Row getRow(RowAccessor rowAccessor, AbstractPageTask task, String parentPath, int rowIndex, FormMaster master) {
		Row row = (Row) rowAccessor.get(master.getRelativePath(), parentPath, rowIndex);
		if (row == null) {
			row = createRow(task, parentPath, rowIndex, master);
		}
		return row;
	}

	private Row createRow(AbstractPageTask task, String parentPath, int rowIndex, FormMaster master) {
		return new Row(master.getRelativePath(), 
				parentPath, 
				rowIndex, 
				master.getNumQuestions());
	}

	private void setupRow(Row row, OMRPageTask task, RowAccessor rowAccessor, FormMaster master, int rowIndex, File pageIDPath) {
		PageTaskResult taskResult = task.getPageTaskResult();
		if (taskResult == null) {
			row.addException(task.getPageTaskException());
			return;
		} else {
			row.clearTaskException();
		}

		List<PageAreaResult> pageAreaResultList = taskResult.getPageAreaResultList();
		List<FormArea> formAreaList = master.getFormAreaListByPageIndex(task.getProcessingPageNumber() - 1);

		for (FormArea formArea : formAreaList) {
			FormAreaResult formAreaResult = (FormAreaResult) pageAreaResultList.get(formArea.getAreaIndexInPage());
			int itemIndex = formArea.getItemIndex();
			int questionIndex = formArea.getQuestionIndex();
			Answer answer = row.getAnswer(questionIndex);
			if (formArea.isMarkArea()) {
				if (answer == null) {
					int numItems = master.getFormAreaList(questionIndex).size();
					answer = new MarkAreaAnswer(formArea.isSelectMultiple(), numItems);
					row.setAnswer(questionIndex, answer);
				}
				float density = formAreaResult.getDensity();
				MarkAreaAnswerItem answerItem = new MarkAreaAnswerItem(itemIndex, density);
				((MarkAreaAnswer) answer).setMarkAreaAnswerItem(answerItem);
			} else if (formArea.isTextArea()) {
				row.setAnswer(questionIndex, new TextAreaAnswer());
			}
		}
	}
}
