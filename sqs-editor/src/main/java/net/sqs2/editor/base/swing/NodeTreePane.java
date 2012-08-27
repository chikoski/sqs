/*

 NodeTreePane.java
 
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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JViewport;
import javax.swing.SwingUtilities;
import javax.swing.tree.TreePath;
// XXX
import javax.swing.JScrollPane;

import net.sqs2.editor.base.modules.AbstractNodeEditor;
import net.sqs2.editor.base.modules.ContainerEditor;
import net.sqs2.exsed.source.DOMTreeSource;

import org.w3c.dom.Node;

/**
 * @author hiroya
 * 
 */

public abstract class NodeTreePane extends AbstractNodeTreePane {
	private static final long serialVersionUID = 1L;
	SourceEditorMediator mediator;
	JComponent editorPane;
	Point mousePressCursorPoint = null;
	Point prevCursorPoint = null;
	Point currentCursorPoint = null;
	
	TreePath currentPopUpMenuNodePath = null;

	public NodeTreePane(SourceEditorMediator mediator, DOMTreeSource source, final JComponent editorPane) {
		super(mediator, source);
		this.mediator = mediator;
		this.editorPane = editorPane;
		setupEditorPane(null);
		this.addMouseListener(createMouseListener(editorPane));
		this.addMouseMotionListener(createMouseMotionListener());
	}
	
	public boolean isReadOnly(){
		return mediator.getSourceEditorTabbedPane().getCurrentEditingSource().isReadOnly();
	}

	private MouseMotionListener createMouseMotionListener() {
		return new MouseMotionListener() {
			public void mouseDragged(MouseEvent ev) {
				if (isSelectionModeMouseDragEvent(ev)) {
					selectNodeGroup(ev);
					dragViewportArea(ev);
				}
			}

			private void selectNodeGroup(MouseEvent ev) {
				currentCursorPoint = ev.getPoint();
				if (mousePressCursorPoint != null) {
					int mousePressRow = getClosestRowForLocation(mousePressCursorPoint.x,
							mousePressCursorPoint.y);
					int currentRow = getClosestRowForLocation(ev.getPoint().x, ev.getPoint().y);
					if (isCtrlPressed(ev)) {
						if (prevCursorPoint != null) {
							selectionModel.xORSelection(mousePressRow, getClosestRowForLocation(
									prevCursorPoint.x, prevCursorPoint.y));
						}
						selectionModel.xORSelection(mousePressRow, currentRow);
					} else {
						selectionModel.clearSelection();
						selectionModel.selectNodeGroup(mousePressRow, currentRow);
					}
				}
				prevCursorPoint = currentCursorPoint;
				mediator.getMenuBarMediator().updateEditMenu();
			}

			private void dragViewportArea(MouseEvent ev) {
				JViewport viewport = (JViewport) getParent();
				Point viewPosition = viewport.getViewPosition();
				Point current = viewport.getViewPosition();
				if (viewPosition.x + viewport.getWidth() < ev.getPoint().x
						&& viewport.getWidth() < getWidth() - viewPosition.x) {
					current.translate(ev.getPoint().x - viewPosition.x - viewport.getWidth(), 0);
				} else if (ev.getPoint().x < viewPosition.x && 0 < viewPosition.x) {
					current.translate(ev.getPoint().x - viewPosition.x, 0);
				}
				if (viewPosition.y + viewport.getHeight() < ev.getPoint().y
						&& viewport.getHeight() < getHeight() - viewPosition.y) {
					current.translate(0, ev.getPoint().y - viewPosition.y - viewport.getHeight());
				} else if (ev.getPoint().y < viewPosition.y && 0 < viewPosition.y) {
					current.translate(0, ev.getPoint().y - viewPosition.y);
				}
				viewport.setViewPosition(current);
			}

			public void mouseMoved(MouseEvent ev) {
			}
		};
	}

	private boolean isOutsideNodeRenderingAreaEvent(MouseEvent ev) {
		return getPathForLocation(ev.getPoint().x, ev.getPoint().y) == null;
	}

