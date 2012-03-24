package net.sqs2.omr.result.tree;

import java.io.PrintWriter;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;
import net.sqs2.omr.result.model.RowItem;
import net.sqs2.omr.result.tree.PathInfoParser.RowListPathItem;
import net.sqs2.util.FileUtil;

public class RowListConnectorServlet extends ConsoleConnectorServlet {

	@Override
	protected void printPathTreeItems(PrintWriter w, long sessionID, String dir) {
		RowListPathItem pathItem = new RowListPathInfoParser(dir).parse();
		printRowTreeItems(w, sessionID, dir, pathItem);
	}

	private void printRowTreeItems(final PrintWriter w, long sessionID, final String rootPath, RowListPathItem pathItem) {
		for(SourceDirectory sourceDirectory:pathItem.getSourceDirectoryList()){
			//FIXME: foreach spreadsheet
			SpreadSheet spreadSheet = new SpreadSheet(sessionID, sourceDirectory.getDefaultFormMaster(), sourceDirectory);
			printRowTreeItems(w, sessionID, rootPath, spreadSheet);	
		}
	}
	
	private void printRowTreeItems(final PrintWriter w, final long sessionID, final String rootPath, final SpreadSheet spreadSheet) {
		final FormMaster formMaster = spreadSheet.getFormMaster();
		final List<PageID> pageIDList = spreadSheet.getPageIDList();
		final int numPages = formMaster.getNumPages();
		final int numRows = pageIDList.size() / numPages;
		
		for(int rowIndex = 0; rowIndex < numRows; rowIndex++ ){
			printListItems(w, rootPath, new ModelToPathItemFactory<List<PageID>>(){
				public ListItem create(int rowIndex, List<PageID> pageIDList){
					RowItem rowItem = new RowItem(pageIDList, numPages, spreadSheet.getSourceDirectory().getRelativePath(), rowIndex);
					if(1 == numPages){
						String name = "["+(rowIndex+1)+"] "+FileUtil.getBasename(pageIDList.get(rowIndex).getFileResourceID().getRelativePath());
						return new ListItem(name, 
									spreadSheet.getSourceDirectory().getRelativePath()+"/"+rowIndex,
									"folder",
									ListItem.State.COLLAPSED_BRANCH,
									rowItem);
					}if(1 < numPages ){
						String start = FileUtil.getBasename(pageIDList.get(rowIndex*numPages).getFileResourceID().getRelativePath());
						String end = FileUtil.getBasename(pageIDList.get((rowIndex+1)*numPages-1).getFileResourceID().getRelativePath());;
						String name = "["+(rowIndex+1)+"] "+start+"-"+end;
						return new ListItem(name, 
									spreadSheet.getSourceDirectory().getRelativePath()+"/"+rowIndex,
									"folder",
									ListItem.State.COLLAPSED_BRANCH,
									rowItem);
					}else{
						throw new IllegalArgumentException("numPages is invalid:"+numPages);
					}
				}				
			}.create(rowIndex, pageIDList));
		}
	}

}
