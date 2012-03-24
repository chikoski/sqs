package net.sqs2.omr.app.command;

import java.net.MalformedURLException;
import java.net.URL;

import net.sqs2.omr.base.MarkReaderJarURIContext;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.omr.sound.SessionFanfare;

public class FinishSessionCommand {
	SessionFanfare fanfare = null;
	public FinishSessionCommand(){
		try{
			this.fanfare = new SessionFanfare(new URL(MarkReaderJarURIContext.getSoundBaseURI()
					+ AppConstants.SESSION_START_FANFARE_SOUND_FILENAME));
		}catch(MalformedURLException ex){
			ex.printStackTrace();
		}
	}
	public void call(){
		if(this.fanfare != null){
			this.fanfare.finishFanfare();
		}
	}
}
