/*

 PageTaskException.java

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

 Created on Jan 7, 2007

 */
package net.sqs2.omr.model;

import java.io.Serializable;

public class PageTaskException extends Exception implements Serializable {
	private static final long serialVersionUID = 0;

	protected PageTaskExceptionModel exceptionModel;

	public PageTaskException() {
	}

	public PageTaskException(PageTaskExceptionModel exceptionModel) {
		this.exceptionModel = exceptionModel;
	}

	public PageTaskExceptionModel getExceptionModel() {
		return this.exceptionModel;
	}

	public PageTaskException(Exception ex) {
		super(ex);
	}

	@Override
	public String toString() {
		return getClass().getName() + "@" + this.getLocalizedMessage();
	}

	@Override
	public boolean equals(Object o) {
		try {
			return exceptionModel.equals(((PageTaskException)o).getExceptionModel());
		} catch (Exception ex) {
			return false;
		}
	}

	@Override
	public int hashCode() {
		return this.exceptionModel.hashCode();
	}

	public int compareTo(PageTaskException o) {
		return exceptionModel.compareTo(((PageTaskException)o).getExceptionModel());
	}

}