	private MouseAdapter createMouseListener(final JComponent editorPane) {
		return new MouseAdapter() {

			public void mouseClicked(MouseEvent ev) {
				if (isSelectionModeButtonPressEvent(ev) && isCtrlPressed(ev)) {
					int currentRow = getClosestRowForLocation(ev.getPoint().x, ev.getPoint().y);
					selectionModel.xORSelection(currentRow, currentRow);
					setupEditorPane(selectionModel.getSelectionPaths());
				}
				mediator.getMenuBarMediator().updateEditMenu();
			}

			public void mousePressed(MouseEvent ev) {
				if (isSelectionModeButtonPressEvent(ev)) {
					if (isCtrlPressed(ev)) {
						ev.consume();
					} else if (!isOutsideNodeRenderingAreaEvent(ev)) {
						selectionModel.clearSelection();
						editorPane.removeAll();
					}
					mousePressCursorPoint = ev.getPoint();
					prevCursorPoint = null;
					currentCursorPoint = ev.getPoint();
				} else {
					if (0 < selectionModel.getSelectionCount()) {
						currentPopUpMenuNodePath = getClosestPathForLocation(ev.getPoint().x, ev.getPoint().y);
						popup = createPopupMenu(false);
						popup.show(self, ev.getX(), ev.getY());
					}
				}
			}

			public void mouseReleased(MouseEvent ev) {
				if (isSelectionModeButtonPressEvent(ev)) {
					if (mousePressCursorPoint.equals(ev.getPoint()) && isOutsideNodeRenderingAreaEvent(ev)) {
						mousePressCursorPoint = null;
						prevCursorPoint = null;
						currentCursorPoint = null;
						return;
					}
					setupEditorPane(selectionModel.getSelectionPaths());
					mousePressCursorPoint = null;
					prevCursorPoint = null;
					currentCursorPoint = null;
				}
				mediator.getMenuBarMediator().updateEditMenu();
				updateEditor();
			}
			/*
			 * private void selectNodeGroup(MouseEvent ev) { int currentRow =
			 * getClosestRowForLocation(ev.getPoint().x, ev.getPoint().y); if( !
			 * isCtrlPressed(ev)){ selectionModel.selectNodeGroup(currentRow,
			 * NodeTreeSelectionModel.NORMAL_MODE); } }
			 */
		};
	}

	private boolean isSelectionModeButtonPressEvent(MouseEvent ev) {
		return isButton1Pressed(ev);
	}

	private boolean isSelectionModeMouseDragEvent(MouseEvent ev) {
		return isButton1Dragged(ev);
	}

	private boolean isButton1Dragged(MouseEvent ev) {
		return (ev.getModifiersEx() & MouseEvent.BUTTON1_DOWN_MASK) == MouseEvent.BUTTON1_DOWN_MASK;
	}

	private boolean isButton1Pressed(MouseEvent ev) {
		return (ev.getButton() == MouseEvent.BUTTON1);
	}

	private boolean isCtrlPressed(MouseEvent ev) {
		return (ev.getModifiersEx() & MouseEvent.CTRL_DOWN_MASK) == MouseEvent.CTRL_DOWN_MASK;
	}

	/*
	 * public JPopupMenu createPopupMenu(int rowIndex, boolean isClickedNode){
	 * return new TreeNodePopupMenu(this, rowIndex, isClickedNode); }
	 */
	public JPopupMenu createPopupMenu(boolean isClickedNode) {
		return new TreeNodePopupMenu(this.mediator, isClickedNode);
	}

	public void paint(Graphics g) {
		super.paint(g);
		if (currentCursorPoint != null && mousePressCursorPoint != null) {
			drawDraggingRectangle(g);
		}
		try {
			drawGuideLineToEditor(g);
		} catch (IndexOutOfBoundsException ignore) {
		}
	}

	private void drawDraggingRectangle(Graphics g) {
		g.setColor(Color.darkGray);
		g.drawPolyline(new int[] { mousePressCursorPoint.x, currentCursorPoint.x, currentCursorPoint.x,
				mousePressCursorPoint.x, mousePressCursorPoint.x },
				new int[] { mousePressCursorPoint.y, mousePressCursorPoint.y, currentCursorPoint.y,
						currentCursorPoint.y, mousePressCursorPoint.y }, 5);
	}

	static final Color[] GUIDE_COLOR = new Color[] { new Color(120, 250, 120, 100),
			new Color(180, 180, 180, 200), new Color(120, 250, 120, 60) };

