/**
 * 
 */
package net.sqs2.omr.util;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class NameBasedComparableFile implements Comparable<NameBasedComparableFile> {
	private File file;

	private NameBasedComparableFile(File file) {
		this.file = file;
	}

	private File getFile() {
		return this.file;
	}

	public int compareTo(NameBasedComparableFile o) {
		try {
			String path = o.getFile().getCanonicalFile().getAbsolutePath();
			return this.file.getCanonicalFile().getAbsolutePath().compareTo(path);
		} catch (IOException ignore) {
			return 0;
		}
	}

	public static File[] createSortedFileArray(File[] items) {
		NameBasedComparableFile[] files = new NameBasedComparableFile[items.length];
		for (int i = 0; i < items.length; i++) {
			files[i] = new NameBasedComparableFile(items[i]);
		}
		Arrays.sort(files);
		File[] ret = new File[items.length];
		for (int i = 0; i < items.length; i++) {
			ret[i] = files[i].getFile();
		}
		return ret;
	}
}
