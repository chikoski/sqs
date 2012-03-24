/*

 PageFrameInvalidException.java

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

 Created on 2006/01/02

 */
package net.sqs2.omr.logic;

import java.awt.Point;
import java.io.Serializable;

import net.sqs2.omr.swing.Messages;


public class PageFrameDistortionExceptionCore extends PageFrameExceptionCore implements Serializable {
	public static final long serialVersionUID = 1L;

	public static final int UNDEFINED = 0;

	public static final int HORIZONTAL = 1;

	public static final int VERTICAL = 2;
	
	int type;
	float distortion;
	
	public PageFrameDistortionExceptionCore(String pageID, int width, int height, Point[] masterCorners, Point[] corners, int type, float distortion) {
		super(pageID, width, height, masterCorners, corners);
		this.type = type;
		this.distortion = distortion;
	}
	
	public String getLocalizedMessage(){
		return Messages.SESSION_ERROR_PAGEFRAMEDISTORTION;
	}

	private String toString(Point corner) {
		if (corner != null) {
			return "{x:" + (int) corner.getX() + ",y:" + (int) corner.getY() + "}";
		} else {
			return "null";
		}
	}

	public String getDescription() {
		if(type == HORIZONTAL){
			return "horizontal distortion:"+ distortion;
		}else if(type == VERTICAL){
			return "vertical distortion:"+ distortion;		
		}else{
			throw new RuntimeException("invalid type of distortion");
		}
		
		/*
		return "{header:{left:" + toString(this.corners[0])+
			",right:"+ toString(this.corners[1]) + "} ,footer:{left:"
			+ toString(this.corners[2]) + ",right:"+toString(this.corners[3]) + "}}";
			*/
	}
}
