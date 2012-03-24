package net.sqs2.omr.session.scan;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.ContentIndexer;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.model.SessionSourceState;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.session.model.SessionStopException;

public abstract class DiffusedSessionSourceScanner extends SessionSourceScanner {

	public DiffusedSessionSourceScanner(SessionSource sessionSource) {
		super(sessionSource);
	}

	@Override
	protected void scan() throws SessionStopException, IOException {
		AbstractSessionSourceScannerWorker worker = createWorker();
		for (FormMaster formMaster : this.sessionSource.getContentIndexer().getFormMasterList()) {

			ContentIndexer indexer = this.sessionSource.getContentIndexer();

			for(SourceDirectory sourceDirectory: indexer.getFlattenSourceDirectoryList(formMaster)){
				List<PageID> pageIDList = sourceDirectory.getPageIDList();

				if (pageIDList == null) {
					continue;
				}

				worker.startScanningSourceDirectory(sourceDirectory);

				int pageIDIndex = 0;

				for (PageID pageID : pageIDList) {

					if (this.sessionSource.getSessionSourceState().equals(SessionSourceState.STOPPED)) {
						throw new SessionStopException();
					}

					worker.work(sourceDirectory, pageID, -1, -1);

					pageIDIndex++;
				}

				Logger.getLogger(getClass().getName()).info(
						"scanning done : " + sourceDirectory.getRelativePath() + " " + pageIDIndex);
			}
		}

		worker.finishScan();
	}

}
