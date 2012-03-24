package net.sqs2.util;

import java.util.List;

import junit.framework.TestCase;

public class StringUtilTest extends TestCase {
	public void testSplit(){
		List<String> ret = null;
		
		ret = StringUtil.split("a/b/c/d", '/');
		assertEquals("a/b/c/d", StringUtil.join(ret, "/"));
		
		ret = StringUtil.split("abcd", '/');
		assertEquals("abcd", StringUtil.join(ret, "/"));
		
		ret = StringUtil.split("/a/b/c/d", '/');
		assertEquals("/a/b/c/d", StringUtil.join(ret, "/"));
		
		ret = StringUtil.split("a/b/c/d/", '/');
		assertEquals("a/b/c/d/", StringUtil.join(ret, "/"));

		assertEquals(1, StringUtil.split("a", '/').size());
		assertEquals(2, StringUtil.split("a/b", '/').size());
		assertEquals(3, StringUtil.split("a/b/c", '/').size());

	}
}
