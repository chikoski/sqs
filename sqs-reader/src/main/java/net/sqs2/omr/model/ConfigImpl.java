/*

 ConfigImpl.java

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

 Created on Apr 7, 2007

 */

package net.sqs2.omr.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ConfigImpl implements Config, Serializable {
	private final static long serialVersionUID = 0L;

	protected List<SourceConfig> sourceConfigList = new ArrayList<SourceConfig>();
	String version;

	public void addSource(SourceConfig sourceConfig) {
		this.sourceConfigList.add(sourceConfig);
	}

	public SourceConfig getPrimarySourceConfig() {
		return this.sourceConfigList.get(0);
	}

	public SourceConfig getSource(int index) {
		return this.sourceConfigList.get(index);
	}

	
	/**
	 * @param pageNumber starts with 1
	 */
	public SourceConfig getSourceConfig(String path, int pageNumber) throws ConfigSchemeException{
		for (SourceConfig sourceConfig : this.sourceConfigList) {
			if (sourceConfig.match(path, pageNumber)) {
				return sourceConfig;
			}
		}
		throw new ConfigSchemeException("no matched config file");
	}

	public SourceConfig getSourceConfig(String path) throws ConfigSchemeException{
		for (SourceConfig sourceConfig : this.sourceConfigList) {
			if (sourceConfig.match(path)) {
				return sourceConfig;
			}
		}
		throw new ConfigSchemeException("no matched config file");
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}
}
