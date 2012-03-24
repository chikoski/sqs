/*

 MetaDescriptionPanel.java
 
 Copyright 2004 KUBO Hiroya (hiroya@sfc.keio.ac.jp).
 
 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at
 
 http://www.apache.org/licenses/LICENSE-2.0
 
 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 
 Created on 2004/08/14

 */
package net.sqs2.editor.sqs.modules.panel;

import java.util.HashMap;
import java.util.Map;

import javax.swing.border.TitledBorder;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.EditorUtil;
import net.sqs2.editor.base.modules.panel.AbstractSingleNodeEditorPanel;
import net.sqs2.editor.base.swing.Messages;

/**
 * @author hiroya
 * 
 */
public class MetaDescriptionPanel extends AbstractSingleNodeEditorPanel {
	public static final long serialVersionUID = 0;
	static Map<String, String> map = new HashMap<String, String>();
	static {
		map.put("DC.Title", Messages.DC_TITLE_LABEL);
		map.put("DC.Title.Short", Messages.DC_TITLE_SHORT_LABEL);
		map.put("DC.Creator", Messages.DC_CREATOR_LABEL);
		map.put("DC.Subject", Messages.DC_SUBJECT_LABEL);
		map.put("DC.Coverage", Messages.DC_COVERAGE_LABEL);
		map.put("DCTERMS.Created", Messages.DCTERMS_CREATED_LABEL);
		map.put("DCTERMS.Valid", Messages.DCTERMS_VALID_LABEL);
		map.put("SQS.TemplateID", Messages.SQS_TEMPLATE_ID_LABEL);
	}

	public MetaDescriptionPanel(AbstractNodeEditor editor) {
		super(editor);
		setBorder(new TitledBorder(EditorUtil.LOWERED_BORDER, getDescriptionLabel()));
		initSize();
	}

	public String getDescriptionXPath() {
		return "@content";
	}

	public String getDescriptionLabel() {
		return getDescriptionLabel(getEditor().getName());
	}

	public static String getDescriptionLabel(String name) {
		return (String) map.get(name);
	}

	public int getPreferredHeight() {
		return 40;
	}
}
