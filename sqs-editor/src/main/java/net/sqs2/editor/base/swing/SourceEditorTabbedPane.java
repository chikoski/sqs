/*

 SourceEditorTabbedPane.java
 
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
 
 Created on 2004/07/31

 */
package net.sqs2.editor.base.swing;

import java.io.File;

import javax.swing.Icon;
import javax.swing.JTabbedPane;

import net.sqs2.exsed.source.DOMTreeSource;
import net.sqs2.exsed.source.Source;
import net.sqs2.swing.FileDropTargetDecorator;

/**
 * @author hiroya
 * 
 */
public abstract class SourceEditorTabbedPane extends JTabbedPane {
	private static final long serialVersionUID = 1L;
	SourceEditorMediator mediator;

	public SourceEditorTabbedPane(final SourceEditorMediator mediator) {
		this.mediator = mediator;
		this.setToolTipText(Messages.PROMPT_DND_TO_FILE_OPEN);
		new FileDropTargetDecorator(this) {
			public void drop(File file) {
				if (file.exists()) {
					mediator.menuBarMediator.open(file);
				}
			}
			@Override
			// XXX ad-hoc
	        public void drop(File[] file){
				if(file.length > 0){
					this.drop(file[0]);
				}
			}

		};
	}

	public void removeTabAt(int index) {
		super.removeTabAt(index);
	}

	public DOMTreeSource getCurrentEditingSource() {
		return (DOMTreeSource) mediator.getSourceManager().get(this.getSelectedIndex());
	}

	public void updateCurrentTitle() {
		Source source = getCurrentEditingSource();
		if (0 <= this.getSelectedIndex()) {
			setTitleAt(this.getSelectedIndex(), source.getTitle());
		}
		if (source.getFile() != null) {
			setToolTipTextAt(getComponentCount() - 1, source.getFile().getAbsolutePath());
		} else {
			setToolTipTextAt(getComponentCount() - 1, source.getURL().toString());
		}
		mediator.menuBarMediator.updateMenu();
	}

	public abstract Icon createTabIcon(DOMTreeSource source);

	/**
	 * @param source
	 */
	public void addComponent(DOMTreeSource source) {
		SourceEditorSplitPane body = createEditorSplitPane(source);
		add(source.getTitle(), body);
		setIconAt(getComponentCount() - 1, createTabIcon(source));
		setSelectedComponent(body);
		updateCurrentTitle();
	}

	/**
	 * @param source
	 */
	public void setComponent(int index, DOMTreeSource source) {
		SourceEditorSplitPane body = createEditorSplitPane(source);
		setComponentAt(index, body);
		setTitleAt(index, source.getTitle());
		setIconAt(index, createTabIcon(source));
		if (source.getFile() != null) {
			setToolTipTextAt(index, source.getFile().getAbsolutePath());
		}
		setSelectedComponent(body);
	}

	/**
	 * @param source
	 * @return
	 */
	private SourceEditorSplitPane createEditorSplitPane(DOMTreeSource source) {
		return createSourceEditorSplitPane(mediator, source, getWidth());
	}

	public SourceEditorSplitPane getCurrentSplitPane() {
		return (SourceEditorSplitPane) this.getComponentAt(this.getSelectedIndex());
	}

	public abstract SourceEditorSplitPane createSourceEditorSplitPane(SourceEditorMediator mediator, DOMTreeSource source, int width);

}
