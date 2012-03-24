/*
 * 

 PathUtilTest.java

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

import java.util.HashSet;

import net.sqs2.util.PathUtil;
import junit.framework.TestCase;

public class PathUtilTest extends TestCase {
	
	public void testCount(){
		assertEquals(4, PathUtil.count("/abcde/ABCED/xyzyz/hoge", '/'));
		assertEquals(3, PathUtil.count("xxx/abcde/ABCED/xyzyzhoge", '/'));
		assertEquals(4, PathUtil.count("xxx/abcde/ABCED/xyzyzhoge/", '/'));
	}

	public void testPathUtil(){
		assertEquals("abc", PathUtil.getSharedPrefix("abcde","abcDE"));
		assertEquals("abcde", PathUtil.getSharedPrefix("abcde","abcde"));
		assertEquals("", PathUtil.getSharedPrefix("Abcde","abcde"));
	}


	public void testSharedPathPrefix(){
		assertEquals("/abcde", PathUtil.getSharedPathPrefix("/abcde/ABCD/xyzyz/hoge","/abcde/DEFGH/xxxx/yyyy", '/'));
		assertEquals("/abcde", PathUtil.getSharedPathPrefix("/abcde/ABCD/xyzyz/hoge","/abcde/DEFGH/xxxx/yyyy/", '/'));
		assertEquals("/abcde", PathUtil.getSharedPathPrefix("/abcde/ABCD/xyzyz/hoge/","/abcde/DEFGH/xxxx/yyyy", '/'));
		assertEquals("/abcde", PathUtil.getSharedPathPrefix("/abcde/ABCD/xyzyz/hoge/","/abcde/DEFGH/xxxx/yyyy/", '/'));
	}


	public void testRelativePathUtil(){
		assertEquals("../../ABCD/xyzyz/hoge", PathUtil.getRelativePath("/abcde/ABCD/xyzyz/hoge","/abcde/DEFGH/xxxx/yyyy", '/'));
		assertEquals("../../../ABCD/xyzyz/hoge", PathUtil.getRelativePath("/abcde/ABCD/xyzyz/hoge","/abcde/DEFGH/xxxx/yyyy/", '/'));
		assertEquals("../../ABCD/xyzyz/hoge/", PathUtil.getRelativePath("/abcde/ABCD/xyzyz/hoge/","/abcde/DEFGH/xxxx/yyyy", '/'));
		assertEquals("../../../ABCD/xyzyz/hoge/", PathUtil.getRelativePath("/abcde/ABCD/xyzyz/hoge/","/abcde/DEFGH/xxxx/yyyy/", '/'));
		assertEquals("../b0004.tif/18-0.png", PathUtil.getRelativePath("b0004.tif/18-0.png", "cuc2006-autumn.pdf/index.html", '/'));
	}

	public void testImageRelativePath(){
		assertEquals("../nagasa/a012.tif", PathUtil.getRelativePath("/home/hiroya/Desktop/nagasa/a012.tif","/home/hiroya/Desktop/nagasa-RESULT/errorlog.xhtml", '/'));
	}

	public void testGetSharedSuperDirectorySet(){
		String[] dirSrc = {"/home/root1/root2/a/b/c", "/home/root1/root2/a/B/c", "/home/root1/root2/a/b/d", "/home/root1/root2/b/d", "/home/root1/root2/a/b/x/y", "/home/root1/root2/a/b/d/e"};
		String[] assumedSrc = {"/home","/","/home/root1", "/home/root1/root2", "/home/root1/root2/a", "/home/root1/root2/a/b", "/home/root1/root2/a/b/d"};

		HashSet<File> fileSet = new HashSet<File>();
		for(String d: dirSrc){
			fileSet.add(new File(d));
		}
		HashSet<String> assumedStringSet = new HashSet<String>();
		for(String d: assumedSrc){
			assumedStringSet.add(d);
		}

		HashSet<File> assumed = new HashSet<File>();
		for(String s: assumedSrc){
			assumed.add(new File(s));
		}

		assertEquals(assumed, PathUtil.getSharedSuperDirectorySet(fileSet));
	}

}

