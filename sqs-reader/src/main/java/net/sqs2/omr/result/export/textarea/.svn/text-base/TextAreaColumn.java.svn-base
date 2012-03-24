/**
 * 
 */
package net.sqs2.omr.result.export.textarea;

import java.util.ArrayList;
import java.util.List;

public class TextAreaColumn {
	private TextAreaColumnMetadata textAreaColumnMetadata;
	private List<TextAreaRowGroup> textAreaRowGroupList;

	TextAreaColumn(TextAreaColumnMetadata textAreaColumnMetadata) {
		this.textAreaColumnMetadata = textAreaColumnMetadata;
		this.textAreaRowGroupList = new ArrayList<TextAreaRowGroup>();
	}

	public TextAreaColumnMetadata getTextAreaColumnMetadata() {
		return this.textAreaColumnMetadata;
	}
	
	public List<TextAreaRowGroup> getTextAreaRowGroupList(){
		return this.textAreaRowGroupList;
	}
	
	public int getTextAreaRowGroupSize(){
		return this.textAreaRowGroupList.size();		
	}

	public void addTextAreaRowGroup(TextAreaRowGroup textAreaRowGroup){
		this.textAreaRowGroupList.add(textAreaRowGroup);		
	}

	public TextAreaRowGroup getTextAreaRowGroup(int index){
		return this.textAreaRowGroupList.get(index);		
	}

	public TextAreaRowGroup getLastTextAreaRowGroup(){
		return this.textAreaRowGroupList.get(this.textAreaRowGroupList.size() - 1);		
	}

	public void clearTextAreaRowGroup(){
		this.textAreaRowGroupList.clear();		
	}
	
	public int getNumRowRangePages() {
		int numRowRangePages = 0;
		for(TextAreaRowGroup textAreaRowGroup: this.textAreaRowGroupList){
			numRowRangePages += textAreaRowGroup.getTextAreaRowRangeSize();
		}
		return numRowRangePages; 
	}

}