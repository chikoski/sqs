package net.sqs2.omr.result.model;

import java.util.List;

import net.sqs2.omr.model.PageAreaResult;
import net.sqs2.omr.model.TextAreaAnswer;

public class FreeAnswerItem extends FormAreaItem {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public FreeAnswerItem(TextAreaAnswer textAreaAnswer, List<PageAreaResult> pageAreaResultList, int formAreaIndex, String queryParamString, boolean isBase64ImageSourceSupported) {
			String value = textAreaAnswer.getValue();
			if (value != null) {
				put(Label.FreeAnswer.VALUE, value.replace("'", "\\'"));
			} else {
				putNull(Label.FreeAnswer.VALUE);
			}
			if (isBase64ImageSourceSupported) {
				put(Label.FreeAnswer.IMAGE_SOURCE_URI, queryParamString);
			} else {
				put(Label.FreeAnswer.IMAGE_SOURCE_URI, createBase64ImageSrc(pageAreaResultList.get(formAreaIndex)));
			}
	}
}
