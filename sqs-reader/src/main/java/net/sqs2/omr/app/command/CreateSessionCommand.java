package net.sqs2.omr.app.command;

import java.io.File;
import java.io.IOException;

import net.sqs2.omr.model.CacheConstants;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.session.service.AbstractRemoteTaskTracker;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.MarkReaderSessions;
import net.sqs2.omr.session.service.OutputEventRecieversFactory;
import net.sqs2.omr.session.service.SessionSourceServerDispatcher;



public class CreateSessionCommand extends AbstractCommand<MarkReaderSession>{

	private AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> taskTracker;
	private SessionSourceServerDispatcher dispatcher;
	private OutputEventRecieversFactory outputEventConsumersFactory;
	
	public CreateSessionCommand(AbstractRemoteTaskTracker<OMRPageTask, SessionSourceServerDispatcher> taskTracker, SessionSourceServerDispatcher dispatcher, File sourceDirectoryRoot,
			OutputEventRecieversFactory outputEventConsumersFactory){
		super(sourceDirectoryRoot);
		this.taskTracker = taskTracker;
		this.dispatcher = dispatcher;
		this.outputEventConsumersFactory = outputEventConsumersFactory;
	}

	public MarkReaderSession call()throws IOException{
		synchronized(CreateSessionCommand.class){
			if (sourceDirectoryRoot == null || ! sourceDirectoryRoot.isDirectory()){
				throw new IOException("DIRECTORY IO ERROR in: " + sourceDirectoryRoot);
			}
			if(sourceDirectoryRoot.getName().endsWith(CacheConstants.CACHE_ROOT_DIRNAME)){
				throw new IOException("DIRECTORY INVALID:"+sourceDirectoryRoot);
			}
			if(MarkReaderSessions.contains(sourceDirectoryRoot)){
				throw new IOException("DIRECTORY ALREADY EXISTS:"+sourceDirectoryRoot);
			}
			return MarkReaderSessions.create(sourceDirectoryRoot, this.taskTracker, this.dispatcher,
					this.outputEventConsumersFactory);
		}
	}
}
