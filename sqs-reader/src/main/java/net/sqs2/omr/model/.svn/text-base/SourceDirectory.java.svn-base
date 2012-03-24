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
package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.util.FileResourceID;

import org.apache.commons.io.FilenameUtils;

public class SourceDirectory implements Serializable, Comparable<SourceDirectory> {

	private static final long serialVersionUID = 0L;

	private File sourceDirectoryRoot;
	private String sourceDirectoryPath;
	private SourceDirectoryConfiguration sourceDirectoryConfiguration;
	private FormMaster defaultFormMaster;
	
	private int id;

	protected List<PageID> pageIDList;
	protected List<SourceDirectory> childSourceDirectoryList;

	public SourceDirectory(File sourceDirectoryRoot) {
		this(sourceDirectoryRoot, "");
	}
	
	public SourceDirectory(File sourceDirectoryRoot, String sourceDirectoryPath) {
		this.sourceDirectoryRoot = sourceDirectoryRoot;
		this.sourceDirectoryPath = sourceDirectoryPath;
		this.pageIDList = new ArrayList<PageID>();
	}
	
	public void setID(int id){
		this.id = id; 
	}

	public int getID(){
		return this.id;
	}

	@Override
	public String toString() {
		return "SourceDirectory(" + this.sourceDirectoryPath + ")";
	}

	public boolean isRoot() {
		return this.sourceDirectoryPath.equals("");
	}

	public boolean isLeaf() {
		return this.childSourceDirectoryList == null || this.childSourceDirectoryList.size() == 0;
	}

	public File getRoot() {
		return this.sourceDirectoryRoot;
	}

	public File getDirectory() {
		return new File(this.sourceDirectoryRoot.getAbsolutePath() + File.separator
				+ this.sourceDirectoryPath);
	}

	public String getRelativePath() {
		return this.sourceDirectoryPath;
	}

	public PageID getPageID(int index) {
		return getPageIDList().get(index);
	}

	public List<PageID> getPageIDList() {
		return this.pageIDList;
	}

	public int getNumPageIDs() {
		return getPageIDList().size();
	}

	public int getNumPageIDsTotal() {
		int numPageIDsTotal = 0;
		if (this.childSourceDirectoryList != null) {
			for (SourceDirectory child : this.childSourceDirectoryList) {
				numPageIDsTotal += child.getNumPageIDsTotal();
			}
		}
		return numPageIDsTotal + getNumPageIDs();
	}

	public SourceDirectoryConfiguration getConfiguration() {
		return this.sourceDirectoryConfiguration;
	}

	public FormMaster getDefaultFormMaster() {
		return this.defaultFormMaster;
	}
	
	public void setDefaultFormMaster(FormMaster defaultFormMaster) {
		this.defaultFormMaster = defaultFormMaster;
	}

	public void close() {
		this.pageIDList.clear();
		this.pageIDList = null;
	}

	public SourceDirectory getChildSourceDirectory(int index) {
		return this.childSourceDirectoryList.get(index);
	}

	public int getNumChildSourceDirectories() {
		if(this.childSourceDirectoryList == null){
			return 0;
		}
		return this.childSourceDirectoryList.size();
	}

	public List<SourceDirectory> getChildSourceDirectoryList() {
		return this.childSourceDirectoryList;
	}

	public List<SourceDirectory> getDescendentSourceDirectoryList() {
		return getDescendentSourceDirectoryList(new ArrayList<SourceDirectory>(),
				this.childSourceDirectoryList);
	}
	
	/**
	 * 
	 * @param descendent
	 * @param children
	 * @return depth first list of SourceDirectories.
	 */
	private List<SourceDirectory> getDescendentSourceDirectoryList(List<SourceDirectory> descendent, List<SourceDirectory> children) {
		if (children != null) {
			for (SourceDirectory sourceDirectory : children) {
				getDescendentSourceDirectoryList(descendent, sourceDirectory.getChildSourceDirectoryList());
				descendent.add(sourceDirectory);
			}
		}
		return descendent;
	}

	@Override
	public int hashCode() {
		return this.sourceDirectoryPath.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o == this) {
			return true;
		} else if (o instanceof SourceDirectory) {
			SourceDirectory sourceDirectory = (SourceDirectory) o;
			return sourceDirectory.getDirectory().equals(this.getDirectory());
		} else {
			return false;
		}
	}

	public void setSourceDirectoryConfiguration(SourceDirectoryConfiguration configHandler) {
		this.sourceDirectoryConfiguration = configHandler;
	}

	public void addChildSourceDirectoryList(SourceDirectory aChild) {
		if (this.childSourceDirectoryList == null) {
			this.childSourceDirectoryList = new LinkedList<SourceDirectory>();
		}
		this.childSourceDirectoryList.add(aChild);
	}

	public void setupPageIDList(File file) {
		int numPages = 0;

		try{
			if (FilenameUtils.getExtension(file.getName()).equals(".mtiff")) {
				numPages = ImageFactory.getNumPages("mtiff", file);
			}else if(file.getName().toLowerCase().endsWith(".pdf")){
				numPages = ImageFactory.getNumPages("pdf", file);
			}else{
				numPages = 1;
			}
		}catch(IOException ex){
			// ignore unreadable file
			return;
		}
		
		String relativePath = file.getAbsolutePath().substring(this.sourceDirectoryRoot.getAbsolutePath().length() + 1);
		for (int i = 0; i < numPages; i++) {
			pageIDList.add(new PageID(new FileResourceID(relativePath, file.lastModified()), i, numPages));
		}
	}

	@Override
	public int compareTo(SourceDirectory o) {
		String path1 = o.sourceDirectoryRoot.getAbsolutePath()+File.separatorChar+o.sourceDirectoryPath;
		String path2 = sourceDirectoryRoot.getAbsolutePath()+File.separatorChar+sourceDirectoryPath;
		return path2.compareTo(path1);
	}

}
