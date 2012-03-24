/**
 * 
 */
package net.sqs2.omr.result.builder;

import java.util.List;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.ContentAccessor;
import net.sqs2.omr.model.MarkAreaAnswer;
import net.sqs2.omr.model.MarkAreaAnswerItem;
import net.sqs2.omr.model.PageTaskAccessor;
import net.sqs2.omr.model.Row;
import net.sqs2.omr.model.RowAccessor;
import net.sqs2.omr.model.SourceDirectory;
import net.sqs2.omr.result.context.ResultBrowserContext;
import net.sqs2.omr.result.path.MultiRowSelection;
import net.sqs2.omr.result.path.MultiRowSingleQuestionPath;
import net.sqs2.omr.result.path.MultiRowSingleQuestionSelection;
import net.sqs2.omr.result.path.SelectedRowID;

public class StatFactory {

	static abstract class StatItemFactory<K> {

		StatisticsResult<K> result;

		StatItemFactory(ResultBrowserContext resultBrowserContext){
			this.result = new StatisticsResult<K>();
			
			MultiRowSelection selection = createMultiRowSelection(resultBrowserContext.getPathInfo());
			
			ContentAccessor contentAccessor = selection.getSessionSource().getContentAccessor();
			PageTaskAccessor pageTaskAccessor = contentAccessor.getPageTaskAccessor();
			RowAccessor rowAccessor = contentAccessor.getRowAccessor();
			
			FormMaster master = selection.getFormMaster();
			String masterPath = master.getRelativePath();
			
			for (SelectedRowID rowID : selection.getSelectedRowIDList()) {
				SourceDirectory sourceDirectory = rowID.getSpreadSheet().getSourceDirectory();
				int rowIndex = (int)rowID.getRowIndex();
				Row row = (Row) rowAccessor.get(masterPath, sourceDirectory.getRelativePath(), rowIndex);
				if (row == null || row.getTaskErrorMultiHashMap() != null) {
					return;
				}
				processRow(rowAccessor, pageTaskAccessor, row, selection, master, sourceDirectory, rowIndex);
			}
		}
		
		private MultiRowSelection createMultiRowSelection(String path){
			return new MultiRowSingleQuestionSelection(new MultiRowSingleQuestionPath(path));
		}
		
		protected abstract void processRow(RowAccessor rowAccessor, PageTaskAccessor pageTaskAccessor, Row row, 
				MultiRowSelection abstractSelection, FormMaster master,
				SourceDirectory sourceDirectory, int rowIndex);
		
		private void processQuestion(List<FormArea> formAreaList,
				int selectedTableSerialNum,
				FormMaster master, Row row,
				int[] selectedItemIndex,
				int userSelectedQuestionSerialNum,
				float densityThreshold) {

			FormArea primaryFormArea = formAreaList.get(0);
			if (primaryFormArea.isSelectSingle()) {
				int index = getSelectedSelectOneFormAreaIndex(
						(MarkAreaAnswer) row.getAnswer(primaryFormArea.getQuestionIndex()),
						densityThreshold);
				selectedItemIndex[userSelectedQuestionSerialNum] = index;
				incrementValue(selectedTableSerialNum, primaryFormArea.getQuestionIndex(),
				selectedItemIndex[userSelectedQuestionSerialNum]);
			} else if (primaryFormArea.isSelectMultiple()) {
				MarkAreaAnswer answer = (MarkAreaAnswer) row
						.getAnswer(primaryFormArea.getQuestionIndex());
				for (FormArea formArea : formAreaList) {
					int itemIndex = formArea.getItemIndex();
					if (isSelectedSelectMultiFormArea(answer, densityThreshold,
							itemIndex)) {
						incrementValue(selectedTableSerialNum, primaryFormArea.getQuestionIndex(), itemIndex);
					}
				}
			}
		}

		protected abstract void incrementValue(int selectedTableSerialNum, int columnIndex,
				int selectedItemIndex);

		int getSelectedSelectOneFormAreaIndex(MarkAreaAnswer answer,
				float densityThreshold) {
			int ret = -1; // no answer
			for (MarkAreaAnswerItem answerItem : answer
					.getMarkAreaAnswerItemArray()) {
				if (answer.isManualMode() && answerItem.isManualSelected()) {
					return answerItem.getItemIndex();
				} else if (answerItem.getDensity() < densityThreshold) {
					if (ret == -1) {
						ret = answerItem.getItemIndex();
					} else {
						return -1;// ERROR multi answer
					}
				}
			}
			return ret;
		}

