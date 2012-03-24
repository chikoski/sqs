package net.sqs2.omr.swing;

import net.sqs2.omr.result.event.SpreadSheetExportEventProducer;
import net.sqs2.omr.result.export.MarkReadStatus;

public interface SessionExportPanel {

	public abstract void updateValue(final MarkReadStatus status);

	public abstract void reset();

	public abstract void bindSpreadSheetExportEventProducer(
			SpreadSheetExportEventProducer eventProducer,
			MarkAreasTableModel noAnswerMarkAreasTableModel,
			MarkAreasTableModel multipleAnswersAnswerMarkAreasTableModel);
	public abstract SessionExportProgressPanel getSessionExportProgressPanel();
}