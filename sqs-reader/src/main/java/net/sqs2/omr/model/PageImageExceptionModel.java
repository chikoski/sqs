/*

 PageTaskExceptionModel.java

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

 Created on Apr 7, 2007

 */
package net.sqs2.omr.model;

import java.io.Serializable;

public abstract class PageImageExceptionModel extends PageTaskExceptionModel implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	protected int width;
	protected int height;

	public PageImageExceptionModel(PageID pageID, int width, int height) {
		super(pageID);
		this.width = width;
		this.height = height;
	}

	public int getWidth() {
		return this.width;
	}

	public int getHeight() {
		return this.height;
	}

}
