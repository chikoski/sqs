package net.sqs2.omr.app;

import static org.testng.Assert.assertTrue;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.logging.Logger;

import net.sqs2.event.EventListener;
import net.sqs2.net.ClassURLStreamHandlerFactory;
import net.sqs2.omr.app.command.RemoveResultFoldersCommand;
import net.sqs2.omr.model.SessionSourceState;
import net.sqs2.omr.session.event.SessionEvent;
import net.sqs2.omr.session.service.MarkReaderSession;
import net.sqs2.omr.session.service.OutputEventRecieversFactory;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public abstract class AbstractMarkReaderSenarioTest {

	public static final File sourceDirectoryRoot0 = new File("src/test/resources/test0");
	public static final File sourceDirectoryRoot1 = new File("src/test/resources/test1");
	public static final File sourceDirectoryRoot2 = new File("src/test/resources/test2");
	public static final File sourceDirectoryRoot3 = new File("src/test/resources/test3");
	public static final File sourceDirectoryRoot4 = new File("src/test/resources/test4");
	
	static protected MarkReaderApp markReaderAppSingleton = null;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		try{
			URL.setURLStreamHandlerFactory(ClassURLStreamHandlerFactory.getSingleton());
		} catch (Error ignore) {
		}
		if(markReaderAppSingleton == null){
			markReaderAppSingleton = new MarkReaderApp(1099, true);
		}
	}
	
	@AfterClass
	public static void afterClass()throws Exception{
		if(markReaderAppSingleton != null){
			markReaderAppSingleton.shutdown();
			markReaderAppSingleton = null;
		}
		new RemoveResultFoldersCommand(sourceDirectoryRoot0).call();
		new RemoveResultFoldersCommand(sourceDirectoryRoot1).call();
		new RemoveResultFoldersCommand(sourceDirectoryRoot2).call();
		new RemoveResultFoldersCommand(sourceDirectoryRoot3).call();
		new RemoveResultFoldersCommand(sourceDirectoryRoot4).call();
	}

	public AbstractMarkReaderSenarioTest() {
		super();
	}

	protected void startAndCloseSession(File sourceDirectoryRoot) throws IOException, InterruptedException,
		ExecutionException {
		startSession(sourceDirectoryRoot);
		closeSessionSource(sourceDirectoryRoot);
	}

	protected void startSession(File sourceDirectoryRoot) throws IOException, InterruptedException,
	ExecutionException {
		OutputEventRecieversFactory factory = new OutputEventRecieversFactory(OutputEventRecieversFactory.Mode.BASE); 
		MarkReaderSession session = createSession(sourceDirectoryRoot, factory);
		session.startSession();
		Future<Boolean> future = createSessionServiceFinishFuture(session);
		boolean sessionHasStopped = future.get();
		assertTrue(sessionHasStopped);
	}

	protected synchronized void closeSessionSource(File sourceDirectoryRoot) throws IOException, InterruptedException,
	ExecutionException {
		if(markReaderAppSingleton == null){
			return;
		}
		markReaderAppSingleton.closeSessionSource(sourceDirectoryRoot);	
	}

	private synchronized MarkReaderSession createSession(File directory, OutputEventRecieversFactory factory) throws IOException {
		MarkReaderSession session;
		session = markReaderAppSingleton.createSession(directory, factory);
		session.getSessionEventSource().addListener(new EventListener<SessionEvent>() {
			@Override
			public void eventHappened(SessionEvent event) {
				if(event.isEnd()){
					Logger.getLogger(getClass().getName()).info("FINISHED!");
				}
			}
		});
		return session;
	}

	private static Future<Boolean> createSessionServiceFinishFuture(final MarkReaderSession session) {
		Future<Boolean> future = Executors.newSingleThreadExecutor().submit(new Callable<Boolean>(){
			public Boolean call(){
				try{
					for(int countDown = 300; 0 < countDown ; countDown--){
						try{
							Thread.sleep(1000);
						}catch(InterruptedException ignore){}
						SessionSourceState state = session.getSessionSourceState();
						if (state.equals(SessionSourceState.FINISHED)) {
							System.err.println(state);
							return Boolean.TRUE;
						}
					}
					return Boolean.FALSE;
				}finally{
				}
			}
		});
		return future;
	}

}