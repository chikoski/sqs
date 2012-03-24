/**
 * TaskBroker.java

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

 Created on 2007/01/31
 Author hiroya
 */

package net.sqs2.omr.task.broker;

import java.rmi.RemoteException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import net.sqs2.lang.GroupThreadFactory;
import net.sqs2.omr.task.ExecutableTaskWrapper;
import net.sqs2.omr.task.PageTask;

public class TaskBroker{

	public static final int PAGETASK_EXECUTOR_THREAD_PRIORIY = Thread.NORM_PRIORITY - 1;
	
	private String name;
	private TaskExecutorPeer taskExecutorPeer = null;
	private TaskExecutorEnv taskExecutorEnv;
	
	private ScheduledExecutorService downloaderThreadPool;
	private Future<?>[] downloaderFutures = null;

	private boolean isConnected = false;

	public TaskBroker() {
		super();
	}

	public TaskBroker(String name, TaskExecutorPeer taskExecutorPeer, TaskExecutorEnv taskExecutorEnv){
		this.name = name;
		this.taskExecutorPeer = taskExecutorPeer;
		this.taskExecutorEnv = taskExecutorEnv;
		start();
	}
	
	public void start(){
		setConnected(true);
		int numDownloaderThreads = 1;
		GroupThreadFactory groupThreadFactory = new GroupThreadFactory(this.name+":TaskDownloaderThread", 
				PAGETASK_EXECUTOR_THREAD_PRIORIY, true);
		if(this.downloaderThreadPool == null){
			this.downloaderThreadPool = Executors.newScheduledThreadPool(numDownloaderThreads, groupThreadFactory);
		}
		if(this.downloaderFutures == null){
			this.downloaderFutures = new Future[numDownloaderThreads];
			for(int i = 0; i < numDownloaderThreads; i++){
				this.downloaderFutures[i] = this.downloaderThreadPool.scheduleWithFixedDelay(new PageTaskDownloader(this.taskExecutorEnv),
						200 * i,
						100,
						TimeUnit.MILLISECONDS);
			}
		}
		this.taskExecutorPeer.start();
	}
	
	public void stop(){
		setConnected(false);
		if(this.downloaderFutures != null){
			for(int i = 0; i < this.downloaderFutures.length; i++){
				if(this.downloaderFutures[i] != null){
					this.downloaderFutures[i].cancel(true);
				}
			}
			this.downloaderFutures = null;
		}
		if(this.downloaderThreadPool != null){
			this.downloaderThreadPool.shutdown();
			this.downloaderThreadPool = null;
		}
	}
	
	public long getKey(){
		return this.taskExecutorEnv.getKey();
	}
	
	public boolean isConnected(){
		try{
			this.taskExecutorEnv.getSessionService().ping(this.taskExecutorEnv.getKey());
			return this.isConnected;
		}catch(RemoteException ignore){
			stop();
			return false;
		}
	}

	public void setConnected(boolean isConnected){
		this.isConnected = isConnected;
	}

	public void shutdown(){
		stop();
		this.taskExecutorPeer.stop();
		if(this.taskExecutorEnv != null){
			this.taskExecutorEnv.close();
			this.taskExecutorEnv = null;
		}
	}
	
	class PageTaskDownloader implements Runnable{
		TaskExecutorEnv executorEnv;
		PageTaskDownloader(TaskExecutorEnv executorEnv){
			this.executorEnv = executorEnv;
		}
		
		public void run(){
			try{
				if(this.executorEnv.isRemote()){
					// remote session
					if(this.executorEnv.getLocalSessionService().existsRunningLocalSessions()){
						sleep(1000);
						return;
					}
				}else{
					// local session
					if( ! this.executorEnv.hasInitialized()){
						sleep(1000);
						return;
						/*
					}else if(0 < this.executorEnv.getSessionService().getNumRemoteSlaveExecutors()){
						sleep(100);
						return;*/
					}
				}
				if(! leaseTask()){
					return;
				}
			}catch(Exception ex){
				Logger.getAnonymousLogger().warning("DISCONNECTED");
				setConnected(false);
				stop();
				ex.printStackTrace();
			}
		}

		private boolean leaseTask() {
			try{
				PageTask pageTask = this.executorEnv.getSessionService().leaseTask(this.executorEnv.getKey()); // may block
				if(pageTask == null){
					sleep(100);
					return false;
				}
				
				boolean offer = false;
				do{
					try{
						offer = taskExecutorPeer.getDownloadedTaskQueue().offer(new ExecutableTaskWrapper(pageTask, this.executorEnv), 100, TimeUnit.MILLISECONDS);
					}catch(InterruptedException ignore){
					}catch(ClassCastException ignore){
						ignore.printStackTrace();
					}catch(NullPointerException ignore){
						ignore.printStackTrace();
					}catch(IllegalArgumentException ignore){
						ignore.printStackTrace();
					}
				}while(! offer);
			}catch(RemoteException ignore){
				//ignore.printStackTrace();
				Logger.getLogger("executor").warning("RemoteSession closed.");
				setConnected(false);
				stop();
				sleep(1000);
			}
			return true;
		}

		private void sleep(int msec) {
	        try{
	        	Thread.sleep(msec);
	        }catch(InterruptedException ignore){}
        }
	}

}
