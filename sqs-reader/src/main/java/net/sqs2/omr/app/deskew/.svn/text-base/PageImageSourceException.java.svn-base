/*

 PageSourceException.java

 Copyright 2007 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2009/01/11

 */
package net.sqs2.omr.app.deskew;

import java.io.Serializable;

import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageImageExceptionModel;
import net.sqs2.omr.model.PageTaskException;

public class PageImageSourceException extends PageTaskException implements Serializable {
	private static final long serialVersionUID = 0L;

	public static class PageSourceExceptionModel extends PageImageExceptionModel {
		private static final long serialVersionUID = 0L;
		float x, y;

		public PageSourceExceptionModel(PageID pageID, int w, int h, float x, float y) {
			super(pageID, w, h);
			this.pageID = pageID;
			this.x = x;
			this.y = y;
		}

		public PageID getPageID() {
			return this.pageID;
		}

		public float getX() {
			return this.x;
		}

		public float getY() {
			return this.y;
		}

		@Override
		public String getLocalizedMessage() {
			return "PageSourceException";
		}
	}

	public PageImageSourceException(PageID pageID, int w, int h, float x, float y) {
		super(new PageSourceExceptionModel(pageID, w, h, x, y));
	}
}
