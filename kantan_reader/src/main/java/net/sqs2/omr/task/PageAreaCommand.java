/*
 * 

 PageAreaComman.java

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
package net.sqs2.omr.task;

import java.io.Serializable;

public class PageAreaCommand implements Serializable{
	private final static long serialVersionUID = 0L;

	protected String id = null;
	protected String imageType = null;
	protected byte[] imageByteArray = null;

	public PageAreaCommand() {
	}

	public PageAreaCommand(String id, String imageType, byte[] imageByteArray) {
		this.id = id;
		this.imageType = imageType;
		this.imageByteArray = imageByteArray;
	}

	public String getID() {
		return id;
	}

	public String getImageType() {
		return this.imageType;
	}

	public byte[] getImageByteArray() {
		return this.imageByteArray;
	}

	public String toString() {
		if(imageByteArray == null){
			return id;
		}else{
			return id+":"+this.imageType+"...ImageByteArray:"+imageByteArray.length+"bytes.";
		}
	}

}