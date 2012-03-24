package net.sqs2.omr.session.scan;

import java.io.IOException;
import java.util.logging.Logger;

import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.session.model.SessionStopException;

public abstract class SessionSourceScanner {

	protected SessionSource sessionSource;

	protected static abstract class AbstractSessionSourceScannerWorker {
		abstract void startScanningSourceDirectory(SourceDirectory sourceDirectory);
	
		abstract void work(SourceDirectory sourceDirectory, PageID pageID, int pageNumber, int rowIndex) throws SessionStopException;
	
		abstract void finishScan();
	}

	protected abstract AbstractSessionSourceScannerWorker createWorker() throws IOException;

	public SessionSourceScanner(SessionSource sessionSource) {
		this.sessionSource = sessionSource;
	}

	public void run() {
		Logger.getLogger(getClass().getName()).info("SessionSource Structure scanning start *********** ");
		try {
			scan();
			Logger.getLogger(getClass().getName()).info("SessionSource Structure scanning end *********** ");
		} catch (SessionStopException ignore) {
			Logger.getLogger(getClass().getName()).info("SessionSource Structure scanning stopped *********** ");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	protected abstract void scan() throws SessionStopException, IOException;

}