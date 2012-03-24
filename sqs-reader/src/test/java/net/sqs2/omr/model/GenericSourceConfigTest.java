/**
 *  SourceConfigTest.java
 
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
 
 Created on 2007/07/31
 Author hiroya
 */

package net.sqs2.omr.model;

import junit.framework.TestCase;
import net.sqs2.omr.model.GenericSourceConfig;

public class GenericSourceConfigTest extends TestCase {
	public void testCreateArgumentList() {

		String[] filenames = { "wed\\,thu\\,fri0099.tif", "[wed,thu,fri0099.tif]", "page0001.tif",
				"[page0001.tif]", "page0003.tif,page0005.tif,page0006.tif",
				"[page0003.tif, page0005.tif, page0006.tif]", "page0009.tif-page0012.tif",
				"[page0009.tif-page0012.tif]",
				"page0113.tif-page0115.tif,page0223.tif-page0399.tif,page0401.tif",
				"[page0113.tif-page0115.tif, page0223.tif-page0399.tif, page0401.tif]", "hoge\\-0001.tif",
				"[hoge\\-0001.tif]", };

		GenericSourceConfig sourceConfig = new GenericSourceConfig();

		for (int i = 0; i < filenames.length; i += 2) {
			sourceConfig.setFilename(filenames[i]);
			assertEquals(filenames[i + 1], sourceConfig.getFilenameArgument().toString());
		}
	}

}
