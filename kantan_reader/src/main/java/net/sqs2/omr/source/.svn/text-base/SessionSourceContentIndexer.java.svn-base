package net.sqs2.omr.source;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.util.RelationList;
import net.sqs2.omr.util.RelationList.SimpleRelationList;
import net.sqs2.util.FileResourceID;
import net.sqs2.util.StringUtil;

public class SessionSourceContentIndexer{

	SessionSourceContentIndexer(){
	}
	
	/*
	class ResourceMap<K,V>{
		Map<K,V> map = new HashMap<K,V>();
		
		public void regist(K key, V value){
			map.put(key, value);
		}
		
		public V lookup(K key){
			return map.get(key);
		}
	}
	
	class ResourceRegistry<K,V> extends ResourceMap<K,V>{
		TreeSet<V> set = new TreeSet<V>();
		//List<V> list = new ArrayList<V>();
		
		Map<K,List<V>> listMap = new HashMap<K,List<V>>();
		
		public List<V> getValueList(K k){
			return listMap.get(k);
		}
		
		public Set<V> getValueSet(){
			return set;
		}
		
		public TreeSet<V> getValueList(){
			return set.;
		}
		
		public void regist(K key, V value){
			super.regist(key, value);
			List<V> list = listMap.get(key);
			if(list == null){
				list = new ArrayList<V>();
				listMap.put(key, list);
			}
			list.add(value);
			set.add(value);
		}
		
		public V lookup(K key, int index){
			List<V> list = listMap.get(key);
			if(list != null){
				return list.get(index);
			}else{
				return null;
			}
		}
	}*/

	private RelationList<FileResourceID,PageMaster> pageMasterRegistry = new SimpleRelationList<FileResourceID,PageMaster>();
	private RelationList<FileResourceID,SourceDirectoryConfiguration> configHandlerRegistry = new SimpleRelationList<FileResourceID,SourceDirectoryConfiguration>();
	private RelationList<PageMaster,SourceDirectory> pageMasterSourceDirectoryRelation = new RelationList.SortedRelationList<PageMaster,SourceDirectory>(); 
	private List<SpreadSheet> spreadSheetList = new ArrayList<SpreadSheet>(); 
	private Map<PageID, RowID> pageIDToRowIDMap = new HashMap<PageID, RowID>();

	private Map<PageMaster, List<SourceDirectory>> depthOrderedListMap = new HashMap<PageMaster, List<SourceDirectory>>();
	private Map<PageMaster, List<SourceDirectory>> sourceDirectoryListMap =new HashMap<PageMaster,List<SourceDirectory>>();
	
	public void addSpreadSheet(SpreadSheet spreadSheet){
		this.spreadSheetList.add(spreadSheet);
	}
	
	public List<SpreadSheet> getSpreadSheetList(){
		return this.spreadSheetList;
	}
	
	public SourceDirectoryConfiguration getConfigHandler(FileResourceID fileResourceID){
		return this.configHandlerRegistry.getValueListB(fileResourceID).get(0);
	}
	
	public void setConfigHandler(FileResourceID fileResourceID, SourceDirectoryConfiguration configHandler){
		this.configHandlerRegistry.put(fileResourceID, configHandler);
	}
	
	public void addSourceDirectory(PageMaster pageMaster, SourceDirectory sourceDirectory){
		this.pageMasterSourceDirectoryRelation.put(pageMaster, sourceDirectory);
	}

	public List<SourceDirectory> getSourceDirectoryList(PageMaster pageMaster){
		return this.pageMasterSourceDirectoryRelation.getValueListB(pageMaster);
	}

	public void putPageMaster(FileResourceID fileResourceID, PageMaster master){
		if(! this.pageMasterRegistry.containsKey(fileResourceID)){
			this.pageMasterRegistry.put(fileResourceID, master);
		}
	}
	
	public List<PageMaster> getPageMasterList(){
		return this.pageMasterRegistry.getUniqueValueList();
	}

	/*
	public Set<PageMaster> getPageMasterSet(){
		return this.pageMasterRegistry.getValueSet();
	}*/
	
	public PageMaster getPageMaster(FileResourceID fileResourceID){
		return this.pageMasterRegistry.getFirstValue(fileResourceID);
	}
	
	public void putRowID(PageID pageID, RowID rowID){
		this.pageIDToRowIDMap.put(pageID, rowID);
	}

	public void removeRowID(PageID pageID){
		this.pageIDToRowIDMap.remove(pageID);
	}

	public RowID getRowID(PageID pageID){
		return this.pageIDToRowIDMap.get(pageID);
	}

	
	public Map<PageMaster, List<SourceDirectory>> getSourceDirectoryDepthOrderedListMap(){
		if(this.depthOrderedListMap == null){
			this.depthOrderedListMap = new SpreadSheetListMapFactory().create(); 			
		}
		return this.depthOrderedListMap;
	}
	
