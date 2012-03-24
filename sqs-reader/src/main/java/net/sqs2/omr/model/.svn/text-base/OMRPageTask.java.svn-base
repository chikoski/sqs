/*

 PageNumberedPageTask.java

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

import java.io.Serializable;
import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

import net.sqs2.util.FileResourceID;

public class OMRPageTask extends PageTask implements Serializable, Delayed{

	private static final long serialVersionUID = 6L;

	protected int processingPageNumber;

	public OMRPageTask() {
	}

	public OMRPageTask(String sourceDirectoryRootPath, FileResourceID config,
			FileResourceID defaultPageMasterFileResourceID,
			int processingPageNumber, PageID pageID, long sessionID) {
		super(sourceDirectoryRootPath, config, defaultPageMasterFileResourceID, sessionID, pageID);
		this.processingPageNumber = processingPageNumber;
		this.id = createID();
	}
	
	public String createID(){
		return createID(this.pageID, this.processingPageNumber);
	}

	public static String createID(PageID pageID, int processingPageNumber) {
		return processingPageNumber + "\t" + pageID.createID();
	}

	@Override
	public long getDelay(TimeUnit unit){
		return this.ticket.getDelay(unit);
	}
	
	@Override
	public boolean equals(Object o) {
		try {
			OMRPageTask task = (OMRPageTask) o;
			return this.id.equals(task.id)
					&& this.defaultPageMasterFileResourceID.equals(task.defaultPageMasterFileResourceID)
					&& this.configResourceID.equals(task.configResourceID)
					&& this.processingPageNumber == task.processingPageNumber && this.pageID.equals(task.pageID);
		} catch (ClassCastException ignore) {
		}
		return false;
	}

	@Override
	public int compareTo(Delayed o) {
		try {
			OMRPageTask task = (OMRPageTask) o;
			int diff = 0;
			if (this.id.equals(task.id)) {
				return 0;
			}
			if ((diff = this.defaultPageMasterFileResourceID.compareTo(task.defaultPageMasterFileResourceID)) != 0) {
				return diff;
			}
			if ((diff = this.configResourceID.compareTo(task.configResourceID)) != 0) {
				return diff;
			}
			if ((diff = this.processingPageNumber - task.processingPageNumber) != 0) {
				return diff;
			}
			if ((diff = this.pageID.compareTo(task.pageID)) != 0) {
				return diff;
			}
		} catch (ClassCastException ignore) {
		}
		return 1;
	}
	
	public int getProcessingPageNumber() {
		return this.processingPageNumber;
	}

}
