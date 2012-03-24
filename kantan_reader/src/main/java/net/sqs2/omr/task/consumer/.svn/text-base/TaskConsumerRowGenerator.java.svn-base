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
package net.sqs2.omr.task.consumer;

import java.io.File;
import java.io.IOException;
import java.rmi.RemoteException;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.Answer;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.result.model.RowAccessor;
import net.sqs2.omr.result.model.TextAreaAnswer;
import net.sqs2.omr.session.Session;
import net.sqs2.omr.source.RowID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSourceContentAccessor;
import net.sqs2.omr.source.SessionSourceContentIndexer;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.task.AbstractTask;
import net.sqs2.omr.task.FormAreaCommand;
import net.sqs2.omr.task.PageAreaCommand;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.omr.task.TaskResult;

public class TaskConsumerRowGenerator extends AbstractTaskConsumer{

	private TaskAccessor taskAccessor;

	public TaskConsumerRowGenerator(Session session)throws IOException{
		super(session);
	}
	
	public void setup(File sourceDirectoryRoot)throws IOException{
		this.taskAccessor = new TaskAccessor(sourceDirectoryRoot);
	}
	
	public void consumeTask(PageTask task)throws RemoteException {
		this.taskAccessor.put(task);
    	setupRowAnswersFromTask(task);
		this.taskAccessor.flush();
	}

	public void setupRowAnswersFromTask(PageTask task) {
		SessionSource sessionSource = SessionSources.get(task.getSessionID());
		SessionSourceContentAccessor sessionSourceAccessor = sessionSource.getSessionSourceContentAccessor();
		SessionSourceContentIndexer sessionSourceIndexer = sessionSource.getSessionSourceContentIndexer();

		FormMaster master = (FormMaster)sessionSourceIndexer.getPageMaster(task.getMasterFileResourceID());
		
		File pageIDPath = new File(task.getPageID().getFileResourceID().getRelativePath());
		String parentPath = pageIDPath.getParent();
		if(parentPath == null){
			parentPath = "";
		}

		RowAccessor rowAccessor = sessionSourceAccessor.getRowAccessor();
		RowID rowID = sessionSource.getSessionSourceContentIndexer().getRowID(task.getPageID());
		int rowIndex = rowID.getRowIndex(); 
		Row row = getRow(rowAccessor, task, parentPath, rowIndex, master);
		setupRow(row, task, rowAccessor, master, rowIndex, pageIDPath);
		rowAccessor.put(row);
		rowAccessor.flush();
	}
	
	private Row getRow(RowAccessor rowAccessor, AbstractTask task, String parentPath, int rowIndex, FormMaster master){
		Row row = (Row)rowAccessor.get(task.getMasterFileResourceID().getRelativePath(), parentPath, rowIndex);
		if(row == null){
			row = createRow(task, parentPath, rowIndex, master);
		}
		return row;
	}

	private Row createRow(AbstractTask task, String parentPath, int rowIndex, FormMaster master) {
		return new Row(task.getMasterFileResourceID().getRelativePath(), parentPath, rowIndex, master.getNumColumns());
	}

	private void setupRow(Row row, PageTask task, RowAccessor rowAccessor, FormMaster master, int rowIndex, File pageIDPath) {
		TaskResult taskResult = task.getTaskResult();
		if(taskResult == null){
			row.addError(task.getTaskError());
			return;
		}else{
			row.clearTaskError();
		}
		
		List<PageAreaCommand> pageAreaCommandList = taskResult.getPageAreaCommandList();
		List<FormArea> formAreaList = master.getFormAreaListByPageIndex(task.getPageNumber() - 1);
		
		for(FormArea formArea: formAreaList){
			FormAreaCommand formAreaCommand = (FormAreaCommand)pageAreaCommandList.get(formArea.getAreaIndexInPage());
			int itemIndex = formArea.getItemIndex();
			int columnIndex = formArea.getColumnIndex();
			Answer answer = row.getAnswer(columnIndex);
			if (formArea.isMarkArea()) {
				if(answer == null){
					int numItems = master.getFormAreaList(columnIndex).size();
					answer = new MarkAreaAnswer(numItems);
					row.setAnswer(columnIndex, answer);
				}
				float density = formAreaCommand.getDensity();
				MarkAreaAnswerItem answerItem = new MarkAreaAnswerItem(itemIndex, density);
				((MarkAreaAnswer)answer).setMarkAreaAnswerItem(answerItem);
			}else if(formArea.isTextArea()) {
				 row.setAnswer(columnIndex, new TextAreaAnswer());
			}
		}
	}
}
