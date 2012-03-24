package net.sqs2.omr.result.event;

import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.result.model.RowAccessor;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSourceContentAccessor;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.task.TaskAccessor;

public abstract class ExportEventProducer implements ExportEventConsumer {

	SessionSource sessionSource = null;
	SessionSourceContentsEventFilter filter = null;
	RowAccessor rowAccessor = null;
	TaskAccessor taskAccessor = null;
	
	public ExportEventProducer(SessionSource sessionSource){
		this.sessionSource = sessionSource; 
		SessionSourceContentAccessor sessionSourceAccessor = sessionSource.getSessionSourceContentAccessor();
		this.rowAccessor = sessionSourceAccessor.getRowAccessor();
		this.taskAccessor = sessionSourceAccessor.getPageTaskAccessor();
	}

	public ExportEventProducer(SessionSource sessionSource, SessionSourceContentsEventFilter filter){
		this(sessionSource);
		this.filter = filter;
	}
	
	public void produceSessionEvents(){
		SessionEvent sessionEvent = new SessionEvent(this.sessionSource.getSessionID());
		sessionEvent.setStart();
		sessionEvent.setIndex(0);
		if(this.filter != null && ! this.filter.filter(sessionEvent)){
			return;
		}
		startSession(sessionEvent);
		processSession(sessionEvent);
		sessionEvent.setEnd();
		endSession(sessionEvent);
	}
	
	protected void processSession(SessionEvent sessionEvent){
		System.err.println("ProcessSession: "+sessionEvent.getSessionID());
		
		List<PageMaster> masterList = this.sessionSource.getSessionSourceContentIndexer().getPageMasterList();
		MasterEvent masterEvent = new MasterEvent(sessionEvent, masterList.size());
		
		for(int masterIndex = 0; masterIndex < masterList.size(); masterIndex++){
			PageMaster master = masterList.get(masterIndex);
			masterEvent.setStart();
			masterEvent.setIndex(masterIndex);
			masterEvent.setFormMaster((FormMaster)master);
			if(this.filter != null && ! this.filter.filter(masterEvent)){
				return;
			}
			startMaster(masterEvent);
			processMaster(masterEvent, (FormMaster)master);
			masterEvent.setEnd();
			endMaster(masterEvent);
		}
	}

	protected void processMaster(MasterEvent masterEvent, FormMaster master){
		System.err.println("ProcessMaster: "+master);
		List<SourceDirectory> sourceDirectoryList = this.sessionSource.getSessionSourceContentIndexer().getSourceDirectoryList(master);
		SourceDirectoryEvent sourceDirectoryEvent = new SourceDirectoryEvent(masterEvent, master, sourceDirectoryList.size());
		
		for(int sourceDirectoryIndex = 0; sourceDirectoryIndex < sourceDirectoryList.size(); sourceDirectoryIndex++){
			SourceDirectory sourceDirectory = sourceDirectoryList.get(sourceDirectoryIndex);
			sourceDirectoryEvent.setStart();
			sourceDirectoryEvent.setIndex(sourceDirectoryIndex);
			sourceDirectoryEvent.setSourceDirectory(sourceDirectory);
			startSourceDirectory(sourceDirectoryEvent);
			processSourceDirectory(sourceDirectoryEvent, master);
			sourceDirectoryEvent.setEnd();
			endSourceDirectory(sourceDirectoryEvent);
		}
	}
	
	abstract protected void processSourceDirectory(SourceDirectoryEvent sourceDirectoryEvent, FormMaster master);

}
