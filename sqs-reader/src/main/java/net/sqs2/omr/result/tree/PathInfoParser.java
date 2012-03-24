package net.sqs2.omr.result.tree;

import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceDirectory;

public class PathInfoParser {
	
	public static class SessionPathItem{
		private String pathInfo;
		private long sessionID;
		public SessionPathItem(String pathInfo, long sessionID){
			this.pathInfo = pathInfo;
			this.sessionID = sessionID;
		}
		
		public String getPathInfo(){
			return this.pathInfo;
		}
		
		public long getSessionID(){
			return sessionID;
		}
	}
	
	public static class FormMasterListPathItem extends SessionPathItem{
		public FormMasterListPathItem(String pathInfo, long sessionID){
			super(pathInfo, sessionID);
		}
	}
	
	public static class FormMasterPathItem extends FormMasterListPathItem{
		FormMaster formMaster;
		
		FormMasterPathItem(String pathInfo, long sessionID, FormMaster formMaster){
			super(pathInfo, sessionID);
			this.formMaster = formMaster;
		}
		
		public FormMaster getFormMaster(){
			return formMaster;
		}
	}

	public static class SourceDirectoryListPathItem extends FormMasterPathItem{
		private SourceDirectory sourceDirectory;
		SourceDirectoryListPathItem(String pathInfo, long sessionID, FormMaster formMaster){
			super(pathInfo, sessionID, formMaster);
		}
		SourceDirectoryListPathItem(String pathInfo, long sessionID, SourceDirectory sourceDirectory){
			super(pathInfo, sessionID, null);
			this.sourceDirectory = sourceDirectory;
		}
		public SourceDirectory getSourceDirectory() {
			return sourceDirectory;
		}
	}

	public static class RowListPathItem extends FormMasterPathItem{
		List<SourceDirectory> sourceDirectoryList;
		RowListPathItem(String pathInfo, long sessionID, FormMaster formMaster, List<SourceDirectory> sourceDirectoryList){
			super(pathInfo, sessionID, formMaster);
			this.sourceDirectoryList = sourceDirectoryList;
		}
		public List<SourceDirectory> getSourceDirectoryList(){
			return this.sourceDirectoryList;
		}
	}

	public static class RowPathItem extends SourceDirectoryListPathItem{
		private int rowIndex; 
		RowPathItem(String pathInfo, long sessionID, SourceDirectory sourceDirectory, int rowIndex){
			super(pathInfo, sessionID, sourceDirectory);
			this.rowIndex = rowIndex;
		}
		
		public int getRowIndex(){
			return this.rowIndex;
		}
	}
	
	
	public static class PageIDListPathItem extends RowPathItem{
		private List<PageID> pageIDList;
		PageIDListPathItem(String pathInfo, long sessionID, SourceDirectory sourceDirectory, int rowIndex, List<PageID> pageIDList){
			super(pathInfo, sessionID, sourceDirectory, rowIndex);
			this.pageIDList = pageIDList;
		}

		public List<PageID> getPageIDList() {
			return pageIDList;
		}
	}
	
	public static class PageIDPathItem extends RowPathItem{
		private int imageIndex;
		private PageID pageID;
		PageIDPathItem(String pathInfo, long sessionID, SourceDirectory sourceDirectory, int rowIndex, int imageIndex, PageID pageID){
			super(pathInfo, sessionID, sourceDirectory, rowIndex);
			this.imageIndex = imageIndex;
			this.pageID = pageID;
		}

		public PageID getPageID() {
			return pageID;
		}
		
		public int getImageIndex(){
			return imageIndex;
		}
	}

	public static class QuestionPathItem extends RowPathItem{
		private int questionIndex;

		QuestionPathItem(String pathInfo, long sessionID, SourceDirectory sourceDirectory, int rowIndex, int questionIndex){
			super(pathInfo, sessionID, sourceDirectory, rowIndex);
			this.questionIndex = questionIndex;
		}

		public int getQuestinIndex(){
			return questionIndex;
		}
	}

	public static class QuestionItemPathItem extends QuestionPathItem{
		private int questionItemIndex;

		QuestionItemPathItem(String pathInfo, long sessionID, SourceDirectory sourceDirectory, int rowIndex, int questionIndex, int questionItemIndex){
			super(pathInfo, sessionID, sourceDirectory, rowIndex, questionIndex);
			this.questionItemIndex = questionItemIndex;
		}

