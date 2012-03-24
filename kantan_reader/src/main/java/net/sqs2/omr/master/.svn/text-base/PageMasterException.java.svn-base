/**

 PageMasterException.java

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

 Created on Jan 7, 2007

 */

package net.sqs2.omr.master;
import java.io.File;
import java.io.Serializable;

public class PageMasterException extends Exception implements Serializable{
	private static final long serialVersionUID = 0;

	Exception ex = null;
	File file = null;
	String path = null;
	
	public PageMasterException() {
	}
	
	public PageMasterException(String path) {
		this.path = path;
	}
	
	public PageMasterException(Exception ex, String path) {
		this.ex = ex;
		this.path = path;
	}
	
	public Exception getException() {
		return this.ex;
	}
	
	public String getPath(){
		return path;
	}

}