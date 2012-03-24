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
package net.sqs2.omr.result.context;

import java.util.Map;

public class ResultBrowserParam{

	public static final int NUM_MAX_ANSWER_ITEMS = 20;
	
	protected String updater;
	protected String viewMode;
	protected String axis;
	protected int numMaxAnswerItems;

	public ResultBrowserParam(Map<String,String> paramMap) {
		this.updater = (String)paramMap.get("u");
		this.viewMode = (String)paramMap.get("v");
		this.axis = (String)paramMap.get("axis");

		if(paramMap.get("N") == null || "".equals((String)paramMap.get("N"))){
			this.numMaxAnswerItems = NUM_MAX_ANSWER_ITEMS;
		}else{
			this.numMaxAnswerItems = Integer.parseInt((String)paramMap.get("N"));
		}
	}

	public ResultBrowserParam() {
	}

	public int getNumMaxAnswerItems() {
		return this.numMaxAnswerItems;
	}

	public String getViewMode() {
		return this.viewMode;
	}

	public String getCrossTableAxis() {
		return this.axis;
	}

	public String getUpdater() {
		return this.updater;
	}

}
