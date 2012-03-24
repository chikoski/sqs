package net.sqs2.omr.session.service;

import java.rmi.RemoteException;

import net.sqs2.omr.model.Ticket;

public abstract class AbstractExecutable<T extends Ticket, D extends ServerDispatcher>{

	protected T task;
	protected D dispatcher;

	public AbstractExecutable(T task, D dispatcher){
		this.task = task;
		this.dispatcher = dispatcher;
	}

	public T getTask() {
		return this.task;
	}

	public D getDispatcher() {
		return this.dispatcher;
	}
	
	public abstract void execute() throws RemoteException;
	public abstract void emit() throws RemoteException;

}