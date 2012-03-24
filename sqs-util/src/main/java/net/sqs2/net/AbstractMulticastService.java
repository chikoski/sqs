/*

 AbstractMulticastService.java

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

 Created on 2007/01/11

 */

package net.sqs2.net;

/*

 AbstractMuticastService.java

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

 Created on 2007/01/11

 */
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;

import net.sqs2.lang.GroupThreadFactory;

public class AbstractMulticastService {
	protected MulticastNetworkConnection connection;
	protected int threadPriority;

	ScheduledExecutorService executorService;
	GroupThreadFactory groupThreadFactory;
	protected Future<?> future;

	public AbstractMulticastService(MulticastNetworkConnection con, int threadPriority) {
		this.connection = con;
		this.threadPriority = threadPriority;
	}
	
	public void start(String threadName) {
		this.groupThreadFactory = new GroupThreadFactory(threadName,
				this.threadPriority, true);
		this.executorService = Executors.newScheduledThreadPool(1, this.groupThreadFactory);
	}
	
	public void setFuture(Future<?> future) {
		this.future = future;
	}

	public void stop() {
		if (this.future != null) {
			this.future.cancel(true);
		}
		if (this.executorService != null) {
			this.executorService.shutdown();
		}
		if (this.groupThreadFactory != null) {
			this.groupThreadFactory.destroy();
		}

	}

	public void shutdown() {
		stop();
		try {
			this.connection.close();
		} catch (IOException ignore) {
			// ignore.printStackTrace();
		}
	}
}
