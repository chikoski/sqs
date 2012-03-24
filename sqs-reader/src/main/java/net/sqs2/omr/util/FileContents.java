/**
 * 
 */
package net.sqs2.omr.util;

import java.io.Serializable;

public class FileContents implements Serializable{
	private static final long serialVersionUID = 1L;
	byte[] bytes;
	long lastModified;

	public FileContents(byte[] bytes, long lastModified) {
		this.bytes = bytes;
		this.lastModified = lastModified;
	}

	public byte[] getBytes() {
		return this.bytes;
	}

	public long getLastModified() {
		return this.lastModified;
	}
}
