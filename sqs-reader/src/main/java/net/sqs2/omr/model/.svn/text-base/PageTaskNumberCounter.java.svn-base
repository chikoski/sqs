/**
 * 
 */
package net.sqs2.omr.model;

public class PageTaskNumberCounter{
	int numTotal;
	int numPrepared;
	int numReused;
	int numLocalLeased;
	int numRemoteLeased;
	int numSubmitted;
	public PageTaskNumberCounter(int numTotal, int numPrepared, int numReused, int numLocalLeased, int numRemoteLeased, int numSubmitted){
		this.numTotal = numTotal;
		this.numPrepared = numPrepared;
		this.numReused = numReused;
		this.numLocalLeased = numLocalLeased;
		this.numRemoteLeased = numRemoteLeased;
		this.numSubmitted = numSubmitted;
	}
	public String toString(){
		return numTotal+"="+this.numReused+"+"+this.getNumExternalized()+"+"+this.numSubmitted+"+"+this.numRemoteLeased+"+"+this.numLocalLeased+"+"+this.numPrepared;
	}
	public int getNumTotal(){
		return numTotal;
	}
	public int getNumPrepared() {
		return numPrepared;
	}
	public int getNumReused() {
		return numReused;
	}
	public int getNumLocalLeased() {
		return numLocalLeased;
	}
	public int getNumRemoteLeased() {
		return numRemoteLeased;
	}
	public int getNumSubmitted() {
		return numSubmitted;
	}
	public int getNumExternalized() {
		return numTotal - numSubmitted - numRemoteLeased - numLocalLeased 
		- numReused - numPrepared;
	}
}