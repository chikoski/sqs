/*

 CornerBlockMissingExceptionCore.java

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

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.model.PageID;
import net.sqs2.omr.model.PageImageExceptionModel;

public class DeskewGuideMissingExceptionModel extends PageImageExceptionModel {

	private static final long serialVersionUID = 1L;

	int errorType;

	public DeskewGuideMissingExceptionModel(PageID pageID, int width, int height, int errorType) {
		super(pageID, width, height);
		this.errorType = errorType;
	}

	public int getErrorType() {
		return this.errorType;
	}

	@Override
	public String getLocalizedMessage() {
		return Messages.SESSION_ERROR_PAGEGUIDEBLOCKMISSING;
	}
}
