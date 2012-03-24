/**
 * MarkReadStatus.java

 Copyright 2009 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Author hiroya
 */

package net.sqs2.omr.session.output;

public class MarkReadStatus {

	public class Select1Status {
		int numOneSelectedQuestions = 0;
		int numNoAnsweredQuestions = 0;
		int numMultiPleSelectedQuestions = 0;
		int numQuestions = 0;

		public int getNumOneSelectedQuestions() {
			return this.numOneSelectedQuestions;
		}

		public int getNumNoAnsweredQuestions() {
			return this.numNoAnsweredQuestions;
		}

		public int getNumMultipleAnsweredQuestions() {
			return this.numMultiPleSelectedQuestions;
		}

		public int getNumQuestions() {
			return this.numQuestions;
		}

		public void add(Select1Status target) {
			this.numOneSelectedQuestions += target.getNumOneSelectedQuestions();
			this.numNoAnsweredQuestions += target.getNumNoAnsweredQuestions();
			this.numMultiPleSelectedQuestions += target.getNumMultipleAnsweredQuestions();
			this.numQuestions += target.getNumQuestions();
		}
	}

	public class SelectStatus {
		int numMultipleSelectedMarks = 0;
		int numQuestions = 0;

		public int getNumMultipleSelectedMarks() {
			return this.numMultipleSelectedMarks;
		}

		public int getNumQuestions() {
			return this.numQuestions;
		}

		public void add(SelectStatus target) {
			this.numMultipleSelectedMarks += target.getNumMultipleSelectedMarks();
			this.numQuestions += target.getNumQuestions();
		}
	}

	int numPages = 0;
	int numErrorPages = 0;
	Select1Status select1Status;
	SelectStatus selectStatus;

	MarkReadStatus() {
		this.select1Status = new Select1Status();
		this.selectStatus = new SelectStatus();
	}

	public int getNumPages() {
		return this.numPages;
	}

	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}

	public void addNumErrorPage() {
		this.numErrorPages++;
	}

	public int getNumErrorPages() {
		return this.numErrorPages;
	}

	public int getNumQuestions() {
		return this.select1Status.getNumQuestions() + this.selectStatus.getNumQuestions();
	}

	public Select1Status getSelect1Status() {
		return this.select1Status;
	}

	public SelectStatus getSelectStatus() {
		return this.selectStatus;
	}

	public void add(MarkReadStatus target) {
		this.select1Status.add(target.getSelect1Status());
		this.selectStatus.add(target.getSelectStatus());
	}
}
