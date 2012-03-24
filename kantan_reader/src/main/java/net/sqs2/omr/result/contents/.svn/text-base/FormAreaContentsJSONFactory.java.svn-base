package net.sqs2.omr.result.contents;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.result.model.TextAreaAnswer;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.task.PageAreaCommand;

import org.apache.commons.codec.binary.Base64;

public abstract class FormAreaContentsJSONFactory extends SimpleContentsFactory {

	protected boolean isMSIE;
	protected PrintWriter w;
	protected boolean hasStartedRow = false;

	public FormAreaContentsJSONFactory(PrintWriter w, SessionSource sessionSource)throws IOException{
		super(sessionSource);
		this.w = w;
	}
	
	void printRowSeparator(){
		if(this.hasStartedRow){
			this.w.print(',');
		}else{
			this.hasStartedRow = true;
		}
	}

	protected String createQueryParamString(int masterIndex, int tableIndex, int rowIndex,
			int columnIndex) {
				StringBuilder sb = new StringBuilder(); 
				sb.append("/i?m=");
				sb.append(masterIndex);
				sb.append("&t=");
				sb.append(tableIndex);
				sb.append("&r=");
				sb.append(rowIndex);
				sb.append("&q=");
				sb.append(columnIndex);
				return sb.toString();
			}

	public void writeMarkAreaAnswer(MarkAreaAnswer markAreaAnswer, List<PageAreaCommand> pageAreaCommandListParRow, int formAreaIndex,
			String queryParamString, float densityThreshold) {
			
				if(pageAreaCommandListParRow == null){
					this.w.print("{}");
				}
				MarkAreaAnswerItem[] itemArray = markAreaAnswer.getMarkAreaAnswerItemArray();
				this.w.print("{");
				if(markAreaAnswer.isManualMode()){
					this.w.print("'M':1,");
				}
				this.w.print("'i':[");
				boolean hasMarkAreaPrinted = false;
				int itemIndex = 0;
				int numMarked = 0;
				for(MarkAreaAnswerItem item: itemArray){
					if(hasMarkAreaPrinted){
						this.w.print(',');
					}else{
						hasMarkAreaPrinted = true;
					}
					if(item == null){
						this.w.print("{d:1}");
						continue;
					}
					this.w.print("{");
					if(markAreaAnswer.isManualMode()){
						if(item.isManualSelected()){
							this.w.print("'M':1,");
						}else{
							this.w.print("'M':0,");
						}
					}
					
					PageAreaCommand pageAreaCommand = pageAreaCommandListParRow.get(formAreaIndex);
					if(pageAreaCommand != null){
						this.w.print("s:\'");
						if(this.isMSIE){
							this.w.print(queryParamString);
							this.w.print("&i=");
							this.w.print(itemIndex);
						}else{
							writeBase64Data(w, pageAreaCommand);
						}
						this.w.print("',");
					}
					this.w.print("d:");
					if(item.getDensity() < densityThreshold){
						numMarked++;
					}
					this.w.print(item.getDensity());
					this.w.print("}");
					
					formAreaIndex++;
					itemIndex++;
				}
				this.w.print("]}");
			}

	protected void writeTextAreaAnswer(TextAreaAnswer textAreaAnswer, List<PageAreaCommand> pageAreaCommandList,
			int formAreaIndex, String queryParamString) {
				String value = textAreaAnswer.getValue();
				this.w.print("{");
				if(value != null){
					this.w.print("v:'");
					this.w.print(value.replace("'", "\\'"));
					this.w.print("',");
				}else{
					this.w.print("v:null,");
				}
				this.w.print("s:\'");
				if(this.isMSIE){
					this.w.print(queryParamString);
				}else{
					writeBase64Data(w, pageAreaCommandList.get(formAreaIndex));
				}
				this.w.print("'");
				this.w.print("}");
	}
	
	void writeBase64Data(PrintWriter w, PageAreaCommand pageAreaCommand){
		String type = pageAreaCommand.getImageType();
		byte[] bytes = pageAreaCommand.getImageByteArray();
		if(bytes == null){
			return;
		}
		this.w.print("data:image/");
		this.w.print(type);
		this.w.print(";base64,");
		for(byte b: Base64.encodeBase64(bytes)){
			this.w.write(b);
		}
	}

	protected void startRow() {
		this.w.print("[");// start row
	}

	protected void endRow() {
		this.w.print("]");// end row
	}

	protected boolean isErrorRow(FormMaster master, Row row, List<PageAreaCommand> pageAreaCommandListParRow) {
		//pageAreaCommandListParRow.size() != master.getFormAreaList().size() || row == null || 
		return row == null || row.getTaskErrorMultiHashMap() != null;
	}

}