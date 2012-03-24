package net.sqs2.omr.result.contents;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.source.SessionSource;

public class StatisticsContentsJSONFactory extends StatisticsContentsFactory{
	protected PrintWriter w;
	public StatisticsContentsJSONFactory(PrintWriter w, SessionSource sessionSource, int viewMode)throws IOException{
		super(sessionSource, viewMode);
		this.w = w;	
	}
	
	@Override
	public void create(FormMaster master,
			Set<Integer> selectedTableIndexSet,
			Set<Integer> selectedRowIndexSet,
			Set<Integer> selectedQuestionIndexSet){
		
		if(selectedTableIndexSet == null || selectedTableIndexSet.isEmpty()){
			this.w.println("contentsHandler.statValues = {};");
			return;
		}

		super.create(master, selectedTableIndexSet,
				selectedRowIndexSet,
				selectedQuestionIndexSet);
		
		this.w.print("contentsHandler.statValues={");
		boolean hasPrinted = false;
		for(String key: this.values.uniqueSet()){
			if(hasPrinted){
				this.w.print(",");
			}else{
				hasPrinted = true;
			}
			this.w.print("'");
			this.w.print(key);
			this.w.print("':");
			this.w.print(this.values.getCount(key));
		}
		this.w.println("};");	
	}

}
