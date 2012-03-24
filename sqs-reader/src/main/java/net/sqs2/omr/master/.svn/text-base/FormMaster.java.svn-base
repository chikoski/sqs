/*

 FormMaster.java
 
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
 
 Created on 2005/02/26

 */
package net.sqs2.omr.master;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.sqs2.util.FileResourceID;

import org.apache.commons.collections15.map.LinkedMap;

public class FormMaster extends PageMaster implements Serializable {
	private final static long serialVersionUID = 10L;

	private ArrayList<FormArea> formAreaList = new ArrayList<FormArea>();
	private ArrayList<ArrayList<FormArea>> formAreaListByPageIndex = new ArrayList<ArrayList<FormArea>>();
	private Map<String, Integer> formAreaIndexInPageByQID = new HashMap<String, Integer>();
	private ArrayList<String> qidList = new ArrayList<String>();
	private Map<String, ArrayList<String>> qidListByTypeMap = new HashMap<String, ArrayList<String>>();
	private LinkedMap<String, ArrayList<FormArea>> formAreaListByQID = new LinkedMap<String, ArrayList<FormArea>>();
	private Map<String, FormArea> formAreaByID = new LinkedHashMap<String, FormArea>();

	private int horizontalOffset = 0;
	private int verticalOffset = 0;

	public FormMaster() {
		super();
	}

	public FormMaster(FileResourceID fileResourceID, PageMasterMetadata metadata) {
		super(fileResourceID, metadata);
	}

	public ArrayList<FormArea> getFormAreaList() {
		return this.formAreaList;
	}

	public ArrayList<FormArea> getFormAreaListByPageIndex(int pageIndex) {
		return this.formAreaListByPageIndex.get(pageIndex);
	}

	public void addFormAreaList(ArrayList<FormArea> formAreaList) {
		this.formAreaListByPageIndex.add(formAreaList);
	}

	public ArrayList<FormArea> getFormAreaList(String qid) {
		return this.formAreaListByQID.get(qid);
	}

	public ArrayList<FormArea> getFormAreaList(int questionIndex) {
		return this.formAreaListByQID.get(this.qidList.get(questionIndex));
	}

	public Set<String> getQIDSet() {
		return this.formAreaListByQID.keySet();
	}

	public void putFormAreaList(String qid, ArrayList<FormArea> formAreaList) {
		this.formAreaListByQID.put(qid, formAreaList);
	}

	public void addQID(String qid, String type) {
		this.qidList.add(qid);
		ArrayList<String> qidListByType = this.qidListByTypeMap.get(type);
		if (qidListByType == null) {
			qidListByType = new ArrayList<String>();
			this.qidListByTypeMap.put(type, qidListByType);
		}
		qidListByType.add(qid);
	}

	public String getQID(String type, int index) {
		List<String> qidList = this.qidListByTypeMap.get(type);
		if (qidList == null) {
			return null;
		}
		try {
			return qidList.get(index);
		} catch (IndexOutOfBoundsException ignore) {
			return null;
		}
	}

	public int getNumQuestions() {
		return this.qidList.size();
	}

	public Set<Map.Entry<String, ArrayList<FormArea>>> getFormAreaListEntrySet() {
		return this.formAreaListByQID.entrySet();
	}

	public FormArea getFormAreaMapByID(String id) {
		return this.formAreaByID.get(id);
	}

	public void setAreaIndexInPage(String qid, int areaIndexInPage) {
		this.formAreaIndexInPageByQID.put(qid, areaIndexInPage);
	}

	public int getAreaIndexInPage(String qid) {
		return this.formAreaIndexInPageByQID.get(qid);
	}

	public void putFormArea(String id, FormArea area) {
		this.formAreaByID.put(id, area);
	}

	public int getHorizontalOffset() {
		return this.horizontalOffset;
	}

	public void setHorizontalOffset(int horizontalOffset) {
		this.horizontalOffset = horizontalOffset;
	}

	public int getVerticalOffset() {
		return this.verticalOffset;
	}

	public void setVerticalOffset(int verticalOffset) {
		this.verticalOffset = verticalOffset;
	}
	
	public String getKey(){
		return createKey(getRelativePath(), getLastModified());
	}
	
	public static String createKey(String relativePath, long lastModified){
		return relativePath+"\t"+lastModified;
	}
	
	public static String createKey(FileResourceID fileResourceID){
		return createKey(fileResourceID.getRelativePath(), fileResourceID.getLastModified());
	}

}
