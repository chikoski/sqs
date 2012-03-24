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

package net.sqs2.hssf;

import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;

public class VirtualSheetHSSFAdapter{
	
	public static final short NUM_COLUMNS_MAX = 256;
	short numHeaderRows;
	short numHeaderColumns;

	Map<String,Short> numColumnsMap;
	HSSFAdapter adapter;
	
	private Map<String,HSSFCellStyle> cellStyleCache = new HashMap<String,HSSFCellStyle>();
	private Map<Short,HSSFFont> fontCache = new HashMap<Short,HSSFFont>();
	
	public VirtualSheetHSSFAdapter(short numHeaderRows, short numHeaderColumns){
		if(NUM_COLUMNS_MAX < numHeaderColumns){
			throw new IllegalArgumentException("Invalid large numHeaderColumns:"+numHeaderRows);
		}
		this.numHeaderRows = numHeaderRows;
		this.numHeaderColumns = numHeaderColumns;

		this.numColumnsMap = new LinkedHashMap<String,Short>();
		this.adapter = new HSSFAdapter();
	}
	
	public Map<String,HSSFCellStyle> getCellStyleCache(){
		return this.cellStyleCache;
	}
	public Map<Short,HSSFFont> getFontCache(){
		return this.fontCache;
	}
	
	public void writeTo(OutputStream xlsOutputStream) throws IOException {
		this.adapter.writeTo(xlsOutputStream);
	}
	
	public int getNumSheets(){
		return this.numColumnsMap.size();
	}
	
	/*
	 * you must call this method when you create every single sheet.
	 */
	public void setNumColumns(String sheetName, short numColumns) {
		this.numColumnsMap.put(sheetName, numColumns);
	}
	
	public HSSFCell getCell(int rowIndex, int virtualColumnIndex, int rowOffset, int columnOffset) {
		int numColumnsTotal = 0;
		for(Map.Entry<String,Short> entry: this.numColumnsMap.entrySet()){
			String sheetName = entry.getKey();
			short numColumns = entry.getValue();
			
			if(numColumnsTotal <= virtualColumnIndex && virtualColumnIndex < numColumnsTotal + numColumns){
				HSSFRow row = this.adapter.getRow(sheetName, rowIndex + rowOffset);
				short physicalColumnIndex =  (short)(virtualColumnIndex - numColumnsTotal + columnOffset);
				return this.adapter.getCell(row, physicalColumnIndex);
			}
			numColumnsTotal += numColumns; 
		}
		throw createException(rowIndex, virtualColumnIndex);
	}

	public HSSFCell getNorthHeaderCell(int rowIndex, int virtualColumnIndex) {
		return getCell(rowIndex, virtualColumnIndex, 0, this.numHeaderColumns);
	}
	
	public HSSFCell[] getWestHeaderCellArray(int rowIndex, short columnIndex) {
		if(columnIndex < this.numHeaderColumns){
			HSSFCell[] cellArray = new HSSFCell[this.numColumnsMap.size()];
			int sheetIndex = 0;
			for(Map.Entry<String,Short> entry: this.numColumnsMap.entrySet()){
				String sheetName = entry.getKey();
				HSSFRow row = this.adapter.getRow(sheetName, rowIndex + this.numHeaderRows);
				HSSFCell cell = this.adapter.getCell(row, columnIndex);
				cellArray[sheetIndex++] = cell;
			}
			return cellArray;
		}
		throw createException(rowIndex, columnIndex);
	}
	
	private IllegalArgumentException createException(int rowIndex, int columnIndex){
		return new IllegalArgumentException("Exceed: rowIndex="+rowIndex+", columnIndex="+columnIndex);
	}
	
	public HSSFCell getBodyCell(int rowIndex, int virtualColumnIndex) {
		return getCell(rowIndex, virtualColumnIndex, this.numHeaderRows, this.numHeaderColumns);
	}
	
	public HSSFCellStyle createCellStyle(){
		return this.adapter.getWorkbook().createCellStyle();
	}
	
	public HSSFFont createFont(){
		return this.adapter.getWorkbook().createFont();
	}
	
}
