/*

 SessionSource.java

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.master.PageMasterAccessor;
import net.sqs2.omr.result.model.RowAccessor;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.omr.task.TaskException;

public class SessionSourceContentAccessor implements Serializable{

	private static final long serialVersionUID = 0L;

	private File rootDirectory;
	private PageMasterAccessor pageMasterAccessor;
	private TaskAccessor pageTaskAccessor;
	private RowAccessor rowAccessor;
	private FileContentCache fileContentCache;
	
	protected Map<String,SourceDirectory> sourceDirectoryMap;

	private static final int SESSION_SOURCE_IMAGE_BYTEARRAY_CACHE_SIZE = 16;// size

	public SessionSourceContentAccessor(File rootDirectory)throws TaskException, IOException{
		this.rootDirectory = rootDirectory;
		this.pageMasterAccessor = new PageMasterAccessor(rootDirectory); 
		this.pageTaskAccessor = new TaskAccessor(rootDirectory);
		this.rowAccessor = new RowAccessor(rootDirectory);
		this.fileContentCache = new FileContentCache(rootDirectory, SESSION_SOURCE_IMAGE_BYTEARRAY_CACHE_SIZE);
		this.sourceDirectoryMap = new HashMap<String, SourceDirectory>();
		validate(rootDirectory);
	}
	
	private void validate(File sourceDirectoryRootFile) throws FileNotFoundException {
		if(! sourceDirectoryRootFile.exists()){
			throw new FileNotFoundException(sourceDirectoryRootFile.getAbsolutePath());
		}
		if(! sourceDirectoryRootFile.isDirectory()){
			throw new FileNotFoundException(sourceDirectoryRootFile.getAbsolutePath());				
		}
	}
	
	public PageMasterAccessor getPageMasterAccessor(){
		return this.pageMasterAccessor;
	}
	
	public TaskAccessor getPageTaskAccessor(){
		return this.pageTaskAccessor;
	}
	
	public RowAccessor getRowAccessor(){
		return this.rowAccessor;
	}
	
	public FileContentCache getFileContentCache(){
		return this.fileContentCache;
	}
	
	public File getRootDirectory(){
		return this.rootDirectory;
	}
	
	public void putSourceDirectory(String path, SourceDirectory sourceDirectory){
		this.sourceDirectoryMap.put(path, sourceDirectory);
	}
	
	public SourceDirectory getSourceDirectory(String path){
		return this.sourceDirectoryMap.get(path);
	}

	@Override
	public String toString(){
		return "SourceDirectoryRoot["+this.getRootDirectory().getAbsolutePath()+"]";
	}
	
	@Override
	public int hashCode(){
		return toString().hashCode();
	}
	
	@Override
	public boolean equals(Object o){
		try{
			SessionSourceContentAccessor sourceDirectoryRoot = (SessionSourceContentAccessor) o;
			return this == sourceDirectoryRoot || (this.rootDirectory.equals(sourceDirectoryRoot.getRootDirectory()));
		}catch(ClassCastException ex){
		}
		return false;
	}
}
