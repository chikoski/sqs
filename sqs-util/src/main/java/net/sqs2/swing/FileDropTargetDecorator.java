/**
 *  FileDropTargetDecorator.java

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

 Created on 2007/02/12
 Author hiroya
 */
package net.sqs2.swing;

import java.awt.Component;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.StringTokenizer;

import edu.emory.mathcs.backport.java.util.Collections;

public abstract class FileDropTargetDecorator implements DropTargetListener {

	public FileDropTargetDecorator(Component component) {
		new DropTarget(component, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
	}

	@Override
	public void drop(DropTargetDropEvent ev) {
		try {
			if ((ev.getDropAction() & DnDConstants.ACTION_COPY_OR_MOVE) != 0) {
				ev.acceptDrop(DnDConstants.ACTION_COPY_OR_MOVE);
				Transferable transferable = ev.getTransferable();
				drop(transferable);
				ev.dropComplete(true);
			} else {
				ev.rejectDrop();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void drop(Transferable transferable) throws IOException {
		List<File> files = new ArrayList<File>();
		try {
			List<?> list = (List<?>) transferable.getTransferData(DataFlavor.javaFileListFlavor);
			for (Object o : list) {
				File file = (File) o;
				//drop(file);
				files.add(file);
			}
			drop(files);
			return;
		} catch (UnsupportedFlavorException ex) {
		}
		try {
			String values = (String) transferable.getTransferData(DataFlavor.stringFlavor);
			StringTokenizer st = new StringTokenizer(values, "\n");
			while (st.hasMoreTokens()) {
				String value = st.nextToken();
				File file = new File(new URI(value.trim()));
				//drop(file);
				files.add(file);
			}
			drop(files);
		} catch (URISyntaxException ex) {
		} catch (UnsupportedFlavorException ex) {
		}
	}

	@Override
	public void dragEnter(DropTargetDragEvent e) {
		e.acceptDrag(DnDConstants.ACTION_COPY);
	}

	@Override
	public void dragExit(DropTargetEvent e) {
	}

	@Override
	public void dragOver(DropTargetDragEvent e) {
	}

	@Override
	public void dropActionChanged(DropTargetDragEvent e) {
	}

	public void drop(List<File> files){
		Collections.sort(files, new Comparator<File>(){
			@Override
			public int compare(File o1, File o2) {
				File file1 = (File)o1; 
				File file2 = (File)o2; 
				return file1.getName().compareTo(file2.getName());
			}
		});
		File[] fileArray = new File[files.size()];
		drop(files.toArray(fileArray));
	}
	
	public abstract void drop(File[] file);
}
