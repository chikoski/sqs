package net.sqs2.omr.app.command;

import java.io.File;

import net.sqs2.omr.model.ContentAccessor;
import net.sqs2.omr.model.FormMasterAccessor;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.PageTaskHolder;
import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;

public class ClearSessionCommand extends AbstractCommand<Void>{	
	
	public ClearSessionCommand(File sourceDirectoryRoot){
		super(sourceDirectoryRoot);
	}
	
	public Void call(){
		clearSession(sourceDirectoryRoot);
		return null;
	}
	
	private void clearSession(final File sourceDirectoryRoot) {
		MarkReaderSession sessionService = MarkReaderSessions.get(sourceDirectoryRoot);
		if(sessionService == null){
			return;
		}
		SessionSource sessionSource = sessionService.getSessionSource();
		if(sessionSource != null){
			clearSessionSource(sessionSource);
		}
		PageTaskHolder taskHolder = sessionService.getTaskHolder();
		taskHolder.clear();
	}

	private void clearSessionSource(SessionSource sessionSource) {
		ContentAccessor accessor = sessionSource.getContentAccessor();
		FormMasterAccessor formMasterAccessor = accessor.getFormMasterAccessor();
		formMasterAccessor.removeAll();
		PageTaskAccessor taskAccessor = accessor.getPageTaskAccessor();
		if (taskAccessor != null) {
			taskAccessor.removeAll();
		}
	}


}
