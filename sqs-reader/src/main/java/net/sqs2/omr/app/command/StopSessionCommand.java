package net.sqs2.omr.app.command;

import java.io.File;

import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;

public class StopSessionCommand extends AbstractCommand<Void>{

	public StopSessionCommand(File sourceDirectoryRoot) {
		super(sourceDirectoryRoot);
	}

	@Override
	public Void call(){
		MarkReaderSession session = MarkReaderSessions.get(sourceDirectoryRoot);
		if(session != null){
			session.stopSession();
		}
		return null;
	}
	
}
