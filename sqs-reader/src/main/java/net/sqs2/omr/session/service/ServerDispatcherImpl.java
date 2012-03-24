package net.sqs2.omr.session.service;



public abstract class ServerDispatcherImpl implements ServerDispatcher{
	
	private LocalSessionSourceServer localServer;
	private RemoteSessionSourceServer remoteServer;
	private long key;

	private boolean hasInitialized = false;

	public ServerDispatcherImpl(LocalSessionSourceServer localServer, RemoteSessionSourceServer remoteServer,
			long key) {
		this.localServer = localServer;
		this.remoteServer = remoteServer;
		this.key = key;
	}

	@Override
	public void setInitialized() {
		this.hasInitialized = true;
	}

	@Override
	public boolean hasInitialized() {
		return this.hasInitialized;
	}

	@Override
	public long getKey() {
		return this.key;
	}

	@Override
	public boolean isRemote() {
		return (this.remoteServer != null);
	}

	@Override
	public LocalSessionSourceServer getLocalServer() {
		return this.localServer;
	}

	@Override
	public LocalSessionSourceServer getServer() {
		if (this.localServer != null) {
			return this.localServer;
		} else if (this.remoteServer != null) {
			return this.remoteServer;
		} else {
			throw new RuntimeException("localServer == null and remoteServer == null");
		}
	}

	@Override
	public void close() {
		if (this.remoteServer != null) {
			this.remoteServer = null;
		}
		if (this.localServer != null) {
			this.localServer = null;
		}
	}
}
