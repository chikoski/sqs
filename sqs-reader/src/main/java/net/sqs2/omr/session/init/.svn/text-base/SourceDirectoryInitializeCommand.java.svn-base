/**
 * 
 */
package net.sqs2.omr.session.init;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import net.sqs2.omr.model.CacheConstants;
import net.sqs2.omr.model.ConfigUtil;

public class SourceDirectoryInitializeCommand implements Callable<Boolean>{
	/**
	 * 
	 */
	File sourceDirectoryRootFile;
	public SourceDirectoryInitializeCommand(File sourceDirectoryRootFile){
		this.sourceDirectoryRootFile = sourceDirectoryRootFile;
	}
	
	public Boolean call()throws IOException{
		File resultDirectoryRoot = createResultDirectoryRoot(this.sourceDirectoryRootFile);
		if (!checkDirectoryExistence(this.sourceDirectoryRootFile, resultDirectoryRoot)) {
			throw new IOException("WRITE ERROR in: " + this.sourceDirectoryRootFile.getAbsolutePath());
		}
		ConfigUtil.createConfigFileIfNotExists(this.sourceDirectoryRootFile);
		return Boolean.TRUE;
	}
	
	private File createResultDirectoryRoot(File sourceDirectoryRoot) {
		return new File(sourceDirectoryRoot.getAbsoluteFile(), CacheConstants.getCacheDirname());
	}

	private boolean checkDirectoryExistence(final File sourceDirectoryRoot, final File resultDirectoryRoot) {
		if (!sourceDirectoryRoot.exists() || !sourceDirectoryRoot.isDirectory()
				|| !sourceDirectoryRoot.canRead() || !sourceDirectoryRoot.canWrite()) { 
			// sourceDirectoryRoot.getName().endsWith(PageTaskResult.RESULT_FOLDER_SUFFIX)
			return false;
		}
		return true;
	}
	

}