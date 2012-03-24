package net.sqs2.omr.task.producer;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;

import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.session.SessionStopException;
import net.sqs2.omr.source.PageID;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;

public abstract class SessionSourceScanner {

	protected SessionSource sessionSource;
	
	public SessionSourceScanner(SessionSource sessionSource) {
		this.sessionSource = sessionSource;
	}
	
	abstract AbstractSessionSourceScannerWorker createWorker()throws IOException;
		
	static protected abstract class AbstractSessionSourceScannerWorker{
		abstract void startScanningSourceDirectory(SourceDirectory sourceDirectory);
		abstract void work(SourceDirectory sourceDirectory,
							int pageNumber, PageID pageID, int rowIndex)throws SessionStopException;
		
		abstract void finishScan();
	}
	
	public void run(){
		if(! this.sessionSource.isInitialized()){
			throw new RuntimeException("SessionSource NOT INITIALIZED");
		}
		Logger.getAnonymousLogger().info("SessionSource Structure scanning start *********** ");
		this.sessionSource.setPreparing();
		
		try{
			scan();
			Logger.getAnonymousLogger().info("SessionSource Structure scanning end *********** ");
		}catch(SessionStopException ignore){
			Logger.getAnonymousLogger().info("SessionSource Structure scanning stopped *********** ");
		}catch(Exception ex){
			ex.printStackTrace();
		}
		this.sessionSource.setPrepared();
	}
	
	private void scan() throws SessionStopException,IOException {
		AbstractSessionSourceScannerWorker worker = createWorker(); 
		for(PageMaster pageMaster: this.sessionSource.getSessionSourceContentIndexer().getPageMasterList()){
			int numPages = pageMaster.getNumPages();
			List<SourceDirectory> sourceDirectoryList = this.sessionSource.getSessionSourceContentIndexer().getSourceDirectoryList(pageMaster);
			Logger.getAnonymousLogger().info("scanning : "+ sourceDirectoryList.size()+" "+sourceDirectoryList);

			for(SourceDirectory sourceDirectory: sourceDirectoryList){
				
				List<PageID> pageIDList = sourceDirectory.getPageIDList();
				
				if(pageIDList == null){
					continue;
				}

				worker.startScanningSourceDirectory(sourceDirectory);
								
				int pageIDIndex = 0;
				
				for(PageID pageID: pageIDList){
					int rowIndex = pageIDIndex / numPages;
					int pageNumber = (pageIDIndex % numPages) + 1;
					
					if(this.sessionSource.hasStopped()){
						throw new SessionStopException();
					}

					worker.work(sourceDirectory, pageNumber, pageID, rowIndex);
					
					pageIDIndex++;
				}
				
				Logger.getAnonymousLogger().info("scanning done : "+sourceDirectory.getPath()+" "+pageIDIndex);
			}
		}
		
		worker.finishScan();
		
	}

}