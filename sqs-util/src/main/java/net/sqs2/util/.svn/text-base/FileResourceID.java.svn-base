/*

 FileResource.java

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
package net.sqs2.util;

import java.io.Serializable;

public class FileResourceID implements Comparable<FileResourceID>, Serializable {
	private static final long serialVersionUID = 2L;

	private String relativePath;

	private long lastModified;

	public FileResourceID() {
	}

	public FileResourceID(String relativePath, long lastModified) {
			this.relativePath = relativePath;
		this.lastModified = lastModified;
	}

	public String toString() {
		return "[FileResourceID:"+this.relativePath + "@" + this.lastModified+"]";
	}

	public String getRelativePath() {
		return this.relativePath;
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public boolean equals(Object o) {
		if (o == this) {
			return true;
		}
		try {
			FileResourceID u = (FileResourceID) o;
			if (u.getRelativePath() == null) {
				if(this.relativePath == null){
					return true;
				}else{
					return false;
				}
			}
			return u.getLastModified() == this.lastModified && u.getRelativePath().equals(this.relativePath);
		} catch (ClassCastException ex) {
			return false;
		}
	}

	public int compareTo(FileResourceID o) {
		if (o == null || o.relativePath == null) {
			return -1;
		}
		if (this.relativePath == null) {
			return 1;
		}
		try {
			int diff = 0;
			if ((diff = o.getRelativePath().compareTo(this.relativePath)) != 0) {
				return diff;
			}
			if ((diff = (int) (o.lastModified - this.lastModified)) != 0) {
				return diff;
			}
		} catch (ClassCastException ignore) {
		}
		return 0;
	}

	public int hashCode() {
		if (this.relativePath == null) {
			return 0;
		} else {
			return (int) this.lastModified % this.relativePath.hashCode();
		}
	}
}
