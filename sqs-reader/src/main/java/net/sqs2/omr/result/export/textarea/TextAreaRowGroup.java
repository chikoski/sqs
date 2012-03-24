package net.sqs2.omr.result.export.textarea;

import java.util.ArrayList;
import java.util.List;

import net.sqs2.omr.model.SourceDirectory;

public class TextAreaRowGroup {
	private SourceDirectory sourceDirectory;
	private int rowIndexBaseOfThisRowGroup;
	private List<TextAreaRowRange> textAreaRowRangeList;

	public TextAreaRowGroup(SourceDirectory sourceDirectory, int rowIndexBaseOfThisRowGroup){
		this.sourceDirectory = sourceDirectory;
		this.rowIndexBaseOfThisRowGroup = rowIndexBaseOfThisRowGroup;
		this.textAreaRowRangeList = new ArrayList<TextAreaRowRange>();
	}

	public SourceDirectory getSourceDirectory() {
		return sourceDirectory;
	}
	
	public int getRowIndexBaseOfThisRowGroup() {
		return rowIndexBaseOfThisRowGroup;
	}
	
	public TextAreaRowRange getLastTextAreaRowRange() {
		return this.textAreaRowRangeList.get(this.textAreaRowRangeList.size()-1);
	}

	public TextAreaRowRange getTextAreaRowRange(int index) {
		return this.textAreaRowRangeList.get(index);
	}

	public List<TextAreaRowRange> getTextAreaRowRangeList() {
		return this.textAreaRowRangeList;
	}

	public int getTextAreaRowRangeSize() {
		return this.textAreaRowRangeList.size();
	}

	public void addTextAreaRowRange(TextAreaRowRange textAreaRowRange) {
		this.textAreaRowRangeList.add(textAreaRowRange);
	}

	public void clearTextAreaRowRangeList() {
		this.textAreaRowRangeList.clear();
	}


}
