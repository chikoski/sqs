package net.sqs2.omr.app.command;

import java.io.File;
import java.util.concurrent.Callable;

abstract class AbstractCommand<T> implements Callable<T>{
	protected File sourceDirectoryRoot;
	
	protected AbstractCommand(File sourceDirectoryRoot){
		this.sourceDirectoryRoot = sourceDirectoryRoot;
	}
}
