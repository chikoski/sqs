/*

 EditorConstants.java
 
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
 
 Created on 2004/08/16

 */
package net.sqs2.editor.base.modules;

import java.awt.Color;

import javax.swing.Box;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.border.EtchedBorder;
import javax.swing.border.LineBorder;
import javax.swing.border.SoftBevelBorder;

/**
 * @author hiroya
 * 
 */
public class EditorUtil {
	public static final Border EMPTY_BORDER = new EmptyBorder(1, 1, 1, 1);
	public static final Border NORMAL_BORDER = new LineBorder(Color.black);
	public static final Border ETCHED_BORDER = new EtchedBorder();
	public static final Border RAISED_BORDER = new SoftBevelBorder(SoftBevelBorder.RAISED);
	public static final Border LOWERED_BORDER = new SoftBevelBorder(SoftBevelBorder.LOWERED);

	public static Box createSpinnerForm(String label, JSpinner spinner) {
		Box panel = Box.createHorizontalBox();
		panel.add(Box.createHorizontalGlue());
		panel.add(new JLabel(label));
		panel.add(Box.createHorizontalGlue());
		panel.add(spinner);
		return panel;
	}
}
