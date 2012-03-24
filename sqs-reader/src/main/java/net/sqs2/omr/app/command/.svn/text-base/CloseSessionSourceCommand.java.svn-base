package net.sqs2.omr.app.command;

import java.io.File;

import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;

public class CloseSessionSourceCommand extends AbstractCommand<Void>{
	public CloseSessionSourceCommand(File sourceDirectory){
		super(sourceDirectory);
	}
	
	public Void call(){
		MarkReaderSession session = MarkReaderSessions.get(sourceDirectoryRoot);
		if(session != null){
			session.closeSessionSource();
		}
		return null;
	}
}