	class SpreadSheetListMapFactory{
		
		public Map<PageMaster, List<SourceDirectory>> create(){
			Map<PageMaster, List<SourceDirectory>> sourceDirectoryListList = new HashMap<PageMaster, List<SourceDirectory>>();
			for(PageMaster master: pageMasterRegistry.getKeySetB()){
				ArrayList<SourceDirectory> flattenSourceDirectoryList = new ArrayList<SourceDirectory>();			
				createTableGroupListByMaster(master, flattenSourceDirectoryList);
				sourceDirectoryListList.put(master, flattenSourceDirectoryList);
			}
			return sourceDirectoryListList;
		}
		
		private void createTableGroupListByMaster(PageMaster master, ArrayList<SourceDirectory> flattenSourceDirectoryList) {
			int maxDepth = -1;
			Map<Integer, List<SourceDirectory>> map = new HashMap<Integer, List<SourceDirectory>>();			
			for(SourceDirectory sourceDirectory: getSourceDirectoryList(master)){
				int depth = (sourceDirectory.getPath().equals(""))? 1 : StringUtil.split(sourceDirectory.getPath(), File.separatorChar).size() + 1;
				maxDepth = Math.max(depth, maxDepth);
				List<SourceDirectory> list = map.get(depth);
				if(list == null){
					list = new ArrayList<SourceDirectory>();
					map.put(depth, list);
				}
				list.add(sourceDirectory);
			}
			createTableGroupListByMaster(map, maxDepth, flattenSourceDirectoryList);
		}
		
		private void createTableGroupListByMaster(Map<Integer, List<SourceDirectory>> map, int maxDepth, List<SourceDirectory> flattenSourceDirectoryList) {
			int indexBase = 0;
			for(int i = 0; i <= maxDepth; i++){
				List<SourceDirectory> sourceDirectoryList = map.get(i);
				if(sourceDirectoryList != null){
					for(SourceDirectory sourceDirectory: sourceDirectoryList){
						flattenSourceDirectoryList.add(sourceDirectory);
					}
					indexBase += sourceDirectoryList.size();
				}
			}
		}
	}

	public List<SourceDirectory> getSourceDirectoryList(FormMaster master){
		return this.pageMasterSourceDirectoryRelation.getValueListB(master);
	}

	/*
	
	public Iterator<PageMasterGroup> getPageMasterGroupIterator(){
		return this.pageMasterGroupSet.iterator();
	}

	public void putPageMaster(int masterIndex, PageMaster master){
		// this.pageMasterList.set(masterIndex, master);
	}

	public void addPageMasterGroup(PageMasterGroup pageMasterGroup){
		this.pageMasterGroupSet.add(pageMasterGroup);
	}
	
	public int getNumRemainsPageTasks(){
		return this.rowIndexMap.size();
	}

	public PageMaster getPageMaster(String relativePath){
		return this.pageMasterMap.get(relativePath);
	}
	
	public void addSourceDirectory(PageMaster master, SourceDirectory sourceDirectory){
		getSourceDirectoryList(master).add(sourceDirectory);
	}

	public ArrayList<SourceDirectory> getSourceDirectoryList(FormMaster master){
		ArrayList<SourceDirectory> ret = this.sourceDirectoryListMap.get(master);
		if(ret == null){
			ret = new ArrayList<SourceDirectory>();
			this.sourceDirectoryListMap.put(master, ret);
		}
		return ret; 
	}
	
	public ArrayList<SourceDirectory> getSourceDirectoryList(PageMaster master){
		ArrayList<SourceDirectory> ret = this.sourceDirectoryListMap.get(master);
		if(ret == null){
			ret = new ArrayList<SourceDirectory>();
			this.sourceDirectoryListMap.put(master, ret);
		}
		return ret; 
	}
	
	public ArrayList<SpreadSheet> getSpreadSheetList(PageMaster master){
		ArrayList<SourceDirectory> ret = this.sourceDirectoryListMap.get(master);
		if(ret == null){
			ret = new ArrayList<SourceDirectory>();
			this.sourceDirectoryListMap.put(master, ret);
		}
		return ret; 
	}
	
	public PageMaster getPageMaster(int masterIndex){
		return this.pageMasterList.get(masterIndex);
	}
	
	public Map<PageMaster, List<SpreadSheet>> getSpreadSheetDepthOrderedListMap(){
		if(this.spreadSheetListMap == null){
			this.spreadSheetListMap = SpreadSheetListMapFactory.create(); 			
		}
		return this.spreadSheetListMap;
	}

	public SpreadSheetGroup getSpreadSheetGroup(FormMaster master){
		//TODO
		throw new RuntimeException("NOT IMPLEMENTED YET");
	}
	

	public static SessionSource getInstance(long sessionID){
		return singletonMap.get(sessionID);
	}
*/

}
