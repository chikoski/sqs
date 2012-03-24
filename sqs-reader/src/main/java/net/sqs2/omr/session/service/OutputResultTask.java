/**
 * 
 */
package net.sqs2.omr.session.service;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

import net.sqs2.omr.model.SessionSource;
import net.sqs2.omr.session.event.SpreadSheetOutputEventReciever;
import net.sqs2.omr.session.model.SessionStopException;
import net.sqs2.omr.session.output.OutputGenerator;
import net.sqs2.omr.session.output.OutputStopException;

public class OutputResultTask implements Callable<Void>{
	
	SessionSource sessionSource;
	OutputEventRecieversFactory resultEventConsumersFactory;
	
	public OutputResultTask(SessionSource sessionSource, OutputEventRecieversFactory resultEventConsumersFactory){
		this.sessionSource = sessionSource;
		this.resultEventConsumersFactory = resultEventConsumersFactory;
	}
	
	public Void call()throws SessionStopException{
		try{
			List<SpreadSheetOutputEventReciever> consumers = this.resultEventConsumersFactory.create();
			OutputGenerator resultVisitor = new OutputGenerator(this.sessionSource);
			for(SpreadSheetOutputEventReciever consumer: consumers){
				resultVisitor.addEventConsumer(consumer);
			}
			resultVisitor.call();
		}catch(OutputStopException ex){
			throw new SessionStopException(ex);
		}catch(IOException ex){
			throw new SessionStopException(ex);
		}
		return null;
	}
}