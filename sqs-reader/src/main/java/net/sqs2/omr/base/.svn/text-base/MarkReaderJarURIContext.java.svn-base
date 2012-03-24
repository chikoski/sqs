/*

 MarkReaderJarURIContext.java
 
 Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2005/08/09

 */
package net.sqs2.omr.base;

public class MarkReaderJarURIContext {
	private static String IMAGE_URI_SUFFIX = "/image/";
	private static String PATTERN_URI_SUFFIX = "/pattern/";
	private static String SOUND_URI_SUFFIX = "/sound/";

	private static final String CLASS_SCHEME_PREFIX = "class://";
	private static final String SLASH_SEPARATOR = "/";
	private static final String XSLT_BASE_URI = CLASS_SCHEME_PREFIX
			+ net.sqs2.omr.base.MarkReaderJarURIContext.class.getCanonicalName() + SLASH_SEPARATOR;
	private static final String IMAGE_BASE_URI = CLASS_SCHEME_PREFIX
			+ net.sqs2.omr.base.MarkReaderJarURIContext.class.getCanonicalName() + IMAGE_URI_SUFFIX;
	private static final String PATTERN_BASE_URI = CLASS_SCHEME_PREFIX
			+ net.sqs2.omr.base.MarkReaderJarURIContext.class.getCanonicalName()
			+ PATTERN_URI_SUFFIX;
	private static final String SOUND_BASE_URI = CLASS_SCHEME_PREFIX
			+ net.sqs2.omr.base.MarkReaderJarURIContext.class.getCanonicalName()
			+ SOUND_URI_SUFFIX;

	public static String getXSLTBaseURI() {
		return XSLT_BASE_URI;
	}

	public static String getImageBaseURI() {
		return IMAGE_BASE_URI;
	}

	public static String getPatternBaseURI() {
		return PATTERN_BASE_URI;
	}

	public static String getSoundBaseURI() {
		return SOUND_BASE_URI;
	}
}
