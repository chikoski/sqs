package net.sqs2.omr.result.model;

import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.TreeSet;

public class MarkAreaAnswerItemSet extends TreeSet<MarkAreaAnswerItem>{
	private static final long serialVersionUID = 0L;
	
	public MarkAreaAnswerItemSet(MarkAreaAnswer answer){
		super(new Comparator<MarkAreaAnswerItem>(){
			public int compare(MarkAreaAnswerItem b, MarkAreaAnswerItem a){
				double value = Math.ceil(a.getDensity() - b.getDensity());
				if(0 == value){
					return a.getItemIndex() - b.getItemIndex();
				}else if(0 < value){
					return (int)Math.ceil(value); 
				}else{
					return (int)Math.floor(value);
				}
			}
			
			public boolean equals(Object obj){
				return false;
			}
		});	

		for(MarkAreaAnswerItem markAreaAnswerItem : answer.getMarkAreaAnswerItemArray()){
			add(markAreaAnswerItem);
		}
	}

	public LinkedList<MarkAreaAnswerItem> getMarkedAnswerItems(float densityThreshold, float recognitionMargin){
		LinkedList<MarkAreaAnswerItem> list = new LinkedList<MarkAreaAnswerItem>(); 
		for(Iterator<MarkAreaAnswerItem> it = iterator(); it.hasNext();){
			MarkAreaAnswerItem item = it.next();
			if(isSelected(densityThreshold, recognitionMargin, last(), item)){
				list.add(item);
			}
		}
		return list;
	}

	public boolean[] getIsSelectedBooleanArray(float densityThreshold, float recognitionMargin){
		boolean[] ret = new boolean[this.size()];
		for(Iterator<MarkAreaAnswerItem> it = iterator(); it.hasNext();){
			MarkAreaAnswerItem item = it.next();
			if(isSelected(densityThreshold, recognitionMargin, last(), item)){
				ret[item.getItemIndex()] = true;
			}
		}
		return ret;
	}
	
	public int getNumSelected(float densityThreshold, float recognitionMargin){
		int ret = 0;
		for(Iterator<MarkAreaAnswerItem> it = iterator(); it.hasNext();){
			MarkAreaAnswerItem item = it.next();
			if(isSelected(densityThreshold, recognitionMargin, last(), item)){
				ret++;
			}
		}
		return ret;

	}
	
	private boolean isSelected(float densityThreshold, float recognitionMargin, MarkAreaAnswerItem last, MarkAreaAnswerItem item) {
		return (item.isManualMode() && item.isManualSelected()) || 
			(item.getDensity() < densityThreshold &&(last != null && (last.getDensity() - item.getDensity()) < recognitionMargin));
	}
	
}
