/**
 * 
 */
package net.sqs2.omr.result.contents;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.AbstractTask;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.omr.task.TaskError;

public class ErrorPageContentsJSONFactory extends SimpleContentsFactory{
	protected PrintWriter w;
	boolean hasRowPrinted = false;

	public ErrorPageContentsJSONFactory(PrintWriter w, SessionSource sessionSource)throws IOException{
		super(sessionSource);
		this.w = w;
	}
	
	@Override
	public void create(FormMaster master,
			Set<Integer> selectedTableIndexSet,
			Set<Integer> selectedRowIndexSet,
			Set<Integer> selectedQuestionIndexSet){
		this.w.append("contentsHandler.errorPageSource = [");
		super.create(master,
				selectedTableIndexSet,
				 selectedRowIndexSet,
				 selectedQuestionIndexSet);
		this.w.println("];");
	}
	
	@Override
	void processRow(FormMaster master,
			Set<Integer> selectedQuestionIndexSet,  SourceDirectory sourceDirectory, int selectedTableIndex, int tableIndex, int selectedRowIndex, int rowIndex){

		if(this.hasRowPrinted){
			this.w.print(',');
		}else{
			this.hasRowPrinted = true;
		}

		try{
			TaskAccessor pageTaskAccessor = new TaskAccessor(sessionSource.getRootDirectory());
			int pageIDIndex = 0;
			List<PageID> pageIDList = sourceDirectory.getPageIDList();
			
			this.w.print('[');

			for(int pageIndex = 0; pageIndex < master.getNumPages(); pageIndex++){
				int index = master.getNumPages() * rowIndex + pageIndex;
				//System.err.println(sourceDirectory.getPath()+":"+index);
				PageID pageID = pageIDList.get(index);
				//System.err.println(pageID.getPath());
				AbstractTask pageTask = pageTaskAccessor.get(master, pageIndex+1, pageID);
				//String configHandlerPath = pageTask.getConfigHandlerPath();
				//PageTaskResult pageTaskResult = pageTask.getPageTaskResult();
				TaskError pageTaskError = pageTask.getTaskError();
		
				if(0 < pageIndex){
					this.w.print(',');	
				}
				
				if(pageTaskError != null){
					Logger.getAnonymousLogger().info(pageID+"\t"+pageTaskError.getLocalizedMessage());
					this.w.print('\'');
					//this.w.print(pageID);
					//this.w.print(':');
					this.w.print(pageTaskError.getLocalizedMessage());
					this.w.print('\'');
				}else{
					this.w.print("null");
				}
				
				pageIDIndex++;
			}
			
			this.w.print(']');
			
		}catch(IOException ex){
			ex.printStackTrace();
		}
	}
}
