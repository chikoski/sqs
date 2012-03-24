/**
 * 
 */
package net.sqs2.omr.session.logic;

import java.awt.image.BufferedImage;

import java.io.IOException;

import net.sqs2.image.ImageFactory;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.OMRPageTask;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.SourceDirectoryConfiguration;

public class PageProcessorSource{
	OMRPageTask pageTask;
	FormMaster formMaster;
	int processingPageNumber;
	SourceDirectoryConfiguration configuration;
	BufferedImage image;

	public PageProcessorSource(
			OMRPageTask pageTask, FormMaster formMaster, int processingPageNumber,
			SourceDirectoryConfiguration configuration, byte[] imageByteArray)throws IOException{
		this.pageTask = pageTask;
		this.formMaster = formMaster;
		this.processingPageNumber = processingPageNumber;
		this.configuration = configuration;
		this.image = createBufferedImage(imageByteArray);
	}

	public OMRPageTask getPageTask() {
		return pageTask;
	}

	public FormMaster getFormMaster() {
		return formMaster;
	}

	public int getProcessingPageNumber() {
		return processingPageNumber;
	}

	public SourceDirectoryConfiguration getConfiguration() {
		return configuration;
	}
	
	public BufferedImage getPageImage(){
		return image;
	}
	
	private BufferedImage createBufferedImage(byte[] imageByteArray) throws IOException {
		PageID pageID = this.pageTask.getPageID();
		String type = pageID.getExtension();
		if (imageByteArray== null) {
			throw new RuntimeException(pageID.toString());
		}
		BufferedImage pageImage = ImageFactory.createImage(type, imageByteArray, pageID.getIndexInFile());
		return pageImage;
	}


}