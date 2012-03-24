package net.sqs2.omr.result.contents;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.AbstractTask;
import net.sqs2.omr.task.PageAreaCommand;
import net.sqs2.omr.task.TaskAccessor;

public class ContentsFactoryUtil {
	
	public static List<PageAreaCommand> createPageAreaCommandListParRow(FormMaster master, SourceDirectory sourceDirectory,
			TaskAccessor pageTaskAccessor, int rowIndex) {
		ArrayList<PageAreaCommand> pageAreaCommandList = new ArrayList<PageAreaCommand>();

		for(int pageIndex = 0; pageIndex < master.getNumPages(); pageIndex++){
			List<FormArea> formAreaList = master.getFormAreaListByPageIndex(pageIndex);
			PageID pageID = sourceDirectory.getPageID(rowIndex * master.getNumPages() + pageIndex);
			AbstractTask pageTask = pageTaskAccessor.get(master, pageIndex + 1, pageID);
			try{
				List<PageAreaCommand> pageAreaCommandListParPage = pageTask.getTaskResult().getPageAreaCommandList();
				for(int formAreaIndex = 0; formAreaIndex < formAreaList.size(); formAreaIndex++){
					PageAreaCommand pageAreaCommand = pageAreaCommandListParPage.get(formAreaIndex);
					pageAreaCommandList.add(pageAreaCommand);
				}
			}catch(NullPointerException ignore){
				// TODO ERROR
			}
		}
		return pageAreaCommandList;
	}

	public static List<PageAreaCommand> createPageAreaCommandListParQuestion(FormMaster master, SourceDirectory sourceDirectory,
			TaskAccessor pageTaskAccessor, int rowIndex, int columnIndex) {
		List<PageAreaCommand> pageAreaCommandParQuestion = new ArrayList<PageAreaCommand>(); 
		List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
		int prevPageIndex = -1;
		List<PageAreaCommand> pageAreaCommandListParPage = null;
		for(FormArea formArea: formAreaList){
			if(prevPageIndex != formArea.getPageIndex()){
				PageID pageID = sourceDirectory.getPageID(rowIndex * master.getNumPages() + formArea.getPageIndex());
				AbstractTask pageTask = pageTaskAccessor.get(master, formArea.getPageIndex() + 1, pageID);
				pageAreaCommandListParPage =  pageTask.getTaskResult().getPageAreaCommandList();
			}
			PageAreaCommand pageAreaCommand = pageAreaCommandListParPage.get(formArea.getAreaIndexInPage());
			pageAreaCommandParQuestion.add(pageAreaCommand);
		}
		return pageAreaCommandParQuestion;
	}

}
