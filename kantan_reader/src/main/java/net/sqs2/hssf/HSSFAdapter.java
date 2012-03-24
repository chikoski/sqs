/*

HSSFAdapter.java

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

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

public class HSSFAdapter {

	private HSSFWorkbook currentWorkbook = null;

	private HSSFRow currentRow;

	private String currentSheetName = null;
	private int currentRowIndex = -1;

	HSSFAdapter(){
		this.currentWorkbook = new HSSFWorkbook();
		this.currentRow = null;
	}

	public HSSFWorkbook getWorkbook(){
		return this.currentWorkbook;
	}

	public void writeTo(OutputStream xlsOutputStream)throws IOException{
		this.currentWorkbook.write(xlsOutputStream);
	}

	public int getSheetIndex(String sheetName){
		getSheet(sheetName);
		return this.currentWorkbook.getSheetIndex(sheetName);	
	}

	public HSSFSheet getSheet(String sheetName) {
		HSSFSheet sheet = this.currentWorkbook.getSheet(sheetName);  
		if(sheet == null){
			sheet = createSheet(sheetName);
		}
		return sheet;
	}

	protected HSSFSheet createSheet(String sheetName) {
		return this.currentWorkbook.createSheet(sheetName);
	}

	public HSSFRow getRow(String sheetName, int rowIndex) {
		if(sheetName.equals(this.currentSheetName) && this.currentRowIndex == rowIndex){
			return this.currentRow;
		}
		HSSFSheet sheet = getSheet(sheetName);
		HSSFRow row = sheet.getRow(rowIndex);
		if(row == null){
			row = sheet.createRow(rowIndex);
		}
		this.currentSheetName = sheetName;
		this.currentRowIndex = rowIndex;
		return this.currentRow = row;
	}

	public HSSFCell getCell(String sheetName, int rowIndex, short columnIndex) {
		return getCell(getRow(sheetName, rowIndex), columnIndex);
	}

	public HSSFCell getCell(HSSFRow row, short columnIndex) {
		HSSFCell cell = row.getCell(columnIndex);
		if(cell == null){
			cell = row.createCell(columnIndex);
		}
		return cell;
	}

}
