/*
 * 

 GroupThreadFactory.java

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
package net.sqs2.lang;

import java.util.concurrent.ThreadFactory;

public class GroupThreadFactory implements ThreadFactory {
	ThreadGroup threadGroup = null;
	int priority;
	int number = 0;
	boolean daemon;

	public GroupThreadFactory(String name) {
		this(name, Thread.NORM_PRIORITY, false);
	}

	public GroupThreadFactory(String name, int priority, boolean daemon) {
		this.threadGroup = new ThreadGroup(name);
		this.threadGroup.setDaemon(daemon);
		this.priority = priority;
		this.daemon = daemon;
	}

	public Thread newThread(Runnable runnable) {
		Thread t = new Thread(this.threadGroup, runnable);
		t.setName("ThreadGroup:" + this.threadGroup.getName() + "[" + (this.number++) + "]");
		t.setPriority(this.priority);
		t.setDaemon(this.daemon);
		return t;
	}

	public void setPriority(int priority) {
		this.priority = priority;
		Thread[] list = new Thread[this.threadGroup.activeCount()];
		this.threadGroup.enumerate(list);
		for (Thread t : list) {
			t.setPriority(priority);
		}
	}

	public void destroy() {
		try {
			this.threadGroup.destroy();
		} catch (IllegalThreadStateException ex) {
			// ex.printStackTrace();
		}
	}
}
