/**
 * 
 */
package net.sqs2.omr.result.export.model;

import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.model.SpreadSheet;

public class TextAreaTableCell extends FormAreaTableCell {

	TextAreaTableCell(SpreadSheet spreadSheet, SourceDirectory rowGroupSourceDirectory, int rowGroupRowIndex,
			int rowIndex, int columnIndex) {
		super(spreadSheet, rowGroupSourceDirectory, rowGroupRowIndex, rowIndex, columnIndex);
	}

}
