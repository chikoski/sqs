package net.sqs2.omr.result.model;

import java.io.Serializable;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.session.logic.MarkAreaPreviewImageConstants;

public class FormMasterItem extends ModelItemMap implements Serializable{
	private static final long serialVersionUID = 0L;
	
	public FormMasterItem(FormMaster formMaster){
		this.put(Label.TEXT, formMaster.getRelativePath());
		this.put(Label.ICON, ImageFileName.PDF);
		this.put(Label.Master.NUM_PAGES, formMaster.getNumPages());
		this.put(Label.Master.MARK_AREA_MARGIN_HORIZONTAL, MarkAreaPreviewImageConstants.HORIZONTAL_MARGIN);
		this.put(Label.Master.MARK_AREA_MARGIN_VERTICAL, MarkAreaPreviewImageConstants.VERTICAL_MARGIN);
	}
}
