/**
 * Legend.java

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

package net.sqs2.omr.result.chart;

import java.util.List;

import net.sqs2.omr.master.FormArea;

public class Legend {
	int questionIndex;
	List<FormArea> formAreaList;
	FormArea primaryFormArea;

	public Legend(int questionIndex, List<FormArea> formAreaList) {
		this.questionIndex = questionIndex;
		this.formAreaList = formAreaList;
		this.primaryFormArea = formAreaList.get(0);
	}

	public int getQuestionIndex() {
		return this.questionIndex;
	}

	public List<FormArea> getFormAreaList() {
		return this.formAreaList;
	}

	public FormArea getPrimaryFormArea() {
		return this.primaryFormArea;
	}

}