		boolean isSelectedSelectMultiFormArea(MarkAreaAnswer answer,
				float densityThreshold, int itemIndex) {
			if (answer.isManualMode()) {
				return answer.getMarkAreaAnswerItem(itemIndex)
						.isManualSelected();
			} else {
				return (answer.getMarkAreaAnswerItem(itemIndex).getDensity() < densityThreshold);
			}
		}
	}

	/*
	static class MultiRowStatItemFactory extends StatItemFactory<String> {

		MultiRowStatItemFactory(ResultBrowserContext resultBrowserContext) {
			super(resultBrowserContext);
			
			
		}

		@Override
		protected void processRow(RowAccessor rowAccessor,
				PageTaskAccessor pageTaskAccessor, Row row,
				MultiRowSelection abstractSelection, FormMaster master,
				SourceDirectory sourceDirectory, int rowIndex) {
		}

		@Override
		protected void incrementValue(int selectedTableSerialNum, int columnIndex,
				int selectedItemIndex) {
			if (ViewModeUtil.isSimpleChartViewMode(this.viewMode)) {
				incrementSimpleChartValue(columnIndex, selectedItemIndex);
			} else if (ViewModeUtil.isGroupSimpleChartViewMode(this.viewMode)) {
				incrementGroupSimpleChartValue(selectedTableSerialNum,
						columnIndex, selectedItemIndex);
			} else if (ViewModeUtil.isGroupCrossChartViewMode(this.viewMode)) {
				incrementGroupDoubleCrossChartValue(selectedTableSerialNum,
						selectedItemIndex);
			}
		}

		
	}


		void incrementGroupSimpleChartValue(int selectedTableSerialNum,
				int questionIndex, int selectedItemIndex) {
			add(new StringBuilder().append(selectedTableSerialNum).append(',')
					.append(questionIndex).append(',')
					.append(selectedItemIndex).toString());
		}

		void incrementDoubleCrossChartValue(int selectedItemIndex0,
				int selectedItemIndex1) {
			add(new StringBuilder().append(',').append(selectedItemIndex0)
					.append(',').append(selectedItemIndex1).toString());
		}

		void incrementTripleCrossChartValue(int selectedItemIndex0,
				int selectedItemIndex1, int selectedItemIndex2) {
			add(new StringBuilder().append(selectedItemIndex0).append(',')
					.append(selectedItemIndex1).append(',').append(
							selectedItemIndex2).toString());
		}

		void incrementGroupDoubleCrossChartValue(int selectedTableSerialNum,
				int selectedItemIndex0) {
			add(new StringBuilder().append(',').append(selectedItemIndex0)
					.append(',').append(selectedTableSerialNum).toString());
		}

		void incrementGroupTripleCrossChartValue(int selectedTableSerialNum,
				int selectedItemIndex0, int selectedItemIndex1) {
			add(new StringBuilder().append(selectedTableSerialNum).append(',')
					.append(selectedItemIndex0).append(',').append(
							selectedItemIndex1).toString());
		}

		void incrementCrossTableList(int autoSelectedQuestionIndex,
				int userSelectedQuestionSerialNum, int selectedItemIndex0,
				int selectedItemIndex1) {
			add(new StringBuilder().append(autoSelectedQuestionIndex).append(
					',').append(userSelectedQuestionSerialNum).append(',')
					.append(selectedItemIndex0).append(',').append(
							selectedItemIndex1).toString());
		}

		void incrementValue(int selectedTableSerialNum, int[] selectedItemIndex) {
			if (ViewModeUtil.isCrossChartViewMode(this.viewMode)) {
				if (selectedItemIndex.length == 2) {
					incrementDoubleCrossChartValue(selectedItemIndex[0],
							selectedItemIndex[1]);
				} else if (selectedItemIndex.length == 3) {
					incrementTripleCrossChartValue(selectedItemIndex[0],
							selectedItemIndex[1], selectedItemIndex[2]);
				}
			} else if (ViewModeUtil.isGroupCrossChartViewMode(this.viewMode)) {
				if (selectedItemIndex.length == 1) {
					incrementGroupDoubleCrossChartValue(selectedTableSerialNum,
							selectedItemIndex[0]);
				} else if (selectedItemIndex.length == 2) {
					incrementGroupTripleCrossChartValue(selectedTableSerialNum,
							selectedItemIndex[0], selectedItemIndex[1]);
				}
			}
		}
	}

	static class SimpleStatisticsItemBuilder extends StatFactory.StatItemFactory<String> {

		public void processRow(RowAccessor rowAccessor, PageTaskAccessor pageTaskAccessor, Row row, 
				MultiRowSelection abstractSelection, FormMaster master,
				SourceDirectory sourceDirectory, int rowIndex) {

			int[] selectedItemIndex = new int[numSelectedQuestions];
			int userSelectedQuestionSerialNum = 0;
			for (int columnIndex : selectedQuestionIndexSet) {
				
				int pageNumber = master.getFormAreaList(columnIndex).get(0).getPage();
				float densityThreshold = ((SourceConfig) sourceDirectory
						.getConfiguration().getConfig().getSourceConfig(sourceDirectory.getRelativePath(), pageNumber))
						.getMarkRecognitionConfig().getMarkRecognitionDensityThreshold();
				
				processQuestion(selectedTableSerialNum, master, row,
						selectedItemIndex, userSelectedQuestionSerialNum,
						columnIndex, densityThreshold);
				userSelectedQuestionSerialNum++;
			}
			incrementValue(selectedTableSerialNum, selectedItemIndex);
		}

		void incrementSimpleChartValue(int columnIndex, int selectedItemIndex) {
			this.result.add(new StringBuilder().append(columnIndex).append(',')
					.append(selectedItemIndex).toString());
		}
	}

	static class SingleCrossStatisticsItemBuilder extends
			StatFactory.StatItemFactory {

		public SingleCrossStatisticsItemBuilder() throws IOException {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	static class DoubleCrossStatisticsItemBuilder extends
			StatFactory.StatItemFactory {

		public DoubleCrossStatisticsItemBuilder() throws IOException {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	static class TripleCrossStatisticsItemBuilder extends
			StatFactory.StatItemFactory {

		public TripleCrossStatisticsItemBuilder() throws IOException {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	static class GroupSingleCrossStatisticsItemBuilder extends
			StatFactory.StatItemFactory {

		public GroupSingleCrossStatisticsItemBuilder() throws IOException {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	static class GroupDoubleCrossStatisticsItemBuilder extends
			StatFactory.StatItemFactory {

		public GroupDoubleCrossStatisticsItemBuilder() throws IOException {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	static class GroupTripleCrossStatisticsItemBuilder extends
			StatFactory.StatItemFactory {

		public GroupTripleCrossStatisticsItemBuilder() throws IOException {
			super();
			// TODO Auto-generated constructor stub
		}

	}

	static class CrossChartListStatisticsItemBuilder extends
			StatFactory.StatItemFactory {

		public void processRow() {
			if (numSelectedQuestions == 0 || 10 < numSelectedQuestions) {
				return;
			}
			for (int columnIndex = 0; columnIndex < master.getNumColumns(); columnIndex++) {
				if (!master.getFormAreaList(columnIndex).get(0).isMarkArea()) {
					continue;
				}

				int selectedItemIndex0 = getSelectedSelectOneFormAreaIndex(
						(MarkAreaAnswer) row.getAnswer(columnIndex),
						densityThreshold);
				int userSelectedQuestionSerialNum = 0;
				for (int userSelectedQuestionIndex : selectedQuestionIndexSet) {
					if (columnIndex == userSelectedQuestionIndex
							|| !master.getFormAreaList(
									userSelectedQuestionIndex).get(0)
									.isMarkArea()) {
						continue;
					}
					int selectedItemIndex1 = getSelectedSelectOneFormAreaIndex(
							(MarkAreaAnswer) row
									.getAnswer(userSelectedQuestionIndex),
							densityThreshold);
					incrementCrossTableList(columnIndex,
							userSelectedQuestionSerialNum, selectedItemIndex1,
							selectedItemIndex0);
					userSelectedQuestionSerialNum++;
				}
			}
		}

	}
	*/
}