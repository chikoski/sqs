/*

VirtualSheetHSSFAdapter.java

Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

Created on 2007/09/05

 */

package net.sqs2.spreadsheet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

public class VirtualSpreadSheetWorkbook extends SpreadSheetWorkbook {

	short numHeaderRows;
	short numHeaderColumns;

	Map<String, Short> numColumnsMap;
	//SpreadSheetWorkbook adapter;

	private Map<String, CellStyle> cellStyleCache = new HashMap<String, CellStyle>();
	private Map<Short, Font> fontCache = new HashMap<Short, Font>();

	public VirtualSpreadSheetWorkbook(Workbook workbook, short numHeaderRows, short numHeaderColumns) {
		super(workbook);
		//this.adapter = new SpreadSheetWorkbook(workbook);
		this.numHeaderRows = numHeaderRows;
		this.numHeaderColumns = numHeaderColumns;

		this.numColumnsMap = new LinkedHashMap<String, Short>();
	}

	public Map<String, CellStyle> getCellStyleCache() {
		return this.cellStyleCache;
	}

	public Map<Short, Font> getFontCache() {
		return this.fontCache;
	}

	public void writeTo(OutputStream xlsOutputStream) throws IOException {
		super.writeTo(xlsOutputStream);
	}

	public int getNumSheets() {
		return this.numColumnsMap.size();
	}

	/*
	 * you must call this method when you create every single sheet.
	 */
	public void setNumColumns(String sheetName, short numColumns) {
		this.numColumnsMap.put(sheetName, numColumns);
	}

	public Cell getCell(int rowIndex, int virtualColumnIndex, int rowOffset, int columnOffset) {
		int numColumnsTotal = 0;
		for (Map.Entry<String, Short> entry : this.numColumnsMap.entrySet()) {
			String sheetName = entry.getKey();
			short numColumns = entry.getValue();

			if (numColumnsTotal <= virtualColumnIndex && virtualColumnIndex < numColumnsTotal + numColumns) {
				Row row = super.getRow(sheetName, rowIndex + rowOffset);
				short physicalColumnIndex = (short) (virtualColumnIndex - numColumnsTotal + columnOffset);
				return super.getCell(row, physicalColumnIndex);
			}
			numColumnsTotal += numColumns;
		}
		throw createIllegalArgumentException(rowIndex, virtualColumnIndex);
	}

	public Cell getNorthHeaderCell(int rowIndex, int virtualColumnIndex) {
		return getCell(rowIndex, virtualColumnIndex, 0, this.numHeaderColumns);
	}

	public Cell[] getWestHeaderCellArray(int rowIndex, short columnIndex) {
		if (columnIndex < this.numHeaderColumns) {
			Cell[] cellArray = new Cell[this.numColumnsMap.size()];
			int sheetIndex = 0;
			for (Map.Entry<String, Short> entry : this.numColumnsMap.entrySet()) {
				String sheetName = entry.getKey();
				Row row = super.getRow(sheetName, rowIndex + this.numHeaderRows);
				Cell cell = super.getCell(row, columnIndex);
				cellArray[sheetIndex++] = cell;
			}
			return cellArray;
		}
		throw createIllegalArgumentException(rowIndex, columnIndex);
	}

	private IllegalArgumentException createIllegalArgumentException(int rowIndex, int columnIndex) {
		return new IllegalArgumentException("Exceed: rowIndex=" + rowIndex + ", columnIndex=" + columnIndex); //$NON-NLS-1$ //$NON-NLS-2$
	}

	public Cell getBodyCell(int rowIndex, int virtualColumnIndex) {
		return getCell(rowIndex, virtualColumnIndex, this.numHeaderRows, this.numHeaderColumns);
	}

}
