package net.sqs2.omr.session.service;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.omr.util.FileContents;
import net.sqs2.util.FileResourceID;

public interface LocalSessionSourceServer extends Remote{

	public boolean existsRunningLocalSessions()throws RemoteException;
	
	public long ping(long key) throws RemoteException;

	public OMRPageTask leaseTask(long key) throws RemoteException;

	public FormMaster getFormMaster(long key, long sessionID, FileResourceID fileResourceID) throws RemoteException;

	public SourceDirectoryConfiguration getConfigration(long key, long sessionID, FileResourceID fileResourceID) throws RemoteException;

	public FileContents getFileContentByteArray(long key, long sessionID, FileResourceID fileResourceID) throws RemoteException, IOException;

	public void submitPageTask(long key, long sessionID, OMRPageTask pageTask) throws RemoteException;
}
