/**
 * 
 */
package net.sqs2.omr.result.export.spreadsheet;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRichTextString;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

class XSSFObjectFactory extends AbstractSpreadSheetObjectFactory implements SpreadSheetObjectFactory{

	/**
	 * @param excelExportModule
	 */
	XSSFObjectFactory(ExcelExportModule excelExportModule) {
		super(excelExportModule);
	}
	
	public Workbook createWorkbook(){
		return new XSSFWorkbook();
	}
	
	public String getSuffix(){
		return "xlsx";
	}
	
	public RichTextString createRichTextString(String value){
		return new XSSFRichTextString(value);
	}
	
	public CellStyle getSelect1CellStyle(){
		return getCellStyle(HSSFColor.BLACK.index, HSSFColor.WHITE.index);
	}
	
	public CellStyle getSelectCellStyle(){
		return getCellStyle(HSSFColor.DARK_BLUE.index, HSSFColor.WHITE.index);
	}
	
	public CellStyle getErrorCellStyle(){
		return getCellStyle(HSSFColor.RED.index, HSSFColor.WHITE.index);
	}
	
	public CellStyle getTextAreaCellStyle(){
		return getCellStyle(HSSFColor.BLACK.index, HSSFColor.GREY_25_PERCENT.index);
	}
	
	public CellStyle getNoAnswerCellStyle(){
		return getCellStyle(HSSFColor.BLACK.index, HSSFColor.YELLOW.index);
	}
	
	public CellStyle getMultipleAnswersCellStyle(){
		return getCellStyle(HSSFColor.BLACK.index, HSSFColor.LIGHT_ORANGE.index);
	}

	@Override
	protected short getSolidForegroundCellStyle() {
		return XSSFCellStyle.SOLID_FOREGROUND;
	}
}