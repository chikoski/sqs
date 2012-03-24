package net.sqs2.omr.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.util.RelationList;
import net.sqs2.omr.util.RelationList.LinkedRelationList;
import net.sqs2.util.FileResourceID;
import edu.emory.mathcs.backport.java.util.Collections;

public class ContentIndexerImpl implements ContentIndexer {

	private RelationList<FileResourceID, FormMaster> formMasterRegistry = new LinkedRelationList<FileResourceID, FormMaster>();
	private Map<FileResourceID, SourceDirectoryConfiguration> configHandlerRegistry = new HashMap<FileResourceID, SourceDirectoryConfiguration>();
	private RelationList<FormMaster, SourceDirectory> formMasterSourceDirectoryRelation = new RelationList.TreeRelationList<FormMaster, SourceDirectory>();
	private Map<PageID, Integer> pageIDToRowIndexMap = new HashMap<PageID, Integer>();
	
	public void sortSourceDirectoryList(){
		for(FormMaster master: formMasterSourceDirectoryRelation.getKeySetA()){
			Collections.sort(formMasterSourceDirectoryRelation.getValueListB(master));
		}
	}
	
	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#close()
	 */
	public void clear(){
		this.formMasterRegistry.clear();
		this.configHandlerRegistry.clear();
		this.formMasterRegistry.clear();
		this.pageIDToRowIndexMap.clear();
	}


	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#getConfigHandler(net.sqs2.util.FileResourceID)
	 */
	public SourceDirectoryConfiguration getConfigHandler(FileResourceID fileResourceID) {
		SourceDirectoryConfiguration sourceDirectoryConfiguration = this.configHandlerRegistry.get(fileResourceID);
		if(sourceDirectoryConfiguration == null){
			throw new IllegalArgumentException(fileResourceID.toString());
		}else{
			return sourceDirectoryConfiguration;
		}
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#setConfigHandler(net.sqs2.util.FileResourceID, net.sqs2.omr.session.config.SourceDirectoryConfiguration)
	 */
	public void setConfiguration(FileResourceID fileResourceID, SourceDirectoryConfiguration configHandler) {
		this.configHandlerRegistry.put(fileResourceID, configHandler);
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#addSourceDirectory(net.sqs2.omr.master.PageMaster, net.sqs2.omr.page.source.SourceDirectory)
	 */
	public void addToSourceDirectoryFlattenList(FormMaster formMaster, SourceDirectory sourceDirectory) {
		this.formMasterSourceDirectoryRelation.put(formMaster, sourceDirectory);
	}
	
	public SourceDirectory getSourceDirectoryRoot(FormMaster master){
		return this.getFlattenSourceDirectoryList(master).get(0);
	}
	

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#getSourceDirectoryList(net.sqs2.omr.master.PageMaster)
	 */
	public List<SourceDirectory> getFlattenSourceDirectoryList(FormMaster formMaster) {
		return this.formMasterSourceDirectoryRelation.getValueListB(formMaster);
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#putPageMaster(net.sqs2.util.FileResourceID, net.sqs2.omr.master.PageMaster)
	 */
	public void putFormMaster(FileResourceID fileResourceID, FormMaster master) {
		if (!this.formMasterRegistry.containsKey(fileResourceID)) {
			this.formMasterRegistry.put(fileResourceID, master);
		}
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#getPageMasterList()
	 */
	public List<FormMaster> getFormMasterList() {
		return this.formMasterRegistry.getUniqueValueList();
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#getPageMaster(net.sqs2.util.FileResourceID)
	 */
	public FormMaster getFormMaster(FileResourceID fileResourceID) {
		return this.formMasterRegistry.getFirstValue(fileResourceID);
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#putRowID(net.sqs2.omr.page.source.PageID, net.sqs2.omr.model.RowID)
	 */
	public void putRowIndex(PageID pageID, int rowIndex) {
		this.pageIDToRowIndexMap.put(pageID, rowIndex);
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#removeRowID(net.sqs2.omr.page.source.PageID)
	 */
	public void removeRowID(PageID pageID) {
		this.pageIDToRowIndexMap.remove(pageID);
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentIndexer#getRowID(net.sqs2.omr.page.source.PageID)
	 */
	public Integer getRowIndex(PageID pageID) {
		return this.pageIDToRowIndexMap.get(pageID);
	}

	/*
	class SpreadSheetListMapFactory {
		public Map<PageMaster, List<SourceDirectory>> create() {
			Map<PageMaster, List<SourceDirectory>> sourceDirectoryListList = new HashMap<PageMaster, List<SourceDirectory>>();
			for (PageMaster master : SessionSourceContentIndexerImpl.this.pageMasterRegistry.getKeySetB()) {
				ArrayList<SourceDirectory> flattenSourceDirectoryList = new ArrayList<SourceDirectory>();
				createTableGroupListByMaster(master, flattenSourceDirectoryList);
				sourceDirectoryListList.put(master, flattenSourceDirectoryList);
			}
			return sourceDirectoryListList;
		}

		private void createTableGroupListByMaster(PageMaster master, ArrayList<SourceDirectory> flattenSourceDirectoryList) {
			int maxDepth = -1;
			Map<Integer, List<SourceDirectory>> map = new HashMap<Integer, List<SourceDirectory>>();
			for (SourceDirectory sourceDirectory : getSourceDirectoryList(master)) {
				int depth = (sourceDirectory.getRelativePath().equals("")) ? 1 : StringUtil.split(
						sourceDirectory.getRelativePath(), File.separatorChar).size() + 1;
				maxDepth = Math.max(depth, maxDepth);
				List<SourceDirectory> list = map.get(depth);
				if (list == null) {
					list = new ArrayList<SourceDirectory>();
					map.put(depth, list);
				}
				list.add(sourceDirectory);
			}
			createTableGroupListByMaster(map, maxDepth, flattenSourceDirectoryList);
		}

		private void createTableGroupListByMaster(Map<Integer, List<SourceDirectory>> map, int maxDepth, List<SourceDirectory> flattenSourceDirectoryList) {
			int indexBase = 0;
			for (int i = 0; i <= maxDepth; i++) {
				List<SourceDirectory> sourceDirectoryList = map.get(i);
				if (sourceDirectoryList != null) {
					for (SourceDirectory sourceDirectory : sourceDirectoryList) {
						flattenSourceDirectoryList.add(sourceDirectory);
					}
					indexBase += sourceDirectoryList.size();
				}
			}
		}
	}*/

	@Override
	public int getNumSourceDirectoriesInFlattenList(FormMaster formMaster) {
		return formMasterSourceDirectoryRelation.getValueListB(formMaster).size();
	}

	@Override
	public SourceDirectory getSourceDirectoryFromFlattenList(FormMaster formMaster, int flattenIndex) {
		return formMasterSourceDirectoryRelation.getValueListB(formMaster).get(flattenIndex);
	}
}
