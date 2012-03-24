package net.sqs2.omr.task.consumer;

import java.io.File;
import java.io.IOException;

import net.sqs2.omr.session.Session;
import net.sqs2.omr.task.TaskException;

public interface TaskConsumerFactory {

	public abstract AbstractTaskConsumer create(File sourceDirectoryRoot, Session session) throws TaskException, IOException;

}