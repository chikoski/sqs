package net.sqs2.omr.session;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.CancellationException;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import net.sqs2.net.MulticastNetworkConnection;
import net.sqs2.net.RMIRegistryMulticastAdvertisingService;
import net.sqs2.omr.MarkReaderJarURIContext;
import net.sqs2.omr.app.App;
import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.app.NetworkPeer;
import net.sqs2.omr.master.FormMasterFactories;
import net.sqs2.omr.master.PageMasterException;
import net.sqs2.omr.master.PageMasterFactory;
import net.sqs2.omr.result.event.SpreadSheetExportEventProducer;
import net.sqs2.omr.result.export.CSVExportModule;
import net.sqs2.omr.result.export.ExcelExportModule;
import net.sqs2.omr.result.export.HTMLReportExportModule;
import net.sqs2.omr.session.event.MarkReaderSessionMonitor;
import net.sqs2.omr.source.ConfigHandlerFactory;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SessionSourceException;
import net.sqs2.omr.source.SessionSources;
import net.sqs2.omr.source.SourceInitializationMonitor;
import net.sqs2.omr.source.config.ConfigHandlerFactoryImpl;
import net.sqs2.omr.task.TaskException;
import net.sqs2.omr.task.broker.TaskExecutorEnv;
import net.sqs2.omr.task.producer.SessionSourceScannerTaskGenerator;
import net.sqs2.sound.SoundManager;

public class MarkReaderSession extends Session{
	
	static private boolean isExportTextAreaImageEnabled = false;
	static private boolean isExportChartImageEnabled = false;
	static private boolean isExportSpreadSheetEnabled = false;
	static private boolean isOpenResultBrowserEnabled = false;
	static private boolean isSearchPageMasterFromAncestorDirectory = false;
	
	protected RMIRegistryMulticastAdvertisingService advertisingService;
	FormMasterFactories pageMasterFactory;
	ConfigHandlerFactory configHandlerFactory;
	SourceInitializationMonitor sessionSourceInitializerMonitor;
	SpreadSheetExportEventProducer sessionExportEventProducer;

	private SessionThreadManager sessionThreadManager;

	private Fanfare fanfare;
	private TaskExecutorEnv localTaskExecutorEnv;
	private Future sessionFuture;
	
	class MultiFormMasterFactory{
		PageMasterFactory sqmFileMasterFactory;
		PageMasterFactory pdfAttachmentMasterFactory;
		PageMasterFactory pdfBookmarkMasterFactory;
	}

	protected MarkReaderSession(File sourceDirectoryRoot, NetworkPeer peer, TaskExecutorEnv localTaskExecutorEnv, SessionThreadManager sessionThreadManager) throws IOException{
		super(sourceDirectoryRoot, peer, localTaskExecutorEnv);
		this.sessionThreadManager = sessionThreadManager;
		this.fanfare = new Fanfare(new URL(MarkReaderJarURIContext.getSoundBaseURI()+MarkReaderConstants.SESSION_START_FANFARE_SOUND_FILENAME));
		this.pageMasterFactory = (FormMasterFactories) new FormMasterFactories();//Class.forName(getPageMasterFactoryClassName()).newInstance();
        this.configHandlerFactory = (ConfigHandlerFactory) new ConfigHandlerFactoryImpl();//Class.forName(getConfigHandlerFactoryClassName()).newInstance(); 
		this.advertisingService = createAdvertisingService(peer, localTaskExecutorEnv.getKey());
		this.localTaskExecutorEnv = localTaskExecutorEnv;
	}
	
	private RMIRegistryMulticastAdvertisingService createAdvertisingService(NetworkPeer peer, long key){
		MulticastNetworkConnection multicastNetworkConnection = peer.getMulticastNetworkConnection();
		int rmiPort = peer.getRMIPort();
        if (multicastNetworkConnection != null) {
        	try{
        		return new RMIRegistryMulticastAdvertisingService(
        				multicastNetworkConnection, key, this.sessionID,
        				MarkReaderConstants.ADVERTISE_SERVICE_THREAD_PRIORITY, rmiPort,
        				MarkReaderConstants.SESSION_SOURCE_ADVERTISE_DELAY_IN_SEC);
        	}catch(IOException ignore){}
        }
        return null;        
	}
	
