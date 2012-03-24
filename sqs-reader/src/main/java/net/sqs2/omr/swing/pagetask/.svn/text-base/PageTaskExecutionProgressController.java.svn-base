package net.sqs2.omr.swing.pagetask;

import net.sqs2.omr.session.service.PageTaskExecutionProgressModel;

public class PageTaskExecutionProgressController {
	PageTaskExecutionProgressBar bar;
	PageTaskExecutionProgressMeter meter;
	PageTaskExecutionProgressModel model;

	public PageTaskExecutionProgressController(final PageTaskExecutionProgressModel model,  
			final PageTaskExecutionProgressBar bar, final PageTaskExecutionProgressMeter meter){
		this.model = model;
		this.bar = bar;
		this.meter = meter;
		/*
		model.addSessionEventListener(new SessionEventAdapter() {
			
			@Override
			public void notifySessionStarted(File sourceDirectoryRootFile) {
				super.notifySessionStarted(sourceDirectoryRootFile);
				if(model.getSessionTimer() != null){
					model.getSessionTimer().cancel(true);
				}
				model.sessionTimer = new ScheduledThreadPoolExecutor(1).scheduleAtFixedRate(new Runnable(){
					public void run(){
						model.update();
					}
				}, 1, 1, TimeUnit.SECONDS);
			}

			@Override
			public void notifySessionFinished(PageTaskExecutionProgressModel model) {
				super.notifySessionFinished(model);
				if(model.getSessionTimer() != null){
					model.sessionTimer.cancel(true);
				}
			}

			@Override
			public void notifySessionStopped(File sourceDirectoryRootFile) {
				super.notifySessionStopped(sourceDirectoryRootFile);
				if(model.sessionTimer != null){
					model.sessionTimer.cancel(true);
				}
			}
		});
		
		model.addPageTaskProducedEventListener(new PageTaskProducedEventListener() {
			@Override
			public void notifyPageTaskProduced(PageNumberedPageTask pageTask) {
				model.update();
			}

			@Override
			public void notifyErrorPageTaskReproduced(PageNumberedPageTask storedTask) {
				// TODO Auto-generated method stub
				
			}
		});
		
		model.addPageTaskConsumedEventListener(new PageTaskConsumedEventListener() {
			@Override
			public void notifyStoreTask(PageNumberedPageTask pageTask) {
				SoundManager.getInstance().play("pyoro37_b.wav");
				model.update();
			}
		});
		 */
	}
	
}