	private void drawGuideLineToEditor(Graphics g) {
		int editorYOffset = ((JViewport) editorPane.getParent()).getViewPosition().y;
		int prevEditorBottom = 0;
		int[] selectedRows = null;
		if (0 < this.getSelectionCount()) {
			selectedRows = (int[]) this.getSelectionRows().clone();
			Arrays.sort(selectedRows);
			drawGuideLinesToEditor(g, editorYOffset, prevEditorBottom, selectedRows);
		}
	}

	private void drawGuideLinesToEditor(Graphics g, int editorYOffset, int prevEditorBottom, int[] selectedRows) {
		((NodeTreeSelectionModel) getSelectionModel()).removeHierachicalSelected();
		int len = this.getSelectionCount();
		for (int i = 0; i < len; i++) {
			int selectedRowTop = selectedRows[i];
			TreePath selectedPath = this.getPathForRow(selectedRowTop);
			int selectedRowBottom = selectedRowTop;

			for (int j = selectedRowTop + 1; j < this.getRowCount(); j++) {
				if (selectedPath.isDescendant(this.getPathForRow(j))) {
					selectedRowBottom = j;
					continue;
				}
				break;
			}
			int editorBottom = ((Integer) editorHeightList.get(i)).intValue();
			Rectangle selectedRowRectTop = this.getRowBounds(selectedRowTop);
			Rectangle selectedRowRectBottom = this.getRowBounds(selectedRowBottom);

			drawGuideLinesToEditor(g, editorYOffset, prevEditorBottom, editorBottom, selectedRowRectTop,
					selectedRowRectBottom);
			prevEditorBottom = editorBottom;
		}
	}

	private void drawGuideLinesToEditor(Graphics g, int editorYOffset, int prevEditorBottom, int editorBottom, Rectangle selectedRowRectTop, Rectangle selectedRowRectBottom) {
		JViewport viewport = (JViewport) this.getParent();
		g.setColor(GUIDE_COLOR[0]);
		g.drawLine(selectedRowRectTop.x, selectedRowRectTop.y, viewport.getWidth()
				+ viewport.getViewPosition().x, prevEditorBottom - editorYOffset
				+ viewport.getViewPosition().y);
		g.drawLine(selectedRowRectBottom.x, selectedRowRectBottom.y + selectedRowRectBottom.height, viewport
				.getWidth()
				+ viewport.getViewPosition().x, editorBottom - editorYOffset + viewport.getViewPosition().y);

		g.setColor(GUIDE_COLOR[2]);
		int[] xPoints = { selectedRowRectTop.x, viewport.getWidth() + viewport.getViewPosition().x,
				viewport.getWidth() + viewport.getViewPosition().x, selectedRowRectBottom.x,
				selectedRowRectTop.x };

		int[] yPoints = { selectedRowRectTop.y,
				prevEditorBottom - editorYOffset + viewport.getViewPosition().y,
				editorBottom - editorYOffset + viewport.getViewPosition().y,
				selectedRowRectBottom.y + selectedRowRectBottom.height, selectedRowRectTop.y };
		g.fillPolygon(xPoints, yPoints, 5);
		/*
		 * g.setColor(GUIDE_COLOR[1]); g.drawLine(selectedRowRectTop.x +
		 * selectedRowRectTop.width, selectedRowRectTop.y,
		 * viewport.getWidth()+viewport.getViewPosition().x, prevEditorBottom -
		 * editorYOffset + viewport.getViewPosition().y);
		 * g.drawLine(selectedRowRectBottom.x + selectedRowRectBottom.width ,
		 * selectedRowRectBottom.y+selectedRowRectBottom.height,
		 * viewport.getWidth()+viewport.getViewPosition().x, editorBottom -
		 * editorYOffset + viewport.getViewPosition().y);
		 */
	}

	ArrayList<Integer> editorHeightList = new ArrayList<Integer>();

	public synchronized void setupEditorPane(final TreePath[] paths) {
		if (paths != null) {
			for (int i = 0; i < paths.length; i++) {
				getSelectionModel().addSelectionPath(paths[i]);
			}
			((NodeTreeSelectionModel) getSelectionModel()).removeHierachicalSelected();
		}
		setupEditorPaneCore();
		updateEditor();

	}

