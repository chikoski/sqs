/*

 SQSHttpdManager.java

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

 Created on 2004/10/19

 */
package net.sqs2.omr.httpd;

import java.net.ProxySelector;

import net.sqs2.omr.app.App;

public class SQSHttpdManager {

	private static MarkReaderHttpd exigridHttpd = null;


	public static MarkReaderHttpd getEXIgridHttpd() throws Exception{
		synchronized(SQSHttpdManager.class){
			if(SQSHttpdManager.exigridHttpd == null) {
				int port = App.HTTP_PORT;
				SQSHttpdManager.exigridHttpd = new MarkReaderHttpd(port);
				try{
					SQSHttpdManager.exigridHttpd.start();
				}catch(java.net.BindException ignore){
				}
				while(! SQSHttpdManager.exigridHttpd.isStarted()){
					Thread.yield();
				}
				ProxySelector.setDefault(new SQSProxySelector(ProxySelector.getDefault()));
			}
		}
		return SQSHttpdManager.exigridHttpd;
	}
	
	public static void initHttpds(){
		new Thread(){
			public void start(){
				try{
					SQSHttpdManager.getEXIgridHttpd();
				}catch(Exception ex){
					ex.printStackTrace();
				}
			}
		}.start();
	}
}
