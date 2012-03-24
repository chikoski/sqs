/*
 * 
   FileUtil.java

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

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.io.output.ByteArrayOutputStream;

public class FileUtil {

	/**
	 * delete all files in specified path
	 * 
	 * @param absolutePath
	 */
	public static void clearDirectory(String absolutePath) {
		clearDirectory(new File(absolutePath));
	}

	/**
	 * delete all files in specified path
	 * 
	 * @param absolutePath
	 */
	public static void clearDirectory(File absolutePath) {
		File[] list = absolutePath.listFiles();
		for (int i = 0; i < list.length; i++) {
			list[i].delete();
		}
	}

	public static void deleteDirectory(File directory, FileFilter filter) {
		for (File file : directory.listFiles()) {
			if(filter.accept(file)){
				if (file.isDirectory()) {
					deleteDirectory(file, filter);
				}
				file.delete();
			}
		}
		if(filter.accept(directory)){
			directory.delete();
		}
	}
	
	/**
	 * delete all files in specified path
	 * 
	 * @param directory
	 */
	public static void deleteDirectory(File directory) {
		for (File file : directory.listFiles()) {
			if (file.isDirectory()) {
				deleteDirectory(file);
			}
			file.delete();
		}
		directory.delete();
	}

	/**
	 * find files which has specified suffix.
	 * 
	 * @param absolutePath
	 * @param suffix
	 * @return list of files which has specified suffix
	 */
	public static List<File> find(String absolutePath, final String suffix) {
		return find(new File(absolutePath), suffix);
	}

	/**
	 * find files which has specified suffix.
	 * 
	 * @param absolutePath
	 * @param suffix
	 * @return list of files which has specified suffix
	 */
	public static List<File> find(File absolutePath, final String suffix) {
		return find(absolutePath, new FileFilter() {
			public boolean accept(File file) {
				return file.getName().toLowerCase().endsWith(suffix);
			}
		});
	}

	/**
	 * find files which has specified suffix.
	 * 
	 * @param absolutePath
	 * @param filter
	 * @return list of files which has specified suffix
	 */
	public static List<File> find(File absolutePath, FileFilter filter) {
		List<File> sourceFileList = new LinkedList<File>();
		return addFoundFileToList(sourceFileList, absolutePath, filter);
	}

	private static List<File> addFoundFileToList(List<File> resultFileList, File currentPath, FileFilter filter) {
		File[] members = currentPath.listFiles(filter);
		if (members == null) {
			return new LinkedList<File>();
		}
		Arrays.sort(members);
		for (int i = 0; i < members.length; i++) {
			File file = members[i];
			resultFileList.add(file);
		}

		members = currentPath.listFiles();
		Arrays.sort(members);
		for (int i = 0; i < members.length; i++) {
			File file = members[i];
			if (file.isDirectory()) {
				addFoundFileToList(resultFileList, file, filter);
			}
		}
		return resultFileList;
	}

	public static void keywordSubstitution(File file, String from, String to) {
		keywordSubstitution(file, from, to, "UTF-8");
	}

	/**
	 * keyword replace in stream (replacing string cannot contain newline
	 * chars).
	 * 
	 * @param file
	 * @param from
	 *            from keyword
	 * @param to
	 *            to keyword
	 * @param encoding
	 * @return true: some keywords replaced, false: no keyword replaced
	 */
	public static boolean keywordSubstitution(File file, String from, String to, String encoding) {
		try {
			File tmpFile = File.createTempFile("tmp", "txt");
			tmpFile.deleteOnExit();
			boolean modified = keywordSubstitution(new FileInputStream(file), new FileOutputStream(tmpFile),
					from, to, encoding);
			if (modified) {
				file.delete();
				// System.err.println(tmpfile.renameTo(file));
				copy(tmpFile, file);
			} else {
				tmpFile.delete();
			}
			return modified;
		} catch (IOException e) {
			e.printStackTrace();
			new RuntimeException(e);
		}
		return false;
	}

	/**
	 * keyword replace in stream (replacing string cannot contain newline
	 * chars).
	 * 
	 * @param inputStream
	 * @param outputStream
	 * @param from
	 *            keyword(from)
	 * @param to
	 *            keyword(to)
	 * @param encoding
	 *            file encoding
	 * @return true: some keywords replaced, false: no keyword replaced
	 */
	public static boolean keywordSubstitution(InputStream inputStream, OutputStream outputStream, String from, String to, String encoding) {
		boolean modified = false;
		try {
			PrintWriter writer = new PrintWriter(new OutputStreamWriter(
					new BufferedOutputStream(outputStream), encoding));
			LineNumberReader reader = new LineNumberReader(new InputStreamReader(new BufferedInputStream(
					inputStream), encoding));
			String line = null;
			while ((line = reader.readLine()) != null) {
				String result = StringUtil.replaceAll(line, from, to);
				writer.println(result);
				if (!result.equals(line)) {
					modified |= true;
				}
			}
			reader.close();
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
			new RuntimeException(e);
		}
		return modified;
	}

	/**
	 * copy file.
	 * 
	 * @param from
	 *            file(from)
	 * @param to
	 *            file(to)
	 * @throws IOException
	 */
	public static void copy(File from, File to) throws IOException {
		pipe(new FileInputStream(from), new FileOutputStream(to));
	}
	
	public static byte[] getBytes(File file)throws IOException{
		InputStream in = new FileInputStream(file);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		pipe(in, out);
		return out.toByteArray();
	}

	/**
	 * pipe inputStream to outputStream. from and to stream will be closed.
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public static void pipe(InputStream from, OutputStream to) throws IOException {
		InputStream in = new BufferedInputStream(from);
		OutputStream out = new BufferedOutputStream(to);
		try {
			connect(in, out);
		} catch (IOException ex) {
			// ex.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (Exception ignore) {
			}
			try {
				in.close();
			} catch (Exception ignore) {
			}
		}
	}

	/**
	 * pipe inputStream to outputStream. from and to stream will be closed.
	 * 
	 * @param from
	 * @param to
	 * @throws IOException
	 */
	public static void connect(InputStream from, OutputStream to) throws IOException {
		byte[] buf = new byte[4096];
		int len = 0;
		while (0 < (len = from.read(buf, 0, 4096))) {
			to.write(buf, 0, len);
			// Thread.yield();
		}
		to.flush();
	}

	/**
	 * get suffix of filename ex. FileUtil.getSuffix(new
	 * File("/foo/var/hoge.ext")); // "ext"
	 * 
	 * @param file
	 * @return suffix of filename
	 */
	public static String getSuffix(File file) {
		return getSuffix(file.getName());
	}

	/**
	 * get suffix of filename ex. FileUtil.getSuffix("/foo/var/hoge.ext"); //
	 * "ext"
	 * 
	 * @param filename
	 * @return suffix of filename
	 */
	public static String getSuffix(String filename) {
		if (0 < filename.lastIndexOf('.')) {
			return filename.substring(filename.lastIndexOf('.') + 1);
		} else {
			return "";
		}
	}

	/**
	 * get basepath of filepath ex. FileUtil.getBasepath(new
	 * File("/foo/var/hoge.ext")); // "/foo/var/hoge"
	 * 
	 * @param file
	 * @return basepath of filename
	 */
	public static String getBasepath(File file) {
		return getBasepath(file.getAbsolutePath());
	}

	/**
	 * get basepath of filename ex. FileUtil.getBasepath("/foo/var/hoge.ext");
	 * // "/foo/var/hoge"
	 * 
	 * @param filename
	 * @return basepath of filename
	 */
	public static String getBasepath(String filename) {
		if (filename == null || filename.lastIndexOf('.') == -1) {
			return null;
		}
		return filename.substring(0, filename.lastIndexOf('.'));
	}

	/**
	 * get baseame of filepath ex. FileUtil.getBasename(new
	 * File("/foo/var/hoge.ext")); // "hoge"
	 * 
	 * @param file
	 * @return basename of filename. return null when the filename dose not
	 *         contain '.' charactor.
	 */
	public static String getBasename(File file) {
		String filename = file.getName();
		int index = filename.lastIndexOf('.');
		if (index == -1) {
			return filename;
		} else {
			return filename.substring(0, index);
		}
	}

	/**
	 * get basepath of filename ex. FileUtil.getBasepath("/foo/var/hoge.ext");
	 * // "/foo/var/hoge"
	 * 
	 * @param filename
	 * @return basepath of filename. return null when the filename dose not
	 *         contain '.' charactor.
	 */
	public static String getBasename(String filename) {
		return getBasename(new File(filename));
	}

	/**
	 * get suffix replaced file path() ex.
	 * FileUtil.getSuffixReplacedFilePath(new File("/foo/var/hoge.ext"), "EXT");
	 * // "/foo/var/hoge.EXT"
	 * 
	 * @param file
	 * @param suffix
	 * @throws IOException
	 *             when file is directory.
	 */
	public static String getSuffixReplacedFilePath(File file, String suffix) throws IOException {
		String basepath = getBasepath(file);
		if (basepath == null) {
			if (file.isFile()) {
				return file.getAbsolutePath() + '.' + suffix;
			} else {
				throw new IOException("File is directory: " + file.getAbsolutePath());
			}
		} else {
			return basepath + '.' + suffix;
		}
	}

	/**
	 * create wrapped writer
	 * 
	 * @param stream
	 * @param encoding
	 * @return new PrintWriter(new OutputStreamWriter(new
	 *         BufferedOutputStream(stream), encoding));
	 * @throws IOException
	 */
	public static PrintWriter createPrintWriter(OutputStream stream, String encoding) throws IOException {
		return new PrintWriter(new OutputStreamWriter(new BufferedOutputStream(stream), encoding));
	}

	/**
	 * create wrapped writer
	 * 
	 * @param file
	 * @param encoding
	 * @return new PrintWriter(new OutputStreamWriter(new
	 *         BufferedOutputStream(new FileOutputStream(file)), encoding));
	 * @throws IOException
	 */
	public static PrintWriter createPrintWriter(File file, String encoding) throws IOException {
		return createPrintWriter(new FileOutputStream(file), encoding);
	}

	/**
	 * create wrapped reader
	 * 
	 * @param input
	 * @param encoding
	 * @return new InputStreamReader(new BufferedInputStream(input), encoding)
	 * @throws IOException
	 */
	public static Reader createReader(InputStream input, String encoding) throws IOException {
		return new InputStreamReader(new BufferedInputStream(input), encoding);
	}

}
