package net.sqs2.omr.result.model;


import net.sqs2.omr.model.PageAreaResult;

import org.apache.commons.codec.binary.Base64;

public class FormAreaItem extends ModelItemMap {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	protected String createBase64ImageSrc(PageAreaResult pageAreaResult) {
		String type = pageAreaResult.getImageType();
		byte[] bytes = pageAreaResult.getImageByteArray();
		if (bytes == null) {
			throw new IllegalArgumentException("bytes == null");
		}
		StringBuilder b = new StringBuilder(128);
		b.append("data:image/");
		b.append(type);
		b.append(";base64,");
		b.append(new String(Base64.encodeBase64(bytes)));
		return b.toString();
	}
}