	public void startAdvertisement(){
		try{
			if(this.advertisingService != null){
				this.advertisingService.export(SessionServiceImpl.getInstance(), MarkReaderConstants.SESSION_SERVICE_PATH);
			}
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	
	public void stopAdvertisement(){
		if(this.advertisingService != null){
			this.advertisingService.stop();
		}

		if(this.advertisingService != null){
			this.advertisingService.unexport(MarkReaderConstants.SESSION_SERVICE_PATH);
		}
		this.advertisingService = null;
	}

	class Fanfare{
		private URL whistleSoundURL;
		
		Fanfare(URL whistleSoundURL){
			this.whistleSoundURL = whistleSoundURL;
		}
		
		public void startFanfare() {
			Logger.getLogger("session").info("*********** START ************");
			SoundManager.getInstance().play(whistleSoundURL);
		}

		public void finishFanfare() {
			Logger.getLogger("session").info("********* FINISHED ***********");
			SoundManager.getInstance().play(whistleSoundURL);
			sleep(500);
			SoundManager.getInstance().play(whistleSoundURL);
			sleep(500);
			SoundManager.getInstance().play(whistleSoundURL);
		}

		private void sleep(int time) {
			try{
				Thread.sleep(time);
			}catch(InterruptedException ignore){}
		}
	}
	
	public void run() {
		
		if(this.sourceDirectoryRootFile == null){
			throw new RuntimeException("this.sourceDirectoryRoot == null");
		}

		this.fanfare.startFanfare();

		try {

			createConfigFileIfNotExists();
			
			SessionSource sessionSource = SessionSources.create(this.sessionID, this.sourceDirectoryRootFile,
						this.pageMasterFactory, 
						isSearchPageMasterFromAncestorDirectory,
						this.configHandlerFactory, (SourceInitializationMonitor)this);
			
			SessionSourceScannerTaskGenerator taskGenerator = new SessionSourceScannerTaskGenerator(sessionSource, (MarkReaderSessionMonitor)this,
					MarkReaderConstants.SESSION_SOURCE_NEWFILE_IGNORE_THRESHOLD_IN_SEC, this.taskHolder);
			
			this.sessionFuture = this.sessionThreadManager.startTaskProducer(taskGenerator);
			this.sessionExportEventProducer = new SpreadSheetExportEventProducer(sessionSource);
			
			this.sessionFuture.get();
			this.localTaskExecutorEnv.setInitialized();
			
			this.sessionFuture = this.sessionThreadManager.startAndWaitTaskConsumer(this.taskConsumer);
			this.sessionFuture.get();
		}catch(PageMasterException ex){
			notifyErrorOnPageMaster(ex);
		}catch(SessionSourceException ignore){
		}catch(CancellationException ignore){
		} catch (IOException ex){
			ex.printStackTrace();
		}catch(InterruptedException ignore){
			ignore.printStackTrace();
		}catch(TaskException ignore){
			ignore.printStackTrace();
		}catch(ExecutionException ignore){
			ignore.printStackTrace();
		}
	}
	
	public synchronized void startSession()throws IOException{
		startAdvertisement();
		super.startSession();
	}
	
	public synchronized void stopSession(){
		super.stopSession();
		this.sessionThreadManager.stop();
		if(this.sessionFuture != null){
			this.sessionFuture.cancel(true);
		}
	}
	
	public static boolean isExportSpreadSheetEnabled() {
		return MarkReaderSession.isExportSpreadSheetEnabled;
	}

	public static boolean isExportChartImageEnabled() {
		return MarkReaderSession.isExportChartImageEnabled;
	}

	public static boolean isExportTextAreaImageEnabled() {
		return MarkReaderSession.isExportTextAreaImageEnabled;
	}	
	
	public static boolean isOpenResultBrowserEnabled() {
		return MarkReaderSession.isOpenResultBrowserEnabled;
	}
	
	public static boolean isSearchPageMasterFromAncestorDirectory(){
		return MarkReaderSession.isSearchPageMasterFromAncestorDirectory;
	}

	public static void setExportTextAreaImageEnabled(boolean isExportTextAreaImageEnabled) {
		MarkReaderSession.isExportTextAreaImageEnabled = isExportTextAreaImageEnabled;
	}
	
	public static void setExportChartImageEnabled(boolean isExportChartImageEnabled) {
		MarkReaderSession.isExportChartImageEnabled = isExportChartImageEnabled;
	}
	
	public static void setExportSpreadSheetEnabled(boolean isExportSpreadSheetEnabled) {
		MarkReaderSession.isExportSpreadSheetEnabled = isExportSpreadSheetEnabled;
	}
	
	public static void setOpenResultBrowserEnabled(boolean isOpenResultBrowserEnabled) {
		MarkReaderSession.isOpenResultBrowserEnabled = isOpenResultBrowserEnabled;
	}
	
	public static void setSearchPageMasterFromAncestorDirectory(boolean isSearchPageMasterFromAncestorDirectory){
		MarkReaderSession.isSearchPageMasterFromAncestorDirectory = isSearchPageMasterFromAncestorDirectory;
	}


	public void exportSession(){
		super.exportSession();

		if(MarkReaderSession.isExportSpreadSheetEnabled){
	   		sessionExportEventProducer.addEventConsumer(new CSVExportModule());
	   		sessionExportEventProducer.addEventConsumer(new ExcelExportModule());
	   	}
		if(MarkReaderSession.isExportTextAreaImageEnabled || MarkReaderSession.isExportChartImageEnabled){
			sessionExportEventProducer.addEventConsumer(new HTMLReportExportModule(App.SKIN_ID));
		}
   		//sessionExportEventProducer.addEventConsumer(new DummyExportModule());
		sessionExportEventProducer.produceSessionEvents();
	}
	
	public void finishSession(){
		super.finishSession();
		stopAdvertisement();	
		this.fanfare.finishFanfare();
	}
	
	public SpreadSheetExportEventProducer getSpreadSheetExportEventProducer(){
		if(this.sessionExportEventProducer == null){
			throw new RuntimeException("not finished session.");
		}
		return this.sessionExportEventProducer;
	}

}
