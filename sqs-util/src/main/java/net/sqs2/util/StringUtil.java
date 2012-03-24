/*
 * 

 StringUtil.java

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
 */
package net.sqs2.util;

import java.util.ArrayList;
import java.util.List;

public class StringUtil {

	public static int count(String src, char delim) {
		int ret = 0;
		int len = src.length();
		for (int i = 0; i < len; i++) {
			if (src.charAt(i) == delim) {
				ret++;
			}
		}
		return ret;
	}

	public static List<String> split(String src, char separator) {
		List<String> ret = new ArrayList<String>();
		int p = 0;
		while (0 <= (p = src.indexOf(separator))) {
			ret.add(src.substring(0, p));
			src = src.substring(p + 1);
		}
		ret.add(src);
		return ret;
	}

	/**
	 * return prefix string by delim(remove delim string and its following
	 * charactors). ex. StringUtil.chop("FOOVAR.ext", "."); // "FOOVAR"
	 * StringUtil.chop("123456789", "456"); // "123"
	 * 
	 * @param src
	 * @param delim
	 * @return prefix string by delim
	 */
	public static String chop(String src, String delim) {
		int index = -1;
		if (0 <= (index = src.lastIndexOf(delim))) {
			return src.substring(0, index);
		} else {
			return src;
		}
	}

	/**
	 * escape \, \n, \r, \t to Tab Separated Values Format(TSV).
	 * 
	 * @param src
	 * @return escaped string
	 */
	public static String escapeTSV(String src) {
		return src.replaceAll("\\\\", "\\\\\\\\").replaceAll("[\n\r]+", "\\\\n").replaceAll("\t", "\\\\t");
		// .replaceAll(",", "\\\\,");
	}

	/**
	 * unescape \, \n, \r, \t from Tab Separated Values Format(TSV).
	 * 
	 * @param src
	 * @return unescaped string
	 */
	public static String unescapeTSV(String src) {
		return src.replaceAll("\\\\\\\\", "\\\\").replaceAll("\\\\r", "").replaceAll("\\\\n", "\n")
				.replaceAll("\\\\t", "\t").replaceAll("\\\\,", ",");
	}

	/**
	 * replace all specified chars(multiple occured chars will be replaced).
	 * 
	 * @param src
	 * @param from
	 * @param to
	 * @return replaced strings
	 */
	public static String replaceAll(String src, char from, char to) {
		return replaceAll(new StringBuilder(), src, String.valueOf(from), String.valueOf(to)).toString();
	}

	/**
	 * replace all specified strings(multiple occured string will be replaced).
	 * 
	 * @param src
	 * @param from
	 * @param to
	 * @return replaced strings
	 */
	public static String replaceAll(String src, String from, String to) {
		return replaceAll(new StringBuilder(), src, from, to).toString();
	}

	private static StringBuilder replaceAll(StringBuilder buf, String src, String from, String to) {
		int index = 0;
		if (0 <= (index = src.indexOf(from))) {
			buf.append(src.substring(0, index));
			buf.append(to);
			replaceAll(buf, src.substring(index + from.length()), from, to);
		} else {
			buf.append(src);
		}
		return buf;
	}

	/**
	 * join list of strings by separator
	 * 
	 * @param stringList
	 *            list of strings
	 * @param separator
	 *            null is accepted (treated same as "").
	 * @return joined string
	 */
	public static String join(List<String> stringList, String separator) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < stringList.size(); i++) {
			if (i != 0) {
				if (separator != null) {
					ret.append(separator);
				}
			}
			ret.append(stringList.get(i));
		}
		return ret.toString();
	}

	/**
	 * join array of strings by separator
	 * 
	 * @param array
	 *            of strings
	 * @param separator
	 * @return joined string
	 */
	public static String join(Object[] array, String separator) {
		StringBuilder ret = new StringBuilder();
		for (int i = 0; i < array.length; i++) {
			if (i != 0) {
				if (separator != null) {
					ret.append(separator);
				}
			}
			ret.append(array[i]);
		}
		return ret.toString();
	}
}
