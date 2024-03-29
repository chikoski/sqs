/**
 *  FrameConfig.java

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

 Created on 2007/04/07
 Author hiroya
 */
package net.sqs2.omr.source.config;

import java.io.Serializable;

public class FrameConfig implements Serializable{
	private static final long serialVersionUID = 0L;

	PageGuideAreaConfig pageGuideAreaConfig;
	ValidationConfig validationConfig;
	
	public FrameConfig(){
	}

	public PageGuideAreaConfig getPageGuideAreaConfig() {
		return pageGuideAreaConfig;
	}

	public void setPageGuideAreaConfig(PageGuideAreaConfig pageGuideAreaConfig) {
		this.pageGuideAreaConfig = pageGuideAreaConfig;
	}

	public ValidationConfig getValidationConfig() {
		return validationConfig;
	}

	public void setValidationConfig(ValidationConfig validationConfig) {
		this.validationConfig = validationConfig;
	}
}
