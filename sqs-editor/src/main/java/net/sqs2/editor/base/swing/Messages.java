package net.sqs2.editor.base.swing;

import net.sqs2.util.Resource;

public class Messages {
	public static String _(String key){
		return Resource._("messages", key);
	}
	public static final String EXIT_CONFIRMATION_MESSAGE = _("Messages.exit.confirmation.message"); //$NON-NLS-1$
	public static final String MENU_FILE = _("Messages.menu.file"); //$NON-NLS-1$
	public static final String MENU_FILE_NEW = _("Messages.menu.file.new"); //$NON-NLS-1$
	public static final String ERROR_FILE_SAVE = _("Messages.error.file.save"); //$NON-NLS-1$
	public static final String MENU_FILE_OPEN = _("Messages.menu.file.open"); //$NON-NLS-1$
	public static final String MENU_FILE_CLOSE = _("Messages.menu.file.close"); //$NON-NLS-1$
	public static final String MENU_FILE_SAVE = _("Messages.menu.file.save"); //$NON-NLS-1$
	public static final String MENU_FILE_SAVE_AS = _("Messages.menu.file.saveAs"); //$NON-NLS-1$
	public static final String MENU_FILE_EXPORT = _("Messages.menu.file.export"); //$NON-NLS-1$
	public static final String MENU_FILE_PAGE_SETTING = _("Messages.menu.file.pageSetting"); //$NON-NLS-1$
	
	public static final String MENU_FILE_QUIT = _("Messages.menu.file.quit"); //$NON-NLS-1$
	
	public static final String MENU_EDIT = _("Messages.menu.edit"); //$NON-NLS-1$
	
	public static final String PROMPT_DND_TO_FILE_OPEN = _("Messages.prompt.dndToFileOpen"); //$NON-NLS-1$

	public static final String VOCABRUARY_SELECTED_NODE = _("Messages.word.node.selected"); //$NON-NLS-1$
	public static final String VOCABRUARY_CLICKED_NODE = _("Messages.word.node.clicked"); //$NON-NLS-1$
	public static final String SUFFIX_CUT_LABEL = _("Messages.node.cut.label.suffix"); //$NON-NLS-1$
	public static final String SUFFIX_COPY_LABEL = _("Messages.node.copy.label.suffix"); //$NON-NLS-1$
	public static final String SUFFIX_EXPAND_NODE_LABEL = _("Messages.node.expand.label.suffix"); //$NON-NLS-1$
	public static final String SUFFIX_HIDE_NODE_LABEL = _("Messages.node.hide.label.suffix"); //$NON-NLS-1$

	public static final String SUFFIX_PASTE_AS_PRECEDING_SIBLING_LABEL = _("Messages.node.pasteAsPrecedingSibling.label.suffix"); //$NON-NLS-1$
	public static final String SUFFIX_PASTE_AS_FOLLOWING_SIBLING_LABEL = _("Messages.node.pasteAsFollowingSibling.label.suffix"); //$NON-NLS-1$
	public static final String SUFFIX_PASTE_AS_CHILD_LABEL = _("Messages.node.pasteAsChild.label.suffix"); //$NON-NLS-1$
	public static final String SUFFIX_DELETE_NODES_CONFIRMATION_MESSAGE = _("Messages.node.cut.confirmation.message"); //$NON-NLS-1$
	public static final String UNSAVED_BUFFER_CONFIRMATION_MESSAGE = _("Messages.buffer.close.unsavedBuffer.confirmation.message"); //$NON-NLS-1$
	public static final String SUFFIX_OVERWRITE_CONFIRMATION_MESSAGE = _("Messages.buffer.override.existingFile.confirmation.message"); //$NON-NLS-1$
	public static final String OVERRIDE_FILE_CONFIRMATION_MESSAGE = _("Messages.buffer.override.updatedFile.confirmation.message"); //$NON-NLS-1$
	public static final String OVERRIDE_BUFFER_MESSAGE = _("Messages.buffer.override.message"); //$NON-NLS-1$
	public static final String REOPEN_FILE_MESSAGE = _("Messages.file.reopen.message"); //$NON-NLS-1$
	public static final String PREFIX_UNSAVED_EXIT_CONFIRMATION_MESSAGE = _("Messages.exit.confirmation.unsavedBuffer.message.prefix"); //$NON-NLS-1$
	public static final String SUFFIX_UNSAVED_EXIT_CONFIRMATION_MESSAGE = _("Messages.exit.confirmation.unsavedBuffer.message.suffix"); //$NON-NLS-1$
	public static final String FILE_PARSE_ERROR_MESSAGE = _("Messages.file.parseError.message"); //$NON-NLS-1$

