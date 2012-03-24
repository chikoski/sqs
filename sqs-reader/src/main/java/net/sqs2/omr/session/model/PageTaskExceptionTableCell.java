/**
 * 
 */
package net.sqs2.omr.session.model;

import java.io.File;

import net.sqs2.omr.model.PageTaskException;
import net.sqs2.swing.ImageThumbnailTableCell;

public class PageTaskExceptionTableCell extends ImageThumbnailTableCell {
	PageTaskException taskException;

	public PageTaskExceptionTableCell(File root, String path, int index, PageTaskException taskException) {
		super(root, path, index);
		this.taskException = taskException;
	}

	public PageTaskException getTaskException() {
		return this.taskException;
	}

}
