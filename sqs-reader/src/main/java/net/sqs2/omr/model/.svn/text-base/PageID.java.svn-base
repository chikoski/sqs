/*

 PageID.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2005/12/27

 */
package net.sqs2.omr.model;

import java.io.Serializable;

import net.sqs2.util.FileResourceID;
import net.sqs2.util.FileUtil;

public class PageID implements Serializable, Comparable<PageID> {
	private static final long serialVersionUID = 1L;

	private FileResourceID fileResourceID;

	private int indexInFile;
	private int numPagesInFile;

	public PageID() {
	}

	public PageID(FileResourceID fileResourceID, int indexInFile, int numPagesInFile) {
		this.fileResourceID = fileResourceID;
		this.indexInFile = indexInFile;
		this.numPagesInFile = numPagesInFile;
	}

	public FileResourceID getFileResourceID() {
		return this.fileResourceID;
	}

	public String getExtension() {
		return FileUtil.getSuffix(this.fileResourceID.getRelativePath());
	}

	public int getIndexInFile() {
		return this.indexInFile;
	}

	public int getNumPagesInFile() {
		return this.numPagesInFile;
	}

	public int compareTo(PageID pageID) {
		int diff = 0;
		if ((diff = pageID.getIndexInFile() - getIndexInFile()) != 0) {
			return diff;
		}
		return pageID.getFileResourceID().compareTo(this.fileResourceID);
	}

	@Override
	public boolean equals(Object o) {
		try {
			PageID pageID = (PageID) o;
			return (pageID.getIndexInFile() == getIndexInFile() && pageID.getFileResourceID().equals(
					this.fileResourceID));
		} catch (ClassCastException ex) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.fileResourceID.hashCode() ^ this.indexInFile ^ this.numPagesInFile;
	}

	@Override
	public String toString() {
		return createID();
	}

	public String createID() {
		return createID(this.fileResourceID.getRelativePath(), this.fileResourceID.getLastModified(),
				this.indexInFile, this.numPagesInFile);
	}

	public static String createID(String path, long lastModified, int index, int numPagesInFile) {
		return path + "\t" + lastModified + "\t" + index + "\t" + numPagesInFile;
	}

	/*
	 * public BufferedImage createImage()throws IOException{ String suffix =
	 * FileUtil.getSuffix(this.path); BufferedInputStream in = new
	 * BufferedInputStream(new FileInputStream(this.path)); return
	 * ImageFactory.createImage(in, this.index, suffix); }
	 */

}
