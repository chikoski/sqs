/**

 SourceDirectory.java

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
package net.sqs2.omr.source;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sqs2.image.ImageFactory;
import net.sqs2.image.ImageUtil;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.util.NameBasedComparableFile;
import net.sqs2.util.FileResourceID;

public class SourceDirectory implements Serializable{
	
	private  static final long serialVersionUID = 0L;
	
	private File sourceDirectoryRoot;
	private String sourceDirectoryPath;
	private SourceDirectoryConfiguration sourceDirectoryConfiguration;
	private PageMaster pageMaster;
		
	protected List<PageID> pageIDList = null;
	protected List<SourceDirectory> childSourceDirectoryList;
	
	public SourceDirectory(File sourceDirectoryRoot){
		this(sourceDirectoryRoot, "");
	}

	public SourceDirectory(File sourceDirectoryRoot, String sourceDirectoryPath){
		this.sourceDirectoryRoot = sourceDirectoryRoot;
		this.sourceDirectoryPath = sourceDirectoryPath;
	}
	
	@Override
	public String toString(){
		return "SourceDirectory("+this.sourceDirectoryPath+")";
	}

	public boolean isRoot(){
		return this.sourceDirectoryPath.equals("");
	}
	
	public boolean isLeaf(){
		return this.childSourceDirectoryList == null || this.childSourceDirectoryList.size() == 0;
	}

	public File getRoot(){
		return this.sourceDirectoryRoot;
	}

	public File getDirectory(){
		return new File(this.sourceDirectoryRoot.getAbsolutePath()+File.separator+this.sourceDirectoryPath);
	}

	public String getPath(){
		return sourceDirectoryPath;
	}

	public PageID getPageID(int index){
		return getPageIDList().get(index);
	}
	
	public List<PageID> getPageIDList(){
		if(this.pageIDList == null){
			this.pageIDList = createPageIDList();
		}
		return this.pageIDList;
	}
	
	/*
	public List<PageID> getAllPageIDList(){
		if(this.allPageIDList == null){
			this.allPageIDList = createAllPageIDList();
		}
		return this.allPageIDList;
	}
	
	public List<PageID> createAllPageIDList(){
		List<PageID> allPageIDList = new ArrayList<PageID>();
		if(this.pageIDList != null){
			allPageIDList.addAll(this.pageIDList);
		}
		if(getChildSourceDirectoryList() != null){
			for(SourceDirectory childSourceDirectory: getChildSourceDirectoryList()){
				allPageIDList.addAll(childSourceDirectory.getAllPageIDList());
			}
		}
		return allPageIDList;
	}
	*/
	
	public int getNumPageIDs(){
		return getPageIDList().size();
	}

	public SourceDirectoryConfiguration getConfiguration(){
		return this.sourceDirectoryConfiguration;
	}

	public PageMaster getPageMaster(){
		return this.pageMaster;
	}

	public void close(){
		this.pageIDList.clear();
		this.pageIDList = null;
	}

	public PageTask createTask(int pageNumber, PageID pageID, long sessionID){
		try{
			return new PageTask(this.sourceDirectoryRoot.getAbsolutePath(),
					this.pageMaster.getFileResourceID(),
					this.sourceDirectoryConfiguration.getFileResourceID(),
					pageNumber, 
					pageID,
					sessionID);
		}catch(Exception ex){
			ex.printStackTrace();
			return null;
		}
	}

	public SourceDirectory getChildSourceDirectory(int index){
		return this.childSourceDirectoryList.get(index);
	}
	
	public int getNumChildSourceDirectories(){
		return this.childSourceDirectoryList.size();
	}

	public List<SourceDirectory> getChildSourceDirectoryList(){
		return this.childSourceDirectoryList;
	}

	public List<SourceDirectory> getDescendentSourceDirectoryList(){
		return getDescendentSourceDirectoryList(new ArrayList<SourceDirectory>(), this.childSourceDirectoryList);
	}
	
	/**
	 * 
	 * @param descendent
	 * @param children
	 * @return depth first list of SourceDirectories. 
	 */
	private List<SourceDirectory> getDescendentSourceDirectoryList(List<SourceDirectory> descendent, List<SourceDirectory> children){
		if(children != null){
			for(SourceDirectory sourceDirectory: children){
				getDescendentSourceDirectoryList(descendent, sourceDirectory.getChildSourceDirectoryList());
				descendent.add(sourceDirectory);
			}
		}
		return descendent;
	}

	@Override
	public int hashCode(){
		return this.sourceDirectoryPath.hashCode();
	}

	@Override
	public boolean equals(Object o){
		if(o == this){
			return true;
		}else if(o instanceof SourceDirectory){
			SourceDirectory sourceDirectory = (SourceDirectory) o;
			return sourceDirectory.getDirectory().equals(this.getDirectory());
		}else{
			return false;
		}
	}
	
	void setSourceDirectoryConfiguration(SourceDirectoryConfiguration configHandler){
		this.sourceDirectoryConfiguration = configHandler;
	}

	void setPageMaster(PageMaster pageMaster){
		this.pageMaster = pageMaster;
	}

	void addChildSourceDirectoryList(SourceDirectory aChild){
		if(this.childSourceDirectoryList == null){
			this.childSourceDirectoryList = new LinkedList<SourceDirectory>();
		}
		this.childSourceDirectoryList.add(aChild);
	}
	
	private void addPageIDList(List<PageID> pageIDList, File file){
		int numPages = 1;
		String filename = file.getName().toLowerCase();
		boolean MULTIPAGE_TIFF_ENABLED = true;
		if(ImageUtil.isMultipageTiff(filename) && MULTIPAGE_TIFF_ENABLED){
			try{
				numPages = ImageFactory.getNumPages(file);
			}catch(IOException ignore){
				ignore.printStackTrace();
			}
		}
		String relativePath = file.getAbsolutePath().substring(this.sourceDirectoryRoot.getAbsolutePath().length() + 1);
		for(int i = 0; i < numPages; i++){
			pageIDList.add(new PageID(new FileResourceID(relativePath, file.lastModified()), i, numPages));
		}
	}

	private List<PageID> createPageIDList(){
		List<PageID> ret = new ArrayList<PageID>();
		File[] files = getDirectory().listFiles(new FileFilter(){
			public boolean accept(File targetFile) {
				if(! targetFile.isDirectory() && ImageFactory.isSupported(targetFile.getName())){
		            return true;
				}
				return false;
			}
		}
		);
		
		File[] sortedFiles = NameBasedComparableFile.createSortedFileArray(files);  
		
		for(File file: sortedFiles){
			addPageIDList(ret, file);
		}

		/*
		if(this.childSourceDirectoryList != null){
			for(SourceDirectory child: this.childSourceDirectoryList){
				ret.addAll(child.createPageIDList());
			}
		}*/
		return ret;
	}
	
	/*
	public Map<String,Integer> getRowIndexBaseMap(){
		return this.rowIndexBaseMap;
	}
	*/
	
}
