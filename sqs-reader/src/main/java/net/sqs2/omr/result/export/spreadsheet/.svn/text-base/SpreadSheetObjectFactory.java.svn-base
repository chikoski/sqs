/**
 * 
 */
package net.sqs2.omr.result.export.spreadsheet;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;

interface SpreadSheetObjectFactory{
	public Workbook createWorkbook();
	public String getSuffix();
	public RichTextString createRichTextString(String value);
	public CellStyle getSelect1CellStyle();
	public CellStyle getSelectCellStyle();
	public CellStyle getErrorCellStyle();
	public CellStyle getTextAreaCellStyle();
	public CellStyle getNoAnswerCellStyle();
	public CellStyle getMultipleAnswersCellStyle();
}