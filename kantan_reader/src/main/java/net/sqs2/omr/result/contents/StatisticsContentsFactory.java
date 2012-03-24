/**
 * 
 */
package net.sqs2.omr.result.contents;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.result.model.MarkAreaAnswer;
import net.sqs2.omr.result.model.MarkAreaAnswerItem;
import net.sqs2.omr.result.model.Row;
import net.sqs2.omr.source.SessionSource;
import net.sqs2.omr.source.SourceDirectory;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;

public class StatisticsContentsFactory extends SimpleContentsFactory{
	Bag<String> values;
	int viewMode;
	public StatisticsContentsFactory(SessionSource sessionSource, int viewMode)throws IOException{
		super(sessionSource);
		this.viewMode = viewMode;
		this.values = new HashBag<String>();
	}
	
	public int getCount(String key){
		return this.values.getCount(key);
	}
	
	public void add(String key){
		this.values.add(key);
	}
	
	@Override
	void processRow(FormMaster master,
			Set<Integer> selectedQuestionIndexSet, SourceDirectory sourceDirectory, int selectedTableSerialNum, int tableIndex, int selectedRowIndex, int rowIndex){
		
		Row row = (Row)this.rowAccessor.get(master.getRelativePath(), sourceDirectory.getPath(), rowIndex);
		//List<PageAreaCommand> pageAreaCommandListParRow = ContentsFactoryUtil.createPageAreaCommandListParRow(sourceDirectory, master, pageTaskAccessor, rowIndex);

		if(row == null || row.getTaskErrorMultiHashMap() != null){
			return;
		}
		
		int numSelectedQuestions = selectedQuestionIndexSet.size();
		float densityThreshold = sourceDirectory.getConfiguration().getConfig().getSourceConfig().getMarkRecognitionConfig().getDensity();
		
		if(ViewMode.isCrossChartListViewMode(this.viewMode)){
			if(numSelectedQuestions == 0 || 10 < numSelectedQuestions){
				return;
			}
			for(int columnIndex = 0;  columnIndex < master.getNumColumns(); columnIndex++){
				if(! master.getFormAreaList(columnIndex).get(0).isMarkArea()){
					continue;
				}
				
				int selectedItemIndex0 = getSelectedSelectOneFormAreaIndex((MarkAreaAnswer)row.getAnswer(columnIndex), densityThreshold);
				int userSelectedQuestionSerialNum = 0;
				for(int userSelectedQuestionIndex: selectedQuestionIndexSet){
					if(columnIndex == userSelectedQuestionIndex || ! master.getFormAreaList(userSelectedQuestionIndex).get(0).isMarkArea()){
						continue;
					}
					int selectedItemIndex1 = getSelectedSelectOneFormAreaIndex((MarkAreaAnswer)row.getAnswer(userSelectedQuestionIndex), densityThreshold);
					incrementCrossTableList(columnIndex, userSelectedQuestionSerialNum, selectedItemIndex1, selectedItemIndex0);
					userSelectedQuestionSerialNum++;
				}
			}
		}else{
			int[] selectedItemIndex = new int[numSelectedQuestions];
			int userSelectedQuestionSerialNum = 0;
			for(int columnIndex: selectedQuestionIndexSet){
				processQuestion(selectedTableSerialNum, master, row, 
						selectedItemIndex, userSelectedQuestionSerialNum, columnIndex, densityThreshold);
				userSelectedQuestionSerialNum++;
			}
			incrementValue(selectedTableSerialNum, selectedItemIndex);
		}
	}
	
	private void processQuestion(
			int selectedTableSerialNum, FormMaster master, Row row,
			int[] selectedItemIndex,
			int userSelectedQuestionSerialNum, int columnIndex,
			float densityThreshold) {
		List<FormArea> formAreaList = master.getFormAreaList(columnIndex);
		FormArea defaultFormArea = formAreaList.get(0);
		if(defaultFormArea.isSelect1()){
			int index = getSelectedSelectOneFormAreaIndex((MarkAreaAnswer)row.getAnswer(columnIndex), densityThreshold);
			selectedItemIndex[userSelectedQuestionSerialNum] = index;
			incrementValue(selectedTableSerialNum, columnIndex, selectedItemIndex[userSelectedQuestionSerialNum]);
		}else if(defaultFormArea.isSelect()){
			MarkAreaAnswer answer = (MarkAreaAnswer)row.getAnswer(columnIndex);
			for(FormArea formArea: formAreaList){
				int itemIndex = formArea.getItemIndex();
				if(isSelectedSelectMultiFormArea(answer, densityThreshold, itemIndex)){
					incrementValue(selectedTableSerialNum, columnIndex, itemIndex);
				}
			}
		}
	}

