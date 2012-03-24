/*
 * 

 Session.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
package net.sqs2.omr.session;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.app.NetworkPeer;
import net.sqs2.omr.master.PageMaster;
import net.sqs2.omr.master.PageMasterAccessor;
import net.sqs2.omr.master.PageMasterException;
import net.sqs2.omr.session.event.MarkReaderSessionMonitor;
import net.sqs2.omr.source.GenericSourceConfig;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceDirectory;
import net.sqs2.omr.source.SourceDirectoryConfiguration;
import net.sqs2.omr.swing.SessionProgressModel;
import net.sqs2.omr.task.PageTask;
import net.sqs2.omr.task.TaskAccessor;
import net.sqs2.omr.task.TaskHolder;
import net.sqs2.omr.task.broker.TaskExecutorEnv;
import net.sqs2.omr.task.consumer.TaskConsumer;
import net.sqs2.omr.task.consumer.TaskConsumerRowGenerator;
import net.sqs2.util.FileUtil;
import edu.emory.mathcs.backport.java.util.Collections;

public abstract class Session implements Runnable,MarkReaderSessionMonitor{

	protected File sourceDirectoryRootFile;
	protected TaskConsumer taskConsumer;

	protected TaskExecutorEnv localTaskExecutorEnv;
	protected long sessionID = -1;
	
	public enum STATE{NOT_INITIALIZED, INITIALIZED, STARTED, STOPPED, FINISHED}
	
	public STATE state = STATE.NOT_INITIALIZED;
	
	protected SessionProgressModel model;
	protected TaskHolder taskHolder;

	protected Set<MarkReaderSessionMonitor> monitors;
	
	protected Session(File sourceDirectoryRoot, NetworkPeer peer, TaskExecutorEnv localTaskExecutorEnv) throws IOException{
		this.localTaskExecutorEnv = localTaskExecutorEnv;
		this.sessionID = System.currentTimeMillis();
		this.taskConsumer = (TaskConsumer) new TaskConsumerRowGenerator(this);
		this.sourceDirectoryRootFile = sourceDirectoryRoot;
		this.taskHolder = new TaskHolder();
		this.model = new SessionProgressModel(sourceDirectoryRoot, taskHolder);
		this.monitors = (Set<MarkReaderSessionMonitor>)Collections.synchronizedSet(new HashSet<MarkReaderSessionMonitor>(1));
		this.state = STATE.INITIALIZED;
		
		addSessionMonitor(this.model);
	}
	
	public SessionSource getSessionSource(){
		return SessionSources.get(this.sessionID);
	}
	
	public SessionProgressModel getSessionProgressModel(){
		return this.model;
	}
	
	public long getKey() {
		return this.localTaskExecutorEnv.getKey();
	}
	
	public void resetCache(final File sourceDirectoryRoot) {
		try{
			PageMasterAccessor pageMasterAccessor = new PageMasterAccessor(sourceDirectoryRoot);
			if(pageMasterAccessor != null){
				pageMasterAccessor.removeAll();
			}
			this.taskHolder.reset();
		}catch(IOException ignore){}
		try{
			TaskAccessor taskAccessor = new TaskAccessor(sourceDirectoryRoot);
			if(taskAccessor != null){
				taskAccessor.removeAll();
			}
		}catch(IOException ignore){
		}
	}
	
	public long getSessionID(){
		return this.sessionID;
	}

	public void createConfigFileIfNotExists() throws MalformedURLException{
	    File configFile = new File(this.sourceDirectoryRootFile, GenericSourceConfig.SOURCE_CONFIG_FILENAME);
	    createConfigFile(configFile);
    }
	
	public static void createConfigFile(File configFile) throws MalformedURLException{
		if(! configFile.exists()){
			try{
				InputStream in = createDefaultConfigFileInputStream();
				OutputStream out = new BufferedOutputStream(new FileOutputStream(configFile));
				FileUtil.pipe(in, out);
			}catch(IOException ignore){
			}
		}
    }
	
	private static InputStream createDefaultConfigFileInputStream()throws IOException{
		if(MarkReaderConstants.USER_CUSTOMIZE_DEFAULT_CONFIG_FILE.exists()){
			return new BufferedInputStream(new FileInputStream(MarkReaderConstants.USER_CUSTOMIZE_DEFAULT_CONFIG_FILE));
		}else{
			return Session.class.getClassLoader().getResourceAsStream(GenericSourceConfig.SOURCE_CONFIG_FILENAME);
		}
	}

	public TaskHolder getTaskHolder() {
		return this.taskHolder;
	}
	
	public boolean isNew(){
		return this.state == STATE.INITIALIZED;
	}

	public boolean isStoppedOrFinished(){
		return this.state == STATE.STOPPED || this.state == STATE.FINISHED;
	}
	
	public synchronized void startSession()throws IOException{
		this.taskConsumer.setup(this.sourceDirectoryRootFile);
		new Thread(this).start();
		this.state = STATE.STARTED;
		notifySessionStarted(this.sourceDirectoryRootFile);
	}
	
	public synchronized void stopSession() {
		this.state = STATE.STOPPED;
		this.notify();
		notifySessionStopped(this.sourceDirectoryRootFile);
	}

	public synchronized void exportSession(){
		notifySessionExport(this.sourceDirectoryRootFile);
	}
	
	public synchronized void exportResult(){
		notifySessionResult(this.sourceDirectoryRootFile);
	}
	
	public synchronized void finishSession(){
		notifySessionFinished(this.sourceDirectoryRootFile);
	}

	public synchronized void addSessionMonitor(MarkReaderSessionMonitor monitor) {
		this.monitors.add(monitor);
	}

	public synchronized void removeSessionMonitor(MarkReaderSessionMonitor monitor) {
		this.monitors.remove(monitor);
	}

	public synchronized void deleteSessionMonitors() {
		this.monitors.clear();
	}
	
	public File getSourceDirectoryRootFile(){
		return this.sourceDirectoryRootFile;
	}

	public synchronized void notifySessionStarted(File sourceDirectoryRootFile) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifySessionStarted(sourceDirectoryRootFile);
		}
	}
	public synchronized void notifySessionStopped(File sourceDirectoryRootFile) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifySessionStopped(sourceDirectoryRootFile);
		}
	}
	public synchronized void notifySessionExport(File sourceDirectoryRootFile) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifySessionExport(sourceDirectoryRootFile);
		}
	}
	public synchronized void notifySessionResult(File sourceDirectoryRootFile) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifySessionResult(sourceDirectoryRootFile);
		}
	}
	public synchronized void notifySessionFinished(File sourceDirectoryRootFile) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifySessionFinished(sourceDirectoryRootFile);
		}
	}

	public synchronized void notifyTaskProduced(PageTask pageTask) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyTaskProduced(pageTask);
		}
	}
	
	public synchronized void notifyErrorTaskReproduced(PageTask storedTask){
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyErrorTaskReproduced(storedTask);
		}
	}

	public synchronized void notifyStoreTask(PageTask pageTask) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyStoreTask(pageTask);
		}
	}

	public synchronized void notifyFoundMaster(PageMaster master) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyFoundMaster(master);
		}
	}

	public synchronized void notifyFoundConfig(SourceDirectoryConfiguration config) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyFoundConfig(config);
		}
	}

	public synchronized void notifyFoundImages(int numAddedImages, File sourceDirectory) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyFoundImages(numAddedImages, sourceDirectory);
		}
	}
	
	public synchronized void notifyScanDirectory(File subSourceDirectory){
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyScanDirectory(subSourceDirectory);
		}	
	}

	public synchronized void notifySourceInitializeDone(File sourceDirectoryRootFile) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifySourceInitializeDone(sourceDirectoryRootFile);
		}
	}
	
	public void notifyErrorDirectoryUnreadable(SourceDirectory sourceDirectory) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyErrorDirectoryUnreadable(sourceDirectory);
		}
	}



	public synchronized void notifyErrorNumOfImages(SourceDirectory sourceDirectory, int numImages){
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyErrorNumOfImages(sourceDirectory, numImages);
		}
	}
	
	public void notifyErrorOnPageMaster(PageMasterException ex) {
		for(MarkReaderSessionMonitor monitor: this.monitors){
			monitor.notifyErrorOnPageMaster(ex);
		}
	}


}
