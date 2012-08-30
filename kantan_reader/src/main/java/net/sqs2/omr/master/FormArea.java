/*

  FormArea.java

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

  Created on 2005/03/13

*/
package net.sqs2.omr.master;

import java.awt.Rectangle;
import java.io.Serializable;

public class FormArea implements Serializable{

    public static final long serialVersionUID = 4;

    public static final int SELECT1 = 0;

    public static final int SELECT = 1;

    public static final int TEXTAREA = 2;

    public static final int INPUT = 3;

    int page = -1;
    String type = null;
    int typeCode = -1;
    String qid = null;
    String label = null;
    String hint = null;
    String[] hints = null;

    int columnIndex = -1;
    int areaIndexInPage = -1; 
    String id = null;
    Rectangle rect = null;


    int itemIndex = -1;
	
    String clazz = null;

    String itemLabel = null;

    String itemValue = null;
	
    String itemClazz = null;


    public FormArea() {
    }

    public String getId() {
	return id;
    }

    public void setID(String id) {
	this.id = id;
    }
	
    public void setQID(String qid) {
	this.qid = qid;
    }

    public void setPage(int page) {
	this.page = page;
    }

    public void setType(String type) {
	this.type = type;
    }

    public void setTypeCode(int typeCode) {
	this.typeCode = typeCode;
    }

    public void setRect(Rectangle rect) {
	this.rect = rect;
    }

    public void setLabel(String label) {
	this.label = label;
    }

    public void setHint(String hint) {
	this.hint = hint;
    }

    public void setHints(String[] hints) {
	this.hints = hints;
    }

    public int getPage() {
	return this.page;
    }

    public int getPageIndex() {
	return this.page - 1;
    }

    public String getType() {
	return this.type;
    }

    public int getColumnIndex(){
	return this.columnIndex;
    }

    public int getAreaIndexInPage() {
	return this.areaIndexInPage;
    }

    public int getTypeCode() {
	return this.typeCode;
    }

    public boolean isSelect1(){
	return this.typeCode == FormArea.SELECT1;
    }

    public boolean isSelect(){
	return this.typeCode == FormArea.SELECT;
    }

    public boolean isTextArea(){
	return this.typeCode == FormArea.TEXTAREA || this.typeCode == FormArea.INPUT;
    }

    public boolean isMarkArea(){
	return this.typeCode == FormArea.SELECT1 || this.typeCode == FormArea.SELECT;
    }

    public String getQID() {
	return this.qid;
    }

    public String getClazz() {
	return this.clazz;
    }

    public String getID() {
	return this.id;
    }

    public Rectangle getRect() {
	return this.rect;
    }

    public String getLabel() {
	return this.label;
    }

    public String getHint() {
	return this.hint;
    }

    public String[] getHints() {
	return this.hints;
    }

    public int getItemIndex(){
	return this.itemIndex;
    }

    public String getItemLabel() {
	return this.itemLabel;
    }

    public void setItemIndex(int itemIndex) {
	this.itemIndex = itemIndex;
    }

    public void setItemLabel(String itemLabel) {
	this.itemLabel = itemLabel;
    }

    public void setItemValue(String itemValue) {
	this.itemValue = itemValue;
    }

    public String getItemValue() {
	return this.itemValue;
    }

    public String getItemClazz() {
	return this.itemClazz;
    }

    public String toString() {
	String hint = getHint();
	if (8 < hint.length()) {
	    hint = hint.substring(0, 8);
	}
	return getID() + ":" + hint;
    }
	
    public void setIndex(int columnIndex, int itemIndex, int areaIndexInPage){
	this.columnIndex = columnIndex;
	this.itemIndex = itemIndex; 
	this.areaIndexInPage = areaIndexInPage;
    }

}
