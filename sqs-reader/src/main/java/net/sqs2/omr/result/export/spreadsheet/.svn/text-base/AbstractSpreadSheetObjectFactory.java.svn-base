package net.sqs2.omr.result.export.spreadsheet;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;

public abstract class AbstractSpreadSheetObjectFactory {

	protected final ExcelExportModule excelExportModule;

	public AbstractSpreadSheetObjectFactory(ExcelExportModule excelExportModule) {
		super();
		this.excelExportModule = excelExportModule;
	}
	
	public CellStyle getCellStyle(short fontColor, short backgroundColor) {
		String key = fontColor + "," + backgroundColor;
		CellStyle style = this.excelExportModule.getSpreadSheetWorkbook().getCellStyleCache().get(key);
		if (style == null) {
			style = createCellStyle(fontColor, backgroundColor);
			this.excelExportModule.getSpreadSheetWorkbook().getCellStyleCache().put(key, style);
		}
		return style;
	}
	
	protected CellStyle createCellStyle(short fontColor, short backgroundColor) {
		Font font = getFont(fontColor);
		CellStyle style = this.excelExportModule.getSpreadSheetWorkbook().createCellStyle();
		style.setFont(font);
		style.setFillForegroundColor(backgroundColor);
		style.setFillPattern(getSolidForegroundCellStyle());
		return style;
	}

	private Font getFont(short fontColor) {
		Font font = this.excelExportModule.getSpreadSheetWorkbook().getFontCache().get(fontColor);
		if (font == null) {
			font = this.excelExportModule.getSpreadSheetWorkbook().createFont();
			font.setColor(fontColor);
			this.excelExportModule.getSpreadSheetWorkbook().getFontCache().put(fontColor, font);
		}
		return font;
	}
	
	protected abstract short getSolidForegroundCellStyle();



}