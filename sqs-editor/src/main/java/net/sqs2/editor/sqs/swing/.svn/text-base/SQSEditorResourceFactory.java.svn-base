/*

 SQSEditorResourceFactory.java
 
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
 
 Created on 2004/08/03

 */
package net.sqs2.editor.sqs.swing;

import java.awt.Color;
import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;

import net.sqs2.editor.base.modules.ContainerEditor;
import net.sqs2.editor.base.modules.ContentTextEditor;
import net.sqs2.editor.base.modules.EditorUtil;
import net.sqs2.editor.base.modules.EmptyEditor;
import net.sqs2.editor.base.modules.resource.EditorResource;
import net.sqs2.editor.base.swing.EditorResourceFactory;
import net.sqs2.editor.sqs.modules.GroupFormEditor;
import net.sqs2.editor.sqs.modules.MatrixFormsEditor;
import net.sqs2.editor.sqs.modules.MetaEditor;
import net.sqs2.editor.sqs.modules.SelectFormColsEditor;
import net.sqs2.editor.sqs.modules.SelectFormEditor;
import net.sqs2.editor.sqs.modules.TextAreaFormEditor;
import net.sqs2.swing.IconFactory;
import net.sqs2.util.Resource;
import net.sqs2.xmlns.SQSNamespaces;

/**
 * @author hiroya
 * 
 */

public class SQSEditorResourceFactory extends EditorResourceFactory {

	static String _(String key){
		return Resource._("messages", key);
	}

	public static final String NODE_TEXT_LABEL = _("node.text.label");// "文字データ";
	public static final String NODE_MATRIX_FORMS_LABEL = _("node.matrix.forms.label");// 設問グループ
	public static final String NODE_MATRIX_FORMS_ROW_ARRAY_LABEL = _("node.matrix.forms.row.array.label");// 設問グループの「行」の定義
	public static final String NODE_MATRIX_FORMS_COLUMN_ARRAY_LABEL = _("node.matrix.forms.column.array.label");// 設問グループの「列」の定義
	public static final String NODE_MATRIX_FORMS_ROW_LABEL = _("node.matrix.forms.row.label");// 設問グループの「行」
	public static final String NODE_MATRIX_FORMS_TEXTAREA_LABEL = _("node.matrix.forms.textarea.label");// 設問グループ内の自由記述欄
	public static final String NODE_MATRIX_FORMS_SELECT_LABEL = _("node.matrix.forms.select.label");// 設問グループ内の複数選択式設問
	public static final String NODE_MATRIX_FORMS_SELECT1_LABEL = _("node.matrix.forms.select1.label");// 設問グループ内の択一選択式設問
	public static final String NODE_MARK_LABEL = _("node.mark.label");// マーク表記のサンプル
	public static final String NODE_MARKING_EXAMPLE_LABEL = _("node.marking.example.label");// マーク記入例
	public static final String NODE_HTML_LABEL = _("node.html.label");// 調査票
	public static final String NODE_HEAD_LABEL = _("node.head.label");// 調査票の属性
	public static final String NODE_BODY_LABEL = _("node.body.label");// 調査票の本文
	public static final String NODE_TITLE_LABEL = _("node.title.label");// 表題
	public static final String NODE_TITLE_DESCRIPTION = _("node.title.description");// 表題
	public static final String NODE_ATTRIBUTE_LABEL = _("node.attribute.label");// 属性
	public static final String NODE_SECTION_LABEL = _("node.section.label");// 章
	public static final String NODE_H_LABEL = _("node.h.label");// 見出し
	public static final String NODE_P_LABEL = _("node.p.label");// 段落
	public static final String NODE_STRONG_LABEL = _("node.strong.label");// 文字データの強調
	public static final String NODE_BR_LABEL = _("node.br.label");// 改行
	public static final String NODE_WARNING_LABEL = _("node.warning.label");// 注意書
	public static final String NODE_WARNING_DESCRIPTION = _("node.warning.description");// 回答者に対する注意書
	public static final String NODE_TEXTAREA_LABEL = _("node.textarea.label");// 自由記述欄
	public static final String NODE_SELECT_LABEL = _("node.select.label");// 複数選択式設問
	public static final String NODE_SELECT1_LABEL = _("node.select1.label");// 択一選択式設問
	public static final String NODE_ITEM_LABEL = _("node.item.label");// 選択肢
	public static final String NODE_LABEL_LABEL = _("node.label.label");// ラベル
	public static final String NODE_VALUE_LABEL = _("node.value.label");// 選択肢の値
	public static final String NODE_HINT_LABEL = _("node.hint.label");// 設問文
	public static final String NODE_HELP_LABEL = _("node.help.label");// 設問の補足
	public static final String NODE_ALERT_LABEL = _("node.alert.label");// 設問の注意

