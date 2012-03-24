package net.sqs2.omr.app;

import java.io.IOException;
import java.util.logging.Logger;

import net.sqs2.net.MulticastNetworkConnection;
import net.sqs2.omr.logic.PageTaskExecutorCoreImpl;
import net.sqs2.omr.task.broker.RemoteTaskExecutorManager;
import net.sqs2.omr.task.broker.SessionDiscoveryService;
import net.sqs2.omr.task.broker.TaskExecutorLogic;
import net.sqs2.omr.task.broker.TaskExecutorPeer;

public class NetworkPeer {
	
	private int rmiPort;
	private String rmiServiceName;
	
	private SessionDiscoveryService discoveryService;
	private RemoteTaskExecutorManager remoteTaskExecutorManager;
	private MulticastNetworkConnection multicastNetworkConnection;
	private TaskExecutorPeer taskExecutorPeer;

    public NetworkPeer(int rmiPort, String serviceName) {
    	
        this.taskExecutorPeer = new TaskExecutorPeer((TaskExecutorLogic) new PageTaskExecutorCoreImpl());//Class.forName(getSessionExecutorCoreClassName()).newInstance());

    	this.rmiPort = rmiPort;
    	this.rmiServiceName = serviceName;
    	
        try {
            this.multicastNetworkConnection =
                    new MulticastNetworkConnection(
                            getSessionServiceMulticastAddress(),
                            getSessionServiceMulticastPort());
            this.remoteTaskExecutorManager = new RemoteTaskExecutorManager(taskExecutorPeer, MarkReaderConstants.PAGETASK_EXECUTORS_MAX_EXECUTORS);
            this.discoveryService = new SessionDiscoveryService(
                    this.multicastNetworkConnection,
                    MarkReaderConstants.DISCOVERY_SERVICE_THREAD_PRIORITY,
                    MarkReaderConstants.SESSION_SOURCE_ADVERTISE_DATAGRAM_PACKET_BUFFER_LENGTH,
                    this.remoteTaskExecutorManager);
            Logger.getLogger("engine").warning("multicast session has established.");
        } catch (IOException ignore) {
            Logger.getLogger("engine").warning("multicast session has NOT established.");
        }
    }
    
    public TaskExecutorPeer getTaskExecutorPeer(){
    	return this.taskExecutorPeer;
    }

	public static String getBaseURI(){
		return "class://net.sqs2.omr.app.NetworkPeer/";
	}

    public String getSessionServiceMulticastAddress() {
        return MarkReaderConstants.MULTICAST_ADDRESS;
    }

    public int getSessionServiceMulticastPort() {
        return MarkReaderConstants.MULTICAST_PORT;
    }

	public int getRMIPort() {
		return this.rmiPort;
	}
	
	public String getRMIServiceName() {
		return this.rmiServiceName;
	}
	
	public MulticastNetworkConnection getMulticastNetworkConnection(){
		return this.multicastNetworkConnection;
	}
	
    public void shutdown(){
		if (this.discoveryService != null) {
			this.discoveryService.shutdown();
			if(this.remoteTaskExecutorManager != null){
				this.remoteTaskExecutorManager.shutdown();
			}
		}
    }

}
