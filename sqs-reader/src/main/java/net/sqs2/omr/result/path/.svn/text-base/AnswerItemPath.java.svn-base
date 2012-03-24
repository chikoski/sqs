/*

 AnswerItemPath.java

 Copyright 2008 KUBO Hiroya (hiroya@cuc.ac.jp).

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.

 Created on 2008/01/13

 */
package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64;

/**
 * This class is used in EditAnswerServlet(EditMarkArea)
 * @author hiroya
 *
 */
public class AnswerItemPath extends SingleRowSingleQuestionPath {

	protected int answerItemIndex;
	
	public AnswerItemPath(String pathInfo) {
		super(pathInfo);
		if(5 < paramDepth){
			this.answerItemIndex = URLSafeRLEBase64.decodeToInt(pathInfoArray[5]);
		}
	}

	public AnswerItemPath(long sessionID, 
			int masterIndex, int spreadSheetIndex, int rowIndex, int questionIndex, int answerItemIndex) {
		super(sessionID, masterIndex, spreadSheetIndex, rowIndex, questionIndex);
		this.answerItemIndex = answerItemIndex;
	}
	
	public int getAnswerItemIndex(){
		if(5 < paramDepth){
			return this.answerItemIndex;
		}
		throw new IllegalArgumentException();
	}
	
	public String toString(){
		return super.toString()+"/"+URLSafeRLEBase64.encode(answerItemIndex);
	}

}