	public static final String COLS_LABEL = _("Messages.col.label"); //$NON-NLS-1$
	public static final String ROWS_LABEL = _("Messages.row.label"); //$NON-NLS-1$
	public static final String COLS_DEFINITION_LABEL = _("Messages.col.definition.label"); //$NON-NLS-1$
	public static final String ROWS_DEFINITION_LABEL = _("Messages.row.definition.label"); //$NON-NLS-1$
	public static final String FORM_HINT_LABEL = _("Messages.form.hint.label"); //$NON-NLS-1$
	public static final String FORM_LAYOUT_LABEL = _("Messages.form.layout.label"); //$NON-NLS-1$
	public static final String FORM_LAYOUT_RATIO_LABEL = _("Messages.form.layout.ratio.label"); //$NON-NLS-1$

	public static final String TOOLBAR_HTML_EXPORT_LABEL = _("Messages.toolbar.html.export.label"); //$NON-NLS-1$
	public static final String TOOLBAR_HTML_EXPORT_TOOLTIP = _("Messages.toolbar.html.export.tooltip"); //$NON-NLS-1$
	public static final String TOOLBAR_PDF_PREVIEW_LABEL = _("Messages.toolbar.pdf.preview.label"); //$NON-NLS-1$
	public static final String TOOLBAR_PDF_PREVIEW_TOOLTIP = _("Messages.toolbar.pdf.preview.tooltip"); //$NON-NLS-1$
	public static final String TOOLBAR_PDF_EXPORT_LABEL = _("Messages.toolbar.pdf.export.label"); //$NON-NLS-1$
	public static final String TOOLBAR_PDF_EXPORT_TOOLTIP = _("Messages.toolbar.pdf.export.tooltip"); //$NON-NLS-1$

	public static final String TOOLBAR_CLOSE_BUFFER_TOOLTIP = _("Messages.toolbar.close.buffer.tooltip"); //$NON-NLS-1$
	public static final String ERROR_FILE_EXPORT_MESSAGE = _("Messages.error.file.export.message"); //$NON-NLS-1$
	public static final String ERROR_EXPORT_FILE_SUFFIX = _("Messages.error.export.file.suffix"); //$NON-NLS-1$
	public static final String VOCABRUARY_CURRENT_FILENAME = _("Messages.word.currentFilename"); //$NON-NLS-1$
	public static final String ERROR_FILE_EXPORT = _("Messages.error.file.export"); //$NON-NLS-1$
	public static final String ERROR_FILE_TRANSFORM = _("Messages.error.file.transform"); //$NON-NLS-1$
	public static final String ERROR_FILE_TRANSFORM_MESSAGE = _("Messages.error.file.transform.message"); //$NON-NLS-1$
	public static final String VOCABRUARY_LOCATION = _("Messages.word.location"); //$NON-NLS-1$
	public static final String VOCABRUARY_CONTENT = _("Messages.word.content"); //$NON-NLS-1$
	// public static final String TOOLBAR_

	public static final String FILECHOOSER_OPEN_SQSSOURCE_TITLE = _("Messages.fileChooser.open.sqsSource.title"); //$NON-NLS-1$
	public static final String FILECHOOSER_SAVE_SQSSOURCE_TITLE = _("Messages.fileChooser.save.sqsSource.titile"); //$NON-NLS-1$
	public static final String FILECHOOSER_SAVE_SQSMASTER_TITLE = _("Messages.fileChooser.save.sqsMaster.title"); //$NON-NLS-1$
	public static final String FILECHOOSER_SAVE_PDFMARKSHEET_TITLE = _("Messages.fileChooser.save.PDFMarkSheet.title"); //$NON-NLS-1$
	public static final String FILECHOOSER_OPEN_SQSFOLDER_TITLE = _("Messages.fileChooser.open.sqsFolder.title"); //$NON-NLS-1$
	public static final String FILECHOOSER_SAVE_RESULTFOLDER_TITLE = _("Messages.fileChooser.save.resultFolder.title"); //$NON-NLS-1$

	public static final String FILECHOOSER_SQSSOURCE_DESCRIPTION = _("Messages.fileChooser.SQSSorce.description"); //$NON-NLS-1$
	public static final String FILECHOOSER_SQSPDF_DESCRIPTION = _("Messages.fileChooser.SQSPDF.description"); //$NON-NLS-1$

