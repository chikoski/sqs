package net.sqs2.omr.result.model;

import java.io.File;
import java.io.Serializable;

import net.sqs2.omr.model.SourceConfig;
import net.sqs2.omr.model.SourceDirectory;

public class SpreadSheetItem extends ModelItemMap implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	
	public SpreadSheetItem(SourceDirectory sourceDirectory){
		if (sourceDirectory.isLeaf()) {
			this.put(Label.SpreadSheet.ICON, ImageFileName.LEAF_DIRECTORY);
		} else {
			this.put(Label.SpreadSheet.ICON, ImageFileName.BRANCH_DIRECTORY);
		}
		this.put(Label.SpreadSheet.TEXT, File.separatorChar + sourceDirectory.getRelativePath());
		float densityThreshold = ((SourceConfig)sourceDirectory.getConfiguration().getConfig().getPrimarySourceConfig())
				.getMarkRecognitionConfig().getMarkRecognitionDensityThreshold();
		this.put(Label.SpreadSheet.DENSITY_THRESHOLD, densityThreshold);
		if (!sourceDirectory.isLeaf()) {
			this.put(Label.DISABLED, Boolean.TRUE); // FIXME! remove this line when item selection relation has implemented
		}
	}
}
