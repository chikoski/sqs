/**
 * TemplateExporter.java

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

package net.sqs2.omr.result.export;

import java.io.IOException;
import java.util.HashMap;

import net.sqs2.omr.base.Messages;
import net.sqs2.omr.model.AppConstants;
import net.sqs2.template.TemplateLoader;

public class HTMLWriter {
	String skin;
	protected TemplateLoader loader;

	public HTMLWriter(String skin) throws IOException {
		this.skin = skin;
		this.loader = new TemplateLoader(AppConstants.USER_CUSTOMIZED_CONFIG_DIR, "ftl", skin);
	}

	class FormMasterMetadata {
		FormMasterMetadata() {
		}
	}
	
	protected void registFTLParameters(HashMap<String, Object> map){
		for(String key: new String[]{
				"folderPrefixLabel",
				"contentsOfResultLabel",
				"listOfSpreadSheetsLabel",
				"xlsSpreadSheetLabel",
				"csvSpreadSheetLabel",
				"listOfResultsLabel",
				"listOfFreeAnswersLabel",
				"listOfStatisticsLabel"
		}){
			map.put(key, Messages._("result.directoryIndex."+key));
		}
	}

}
