package net.sqs2.omr.source.config;

import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

import net.sqs2.xml.XMLFilterUtil;

public class ConfigXMLFileFilter {
	
	File target;
	String densityThreshold = null;
	String recognitionMargin = null;
	
	private static final String DENSITY_XPATH = "/config/sources/source/markRecognition/@density";
	private static final String RECOGNITON_MARGIN_XPATH = "/config/sources/source/markRecognition/@recognitionMargin";
	
	public ConfigXMLFileFilter(File target){
		this.target = target;
		try {
			String[] values = XMLFilterUtil.selectValues(this.target, new String[]{DENSITY_XPATH, RECOGNITON_MARGIN_XPATH});
			this.densityThreshold = values[0];
			this.recognitionMargin = values[1];
		} catch (IOException ex) {
			Logger.getAnonymousLogger().info(ex.getLocalizedMessage());
			this.densityThreshold = null;
			this.recognitionMargin = null;
		}
	}
	
	public void overwriteValues(float densityThreshold, float recognitionMargin){
		String[][] args = new String[][]{{DENSITY_XPATH, Float.toString(densityThreshold)},
				{RECOGNITON_MARGIN_XPATH, Float.toString(recognitionMargin)}};
		this.densityThreshold = args[0][1];
		this.recognitionMargin = args[1][1];
		XMLFilterUtil.overwrite(this.target, args);
	}
	
	public String getDensityThreshold(){
		return this.densityThreshold;
	}
	
	public String getRecognitionMargin(){
		return this.recognitionMargin;
	}
	
	/*
	public static void main(String[] args){
		ConfigXMLFileFilter self = new ConfigXMLFileFilter(new File("/tmp/config.xml"));
		self.overwriteDensityThreashold(0.99f);
	}*/
}
