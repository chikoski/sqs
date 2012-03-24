/*
 * 

 PageMaster.java

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
package net.sqs2.omr.master;

import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

import net.sqs2.util.FileResourceID;

public class PageMaster implements Serializable {
	private final static long serialVersionUID = 5L;

	protected PageMasterMetadata pageMasterMetadata;
	protected FileResourceID fileResourceID = null;
	protected int numPages;
	protected Point2D[] deskewGuideCenterPoints = null;

	protected Rectangle2D footerRightRectangle;
	protected Rectangle2D footerLeftRectangle;
	protected Rectangle2D headerCheckArea;
	protected Rectangle2D footerCheckArea;
	protected String version;

	public PageMaster() {
		super();
	}

	public PageMaster(FileResourceID fileResourceID, PageMasterMetadata pageMasterMetadata) {
		this.fileResourceID = fileResourceID;
		this.pageMasterMetadata = pageMasterMetadata;
	}

	public PageMasterMetadata getPageMasterMetadata() {
		return this.pageMasterMetadata;
	}

	public FileResourceID getFileResourceID() {
		return this.fileResourceID;
	}

	public Point2D[] getDeskewGuideCenterPoints() {
		return this.deskewGuideCenterPoints;
	}

	public void setDeskewGuideCenterPoints(Point2D[] deskewGuideCenterPoints) {
		this.deskewGuideCenterPoints = deskewGuideCenterPoints;
	}

	public int getNumPages() {
		return this.numPages;
	}

	public void setNumPages(int numPages) {
		this.numPages = numPages;
	}

	public String getRelativePath() {
		return this.fileResourceID.getRelativePath();
	}

	public long getLastModified() {
		return this.fileResourceID.getLastModified();
	}

	public String getVersion() {
		return this.version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public Rectangle2D getFooterRightRectangle() {
		return this.footerRightRectangle;
	}

	public Rectangle2D getFooterLeftRectangle() {
		return this.footerLeftRectangle;
	}

	public void setFooterRightRectangle(Rectangle2D rectangle) {
		this.footerRightRectangle = rectangle;
	}

	public void setFooterLeftRectangle(Rectangle2D rectangle) {
		this.footerLeftRectangle = rectangle;
	}

	public Rectangle2D getHeaderCheckArea() {
		return this.headerCheckArea;
	}

	public void setHeaderCheckArea(Rectangle2D headerCheckArea) {
		this.headerCheckArea = headerCheckArea;
	}

	public Rectangle2D getFooterCheckArea() {
		return this.footerCheckArea;
	}

	public void setFooterCheckArea(Rectangle2D footerCheckArea) {
		this.footerCheckArea = footerCheckArea;
	}

	@Override
	public String toString() {
		return getRelativePath();
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		return this.fileResourceID.equals(((PageMaster) o).getFileResourceID());
	}

	@Override
	public int hashCode() {
		return this.fileResourceID.hashCode();
	}
	
}