	int getSelectedSelectOneFormAreaIndex(MarkAreaAnswer answer, float densityThreshold){
		int ret = -1; // no answer
		for(MarkAreaAnswerItem answerItem: answer.getMarkAreaAnswerItemArray()){
			if(answer.isManualMode() && answerItem.isManualSelected()){
				return answerItem.getItemIndex();
			}else if(answerItem.getDensity() < densityThreshold){
				if(ret == -1){
					ret = answerItem.getItemIndex();
				}else{
					return -1;//ERROR multi answer
				}
			}
		}
		return ret;
	}

	boolean isSelectedSelectMultiFormArea(MarkAreaAnswer answer, float densityThreshold, int itemIndex){
		if(answer.isManualMode()){
			return answer.getMarkAreaAnswerItem(itemIndex).isManualSelected();
		}else{
			return (answer.getMarkAreaAnswerItem(itemIndex).getDensity() < densityThreshold);
		}
	}
	
	void incrementSimpleChartValue(int columnIndex, int selectedItemIndex){
		this.values.add(new StringBuilder().
				append(columnIndex).
				append(',').
				append(selectedItemIndex).
				toString());
	}
	
	void incrementGroupSimpleChartValue(int selectedTableSerialNum, int questionIndex, int selectedItemIndex){
		add(new StringBuilder().
				append(selectedTableSerialNum).
				append(',').
				append(questionIndex).
				append(',').
				append(selectedItemIndex).
				toString());
	}

	void incrementDoubleCrossChartValue(int selectedItemIndex0, int selectedItemIndex1){
		add(new StringBuilder().
				append(',').
				append(selectedItemIndex0).
				append(',').
				append(selectedItemIndex1).
				toString());
	}

	void incrementTripleCrossChartValue(int selectedItemIndex0, int selectedItemIndex1, int selectedItemIndex2){
		add(new StringBuilder().
				append(selectedItemIndex0).
				append(',').
				append(selectedItemIndex1).
				append(',').
				append(selectedItemIndex2).
				toString());
	}
	
	void incrementGroupDoubleCrossChartValue(int selectedTableSerialNum, int selectedItemIndex0){
		add(new StringBuilder().
				append(',').
				append(selectedItemIndex0).
				append(',').				
				append(selectedTableSerialNum).
				toString());
	}

	void incrementGroupTripleCrossChartValue(int selectedTableSerialNum, int selectedItemIndex0, int selectedItemIndex1){
		add(new StringBuilder().
				append(selectedTableSerialNum).
				append(',').
				append(selectedItemIndex0).
				append(',').
				append(selectedItemIndex1).
				toString());
	}
	
	void incrementCrossTableList(int autoSelectedQuestionIndex, int userSelectedQuestionSerialNum, int selectedItemIndex0, int selectedItemIndex1){
		add(new StringBuilder().
				append(autoSelectedQuestionIndex).
				append(',').
				append(userSelectedQuestionSerialNum).
				append(',').
				append(selectedItemIndex0).
				append(',').
				append(selectedItemIndex1).
				toString());
	}

	void incrementValue(int selectedTableSerialNum, int columnIndex, int selectedItemIndex) {
		if(ViewMode.isSimpleChartViewMode(this.viewMode)){
			incrementSimpleChartValue(columnIndex, selectedItemIndex);
		}else if(ViewMode.isGroupSimpleChartViewMode(this.viewMode)){
			incrementGroupSimpleChartValue(selectedTableSerialNum,columnIndex, selectedItemIndex);	
		}else if(ViewMode.isGroupCrossChartViewMode(this.viewMode)){
			incrementGroupDoubleCrossChartValue(selectedTableSerialNum, selectedItemIndex);	
		}
	}
	
	void incrementValue(int selectedTableSerialNum, int[] selectedItemIndex) {
		if(ViewMode.isCrossChartViewMode(this.viewMode)){
			if(selectedItemIndex.length == 2){
				incrementDoubleCrossChartValue(selectedItemIndex[0], selectedItemIndex[1]);
			}else if(selectedItemIndex.length == 3){
				incrementTripleCrossChartValue(selectedItemIndex[0], selectedItemIndex[1], selectedItemIndex[2]);
			}
		}else if(ViewMode.isGroupCrossChartViewMode(this.viewMode)){
			 if(selectedItemIndex.length == 1){
				 incrementGroupDoubleCrossChartValue(selectedTableSerialNum, selectedItemIndex[0]);	 
			 }else if(selectedItemIndex.length == 2){
				 incrementGroupTripleCrossChartValue(selectedTableSerialNum, selectedItemIndex[0], selectedItemIndex[1]);
			 }
		}
	}
}
