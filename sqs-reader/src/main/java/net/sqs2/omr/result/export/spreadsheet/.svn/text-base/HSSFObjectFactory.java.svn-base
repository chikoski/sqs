/**
 * 
 */
package net.sqs2.omr.result.export.spreadsheet;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.RichTextString;
import org.apache.poi.ss.usermodel.Workbook;

class HSSFObjectFactory extends AbstractSpreadSheetObjectFactory implements SpreadSheetObjectFactory{

	/**
	 * @param excelExportModule
	 */
	
	HSSFObjectFactory(ExcelExportModule excelExportModule) {
		super(excelExportModule);
	}
	
	public Workbook createWorkbook(){
		return new HSSFWorkbook();
	}
	
	public String getSuffix(){
		return "xls";
	}
	
	public RichTextString createRichTextString(String value){
		return new HSSFRichTextString(value);
	}
	

	protected short getSolidForegroundCellStyle(){
		return HSSFCellStyle.SOLID_FOREGROUND;
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
}