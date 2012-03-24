/**
 *  PathUtil.java

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

 Created on 2007/03/04
 Author hiroya
 */
package net.sqs2.util;

import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.commons.collections15.Bag;
import org.apache.commons.collections15.bag.HashBag;

public class PathUtil {
	public static String getSharedPrefix(String a, String b) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}
		int al = a.length();
		int bl = b.length();
		if (al == 0 || bl == 0) {
			return "";
		}
		for (int i = 0; i < al && i < bl; i++) {
			if (a.charAt(i) != b.charAt(i)) {
				return a.substring(0, i);
			}
		}
		if (al <= bl) {
			return a;
		} else {
			return b;
		}
	}

	/*
	 * public static Map<File,List<File>>
	 * getSharedSuperDirectoryMap(Iterable<File> files) {
	 * 
	 * Map<File,List<File>> ret = new HashMap<File,List<File>>(); Bag<File> bag
	 * = new HashBag<File>();
	 * 
	 * for (File file : files) { File superdir = file; do { //
	 * System.err.print("SRC:"+file); bag.add(superdir); //
	 * System.err.print("\t"); // System.err.print(superdir); } while ((superdir
	 * = superdir.getParentFile()) != null); }
	 * 
	 * for (File file : bag.uniqueSet()) { if (1 < bag.getCount(file)) {
	 * ret.add(file); } } return ret; }
	 */

	public static Set<File> getSharedSuperDirectorySet(Iterable<File> files) {

		Bag<File> bag = new HashBag<File>();
		for (File file : files) {
			File superdir = file;
			do {
				// System.err.print("SRC:"+file);
				bag.add(superdir);
				// System.err.print("\t");
				// System.err.print(superdir);
			} while ((superdir = superdir.getParentFile()) != null);
		}

		Set<File> ret = new HashSet<File>();
		for (File file : bag.uniqueSet()) {
			if (1 < bag.getCount(file)) {
				ret.add(file);
			}
		}
		return ret;
	}

	public static String getSharedPathPrefix(String a, String b, char separatorChar) {
		if (a == null || b == null) {
			throw new IllegalArgumentException();
		}

		int al = a.length();
		int bl = b.length();
		if (al == 0 || bl == 0) {
			return "";
		}
		int separatorPosition = getSharedPathPrefix(a, b, al, bl, separatorChar);
		if (separatorPosition == -1) {
			return "";
		} else {
			return a.substring(0, separatorPosition);
		}
	}

	private static int getSharedPathPrefix(String a, String b, int al, int bl, char separatorChar) {
		int separatorPosition = -1;
		for (int i = 0; i < al && i < bl; i++) {
			if (a.charAt(i) == separatorChar && b.charAt(i) == separatorChar) {
				separatorPosition = i;
			}
			if (a.charAt(i) != b.charAt(i)) {
				break;
			}
		}
		return separatorPosition;
	}

	public static int count(String src, char delim) {
		int count = 0;
		for (int i = 0; i < src.length(); i++) {
			char c = src.charAt(i);
			if (c == delim) {
				count++;
			}
		}
		return count;
	}

	/**
	 * 
	 * @param target
	 *            file
	 * @param base
	 *            file
	 * @return relative path
	 * @throws IOException
	 */
	public static String getRelativePath(File target, File base, char separatorChar) throws IOException {
		if (base.isDirectory()) {
			return getRelativePath(target.getCanonicalPath(), base.getCanonicalPath() + File.separatorChar
					+ "dummyfile", separatorChar);
		} else {
			return getRelativePath(target.getCanonicalPath(), base.getCanonicalPath(), separatorChar);
		}
	}

	/**
	 * 
	 * @param target
	 *            file
	 * @param base
	 *            file
	 * @return relative path
	 * @throws IOException
	 */
	public static String getRelativePath(String target, String base, char separatorChar) {
		if (base == null) {
			throw new IllegalArgumentException("param base is null");
		}
		if (target == null) {
			throw new IllegalArgumentException("param target is null");
		}
		target = target.trim();
		base = base.trim();
		int al = target.length();
		int bl = base.length();
		if (al == 0 || bl == 0) {
			return "";
		}
		String sharedPrefix = PathUtil.getSharedPathPrefix(target, base, separatorChar);
		int shift = 1;
		if(base.toLowerCase().endsWith("index.html") || base.toLowerCase().endsWith("index.htm")){
			shift = 0;
		}
		String uniqueSuffixA = target.substring(sharedPrefix.length() + shift);// remove
		// +1?
		String uniqueSuffixB = base.substring(sharedPrefix.length() + shift);// remove
		// +1?
		int uniqueSuffixDepthB = PathUtil.count(uniqueSuffixB, separatorChar);
		StringBuilder ret = new StringBuilder();

		for (int i = 0; i < uniqueSuffixDepthB; i++) {
			ret.append(".." + separatorChar);
		}
		ret.append(uniqueSuffixA);
		return ret.toString();
	}
}
