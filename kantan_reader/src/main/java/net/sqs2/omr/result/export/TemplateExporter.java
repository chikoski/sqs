/**
 * 
 */
package net.sqs2.omr.result.export;

import java.io.IOException;

import net.sqs2.omr.app.MarkReaderConstants;
import net.sqs2.omr.master.FormArea;

class TemplateExporter{
	String skin;
	TemplateLoader loader;
	
	TemplateExporter(String skin)throws IOException{
		this.skin = skin;
		this.loader = new TemplateLoader(MarkReaderConstants.USER_CUSTOMIZE_CONSTANTS_DIR, "ftl");
	}
	
	class FormMasterMetadata{
		FormMasterMetadata(){
		}
	}
	
	public class ColumnMetadata{
		FormArea defaultFormArea;
		String sourceDirectoryPath;
		String rowDirectoryPath;
		int columnIndex;
		int numRows;
		
		ColumnMetadata(FormArea defaultFormArea, String sourceDirectoryPath, String rowDirectoryPath, int columnIndex, int numRows){
			this.defaultFormArea = defaultFormArea;
			this.sourceDirectoryPath = sourceDirectoryPath;
			this.rowDirectoryPath = rowDirectoryPath;
			this.columnIndex = columnIndex;
			this.numRows = numRows;
		}
		
		public FormArea getDefaultFormArea(){
			return defaultFormArea;
		}
		
		public void setSourceDirectoryPath(String sourceDirectoryPath) {
			this.sourceDirectoryPath = sourceDirectoryPath;
		}
		public String getSourceDirectoryPath() {
			return sourceDirectoryPath;
		}
		public String getRowDirectoryPath() {
			return rowDirectoryPath;
		}
		public int getColumnIndex() {
			return columnIndex;
		}		
		public int getNumRows(){
			return this.numRows;
		}
		public int getPage(){
			return this.defaultFormArea.getPage();
		}
	}
}