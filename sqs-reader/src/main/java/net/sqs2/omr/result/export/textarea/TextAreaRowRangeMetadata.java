/**
 * 
 */
package net.sqs2.omr.result.export.textarea;

public class TextAreaRowRangeMetadata {
	private int rowRangeIndex;
	private int startRowIndex, endRowIndex;

	TextAreaRowRangeMetadata(int rowRangeIndex, int startRowIndex, int endRowIndex) {
		this.rowRangeIndex = rowRangeIndex; 
		this.startRowIndex = startRowIndex;
		this.endRowIndex = endRowIndex;
	}

	public int getRowRangeIndex() {
		return this.rowRangeIndex;
	}

	public int getStartRowIndex() {
		return this.startRowIndex;
	}

	public int getEndRowIndex() {
		return this.endRowIndex;
	}
}