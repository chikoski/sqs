/*

 PageTask.java

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
package net.sqs2.omr.task;

import java.io.Serializable;
import java.util.Comparator;
import java.util.concurrent.Delayed;

import net.sqs2.omr.source.PageID;
import net.sqs2.util.FileResourceID;

public class PageTask extends AbstractTask implements Serializable,Delayed{

	private static final long serialVersionUID = 5L;

	protected int pageNumber;
	protected PageID pageID;
	protected String id;

	public PageTask(){}
	
	public PageTask(String sourceDirectoryRootPath, 
			FileResourceID master,
			FileResourceID configSource,
			int pageNumber,
			PageID pageID,
			long sessionID) {
		super(sourceDirectoryRootPath, master, configSource, sessionID);
		this.pageID = pageID;
		this.pageNumber = pageNumber;
		this.id = createID(this.masterResourceID, pageNumber, pageID);
	}
	
	public static String createID(FileResourceID master, int pageNumber, PageID pageID) {
		return master.toString()+"#"+pageNumber+"\t"+pageID.createID();
	}
	
	public static class TaskComparator implements Comparator<PageTask>{
		public int compare(PageTask a, PageTask b){
			int diff = a.getPageID().getFileResourceID().compareTo(b.getPageID().getFileResourceID());
			if(diff != 0){
				return diff;
			}
			diff = a.getPageID().getIndex() - b.getPageID().getIndex();
			if(diff != 0){
				return diff;
			}
			diff = a.getMasterFileResourceID().getRelativePath().compareTo(b.getMasterFileResourceID().getRelativePath());
			if(diff != 0){
				return diff;
			}
			diff = a.getConfigHandlerFileResourceID().getRelativePath().compareTo(b.getConfigHandlerFileResourceID().getRelativePath());
			if(diff != 0){
				return diff;
			}else{
				return (int)(a.configResourceID.getLastModified() - b.configResourceID.getLastModified());
			}
		}
	}

	public PageID getPageID(){
		return this.pageID;
	}

	public int getPageNumber(){
		return this.pageNumber;
	}
	
	public String getID() {
		return this.id;
	}
	
	public String toString() {
		return this.id;
	}

	public boolean equals(Object o) {
		try{
			PageTask task = (PageTask) o; 
			return this.id.equals(task.id) && this.masterResourceID.equals(task.masterResourceID) && this.configResourceID.equals(task.configResourceID) 
			&& this.pageNumber == task.pageNumber && this.pageID.equals(task.pageID);
		}catch(ClassCastException ignore){
		}
		return false;
	}

	public int compareTo(Delayed o) {
		try{
			PageTask task = (PageTask) o;
			int diff = 0;
			if(this.id.equals(task.id)){
				return 0;
			}
			if((diff = this.masterResourceID.compareTo(task.masterResourceID)) != 0){
				return diff; 
			}
			if((diff = this.configResourceID.compareTo(task.configResourceID)) != 0){
				return diff;
			}
			if((diff = this.pageNumber - task.pageNumber) != 0){
				return diff;
			}
			if((diff = this.pageID.compareTo(task.pageID)) != 0){
				return diff;
			}
		}catch(ClassCastException ignore){
		}
		return 1;
	}

	public int hashCode() {
		return this.id.hashCode();
	}


}
