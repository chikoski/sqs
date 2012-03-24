package net.sqs2.omr.result.export.textarea;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import net.sqs2.omr.master.FormArea;
import net.sqs2.omr.master.FormMaster;
import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.PageTask;
import net.sqs2.omr.model.PageTaskResult;
import net.sqs2.omr.result.export.ResultDirectoryUtil;
import net.sqs2.omr.session.event.PageEvent;
import net.sqs2.omr.session.event.QuestionEvent;
import net.sqs2.omr.session.event.QuestionItemEvent;

public class FormAreaImageWriter {
	
	public static void exportFormAreaImageFile(QuestionItemEvent questionItemEvent){
		exportFormAreaImageFile(questionItemEvent.getPageEvent(),
				questionItemEvent.getQuestionEvent(), questionItemEvent.getFormArea());
	}

	private static void exportFormAreaImageFile(PageEvent pageEvent, QuestionEvent questionEvent, FormArea formArea) {
		byte[] bytes = getImageBytes(pageEvent, questionEvent, formArea);
		if (bytes == null) {
			return;
		}
		File formAreaImageFile = FormAreaResultDirectoryUtil.createFormAreaRowFile(questionEvent, formArea);

		if (ResultDirectoryUtil.isValidOldFileIs(pageEvent, formAreaImageFile)) {
			return;
		}

		try {
			OutputStream formAreaImageOutputStream = new BufferedOutputStream(new FileOutputStream(formAreaImageFile));
			formAreaImageOutputStream.write(bytes);
			formAreaImageOutputStream.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	private static byte[] getImageBytes(PageEvent pageEvent, QuestionEvent questionEvent, FormArea formArea) {
		FormMaster master = questionEvent.getFormMaster();
		int formAreaIndexInPage = master.getAreaIndexInPage(formArea.getQID());
		PageTask pageTask = pageEvent.getPageTask();
		if (pageTask == null) {
			return null;
		}
		PageTaskResult taskResult = pageTask.getPageTaskResult();
		if (taskResult == null) {
			return null;
		}
		PageAreaResult result = taskResult.getPageAreaResultList().get(formAreaIndexInPage);
		return result.getImageByteArray();
	}
}
