package net.sqs2.omr.model;

import java.io.File;
import java.io.IOException;

import net.sqs2.omr.util.FileContentsCache;

public interface ContentAccessor {

	public abstract void close() throws IOException;
	
	public abstract void flush() throws IOException;

	public abstract FormMasterAccessor getFormMasterAccessor();

	public abstract PageTaskAccessor getPageTaskAccessor();

	public abstract RowAccessor getRowAccessor();

	public abstract FileContentsCache getFileContentCache();

	public abstract File getRootDirectory();

	public abstract void putSourceDirectory(String path,
			SourceDirectory sourceDirectory);

	public abstract SourceDirectory getSourceDirectory(String path);
	
}