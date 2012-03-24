/*

 ImageFactory.java
 
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
 
 Created on 2007/01/28

 */
package net.sqs2.image;
import java.awt.image.BufferedImage;
import java.io.File;

import junit.framework.TestCase;

public class ImageFactoryTest extends TestCase {
	public void testCreateImage(){
		try{
			BufferedImage a001 = ImageFactory.createImage(new File("../sqs-reader/src/test/resources/test1/a001.tif"), 0);
			BufferedImage a002 = ImageFactory.createImage(new File("../sqs-reader/src/test/resources/test1/a002.tif"), 0);
			assertEquals(1232, a001.getWidth());
			assertEquals(1753, a001.getHeight());
			assertEquals(1232, a002.getWidth());
			assertEquals(1753, a002.getHeight());
		}catch(Exception ex){
			fail(ex.getLocalizedMessage());
		}
	}
}
