/*
 * 

 FileUtilTest.java

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
 */
package net.sqs2.util;

import java.io.File;


import junit.framework.TestCase;

public class FileUtilTest extends TestCase {

	public void testGetSuffix(){
		assertEquals("hoge", FileUtil.getSuffix(new File("/foo/VAR/hoge.hoge")));
		assertEquals("ext", FileUtil.getSuffix("/foo/var/hoge.ext"));
		assertEquals("", FileUtil.getSuffix("/foo/var/hoge"));
	}

	public void testGetBasepath(){
		assertEquals("/foo/var/hoge", FileUtil.getBasepath("/foo/var/hoge.ext"));
		assertEquals(null, FileUtil.getBasepath("/foo/var/hoge"));
	}

	public void testGetBasename(){
		assertEquals("hoge", FileUtil.getBasename("/foo/var/hoge.ext"));
		assertEquals("hoge", FileUtil.getBasename("/foo/var/hoge"));
		assertEquals("hoge", FileUtil.getBasename("/foo/var/hoge/"));
	}

}
