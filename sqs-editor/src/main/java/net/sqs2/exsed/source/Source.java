/*

Source.java 
Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Created on 2004/07/28

 */
package net.sqs2.exsed.source;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

/**
 * @author hiroya
 * 
 */
public abstract class Source {
	String title = null;
	File file = null;
	URL url = null;
	long mtime = 0L;
	boolean dirty = false;
	boolean readonly = false;
	static int serial = 1;

	public Source() throws SourceException {
		this.title = "*Undefined" + (serial++);
		this.file = null;
		this.dirty = false;
		this.readonly = false;
	}

	public Source(File file, InputStream in, File originalFile) throws SourceException {
		this.title = null;
		this.file = file;
		if (originalFile == null) {
			this.mtime = file.lastModified();
			this.readonly = !file.canWrite();
			this.dirty = false;
		} else {
			this.mtime = originalFile.lastModified();
			this.readonly = false;
			this.dirty = true;
		}
		initialize(in);
	}

	public Source(URL url, boolean readonly, String title) throws SourceException {
		this.url = url;
		String readOnlyMark = readonly ? "%" : "";
		if (title != null) {
			this.title = readOnlyMark + title;
		} else {
			String filePart = url.getFile();
			try {
				String file = filePart.substring(filePart.lastIndexOf('/') + 1);
				this.title = readOnlyMark + file;
			} catch (NullPointerException ex) {
				this.title = readOnlyMark + filePart;
			}
		}
		if (url.getProtocol().toLowerCase().equals("file")) {
			setFile(new File(url.getFile()));
		} else {
			this.file = null;
		}
		this.readonly = readonly;
		this.dirty = false;
		try {
			initialize(url.openStream());
		} catch (IOException ex) {
			throw new SourceException(ex);
		}
	}

	public String toString() {
		return "Source[" + this.title + " " + this.file + "(" + this.url + ")]";
	}

	public abstract void initialize(InputStream stream) throws SourceException;

	public abstract InputStream createInputStream() throws IOException;

	public void update() {
		this.mtime = this.file.lastModified();
	}

	public long lastModified() {
		return this.mtime;
	}

	public boolean isModified() {
		if (this.file != null) {
			return this.mtime < this.file.lastModified();
		} else {
			return false;
		}
	}

	public void setFile(File file) {
		this.file = file;
		if (file != null) {
			this.readonly = !file.canWrite();
		}
	}

	@Override
	public int hashCode() {
		if (this.file != null) {
			return this.file.hashCode();
		} else if (this.url != null) {
			return this.url.toString().hashCode();
		}
		return super.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		try {
			Source source = (Source) obj;
			if (source == null || source.getFile() == null || getFile() == null) {
				return false;
			}
			return source.getFile().getAbsoluteFile().equals(getFile().getAbsoluteFile());
		} catch (ClassCastException ex) {
			return false;
		}
	}

	public File getFile() {
		return this.file;
	}

	public URL getURL() {
		return this.url;
	}

	public String getTitle() {
		String prefix = "";
		if (this.readonly) {
			prefix = "%";
		} else if (this.dirty) {
			prefix = "*";
		}
		if (this.file != null) {
			return prefix + this.file.getName();
		} else {
			return prefix + this.title;
		}
	}

	public boolean isDirty() {
		return this.dirty;
	}

	public boolean isReadOnly() {
		return this.readonly;
	}

	public void setReadOnly(boolean readonly) {
		this.readonly = readonly;
	}

	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}

	public void save() throws IOException {
		InputStream in = null;
		// PrintWriter writer = new PrintWriter(new OutputStreamWriter(new
		// BufferedOutputStream(new FileOutputStream(source.getFile())),
		// "UTF-8"));
		OutputStream out = null;
		try {
			in = createInputStream();
			out = new BufferedOutputStream(new FileOutputStream(getFile()));
			byte[] buf = new byte[4096];
			int len;
			while (0 < (len = in.read(buf, 0, 1024))) {
				out.write(buf, 0, len);
			}
			setDirty(false);
		} finally {
			if (out != null) {
				out.close();
			}
			if (in != null) {
				in.close();
			}
			update();
		}
	}

}
