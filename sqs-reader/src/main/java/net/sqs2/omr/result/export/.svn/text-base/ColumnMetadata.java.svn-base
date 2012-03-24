/**
 * 
 */
package net.sqs2.omr.result.export;

import net.sqs2.omr.master.FormArea;

public class ColumnMetadata {
	protected FormArea primaryFormArea;
	protected String sourceDirectoryPath;
	protected String rowDirectoryPath;
	protected int questionIndex;
	//protected int numRows;

	public ColumnMetadata(FormArea primaryFormArea, String sourceDirectoryPath, String rowDirectoryPath,
			int questionIndex
			/*, int numRows*/
			) {
		this.primaryFormArea = primaryFormArea;
		this.sourceDirectoryPath = sourceDirectoryPath;
		this.rowDirectoryPath = rowDirectoryPath;
		this.questionIndex = questionIndex;
		//this.numRows = numRows;
	}

	public FormArea getPrimaryFormArea() {
		return this.primaryFormArea;
	}

	public void setSourceDirectoryPath(String sourceDirectoryPath) {
		this.sourceDirectoryPath = sourceDirectoryPath;
	}

	public String getSourceDirectoryPath() {
		return this.sourceDirectoryPath;
	}

	public String getRowDirectoryPath() {
		return this.rowDirectoryPath;
	}

	public int getQuestionIndex() {
		return this.questionIndex;
	}

	/*
	public int getNumRows() {
		return this.numRows;
	}
	*/

	public int getPage() {
		return this.primaryFormArea.getPage();
	}
}