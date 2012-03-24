/*

 PageMasterMetadata.java
 
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
 
 Created on 2005/02/26

 */

package net.sqs2.omr.master;

import java.io.File;
import java.io.Serializable;

import org.w3c.dom.Document;

public class PageMasterMetadata implements Serializable {
	private final static long serialVersionUID = 3L;

	private File sourceDirectoryRoot;
	private String masterPath;
	private long lastModified;
	private String type;
	private Document sourceDocument = null;

	public PageMasterMetadata(File sourceDirectoryRoot, String masterPath, String type, long lastModified,
			Document sourceDocument) {
		this.sourceDirectoryRoot = sourceDirectoryRoot;
		this.masterPath = masterPath;
		this.type = type;
		this.lastModified = lastModified;
		this.sourceDocument = sourceDocument;
	}

	public File getSourceDirectoryRoot() {
		return this.sourceDirectoryRoot;
	}

	public String getMasterPath() {
		return this.masterPath;
	}

	public File getMasterFile() {
		return new File(this.sourceDirectoryRoot, this.masterPath);
	}

	public long getLastModified() {
		return this.lastModified;
	}

	public String getType() {
		return this.type;
	}

	public Document getSourceDocument() {
		return this.sourceDocument;
	}
}
