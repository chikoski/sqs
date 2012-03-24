/**
 *  GenericSourceConfig.java

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

 Created on 2007/04/07
 Author hiroya
 */
package net.sqs2.omr.model;

import java.util.ArrayList;
import java.util.List;

public class GenericSourceConfig {

	class Argument {
		public static final int FILE = 1;
		public static final int PAGE = 2;
		int type;
		String value;

		Argument(int type, String value) {
			this.type = type;
			this.value = value;
		}

		int getType() {
			return this.type;
		}

		String getValue() {
			return this.value;
		}

		@Override
		public String toString() {
			return getValue();
		}
	}

	class RangeArgument extends Argument {
		String start;
		String end;

		RangeArgument(int type, String start, String end) {
			super(type, null);
			this.start = start;
			this.end = end;
		}

		@Override
		String getValue() {
			return this.start + "-" + this.end;
		}

		String getStart() {
			return this.start;
		}

		String getEnd() {
			return this.end;
		}
	}

	private String filename;
	private String page;

	private List<Argument> filenameArgumentList = null;
	private List<Argument> pageArgumentList = null;

	public GenericSourceConfig() {
	}

	public String getFilename() {
		return this.filename;
	}

	public String getPage() {
		return this.page;
	}

	public List<Argument> getFilenameArgument() {
		return this.filenameArgumentList;
	}

	public List<Argument> getPageArgument() {
		return this.pageArgumentList;
	}

	public void setFilename(String filename) {
		this.filename = filename;
		this.filenameArgumentList = new ArrayList<Argument>();
		createArgumentList(this.filenameArgumentList, filename);
	}

	/**
	 * 
	 * @param pageNumber starts with 1
	 */
	public void setPage(String pageNumber) {
		this.page = pageNumber;
		this.pageArgumentList = new ArrayList<Argument>();
		createArgumentList(this.pageArgumentList, pageNumber);
	}

	private void createArgumentList(List<Argument> argumentList, String src) {
		createArgumentList(argumentList, src, ",");
	}

	private void createArgumentList(List<Argument> argumentList, String src, String delim) {
		String prevArg = null;
		String delim2 = (",".equals(delim)) ? "-" : null;
		for (String arg : src.split(delim)) {
			if (prevArg != null && prevArg.endsWith("\\")) {
				arg = prevArg.substring(0, prevArg.length() - 1) + delim + arg;
			}
			if (!arg.endsWith("\\")) {
				argumentList.add(createArgument(arg, delim2));
			}
			prevArg = arg;
		}
	}

	private Argument createArgument(String src, String delim) {
		if ("-".equals(delim) && 0 < src.indexOf('-')) {
			List<Argument> argumentList = new ArrayList<Argument>();
			createArgumentList(argumentList, src, "-");
			int size = argumentList.size();
			if (size == 1) {
				return new Argument(Argument.FILE, src);
			} else if (size == 2) {
				return new RangeArgument(Argument.FILE, argumentList.get(0).getValue(), argumentList.get(1)
						.getValue());
			}
			throw new IllegalArgumentException(src);
		} else {
			return new Argument(Argument.FILE, src);
		}
	}

	/**
	 * 
	 * @param pageNumber starts with 1
	 */
	public boolean match(String filename, int pageNumber) {
		if (this.filenameArgumentList != null) {
			for (Argument arg : this.filenameArgumentList) {
				if (match(arg, filename)) {
					return true;
				}
			}
			return false;
		} else if (this.pageArgumentList != null) {
			for (Argument arg : this.pageArgumentList) {
				if (match(arg, pageNumber)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	public boolean match(String filename) {
		if (this.filenameArgumentList != null) {
			for (Argument arg : this.filenameArgumentList) {
				if (match(arg, filename)) {
					return true;
				}
			}
			return false;
		}
		return true;
	}

	/**
	 * 
	 * @param pageNumber starts with 1
	 */
	private boolean match(Argument arg, int pageNumber) {
		if (arg instanceof RangeArgument) {
			RangeArgument rarg = (RangeArgument) arg;
			if (rarg.getStart() != null) {
				int start = Integer.parseInt(rarg.getStart());
				if (rarg.getEnd() != null) {
					int end = Integer.parseInt(rarg.getEnd());
					return (start <= pageNumber) && (pageNumber <= end);
				} else {
					return (start <= pageNumber);
				}
			} else if (rarg.getEnd() != null) {
				int end = Integer.parseInt(rarg.getEnd());
				return (pageNumber <= end);
			}
			return false;
		} else {
			return (Integer.parseInt(arg.getValue()) == pageNumber);
		}
	}

	private boolean match(Argument arg, String filename) {
		if (arg instanceof RangeArgument) {
			RangeArgument rarg = (RangeArgument) arg;
			if (rarg.getStart() != null) {
				if (rarg.getEnd() != null) {
					return 0 <= rarg.getStart().compareTo(filename) && 0 <= filename.compareTo(rarg.getEnd());
				} else {
					return 0 <= rarg.getStart().compareTo(filename);
				}
			} else if (rarg.getEnd() != null) {
				return 0 <= filename.compareTo(rarg.getEnd());
			}
			return false;
		} else {
			return filename.equals(arg.getValue());
		}
	}
}
