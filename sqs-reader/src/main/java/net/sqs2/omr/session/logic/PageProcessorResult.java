/**
 * 
 */
package net.sqs2.omr.session.logic;

import net.sqs2.omr.app.deskew.DeskewedImageSource;
import net.sqs2.omr.app.deskew.ExtractedDeskewGuides;

class PageProcessorResult{
	private DeskewedImageSource deskewedImageSource;
	private ExtractedDeskewGuides extractedDeskewGuides;
	
	
	private PageProcessorResult(DeskewedImageSource deskewedImageSource,
			ExtractedDeskewGuides extractedDeskewGuides){
		this.deskewedImageSource = deskewedImageSource;
		this.extractedDeskewGuides = extractedDeskewGuides;
	}

	public DeskewedImageSource getDeskewedImageSource() {
		return deskewedImageSource;
	}

	public ExtractedDeskewGuides getExtractedDeskewGuides() {
		return extractedDeskewGuides;
	}
	
}