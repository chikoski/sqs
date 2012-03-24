/*
 * 

 GenericHttpdTest.java

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
package net.sqs2.httpd;

import junit.framework.TestCase;

public class GenericHttpdTest extends TestCase {
	public static void main(String[] args) throws Exception {
	}

	public void testHttpd() throws Exception{
		GenericHttpd httpd = new GenericHttpd(2000);
		httpd.start();
		assertTrue(httpd.isStarted());
		httpd.stop();
	}
}