		public int getQuestinItemIndex(){
			return questionItemIndex;
		}
	}

	
	/*
	public FormMasterListPathItem parse(String pathInfo){
		String[] pathInfoArray = null;
		int paramDepth = 0;
		pathInfoArray = pathInfo.substring(1).split("/");
		paramDepth = pathInfoArray.length;

		if(0 == paramDepth){
			new IllegalArgumentException(pathInfo);	
		}
		
		String command = pathInfoArray[0];
		if(1 < paramDepth){
			long sessionID = URLSafeRLEBase64.decodeToLong(pathInfoArray[1]);
			SessionSource sessionSource = SessionSources.get(sessionID);
			
			if("m".equals(command)){
				return new FormMasterListPathItem(pathInfo, sessionID);
			}
			if(2 < paramDepth){				
				int formMasterIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[2]);
				List<FormMaster> formMasterList = sessionSource.getContentIndexer().getFormMasterList(); 
				FormMaster formMaster = formMasterList.get(formMasterIndex);
				if("p".equals(command)){
					if(3 == paramDepth){
						return new SourceDirectoryRootListPathItem(pathInfo, sessionID, formMaster);
					}else{
						SourceDirectory masterLocalSourceDirectoryRoot = sessionSource.getContentIndexer().getSourceDirectoryRoot(formMaster);
						return parse(pathInfo, pathInfoArray, 2, sessionID, masterLocalSourceDirectoryRoot);
					}
				}else if("r".equals(command)){
					
				}else if("q".equals(command)){
					
				}				
			}
			
		}
		throw new IllegalArgumentException(pathInfo);
	}
	

	public static SessionSelection parseAnswer(String pathInfo){
		String[] pathInfoArray = null;
		int paramDepth = 0;
		pathInfoArray = pathInfo.split("/");
		paramDepth = pathInfoArray.length;
		if(0 < paramDepth){
			long sessionID = URLSafeRLEBase64.decodeToLong(pathInfoArray[0]);
			if(1 < paramDepth){
				int masterIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[1]);
				if(2 < paramDepth){
					Selection spreadSheetEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[2]);
					if(3 < paramDepth){
						Selection rowEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[3]);
						if(4 < paramDepth){
							Selection questionEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[4]);
							if(5 < paramDepth){
								Selection itemEntry = URLSafeRLEBase64.decodeToSelection(pathInfoArray[5]);
								if(questionEntry instanceof MultiSelection){
									if(spreadSheetEntry instanceof MultiSelection &&
											rowEntry instanceof MultiSelection &&
											questionEntry instanceof MultiSelection &&
											itemEntry instanceof SingleSelection){
										int pageIndex = ((URLSafeRLEBase64.SingleSelection)itemEntry).getValue();
										return new MultiRowMultiQuestionPageSelection(
												new MultiRowMultiQuestionPagePath(sessionID, masterIndex, 
												((MultiSelection)spreadSheetEntry),
												((MultiSelection)rowEntry), 
												((MultiSelection)questionEntry), 
												pageIndex));
									}else if(itemEntry instanceof MultiSelection){
										throw new IllegalArgumentException(pathInfo);
									}
								}else if(spreadSheetEntry instanceof SingleSelection && 
										rowEntry instanceof SingleSelection && 
										questionEntry instanceof SingleSelection){
									if(itemEntry instanceof SingleSelection){
										return new AnswerItemSelection(new AnswerItemPath(sessionID, masterIndex, 
												((SingleSelection)spreadSheetEntry).getValue(),
												((SingleSelection)rowEntry).getValue(), 
												((SingleSelection)questionEntry).getValue(),
												((SingleSelection)itemEntry).getValue()));
									}else{
										throw new IllegalArgumentException(pathInfo);
									}
								}else{
									throw new IllegalArgumentException(pathInfo);
								}
							}
							
							if(spreadSheetEntry instanceof MultiSelection &&
									rowEntry instanceof MultiSelection){
								if(questionEntry instanceof MultiSelection){
									return new MultiRowMultiQuestionSelection(new MultiRowMultiQuestionPath(sessionID, masterIndex, 
											(MultiSelection)spreadSheetEntry,
											(MultiSelection)rowEntry,
											(MultiSelection)questionEntry)); 
								}else if(questionEntry instanceof SingleSelection){
									return new MultiRowSingleQuestionSelection(new MultiRowSingleQuestionPath(sessionID, masterIndex, 
											(MultiSelection)spreadSheetEntry,
											(MultiSelection)rowEntry,
											((SingleSelection)questionEntry).getValue())); 
								}else{
									throw new IllegalArgumentException(pathInfo);
								}
							}else if(spreadSheetEntry instanceof SingleSelection &&
									rowEntry instanceof SingleSelection &&
									questionEntry instanceof SingleSelection){
								return new SingleRowSingleQuestionSelection(new SingleRowSingleQuestionPath(sessionID, masterIndex, 
										((SingleSelection)spreadSheetEntry).getValue(),
										((SingleSelection)rowEntry).getValue(), 
										((SingleSelection)questionEntry).getValue()));
							}else{
								throw new IllegalArgumentException(pathInfo);
							}
						}	
						if(spreadSheetEntry instanceof MultiSelection &&
								rowEntry instanceof MultiSelection){
							return new MultiRowSelection(new MultiRowPath(sessionID, masterIndex, 
									(MultiSelection)spreadSheetEntry,
									(MultiSelection)rowEntry)); 
						}else{
							throw new IllegalArgumentException(pathInfo);
						}
					}
					if(spreadSheetEntry instanceof MultiSelection){
						return new MultiSpreadSheetSelection(new MultiSpreadSheetPath(sessionID, masterIndex, 
								((MultiSelection)spreadSheetEntry)));
					}else if(spreadSheetEntry instanceof SingleSelection){
						return new SingleSpreadSheetSelection(new SingleSpreadSheetPath(sessionID, masterIndex, 
								((SingleSelection)spreadSheetEntry).getValue()));
					}
				}
				return new SingleMasterSelection(new SingleMasterPath(sessionID, masterIndex));
			}
		}
		throw new IllegalArgumentException(pathInfo);
	}
	*/
}
