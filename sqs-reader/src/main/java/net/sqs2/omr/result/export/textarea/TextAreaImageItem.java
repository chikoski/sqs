/**
 * 
 */
package net.sqs2.omr.result.export.textarea;

public class TextAreaImageItem {
	private String path;
	private int rowIndex;
	private String value;

	TextAreaImageItem(String path, int rowIndex, String value) {
		this.path = path;
		this.rowIndex = rowIndex;
		this.value = value;
	}

	@Override
	public String toString() {
		return "TextAreaImageItem[" + this.path + "," + this.rowIndex + "," + this.value + "]";
	}

	public String getPath() {
		return this.path;
	}

	public int getRowIndex() {
		return this.rowIndex;
	}

	public String getValue() {
		if (this.value == null) {
			return "";
		}
		return this.value;
	}
}