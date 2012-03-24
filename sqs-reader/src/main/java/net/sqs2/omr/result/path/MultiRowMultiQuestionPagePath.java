/*

 MultiRowMultiQuestionPagePath.java

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

 Created on 2010/03/03

 */
package net.sqs2.omr.result.path;

import net.sqs2.omr.util.URLSafeRLEBase64.MultiSelection;

/**
 * This class is used in ViewAnswersServlet
 * @author hiroya
 *
 */
public class MultiRowMultiQuestionPagePath extends MultiRowMultiQuestionPath {

	protected int startIndex = -1;
	protected int endIndex = -1;
	
	public MultiRowMultiQuestionPagePath(String pathInfo) {
		super(pathInfo);
		try{
			if(5 < paramDepth){
				int index = Integer.parseInt(pathInfoArray[5]);
				if(0 <= index){
					this.startIndex = index;
				}else{
					this.endIndex = -1 * index;
				}
			}
		}catch(NumberFormatException ignore){
			paramDepth = -1;
		}
	}

	public MultiRowMultiQuestionPagePath(long sessionID, 
			int masterIndex,
			MultiSelection spreadSheetSelection, 
			MultiSelection rowSelection, 
			MultiSelection questionSelection, int pageIndex) {
		super(sessionID, masterIndex, spreadSheetSelection, rowSelection, questionSelection);
		this.startIndex = pageIndex;
	}
	
	public int getStartIndex(){
		if(5 < paramDepth){
			return this.startIndex;
		}
		throw new IllegalArgumentException();
	}
	
	public int getEndIndex(){
		if(5 < paramDepth){
			return this.endIndex;
		}
		throw new IllegalArgumentException();
	}
	
	public String toString(){
		return super.toString()+"/"+startIndex;
	}

}
