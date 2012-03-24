/*

 PBMImageFactory.java

 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2004/08/06

 */
package net.sqs2.image;

import java.awt.image.BufferedImage;
import java.awt.image.WritableRaster;
import java.io.IOException;
import java.io.InputStream;

public class PBMImageFactory {
	InputStream in;

	private static final int[] BLACK = new int[] { 0x00, 0x00, 0x00 };

	private static final int[] WHITE = new int[] { 0xff, 0xff, 0xff };

	public PBMImageFactory(InputStream in) {
		this.in = in;
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private int getHeight() throws IOException {
		int c;
		c = skipIfCommentLine();
		int height = parseIntValue(in, c);
		return height;
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private int getWidth() throws IOException {
		int c;
		in.read();
		c = skipIfCommentLine();
		int width = parseIntValue(in, c);
		return width;
	}

	/**
	 * @param in
	 * @return
	 * @throws IOException
	 */
	private int getType() throws IOException {
		int c;
		int type;
		c = skipIfCommentLine();
		if (c != 'P') {
			throw new IllegalArgumentException("pnm is not supported");
		}
		type = in.read() - '4';
		if (type < 0 || 2 < type) {
			throw new IllegalArgumentException("data type is invalid");
		}
		return type;
	}

	private int skipIfCommentLine() throws IOException {
		int c = in.read();
		if (c == '#') {
			while ((c = in.read()) != '\n') {
			}
			c = in.read();
		}
		return c;
	}

	private int parseIntValue(InputStream in, int c) throws IOException {
		int value = 0;
		while ('0' <= c && c <= '9') {
			value = value * 10 + c - '0';
			c = in.read();
		}
		return value;
	}

	public BufferedImage createByPBM() throws IOException, IllegalArgumentException {
		int type = getType();
		int width = getWidth();
		int height = getHeight();
		if (width < 1 || height < 1) {
			throw new IllegalArgumentException("width or height may be 0");
		}
		// int nColors = getNumColors(type);
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		WritableRaster ret = image.getRaster();
		switch (type) {
		case 0: {
			createImageByPBM(ret, width, height);
			break;
		}
		default:
			throw new IllegalArgumentException("not supported format type");
		}
		return image;
	}

	/**
	 * @param in
	 * @param width
	 * @param height
	 * @param ret
	 * @throws IOException
	 */
	private void createImageByPBM(WritableRaster ret, int width, int height) throws IOException {
		int length = width >> 3;
		byte[] line = new byte[(width + 7) / 8];
		for (int i = 0; i < height; i++) {
			// int l = in.read(line);
			for (int j = 0; j < length; j++) {
				int bits = ~line[j];
				int len;
				if (j * 8 <= width) {
					len = 8;
				} else {
					len = j * 8 - width;
				}
				for (int k = 0; k < len; k++) {
					if (((bits >> (8 - k)) & 1) == 1) {
						ret.setPixel(j * 8 + k, i, WHITE);
					} else {
						ret.setPixel(j * 8 + k, i, BLACK);
					}
				}
			}
		}
	}

}
