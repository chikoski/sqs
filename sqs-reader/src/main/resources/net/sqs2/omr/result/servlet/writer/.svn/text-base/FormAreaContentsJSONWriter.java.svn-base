package net.sqs2.omr.result.servlet.writer;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.TextAreaAnswer;
import net.sqs2.omr.result.context.ResultBrowserContext;

import org.apache.commons.codec.binary.Base64;

public abstract class FormAreaContentsJSONWriter extends SimpleContentsWriter {

	protected boolean isMSIE;
	protected PrintWriter w;
	protected boolean hasStartedRow = false;

	public FormAreaContentsJSONWriter(PrintWriter w, ResultBrowserContext contentSelection) throws IOException {
		super(sessionSource);
		this.w = w;
	}

	void printRowSeparator() {
		if (this.hasStartedRow) {
			this.w.print(',');
		} else {
			this.hasStartedRow = true;
		}
	}

	protected String createQueryParamString(int masterIndex, int tableIndex, int rowIndex, int columnIndex) {
		StringBuilder sb = new StringBuilder();
		sb.append("/i?m=");
		sb.append(masterIndex);
		sb.append("&t=");
		sb.append(tableIndex);
		sb.append("&r=");
		sb.append(rowIndex);
		sb.append("&q=");
		sb.append(columnIndex);
		sb.append("&sid=");
		sb.append(this.sessionSource.getSessionID());
		return sb.toString();
	}

	public void writeMarkAreaAnswer(MarkAreaAnswer markAreaAnswer, List<PageAreaResult> pageAreaResultListParRow, int formAreaIndex, String queryParamString, float densityThreshold) {

		if (pageAreaResultListParRow == null) {
			this.w.print("{}");
		}
		MarkAreaAnswerItem[] itemArray = markAreaAnswer.getMarkAreaAnswerItemArray();
		this.w.print("{");
		if (markAreaAnswer.isManualMode()) {
			this.w.print("'M':1,");
		}
		this.w.print("'i':[");
		boolean hasMarkAreaPrinted = false;
		int itemIndex = 0;
		int numMarked = 0;
		for (MarkAreaAnswerItem item : itemArray) {
			if (hasMarkAreaPrinted) {
				this.w.print(',');
			} else {
				hasMarkAreaPrinted = true;
			}
			if (item == null) {
				this.w.print("{d:1}");
				continue;
			}
			this.w.print("{");
			if (markAreaAnswer.isManualMode()) {
				if (item.isManualSelected()) {
					this.w.print("'M':1,");
				} else {
					this.w.print("'M':0,");
				}
			}

			PageAreaResult pageAreaResult = pageAreaResultListParRow.get(formAreaIndex);
			if (pageAreaResult != null) {
				this.w.print("s:\'");
				if (this.isMSIE) {
					this.w.print(queryParamString);
					this.w.print("&i=");
					this.w.print(itemIndex);
				} else {
					writeBase64Data(this.w, pageAreaResult);
				}
				this.w.print("',");
			}
			this.w.print("d:");
			if (item.getDensity() < densityThreshold) {
				numMarked++;
			}
			this.w.print(item.getDensity());
			this.w.print("}");

			formAreaIndex++;
			itemIndex++;
		}
		this.w.print("]}");
	}

	protected void writeTextAreaAnswer(TextAreaAnswer textAreaAnswer, List<PageAreaResult> pageAreaResultList, int formAreaIndex, String queryParamString) {
		String value = textAreaAnswer.getValue();
		this.w.print("{");
		if (value != null) {
			this.w.print("v:'");
			this.w.print(value.replace("'", "\\'"));
			this.w.print("',");
		} else {
			this.w.print("v:null,");
		}
		this.w.print("s:\'");
		if (this.isMSIE) {
			this.w.print(queryParamString);
		} else {
			writeBase64Data(this.w, pageAreaResultList.get(formAreaIndex));
		}
		this.w.print("'");
		this.w.print("}");
	}

	void writeBase64Data(PrintWriter w, PageAreaResult pageAreaResult) {
		String type = pageAreaResult.getImageType();
		byte[] bytes = pageAreaResult.getImageByteArray();
		if (bytes == null) {
			return;
		}
		this.w.print("data:image/");
		this.w.print(type);
		this.w.print(";base64,");
		for (byte b : Base64.encodeBase64(bytes)) {
			this.w.write(b);
		}
	}

	protected void startRow() {
		this.w.print("[");// start row
	}

	protected void endRow() {
		this.w.print("]");// end row
	}

	protected boolean isErrorRow(FormMaster master, Row row, List<PageAreaResult> pageAreaResultListParRow) {
		// pageAreaResultListParRow.size() != master.getFormAreaList().size()
		// || row == null ||
		return row == null || row.getTaskErrorMultiHashMap() != null;
	}

}
