/**
 *  TextAreaAnswer.java
 
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
 
 Created on 2007/07/31
 Author hiroya
 */

package net.sqs2.omr.model;

import java.io.Serializable;

public class TextAreaAnswer extends Answer implements Serializable {
	private static final long serialVersionUID = 2L;

	private String value;
	private String textAreaFilePath;
	private boolean isNoAnswer;

	public TextAreaAnswer() {
	}

	@Override
	public int size() {
		return 1;
	}

	public String getValue() {
		return this.value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public void setTextAreaImageFilePath(String areaFilePath) {
		this.textAreaFilePath = areaFilePath;
	}

	public String getAreaImageFilePath() {
		return this.textAreaFilePath;
	}

	public boolean isNoAnser() {
		return this.isNoAnswer;
	}

	public void setNoAnswer(boolean isNoAnswer) {
		this.isNoAnswer = isNoAnswer;
	}

	@Override
	public String toString() {
		return "TextAreaAnswer: value=" + this.value + ", path=" + this.textAreaFilePath;
	}
}