	public static final String FILECHOOSER_SQSFOLDER_DESCRIPTION = _("Messages.fileChooser.SQSFolder.description"); //$NON-NLS-1$
	public static final String FILECHOOSER_SQSMASTER_DESCRIPTION = _("Messages.fileChooser.sqsMaster.description"); //$NON-NLS-1$
	public static final String FILECHOOSER_HTMLFORM_DESCRIPTION = _("Messages.fileChooser.htmlForm.description"); //$NON-NLS-1$
	public static final String FILECHOOSER_PDFMARKSHEET_DESCRIPTION = _("Messages.fileChooser.PDFMarkSheet.description"); //$NON-NLS-1$
	public static final String FILECHOOSER_RESULTFOLDER_DESCRIPTION = _("Messages.fileChooser.ResultFolder.description"); //$NON-NLS-1$
	public static final String TEMPLATE_CHECK_LABEL = _("Messages.template.check.label"); //$NON-NLS-1$
	public static final String TEMPLATE_DISCOVER_LABEL = _("Messages.template.discover.label"); //$NON-NLS-1$
	public static final String TEMPLATE_IMPROVE_LABEL = _("Messages.template.improve.label"); //$NON-NLS-1$
	public static final String TEMPLATE_SIMPLE_LABEL = _("Messages.template.simple.label"); //$NON-NLS-1$
	public static final String TEMPLATE_CHECK_ACTUALIZATION = _("Messages.template.checkActualization.label"); //$NON-NLS-1$
	public static final String TEMPLATE_CHECK_AGREEMENT = _("Messages.template.checkAgreement.label"); //$NON-NLS-1$
	public static final String TEMPLATE_CHECK_IMPORTANCE = _("Messages.template.checkImportance.label"); //$NON-NLS-1$
	public static final String TEMPLATE_CHECK_SATISFACTION = _("Messages.template.checkSatisfaction.label"); //$NON-NLS-1$
	public static final String TEMPLATE_DISCOVER_IMPORTANCE_SATISFACTION = _("Messages.template.discoverImportanceSatisfaction.label"); //$NON-NLS-1$
	public static final String TEMPLATE_DISCOVER_IMPORTANCE_ACTUALIZATION = _("Messages.template.discoverImortanceActualization..label"); //$NON-NLS-1$
	public static final String TEMPLATE_MEZASOU = _("Messages.template.mezasou"); //$NON-NLS-1$

	public static final String DC_TITLE_LABEL = _("Messages.dc.title.label"); //$NON-NLS-1$
	public static final String DC_TITLE_SHORT_LABEL = _("Messages.dc.title.short.label"); //$NON-NLS-1$
	public static final String DC_CREATOR_LABEL = _("Messages.dc.creator.label"); //$NON-NLS-1$
	public static final String DC_SUBJECT_LABEL = _("Messages.dc.subject.label"); //$NON-NLS-1$
	public static final String DC_COVERAGE_LABEL = _("Messages.dc.coverage.label"); //$NON-NLS-1$
	public static final String DCTERMS_CREATED_LABEL = _("Messages.dcterms.created.label"); //$NON-NLS-1$
	public static final String DCTERMS_VALID_LABEL = _("Messages.dctarms.valid.label"); //$NON-NLS-1$
	public static final String SQS_TEMPLATE_ID_LABEL = _("Messages.sqs.templateId.label"); //$NON-NLS-1$
	public static final String MARK_ITEM_DISPLAY_WIDTH_LABEL = _("Messages.form.item.display.width.label"); //$NON-NLS-1$
	public static final String FORM_COLS_LAYOUT_LABEL = _("Messages.form.cols.layout.label"); //$NON-NLS-1$
	public static final String FORM_COLS_LAYOUT_NUM_LABEL = _("Messages.form.cols.layout.num.label"); //$NON-NLS-1$
	public static final String FORM_QUESTION_ITEMS = _("Messages.form.question.items"); //$NON-NLS-1$
	public static final String ERROR_FORM_ITEM_VALUE_OVERLOAD = _("Messages.error.form.item.value.overload"); //$NON-NLS-1$

	public static final String FORM_QUESTION_PASTE_ITEM_AS_PRECEDING_SIBLING = _("Messages.form.question.paste.item.as.preceding.sibling"); //$NON-NLS-1$
	public static final String FORM_QUESTION_PASTE_ITEM_AS_FOLLOWING_SIBLING = _("Messages.form.question.paste.item.as.following.sibling"); //$NON-NLS-1$
	public static final String FORM_QUESTION_DELETE_ITEM = _("Messages.form.question.delete.item"); //$NON-NLS-1$

	public static final String FORM_TABLE_SIZE_HEIGHT = _("Messages.form.table.size.height"); //$NON-NLS-1$
	public static final String FORM_TABLE_SIZE_WIDTH = _("Messages.form.table.size.width"); //$NON-NLS-1$
	public static final String FORM_TABLE_SIZE_LABEL = _("Messages.form.table.size"); //$NON-NLS-1$
	public static final String UNDO_LABEL = _("EditMenuFeature.UNDO_LABEL");
	public static final String REDO_LABEL = _("EditMenuFeature.REDO_LABEL");
	public static final String CUT_LABEL = _("EditMenuFeature.CUT_LABEL");
	public static final String COPY_LABEL = _("EditMenuFeature.COPY_LABEL");
	public static final String PASTE_LABEL = _("EditMenuFeature.PASTE_LABEL");
	public static final String SELECT_ALL_LABEL = _("EditMenuFeature.SELECT_ALL_LABEL");
	public static final String FONT_BASE_URI = _("font.base.uri");
}
