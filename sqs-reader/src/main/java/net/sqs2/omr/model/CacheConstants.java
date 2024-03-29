/*

 CacheConstants.java

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

 Created on 2007/01/11

 */

package net.sqs2.omr.model;

import java.io.File;


public class CacheConstants {
	public static final String CACHE_DB_VERSION = "1.3.1";
	public static final String CACHE_ROOT_DIRNAME = "_CACHE";

	public static String getCacheDirname() {
		return AppConstants.RESULT_DIRNAME + File.separatorChar + CACHE_ROOT_DIRNAME + File.separatorChar
				+ CACHE_DB_VERSION;
	}
}
