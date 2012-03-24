/*
 * 
   TranslatorException.java

   Copyright 2004-2007 KUBO Hiroya (hiroya@cuc.ac.jp).

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
package net.sqs2.translator;

public class TranslatorException extends Exception {
	private static final long serialVersionUID = 0;
	private String[] messages;

	public TranslatorException() {
		super();
	}

	public TranslatorException(String[] messages) {
		super(messages[0]);
		this.messages = messages;
	}

	public TranslatorException(String message) {
		super(message);
		this.messages = new String[] { message };
	}

	public TranslatorException(Exception ex) {
		super(ex);
	}

	public String[] getMessages() {
		return this.messages;
	}
}
