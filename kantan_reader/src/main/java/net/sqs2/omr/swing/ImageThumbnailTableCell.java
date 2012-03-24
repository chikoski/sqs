package net.sqs2.omr.swing;

import java.io.File;

public class ImageThumbnailTableCell {
	File root;
	String path;

	public ImageThumbnailTableCell(File root, String path) {
		super();
		this.root = root;
		this.path = path;
	}
	public File getRoot() {
		return root;
	}

	public String getPath() {
		return path;
	}

}