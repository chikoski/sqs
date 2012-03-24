/**
 * 
 */
package net.sqs2.omr.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import net.sqs2.util.FileResourceID;
import net.sqs2.util.FileUtil;

public class FileContentsCache extends FileResourceCache<FileContents> {

	File rootDirectory;

	public FileContentsCache(File rootDirectory, int cacheSize) {
		super(cacheSize);
		this.rootDirectory = rootDirectory;
	}

	public FileContents get(FileResourceID fileResourceID) throws IOException {
		FileContents fileContents;
		synchronized (this.map) {
			fileContents = this.map.get(fileResourceID);
			if (fileContents == null) {
				byte[] bytes = createByteArray(fileResourceID.getRelativePath());
				long lastModified = getLastModified(fileResourceID.getRelativePath());
				fileContents = new FileContents(bytes, lastModified);
				this.map.put(fileResourceID, fileContents);
			}
		}
		return fileContents;
	}

	public long getLastModified(String relativePath) {
		File file = new File(this.rootDirectory.getAbsolutePath(), relativePath);
		return file.lastModified();
	}

	private byte[] createByteArray(String relativePath) throws IOException {
		BufferedInputStream in = null;
		ByteArrayOutputStream out = null;
		try {
			File file = new File(this.rootDirectory, relativePath);
			in = new BufferedInputStream(new FileInputStream(file));
			out = new ByteArrayOutputStream();
			FileUtil.connect(in, out);
		} finally {
			if (in != null) {
				in.close();
			}
			if (out != null) {
				out.close();
			}
		}
		return out.toByteArray();
	}
}
