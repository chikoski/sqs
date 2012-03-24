package net.sqs2.omr.result.contents;

public class ViewMode {
	public static boolean isFormEditMode(int viewMode){
		return viewMode == 0;
	}
	
	public static boolean isExportMode(int viewMode){
		return viewMode == 2;
	}
	public static boolean isSimpleChartViewMode(int viewMode){
		return viewMode == 3;
	}
	public static boolean isGroupSimpleChartViewMode(int viewMode){
		return viewMode == 4;
	}
	public static boolean isCrossChartViewMode(int viewMode){
		return viewMode == 5;
	}
	public static boolean isGroupCrossChartViewMode(int viewMode){
		return viewMode == 6;
	}
	public static boolean isCrossChartListViewMode(int viewMode){
		return viewMode == 7;
	}

	public static boolean isPageErrorViewMode(int viewMode){
		return viewMode == 9;
	}
	
	public static boolean isLowReliabilityViewMode(int viewMode){
		return viewMode == 10;
	}
	
	public static boolean isNoAnswerErrorViewMode(int viewMode){
		return viewMode == 11;
	}
	
	public static boolean isMultiAnswerErrorViewMode(int viewMode){
		return viewMode == 12;
	}

}
