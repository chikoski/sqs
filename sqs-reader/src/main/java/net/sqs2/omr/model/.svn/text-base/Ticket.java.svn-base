package net.sqs2.omr.model;

import java.io.Serializable;
import java.util.concurrent.TimeUnit;

public class Ticket implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static final int LEASE_TIMEOUT_IN_SEC = 14;
	protected long expiredTime = 0L;
	protected long sessionID = 0L;

	public Ticket() {
	}

	public void setLeased() {
		this.expiredTime = System.currentTimeMillis() + LEASE_TIMEOUT_IN_SEC * 1000;
	}

	public long getDelay(TimeUnit unit) {
		return unit.convert(this.expiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
	}

}