	public void setupEditorPaneCore() {
		int[] rows = selectionModel.getSelectionRows();
		editorPane.removeAll();
		editorPane.setLayout(new BoxLayout(editorPane, BoxLayout.Y_AXIS));
		if (rows == null) {
			setupBackgroundPanel();
			return;
		}
		int paneHeight = getEditorExtentHeight();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
		Arrays.sort(rows);
		int totalHeight = 0;
		AbstractNodeEditor pane = null;

		for (int i = 0; i < rows.length; i++) {
			TreePath path = getPathForRow(rows[i]);
			// TreePath path = new
			// TreePath(((DOMTreeWalkerTreeModel)model).getNodes(rows[i]));
			pane = createEditorPane(path);
			if (pane != null) {
				editorPane.add(pane);
				totalHeight += pane.getPreferredHeight();
			}
		}
		if (totalHeight < paneHeight) {
			updatePreferredSize(rows, paneHeight, totalHeight, pane);
		} else {
			editorPane.setPreferredSize(new Dimension(200, totalHeight));
			JViewport viewport = (JViewport) editorPane.getParent();
			if (mousePressCursorPoint == null || currentCursorPoint == null) {
				this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
				return;
			}
			updateViewPosition(totalHeight, viewport);
		}

		updateEditorHeightList();
		this.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
	}

	private void updateEditor() {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				updateUI();
				editorPane.updateUI();
			}

		});
	}

	private int getEditorExtentHeight() {
		// XXXX
		JScrollPane pane = mediator.getCurrentEditorScrollPane();
//		SourceEditorSplitPane pane = mediator.getCurrentEditorScrollPane();
		if(pane == null){
			return 0;
		}else{
			return pane.getViewport().getExtentSize().height;
		}
	}

	private void updatePreferredSize(int[] rows, int paneHeight, int totalHeight, AbstractNodeEditor pane) {
		if (rows.length == 1 && pane != null) {
			addEditorPane(paneHeight, totalHeight, pane);
			pane.setPreferredSize(new Dimension(200, paneHeight));
		} else {
			editorPane.setPreferredSize(new Dimension(200, paneHeight));
			for (int i = 0; i < editorPane.getComponentCount(); i++) {
				((JComponent) editorPane.getComponent(i)).setPreferredSize(new Dimension(200, paneHeight
						/ editorPane.getComponentCount()));
			}
		}
	}

	private void updateViewPosition(int totalHeight, JViewport viewport) {
		if (mousePressCursorPoint.y < currentCursorPoint.y) {
			// Point viewPosition = viewport.getViewPosition();
			viewport.setViewPosition(new Point(0, totalHeight - viewport.getExtentSize().height));
		} else {
			viewport.setViewPosition(new Point(0, 0));
		}
	}

	private void updateEditorHeightList() {
		editorHeightList.clear();
		int t = 0;
		for (int i = 0; i < editorPane.getComponentCount(); i++) {
			int h = (int) editorPane.getComponent(i).getPreferredSize().getHeight();
			t += h;
			editorHeightList.add(Integer.valueOf(t));
		}
	}

	private void addEditorPane(int paneHeight, int totalHeight, AbstractNodeEditor pane) {
		// String localName = pane.getNode().getLocalName();
		if (pane instanceof ContainerEditor) {// "head".equals(localName) ||
												// "column-array".equals(localName)
			if (true) {
				editorPane.add(Box.createVerticalStrut(paneHeight - totalHeight));
				pane.setSize(new Dimension(200, paneHeight));
			} else {
				editorPane.removeAll();
				editorPane.setLayout(new BorderLayout());
				editorPane.add(pane);
				editorPane.setSize(new Dimension(200, paneHeight));
			}
		}
		editorPane.setPreferredSize(new Dimension(200, paneHeight));
	}

	private void setupBackgroundPanel() {
		editorPane.setSize(new Dimension(200, 0));
		editorPane.setPreferredSize(new Dimension(200, 0));
		editorPane.setLayout(new BorderLayout());
		JPanel panel = mediator.getBackgroundPanel();
		editorPane.add(panel, BorderLayout.SOUTH);
	}

	public AbstractNodeEditor createEditorPane(TreePath path) {
		if (path == null) {
			return null;
		}
		return editorResourceFactory.create(mediator, source, (Node) path.getLastPathComponent());
	}

	public JComponent getEditorPane() {
		return editorPane;
	}

}
