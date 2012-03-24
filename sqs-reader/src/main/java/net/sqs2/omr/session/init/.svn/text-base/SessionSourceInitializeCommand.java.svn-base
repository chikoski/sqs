/**
 * 
 */
package net.sqs2.omr.session.init;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.Callable;

import net.sqs2.event.EventSource;
import net.sqs2.omr.master.FormMasterException;
import net.sqs2.omr.model.CacheConstants;
import net.sqs2.omr.model.ConfigSchemeException;
import net.sqs2.omr.model.ConfigUtil;
import net.sqs2.omr.model.MarkReaderConfiguration;
import net.sqs2.omr.model.PageTaskException;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSourceState;
import net.sqs2.omr.session.model.SessionStopException;

public class SessionSourceInitializeCommand implements Callable<SessionSource>{

	SessionSource sessionSource;
	SessionSourceInitializer sessionSourceInitializer;
	EventSource<SessionSourceInitializationEvent> eventSource;

	public SessionSourceInitializeCommand(SessionSource sessionSource, 
			EventSource<SessionSourceInitializationEvent> eventSource)
	throws IOException, PageTaskException, SessionSourceException, FormMasterException, SessionStopException {
		
		this.sessionSource = sessionSource;
		this.sessionSourceInitializer = new SessionSourceInitializer(new MergedFormMasterFactory(),
				this.sessionSource, eventSource,
				MarkReaderConfiguration.isEnabled(MarkReaderConfiguration.KEY_SEARCHANCESTOR));
	}
	
	public SessionSource call()throws SessionSourceException, FormMasterException, ConfigSchemeException, SessionSourceInitializationStopException, SessionStopException, IOException{
		File sourceDirectoryRootFile = this.sessionSource.getRootDirectory();
		File resultDirectoryRoot = createResultDirectoryRoot(sourceDirectoryRootFile);
		if (!checkDirectoryExistence(sourceDirectoryRootFile, resultDirectoryRoot)) {
			throw new IOException("WRITE ERROR in: " + sourceDirectoryRootFile.getAbsolutePath());
		}
		ConfigUtil.createConfigFileIfNotExists(sourceDirectoryRootFile);
		
		if(this.sessionSourceInitializer == null){
			throw new RuntimeException("SessionSourceFactory cannot reused.");
		}
		this.sessionSourceInitializer.call();
		this.sessionSourceInitializer = null;
		this.sessionSource.setSessionSourceState(SessionSourceState.INITIALIZED);
		return this.sessionSource;
		
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