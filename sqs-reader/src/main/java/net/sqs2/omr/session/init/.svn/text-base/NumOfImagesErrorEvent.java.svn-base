package net.sqs2.omr.session.init;

import net.sqs2.omr.model.SpreadSheet;

public class NumOfImagesErrorEvent extends SessionSourceInitializationEvent{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	SpreadSheet spreadSheet;
	int numImages;
	
	public NumOfImagesErrorEvent(Object source, SpreadSheet spreadSheet, int numImages){
		super(source);
		this.spreadSheet = spreadSheet;
		this.numImages = numImages;
	}

	public SpreadSheet getSpreadSheet() {
		return spreadSheet;
	}

	public int getNumImages() {
		return numImages;
	}
	
}