	Map<String, EditorResource> editorMap;

	private static Icon createIcon(String name) {
		return IconFactory.create(name);
	}

	private static Icon createIcon(String name, String desc, int overlayTextSize, int overlayTextXOffset) {
		return IconFactory.create(name, desc, overlayTextSize, overlayTextXOffset);
	}

	public SQSEditorResourceFactory() {
		this.editorMap = new HashMap<String, EditorResource>();
		editorMap.put(getKey(null, null), new EditorResource(ContentTextEditor.class, createIcon("a.gif"),
				NODE_TEXT_LABEL, new Color(210, 210, 210), 36, true, true, EditorUtil.EMPTY_BORDER));

		forms();

		structures();

		marks();

		matrixForms();

	}

	private void matrixForms() {
		editorMap.put(getKey("matrix-forms", SQSNamespaces.SQS2004_URI), new EditorResource(
				MatrixFormsEditor.class, createIcon("matrix-forms.gif"), NODE_MATRIX_FORMS_LABEL, new Color(
						190, 255, 190), 400, true, true, EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("row-array", SQSNamespaces.SQS2004_URI), new EditorResource(
				ContainerEditor.class, createIcon("row-array.gif"), NODE_MATRIX_FORMS_ROW_ARRAY_LABEL,
				new Color(190, 255, 190), 180, true, false, EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("column-array", SQSNamespaces.SQS2004_URI), new EditorResource(
				ContainerEditor.class, createIcon("column-array.gif"), NODE_MATRIX_FORMS_COLUMN_ARRAY_LABEL,
				new Color(190, 255, 190), 180, true, false, EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("group", SQSNamespaces.XFORMS_URI), new EditorResource(GroupFormEditor.class,
				createIcon("box1.gif"), NODE_MATRIX_FORMS_ROW_LABEL, new Color(150, 225, 150), 30, true,
				true, EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("input", SQSNamespaces.SQS2004_URI), new EditorResource(
				TextAreaFormEditor.class, createIcon("image1m.gif"), NODE_MATRIX_FORMS_TEXTAREA_LABEL,
				new Color(250, 250, 200), 140, true, true, EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("textarea", SQSNamespaces.SQS2004_URI), new EditorResource(
				TextAreaFormEditor.class, createIcon("image1m.gif"), NODE_MATRIX_FORMS_TEXTAREA_LABEL,
				new Color(250, 250, 200), 140, true, true, EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("select", SQSNamespaces.SQS2004_URI), new EditorResource(SelectFormEditor.class,
				createIcon("sphere2m.gif"), NODE_MATRIX_FORMS_SELECT_LABEL, new Color(255, 220, 240), 280,
				true, true, EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("select1", SQSNamespaces.SQS2004_URI), new EditorResource(
				SelectFormEditor.class, createIcon("sphere1m.gif"), NODE_MATRIX_FORMS_SELECT1_LABEL,
				new Color(255, 230, 200), 280, true, true, EditorUtil.ETCHED_BORDER));
	}

	private void marks() {
		editorMap.put(getKey("mark", SQSNamespaces.SQS2004_URI), new EditorResource(EmptyEditor.class,
				createIcon("mark.gif"), NODE_MARK_LABEL, new Color(230, 230, 230), 30, true, false,
				EditorUtil.EMPTY_BORDER));
		editorMap.put(getKey("marking-example", SQSNamespaces.SQS2004_URI), new EditorResource(
				EmptyEditor.class, createIcon("marking-example.gif"), NODE_MARKING_EXAMPLE_LABEL, new Color(
						230, 230, 230), 30, true, false, EditorUtil.EMPTY_BORDER));
	}

	private void structures() {
		editorMap.put(getKey("html", SQSNamespaces.XHTML2_URI), new EditorResource(ContainerEditor.class,
				createIcon("sqs-html.gif"), NODE_HTML_LABEL, new Color(230, 230, 230), 100, true, false,
				EditorUtil.EMPTY_BORDER));
		editorMap.put(getKey("head", SQSNamespaces.XHTML2_URI), new EditorResource(ContainerEditor.class,
				createIcon("envelope.gif"), NODE_HEAD_LABEL, new Color(230, 230, 230), 100, true, false,
				EditorUtil.EMPTY_BORDER));
		editorMap.put(getKey("body", SQSNamespaces.XHTML2_URI), new EditorResource(ContainerEditor.class,
				createIcon("body.gif"), NODE_BODY_LABEL, new Color(230, 230, 230), 100, true, false,
				EditorUtil.EMPTY_BORDER));

		editorMap.put(getKey("title", SQSNamespaces.XHTML2_URI), new EditorResource(ContentTextEditor.class,
				createIcon("folder-cyan.gif", NODE_TITLE_LABEL, 12, 0), NODE_TITLE_DESCRIPTION, new Color(
						230, 230, 255), 35, true, true, EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("meta", SQSNamespaces.XHTML2_URI), new EditorResource(MetaEditor.class,
				createIcon("rdf-small.gif"), NODE_ATTRIBUTE_LABEL, new Color(230, 230, 255), 55, true, false,
				EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("section", SQSNamespaces.XHTML2_URI), new EditorResource(ContainerEditor.class,
				createIcon("folder-green.gif", NODE_SECTION_LABEL, 12, 0), NODE_SECTION_LABEL, new Color(230,
						230, 230), 120, true, true, EditorUtil.NORMAL_BORDER));
		editorMap.put(getKey("h", SQSNamespaces.XHTML2_URI), new EditorResource(ContentTextEditor.class,
				createIcon("folder-cyan.gif", NODE_H_LABEL, 10, +1), NODE_H_LABEL, new Color(230, 230, 230),
				120, true, true, EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("p", SQSNamespaces.XHTML2_URI), new EditorResource(ContentTextEditor.class,
				createIcon("square-blue.gif"), NODE_P_LABEL, new Color(230, 230, 230), 50, true, true,
				EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("strong", SQSNamespaces.XHTML2_URI), new EditorResource(ContentTextEditor.class,
				createIcon("burst.gif"), NODE_STRONG_LABEL, new Color(230, 230, 230), 50, true, true,
				EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("br", SQSNamespaces.XHTML2_URI), new EditorResource(EmptyEditor.class,
				createIcon("back.gif"), NODE_BR_LABEL, new Color(230, 230, 230), 30, true, false,
				EditorUtil.EMPTY_BORDER));

		editorMap.put(getKey("warning", SQSNamespaces.SQS2004_URI), new EditorResource(ContainerEditor.class,
				createIcon("folder-blue.gif", NODE_WARNING_LABEL, 10, +1), NODE_WARNING_DESCRIPTION,
				new Color(230, 230, 230), 30, true, true, EditorUtil.ETCHED_BORDER));
	}

	private void forms() {
		editorMap.put(getKey("input", SQSNamespaces.XFORMS_URI), new EditorResource(TextAreaFormEditor.class,
				createIcon("image1.gif"), NODE_TEXTAREA_LABEL, new Color(250, 250, 200), 140, true, true,
				EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("textarea", SQSNamespaces.XFORMS_URI), new EditorResource(
				TextAreaFormEditor.class, createIcon("image1.gif"), NODE_TEXTAREA_LABEL, new Color(250, 250,
						200), 140, true, true, EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("select", SQSNamespaces.XFORMS_URI), new EditorResource(
				SelectFormColsEditor.class, createIcon("sphere2.gif"), NODE_SELECT_LABEL, new Color(255, 220,
						240), 280, true, true, EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("select1", SQSNamespaces.XFORMS_URI), new EditorResource(
				SelectFormColsEditor.class, createIcon("sphere1.gif"), NODE_SELECT1_LABEL, new Color(255,
						230, 200), 280, true, true, EditorUtil.ETCHED_BORDER));

		editorMap.put(getKey("item", SQSNamespaces.XFORMS_URI), new EditorResource(ContainerEditor.class,
				createIcon("ball.red.gif"), NODE_ITEM_LABEL, new Color(230, 230, 230), 30, true, true,
				EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("label", SQSNamespaces.XFORMS_URI), new EditorResource(ContentTextEditor.class,
				createIcon("right.gif"), NODE_LABEL_LABEL, new Color(230, 230, 230), 30, true, true,
				EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("value", SQSNamespaces.XFORMS_URI), new EditorResource(ContentTextEditor.class,
				createIcon("down.gif"), NODE_VALUE_LABEL, new Color(230, 230, 230), 30, true, true,
				EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("hint", SQSNamespaces.XFORMS_URI), new EditorResource(ContentTextEditor.class,
				createIcon("hand.up.gif"), NODE_HINT_LABEL, new Color(230, 230, 230), 30, true, true,
				EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("help", SQSNamespaces.XFORMS_URI), new EditorResource(ContentTextEditor.class,
				createIcon("hand.right.gif"), NODE_HELP_LABEL, new Color(230, 230, 230), 30, true, true,
				EditorUtil.ETCHED_BORDER));
		editorMap.put(getKey("alart", SQSNamespaces.XFORMS_URI), new EditorResource(ContentTextEditor.class,
				createIcon("hand.down.gif"), NODE_ALERT_LABEL, new Color(230, 230, 230), 30, true, true,
				EditorUtil.ETCHED_BORDER));
	}

	public EditorResource getEditorResource(String key) {
		return (EditorResource) editorMap.get(key);
	}

}
