package net.sqs2.omr.result.export.model;

import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;

public class FormAreaTableCell {
	SpreadSheet spreadSheet;
	SourceDirectory rowGroupSourceDirectory;
	int rowGroupRowIndex;
	int rowIndex;
	int questionIndex;

	FormAreaTableCell(SpreadSheet spreadSheet, SourceDirectory rowGroupSourceDirectory, int rowGroupRowIndex,
			int rowIndex, int questionIndex) {
		this.spreadSheet = spreadSheet;
		this.rowGroupSourceDirectory = rowGroupSourceDirectory;
		this.rowGroupRowIndex = rowGroupRowIndex;
		this.rowIndex = rowIndex;
		this.questionIndex = questionIndex;
	}

	public SpreadSheet getSpreadSheet() {
		return this.spreadSheet;
	}

	public SourceDirectory getRowGroupSourceDirectory() {
		return this.rowGroupSourceDirectory;
	}

	public int getRowGroupRowIndex() {
		return this.rowGroupRowIndex;
	}

	public void setRowGroupRowIndex(int rowGroupRowIndex) {
		this.rowGroupRowIndex = rowGroupRowIndex;
	}

	public int getRowIndex() {
		return this.rowIndex;
	}

	public int getQuestionIndex() {
		return this.questionIndex;
	}

	@Override
	public int hashCode() {
		return this.spreadSheet.hashCode() + this.rowIndex
				* this.spreadSheet.getFormMaster().getNumPages() + this.questionIndex;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof TextAreaTableCell)) {
			return false;
		}
		FormAreaTableCell m = (FormAreaTableCell) o;

		return m.rowIndex == this.rowIndex && m.questionIndex == this.questionIndex
				&& m.spreadSheet == this.spreadSheet;
	}

	@Override
	public String toString(){
		return "FormAreaTableCell["+rowGroupRowIndex+","+rowIndex+","+questionIndex+"]";
	}

}
