/*

 SessionSourceContentAccessor.java

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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import net.sqs2.omr.util.FileContentsCache;

public class ContentAccessorImpl implements Serializable, ContentAccessor {

	private static final long serialVersionUID = 0L;

	private PageTaskAccessor pageTaskAccessor;
	
	private File rootDirectory;
	private FormMasterAccessor pageMasterAccessor;
	private RowAccessor rowAccessor;
	private FileContentsCache fileContentCache;

	protected Map<String, SourceDirectory> sourceDirectoryMap;

	private static final int SESSION_SOURCE_IMAGE_BYTEARRAY_CACHE_SIZE = 16;// size

	public ContentAccessorImpl(File rootDirectory) throws IOException {
		this.rootDirectory = rootDirectory;
		this.pageMasterAccessor = FormMasterAccessor.createInstance(rootDirectory);
		this.pageTaskAccessor = PageTaskAccessor.createInstance(rootDirectory);
		this.rowAccessor = RowAccessor.createInstance(rootDirectory);
		this.fileContentCache = new FileContentsCache(rootDirectory, SESSION_SOURCE_IMAGE_BYTEARRAY_CACHE_SIZE);
		this.sourceDirectoryMap = new HashMap<String, SourceDirectory>();
		validate(rootDirectory);
	}
	
	public void close() throws IOException{
		flush();
		this.pageMasterAccessor.dispose();
		this.pageTaskAccessor.dispose();
		this.rowAccessor.dispose();
		this.fileContentCache.clear();
		this.sourceDirectoryMap.clear();
	}
	
	public void flush() throws IOException{
		this.pageMasterAccessor.flush();
		this.pageTaskAccessor.flush();
		this.rowAccessor.flush();
	}
	
	private void validate(File sourceDirectoryRootFile) throws FileNotFoundException {
		if (!sourceDirectoryRootFile.exists()) {
			throw new FileNotFoundException(sourceDirectoryRootFile.getAbsolutePath());
		}
		if (!sourceDirectoryRootFile.isDirectory()) {
			throw new FileNotFoundException(sourceDirectoryRootFile.getAbsolutePath());
		}
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentAccessor#getPageMasterAccessor()
	 */
	public FormMasterAccessor getFormMasterAccessor() {
		return this.pageMasterAccessor;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentAccessor#getPageTaskAccessor()
	 */
	public PageTaskAccessor getPageTaskAccessor() {
		return this.pageTaskAccessor;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentAccessor#getRowAccessor()
	 */
	public RowAccessor getRowAccessor() {
		return this.rowAccessor;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentAccessor#getFileContentCache()
	 */
	public FileContentsCache getFileContentCache() {
		return this.fileContentCache;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentAccessor#getRootDirectory()
	 */
	public File getRootDirectory() {
		return this.rootDirectory;
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentAccessor#putSourceDirectory(java.lang.String, net.sqs2.omr.page.source.SourceDirectory)
	 */
	public void putSourceDirectory(String path, SourceDirectory sourceDirectory) {
		this.sourceDirectoryMap.put(path, sourceDirectory);
	}

	/* (non-Javadoc)
	 * @see net.sqs2.omr.session.source.SessionSourceContentAccessor#getSourceDirectory(java.lang.String)
	 */
	public SourceDirectory getSourceDirectory(String path) {
		return this.sourceDirectoryMap.get(path);
	}

	@Override
	public String toString() {
		return "SourceDirectoryRoot[" + this.getRootDirectory().getAbsolutePath() + "]";
	}

	@Override
	public int hashCode() {
		return toString().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		try {
			ContentAccessor sourceDirectoryRoot = (ContentAccessor) o;
			return this == sourceDirectoryRoot
					|| (this.rootDirectory.equals(sourceDirectoryRoot.getRootDirectory()));
		} catch (ClassCastException ex) {
		}
		return false;
	}
}
