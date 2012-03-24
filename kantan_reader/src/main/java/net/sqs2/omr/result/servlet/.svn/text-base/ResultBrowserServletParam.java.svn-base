/*

 ResultBrowserParam.java

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
package net.sqs2.omr.result.servlet;

import net.sqs2.omr.result.contents.ResultContentSelectorParam;

public class ResultBrowserServletParam extends ResultContentSelectorParam {
	
	protected String updater = null;
	protected int viewMode = -1;
	protected long sid;
	protected String axis;
	protected int answerItemIndex;
	protected int numMaxAnswerItems;

	protected boolean isMSIE;
		
	public ResultBrowserServletParam(){
	}
	
	public long getSid() {
		return this.sid;
	}
	
	public void setMSIE(boolean isMSIE){
		this.isMSIE = isMSIE;
	}

	public void setSid(long sid) {
		this.sid = sid;
	}

	public void setUpdater(String updater) {
		this.updater = updater;
	}

	public void setViewMode(int viewMode) {
		this.viewMode = viewMode;
	}

	public void setCrossTableAxis(String axis) {
		this.axis = axis;
	}

	public void setAnswerItemIndex(int answerItemIndex) {
		this.answerItemIndex = answerItemIndex;
	}

	public void setNumMaxAnswerItems(int numMaxAnswerItems) {
		this.numMaxAnswerItems = numMaxAnswerItems;
	}

	public int getAnswerItemIndex(){
		return this.answerItemIndex;
	}

	public int getNumMaxAnswerItems(){
		return this.numMaxAnswerItems;
	}

	public int getViewMode(){
		return this.viewMode; 
	}
	
	public String getCrossTableAxis(){
		return this.axis;
	}
	
	public boolean isMSIE(){
		return this.isMSIE;
	}
	
	public long getSessionID(){
		return this.sid;
	}

	public String getUpdater(){
		return this.updater;
	}

}
