package net.sqs2.omr.result.servlet.writer;

import java.util.Set;

import net.sqs2.omr.master.FormMaster;

public interface ContentsWriter {
	public void create(FormMaster master, Set<Integer> selectedTableIndexSet, Set<Integer> selectedRowIndexSet, Set<Integer> selectedQuestionIndexSet);
}
