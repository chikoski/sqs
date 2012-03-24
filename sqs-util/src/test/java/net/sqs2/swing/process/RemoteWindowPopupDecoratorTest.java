/*
 * 

 RemoteWindowAccessorTest.java

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

 package net.sqs2.swing.process;

import java.awt.Color;

import java.awt.Graphics;

import javax.swing.JFrame;

import org.junit.Test;

import static org.junit.Assert.*;

public class RemoteWindowPopupDecoratorTest{
	
	@Test
	public void testActivate(){
		
		final JFrame frame1 = new JFrame("TEST1"){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				setForeground(Color.green);
				g.fillRect(0, 0, getWidth(), getHeight());
			}			
		};
		boolean isActivated1 = RemoteWindowPopupDecorator.activate(frame1, 1099);
		assertEquals(true, isActivated1);
		if(isActivated1){
			frame1.setSize(100,100);
			frame1.setVisible(true);
		}
		
		try{
			Thread.sleep(100);
		}catch(InterruptedException ignore){}

		final JFrame frame2 = new JFrame("TEST2"){
			private static final long serialVersionUID = 1L;
			public void paint(Graphics g){
				setForeground(Color.red);
				g.fillRect(0, 0, getWidth(), getHeight());
			}
		};
		boolean isActivated2 = RemoteWindowPopupDecorator.activate(frame2, 1099);
		assertEquals(false, isActivated2);
		if(isActivated2){
			frame2.setSize(100,100);
			frame2.setVisible(true);
		}
	}
}
