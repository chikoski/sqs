package net.sqs2.util;

public class Path implements Comparable<Path> {

	String absoluteRootPath;
	String relativePath;

	public Path(String absoluteRootPath, String relativePath) {
		this.absoluteRootPath = absoluteRootPath;
		this.relativePath = relativePath;
	}

	public Path(String relativePath) {
		this.relativePath = relativePath;
	}

	public String getAbsoluteRootPath() {
		return this.absoluteRootPath;
	}

	public String getRelativePath() {
		return this.relativePath;
	}

	public int hashCode() {
		return this.absoluteRootPath.hashCode() + this.relativePath.hashCode();
	}

	public boolean equals(Object o) {
		try {
			Path p = (Path) o;
			return this.relativePath.equals(p.getRelativePath())
					&& ((this.absoluteRootPath == null && this.absoluteRootPath == null) || this.absoluteRootPath
							.equals(p.getAbsoluteRootPath()));
		} catch (Exception e) {
			return false;
		}
	}

	public int compareTo(Path o) {
		try {
			Path p = (Path) o;
			if (this.absoluteRootPath == null && p.absoluteRootPath != null) {
				return 1;
			}
			if (this.absoluteRootPath != null && p.absoluteRootPath == null) {
				return -1;
			}
			if (this.absoluteRootPath != null && this.absoluteRootPath != null) {
				int ret = this.absoluteRootPath.compareTo(p.getAbsoluteRootPath());
				if (ret != 0) {
					return ret;
				}
			}
			return this.relativePath.compareTo(p.getRelativePath());
		} catch (Exception e) {
			return 1;
		}
	}
}
