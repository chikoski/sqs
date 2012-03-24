package net.sqs2.omr.result.model;

public interface Label{
	public static final String TEXT = "text"; 
	public static final String ICON = "icon";
	public static final String DISABLED = "disabled";
	
	public interface Master{
		public static final String NUM_PAGES = "numPages";
		public static final String MARK_AREA_MARGIN_HORIZONTAL = "markAreaMarginHorizontal";
		public static final String MARK_AREA_MARGIN_VERTICAL = "markAreaMarginVertical";
	}
	
	public interface SpreadSheetGroup{
		public static final String LABEL = "optgroup";
		public static final String ITEMS = "items";
	}
	
	public interface SpreadSheet{
		public static final String ICON = "icon";
		public static final String TEXT = "text"; // FIXME: This value should be "label"?
		public static final String DENSITY_THRESHOLD = "densityThreshold";
	}
	
	public interface RowGroup{
		public static final String ICON = "icon";
		public static final String LABEL = "optgroup";
		public static final String ITEMS = "items";
		public static final String SPREADSHEET_PATH = "path";
	}

	public interface Row{
		public static final String ICON = "icon";
		public static final String ITEMS = "items";
	}
	
	public interface Question{
		public static final String LABEL = "label";
		public static final String HINTS = "hints";
		public static final String QUESTION_ID = "qid";
		public static final String TYPE = "type";
		public static final String CLASS = "clazz";
		public static final String ITEMS = "items";
		
		public interface Item{
			public static final String LABEL = "l";
			public static final String VALUE = "v";
			public static final String CLASS = "c";
			public static final String PAGE = "p";
			public static final String X = "x";
			public static final String Y = "y";
			public static final String WIDTH = "w";
			public static final String HEIGHT = "h";
		}
	}
	
	public interface FreeAnswer{
		public static final String VALUE = "v";
		public static final String IMAGE_SOURCE_URI = "s";
	}

	public interface MarkAnswer{
		public static final String MANUAL_SELECT_MODE = "M";
		public static final String VALUE = "v";
		public static final String ITEMS = "i";
		
		public interface MarkItem{
			public static final String MANUAL_DELETED = "d";
			public static final String MANUAL_SELECTED = "M";
			public static final String IMAGE_SOURCE_URI = "s";
			public static final String MARK_DENSITY = "d";
		}
	}
	
	public interface Page{
		public static final String FILENAME = "filename";
	}
}
