package net.sqs2.omr.session.service;

import java.io.IOException;
import java.rmi.RemoteException;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.SourceDirectoryConfiguration;
import net.sqs2.util.FileResourceID;

public interface SessionSourceServer {
	
	public abstract byte[] getFileContentByteArray(long sessionID,
			FileResourceID fileResourceID) throws RemoteException, IOException;

	public abstract FormMaster getFormMaster(long sessionID,
			FileResourceID fileResourceID) throws RemoteException;

	public abstract SourceDirectoryConfiguration getConfiguration(
			long sessionID, FileResourceID fileResourceID)
			throws RemoteException;

}
