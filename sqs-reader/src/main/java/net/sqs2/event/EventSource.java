package net.sqs2.event;

import java.util.Vector;

public class EventSource <Event>{
	protected Vector<EventListener<Event>> listeners = new Vector<EventListener<Event>>();
	
	public void addListener(EventListener<Event> l){
		listeners.add(l);
	}
	
	public void removeListener(EventListener<Event> l){
		listeners.remove(l);
	}
	
	public void fireEvent(Event e){
		for(int i = 0; i < listeners.size(); i++){
			listeners.get(i).eventHappened(e);
		}
	}
}
